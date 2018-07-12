package com.example.danico.momasampler.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danico.momasampler.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    /***Atributos para Firebase***/
   // private static final String TAG = "Danielito";
    private EditText editTextEmail;
    private EditText editTextContraseña;
    private Button buttonIngresar;
    private Button signUp;
    private Button buttonOlvideContraseña;
    /***Atributos para Firebase***/



    /***Atributos para Google***/
    //con esta variable global desp la voy a usar para autenticar google
    //private GoogleApiClient googleApiClient;

    //agrego el boton del Layout
    private SignInButton signInButton;

    GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 2;
    private FirebaseAuth mAuth;  // Google y particular, con esta variable nos conectamos
    FirebaseAuth.AuthStateListener mAuthListener; //este va a estar siempre escuchando a
                                                  // ver si se puso bien el nombre de usuario y constraseña

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    //datos del logIn para desp cargarlos en el drawer
    private ImageView imagenUsuario;
    private TextView nombreUsuario;
    private TextView mailUsuario;
    private TextView idUsuario;
    /***Atributos para Google***/



    /*** Atributos para Facebook ***/
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    /*** Atributos para Facebook ***/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /***Para Firebase dentro del onCreate***/
        //conecto los atributos con el Layout
        editTextEmail = findViewById(R.id.mailUsuario);
        editTextContraseña = findViewById(R.id.contraseñaUsuario);
        buttonIngresar = findViewById(R.id.buttonIngresar);
        buttonOlvideContraseña = findViewById(R.id.btn_reset_password);
        signUp = findViewById(R.id.btn_signup);




       //le asigno que hacer a cada boton al ser apretado
        //todo: este me lleva a la actividad para registrarme
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        //todo: este me lleva a la actividad para recuperar contraseña
        buttonOlvideContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });


        //todo: este activa el metodo LoguearUsuario
        buttonIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoguearUsuario();   // se verifica que el usuario este cargado en firebase

            }
        });



        /*** Para Google dentro del onCreate***/
        //datos del logIn para cargar luego en el drawer.. los conecto con el layout
        imagenUsuario = findViewById(R.id.imagenUsuario);
        nombreUsuario = findViewById(R.id.nombreUsuario);
        mailUsuario = findViewById(R.id.mailUsuario);
        idUsuario = findViewById(R.id.idUsuario);


        signInButton = findViewById(R.id.bottonGoogle);
        mAuth = FirebaseAuth.getInstance();  // Google y particular
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        // este metodo va a ser usado tanto por Google como por usuario comun tambien
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //UNA VEZ QUE INICIA SESION PASA A LA OTRA ACTIVIDAD
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else {
                    Toast.makeText(LoginActivity.this, "Datos incorrectos, intente de nuevo", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        //escuchador de cuenta de Google por si algo sale mal .. //Para Google Login - Sign Up
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LoginActivity.this, "Parece que algo no salio bien", Toast.LENGTH_SHORT).show();

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        /*** Para Google dentro del onCreate***/



        /*** Para Facebook dentro del OnCreate***/
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }

        callbackManager = CallbackManager.Factory.create();

        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                handleFacebookAccessToken(loginResult.getAccessToken());
                //Toast.makeText(MainActivity.this, "Logueo Exitoso", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                //startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Logueo cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Logueo Erroneo", Toast.LENGTH_SHORT).show();
            }
        });
        /*** Para Facebook dentro del OnCreate***/
    }


    /***Para FireBasa comun fuera del onCreate ***/
    public void LoguearUsuario() {
        String email = editTextEmail.getText().toString();
        String password = editTextContraseña.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
/***Para FireBasa comun fuera del onCreate ***/

    /*** Para Google fuera del OnCreate***/
    @Override // Este metodo se va a ejecutar cuando algo salga mal en la coneccion
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    //Para login con Google
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data); /***SOLO esta linea corresponde a FACEBOOK***/
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In fue exitoso y se pudo autenticar con Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                nombreUsuario.setText(account.getDisplayName());
                mailUsuario.setText(account.getEmail());
                idUsuario.setText(account.getId());

                Glide.with(this).load(account.getPhotoUrl()).into(imagenUsuario);
            } else {
                Toast.makeText(this, "La Autenticacion salio mal", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    //Snackbar.make(findViewById(R.id.), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                    //updateUI(null);
                }
            }
        });
    }
    /*** Para Google fuera del OnCreate***/



    /*** Para Facebook fuera del OnCreate***/
   /* @Override   //ESTE METODO YA ESTA EN LA PARTE DE GOOGLE
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }*/
    private void handleFacebookAccessToken(AccessToken token) {
        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();

                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });
        /*** Para Facebook fuera del OnCreate***/

    }

}
