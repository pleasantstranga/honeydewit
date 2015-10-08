package com.honeydewit.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeydewit.BasicActivity;
import com.honeydewit.Constants;
import com.honeydewit.DrawNoteActivity;
import com.honeydewit.HoneyDewApplication;
import com.honeydewit.HoneyDewDialogFragment;
import com.honeydewit.ItemActivity;
import com.honeydewit.ItemsPrice;
import com.honeydewit.ListItemWatcher;
import com.honeydewit.NotesActivity;
import com.honeydewit.R;
import com.honeydewit.ToDoItemActivity;
import com.honeydewit.listeners.OneOffClickListener;
import com.honeydewit.pojos.ListItem;
import com.honeydewit.utils.ImageUtil;
import com.honeydewit.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

public class ListItemAdapterBkup extends ArrayAdapter<ListItem> {
	private ListItemWatcher watcher;
	Integer listTypeId;

	public ListItemAdapterBkup(Context context, int layoutId, List<ListItem> items) {
		super(context, R.layout.listrow, R.id.topRow, items);
		if(items != null && items.size() > 0) {
			listTypeId = items.get(0).getList().getListTypeId();
		}
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View linearLayout = super.getView(position, convertView, parent);
		final ListItem item = getItem(position);


		TextView name = (TextView) linearLayout.findViewById(R.id.topRow);
		TextView bottomRowTitle = (TextView) linearLayout.findViewById(R.id.bottomRowTitle);
		TextView bottomRowValue = (TextView) linearLayout.findViewById(R.id.bottomRow);
		TextView errorExists = (TextView)linearLayout.findViewById(R.id.errorExists);
		LinearLayout mainLayout = (LinearLayout)linearLayout.findViewById(R.id.mainLayout);
		TextView numItemsText = (TextView) ((RelativeLayout)parent.getParent()).findViewById(R.id.numItems);
		TextView totalPriceText =  (TextView) ((RelativeLayout)parent.getParent()).findViewById(R.id.totalPrice);

		if(watcher == null) {
			watcher = new ListItemWatcher(initaliseItemsPrice(), numItemsText, totalPriceText);
		}

		if (item != null) {

			initImage(item, (ViewGroup)linearLayout);
			name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			name.setFocusable(true);
			name.setFocusableInTouchMode(true);
			name.setSingleLine();
			name.setMarqueeRepeatLimit(-1);
			name.setSelected(true);
			name.requestFocus();
			name.setText(item.getName());

			if(listTypeId== Constants.SHOPPING_LIST_TYPE_CDE ) {
				bottomRowTitle.setText(R.string.numItemsUnique);
				bottomRowValue.setText(NumberUtil.returnNumberString(item.getQuantity()));

			}
			else if(listTypeId == Constants.TODO_LIST_TYPE_CDE) {
				bottomRowTitle.setText(R.string.descriptionTxt);
				bottomRowValue.setText(item.getDescription().trim());
				bottomRowValue.setEllipsize(TextUtils.TruncateAt.MARQUEE);
				bottomRowValue.setFocusable(true);
				bottomRowValue.setFocusableInTouchMode(true);
				bottomRowValue.setSingleLine();
				bottomRowValue.setMarqueeRepeatLimit(-1);
				bottomRowValue.setSelected(true);
				bottomRowValue.requestFocus();
			}
			if(item.hasErrors()) {
				errorExists.setVisibility(View.VISIBLE);
				errorExists.setText(getContext().getResources().getText(R.string.errorExists));
				errorExists.setTextColor(getContext().getResources().getColor(R.color.error_text_color));
				errorExists.setTextSize(12);
			}
			else {
				errorExists.setVisibility(View.GONE);
			}
		}
		mainLayout.setOnClickListener(new OneOffClickListener() {


			@Override
			public void onClick(View v) {
				Intent newListIntent = null;
				if(listTypeId == Constants.SHOPPING_LIST_TYPE_CDE) {
					newListIntent = new Intent(getContext(), ItemActivity.class);

				}
				else if(listTypeId == Constants.TODO_LIST_TYPE_CDE) {
					newListIntent = new Intent(getContext(), ToDoItemActivity.class);
				}
				else if(listTypeId == Constants.NOTES_LIST_TYP_CODE) {
					if(item.getImageName() == null) {
						newListIntent = new Intent(getContext(), NotesActivity.class);
					}
					else {
						newListIntent = new Intent(getContext(), DrawNoteActivity.class);

					}
				}
				//newListIntent.addFlags(Intent.FLA);
				newListIntent.putExtra("itemId", item.get_id());
				((BasicActivity)getContext()).startActivityForResult(newListIntent, Constants.ITEM_TYPE_REQUEST);

			}
		});
		ImageButton deleteButton = (ImageButton) linearLayout.findViewById(R.id.deleteButton);
		deleteButton.setOnClickListener(new OneOffClickListener() {


			@Override
			public void onClick(View v) {
				final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
				dialog.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "delete");
				boolean isExec = ((FragmentActivity)getContext()).getSupportFragmentManager().executePendingTransactions();

				dialog.setTitle(getContext().getText(R.string.delete).toString());
				dialog.setMessage(getContext().getText(R.string.deleteItemWarning).toString());
				dialog.setPositiveButton(getContext().getText(R.string.ok).toString(), new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						((HoneyDewApplication)getContext().getApplicationContext()).getShoppingListDbHelper().deleteListItem(item.get_id());
						remove(item);
						watcher.removeItem(item);
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
		final CheckBox checkBox = (CheckBox)linearLayout.findViewById(R.id.checked);
		checkBox.setOnClickListener(new OneOffClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout parent = ((LinearLayout)v.getParent());
				TextView tt = (TextView) parent.findViewById(R.id.topRow);
				TextView quantity = (TextView) parent.findViewById(R.id.bottomRow);
				changeBackgroundOnChecked(checkBox, parent, tt, quantity);
				item.setChecked(checkBox.isChecked() ? Constants.TRUE : Constants.FALSE);
				((HoneyDewApplication)getContext().getApplicationContext()).getShoppingListDbHelper().addUpdateListItem(item);
				watcher.setChecked(item);
			}

		});
		checkBox.setChecked(item.getChecked() == 1);
		changeBackgroundOnChecked(checkBox, linearLayout, name, bottomRowValue);


		return linearLayout;
	}

	public void initImage(final ListItem item, ViewGroup parent) {
		ImageView imageView = (ImageView) parent.findViewById(R.id.noteImage);
		if(item.getImageName() == null || item.getImageName().trim().length() == 0) {
			imageView.setVisibility(View.GONE);
		}
		else {
			if(item.getImageName() != null && item.getImageName().trim().length() > 0) {

				String filePath = ((HoneyDewApplication)getContext().getApplicationContext()).getImagesDirectory() + item.getImageName();
				Bitmap bm = ImageUtil.getThumbnailBitmap(filePath);
				if(bm == null) {
					bm = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.nophoto, null), 26, 26);
				}
				imageView.setImageBitmap(bm);
			}
			else {
				imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.nophoto));
			}
			imageView.setVisibility(View.VISIBLE);
		}


	}
	public void changeBackgroundOnChecked(final CheckBox checked, View parent, TextView tt, TextView quantity) {
		if(checked.isChecked()) {
			if(null != tt) {
				tt.setPaintFlags(tt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}
			if(null != quantity) {
				quantity.setPaintFlags(quantity.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			}

		}
		else {
			//parent.setAlpha(1);
			if(null != tt) {
				tt.setPaintFlags( tt.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
			}
			if(null != quantity) {
				quantity.setPaintFlags(quantity.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

			}
		}
	}
	private List<ItemsPrice> initaliseItemsPrice() {
		List<ItemsPrice> itemPrices = new ArrayList<ItemsPrice>();
		for(int position = 0; position < getCount(); position++) {
			ListItem item = getItem(position);
			if(item.isChecked()) {
				ItemsPrice itemPrice = new ItemsPrice(item.get_id(), item.getQuantity(), null != item.getPricePerUnit() ? item.getPricePerUnit() : 0) ;
				itemPrices.add(itemPrice);
			}
		}
		return itemPrices;
	}


}
