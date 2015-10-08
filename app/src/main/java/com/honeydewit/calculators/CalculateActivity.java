package com.honeydewit.calculators;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.honeydewit.OptionsMenuActivity;
import com.honeydewit.R;
import com.honeydewit.utils.NumberUtil;
import com.honeydewit.utils.StringUtils;

/**
 * @author aaronbernstein
 *
 */
public class CalculateActivity extends OptionsMenuActivity {

	
	CalculatorView calculatorView;
    boolean doSet = false;

    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator_activity);
        this.calculatorView = (CalculatorView)findViewById(R.id.calculatorView);
        if(getIntent().hasExtra("doSet")) {
            doSet = true;
            calculatorView.findViewById(R.id.buttonSet).setVisibility(View.VISIBLE);
            calculatorView.findViewById(R.id.buttonSet).setBackgroundResource(R.drawable.go_button_background);
            calculatorView.findViewById(R.id.buttonSet).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(calculatorView.getCalculatorEquationDisplay().getText() != null && !StringUtils.isEmpty(calculatorView.getCalculatorEquationDisplay().getText().toString()) && NumberUtil.isDouble(calculatorView.getCalculatorEquationDisplay().getText().toString())) {
                        getIntent().putExtra("value", calculatorView.getCalculatorEquationDisplay().getText().toString());
                        setResult(RESULT_OK, getIntent());
                        finish();
                    }
                    else {
                        if(calculatorView.getCalculatorEquationDisplay().getText() != null) {
                            String value = calculatorView.getCalculatorEquationDisplay().getText().toString();
                            if(StringUtils.isEmpty(value)) {
                                value = "Blank";
                            }
                            Toast.makeText(getBaseContext(),value + " is not a valid number. Please fix and try again.",Toast.LENGTH_LONG).show();

                        }
                    }
                }
            });
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
        // Handle presses on the action bar items
        if(item.getItemId() == R.id.back) {
          onBackPressed();
        }
        return true;
    }

}
