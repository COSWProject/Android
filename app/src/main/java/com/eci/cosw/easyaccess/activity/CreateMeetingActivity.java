package com.eci.cosw.easyaccess.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.model.Access;
import com.eci.cosw.easyaccess.model.User;
import com.eci.cosw.easyaccess.service.AccessService;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.eci.cosw.easyaccess.util.SharedPreference;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class CreateMeetingActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreference sharedPreference;
    private RetrofitHttp retrofitHttp;

    private String file;

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(1);

    private UserService userService;
    private AccessService accessService;

    private static final String CERO = "0";
    private static final String DOS_PUNTOS = ":";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    //Widgets
    EditText etFecha, etHora, owner;
    ImageButton ibObtenerFecha, ibObtenerHora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        file = getString(R.string.preference_file_key);

        sharedPreference = new SharedPreference(this, file);

        retrofitHttp = new RetrofitHttp(sharedPreference.getValue("TOKEN_KEY"));

        accessService = retrofitHttp.getRetrofit().create(AccessService.class);
        userService = retrofitHttp.getRetrofit().create(UserService.class);

        etFecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);

        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        ibObtenerHora = (ImageButton) findViewById(R.id.ib_obtener_hora);

        ibObtenerFecha.setOnClickListener(this);
        ibObtenerHora.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainCompanyActivity.class);
        startActivity(intent);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha:
                obtenerFecha();
                break;
            case R.id.ib_obtener_hora:
                obtenerHora();
                break;
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                final int mesActual = month + 1;

                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);

                etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
        },anio, mes, dia);

        recogerFecha.show();

    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                String horaFormateada =  (hourOfDay < 9)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                String minutoFormateado = (minute < 9)? String.valueOf(CERO + minute):String.valueOf(minute);

                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }

                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
            }

        }, hora, minuto, false);

        recogerHora.show();
    }

    public void findViews() {
        owner = findViewById(R.id.owner);
    }

    public void createMeeting(final View view) {
        view.setEnabled(false);

        findViews();

        final String date = etFecha.getText().toString();
        final String time = etHora.getText().toString();
        //final Integer owner = Integer.parseInt(owner.getText().toString());
        //userService.getUserByCedula(owner.getText().toString()).execute().body().getName()

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //showMessage(view,  userService.getByCardId(owner.getText().toString()).execute().body().getName());
                    User user = userService.getByCardId(owner.getText().toString()).execute().body();
                    User company = userService.getUserByEmail(sharedPreference.getValue("USER_LOGGED")).execute().body();
                    Access access = new Access(owner.getText().toString(),  user.getName()+"-"+owner.getText().toString()+"-"+company.getName()+"-"+date+"-"+time, company.getCedula(), company.getName(), time, date, "");
                    Response<ResponseBody> accessResponse = accessService.createAccess(access).execute();
                    if (accessResponse.isSuccessful()) {
                        startMainActivity();
                        showMessage(view, "Create "+access.getOwner());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    showMessage(view, "Error creating the meeting");
                }

            }
        });
    }

    public void startMainActivity(){
        Intent intent = new Intent(this, MainCompanyActivity.class);
        startActivity(intent);
        finish();
    }

    public void showMessage(final View view, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                Snackbar.make(view, message,
                        Snackbar.LENGTH_LONG)
                        .show();

            }
        });
    }



}