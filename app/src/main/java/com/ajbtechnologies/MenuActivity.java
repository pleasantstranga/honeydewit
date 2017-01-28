package com.ajbtechnologies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

import com.ajbtechnologies.adapters.LinksButtonGridAdapter;
import com.ajbtechnologies.pojos.Links;

import java.util.List;

@SuppressLint("Registered")
public class MenuActivity extends BasicActivity {
	
	private GridView linksView;
	private List<Links> links;

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		// TODO Auto-generated method stub
		return super.onCreateView(parent, name, context, attrs);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		linksView =  (GridView) findViewById(R.id.linksGrid);
		linksView.setAdapter(new LinksButtonGridAdapter(this, links));
		super.onCreate(savedInstanceState);

	}

	public void setLinks(List<Links> links) {
		this.links = links;
	}


}
