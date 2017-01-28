package com.ajbtechnologies.calculators;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.ads.AdView;
import com.ajbtechnologies.Constants;
import com.ajbtechnologies.Application;
import com.ajbtechnologies.R;
import com.ajbtechnologies.pojos.Unit;

import java.util.List;

public class CalculateUnitFragment extends android.app.Fragment {
	
	private Spinner fromSpinner;
	private Spinner toSpinner;
	private EditText numUnits;
	private EditText result;
	private ArrayAdapter<CharSequence> adapter;
	private List<Unit> units;
	private Integer subCategoryCode = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.unit_calculator, container, false);
        getActivity().setTitle(getArguments().getString(Constants.CALC_TYPE));
		((Application)getActivity().getApplicationContext()).loadBannerAd((AdView) rootView.findViewById(R.id.adView));
        subCategoryCode = getArguments().getInt(com.ajbtechnologies.Constants.SUB_CAT_CODE);
       
        units = ((Application)getActivity().getApplicationContext()).getShoppingListDbHelper().getUnitsOfMeasurements(subCategoryCode);
        
        adapter = new ArrayAdapter<CharSequence> (getActivity(), android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        populateAdapter();
        		
        fromSpinner = (Spinner) rootView.findViewById(R.id.unitFrom);
        toSpinner = (Spinner) rootView.findViewById(R.id.unitTo);
        numUnits = (EditText) rootView.findViewById(R.id.numUnits);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);
        result = (EditText)rootView.findViewById(R.id.result);
		CalculatorItemListener itemListener = new CalculatorItemListener();
        numUnits.setText("1");
		numUnits.setSelection(0);
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
				if (!s.toString().equals(".")) {
					doConversion();
				}
			}
		});
        toSpinner.setOnItemSelectedListener(itemListener);
        fromSpinner.setOnItemSelectedListener(itemListener);

		return rootView;
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imgr.showSoftInput(getView(), InputMethodManager.SHOW_IMPLICIT);
	}
}
