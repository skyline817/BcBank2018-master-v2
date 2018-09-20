package com.apimlab.bcbank2018;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ca.mas.foundation.MAS;
import com.ca.mas.foundation.MASCallback;
import com.ca.mas.foundation.MASRequest;
import com.ca.mas.foundation.MASResponse;
import com.ca.mas.foundation.MASUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;

public class ProfileActivity extends AppCompatActivity {

    private TextView mUserName;
    private TextView mEmail;
    private TextView mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

        mUserName = (TextView) findViewById(R.id.my_name);
        mEmail = (TextView) findViewById(R.id.my_email);
        mPhone = (TextView) findViewById(R.id.my_mobile);
        // Set up the login form.
        MAS.start(this,true);
        try {
            invokeAPI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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
     */
    private void invokeAPI() throws URISyntaxException {

        // - The endpoint typed in the endpoint field
        String endPoint = "/bcbank/userinfo";
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
                    final JSONObject j = response.getBody().getContent();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                mUserName.setText(j.getString("name").toString());
                                mEmail.setText(j.getString("email").toString());
                                mPhone.setText(j.getString("mobile").toString() );
                            } catch (JSONException e) {
                                updateResults(e.getMessage());
                            }
                        }
                        });
                }
            }

            @Override
            public void onError(Throwable e) {
                updateResults(e.getMessage());
            }
        });
    }

}
