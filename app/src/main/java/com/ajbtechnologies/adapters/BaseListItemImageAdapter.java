package com.ajbtechnologies.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.R;
import com.ajbtechnologies.pojos.ListItem;
import com.ajbtechnologies.utils.ImageUtil;

import java.util.List;

public abstract class BaseListItemImageAdapter extends ArrayAdapter<ListItem> {



	private List<ListItem> items;

	public BaseListItemImageAdapter(Context context, int layoutId, List<ListItem> items) {
		super(context, layoutId, items);
		this.items = items;
	}

	public void initImage(final ListItem item, ViewGroup linearLayout) {
		ImageView imageView = (ImageView) linearLayout.findViewById(R.id.noteImage);
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
				imageView.setImageBitmap(bm);
			}
			else {
				imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.nophoto));
			}
			imageView.setVisibility(View.VISIBLE);
		}


	}

	public void changeBackgroundOnChecked(final CheckBox checked, ViewGroup parent, TextView tt, TextView quantity) {
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
				quantity.setPaintFlags( quantity.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));

			}
		}
	}
	public List<ListItem> getItems() {
		return items;
	}


}