package com.apimlab.bcbank2018;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ca.mas.core.service.MssoIntents;
import com.ca.mas.foundation.MAS;
import com.ca.mas.foundation.MASAuthenticationListener;
import com.ca.mas.foundation.MASCallback;
import com.ca.mas.foundation.MASOtpAuthenticationHandler;
import com.ca.mas.foundation.MASRequest;
import com.ca.mas.foundation.MASResponse;
import com.ca.mas.foundation.auth.MASAuthenticationProviders;
import com.ca.mas.ui.MASLoginActivity;
import com.ca.mas.ui.otp.MASOtpDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;

public class TransferMoneyActivity extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEndpoint;
    private TextView mDisplayResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mEndpoint = (AutoCompleteTextView) findViewById(R.id.value_transfer);

        Button mInvokeAPI = (Button) findViewById(R.id.call_endpoint);
        mInvokeAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    invokeAPI();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        mDisplayResults = (TextView) findViewById(R.id.display_results_textview);
        MAS.start(this,true);

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
                android.app.DialogFragment otpFragment = MASOtpDialogFragment.newInstance(handler);
                otpFragment.show(((Activity) context).getFragmentManager(), "OTPDialog");
            }
        });

    }

    /**
     * Call this method to updatate the mDisplayResults TextView
     */
    private void updateResults(String values){
        final String val = values;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MAS.getContext(),val,Toast.LENGTH_LONG).show();
            }
        });
    }

    // - TODO: Transfer/Send Money
    /**
     *
     *
     *
     */
    private void invokeAPI() throws URISyntaxException {

        // - The endpoint typed in the endpoint field
        String endPoint = "/bcbank/transfer?value="+mEndpoint.getText().toString();
        // URI Builder
        Uri.Builder uriBuilder = new Uri.Builder().encodedPath(endPoint);
        // Create the MASRequest using the Uri
        MASRequest.MASRequestBuilder requestBuilder = new MASRequest.MASRequestBuilder(uriBuilder.build());
        // Build the request
        MASRequest request = requestBuilder.get().build();

        // Make the call to the endpoit uri
        MAS.invoke(request, new MASCallback<MASResponse<JSONObject>>() {
            @Override
            public void onSuccess(MASResponse<JSONObject> response) {
                //Check for the response code;
                //The module considers success when receiving a response with HTTP status code range 200-299
                if (HttpURLConnection.HTTP_OK == response.getResponseCode()) {
                    JSONObject j = response.getBody().getContent();
                    try {
                       updateResults( j.getString("message").toString() );
                    } catch (JSONException e) {
                        updateResults(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                updateResults(e.getMessage());
            }
        });
    }

}
