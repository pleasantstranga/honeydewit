package com.ajbtechnologies.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ajbtechnologies.ExceptionReportHandler;
import com.ajbtechnologies.Application;
import com.ajbtechnologies.ImportError;
import com.ajbtechnologies.ImportedList;
import com.ajbtechnologies.ListHomeActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.converter.CSVTextWorkbookFactory;
import com.ajbtechnologies.converter.Workbook;
import com.ajbtechnologies.converter.WorkbookToListsConverter;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.pojos.ListItem;
import com.ajbtechnologies.utils.StringUtils;
import com.ajbtechnologies.validate.ImportedListValidator;
import com.ajbtechnologies.validate.ListItemImportValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TextImportService extends IntentService {
    private ArrayList<ImportedList> sheets;
    private File tempFiles;
    private String fileName;
    protected ImportedListValidator importedListValidator;
    protected ListItemImportValidator listItemImportValidator;
    private Application app;
    private NotificationManager notificationManager;
    private int mainNotification = 0;
    private static Map<String, Integer> importedSheetNotificationIds;
    public TextImportService() {
        super("ExcelImportService");
        importedSheetNotificationIds = new HashMap<String,Integer>();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        app = (Application)this.getApplicationContext();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();
        mainNotification = random.nextInt();
        sheets = (ArrayList<ImportedList>)intent.getSerializableExtra("sheets");
        tempFiles = (File)intent.getExtras().get("tempFiles");
        fileName = intent.getStringExtra("fileName");
        importedListValidator = new ImportedListValidator(((Application)getApplicationContext()).getShoppingListDbHelper());
        listItemImportValidator = new ListItemImportValidator((Application)getApplicationContext());
        listItemImportValidator.setValidateAttachment(true);

        List<String> sheetNames = new ArrayList<String>();
        for(ImportedList sheet : sheets) {
            if(!StringUtils.isEmpty(sheet.getNewName())) {
                sheetNames.add(sheet.getNewName());
            }
            else {
                sheetNames.add(sheet.getCurrentName());
            }

        }
        //show notification for all sheets
        showInitialMessage(sheetNames);

        try {


            Workbook workbook = CSVTextWorkbookFactory.createWorkbook(tempFiles, sheets.get(0).getNewName());
            if (importedListValidator.validate(workbook.getSheets())) {


                Map<BasicList, List<ListItem>> lists = new WorkbookToListsConverter(getBaseContext(), workbook).convert();

                for (Map.Entry<BasicList, List<ListItem>> entry : lists.entrySet()) {
                    initNotification(entry.getKey());
                    for (ListItem item : entry.getValue()) {

                        listItemImportValidator.validate(item);
                    }

                    app.getShoppingListDbHelper().addUpdateShoppingList(entry.getKey(), true);
                    app.getShoppingListDbHelper().addAllListItems(entry.getKey(), entry.getValue());
                    List<ImportError> errors = listItemImportValidator.getImportErrors();
                    for(ImportError error : errors) {
                        error.setList(entry.getKey());
                    }
                    app.getShoppingListDbHelper().saveImportErrors(errors);
                    updateNotification(entry.getKey());
                }

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
    public void showInitialMessage(Collection<String> sheet) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.honeydewit_icon)
                .setContentTitle("HoneyDew Import Started")
                .setContentText("The import process has started for the following lists");

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("The import process has started for:");

        // Moves events into the expanded layout

        for (String sheetName : sheet) {
            inboxStyle.addLine(sheetName);
        }
        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

        Notification notification = mBuilder.build();


        notificationManager.notify(mainNotification, notification);
    }
    public void initNotification(BasicList list) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.honeydewit_icon)
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
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.honeydewit_icon)
                .setContentTitle(list.getListName())
                .setContentText("The list has been imported")
                .setContentIntent(pendingIntent);

        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("The list has been imported");
        inboxStyle.addLine("Click here to view list");

        // Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);

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
