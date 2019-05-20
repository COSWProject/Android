package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.model.User;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private RetrofitHttp retrofitHttp;
    private UserService userService;

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(1);

    private EditText userEmail;
    private EditText userPassword;
    private EditText userName;
    private EditText userMobilePhone;
    private EditText userCity;
    private EditText userCedula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        retrofitHttp = new RetrofitHttp();
        userService = retrofitHttp.getRetrofit().create(UserService.class);
    }

    public void findViews() {
        userEmail = (EditText) findViewById(R.id.userEmail);
        userPassword = (EditText) findViewById(R.id.userPassword);
        userName = (EditText) findViewById(R.id.userName);
        userMobilePhone = (EditText) findViewById(R.id.userMobilePhone);
        userCity = (EditText) findViewById(R.id.userCity);
        userCedula = (EditText) findViewById(R.id.cedula);
    }

    public void register(final View view) {

        view.setEnabled(false);

        findViews();

        final String emailText = userEmail.getText().toString();
        final String passwordText = userPassword.getText().toString();
        final String nameText = userName.getText().toString();
        final Integer mobilePhoneInt = Integer.parseInt(userMobilePhone.getText().toString());
        final String cityText = userCity.getText().toString();
        final String cedulaText = userCedula.getText().toString();

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                User user;

                if (checkBox.isChecked()) {
                    user = new User(nameText, emailText, passwordText, mobilePhoneInt,
                            cityText, "Company", cedulaText);
                } else {
                    user = new User(nameText, emailText, passwordText, mobilePhoneInt,
                            cityText, "User", cedulaText);
                }
                try {
                    Response<ResponseBody> userResponse = userService.createUser(user).execute();

                    if (userResponse.isSuccessful()) {
                        startLoginActivity();

                        finish();
                    }
                } catch (IOException e) {
                    showMessage(view, "Error creating the user");
                }
            }
        });
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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

    public void uploadId(final View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select picture"),
                PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            final Uri imageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
