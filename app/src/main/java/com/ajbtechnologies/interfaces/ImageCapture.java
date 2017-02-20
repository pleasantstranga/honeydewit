package com.ajbtechnologies.interfaces;

/**
 * Created by aaronbernstein on 1/14/16.
 */
public interface ImageCapture {
    void capture(int requestCode, String path, String fileName, String fileNameExtension);
}
