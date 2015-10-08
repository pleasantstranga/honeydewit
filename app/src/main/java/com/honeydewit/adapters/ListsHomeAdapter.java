package com.honeydewit.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honeydewit.HoneyDewApplication;
import com.honeydewit.HoneyDewDialogFragment;
import com.honeydewit.ListHomeActivity;
import com.honeydewit.ListsHomeAdapterData;
import com.honeydewit.R;
import com.honeydewit.listeners.OneOffClickListener;
import com.honeydewit.pojos.BasicList;
import com.honeydewit.services.DeleteListService;

import java.io.Serializable;
import java.util.ArrayList;

public class ListsHomeAdapter extends ArrayAdapter<ListsHomeAdapterData> implements Serializable {

	private ArrayList<ListsHomeAdapterData> items;
	private Integer layoutId;
	
	
	public ListsHomeAdapter(Context context, ArrayList<ListsHomeAdapterData> items) {
		super(context, R.layout.listrow, items);
		this.layoutId = R.layout.listrow;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(layoutId, null);
		}
		
		final ListsHomeAdapterData data = items.get(position);
		TextView topRow = (TextView) v.findViewById(R.id.topRow);
		TextView errorRow = (TextView) v.findViewById(R.id.errorExists);
		TextView bottomRow = (TextView) v.findViewById(R.id.bottomRow);
		TextView bottomRowTitle = (TextView) v.findViewById(R.id.bottomRowTitle);
		LinearLayout mainLayout = (LinearLayout)v.findViewById(R.id.mainLayout);

		if (data != null) {


			topRow.setText(data.getListName()); 
			topRow.setEllipsize(TruncateAt.MARQUEE);
			topRow.setFocusable(true);
			topRow.setFocusableInTouchMode(true);
			topRow.setSingleLine(); 
			topRow.setMarqueeRepeatLimit(-1);
			topRow.setSelected(true);
			topRow.requestFocus();
				  
			topRow.setSelected(true);
			bottomRow.setText(String.valueOf(data.getItemCount()));
			bottomRowTitle.setText(R.string.numItemsUnique);

            if(data.getErrorCount() != null && data.getErrorCount() > 0) {
				errorRow.setVisibility(View.VISIBLE);
				errorRow.setTextColor(getContext().getResources().getColor(R.color.error_text_color));
				errorRow.setText(getContext().getText(R.string.errorExists));
				errorRow.setTextSize(12);
			}

		}
		mainLayout.setOnClickListener(new OneOffClickListener() {

			@Override
			public void onClick(View v) {
				Intent listHomeIntent = new Intent(getContext(), ListHomeActivity.class);
				listHomeIntent.putExtra("listId",data.getListId());
				listHomeIntent.putExtra("listTypeId", data.getListTypeId());
				listHomeIntent.addCategory(Intent.ACTION_EDIT);
				listHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getContext().startActivity(listHomeIntent);
				((Activity)getContext()).finish();
			}
		});

		ImageButton deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
		deleteButton.setOnClickListener(new OneOffClickListener(){


			@Override
			public void onClick(View v) {
				showDeleteListDialog(data);

			}
		});

		final CheckBox checked = (CheckBox)v.findViewById(R.id.checked);
		checked.setChecked(data.isChecked());
		changeStrikeThroughText(checked, v);	
		checked.setOnClickListener(new OneOffClickListener() {

			@Override
			public void onClick(View v) {
				changeStrikeThroughText(checked, (LinearLayout)v.getParent());	
				 ((HoneyDewApplication)getContext().getApplicationContext()).getShoppingListDbHelper().setListChecked(checked.isChecked(), data.getListId());
			}

		});
		
		return v;
	}
	private void changeStrikeThroughText(final CheckBox checked,
			View parent) {
	
		TextView topRow = (TextView) parent.findViewById(R.id.topRow);
		TextView bottomRow = (TextView) parent.findViewById(R.id.bottomRow);
		if(checked.isChecked()) {
			topRow.setPaintFlags(topRow.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			bottomRow.setPaintFlags(bottomRow.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}
		else {
			topRow.setPaintFlags( topRow.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			bottomRow.setPaintFlags( topRow.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
		}
	}
	private void showDeleteListDialog(final ListsHomeAdapterData data) {
		final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "delete");
		boolean isExec = ((FragmentActivity)getContext()).getSupportFragmentManager().executePendingTransactions();


		dialog.setMessage(getContext().getText(R.string.areYouSure).toString());
		dialog.setTitle(getContext().getText(R.string.delete).toString());
		dialog.setPositiveButton(getContext().getText(R.string.delete).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if (data != null) {

					final Integer listId = data.getListId();
					remove(data);
					notifyDataSetChanged();
					dialog.dismiss();
					((HoneyDewApplication) getContext().getApplicationContext()).getListsIdsToDelete().add(listId);
					Intent mServiceIntent = new Intent(getContext(), DeleteListService.class);
					mServiceIntent.putExtra("listId", listId);
					getContext().startService(mServiceIntent);


				}
			}
		});

		dialog.setNeutralButton("Cancel", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

}
