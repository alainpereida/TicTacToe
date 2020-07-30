package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private LoginButton loginButtonFacebook;
    private Button loginButton, siginButton;
    private EditText usernameEditText, passwordEditText;
    private CallbackManager callbackManager = CallbackManager.Factory.create();
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);

        loginButton = findViewById(R.id.login);
        siginButton = findViewById(R.id.sign);

        siginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registroNuevoUsuario();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserCuenta();
            }
        });

        loginButtonFacebook = findViewById(R.id.login_button);

        loginButtonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
            }
        });

        loginButtonFacebook.setReadPermissions("email", "public_profile");
        loginButtonFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Activity Here", "facebook:onSuccess:" + loginResult);
                Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_SHORT).show();
                handlerFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancelar, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
            }
        });

        auth = FirebaseAuth.getInstance();
        //Se ejecuta cuando deteca algun cambio en la autenticacion

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    System.err.println(user.getEmail());
                    goHome(user.getEmail());
                    System.err.println("Exito");
                }
                System.err.println("Cancelado");
            }
        };
    }

    private void handlerFacebookAccessToken(AccessToken accessToken) {
        loginButtonFacebook.setVisibility(View.GONE);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        //Se ejecuta cuando deteca que termina todo el proceso
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    System.err.println(task.getException().getMessage());
                    Toast.makeText(getApplicationContext(), R.string.firebase_error_login, Toast.LENGTH_SHORT).show();
                }
                System.err.println("OnComplete");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(firebaseAuthListener);
    }

    private void loginUserCuenta() {
        //Variables para guardar el estado del EditText
        final String eMail, password;

        eMail = this.usernameEditText.getText().toString();
        password = this.passwordEditText.getText().toString();

        //Verificamos que no esten vacios los datos
        if (TextUtils.isEmpty(eMail) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Ingrese datos.", Toast.LENGTH_LONG).show();
            return;
        }

        auth.signInWithEmailAndPassword(eMail, password) //Metodo de Firebase para el entrar
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {//Llamada a metodo asincrono
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) { //Cuando la tarea es completada
                            Toast.makeText(getApplicationContext(), "Login Correcto!", Toast.LENGTH_LONG).show();
                            goHome(eMail);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Login fallido!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void registroNuevoUsuario() {
        //Variables para guardar el estado del EditText
        final String eMail, password;

        eMail = this.usernameEditText.getText().toString();
        password = this.passwordEditText.getText().toString();

        //Verificamos que no esten vacios los datos
        if (TextUtils.isEmpty(eMail) || TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Ingrese datos.", Toast.LENGTH_LONG).show();
            return;
        }

        auth.createUserWithEmailAndPassword(eMail, password) //Metodo de Firebase para crear el usuario
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() { //Llamada a metodo asincrono
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//Cuando la tarea es completada
                            Toast.makeText(getApplicationContext(), "Registro exitoso!", Toast.LENGTH_LONG).show();
                            goHome(eMail);
                        } else {
                            Toast.makeText(getApplicationContext(), "Registro fallido", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void goHome(String name) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("name", name);
        startActivity(intent);
    }
}
