package com.honeydewit;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.honeydewit.errorhandling.ExceptionHandler;
import com.honeydewit.utils.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressLint("Registered")
public class BasicActivity extends FragmentActivity {
	

	public static Resources resource;
	public static final int ACTIVITY_OK = 0;
	public static final int ACTIVITY_CANCEL = 1;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		resource = getResources();
		showOnLoaddError();



		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		
	}
	
	@Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = super.onCreateView(parent, name, context, attrs);


        return formatText(name, attrs, view);

    }
	
	public void showError(int errorStringId) {
		final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(getSupportFragmentManager(), "error");
		boolean isExec = getSupportFragmentManager().executePendingTransactions();
		dialog.setMessage(getText(errorStringId).toString());
		dialog.setTitle(getText(R.string.error).toString());
		dialog.setPositiveButton(getBaseContext().getText(R.string.ok).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});
	}
	public void showError(int errorStringId, final Callable<Integer> okButtonMethod) {
		final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(getSupportFragmentManager(), "error");
		boolean isPend = getSupportFragmentManager().executePendingTransactions();
		dialog.setMessage(getText(errorStringId).toString());
		dialog.setTitle(getText(R.string.error).toString());
		dialog.setPositiveButton(getBaseContext().getText(R.string.ok).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				try {
					okButtonMethod.call();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
        
       
	}


	public void showErrors(List<String> errorMessages) {
		final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(getSupportFragmentManager(), "error");
		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		StringBuilder string = new StringBuilder();
		for(String errorMessage : errorMessages) {
			string.append("* " + errorMessage + "\n");
		}
		dialog.setTitle(getText(R.string.error).toString());
		dialog.setMessage(string.toString());
		dialog.setPositiveButton(getText(R.string.ok).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.dismiss();

			}
		});
        errorMessages.clear();

	}
	public void showCancelMessageDialog(int messageId, final Integer arrayAdapterPositionToReturnTo) {

		final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(getSupportFragmentManager(), "cancelMessage");
		boolean isPend = getSupportFragmentManager().executePendingTransactions();
		dialog.setMessage(getText(messageId).toString());
		dialog.setTitle(getText(R.string.cancel).toString());
		dialog.setPositiveButton(getText(R.string.yes).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				if (null != arrayAdapterPositionToReturnTo) {
					intent.putExtra(Constants.POSITION, arrayAdapterPositionToReturnTo);
				}
				setResult(RESULT_CANCELED, intent);
					finish();
				dialog.dismiss();
			}
		});
		dialog.setNegativeButton(getText(R.string.no).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}

	@Override
	public HoneyDewApplication getApplicationContext() {
		// TODO Auto-generated method stub
		return (HoneyDewApplication) super.getApplicationContext();
	}
	public final View formatText(String name, AttributeSet attrs, View view) {

		 	if (name.contains("ActionMenuItemView")) {
		 		if(view != null) {
		 			  ((TextView) view).setTypeface(getApplicationContext().getTypeface(),Typeface.BOLD);
			            ((TextView) view).setTextSize(getResources().getDimension(R.dimen.default_text_size));
		 		}

	        }
	        if ("TextView".equals(name)) {
	            view = new TextView(this, attrs);
	            ((TextView) view).setTypeface(getApplicationContext().getTypeface());
				if(view.getId() == R.id.headerTxt) {
					((TextView) view).setTextSize(getResources().getDimension(R.dimen.default_header_size));
				}
	        }

	        if ("EditText".equals(name)) {
	            view = new EditText(this, attrs);
	            ((EditText) view).setTypeface(getApplicationContext().getTypeface());
	        }

	        if ("Button".equals(name)) {
	            view = new Button(this, attrs);
	            ((Button) view).setTypeface(getApplicationContext().getTypeface());

	        }
	       if("TableRow".equals(name)) {
			   view = new TableRow(this,attrs);
			   TableLayout.LayoutParams tableRowParams=
					   new TableLayout.LayoutParams
							   (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);


			   tableRowParams.setMargins(0, 0, 0, 10);
			   view.setLayoutParams(tableRowParams);
		   }
	       return view;
	}
	public void showSaveOnBackButtonDialog(final Context context, final Callable<Integer> saveMethod) {
		showSaveOnBackButtonDialog(saveMethod, null);
	}
	public void showSaveOnBackButtonDialog(final Callable<Integer> saveMethod, final Integer arrayAdapterPositionToReturnTo) {
		
		final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(getSupportFragmentManager(),"error");
		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		dialog.setTitle(getText(R.string.saveTitle).toString());
		dialog.setMessage(getText(R.string.saveOnBack).toString());
		dialog.setPositiveButton(getText(R.string.save).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				try {
					saveMethod.call();
					Intent intent = getIntent();
					if (null != arrayAdapterPositionToReturnTo) {
						intent.putExtra(Constants.POSITION, arrayAdapterPositionToReturnTo);
					}
					setResult(RESULT_OK, intent);
					dialog.dismiss();
					finish();
				} catch (Exception e) {
						e.printStackTrace();
				}

			}
		});
		dialog.setNegativeButton(getText(R.string.dontSave).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = getIntent();
				if (null != arrayAdapterPositionToReturnTo) {
					intent.putExtra(Constants.POSITION, arrayAdapterPositionToReturnTo);
				}
				setResult(RESULT_CANCELED, intent);
				dialog.dismiss();
				finish();
			}
		});


	}
	public void showOnLoaddError() {
 		Intent intent = getIntent();
		int redirectErrorId = intent.getIntExtra("redirectError", -1);
		if(redirectErrorId != -1) {
			showError(redirectErrorId);
		}
	}
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

	public void dispatchTakePictureIntent(int requestCode, String fileName, String fileNameExtension) {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {

				photoFile = ImageUtil.createTempImageFile(getApplicationContext().getImagesDirectory(), fileName, fileNameExtension);
			} catch (IOException ex) {
				// Error occurred while creating the File
				ex.printStackTrace();
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(photoFile));
				takePictureIntent.putExtra("imageName", photoFile.getName());
				getIntent().putExtra("imageName", photoFile.getName());
				startActivityForResult(takePictureIntent, requestCode);
			}
		}

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();


	}
}
