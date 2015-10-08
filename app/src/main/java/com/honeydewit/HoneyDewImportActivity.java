package com.honeydewit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.honeydewit.converter.Sheet;
import com.honeydewit.pojos.BasicList;
import com.honeydewit.validate.ImportedListValidator;
import com.honeydewit.validate.ListValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class HoneyDewImportActivity extends BasicActivity implements Importer {
	
	String fileName;
	File tempFiles;
	BasicList list;
	HoneyDewApplication appContext;
	protected ImportedListValidator importedListValidator;
    SerializableArrayList<ImportedList> listInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tempFiles = new File(savedInstanceState.getString("tempDirectory"));
        fileName = savedInstanceState.getString("fileName");
        importedListValidator = new ImportedListValidator(getApplicationContext().getShoppingListDbHelper());
        listInfo = prepareImport();
    }

    public void startImport() {


		XMLMarshaller marshaller = new XMLMarshaller(this);
		String listName = tempFiles.getName().substring(0, tempFiles.getName().indexOf(Constants.HDEW_EXT));
		if(getApplicationContext().getShoppingListDbHelper().isListExists(listName, Constants.SHOPPING_LIST_TYPE_CDE, true)) {
			showImportChoicesDialog(listName, false,true, false,Constants.XML_CHECK_REQ);
		}
		else {
			try {
				
				list = marshaller.writeListFromXML(getXmlFile(), null);
				saveImages(tempFiles);
				
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
		if(tempFiles != null) {
			tempFiles.delete();
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

		XMLMarshaller marshaller = new XMLMarshaller(this);
		if(listInfo.get(0).isRename()) {
			list = marshaller.writeListFromXML(getXmlFile(), listInfo.get(0).getNewName());
		}
		else {
			list = marshaller.writeListFromXML(getXmlFile(), null);
		}
	    getApplicationContext().getShoppingListDbHelper().addUpdateShoppingList(list, listInfo.get(0).isReplace());
		
	}

	private void saveImages(File f) {
		File[] filesInDirectory = f.listFiles();
		
		for(File src : filesInDirectory) {
			if(!src.getName().endsWith(Constants.XML_EXT)) {
				File dest = new File(appContext.getImagesDirectory()+src.getName());
				try {
					copyImage(src,dest);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static void copyImage(File source, File dest) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} finally {
			input.close();
			output.close();
		}
	}
	private File getXmlFile() {
		File xmlFile = null;
		File[] files = tempFiles.listFiles();
		for(File file : files) {
			Log.d("HonewDewImport Get XML", file.getPath());
			if(file.getName().endsWith(Constants.XML_EXT)) {
				xmlFile = file;
				break;
			}
		}
		return xmlFile;
	}

	@Override
	public boolean validate(List<Sheet> sheets) {
		ListValidator validator = new ListValidator();
		return validator.validate(getApplicationContext(), list);
	}

	@Override
	public void deleteTempFiles(File tempFile) {
		// TODO Auto-generated method stub
		
	}
    public void showImportChoicesDialog(String listName, boolean isEmpty,boolean isExists, boolean isFromExcelFile, int requestCode) {
        ImportedList listInfo = new ImportedList();
        listInfo.setFromExcelFile(isFromExcelFile);
        listInfo.setCurrentName(listName);

        Intent intent = new Intent(this, ImportedListNameChoiceActivity.class);
        intent.putExtra("isExists", isExists);
        intent.putExtra("isEmpty", isEmpty);
        intent.putExtra("sheetInfo", listInfo);
        startActivityForResult(intent,requestCode);

    }
}
