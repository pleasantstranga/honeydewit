package com.ajbtechnologies.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.Constants;
import com.ajbtechnologies.ImportedList;
import com.ajbtechnologies.ListHomeActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.XMLMarshaller;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.validate.ImportedListValidator;
import com.ajbtechnologies.validate.ListItemImportValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class XMLImportService extends IntentService {
    private static Map<String, Integer> importedSheetNotificationIds;
    protected ImportedListValidator importedListValidator;
    protected ListItemImportValidator listItemImportValidator;
    private ArrayList<ImportedList> sheets;
    private File tempFiles;
    private String fileName;
    private Application app;
    private NotificationManager notificationManager;
    private int mainNotification = 0;
    private Random random = new Random();

    public XMLImportService() {
        super("XMLImportService");
        importedSheetNotificationIds = new HashMap<String, Integer>();

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

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mainNotification = random.nextInt();

            sheets = (ArrayList<ImportedList>) intent.getSerializableExtra(Constants.SHEETS);
            tempFiles = (File) intent.getExtras().get(Constants.tempDirectory);
            fileName = intent.getStringExtra(Constants.fileName);

            XMLMarshaller marshaller = new XMLMarshaller(this);

            BasicList list = marshaller.writeListFromXML(getXmlFile(), fileName);

            initNotification(list);

            ((Application) getApplicationContext()).getShoppingListDbHelper().addUpdateShoppingList(list, sheets.get(0).isReplace());

            saveImages(tempFiles);

            updateNotification(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    private void saveImages(File f) {
        File[] filesInDirectory = f.listFiles();

        for (File src : filesInDirectory) {
            if (!src.getName().endsWith(Constants.XML_EXT)) {
                File dest = new File(((Application) getApplicationContext()).getImagesDirectory() + src.getName());
                try {
                    copyImage(src, dest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void initNotification(BasicList list) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.application_icon)
                .setContentTitle(getText(R.string.app_name) + " " + getText(R.string.importStarted))
                .setContentText(list.getListName());


        Notification notification = mBuilder.build();

        importedSheetNotificationIds.put(list.getListName(), new Random().nextInt());
        notificationManager.notify(importedSheetNotificationIds.get(list.getListName()), notification);

    }


    public void updateNotification(BasicList list) {
        Intent notificationIntent = new Intent(this, ListHomeActivity.class);
        notificationIntent.putExtra("listId", list.get_id());
        notificationIntent.putExtra("isNotification", true);
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

    private File getXmlFile() {
        File xmlFile = null;
        File[] files = tempFiles.listFiles();
        for (File file : files) {
            Log.d(getText(R.string.app_name) + "importing: ", file.getPath());
            if (file.getName().endsWith(Constants.XML_EXT)) {
                xmlFile = file;
                break;
            }
        }
        return xmlFile;
    }

}
