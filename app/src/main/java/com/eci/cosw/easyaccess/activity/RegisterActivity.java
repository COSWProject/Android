package com.eci.cosw.easyaccess.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eci.cosw.easyaccess.R;
import com.eci.cosw.easyaccess.model.User;
import com.eci.cosw.easyaccess.service.UserService;
import com.eci.cosw.easyaccess.util.RetrofitHttp;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final String TAG = "";
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

    private List<String> userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        retrofitHttp = new RetrofitHttp();
        userService = retrofitHttp.getRetrofit().create(UserService.class);

        userInfo = new ArrayList<String>();
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

                FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage
                        .fromBitmap(selectedImage);

                FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision
                        .getInstance().getOnDeviceTextRecognizer();

                Task<FirebaseVisionText> firebaseVisionTextTask =
                        firebaseVisionTextRecognizer.processImage(firebaseVisionImage)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                        obtainText(firebaseVisionText);

                                        obtainAllUserInfoFromCedula();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void obtainText(FirebaseVisionText firebaseVisionText) {
        String resultText = firebaseVisionText.getText();
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
            String blockText = block.getText();
            Float blockConfidence = block.getConfidence();
            List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
            Point[] blockCornerPoints = block.getCornerPoints();
            Rect blockFrame = block.getBoundingBox();
            for (FirebaseVisionText.Line line : block.getLines()) {
                String lineText = line.getText();
                userInfo.add(lineText);
                Float lineConfidence = line.getConfidence();
                List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                Point[] lineCornerPoints = line.getCornerPoints();
                Rect lineFrame = line.getBoundingBox();
                for (FirebaseVisionText.Element element : line.getElements()) {
                    String elementText = element.getText();
                    Float elementConfidence = element.getConfidence();
                    List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                    Point[] elementCornerPoints = element.getCornerPoints();
                    Rect elementFrame = element.getBoundingBox();
                }
            }
        }
    }

    private void obtainAllUserInfoFromCedula() {
        List<Character> strings = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', '0');
        String fullName = "";
        String fullId = "";
        for (String x : userInfo) {
            if (!(x.contains("NOMBRES") || x.contains("REPUBLICA") ||
                    x.contains("CEDULA") || x.contains("APELLIDOS") ||
                    x.contains("IDENTIFICACION") || x.contains("FIRMA"))) {
                if (x.contains("NUMERO")) {
                    for (char y : x.toCharArray()) {
                        if (strings.contains(y)) {
                            fullId += y;
                        }
                    }
                } else {
                    fullName += x;
                }
            }
        }
    }
}
