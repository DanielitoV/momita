package com.example.danico.momasampler.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.danico.momasampler.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText mailUsuario;
    private Button buttonReset;
    private Button buttonRegresar;

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mailUsuario = findViewById(R.id.mailUsuario);
        buttonReset = findViewById(R.id.btn_reset_password);
        buttonRegresar = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mailUsuario.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(ResetPasswordActivity.this, "Ingresa tu direccion de email", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Te vamos a mandar instrucciones pare resetear la contraseña, gilazo!!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ResetPasswordActivity.this, "Fallo el envio de mail", Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
