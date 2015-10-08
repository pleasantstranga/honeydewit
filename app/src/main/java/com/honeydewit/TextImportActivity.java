package com.honeydewit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.honeydewit.converter.Sheet;
import com.honeydewit.services.TextImportService;
import com.honeydewit.validate.ImportedListValidator;
import com.honeydewit.validate.ListItemImportValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TextImportActivity extends BasicActivity implements Importer {
    private String fileName;
    private File tempFiles;
    protected ImportedListValidator importedListValidator;
    protected ListItemImportValidator listItemImportValidator;
    private ArrayList<ImportedList> sheets = new SerializableArrayList<ImportedList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileprocessorscreen);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.show();
        ((TextView)findViewById(R.id.headerTxt)).setText("HoneyDew-It");
        tempFiles = new File(getIntent().getStringExtra("tempDirectory"));
        fileName = getIntent().getStringExtra("fileName");
        importedListValidator = new ImportedListValidator(getApplicationContext().getShoppingListDbHelper());
        listItemImportValidator = new ListItemImportValidator(getApplicationContext());
        listItemImportValidator.setValidateAttachment(true);

        try {

            sheets = prepareImport();
            if(sheets != null && sheets.size() > 0) {
                dialog.dismiss();
                Intent intent = new Intent(this, CheckBoxListActivity.class);
                intent.putExtra("importedListSheets",this.sheets);
                startActivityForResult(intent, Constants.CHECKBOX_DIALOG_REQUEST_CODE);
            }
            else {

                doRedirect(RESULT_CANCELED, R.string.importError);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public SerializableArrayList<ImportedList> prepareImport()   {
        SerializableArrayList<ImportedList>sheets = new SerializableArrayList<ImportedList>();

        ImportedList item = new ImportedList();
        item.setFromExcelFile(true);
        item.setIndex(1);
        item.setChecked(false);
        item.setCurrentName(fileName);
        item.setNewName(fileName);
        sheets.add(item);
        return sheets;


    }

    @Override
    public void doRedirect(int resultCode, final Integer messageId) {
        if(resultCode == RESULT_CANCELED) {

            Intent intent = new Intent(this,MainMenuActivity.class);
            intent.putExtra("redirectError", messageId);
            startActivity(intent);
            finish();
        }
        if(resultCode == RESULT_OK) {
            startActivity(new Intent(this,ListsHomeActivity.class));
            finish();
        }
    }
    @Override
    public boolean validate(List<Sheet> sheets) {
        return 	importedListValidator.validate(sheets);
    }
    @Override
    public void deleteTempFiles(File tempFile) {

        if (tempFile.isDirectory()) {
            for (File child : tempFile.listFiles()) {
                deleteTempFiles(child);
            }
            android.util.Log.d("ExcelImport:Delete", "Deleting directory: " + tempFile.getAbsolutePath());
            tempFile.delete();
        }
        else {
            android.util.Log.d("ExcelImport:Delete", "Deleting tempFile: " + tempFile.getAbsolutePath());
            tempFile.delete();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CANCELED) {
            doRedirect(RESULT_CANCELED, R.string.importCancelled);
        }
        else if(requestCode == Constants.CHECKBOX_DIALOG_REQUEST_CODE) {
            //noinspection unchecked
            sheets = (ArrayList<ImportedList>)data.getSerializableExtra("importedListSheets");

            Intent intent = new Intent(this, TextImportService.class);
            intent.putExtra("sheets",sheets);
            intent.putExtra("tempFiles", tempFiles);
            intent.putExtra("fileName", fileName);
            startService(intent);

            final HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
            dialog.show(getSupportFragmentManager(), "cancelMessage");
            boolean isPend = getSupportFragmentManager().executePendingTransactions();
            dialog.setMessage("The import process has started. You will be notified when the import has finished.");
            dialog.setTitle("Import in progress...");
            dialog.setPositiveButton(getText(R.string.ok).toString(), new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

        }

    }
}
