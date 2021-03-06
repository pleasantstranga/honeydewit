package com.ajbtechnologies;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;


public class MainMenuActivity extends MenuActivity {
	private boolean isExit = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		try {
			getActionBar().hide();
			setContentView(R.layout.main);
			ProgramInitializer.initialize(getApplicationContext());
			getApplicationContext().loadBannerAd((AdView) findViewById(R.id.adView));
			setLinks(getApplicationContext().getShoppingListDbHelper().getLinks(1));
			((TextView)findViewById(R.id.headerTxt)).setText(R.string.app_name);
			if (getIntent().hasExtra(Constants.MESSAGE)) {
				if (getIntent().getExtras().get(Constants.MESSAGE) instanceof Integer) {
					Toast.makeText(getBaseContext(), getText(getIntent().getIntExtra(Constants.MESSAGE, -1)),
							Toast.LENGTH_SHORT).show();
				} else if (getIntent().getExtras().get(Constants.MESSAGE) instanceof String) {
					Toast.makeText(getBaseContext(), getIntent().getStringExtra(Constants.MESSAGE),
							Toast.LENGTH_SHORT).show();
				}

			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onBackPressed() {
		if(isExit) {
			super.onBackPressed();
		}
		else {
			showExitDialog();
		}

	}
	public void showExitDialog() {

		final DialogFragment dialog = new DialogFragment();
		dialog.show(getSupportFragmentManager(),"exit");
		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		dialog.setMessage("Are you sure you want to exit " + getText(R.string.app_name) + "?");
		dialog.setPositiveButton(getText(R.string.ok).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				try {
					dialog.dismiss();
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		dialog.setNegativeButton(getText(R.string.cancel).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				dialog.dismiss();

			}
		});
		isExit = true;

	}
}