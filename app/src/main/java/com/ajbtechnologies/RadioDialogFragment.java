package com.ajbtechnologies;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ajbtechnologies.pojos.Links;

import java.util.List;

public class RadioDialogFragment extends DialogFragment {

	private RadioGroup radioGroup;
	private SerializableArrayList<Links> links;
	private Integer requestCode = null;

	public RadioDialogFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.honewdew_views_dialog, container);
		initViews(view,false);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void initViews(View view,boolean hideTitleBar) {

		super.initViews(view, hideTitleBar);

		radioGroup = (RadioGroup)view.findViewById(R.id.dialogRadio);

		radioGroup.setVisibility(View.VISIBLE);

		links = (SerializableArrayList<Links>)getArguments().getSerializable("links");

		if(getArguments() != null && getArguments().containsKey("requestCode")) {
			this.requestCode = getArguments().getInt("requestCode");
		}

		populateRadioGroup(links, radioGroup);

		setPositiveButton(getActivity().getText(R.string.ok).toString(), new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				int id= radioGroup.getCheckedRadioButtonId();
				View radioButton = radioGroup.findViewById(id);
				int radioId = radioGroup.indexOfChild(radioButton);
				RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
				String currentItemSelected = btn.getText().toString();

				Intent intent = getIntent(currentItemSelected);
				if (requestCode != null) {
					try {

						getActivity().startActivityForResult(intent, requestCode);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					getActivity().startActivity(intent);
				}
				dismiss();

			}
		});


	}


	private void populateRadioGroup(List<Links> links, RadioGroup radioGroup) {
		int count = 0;

		for(Links link : links) {

			RadioButton button = new RadioButton(getActivity());
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
			for(Links link : links) {

				if(link.getLinkTxt().equals(linkText)) {
					Class intentClass = Class.forName(link.getIntent());
					intent = new Intent(getActivity(), intentClass);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
}