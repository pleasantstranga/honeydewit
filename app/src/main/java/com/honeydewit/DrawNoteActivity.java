package com.honeydewit;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.honeydewit.calendar.DateUtil;
import com.honeydewit.customviews.DrawingView;
import com.honeydewit.pojos.ListItem;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.concurrent.Callable;

/**
 * DrawNoteActivity
 */
public class DrawNoteActivity extends OptionsMenuActivity implements OnClickListener {

	//custom drawing view
	private DrawingView drawView;
	//buttons
	private ImageButton currPaint;

	//sizes
	private float xsBrush,smallBrush, mediumBrush, largeBrush;
	private ListItem listItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawable_screen);
		//String imageName = null;
		//get drawing view
		drawView = (DrawingView)findViewById(R.id.drawing);
		listItem = new ListItem();
		if(getIntent() != null && getIntent().getExtras() != null) {
			if(getIntent().getExtras().containsKey("itemId")) {
				int id = getIntent().getIntExtra("itemId", -1);		
				listItem = getApplicationContext().getCurrentList().getItemById(id);
			}
		}
		if(listItem != null && listItem.getImageName() != null) {
			drawView.setImageBackground(listItem.getImageName());
		}

		//get the palette and first color button
		LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_shader);
		currPaint = (ImageButton)paintLayout.getChildAt(0);
		currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

		//sizes from dimensions
		xsBrush = getResources().getInteger(R.integer.xsmall_size);
		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);

		//draw button
		ImageButton drawBtn = (ImageButton) findViewById(R.id.draw_btn);
		drawBtn.setOnClickListener(this);

		//set initial size
		drawView.setBrushSize(xsBrush);

		//erase button
		ImageButton eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
		eraseBtn.setOnClickListener(this);

		Button saveButton = (Button) findViewById(R.id.saveBtn);
		saveButton.setOnClickListener(this);

		Button clearButton = (Button)findViewById(R.id.clearBtn);
		clearButton.setOnClickListener(this);

		Button cancelButton = (Button)findViewById(R.id.cancelBtn);
		cancelButton.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_draw_note_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		int itemId = item.getItemId();
	    
	        if(itemId ==  R.id.optionCancelButton) {
	        	showCancelMessageDialog(R.string.areYouSure,listItem.getRowNumber());
	        }
	        else if(itemId == R.id.optionSaveButton) {
	        	save();
	        }
			else if(itemId == R.id.clearButton) {
				clearDrawing();
			}
	    
	    return true;
	}
	//user clicked paint
	public void paintClicked(View view){
		//use chosen color

		//set erase false
		drawView.setErase(false);
		drawView.setBrushSize(drawView.getLastBrushSize());

		if(view!=currPaint){
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			drawView.setColor(color);
			//update ui
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
			currPaint=(ImageButton)view;
		}
	}

	@Override
	public void onClick(View view){

		if(view.getId()==R.id.draw_btn){
			//draw button clicked
			openBrushDialog("Brush Size:",false);
		}
		else if(view.getId()==R.id.erase_btn){
			//switch to erase - choose size
			openBrushDialog("Eraser Size:", true);
		}
		else if(view.getId()==R.id.clearBtn){
			clearDrawing();
		}
		else if(view.getId() == R.id.saveBtn) {
			save();
		}
		else if(view.getId() == R.id.cancelBtn) {
			showCancelMessageDialog(R.string.confirmCancel, listItem.getRowNumber());
		}

	}

	private void clearDrawing() {
		//new button
		final HoneyDewDialogFragment newDialog = new HoneyDewDialogFragment();
		newDialog.show(getSupportFragmentManager(),"newDrawing");
		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		newDialog.setTitle("Clear Drawing?");
		newDialog.setPositiveButton(getText(R.string.yes).toString(), new OnClickListener() {
            public void onClick(View view) {
                drawView.startNew();
                newDialog.dismiss();
            }
        });
		newDialog.setNegativeButton(getText(R.string.cancel).toString(), new OnClickListener() {
            public void onClick(View view){
                newDialog.dismiss();
            }
        });
	}

	private void openBrushDialog(String title, final boolean isErase) {
		final Dialog brushDialog = new Dialog(this);
		brushDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		brushDialog.setContentView(R.layout.brush_chooser);


		((TextView)brushDialog.findViewById(R.id.brushTitle)).setText(title);

		ImageButton xsmallBtn = (ImageButton)brushDialog.findViewById(R.id.extra_small_brush);
		xsmallBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				drawView.setErase(isErase);
				drawView.setBrushSize(xsBrush);
				brushDialog.dismiss();
			}
		});
		ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
		smallBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				drawView.setErase(isErase);
				drawView.setBrushSize(smallBrush);
				brushDialog.dismiss();
			}
		});
		ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
		mediumBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				drawView.setErase(isErase);
				drawView.setBrushSize(mediumBrush);
				brushDialog.dismiss();
			}
		});
		ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
		largeBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				drawView.setErase(isErase);
				drawView.setBrushSize(largeBrush);
				brushDialog.dismiss();
			}
		});
		brushDialog.show();
	}

	private Integer save() {
		drawView.setDrawingCacheEnabled(true);	
	
		String directory = getApplicationContext().getImagesDirectory();
		String filename = DateUtil.tempFileNameFormat.format(Calendar.getInstance().getTime()) + ".jpeg";
		File imageFile = new File(directory + filename);
		
		Bitmap bitmap = drawView.getDrawingCache();
		
		
		try {
		     FileOutputStream out = new FileOutputStream(imageFile);
		     bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
		     
		     out.flush();
		     out.close();
		     
		} catch (Exception e) {
		     e.printStackTrace();
			 Toast.makeText(getApplicationContext(), "There was an error saving the image.", Toast.LENGTH_SHORT).show();

		}
		//feedback
		if(imageFile!=null){
			Toast.makeText(getApplicationContext(), "Drawing saved!", Toast.LENGTH_SHORT).show();
			bitmap.recycle();
			drawView.destroyDrawingCache();
			
			
			//save list item
			if(listItem == null) {
				listItem = new ListItem();
			}
			listItem.setName(filename);
			listItem.setImageName(filename);
			listItem.setList(getApplicationContext().getCurrentList());
			listItem.setIsDrawingImage(Constants.TRUE);


			getApplicationContext().getShoppingListDbHelper().addUpdateListItem(listItem);
			getApplicationContext().setCurrentItem(listItem);
			setResult(RESULT_OK, getIntent());

			finish();
		}
		return 1;
	}
	@Override
	public void onBackPressed() {
		showSaveOnBackButtonDialog(new Callable<Integer>() {
			public Integer call() {
				try {
					save();
					return 1;
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}

			}
		}, listItem.getRowNumber());
	}

 }
