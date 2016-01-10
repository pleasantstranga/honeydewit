package com.honeydewit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.honeydewit.adapters.BitmapWorkerTask;
import com.honeydewit.calculators.CalculateActivity;
import com.honeydewit.calendar.DateUtil;
import com.honeydewit.pojos.Links;
import com.honeydewit.pojos.ListItem;
import com.honeydewit.utils.StringUtils;
import com.honeydewit.validate.BaseImportValidator;
import com.honeydewit.validate.BaseValidator;
import com.honeydewit.validate.ListItemValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;


public class ItemActivity extends OptionsMenuActivity implements OnClickListener {
	private String valueFromCalculator;
	private Button saveButton;
	private Button cancelButton;
	private Button cameraButton;
    private ImageButton calculateQtyButton;
	private ImageButton calculateUnitPriceBtn;
	private ImageButton cameraImage;
	private EditText quantity;
	private EditText unitPrice;
	private EditText itemName;
	private EditText discount;
	private EditText unitType;
	private ListItem listItem;
	private EditText description;
	private BaseValidator listItemValidator;
	private String tempImageName = null;
	private String calcType = null;
	static final int REQUEST_IMAGE_CAPTURE = 1234;
	private ListView errorListView;
	private ArrayAdapter<String> errorsAdapter = null;
	private List<String> errors = new ArrayList<>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item);

		cameraButton = (Button)findViewById(R.id.cameraButton);
		cameraImage = (ImageButton)findViewById(R.id.cameraImage);
		itemName = (EditText)findViewById(R.id.itemName);
		quantity = (EditText)findViewById(R.id.quantity);
		unitType = (EditText)findViewById(R.id.unitType);
		unitPrice = (EditText)findViewById(R.id.unitPrice);
		discount = (EditText)findViewById(R.id.discountAmt);
		description = (EditText)findViewById(R.id.description);
		saveButton = (Button) findViewById(R.id.saveBtn);
		saveButton.setOnClickListener(this);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		cancelButton.setOnClickListener(this);
		cameraButton.setOnClickListener(this);
		cameraImage.setOnClickListener(this);
		listItemValidator = new ListItemValidator(getApplicationContext());
		cameraButton.setVisibility(View.VISIBLE);
		cameraImage.setVisibility(View.GONE);
        calculateQtyButton = (ImageButton)findViewById(R.id.calculateQtyBtn);
        calculateQtyButton.setOnClickListener(this);
		calculateUnitPriceBtn = (ImageButton)findViewById(R.id.calculateUnitPriceBtn);
		calculateUnitPriceBtn.setOnClickListener(this);
		itemName.addTextChangedListener(new TitleTextWatcher(this));
		errorListView = (ListView)findViewById(R.id.errorList);


	}

	@Override
	public void onClick(View v) {
		if(v == saveButton) {
			save();
		}
		else if(v == cancelButton) {
			showCancelMessageDialog( R.string.areYouSure, listItem.getRowNumber());
		}
		else if(v == cameraButton) {
			tempImageName = DateUtil.tempFileNameFormat.format(new Date());
			dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE, tempImageName, Constants.JPEG);
		}
		else if(v == cameraImage) {
			showImageChoiceDialog();
		}
        else if(v == calculateQtyButton || v == calculateUnitPriceBtn) {

			if(v == calculateQtyButton) {
				getIntent().putExtra(Constants.CALC_TYPE,"Quantity");
				try {
					FragmentManager fm = getSupportFragmentManager();
					Bundle bundle = new Bundle();
					bundle.putString("title", getText(R.string.chooseCalculatorDialogTitle).toString().replaceFirst("\\^", "Quantity"));
					bundle.putString("message", getText(R.string.chooseCalculatorDialogMessage).toString().replaceFirst("\\^", "Quantity"));
					bundle.putInt("requestCode", Constants.REQUEST_CALCULATOR);
					List<Links> linksList = (List<Links>)getApplicationContext().getShoppingListDbHelper().getLinks(3, new Integer[]{11});
					SerializableArrayList<Links> links = new SerializableArrayList<Links>();
					for(Links link : linksList) {
						links.add(link);
					}
					bundle.putSerializable("links", links);

					HoneyDewRadioDialogFragment dialog = new HoneyDewRadioDialogFragment();


					dialog.setArguments(bundle);
					dialog.show(fm,"calculator");
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			else {
				getIntent().putExtra(Constants.CALC_TYPE, "Unit Price");
				Intent intent = new Intent(this, CalculateActivity.class);
				intent.putExtra("doSet",true);
				startActivityForResult(intent, Constants.REQUEST_CALCULATOR);
			}


        }
	}

	private void save() {
		populateListItem();

		if(listItemValidator.validate(this,listItem)) {
			boolean isUpdate = listItem.get_id() != null;

			int listId =getApplicationContext().getShoppingListDbHelper().addUpdateListItem(listItem);

			if(isUpdate) {
				getApplicationContext().getShoppingListDbHelper().deleteImportErrors(listItem);
			}

			getApplicationContext().setCurrentItem(listItem);
			getIntent().putExtra("isUpdate", isUpdate);
			setResult(RESULT_OK, getIntent());


			finish();
		}
		else {
			errors = listItemValidator.getErrorMessages();
			if(errorsAdapter == null) {
				errorsAdapter = new ArrayAdapter<String>(this, R.layout.error_layout,errors);
				errorListView.setAdapter(errorsAdapter);
			}
			else {

				errorsAdapter.notifyDataSetChanged();
			}

		}
	}

	private void populateListItem() {
		listItem.setList(getApplicationContext().getCurrentList());
		listItem.setName(itemName.getText().toString());
		listItem.setDescription(description.getText().toString());
		listItem.setUnitType(unitType.getText().toString());
		listItem.setPricePerUnit(unitPrice.getText().length() > 0 ? Double.parseDouble(unitPrice.getText().toString()) : 0);
		listItem.setQuantity(quantity.getText().length() > 0 ? Double.parseDouble(quantity.getText().toString()) : 0);
		listItem.setDiscountCoupon(discount.getText().length() > 0 ? Double.parseDouble(discount.getText().toString()) : 0);
	}
	private void initForm() {

		itemName.setText(listItem.getName());
		if(isQtyCalc()) {
			if(valueFromCalculator != null) {
				quantity.setText(valueFromCalculator);

			}
		}
		else  {
			quantity.setText(String.valueOf(listItem.getQuantity()));
		}
		if(isUnitPriceCalc()) {
			if(valueFromCalculator != null) {
				unitPrice.setText(valueFromCalculator);

			}
		}
		else {
			unitPrice.setText(String.valueOf(listItem.getPricePerUnit()));
		}
		getIntent().removeExtra(Constants.CALC_TYPE);
		unitType.setText(listItem.getUnitType());
		discount.setText(String.valueOf(listItem.getDiscountCoupon()));
		description.setText(listItem.getDescription());  
		
		if(!StringUtils.isEmpty(listItem.getImageName())) {

			if(new File(getApplicationContext().getImagesDirectory()+listItem.getImageName()).isFile()) {
				cameraImage.setVisibility(View.VISIBLE);
				cameraButton.setVisibility(View.GONE);
				try {
					BitmapWorkerTask bgTask = new BitmapWorkerTask(false,cameraImage);
					float maxWidth = (float)getResources().getDimensionPixelSize(R.dimen.max_listitemimage_height);
					int mDstWidth = getResources().getDimensionPixelSize(R.dimen.max_listitemimage_width);
					int mDstHeight = getResources().getDimensionPixelSize(R.dimen.max_listitemimage_height);

					String[] params = new String[4];
					params[0] = getApplicationContext().getImagesDirectory() + listItem.getImageName();
					params[1] = String.valueOf(maxWidth);
					params[2] = String.valueOf(mDstWidth);
					params[3] = String.valueOf(mDstHeight);
					bgTask.execute(params);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}

		}
		try {
			if(listItem.getErrors() != null && listItem.getErrors().size() > 0) {

				errorListView.setVisibility(View.VISIBLE);
				findViewById(R.id.rowTableRow).setVisibility(View.VISIBLE);
				((TextView)findViewById(R.id.rowNumber)).setText(listItem.getImportRow().toString());

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
						R.layout.error_layout, listItem.getErrorsAsStrings());

				errorListView.setAdapter(adapter);
			}
		}
		catch(RuntimeException e) {
			e.printStackTrace();
			Toast.makeText(this, "There was an error with your request. Please try again.", Toast.LENGTH_LONG).show();
		}


	}
	private void initialiseListItem() {
		if(null == listItem) {
			if(null != getIntent().getExtras() && getIntent().getExtras().containsKey("itemId")) { 
				int id = getIntent().getIntExtra("itemId", -1);
				listItem =  getApplicationContext().getCurrentList().getItemById(id);
			}
	
			else {
				listItem = new ListItem();
				listItem.setQuantity(1D);
				quantity.setText(listItem.getQuantity().toString());
				listItem.setList(getApplicationContext().getCurrentList());
			}
		}
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

			showCancelMessageDialog(R.string.areYouSure, listItem.getRowNumber());
			return true;
		}
		return false;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE) {
			if(resultCode == RESULT_OK) {
				try {
					if(getIntent().hasExtra("imageName")) {
						cameraImage.setVisibility(View.VISIBLE);
						cameraButton.setVisibility(View.GONE);

						listItem.setImageName(getIntent().getStringExtra("imageName"));

						BitmapWorkerTask bgTask = new BitmapWorkerTask(true,cameraImage);
						float maxWidth = (float)getResources().getDimensionPixelSize(R.dimen.max_listitemimage_height);
						String[] params = new String[] {getApplicationContext().getImagesDirectory() + listItem.getImageName(),String.valueOf(maxWidth)};
						bgTask.execute(params);
					}
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
			else {
				tempImageName = null;
				Toast.makeText(this, getText(R.string.imageCancelled), Toast.LENGTH_LONG).show();
			}
		}
		else if(requestCode == Constants.REQUEST_CALCULATOR) {
			if(resultCode == RESULT_OK) {
				valueFromCalculator = data.getStringExtra("value");
			}
			else {
				valueFromCalculator = null;
			}
		}
		else {
			System.out.println("Error");
			Log.d(ItemActivity.class.getName(), "OnActivityResult");
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		populateListItem();
		outState.putString("tempImageName", tempImageName);
		outState.putSerializable("listItem", listItem);
		outState.putSerializable(Constants.CALC_TYPE, getIntent().getStringExtra(Constants.CALC_TYPE));

	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onRestoreInstanceState(savedInstanceState);
		listItem = (ListItem)savedInstanceState.get("listItem");
		tempImageName = savedInstanceState.getString("tempImageName");
		getIntent().putExtra(Constants.CALC_TYPE, savedInstanceState.getString(Constants.CALC_TYPE));
		calcType = savedInstanceState.getString(Constants.CALC_TYPE);
	}




	public void showImageChoiceDialog() {
		final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(getSupportFragmentManager(), "honeyDewDialog");
		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		dialog.setMessage(getText(R.string.imageChoice).toString());
		dialog.setPositiveButton(getText(R.string.replace).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				tempImageName = DateUtil.tempFileNameFormat.format(new Date());
				dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE, tempImageName, Constants.JPEG);
				dialog.dismiss();
			}
		});


		dialog.setNeutralButton(getText(R.string.delete).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.dismiss();
				listItem.setImageName(null);
				cameraImage.setVisibility(View.GONE);
				cameraButton.setVisibility(View.VISIBLE);
			}
		});
		dialog.setNegativeButton(getText(R.string.cancel).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

	}  
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initialiseListItem();
		initForm();
	}
    public void showErrors(BaseImportValidator validator) {
        final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(getSupportFragmentManager(),"errors");

		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		StringBuilder string = new StringBuilder();
        for(String errorMessage : validator.getErrorMessages().get(listItem.getList().getListName())) {
            string.append("* " + errorMessage + "\n");
		}
		dialog.setTitle(getText(R.string.error).toString());
        dialog.setMessage(string.toString());
		dialog.setNeutralButton("Ok", new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.dismiss();

			}
		});
        validator.getErrorMessages().clear();

    }
	public boolean isQtyCalc() {
		if(getIntent().hasExtra(Constants.CALC_TYPE)) {
			if(getIntent().getExtras().get(Constants.CALC_TYPE) != null) {
				calcType =  getIntent().getStringExtra(Constants.CALC_TYPE);
			}
		}
		return calcType != null && calcType.equals("Quantity");

	}
	public boolean isUnitPriceCalc() {
		if(getIntent().hasExtra(Constants.CALC_TYPE)) {
			if(getIntent().getExtras().get(Constants.CALC_TYPE) != null) {
				calcType = getIntent().getStringExtra(Constants.CALC_TYPE);
			}
		}
		return calcType != null && calcType.equals("Unit Price");
	}

}
