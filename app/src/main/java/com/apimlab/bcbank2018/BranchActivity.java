package com.apimlab.bcbank2018;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import com.ca.mas.foundation.MAS;
import com.ca.mas.foundation.MASCallback;
import com.ca.mas.foundation.MASRequest;
import com.ca.mas.foundation.MASRequestBody;
import com.ca.mas.foundation.MASResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends AppCompatActivity {

    // UI references.
    private String mEndpointView;
    private TextView mDisplayResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);
        // Set up the login form.
        mEndpointView = "/bcbank/branch";
        mDisplayResults = (TextView) findViewById(R.id.display_results_textview);
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
                mDisplayResults.setText(val);
            }
        });
    }

    // - TODO: Complete this method Exercise 2
    /**
     * Consume the endpoint created in the server using MAS.Invoke. You must use the MASUI to trigger the user authentication.
     * NOTE: Make sure the user is logged off in the previous exercise.
     * @see <a href="http://mas.ca.com/docs/android/1.8.00/guides/#send-http-requests-to-apis">Protect API with OAuth</a>
     */
    private void invokeAPI() throws URISyntaxException {

        // - The endpoint typed in the endpoint field
        String endPoint = mEndpointView;
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
                    updateResults(j.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                updateResults(e.getMessage());
            }
        });
    }
}
