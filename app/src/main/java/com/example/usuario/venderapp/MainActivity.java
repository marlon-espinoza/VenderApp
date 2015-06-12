package com.example.usuario.venderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.usuario.venderapp.DataBase.DbLote;
import com.example.usuario.venderapp.DataBase.DbModelo;
import com.example.usuario.venderapp.DataBase.DbProyecto;

import java.io.File;


public class MainActivity extends ActionBarActivity {
    public FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this,
                getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab_urbanizaciones").setIndicator("URBANIZACIONES"),
                TabUrbanizaciones.class, null);
        final TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        final View tabView = tw.getChildTabViewAt(0);
        final TextView tv = (TextView)tabView.findViewById(android.R.id.title);
        tv.setTextSize(20);
        tv.setTextColor(getResources().getColor(R.color.azul));

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_main);
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
            return true;
        }
        else if(id == R.id.cerrar){
            String user = "llaveUsuarioSIDWeb";
            SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.putString(user, sharedpreferences.getString(user, null));
            editor.commit();

            DbModelo dbModelo=new DbModelo(this);
            dbModelo.vaciar();
            DbLote dbLote=new DbLote(this);
            dbLote.vaciar();
            DbProyecto dbProyecto=new DbProyecto(this);
            dbProyecto.vaciar();
            //moveTaskToBack(true);
            clearApplicationData();
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void logout() {

        String user = "llaveUsuarioSIDWeb";
        SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.putString(user, sharedpreferences.getString(user, null));
        editor.commit();

        //moveTaskToBack(true);

        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        this.finish();
    }
    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")&&s.contentEquals("Imagenes")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
    public FragmentTabHost getMyTabHost(){
        return tabHost;
    }
}
