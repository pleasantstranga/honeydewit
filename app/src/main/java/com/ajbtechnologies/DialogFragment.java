package com.ajbtechnologies;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class DialogFragment extends android.support.v4.app.DialogFragment {

	private TextView message;
	private TextView title;
	private Button positiveButton;
	private Button negativeButton;
	private Button neutralButton;
	private CheckBox checkbox;
	private EditText editText;
	private Map<Object,Object> extras = new HashMap<Object, Object>();
	private String tag = null;

	public static DialogFragment newInstance() {
		DialogFragment frag = new DialogFragment();

		Bundle args = new Bundle();

		frag.setArguments(args);
		frag.setRetainInstance(true);

		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	public void initViews(View view,boolean hideTitleBar) {

		title = (TextView)view.findViewById(R.id.title);
		title.setTextColor(view.getContext().getResources().getColor(R.color.edit_text_color));
		title.setVisibility(View.GONE);

		message = (TextView)view.findViewById(R.id.description);
		message.setVisibility(View.GONE);

		positiveButton = (Button)view.findViewById(R.id.positiveButton);
		negativeButton = (Button)view.findViewById(R.id.negativeButton);
		neutralButton = (Button)view.findViewById(R.id.neutralButton);
		checkbox = (CheckBox)view.findViewById(R.id.dialogCheckBox);
		editText = (EditText)view.findViewById(R.id.dialogTextBox);

		LinearLayout.LayoutParams layoutParamsMessage = (LinearLayout.LayoutParams) message.getLayoutParams();
		layoutParamsMessage.setMargins(10, 10, 10, 10);
		message.setLayoutParams(layoutParamsMessage);

		if(null != getArguments()) {
			if(getArguments().containsKey("title")) {
				setTitle(getArguments().getString("title"));

			}
			if(getArguments().containsKey("message")) {
				setMessage(getArguments().getString("message"));
			}

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater,container,savedInstanceState);
		View view = inflater.inflate(R.layout.honewdew_views_dialog, container);
		initViews(view, false);


		return view;
	}
	public TextView getTitle() {
		return title;
	}
	public TextView getMessage() {
		return message;
	}

	public void setMessage(String description) {
		if(this.message != null) {
			if(null != description) {
				this.message.setVisibility(View.VISIBLE);
				this.message.setText(description);
			}
			else {
				this.message.setVisibility(View.GONE);
				this.message.setText(null);
			}
		}

	}
	public void setTitle(String title) {
		if(this.title != null) {
			if(null != title) {
				this.title.setVisibility(View.VISIBLE);
				this.title.setText(title);
			}
			else {
				this.title.setVisibility(View.GONE);
				this.title.setText(null);
			}
		}

	}

	public Button getPositiveButton() {
		return positiveButton;
	}

	public void setPositiveButton(String buttonText, View.OnClickListener onClickListener) {
		if(positiveButton != null) {
			if(buttonText != null && onClickListener != null) {
				positiveButton.setVisibility(View.VISIBLE);
				positiveButton.setText(buttonText);
				positiveButton.setOnClickListener(onClickListener);

			}
			else {
				positiveButton.setVisibility(View.GONE);
				positiveButton.setText(null);
				positiveButton.setOnClickListener(null);
			}
		}


	}
	public void setCheckbox(String buttonText, CompoundButton.OnCheckedChangeListener onClickListener) {
		if(checkbox != null) {
			if(buttonText != null && onClickListener != null) {
				checkbox.setVisibility(View.VISIBLE);
				checkbox.setText(buttonText);
				checkbox.setOnCheckedChangeListener(onClickListener);

			}
			else {
				checkbox.setVisibility(View.GONE);
				checkbox.setText(null);
				checkbox.setOnCheckedChangeListener(null);
			}
		}


	}
	public Button getNegativeButton() {
		return negativeButton;
	}

	public void setNegativeButton(String buttonText, View.OnClickListener onClickListener) {
		if(negativeButton != null ) {
			if(buttonText != null && onClickListener != null) {
				negativeButton.setVisibility(View.VISIBLE);
				negativeButton.setText(buttonText);
				negativeButton.setOnClickListener(onClickListener);
			}
			else {
				negativeButton.setVisibility(View.GONE);
				negativeButton.setText(null);
				negativeButton.setOnClickListener(null);
			}
		}

	}
	public Button getNeutralButton() {
		return neutralButton;
	}
	public void setNeutralButton(String buttonText, View.OnClickListener onClickListener) {
		if(neutralButton != null) {
			if(buttonText != null && onClickListener != null) {
				neutralButton.setVisibility(View.VISIBLE);
				neutralButton.setText(buttonText);
				neutralButton.setOnClickListener(onClickListener);
			}
			else {
				neutralButton.setVisibility(View.GONE);
				neutralButton.setText(null);
				neutralButton.setOnClickListener(null);
			}
		}

	}

	public Map<Object,Object> getExtras() {
		return extras;
	}
	public void putExtra(String key, Object value) {
		extras.put(key,value);
	}


	public void setTitle(TextView title) {
		this.title = title;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		return dialog;
	}



	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance()) {
			getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("tag", getTag());
		super.onSaveInstanceState(outState);

	}
	public void setEditText(String hintText, String value) {
		if(editText != null) {
			editText.setVisibility(View.VISIBLE);
			editText.setHint(hintText);

			if(value != null) {
				editText.setText(value);
			}
		}
	}
	public String getEditTextValue() {
		String value = null;
		if(editText != null) {
			value =  editText.getText().toString();
		}
		return value;
	}


}