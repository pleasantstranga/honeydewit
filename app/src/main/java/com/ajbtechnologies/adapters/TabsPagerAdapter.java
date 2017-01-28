package com.ajbtechnologies.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ajbtechnologies.pojos.Links;

import java.util.List;

public class TabsPagerAdapter extends FragmentPagerAdapter {
	List<Links> links;

	public TabsPagerAdapter(FragmentManager fm, List<Links> links) {
		super(fm);
		this.links = links;
	}

	@Override
	public Fragment getItem(int index) {
		Fragment fragment = null;
		Links link = links.get(index);
		try {
			fragment =  getFragment(link);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return links.size();
	}
	private Fragment getFragment(Links link) throws Exception {

			Class fragmentClass = null;

			fragmentClass = Class.forName(link.getIntent());


			if (null != fragmentClass) {
				return (Fragment)fragmentClass.newInstance();
			}
			return null;
	}

}
