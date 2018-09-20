package com.apimlab.bcbank2018;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ca.mas.foundation.MAS;
import com.ca.mas.foundation.MASCallback;
import com.ca.mas.foundation.MASUser;

public class MyAccountActivity extends AppCompatActivity {

    private EditText mName;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        Button mBalanceButton = (Button) findViewById(R.id.view_balance_button);
        Button mProfileButton = (Button) findViewById(R.id.view_profile_button);
        Button mTransferButton = (Button) findViewById(R.id.view_transfer_button);
        Button mLogoutButton = (Button) findViewById(R.id.view_logout_button);
        mName = (EditText) findViewById(R.id.user_logged);

        mBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MAS.getContext(),"Your Balance is: $ 3000.00",Toast.LENGTH_LONG).show();
            }
        });

        mProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myProfile();
            }
        });

        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransferMoney();
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        MAS.debug();
        MAS.start(this, true);

        mName.setText("Hello " + MASUser.getCurrentUser().getName().getFamilyName() );

    }
    /**
     * Call this method to updatate display Alerts
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

    // Logout Activity
    private void Logout() {
        MASUser.getCurrentUser().logout(true, new MASCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                updateResults("Logout Success !");
                Intent myIntent = new Intent(MAS.getContext(), MainActivity.class);
                startActivity(myIntent);
            }

            @Override
            public void onError(Throwable e) {
                updateResults("Logout Failed !");
                Intent myIntent = new Intent(MAS.getContext(), MainActivity.class);
                startActivity(myIntent);
            }
        });

    }

    private void TransferMoney() {
        Intent myIntent = new Intent(MAS.getContext(), TransferMoneyActivity.class);
        startActivity(myIntent);
    }

    private void myProfile() {
        Intent myIntent = new Intent(MAS.getContext(), ProfileActivity.class);
        startActivity(myIntent);

    }


}
