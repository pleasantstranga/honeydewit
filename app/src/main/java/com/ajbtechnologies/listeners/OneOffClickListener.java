package com.ajbtechnologies.listeners;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

/** 
 * This class allows a single click and prevents multiple clicks on
 * the same button in rapid succession. Setting unclickable is not enough
 * because click events may still be queued up.
 * 
 * Override onOneClick() to handle single clicks. Call reset() when you want to
 * accept another click.
 */
public class OneOffClickListener implements OnClickListener {
    
    /**
     * Override onOneClick() instead.
     */
    @Override
    public void onClick(final View v) {
 
    	v.setClickable(false);
        v.setEnabled(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                v.setEnabled(true);
                v.setClickable(true);
            }
        }, 2000);
    }
}