package com.britalstar.raghava.filewarp;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Send extends ActionBarActivity {
    private Button Add;
    ArrayList names=new ArrayList();
    int File_Select=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        Toolbar toolbar=(Toolbar)findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.tint));}
        getSupportActionBar().setTitle(R.string.Files);
        ImageButton fab=(ImageButton)findViewById(R.id.fab_button);
        fab.setVisibility(View.GONE);



}
    private void listit()
    {
        final ListView listView=(ListView)findViewById(android.R.id.list);
        ArrayAdapter adapter; adapter = new ArrayAdapter(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, names);
        listView.setAdapter(adapter);
        if(names.size()!=0){
            LinearLayout empty=(LinearLayout)findViewById(R.id.layout_empty);
            empty.setVisibility(View.GONE);
            ImageButton fab=(ImageButton)findViewById(R.id.fab_button);
            fab.setVisibility(View.VISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final ProgressDialog Asyncbar =new ProgressDialog(Send.this);
                    Asyncbar.setTitle("Converting...");
                    Asyncbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    //set the Cancel button
                    Asyncbar.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton){
                            Toast.makeText(getApplicationContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                            Asyncbar.dismiss();
                        }
                    });
                    Asyncbar.setProgress(0);


                    Append task=new Append(Send.this);
                    task.setProgressBar(Asyncbar);
                    task.execute(names);
                    Asyncbar.show();


                }
            });

    }}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == File_Select) {
            if (resultCode == RESULT_OK) {
                ArrayList Filenames=data.getStringArrayListExtra("Filename");
                names.addAll(Filenames);
                listit();



            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent a= new Intent (this, File_Browser.class);
            startActivityForResult(a,File_Select);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}






