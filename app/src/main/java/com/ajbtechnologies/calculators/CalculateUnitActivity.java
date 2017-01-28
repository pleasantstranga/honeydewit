package com.ajbtechnologies.calculators;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.ajbtechnologies.Constants;
import com.ajbtechnologies.OptionsMenuActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.pojos.Unit;
import com.ajbtechnologies.utils.NumberUtil;
import com.ajbtechnologies.utils.StringUtils;

import java.util.List;

public class CalculateUnitActivity extends OptionsMenuActivity {
	
	private Spinner fromSpinner;
	private Spinner toSpinner;
	private EditText numUnits;
	private EditText result;
	private ArrayAdapter<CharSequence> adapter;
	private List<Unit> units;
	private Integer subCategoryCode = null;
	boolean doSet = false;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_calculator);       
        setTitle("Kitchen Calculator");
        getApplicationContext().loadBannerAd((AdView) findViewById(R.id.adView));
        subCategoryCode = getIntent().getIntExtra(Constants.SUB_CAT_CODE, -1);
       
        units = getApplicationContext().getShoppingListDbHelper().getUnitsOfMeasurements(subCategoryCode);
        
        adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        populateAdapter();

		Button setButton = (Button) findViewById(R.id.buttonSet);
        fromSpinner = (Spinner) findViewById(R.id.unitFrom);
        toSpinner = (Spinner) findViewById(R.id.unitTo);
        numUnits = (EditText) findViewById(R.id.numUnits);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
        result = (EditText)findViewById(R.id.result);
		CalculatorItemListener itemListener = new CalculatorItemListener();
        numUnits.setText("1");
        numUnits.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!s.toString().equals(".")) {
					doConversion();
				}
			}
		});
        toSpinner.setOnItemSelectedListener(itemListener);
        fromSpinner.setOnItemSelectedListener(itemListener);

		if(getIntent().hasExtra("doSet")) {
			doSet = true;
			setButton.setVisibility(View.VISIBLE);
			setButton.setBackgroundResource(R.drawable.go_button_background);
			setButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (result.getText() != null && !StringUtils.isEmpty(result.getText().toString()) && NumberUtil.isDouble(result.getText().toString())) {
						getIntent().putExtra("value", result.getText().toString());
						setResult(RESULT_OK, getIntent());
						finish();
					} else {
						String value = "Blank";
						if(result.getText() != null) {
							value = result.getText().toString();
						}
						Toast.makeText(getBaseContext(), value + " is not a valid number. Please fix and try again.", Toast.LENGTH_LONG).show();
					}
				}
			});

		}

	}

	private void populateAdapter() {
		for(Unit unit : units) {
			   adapter.add(unit.getDescription());
		}
	}
	private void doConversion() {
		result.setText(null);
		if(null != numUnits.getText() && !numUnits.getText().toString().isEmpty()
				&& null != fromSpinner.getSelectedItem() && null != toSpinner.getSelectedItem()) {
			result.setText("");
			
			if(subCategoryCode != null) {
				Unit from = getUnit(fromSpinner.getSelectedItem().toString());
				Unit to = getUnit(toSpinner.getSelectedItem().toString());
				Float amount = Float.valueOf(numUnits.getText().toString());
				String text = null;
				if(subCategoryCode == 5) {
					Float temp = CookingUnitConverter.convertTemp(from.getDescription(), to.getDescription(), amount);
					text = temp.toString();
				}
				else {
					
					text = CookingUnitConverter.convertWeightVolume(from, amount, to).toString();
				}
				result.setText(String.valueOf(text));
			}
			
			
			
		}
		
	}
	public Unit getUnit(String description) {
		Unit unitToReturn = null;
		for(Unit unit : units) {
			if(description.equals(unit.getDescription())) {
				unitToReturn = unit;
				break;
			}
		}
		return unitToReturn;
	}
	private class CalculatorItemListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
	
			doConversion();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

		finish();
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
}
