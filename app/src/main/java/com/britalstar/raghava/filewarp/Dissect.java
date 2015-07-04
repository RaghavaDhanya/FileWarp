package com.britalstar.raghava.filewarp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Raghava on 26-04-2015.
 */
public class Dissect extends AsyncTask<String, Integer, Void> {

    Context c;
    ProgressDialog bar;
    public Dissect(Context c) {
        this.c = c;
    }
    public void setProgressBar(ProgressDialog bar) {
        this.bar = bar;
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (this.bar != null) {
            bar.setProgress(values[0]);
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        String _zipFile= params[0];
        int count=0;

        try {
            File unzipDestinationDirectory = new File(Environment.getExternalStorageDirectory()+"/FileWarp/extracted/");
            unzipDestinationDirectory.mkdirs();
            FileInputStream src=new FileInputStream(_zipFile);
            FileOutputStream out=new FileOutputStream(Environment.getExternalStorageDirectory()+"/FileWarp/out.zip");
            src.getChannel().position(60603);
            byte[] buffer = new byte[1024];
            int byt;
            while ((byt = src.read(buffer)) != -1) {
                out.write(buffer, 0, byt);
            }
            onProgressUpdate(10);
            int BUFFER = 2048;
            List<String> zipFiles = new ArrayList<String>();
            File sourceZipFile = new File(Environment.getExternalStorageDirectory()+"/FileWarp/out.zip");

            ZipFile zipFile;
            zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
            Enumeration zipFileEntries = zipFile.entries();
            while (zipFileEntries.hasMoreElements()) {

                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                count++;
                onProgressUpdate((9*count*10)/zipFile.size());
                String currentEntry = entry.getName();
                File destFile = new File(unzipDestinationDirectory, currentEntry);
                if (currentEntry.endsWith(".zip")) {
                    zipFiles.add(destFile.getAbsolutePath());
                }

                File destinationParent = destFile.getParentFile();

                destinationParent.mkdirs();

                try {
                    if (!entry.isDirectory()) {
                        BufferedInputStream is =
                                new BufferedInputStream(zipFile.getInputStream(entry));
                        int currentByte;
                        byte data[] = new byte[BUFFER];

                        FileOutputStream fos = new FileOutputStream(destFile);
                        BufferedOutputStream dest =
                                new BufferedOutputStream(fos, BUFFER);
                        while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                            dest.write(data, 0, currentByte);
                        }
                        dest.flush();
                        dest.close();
                        is.close();

                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            zipFile.close();
            sourceZipFile.delete();
             onProgressUpdate(100);
        } catch (IOException e) {
            e.printStackTrace();

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        bar.dismiss();

        Intent intent=new Intent(c,Extracted.class);
        c.startActivity(intent);
         ((Activity)c).finish();
        super.onPostExecute(aVoid);
    }
}
