package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.eci.cosw.easyaccess.R;

public class Meetings_details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent infoIntent = getIntent();
        String value = infoIntent.getStringExtra(Intent.EXTRA_TEXT);
        String [] values = value.split("-");
        TextView nombre = (TextView) findViewById(R.id.nombre);
        TextView cedula = (TextView) findViewById(R.id.cedula);
        TextView institucion = (TextView) findViewById(R.id.institucion);
        TextView fecha = (TextView) findViewById(R.id.fecha);
        TextView hora = (TextView) findViewById(R.id.hora);
        nombre.setText(values[0]);
        cedula.setText(values[1]);
        institucion.setText(values[2]);
        fecha.setText(values[3]);
        hora.setText(values[4]);
    }

}
