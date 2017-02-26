package com.ajbtechnologies.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.ExceptionReportHandler;
import com.ajbtechnologies.ImportedList;
import com.ajbtechnologies.ListHomeActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.converter.ExcelWorkbookFactory;
import com.ajbtechnologies.converter.Workbook;
import com.ajbtechnologies.converter.WorkbookToListsConverter;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.pojos.ListItem;
import com.ajbtechnologies.validate.ImportedListValidator;
import com.ajbtechnologies.validate.ListItemImportValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ExcelImportService extends IntentService {
    private static Map<String, Integer> importedSheetNotificationIds;
    protected ImportedListValidator importedListValidator;
    protected ListItemImportValidator listItemImportValidator;
    private ArrayList<ImportedList> chosenSheets;
    private File tempFiles;
    private String fileName;
    private NotificationManager notificationManager;
    private int mainNotification = 0;
    public ExcelImportService() {
        super("ExcelImportService");
        importedSheetNotificationIds = new HashMap<String,Integer>();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        mainNotification = random.nextInt();
        Map<String, String> checkedSheetsNames = new HashMap<String, String>();
        chosenSheets = (ArrayList<ImportedList>)intent.getSerializableExtra("sheets");
        tempFiles = (File)intent.getExtras().get("tempFiles");
        fileName = intent.getStringExtra("fileName");
        importedListValidator = new ImportedListValidator(((Application)getApplicationContext()).getShoppingListDbHelper());
        listItemImportValidator = new ListItemImportValidator((Application)getApplicationContext());
        listItemImportValidator.setValidateAttachment(true);

        for (ImportedList sheet : chosenSheets) {
            if (sheet.isChecked()) {
                if (sheet.getNewName() != null && sheet.getNewName().length() > 0) {
                    checkedSheetsNames.put(sheet.getCurrentName(), sheet.getNewName());
                } else {
                    checkedSheetsNames.put(sheet.getCurrentName(), sheet.getCurrentName());
                }
            }
        }

        try {
            if (checkedSheetsNames.size() > 0) {

                Workbook workbook = ExcelWorkbookFactory.createWorkbook(tempFiles, fileName, checkedSheetsNames);
                if (importedListValidator.validate(workbook.getSheets())) {

                    Map<BasicList, List<ListItem>> lists = new WorkbookToListsConverter(getBaseContext(), workbook).convert();

                    for (Map.Entry<BasicList, List<ListItem>> entry : lists.entrySet()) {
                        initNotification(entry.getKey());
                        for (ListItem item : entry.getValue()) {

                            listItemImportValidator.validate(item);
                        }

                        ((Application)getApplicationContext()).getShoppingListDbHelper().addUpdateShoppingList(entry.getKey(), true);
                        ((Application)getApplicationContext()).getShoppingListDbHelper().addAllListItems(entry.getKey(), entry.getValue());
                        ((Application)getApplicationContext()).getShoppingListDbHelper().saveImportErrors(listItemImportValidator.getImportErrors());

                        updateNotification(entry.getKey());

                    }

                }
            } else {
                Toast.makeText(getBaseContext(), getText(R.string.noSheetsSeleceted), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            ExceptionReportHandler.sendExceptionReport(e,this);

        }
        finally {
            removeAllListsNotification();
            deleteTempFiles(tempFiles);
        }

    }
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

    public void initNotification(BasicList list) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.application_icon)
                .setContentTitle(list.getListName())
                .setContentText("The list is being imported");

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("The list is being imported");

        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        Notification notification = mBuilder.build();

        importedSheetNotificationIds.put(list.getListName(), new Random().nextInt());
        notificationManager.notify(importedSheetNotificationIds.get(list.getListName()), notification);

    }


    public void updateNotification(BasicList list) {
        Intent notificationIntent = new Intent(this, ListHomeActivity.class);
        notificationIntent.putExtra("listId",list.get_id());
        notificationIntent.putExtra("isNotification",true);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.application_icon)
                .setContentTitle(list.getListName())
                .setContentText("Click here to view list")
                .setContentIntent(pendingIntent);


        Notification notification = mBuilder.build();

        // Hide the notification after it's selected
        notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
        notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
        notification.defaults |= Notification.DEFAULT_SOUND;

        notificationManager.notify(importedSheetNotificationIds.get(list.getListName()), notification);
        importedSheetNotificationIds.remove(list.getListName());

    }

    public void removeAllListsNotification() {

        notificationManager.cancel(mainNotification);
    }
}
