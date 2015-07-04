package com.britalstar.raghava.filewarp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Raghava on 18-04-2015.
 */
public class Append extends AsyncTask<ArrayList<String>,Integer, String> {
    Context c;
    ProgressDialog bar;
    public Append(Context c) {
        this.c = c;
    }
    public void setProgressBar(ProgressDialog bar) {
        this.bar = bar;
    }
    public void finishasync(String result){
        Uri uri = Uri.parse(result);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
        shareIntent.setType("audio/mp3");
        c.startActivity(Intent.createChooser(shareIntent, "Send to"));
        ((Activity)c).finish();
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (this.bar != null) {
            bar.setProgress(values[0]);
        }
    }

    @Override
    protected String doInBackground(ArrayList<String>... params) {

        ArrayList<String> names = params[0];
        File a = new File(Environment.getExternalStorageDirectory().getPath() + "/FileWarp/");
        a.mkdirs();
        String zipFileName = Environment.getExternalStorageDirectory().getPath() + "/FileWarp/new.zip";


        try {

            int BUFFER = 1024;
            BufferedInputStream origin;
            FileOutputStream dest = new FileOutputStream(zipFileName);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[BUFFER];

            for (int i = 0; i < names.size(); i++) {

                publishProgress((9 * i)*10/names.size());
                FileInputStream fi = new FileInputStream(names.get(i));
                origin = new BufferedInputStream(fi, BUFFER);

                ZipEntry entry = new ZipEntry(names.get(i).substring(names.get(i).lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();

            }

            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
       String result=Environment.getExternalStorageDirectory().getPath() + "/FileWarp/"+new
               SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date())+".mp3";
        try {
            publishProgress(90);
            Resources resources = c.getResources();
            InputStream src1 = resources.openRawResource(R.raw.appender);
            FileOutputStream des = new FileOutputStream(result);
            byte[] buffer = new byte[1024];
            int byt;
            while ((byt = src1.read(buffer)) != -1) {
                des.write(buffer, 0, byt);
            }
            FileOutputStream des1 = new FileOutputStream(result, true);
            FileInputStream src2 = new FileInputStream(zipFileName);
            while ((byt = src2.read(buffer)) != -1) {
                des1.write(buffer, 0, byt);
            }
            src1.close();
            src2.close();
            des.close();
            des1.close();
            File temp=new File(zipFileName);
            temp.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
       onProgressUpdate(100);

        return result;
    }
    @Override
    protected void onPostExecute(String result) {
        bar.dismiss();
        finishasync(result);
    }
}
