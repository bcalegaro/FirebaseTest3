package com.bcalegaro.firebasetest3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseTestTAG";
    private static final int RC_SIGN_IN = 200;
    // Initialize variables
    GoogleSignInClient mGoogleSignInClient ;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sign In Button Config
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener((view -> signInGoogle()));

        // Initialize sign in options
        // the client-id is copied form
        // google-services.json file
        GoogleSignInOptions gso =new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null)
            Log.d(TAG, "Usuário Google logado");
        else
            Log.d(TAG, "Usuário Google não logado");

        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        // Check condition
        if(firebaseUser!=null)
        {
            Log.d(TAG, "Usuário Firebase logado");
            // When user already sign in
            // redirect to profile activity
            startActivity(new Intent(MainActivity.this,ProfileActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void signInGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Log.d(TAG,"Google signed in sucess");

            if (account != null){
                // When signed in account initialize auth credential for firabase
                AuthCredential authCredential= GoogleAuthProvider
                        .getCredential(account.getIdToken()
                                ,null);
                //check credential
                firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this,
                        task -> {
                           //check condition
                           if (task.isSuccessful()){
                               //when logged in firabase with Google account redirect to profile page
                               // When task is successful
                               // Redirect to profile activity
                               startActivity(new Intent(MainActivity.this
                                       ,ProfileActivity.class)
                                       .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                               // Display Toast
                               displayToast("Firebase authentication successful");
                           }
                           else
                           {
                               // When task is unsuccessful
                               // Display Toast
                               displayToast("Authentication Failed :"+task.getException()
                                       .getMessage());
                               Log.d(TAG, "Authentication Failed :"+task.getException()
                                       .getMessage());
                           }
                        });
            }

            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}