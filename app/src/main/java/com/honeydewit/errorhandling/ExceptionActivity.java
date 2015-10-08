package com.honeydewit.errorhandling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.honeydewit.BasicActivity;
import com.honeydewit.MainMenuActivity;
import com.honeydewit.R;

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

        sendReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = getIntent().getStringExtra("error");
                getApplicationContext().sendError(new String[] {"aaronbernstein1975@gmail.com"}, error);

                Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();

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
}
