package com.honeydewit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.honeydewit.adapters.LinksButtonGridAdapter;
import com.honeydewit.pojos.Links;

import java.util.List;


public class CreateEditListsActivity extends BasicActivity {
    

    @Override
    public void onCreate(Bundle savedInstanceState) {

    		 super.onCreate(savedInstanceState);
				getActionBar().hide();
    	        setContentView(R.layout.main);
    	        ((TextView)findViewById(R.id.headerTxt)).setText(R.string.app_name);
    	        getApplicationContext().loadBannerAd((AdView)findViewById(R.id.adView));
    	        GridView linksView =  (GridView) findViewById(R.id.linksGrid);
    	        List<Links> links = getApplicationContext().getShoppingListDbHelper().getLinks(2);
				linksView.setAdapter(new LinksButtonGridAdapter(this, links));

    
    }
    public void onBackPressed() {

		getApplicationContext().setCurrentList(null);
		Intent intent = new Intent(this, MainMenuActivity.class);
		startActivity(intent);
		super.onBackPressed();
    	
    }
   

}