package com.ajbtechnologies;

import android.content.Intent;
import android.os.Bundle;

import com.ajbtechnologies.converter.Sheet;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.services.XMLImportService;
import com.ajbtechnologies.utils.StringUtils;
import com.ajbtechnologies.validate.ImportedListValidator;
import com.ajbtechnologies.validate.ListValidator;

import java.io.File;
import java.util.List;

public class XMLImportActivity extends BasicActivity implements Importer {
	
	protected ImportedListValidator importedListValidator;
	private String fileName;
	private File tempDirectory;
	private SerializableArrayList<ImportedList> sheets;
	private BasicList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		tempDirectory = new File(getIntent().getStringExtra(Constants.tempDirectory));
		fileName = getIntent().getStringExtra(Constants.fileName);
		importedListValidator = new ImportedListValidator(getApplicationContext().getShoppingListDbHelper());
		sheets = prepareImport();
		startImport();
	}

    public void startImport() {
		if (getApplicationContext().getShoppingListDbHelper().isListExists(fileName, Constants.SHOPPING_LIST_TYPE_CDE, true)) {
			showImportChoicesDialog(fileName, false, true, false, Constants.XML_CHECK_REQ);
		}
		else {
			try {
				doImport();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				doRedirect(1,null );
			}
		}
	}

    public SerializableArrayList<ImportedList> prepareImport() {
        SerializableArrayList<ImportedList> lists = new SerializableArrayList<ImportedList>();
        ImportedList listInfo = new ImportedList();
        listInfo.setFromExcelFile(false);
        listInfo.setIndex(0);
        listInfo.setChecked(true);
        listInfo.setCurrentName(fileName);
        lists.add(listInfo);
        return lists;
    }

    @Override
	public void doRedirect(int resultCode, Integer messageId) {
		Intent intent = null;
		if (tempDirectory != null) {
			tempDirectory.delete();
		}
		if(list != null) {
			intent = new Intent(this, ListHomeActivity.class);
			intent.putExtra("listId", list.get_id());
		}
		else {
			intent = new Intent(this, MainMenuActivity.class);
		}
		startActivity(intent);
		finish();


	}

	public void doImport() {
		Intent intent = new Intent(this, XMLImportService.class);
		intent.putExtra(Constants.SHEETS, sheets);
		intent.putExtra(Constants.tempDirectory, tempDirectory);
		intent.putExtra(Constants.fileName, fileName);
		startService(intent);
	}


	@Override
	public boolean validate(List<Sheet> sheets) {
		ListValidator validator = new ListValidator();
		return validator.validate(getApplicationContext(), list);
	}


    public void showImportChoicesDialog(String listName, boolean isEmpty,boolean isExists, boolean isFromExcelFile, int requestCode) {
        ImportedList listInfo = new ImportedList();
        listInfo.setFromExcelFile(isFromExcelFile);
        listInfo.setCurrentName(listName);

		Intent intent = new Intent(this, ImportedListNameChoiceDialogActivity.class);
		intent.putExtra("isExists", isExists);
        intent.putExtra("isEmpty", isEmpty);
        intent.putExtra("sheetInfo", listInfo);
        startActivityForResult(intent,requestCode);

    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data.getExtras().containsKey(Constants.LIST_INFO)) {
				ImportedList listInfo = (ImportedList) data.getExtras().get(Constants.LIST_INFO);
				if (listInfo.isRename() && !StringUtils.isEmpty(listInfo.getNewName())) {
					fileName = listInfo.getNewName();
				}
			}
			doImport();
		} else {
			Intent intent = new Intent(this, MainMenuActivity.class);
			intent.putExtra(Constants.MESSAGE, R.string.importCancelled);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void deleteTempFiles(File tempFiles) {

	}

}
