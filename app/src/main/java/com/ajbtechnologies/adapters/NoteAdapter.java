package com.ajbtechnologies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajbtechnologies.BasicActivity;
import com.ajbtechnologies.Constants;
import com.ajbtechnologies.DialogFragment;
import com.ajbtechnologies.DrawNoteActivity;
import com.ajbtechnologies.NoteImageActivity;
import com.ajbtechnologies.NotesActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.dataaccess.DbHelperImpl;
import com.ajbtechnologies.listeners.OneOffClickListener;
import com.ajbtechnologies.pojos.ListItem;

import java.io.Serializable;
import java.util.List;

@SuppressLint("ViewConstructor")
public class NoteAdapter extends BaseListItemImageAdapter implements Serializable {

	private DbHelperImpl dbHelper;
	private int layoutId;
	
	public NoteAdapter(BasicActivity context, int layoutId, List<ListItem> items){
		super(context, layoutId, items);
		dbHelper = new DbHelperImpl(context);
		this.layoutId = layoutId;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ListItem item = getItem(position);

			ViewGroup relativeLayout = (RelativeLayout)convertView;
			if (relativeLayout == null) {
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				relativeLayout = (ViewGroup)vi.inflate(layoutId, null);
			}

			if (item != null) {

				TextView name = (TextView) relativeLayout.findViewById(R.id.topRow);
				LinearLayout mainLayout = (LinearLayout)relativeLayout.findViewById(R.id.mainLayout);
				initImage(item, relativeLayout);


				name.setEllipsize(TruncateAt.MARQUEE);
				name.setFocusable(true);
				name.setFocusableInTouchMode(true);
				name.setSingleLine(); 
				name.setMarqueeRepeatLimit(-1);
				name.setText(item.getName());
				name.setSelected(true);
				name.requestFocus();

				mainLayout.setOnClickListener(new OneOffClickListener() {


					@Override
					public void onClick(View v) {
						Intent newListIntent = null;
						if (item.getImageName() == null) {
							newListIntent = new Intent(getContext(), NotesActivity.class);
						} else {
							if (item.isCameraImage()) {
								newListIntent = new Intent(getContext(), NoteImageActivity.class);
							} else if (item.isDrawingImage()) {
								newListIntent = new Intent(getContext(), DrawNoteActivity.class);
							}

						}
						//newListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						newListIntent.putExtra("itemId", item.get_id());
						((BasicActivity)getContext()).startActivityForResult(newListIntent, Constants.ITEM_TYPE_REQUEST);


					}
				});
				ImageButton deleteButton = (ImageButton) relativeLayout.findViewById(R.id.deleteButton);
				deleteButton.setOnClickListener(new OneOffClickListener() {


					@Override
					public void onClick(View v) {

						final DialogFragment dialog = new DialogFragment();
						dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "delete");
						boolean isExec = ((FragmentActivity) getContext()).getSupportFragmentManager().executePendingTransactions();

						dialog.setTitle(getContext().getText(R.string.delete).toString());
						dialog.setMessage(getContext().getText(R.string.deleteItemWarning).toString());
						dialog.setPositiveButton(getContext().getText(R.string.ok).toString(), new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								dbHelper.deleteListItem(item.get_id());
								remove(item);
								notifyDataSetChanged();
								dialog.dismiss();
							}
						});
						dialog.setNegativeButton(getContext().getText(R.string.cancel).toString(), new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialog.dismiss();
							}
						});

					}
				});
				final CheckBox checkBox = (CheckBox)relativeLayout.findViewById(R.id.checked);


				changeBackgroundOnChecked(checkBox, relativeLayout, name, null);

				checkBox.setOnClickListener(new OneOffClickListener() {

					@Override
					public void onClick(View v) {
						LinearLayout parent = ((LinearLayout) v.getParent());
						TextView tt = (TextView) parent.findViewById(R.id.topRow);
						changeBackgroundOnChecked(checkBox, parent, tt, null);
						item.setChecked(checkBox.isChecked() ? Constants.TRUE : Constants.FALSE);
						dbHelper.addUpdateListItem(item);
					}

				});
				checkBox.setChecked(item.getChecked() == 1);

				
			}
			return relativeLayout;
	
	}

    
}