package com.honeydewit;

import android.content.Intent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.honeydewit.pojos.Links;

import java.util.List;

public class HoneyDewDropdownDialogFragment extends HoneyDewDialogFragment {

	private RadioGroup radioGroup;

	public HoneyDewDropdownDialogFragment() {
		super();
	}
	@Override
	public void initViews(boolean hideTitleBar) {
		super.initViews(hideTitleBar);

		radioGroup = (RadioGroup)findViewById(R.id.dialogDropDown);

		radioGroup.setVisibility(View.VISIBLE);
		populateRadioGroup(links, radioGroup);

		setPositiveButton(getContext().getText(R.string.ok).toString(), new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int id= radioGroup.getCheckedRadioButtonId();
				View radioButton = radioGroup.findViewById(id);
				int radioId = radioGroup.indexOfChild(radioButton);
				RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
				String currentItemSelected = (String) btn.getText().toString();

				Intent intent = getIntent(currentItemSelected);
				if (getExtras().containsKey("requestCode")) {
					try {

						BasicActivity.scanForActivity(getContext()).startActivityForResult(intent, (Integer) getExtras().get("requestCode"));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					getContext().startActivity(intent);
				}
				dismiss();

			}
		});

	}


	private void populateRadioGroup(List<Links> links, RadioGroup radioGroup) {
		int count = 0;
		for(Links link : links) {

			RadioButton button = new RadioButton(getContext());
			button.setText(link.getLinkTxt());
			radioGroup.addView(button);

			if(count == 0) {
				button.setChecked(true);
			}
			count+=1;
		}
	}
	public Intent getIntent(String linkText) {
		Intent intent = null;
		try {
			for(Links link : getLinks()) {

				if(link.getLinkTxt().equals(linkText)) {
					Class intentClass = Class.forName(link.getIntent());
					intent = new Intent(getContext(), intentClass);
					intent.putExtra(Constants.SUB_CAT_CODE, link.getSubCategoryCode());
					intent.putExtra("doSet", true);
					break;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return intent;
	}

}