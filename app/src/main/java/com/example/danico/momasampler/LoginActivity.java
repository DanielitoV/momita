package com.example.danico.momasampler;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
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

    /***Atributos para Google***/
    // private FirebaseAuth mAuth;

    //con esta variable global desp la voy a usar para autenticar google
    private GoogleApiClient googleApiClient;

    //agrego el boton del Layout
    private SignInButton signInButton;

    GoogleApiClient mGoogleApiClient;
    private final static int RC_SIGN_IN = 2;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    //voy a crear una variable aca para evitar mantener un valor constante
    // public static final int SIGN_IN_CODE = 9001;


    //datos del logIn
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

        /*** Para Google dentro del onCreate***/

        //datos del logIn
        imagenUsuario = findViewById(R.id.imagenUsuario);
        nombreUsuario = findViewById(R.id.nombreUsuario);
        mailUsuario = findViewById(R.id.mailUsuario);
        idUsuario = findViewById(R.id.idUsuario);


        signInButton = findViewById(R.id.bottonGoogle);

        mAuth = FirebaseAuth.getInstance();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    //UNA VEZ QUE INICIA SESION PASA A LA OTRA ACTIVIDAD
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

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

       /* signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        //le doy una accion al botonDeGoogle
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override//se ejecuta este metodo, y aca le digo lo qu tiene que hacer
            public void onClick(View view) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);

            }
        });
        // el requestEmail es por si queremos obtener informacion del mail
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();*/
        /*** Para Google dentro del onCreate***/


        /*** Para Facebook dentro del OnCreate***/
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

                // ...
            }
        });
    }



  /*  private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }*/


   /* @Override // a este metodo van  a llegar los resultados, y tenemos que
    // preguntarle si es el metodo que queremos
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data); *//***solo esta linea es para Facebook***//*
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == SIGN_IN_CODE) {  //este resultado lo obtenemos mediante un intent (data)
            // para usar bien el resultado que obtenemos aca, se lo pasamos a otro metodo (handleSignInResult)
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override  //para tener un login silencioso
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });

        }
    }


    //creo el metodo de arriba aca y le digo a este, lo que quiero que haga con el result
    // que seria comprobar si la operacion fue exitosa
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            nombreUsuario.setText(account.getDisplayName());
            mailUsuario.setText(account.getEmail());
            idUsuario.setText(account.getId());

          *//*  Log.d("MIAPP", account.getPhotoUrl().toString());*//*
            Glide.with(this).load(account.getPhotoUrl()).into(imagenUsuario);
            goMainSceen();
        } else {
            Toast.makeText(this, "No pudiste iniciar sesion pascualito, proba de nuevo", Toast.LENGTH_SHORT).show();

        }
    }

    private void goMainSceen() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }*/
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

                        // ...
                    }
                });
        /*** Para Facebook fuera del OnCreate***/

    }

}
