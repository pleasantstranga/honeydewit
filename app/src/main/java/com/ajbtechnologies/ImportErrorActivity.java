package com.ajbtechnologies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.ajbtechnologies.adapters.ImportErrorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aaronbernstein on 3/29/15.
 */
public class ImportErrorActivity extends BasicActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_error_home);

        ArrayAdapter<String> adapter = new ImportErrorAdapter(this,R.layout.import_error_row,getAllErrorsList());
        ListView listView = (ListView)findViewById(R.id.list);
        listView.setAdapter(adapter);

        Button button = (Button)findViewById(R.id.okBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),MainMenuActivity.class));
                finish();
            }
        });
    }
    public List<String> getAllErrorsList() {
        List<String> list = new ArrayList<String>();
        //noinspection unchecked
        Map<String,List<String>> errors = (HashMap<String,List<String>>)getIntent().getSerializableExtra("errors");

        for(Map.Entry<String,List<String>> entry : errors.entrySet()) {
            for(String error : entry.getValue()) {
                list.add(error);
            }
        }
        return list;
    }
}
