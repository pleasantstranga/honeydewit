package com.ajbtechnologies.errorhandling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.ajbtechnologies.BasicActivity;
import com.ajbtechnologies.MainMenuActivity;
import com.ajbtechnologies.R;

public class ExceptionActivity extends BasicActivity {

    Button sendReportBtn;
    Button goHomeBtn;
    CheckBox rememberChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exceptionactivity);
        setTitle("Error");


        sendReportBtn = (Button)findViewById(R.id.sendReportBtn);
        goHomeBtn = (Button)findViewById(R.id.goHomeBtn);

        sendReportBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = getIntent().getStringExtra("error");
                String[] emailAddresses = new String[] {"aaronbernstein1975@gmail.com"};
                if (error != null) {
                    Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,emailAddresses);
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " Error Report");
                    emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,error);
                    startActivityForResult(emailIntent,100);
                }
                else {
                    Toast.makeText(getBaseContext(), "There was an error sending the report. Please try again", Toast.LENGTH_LONG).show();
                }

            }
        });
        goHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getApplicationContext().getShoppingListDbHelper().close();
        getApplicationContext().setShoppingListDbHelper(null);
        getApplicationContext().setCalDbHelper(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
