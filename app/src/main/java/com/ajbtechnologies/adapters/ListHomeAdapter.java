package com.ajbtechnologies.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.BasicActivity;
import com.ajbtechnologies.Constants;
import com.ajbtechnologies.DialogFragment;
import com.ajbtechnologies.DrawNoteActivity;
import com.ajbtechnologies.ItemActivity;
import com.ajbtechnologies.ItemsPrice;
import com.ajbtechnologies.ListItemWatcher;
import com.ajbtechnologies.NotesActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.ToDoItemActivity;
import com.ajbtechnologies.listeners.OneOffClickListener;
import com.ajbtechnologies.pojos.ListItem;
import com.ajbtechnologies.utils.ImageUtil;
import com.ajbtechnologies.utils.NumberUtil;
import com.ajbtechnologies.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListHomeAdapter extends ArrayAdapter<ListItem> implements Serializable{

	private ListItemWatcher watcher;

	public ListHomeAdapter(Context context, int layoutId, List<ListItem> items) {
		super(context, R.layout.listrow, R.id.topRow, items);

	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View linearLayout = null;
		final ViewHolder vh;

		if(convertView==null){
			LayoutInflater li = ((BasicActivity)getContext()).getLayoutInflater();
			linearLayout = li.inflate(R.layout.listrow, null);
			vh = new ViewHolder();
			vh.name = (TextView) linearLayout.findViewById(R.id.topRow);
			vh.bottomRowTitle = (TextView) linearLayout.findViewById(R.id.bottomRowTitle);
			vh.bottomRowValue = (TextView) linearLayout.findViewById(R.id.bottomRow);
			vh.errorExists = (TextView)linearLayout.findViewById(R.id.errorExists);
			vh.mainLayout = (LinearLayout)linearLayout.findViewById(R.id.mainLayout);
			linearLayout.setTag(vh);
		}
		else{
			linearLayout= convertView;
			vh = (ViewHolder)linearLayout.getTag();
		}


		final ListItem item = getItem(position);


		if(watcher == null) {
			TextView numItemsText = (TextView) ((RelativeLayout)parent.getParent()).findViewById(R.id.numItems);
			TextView totalPriceText =  (TextView) ((RelativeLayout)parent.getParent()).findViewById(R.id.totalPrice);
			watcher = new ListItemWatcher(initaliseItemsPrice(), numItemsText, totalPriceText);
		}

		if (item != null) {

			initImage(item, (ViewGroup) linearLayout);
			vh.name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			vh.name.setFocusable(true);
			vh.name.setFocusableInTouchMode(true);
			vh.name.setSingleLine();
			vh.name.setMarqueeRepeatLimit(-1);
			vh.name.setSelected(true);
			vh.name.requestFocus();
			vh.name.setText(item.getName());


            if (item.getList().getListTypeId() == Constants.SHOPPING_LIST_TYPE_CDE) {
                vh.bottomRowTitle.setText(R.string.numItemsUnique);
				vh.bottomRowValue.setText(NumberUtil.returnNumberString(item.getQuantity()));

            } else if (item.getList().getListTypeId() == Constants.NOTES_LIST_TYP_CODE) {

				if (!StringUtils.isEmpty(item.getDescription())) {
					vh.bottomRowTitle.setText(R.string.descriptionTxt);
					vh.bottomRowValue.setText(item.getDescription().trim());
					vh.bottomRowValue.setEllipsize(TextUtils.TruncateAt.MARQUEE);
					vh.bottomRowValue.setFocusable(true);
					vh.bottomRowValue.setFocusableInTouchMode(true);
					vh.bottomRowValue.setSingleLine();
					vh.bottomRowValue.setMarqueeRepeatLimit(-1);
					vh.bottomRowValue.setSelected(true);
					vh.bottomRowValue.requestFocus();
                } else {
					vh.bottomRowTitle.setText(R.string.descriptionTxt);
					vh.bottomRowValue.setText("");
				}
			} else if (item.getList().getListTypeId() == Constants.TODO_LIST_TYPE_CDE) {
                vh.bottomRowTitle.setText(R.string.descriptionTxt);
				vh.bottomRowValue.setText(item.getDescription().trim());
				vh.bottomRowValue.setEllipsize(TextUtils.TruncateAt.MARQUEE);
				vh.bottomRowValue.setFocusable(true);
				vh.bottomRowValue.setFocusableInTouchMode(true);
				vh.bottomRowValue.setSingleLine();
				vh.bottomRowValue.setMarqueeRepeatLimit(-1);
				vh.bottomRowValue.setSelected(true);
				vh.bottomRowValue.requestFocus();
			}
			if(item.hasErrors()) {
				vh.errorExists.setVisibility(View.VISIBLE);
				vh.errorExists.setText(getContext().getResources().getText(R.string.errorExists));
				vh.errorExists.setTextColor(getContext().getResources().getColor(R.color.error_text_color));
				vh.errorExists.setTextSize(12);
			}
			else {
				vh.errorExists.setVisibility(View.GONE);
			}
		}
		vh.mainLayout.setOnClickListener(new OneOffClickListener() {


			@Override
			public void onClick(View v) {
				Intent newListIntent = null;
                if (item.getList().getListTypeId() == Constants.SHOPPING_LIST_TYPE_CDE) {
                    newListIntent = new Intent(getContext(), ItemActivity.class);
				} else if (item.getList().getListTypeId() == Constants.TODO_LIST_TYPE_CDE) {
                    newListIntent = new Intent(getContext(), ToDoItemActivity.class);
				} else if (item.getList().getListTypeId() == Constants.NOTES_LIST_TYP_CODE) {
                    if(item.getImageName() == null) {
						newListIntent = new Intent(getContext(), NotesActivity.class);
					}
					else {
						newListIntent = new Intent(getContext(), DrawNoteActivity.class);

					}
				}
				newListIntent.putExtra("itemId", item.get_id());
				((BasicActivity)getContext()).startActivityForResult(newListIntent, Constants.ITEM_TYPE_REQUEST);

			}
		});
		ImageButton deleteButton = (ImageButton) linearLayout.findViewById(R.id.deleteButton);
		deleteButton.setOnClickListener(new OneOffClickListener() {


			@Override
			public void onClick(View v) {
				final DialogFragment dialog = new DialogFragment();
				dialog.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "delete");
				((FragmentActivity)getContext()).getSupportFragmentManager().executePendingTransactions();

				dialog.setTitle(getContext().getText(R.string.delete).toString());
				dialog.setMessage(getContext().getText(R.string.deleteItemWarning).toString());
				dialog.setPositiveButton(getContext().getText(R.string.ok).toString(), new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						((Application)getContext().getApplicationContext()).getShoppingListDbHelper().deleteListItem(item.get_id());
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
				changeBackgroundOnChecked(checkBox, parent, (TextView) parent.findViewById(R.id.topRow), (TextView) parent.findViewById(R.id.bottomRow));
				item.setChecked(checkBox.isChecked() ? Constants.TRUE : Constants.FALSE);
				((Application)getContext().getApplicationContext()).getShoppingListDbHelper().addUpdateListItem(item);
				watcher.setChecked(item);
			}

		});
		checkBox.setChecked(item.getChecked() == 1);
		changeBackgroundOnChecked(checkBox, linearLayout, vh.name, vh.bottomRowValue);


		return linearLayout;
	}

	public void initImage(final ListItem item, ViewGroup parent) {
		ImageView imageView = (ImageView) parent.findViewById(R.id.noteImage);
		if(item.getImageName() == null || item.getImageName().trim().length() == 0) {
			imageView.setVisibility(View.GONE);
		}
		else {
			if(item.getImageName() != null && item.getImageName().trim().length() > 0) {

				String filePath = ((Application)getContext().getApplicationContext()).getImagesDirectory() + item.getImageName();
				Bitmap bm = ImageUtil.getThumbnailBitmap(filePath);
				if(bm == null) {
					bm = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.nophoto, null), 26, 26);
				}
				if(bm != null) {
					imageView.setImageBitmap(bm);
					imageView.setVisibility(View.VISIBLE);
				}
			}
			else {
				imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.nophoto));
				imageView.setVisibility(View.VISIBLE);

			}

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

	private class ViewHolder {
		TextView name;
		TextView bottomRowTitle;
		TextView bottomRowValue;
		TextView errorExists;
		LinearLayout mainLayout;
	}


}
