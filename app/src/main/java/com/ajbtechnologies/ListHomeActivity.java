package com.ajbtechnologies;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.ajbtechnologies.calendar.CalendarActivity;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.utils.EmailUtil;
import com.ajbtechnologies.validate.BaseValidator;
import com.ajbtechnologies.validate.ListNameValidator;

public class ListHomeActivity extends BasicActivity {
	public boolean isShowImportErrorDialog = false;
	private ListHomeFragment fragment;
	private static final int RENAME_LIST = 3245;
	private static final int ADD_REMINDER_REQ = 345;
	private ActionBarDrawerToggle mDrawerToggle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_drawer_base_layout);

		fragment = new ListHomeFragment();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		initDrawer();
	}


	@Override
	public void setTitle(CharSequence title) {
		if(getApplicationContext().getCurrentList() != null) {
			getActionBar().setTitle(getApplicationContext().getCurrentList().getListName());
		}
	}

	private void openNoteDrawableScreen(BasicList list) {
		if(getApplicationContext().getCurrentList().getListTypeId() == Constants.NOTES_LIST_TYP_CODE) {
			Intent newListIntent = new Intent(getBaseContext(), DrawNoteActivity.class);
			startActivityForResult(newListIntent, Constants.REQUEST_DRAW_NOTE);
		}
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent mainIntent = null;
		if(null == getApplicationContext().getCurrentList() || getApplicationContext().getCurrentList().getListTypeId() == Constants.NOTES_LIST_TYP_CODE) {
			mainIntent = new Intent(this, MainMenuActivity.class);
		}
		else {
			mainIntent = new Intent(this, ListsHomeActivity.class);
			mainIntent.putExtra(Constants.SUB_CAT_CODE, getApplicationContext().getCurrentList().getListTypeId());
		}
		getApplicationContext().setCurrentList(null);
		startActivity(mainIntent);
		finish();
	}


	public void showRenameListDialog() {

		final DialogFragment dialog = new DialogFragment();
		dialog.show(getSupportFragmentManager(), "renameList");
		boolean isExec = getSupportFragmentManager().executePendingTransactions();
		dialog.setTitle(getText(R.string.renameList).toString());
		dialog.setEditText("Enter list name", getApplicationContext().getCurrentList().getListName());
		dialog.setMessage("");
		dialog.setPositiveButton(getBaseContext().getText(R.string.ok).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseValidator validator = new ListNameValidator();
				if(dialog.getEditTextValue().equals(getApplicationContext().getCurrentList().getListName())) {
					dialog.dismiss();
				}
				else if (validator.validate(getBaseContext(), dialog.getEditTextValue())) {
					getApplicationContext().getCurrentList().setListName(dialog.getEditTextValue());
					getApplicationContext().getShoppingListDbHelper().addUpdateShoppingList(getApplicationContext().getCurrentList());
					setTitle(getApplicationContext().getCurrentList().getListName());
					dialog.dismiss();
				} else {
					dialog.setMessage(validator.getErrorMessages().get(0));
					dialog.getMessage().setTextColor(getResources().getColor(R.color.red));
				}

			}
		});
		dialog.setNegativeButton(getBaseContext().getText(R.string.cancel).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});

	}

	private void initDrawer() {


		final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, getResources().getStringArray(R.array.list_home_drawer_menu_array)));
		mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mDrawerLayout.closeDrawer(Gravity.LEFT);
				String item = getResources().getStringArray(R.array.list_home_drawer_menu_array)[position];
				if (item.equals(getResources().getString(R.string.renameList))) {
					showRenameListDialog();

				} else if (item.equals(getResources().getString(R.string.addReminder))) {
					Intent intent = new Intent(getBaseContext(), CalendarActivity.class);
					startActivityForResult(intent, ADD_REMINDER_REQ);
				} else if (item.equals(getResources().getString(R.string.shareList))) {
					EmailUtil.shareList(getApplicationContext(), getApplicationContext().getCurrentList());
				} else if (item.equals(getResources().getString(R.string.deleteList))) {

				} else if (item.equals(getResources().getString(R.string.selectAll))) {

				} else if (item.equals(getResources().getString(R.string.unselectAll))) {

				} else if (item.equals(getResources().getString(R.string.addItem))) {
					Intent intent = new Intent(getBaseContext(), ItemActivity.class);
					startActivityForResult(intent, 0);
				}
			}
		});

		// enable ActionBar app icon to behave as action to toggle nav drawer


		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.open_drawer,  /* "open drawer" description for accessibility */
				R.string.close_drawer  /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_drawer));
		getActionBar().setHomeButtonEnabled(true);

	}
	@Override
	protected void onResume() {
		super.onResume();


		if(isShowImportErrorDialog && getApplicationContext().getCurrentList().isShowErrorDialogInd() && getApplicationContext().getCurrentList().getItemsWithErrors().size() > 0) {
			showImportErrorDialog();
		}

	}

	public void showImportErrorDialog() {


		Bundle bundle = new Bundle();
		bundle.putString("title", "Error Importing");
		bundle.putString("message", getText(R.string.errorImportingFix).toString());
		bundle.putInt("requestCode", Constants.REQUEST_CALCULATOR);

		final DialogFragment dialog = new DialogFragment();
		dialog.show(getSupportFragmentManager(), "error");
		dialog.setArguments(bundle);

		boolean isExec = getSupportFragmentManager().executePendingTransactions();


		dialog.setCheckbox(getText(R.string.dontAskAgain).toString(), new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					getApplicationContext().getCurrentList().setIsShowErrorDialogInd(false);
				} else {
					getApplicationContext().getCurrentList().setIsShowErrorDialogInd(true);
				}
			}
		});

		dialog.setPositiveButton(getText(R.string.ignoreErrors).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getApplicationContext().getCurrentList().setIsShowErrorDialogInd(false);
				getApplicationContext().getCurrentList().setIsShowErrorInd(false);
				getApplicationContext().getShoppingListDbHelper().addUpdateShoppingList(getApplicationContext().getCurrentList());
				getApplicationContext().getShoppingListDbHelper().deleteErrorListItems(getApplicationContext().getCurrentList());
				//setupListAdapter(false);
				//adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
		dialog.setNegativeButton("Fix Errors", new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				getApplicationContext().getShoppingListDbHelper().addUpdateShoppingList(getApplicationContext().getCurrentList());
				dialog.dismiss();
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		Integer subTypeCatCode = getIntent().getIntExtra(Constants.SUB_CAT_CODE, -1);
		if(subTypeCatCode == Constants.NOTES_LIST_TYP_CODE) {
			inflater.inflate(R.menu.action_bar_note_menu, menu);

		}
		else {
			inflater.inflate(R.menu.action_bar_list_home_menu, menu);
			menu.findItem(R.id.addItem).setTitle("Add");
		}
		return super.onCreateOptionsMenu(menu);

	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		else if(item.getItemId() == R.id.addItemKeyboard || item.getItemId() == R.id.addItem) {
			addItem(getApplicationContext().getCurrentList());
		}
		else if(item.getItemId() == R.id.shareItem) {
			EmailUtil.shareList(getApplicationContext(), getApplicationContext().getCurrentList());

		}
		else if(item.getItemId() == R.id.backBtn) {
			onBackPressed();
		}
		else if(item.getItemId() == R.id.addItemImage) {
			Intent noteImageIntent = new Intent(this, NoteImageActivity.class);
			startActivityForResult(noteImageIntent, Constants.REQUEST_IMAGE_CAPTURE);
		}

		else {
			openNoteDrawableScreen(getApplicationContext().getCurrentList());
		}


		return true;

	}
	private void addItem(BasicList list) {
		Intent newListIntent;
		if(getApplicationContext().getCurrentList().getListTypeId() == Constants.TODO_LIST_TYPE_CDE) {
			newListIntent = new Intent(getBaseContext(), ToDoItemActivity.class);

		}
		else if(getApplicationContext().getCurrentList().getListTypeId() == Constants.NOTES_LIST_TYP_CODE) {
			newListIntent = new Intent(getBaseContext(), NotesActivity.class);

		}
		else {
			newListIntent = new Intent(getBaseContext(), ItemActivity.class);


		}
		startActivityForResult(newListIntent, 0);

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub

		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode == RENAME_LIST) {
			setTitle(getApplicationContext().getCurrentList().getListName());
		}
		if(null != getApplicationContext().getCurrentItem()) {
			if(intent.hasExtra(Constants.IS_UPDATE) && intent.getBooleanExtra(Constants.IS_UPDATE, false)) {
				fragment.refreshList(getApplicationContext().getCurrentItem());
			}
			else {
				fragment.addItemToList(getApplicationContext().getCurrentItem());
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getApplicationContext().setCurrentItem(null);
		getApplicationContext().setCurrentList(null);
	}
}