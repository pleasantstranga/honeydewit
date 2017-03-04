package com.ajbtechnologies;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ajbtechnologies.adapters.BitmapWorkerTask;
import com.ajbtechnologies.calendar.DateUtil;
import com.ajbtechnologies.pojos.ListItem;

import java.util.Calendar;


public class NoteImageActivity extends OptionsMenuActivity implements View.OnClickListener {
    private static final int REQUEST_CAPTURE_IMAGE = 12344;
    EditText description = null;
    ImageButton imageButton;
    ListItem listItem = null;
    Button takePicButton = null;
    Button saveButton = null;
    Button cancelButton = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_image);

        imageButton = (ImageButton)findViewById(R.id.imageButton);
        description = (EditText)findViewById(R.id.description);
        takePicButton = (Button)findViewById(R.id.takePictureButton);
        cancelButton = (Button)findViewById(R.id.cancelBtn);
        saveButton = (Button)findViewById(R.id.saveBtn);

        takePicButton.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        if(getIntent().hasExtra("itemId")) {
            listItem = getApplicationContext().getShoppingListDbHelper().getListItemById(getIntent().getIntExtra("itemId",-1));
            description.setText(listItem.getName());
            if(listItem.getImageName() != null) {
                try {
                    imageButton.setVisibility(View.VISIBLE);
                    imageButton.setImageBitmap(BitmapFactory.decodeFile(getApplicationContext().getImagesDirectory() + listItem.getImageName()));

                }
                catch(OutOfMemoryError ooom) {
                    ooom.printStackTrace();
                    Toast.makeText(this, "There is not enough memory to view image", Toast.LENGTH_LONG).show();
                    finish();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            listItem = new ListItem();
            imageButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == takePicButton) {
            dispatchTakePictureIntent(REQUEST_CAPTURE_IMAGE,DateUtil.tempFileNameFormat.format(Calendar.getInstance().getTime()),Constants.JPEG );
        }
        else if(v == saveButton) {
            save();
        }
        else if(v == cancelButton) {
            showCancelMessageDialog(R.string.areYouSure, listItem.getRowNumber());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAPTURE_IMAGE) {
            if(resultCode == RESULT_OK) {
                try {
                    String tempImageName = getIntent().getStringExtra("imageName");
                    listItem.setImageName(tempImageName);
                    listItem.setList(getApplicationContext().getCurrentList());
                    listItem.setIsCameraImage(Constants.TRUE);

                    if(description.getText().length() == 0) {
                        listItem.setName(tempImageName);
                        description.setText(listItem.getName());
                    }

                    imageButton.setVisibility(View.VISIBLE);
                    BitmapWorkerTask bgTask = new BitmapWorkerTask(true, imageButton);


                    float maxWidth = (float)getResources().getDimensionPixelSize(R.dimen.max_noteimage_width);
                    String[] params = new String[] {getApplicationContext().getImagesDirectory() + listItem.getImageName(),String.valueOf(maxWidth)};
                    bgTask.execute(params);

                }
                catch(OutOfMemoryError ooom) {
                    ooom.printStackTrace();
                    Toast.makeText(this, "There is not enough memory to view image", Toast.LENGTH_LONG).show();
                    finish();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public void save() {
        if(validate()) {

            boolean isUpdate = listItem.isUpdate();
            getApplicationContext().getShoppingListDbHelper().addUpdateListItem(listItem);
            getIntent().putExtra("isUpdate", isUpdate);
            getApplicationContext().setCurrentItem(listItem);
            setResult(RESULT_OK, getIntent());

            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_save_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // Handle presses on the action bar items
        int itemId = item.getItemId();

        if(itemId == R.id.optionSaveButton) {
            save();
            return true;
        }
        else if(itemId == R.id.optionCancelButton) {
            Integer rowNumber = listItem != null ? listItem.getRowNumber() : null;
            showCancelMessageDialog(R.string.areYouSure, rowNumber);
            return true;
        }
        return false;
    }
    public boolean validate() {
        boolean isValid = false;
        if(description.getText().length() > 0) {
            if(description.getText().length() < 3) {
                showError(R.string.descriptionLengthError);
            }
            else {
                isValid = true;
                listItem.setName(description.getText().toString());
            }
        }
        else {
            isValid = true;
        }
        return isValid;
    }
}
