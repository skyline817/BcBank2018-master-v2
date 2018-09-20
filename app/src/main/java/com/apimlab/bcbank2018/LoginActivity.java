package com.apimlab.bcbank2018;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ca.mas.core.service.MssoIntents;
import com.ca.mas.foundation.MAS;
import com.ca.mas.foundation.MASAuthenticationListener;
import com.ca.mas.foundation.MASCallback;
import com.ca.mas.foundation.MASOtpAuthenticationHandler;
import com.ca.mas.foundation.MASUser;
import com.ca.mas.foundation.auth.MASAuthenticationProviders;
import com.ca.mas.ui.MASLoginActivity;


public class LoginActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mPMFKeyView;
    private EditText mPasswordView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        MAS.start(this, true);

        if (MASUser.getCurrentUser() != null) {
            Intent myIntent = new Intent(LoginActivity.this, MyAccountActivity.class);
            startActivity(myIntent);
        } else {
            Login();
        }
       }

    private void Login() {
            //Setup the MASAuthenticationListener for a user authentication request.
            MAS.setAuthenticationListener(new MASAuthenticationListener() {

                @Override
                public void onAuthenticateRequest(Context context, long requestId, MASAuthenticationProviders providers) {
                    Intent loginIntent = new Intent(context, MASLoginActivity.class);
                    loginIntent.putExtra(MssoIntents.EXTRA_AUTH_PROVIDERS, providers);
                    loginIntent.putExtra(MssoIntents.EXTRA_REQUEST_ID, requestId);
                    startActivity(loginIntent);
                }

                @Override
                public void onOtpAuthenticateRequest(Context context, MASOtpAuthenticationHandler handler) {

                }
            });

            //Will trigger the MASAuthenticationListener when the user's token has expired.
            MASUser.login(new MASCallback<MASUser>() {
                @Override
                public void onSuccess(MASUser result) {
                    //User login successfully
                    updateResults("Login Success");
                    Intent myIntent = new Intent(LoginActivity.this, MyAccountActivity.class);
                    startActivity(myIntent);

                }

                @Override
                public void onError(Throwable e) {
                    //Handle the error
                    updateResults(e.getMessage());
                }
            });
    }

    private void callMyActivity () {
        Intent myIntent = new Intent(LoginActivity.this, MyAccountActivity.class);
        startActivity(myIntent);
    }
    /**
     * Call this method to updatate the mDisplayResults TextView
     */
    private void updateResults(String values){
        final String val = values;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),val,Toast.LENGTH_LONG).show();
            }
        });
    }

}