package com.britalstar.raghava.filewarp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.net.URI;


public class MainActivity extends ActionBarActivity {
    Button SEND;
    Button RECEIVE;
    Button EXTRACTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar actionBar = (Toolbar) findViewById(R.id.actionBar);
        setSupportActionBar(actionBar);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.tint));}
        getSupportActionBar().setLogo(R.drawable.ic_launcher);

        SEND=(Button)findViewById(R.id.Send);
        RECEIVE=(Button)findViewById(R.id.Receive);
        EXTRACTED=(Button)findViewById(R.id.Extracted);
        final Intent a=new Intent(this,Send.class);
        final Intent b=new Intent(this,mp3_browser.class);
        final Intent c=new Intent(this,Extracted.class);
        SEND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(a);
            }
        });
        RECEIVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Select the received mp3 file",Toast.LENGTH_LONG).show();
                startActivity(b);

            }
        });
        EXTRACTED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(c);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings=new Intent(this,Settings.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
