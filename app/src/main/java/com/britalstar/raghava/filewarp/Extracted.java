package com.britalstar.raghava.filewarp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Extracted extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extracted);
        Toolbar toolbar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.tint));}

        final ListView listView = (ListView) findViewById(android.R.id.list);
        List values = new ArrayList();
        final String path = Environment.getExternalStorageDirectory() + "/FileWarp/extracted/";
        new File(path).mkdirs();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                   values.add(file);
                }


            }

        Collections.sort(values);
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = (String) listView.getAdapter().getItem(position);
                filename=path+filename;
                MimeTypeMap fileMime=MimeTypeMap.getSingleton();
                Intent intent =new Intent(Intent.ACTION_VIEW);
                String Extension=filename.substring(filename.lastIndexOf(".")).toLowerCase();
                String mimeType=fileMime.getMimeTypeFromExtension(Extension.substring(1));
                Uri uri=Uri.fromFile(new File(filename));
                intent.setDataAndType(uri, mimeType);
                Extracted.this.startActivity(Intent.createChooser(intent,"Open With"));


            }
        });
    }


}