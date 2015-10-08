 package com.honeydewit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.honeydewit.pojos.Links;

import java.util.ArrayList;
import java.util.List;

 public class CalculatorsDialogActivity extends BasicActivity implements View.OnClickListener{
     TextView messageView;
     String title;
     String message;
     Spinner calculatorTypes;
     Intent calcIntent;
     Button okBtn;
     Button cancelBtn;
     List<Links> links;

     private static final int REQUEST_CODE = 32;
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         try {
             setContentView(R.layout.calculators_dialog_activity);
             setTheme(R.style.defaultDialog);
             okBtn = (Button)findViewById(R.id.okBtn);
             cancelBtn = (Button)findViewById(R.id.cancelBtn);
             okBtn.setOnClickListener(this);
             cancelBtn.setOnClickListener(this);
             title = getText(R.string.chooseCalculatorDialogTitle).toString();
             title = title.replaceFirst("\\^", getIntent().getStringExtra(Constants.CALC_TYPE));
             setTitle(title);

             message = getText(R.string.chooseCalculatorDialogMessage).toString();
             message = message.replaceFirst("\\^", getIntent().getStringExtra(Constants.CALC_TYPE));
             messageView = (TextView)findViewById(R.id.message);
             messageView.setText(message);
             links = getApplicationContext().getShoppingListDbHelper().getLinks(3, new Integer[]{11});
             calculatorTypes = (Spinner)findViewById(R.id.calculatorTypes);
             ArrayAdapter<String> adapter = new ArrayAdapter<String>(CalculatorsDialogActivity.this,android.R.layout.simple_spinner_dropdown_item,
                     getSpinnerValues(links).toArray(new String[links.size()]));
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            // Apply the adapter to the spinner
             calculatorTypes.setAdapter(adapter);
             calculatorTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                     String linkText = ((CheckedTextView)selectedItemView).getText().toString();
                     calcIntent = getIntent(linkText);
                 }

                 @Override
                 public void onNothingSelected(AdapterView<?> parentView) {
                     // your code here
                 }

             });



         }
        catch(Exception e) {
            e.printStackTrace();
        }

     }

     @Override
     protected void onDestroy() {
         // TODO Auto-generated method stub
         super.onDestroy();
         getApplicationContext().getShoppingListDbHelper().close();
         getApplicationContext().setShoppingListDbHelper(null);
         getApplicationContext().setCalDbHelper(null);
     }
     @Override
     public void onBackPressed() {
         // TODO Auto-generated method stub
         super.onBackPressed();
         Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
         startActivity(mainMenuIntent);

     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu items for use in the action bar
         MenuInflater inflater = getMenuInflater();
         inflater.inflate(R.menu.action_bar_back, menu);
         return super.onCreateOptionsMenu(menu);

     }
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         if(item.getItemId() == R.id.back) {
             onBackPressed();

         }
         finish();
         return true;

     }
     public ArrayList<String> getSpinnerValues(List<Links> links) {
         ArrayList<String> calculators = new ArrayList<String>();
         for(Links link : links) {
             calculators.add(link.getLinkTxt());
         }
         return calculators;
     }
     public Intent getIntent(String linkText) {
         Intent intent = null;

         Class<?> intentClass = null;

         try {
             for(Links link : links) {
                 if(link.getLinkTxt().equals(linkText)) {
                     intentClass = Class.forName(link.getIntent());
                     break;
                 }

             }
         }
         catch(Exception e) {
             e.printStackTrace();
         }
         if(null != intentClass) {
             intent = new Intent(this, intentClass);
             intent.putExtra("doSet", true);
         }
         return intent;
     }

     @Override
     public void onClick(View v) {
         if(v == okBtn) {
             if(calcIntent != null) {
                 startActivityForResult(calcIntent,REQUEST_CODE);
             }
         }
         else {
             setResult(RESULT_CANCELED);
             finish();
         }
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
             data.putExtra("value", data.getDoubleExtra("value",0D));
             setResult(resultCode, data);
             finish();
         }
         if(requestCode == REQUEST_CODE && resultCode == RESULT_CANCELED) {
             setResult(resultCode, data);
             finish();
         }
     }

 }
