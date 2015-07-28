package com.example.usuario.venderapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.venderapp.DataBase.DbFinanciamiento;
import com.example.usuario.venderapp.DataBase.DbLote;
import com.example.usuario.venderapp.DataBase.DbModelo;
import com.example.usuario.venderapp.DataBase.DbProyecto;
import com.example.usuario.venderapp.DataBase.MyConnection;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
        tabHost.addTab(tabHost.newTabSpec("tab_financiamiento").setIndicator("FINANCIAMIENTOs"),
                TabFinanciamientos.class, null);
        final TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        final View tabView1 = tw.getChildTabViewAt(0);
        final View tabView2 = tw.getChildTabViewAt(1);
        final TextView tv = (TextView)tabView1.findViewById(android.R.id.title);
        final TextView tv2 = (TextView)tabView2.findViewById(android.R.id.title);
        tv2.setTextSize(20);
        tv.setTextSize(20);
        tv2.setTextColor(getResources().getColor(R.color.azul));
        tv.setTextColor(getResources().getColor(R.color.azul));

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
        String user = "llaveUsuarioSIDWeb";

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.cerrar:
                SharedPreferences sharedpreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear();
                editor.putString(user, sharedpreferences.getString(user, null));
                editor.commit();
                borrarDatos();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                this.finish();
                break;
            case R.id.actualizar_todo:
                sharedpreferences=getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                String u=sharedpreferences.getString(user, null);
                borrarDatos();

                ActualizarDatos act=new ActualizarDatos(MainActivity.this,u);
                act.execute();
                break;
            }


        return super.onOptionsItemSelected(item);
    }
    public void borrarDatos() {

        DbModelo dbModelo = new DbModelo(this);
        dbModelo.vaciar();
        DbLote dbLote = new DbLote(this);
        dbLote.vaciar();
        DbProyecto dbProyecto = new DbProyecto(this);
        dbProyecto.vaciar();
        //moveTaskToBack(true);
        clearApplicationData();
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
    public class ActualizarDatos extends AsyncTask<String, Void, String> {
        Context contexto;
        String user;
        ProgressDialog progressDoalog;
        Handler pro_handler;
        public ActualizarDatos(Context c,String u){
            contexto=c;
            user=u;
        }
        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(MainActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Cargando urbanizaciones, lotes y modelos...");
            progressDoalog.setTitle("Actualización");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDoalog.setCanceledOnTouchOutside(false);
            progressDoalog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            MyConnection con=new MyConnection();
            con.actualizarDatos(contexto,user,progressDoalog);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            progressDoalog.dismiss();


        }

        @Override
        protected void onCancelled() {

        }
    }


}
