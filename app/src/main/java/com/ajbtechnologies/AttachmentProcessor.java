package com.ajbtechnologies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ajbtechnologies.calendar.DateUtil;
import com.ajbtechnologies.converter.ImportUtils;

import java.io.File;
import java.io.InputStream;
import java.util.Date;



public class AttachmentProcessor extends BasicActivity   {
	boolean doConvert = false;
	private Importer importer;
    private String fileName;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileprocessorscreen);

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Processing Request:");
        dialog.show();

        ProgramInitializer.initialize(getApplicationContext());
        ((TextView)findViewById(R.id.headerTxt)).setText(getResources().getText(R.string.honeDewItImporting));
        File files = null;
        try {

            files = new GenerateTempFilesTask().execute(dialog).get();

        }
        catch(Exception e) {
            ExceptionReportHandler.sendExceptionReport(e, this);
        }
        finally {
            if(files != null) {
                Uri returnUri = getIntent().getData();
                String mimeType = getContentResolver().getType(returnUri);
                if(mimeType != null) {
                    processMailAttachment(files, mimeType);
                }
                else {
                    processByFileExtension(files);
                }

            }
        }


	}

    private void processMailAttachment(File files, String mimeType) {

            if(ExcelMimeTypes.isExcelMimeType(mimeType)) {
                Intent intent = new Intent(this,ExcelImportActivity.class);
                intent.putExtra(Constants.fileName, fileName);
                intent.putExtra(Constants.tempDirectory, files.getAbsolutePath());
                startActivity(intent);
                finish();
            }
            else if(mimeType.equals(Constants.TEXT_MIME) || mimeType.equals(Constants.CSV_MIME)) {
                Intent intent = new Intent(this,TextImportActivity.class);
                intent.putExtra(Constants.fileName, fileName);
                intent.putExtra(Constants.tempDirectory, files.getAbsolutePath());
                startActivity(intent);
                finish();
            }
            else if(mimeType.equals(Constants.APPLICATION_OCTET_MIME)) {
                Intent intent = new Intent(this, XMLImportActivity.class);
                intent.putExtra(Constants.fileName, fileName);
                intent.putExtra(Constants.tempDirectory, files.getAbsolutePath());
                startActivity(intent);
                finish();
            }

    }
    private void processByFileExtension(File files) {
        String fileName = files.getName().toLowerCase();
        if(fileName.endsWith(Constants.XLSX_EXT) || fileName.endsWith(Constants.XLS_EXT)) {
            Intent intent = new Intent(this,ExcelImportActivity.class);
            intent.putExtra(Constants.fileName, fileName);
            intent.putExtra(Constants.tempDirectory, files.getAbsolutePath());
            startActivity(intent);
            finish();
        }
        if(fileName.endsWith(Constants.CSV_EXT) || fileName.endsWith(Constants.TXT_EXT)) {
            Intent intent = new Intent(this,TextImportActivity.class);
            intent.putExtra(Constants.fileName, fileName);
            intent.putExtra(Constants.tempDirectory, files.getAbsolutePath());
            startActivity(intent);
            finish();
        }
        else if(fileName.endsWith(Constants.HDEW_EXT)) {
            Intent intent = new Intent(this, XMLImportActivity.class);
            intent.putExtra(Constants.fileName, fileName);
            intent.putExtra(Constants.tempDirectory, files.getAbsolutePath());
            startActivity(intent);
            finish();
        }

    }

    private File generateTempFiles() {
		File tempFiles= null;
		try {
            String tempDirectory = getApplicationContext().getImportDirectory() + "/" + DateUtil.tempFileNameFormat.format(new Date());
            InputStream inputStream = ImportUtils.getFileInputStream(getIntent().getData(), getBaseContext());
			fileName = ImportUtils.getAttachmentFileName(getBaseContext(), getIntent().getData(),inputStream);
            tempFiles = ImportUtils.generateImportTempFiles(inputStream, tempDirectory, fileName);
		}
		catch(Exception e) {
			Toast.makeText(this, "There was an error importing. Please try again.", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this, MainMenuActivity.class));
			finish();
		}
		return tempFiles;

	}
	@Override
	public void onBackPressed() {
		showCancelMessageDialog(R.string.cancelImportMessage, null);
	}

    private class GenerateTempFilesTask extends AsyncTask<ProgressDialog, Integer, File> {

        ProgressDialog dialog;

        @Override
        protected File doInBackground(ProgressDialog... params) {
            File tempFiles = null;
            try {

                this.dialog = params[0];
                dialog.setTitle("Generating Temp Files...");
                dialog.setMessage("HoneyDewIt is creating temp files for import");
                tempFiles = generateTempFiles();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
            return tempFiles;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            dialog.dismiss();
        }
    }

}