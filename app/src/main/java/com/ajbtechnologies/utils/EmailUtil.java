package com.ajbtechnologies.utils;

import android.net.Uri;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.XMLMarshaller;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.pojos.ListItem;
import com.fileutil.ZipUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aaronbernstein on 9/1/15.
 */
public class EmailUtil {

    public static void shareList(Application context, BasicList list) {
        XMLMarshaller marshaller = new XMLMarshaller(context);
        File tempFile = marshaller.writeListToXml(list,context.getExportTempDirectory());

        //get image names
        String filePath = context.getZipDirectory() + list.getListName() + ".hdew";
        List<String> fileNames = getItemsImagesPaths(context,list);
        fileNames.add(tempFile.getPath());
        ZipUtil.zip(fileNames, filePath);

        String[] emailAddresses ={};
        // context.sendXml(emailAddresses, Uri.fromFile(tempFile));
        context.sendXml(emailAddresses, Uri.fromFile(new File(filePath)));
    }
    public static List<String> getItemsImagesPaths(Application context, BasicList list) {
        List<String> imageNames = new ArrayList<>();
        for(ListItem item : list.getItems()) {
            if(item.getImageName() != null && item.getImageName().trim().length() > 0) {
                if(new File(context.getImagesDirectory() + item.getImageName()).exists()) {
                    imageNames.add(context.getImagesDirectory() + item.getImageName());
                }

            }
        }
        return imageNames;
    }

}
