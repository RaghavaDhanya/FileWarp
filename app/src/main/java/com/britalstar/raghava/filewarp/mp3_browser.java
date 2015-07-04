package com.britalstar.raghava.filewarp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class mp3_browser extends ListActivity {

    private String path;
    TextView title;
    ListView a;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_browser);

        title=(TextView)findViewById(R.id.texts);

        a = (ListView)findViewById(android.R.id.list);


        // Use the current directory as title

        path = findStoragePath();

        listit();



    }
    private void listit(){
        // Read all files sorted into the values-array
        title.setText(path);
        this.setTitle(path);
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list){
                if (path.endsWith(File.separator)) {
                if(file.endsWith(".mp3")||new File(path+file).isDirectory())
                values.add(file);}
                else{
                    if(file.endsWith(".mp3")||new File(path+"/"+file).isDirectory())
                        values.add(file);}


            }
        }
        Collections.sort(values);
        if (values.isEmpty())
            Toast.makeText(this, "Empty Directory", Toast.LENGTH_SHORT).show();


        // Put the data into the list
       final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        Animation animation_out=AnimationUtils.loadAnimation(mp3_browser.this,R.anim.slide_out);
        a.startAnimation(animation_out);
        final Animation animation_in=AnimationUtils.loadAnimation(mp3_browser.this,R.anim.slide_in);
        animation_out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) { a.startAnimation(animation_in);
                setListAdapter(adapter);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        String filename = (String) getListAdapter().getItem(position);

        if (path.endsWith(File.separator)) {
                filename = path + filename;
            } else {
                filename = path + File.separator + filename;
            }
            if (new File(filename).isDirectory()) {
                path=filename;
                listit();
            } else {
                final ProgressDialog Asyncbar =new ProgressDialog(mp3_browser.this);
                Asyncbar.setTitle("Converting...");
                Asyncbar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                //set the Cancel button
                Asyncbar.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                        Asyncbar.dismiss();
                    }
                });
                Asyncbar.setProgress(0);
                Dissect task=new Dissect(mp3_browser.this);
                task.setProgressBar(Asyncbar);
                task.execute(filename);
                Asyncbar.show();

            }}




    @Override
    public boolean onKeyDown(int keyCode,@NonNull KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            File temp=new File(path);
            if(!( path.equals(findStoragePath()))){
                    path=temp.getParent();
                    listit();
                }
            else
            finish();


            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    private String findStoragePath(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getString("mp3Path",null)!=null&&(new File(preferences.getString("mp3Path",null)).exists()))
            return preferences.getString("mp3Path",null);
        else{
            if(Environment.isExternalStorageEmulated()){
                if(System.getenv("ANDROID_STORAGE")!=null&&(new File(System.getenv("ANDROID_STORAGE")).exists()))
                    return System.getenv("ANDROID_STORAGE");
                else if(System.getenv("SECONDARY_STORAGE")!=null&&(new File(System.getenv("SECONDARY_STORAGE")).exists()))
                {if(new File(System.getenv("SECONDARY_STORAGE")).getParent()!=null)
                    return new File(System.getenv("SECONDARY_STORAGE")).getParent();
                else
                    return System.getenv("SECONDARY_STORAGE");}
                else
                {if(new File(Environment.getExternalStorageDirectory().getParent()).exists())
                    return Environment.getExternalStorageDirectory().getParent();
                else
                    return Environment.getExternalStorageDirectory().getAbsolutePath();}
            }
            else
            {if(new File(Environment.getExternalStorageDirectory().getParent()).exists())
                return Environment.getExternalStorageDirectory().getParent();
            else
                return Environment.getExternalStorageDirectory().getAbsolutePath();}}

    }
    public void showPopup(View v){
        ImageButton fam=(ImageButton)findViewById(R.id.fam_button);
        Animation rotate= AnimationUtils.loadAnimation(mp3_browser.this, R.anim.rotate);
        fam.startAnimation(rotate);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor=preferences.edit();
        PopupMenu popupMenu=new PopupMenu(this,v);
        popupMenu.inflate(R.menu.menu_fam);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.pathSelect:
                        editor.putString("filePath",path);
                        editor.commit();
                        return true;
                    case R.id.mp3Select:
                        editor.putString("mp3Path",path);
                        editor.commit();
                        return true;
                    default:
                        return false;

                }


            }
        });
    }
}




