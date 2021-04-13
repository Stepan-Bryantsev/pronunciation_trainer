package com.example.coursework;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AudioAdapter {
    public static byte[] convertToBytes(String path) throws IOException {
        File file = new File(path);
        FileInputStream fis = null;
        fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] b = new byte[(int)file.length()];

        if (fis != null) {
            for (int readNum; (readNum = fis.read(b)) != -1;) {
                bos.write(b, 0, readNum);
            }
        }

        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    public static void convertTo3GPP(String path, byte[] bytes) throws IOException {
        //path with .3gpp at the end
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(bytes);
        fos.close();
    }
}
