package com.eci.cosw.easyaccess.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.eci.cosw.easyaccess.R;

import net.glxn.qrgen.android.QRCode;

public class CodeGeneratorActivity extends AppCompatActivity {
    private static final int ALTURA_CODIGO = 500, ANCHURA_CODIGO = 500;
    private static final int CODIGO_PERMISO_ESCRIBIR_ALMACENAMIENTO = 1;
    private EditText etTextoParaCodigo;
    private String text;

    private boolean tienePermisoParaEscribir = false; // Para los permisos en tiempo de ejecución

    private String obtenerTextoParaCodigo() {
        etTextoParaCodigo.setError(null);
        String posibleTexto = etTextoParaCodigo.getText().toString();
        if (posibleTexto.isEmpty()) {
            etTextoParaCodigo.setError("Escribe el texto del código QR");
            etTextoParaCodigo.requestFocus();
        }
        return posibleTexto;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codegenerator);

        Intent intent = getIntent();
        text = intent.getStringExtra("QR");


        final ImageView imagenCodigo = findViewById(R.id.ivCodigoGenerado);


        Bitmap bitmap = QRCode.from(text).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).bitmap();
        imagenCodigo.setImageBitmap(bitmap);


    }


    private void verificarYPedirPermisos() {
        if (
                ContextCompat.checkSelfPermission(
                        CodeGeneratorActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        ==
                        PackageManager.PERMISSION_GRANTED
        ) {
            // En caso de que haya dado permisos ponemos la bandera en true
            tienePermisoParaEscribir = true;
        } else {
            // Si no, entonces pedimos permisos
            ActivityCompat.requestPermissions(CodeGeneratorActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CODIGO_PERMISO_ESCRIBIR_ALMACENAMIENTO);
        }
    }

    private void noTienePermiso() {
        Toast.makeText(CodeGeneratorActivity.this, "No has dado permiso para escribir", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODIGO_PERMISO_ESCRIBIR_ALMACENAMIENTO:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // SÍ dieron permiso
                    tienePermisoParaEscribir = true;

                } else {
                    // NO dieron permiso
                    noTienePermiso();
                }
        }
    }
}
