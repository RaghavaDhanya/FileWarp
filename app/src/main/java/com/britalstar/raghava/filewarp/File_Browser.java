package com.britalstar.raghava.filewarp;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
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
import java.util.Map;
import java.util.zip.Inflater;


public class File_Browser extends ListActivity {

    private String path;
    TextView title;
    ListView a ;
    int x;
    ArrayList Filenames=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file__browser);

        title=(TextView)findViewById(R.id.texts);

        a= (ListView)findViewById(android.R.id.list);



        // Use the current directory as title

        path=findStoragePath();
        x=android.R.layout.simple_list_item_1;
        listit();



    }
    private void listit(){

        // Read all files sorted into the values-array
        title.setText(path);
        this.setTitle(path);
        final List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list){
                values.add(file);

            }
        }
        Collections.sort(values);
        if (values.isEmpty())
            Toast.makeText(this, "Empty Directory", Toast.LENGTH_SHORT).show();


        // Put the data into the list
       final ArrayAdapter adapter = new ArrayAdapter(this,
                x, android.R.id.text1, values);

        Animation animation_out=AnimationUtils.loadAnimation(File_Browser.this,R.anim.slide_out);
        a.startAnimation(animation_out);
        final Animation animation_in=AnimationUtils.loadAnimation(File_Browser.this,R.anim.slide_in);
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


        a.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(x!=android.R.layout.simple_list_item_multiple_choice)
                {a.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    x=android.R.layout.simple_list_item_multiple_choice;
                    Button add =new Button(File_Browser.this);
                    final Button selectAll=new Button(File_Browser.this);
                    selectAll.setText("Select All");
                    add.setText("Add Selected");
                    final LinearLayout ll = (LinearLayout)findViewById(R.id.LINEAR);
                    add.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    add.setId(R.id.Dynamic_Button);
                    selectAll.setId(R.id.Dyanamic_Button_2);
                    selectAll.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll.addView(add, 0);
                    ll.addView(selectAll,1);
                    add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SparseBooleanArray pos=a.getCheckedItemPositions();
                             int count=0;
                            for(int i=0;i<a.getCount();i++)
                            {if(pos.get(i))
                                if (path.endsWith(File.separator)) {
                                    if(!(new File(path+a.getItemAtPosition(i).toString()).isDirectory()))
                                    Filenames.add(path +a.getItemAtPosition(i).toString());
                                    else count++;
                                } else {
                                    if(!(new File(path+File.separator+a.getItemAtPosition(i).toString()).isDirectory()))
                                    Filenames.add(path + File.separator +a.getItemAtPosition(i).toString());
                                    else count++;

                                }

                            }

                            if(count>0)
                              Toast.makeText(File_Browser.this,"Adding Folders is not yet supported",Toast.LENGTH_SHORT).show();
                            Intent result=new Intent();
                            result.putStringArrayListExtra("Filename",Filenames);
                            setResult(RESULT_OK,result);
                            finish();
                        }
                    });
                   final Button clearSelection=new Button(File_Browser.this);
                    clearSelection.setText("Deselect All");

                    clearSelection.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));

                    clearSelection.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<values.size();i++)
                                a.setItemChecked(i,false);
                            ll.removeView(clearSelection);
                            ll.addView(selectAll);
                        }
                    });
                    selectAll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<values.size();i++)
                                a.setItemChecked(i,true);
                            ll.removeView(selectAll);
                            ll.addView(clearSelection);


                        }
                    });

                    listit();
                    return true;}
                return true;
            }

        });


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if(x==android.R.layout.simple_list_item_multiple_choice)
        {}
        else
        {String filename = (String) getListAdapter().getItem(position);

            if (path.endsWith(File.separator)) {
                filename = path + filename;
            } else {
                filename = path + File.separator + filename;
            }
            if (new File(filename).isDirectory()) {
                path=filename;
                listit();
            } else {
                Filenames.add(filename);
                Intent result=new Intent();
                result.putStringArrayListExtra("Filename",Filenames);
                setResult(RESULT_OK,result);
                finish();

            }}


    }

    @Override
    public boolean onKeyDown(int keyCode,@NonNull KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(x!=android.R.layout.simple_list_item_multiple_choice)
            {File temp=new File(path);
                if(!( path.equals(findStoragePath()))){
                    path=temp.getParent();

                    listit();
                }
                else{setResult(RESULT_CANCELED);
                    finish();}}
            else {
                x = android.R.layout.simple_list_item_1;
                a.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                ViewGroup main=(ViewGroup)findViewById(R.id.LINEAR);

                main.removeAllViews();
                listit();

            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
private String findStoragePath(){
    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    if (preferences.getString("filePath",null)!=null&&(new File(preferences.getString("filePath",null)).exists()))
        return preferences.getString("filePath",null);
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
        return Environment.getExternalStorageDirectory().getAbsolutePath();}

}}



    public void showPopup(View v){
        ImageButton fam=(ImageButton)findViewById(R.id.fam_button);
        Animation rotate= AnimationUtils.loadAnimation(File_Browser.this,R.anim.rotate);
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
                        editor.apply();
                        return true;
                    case R.id.mp3Select:
                        editor.putString("mp3Path",path);
                        editor.apply();
                        return true;
                    default:
                        return false;

                }


            }
        });
    }

}