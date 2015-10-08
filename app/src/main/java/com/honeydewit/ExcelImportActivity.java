package com.honeydewit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.honeydewit.converter.ExcelWorkbookFactory;
import com.honeydewit.converter.Sheet;
import com.honeydewit.services.ExcelImportService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class ExcelImportActivity extends BasicActivity implements Importer {
    private String fileName;
    private File tempFiles;
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
        SerializableArrayList<ImportedList> sheets = new SerializableArrayList<ImportedList>();
        try {
            File sheetFile = (fileName.endsWith(Constants.XLS_EXT)) ? tempFiles : new File(tempFiles.getAbsolutePath() + "/xl/workbook.xml");
            SparseArray<String> sheetIndexes = ExcelWorkbookFactory.getSheetNames(sheetFile);
            for(int key = 0;key<sheetIndexes.size();key++) {
                Integer keyIndex = sheetIndexes.keyAt(key);
                ImportedList item = new ImportedList();
                item.setFromExcelFile(true);
                item.setIndex(keyIndex);
                item.setChecked(false);
                item.setCurrentName(sheetIndexes.get(keyIndex));
                sheets.add(item);
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            doRedirect(RESULT_CANCELED, R.string.errorImporting);
            finish();
        }
        finally {
            return sheets;
        }


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
        return 	true;
    }
    @Override
    public void deleteTempFiles(File tempFile) {

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
            Intent intent = new Intent(this, ExcelImportService.class);
            intent.putExtra("sheets",sheets);
            intent.putExtra("tempFiles", tempFiles);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteTempFiles(tempFiles.getParentFile());
    }
}
