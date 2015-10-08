package com.honeydewit;

import android.content.Context;

import com.honeydewit.converter.BasicListShareConverter;
import com.honeydewit.pojos.BasicList;
import com.honeydewit.pojos.BasicListShare;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
 
public class XMLMarshaller {
	
	private Context context;
	
	public XMLMarshaller(Context context) {
		this.context = context;
	}

	public File writeListToXml(BasicList list, File tempDirectory) {
		BasicListShare share = BasicListShareConverter.convertToBasicListShare(list);
		
		String fileName = tempDirectory + "/" + list.getListName() + Constants.XML_EXT;
		Serializer serializer = new Persister();
		File result = new File(fileName);

		try {
			serializer.write(share, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
       
    }
	
	public BasicList writeListFromXML(File xmlFile, String newListName) {
		
		Thread.currentThread().setContextClassLoader(context.getClassLoader());
		Serializer serializer = new Persister();
		BasicList list = null;
		try {
			BasicListShare share = serializer.read(BasicListShare.class, xmlFile);
			if(newListName != null) {
				share.setListName(newListName);
			}
			list = BasicListShareConverter.convertFromBasicListShare(context, share);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return list;
       
    }
}