package com.example.usuario.venderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.venderapp.DataBase.DbLote;
import com.example.usuario.venderapp.DataBase.DbModelo;
import com.example.usuario.venderapp.DataBase.DbProyecto;
import com.example.usuario.venderapp.DataBase.DbUrbanizacion;
import com.example.usuario.venderapp.DataBase.MyConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by USUARIO on 06-may-15.
 */
public class LoginActivity extends Activity {
    private UserLoginTask mAuthTask = null;
    private int userId=0;
    private Activity act=this;

    // UI references.
    public static final String MyPREFERENCES = "SIDWebPrefs" ;
    SharedPreferences sharedpreferences;

    private String user="llaveUsuarioSIDWeb";
    private String user_log="llaveUsuarioLogSIDWeb";
    private EditText mUser;
    private EditText mPassword;
    private ProgressBar mProgress;
    private View mLoginFormView;
    private String log_bool;
    private Button mButton;
    private boolean is_log=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUser = (EditText) findViewById(R.id.user);
        mProgress = (ProgressBar)findViewById(R.id.login_progress);
        mButton = (Button) findViewById(R.id.sign_in_button);
        mPassword = (EditText) findViewById(R.id.password);

        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER))||
                        id == R.id.login || id == EditorInfo.IME_NULL|| (id == EditorInfo.IME_ACTION_DONE)) {

                    String user = mUser.getText().toString();
                    String pass = mPassword.getText().toString();

                    mAuthTask = new UserLoginTask(user, pass);
                    mAuthTask.execute(user, pass);


                    return true;
                }
                return false;
            }
        });


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (is_log) {
                    mAuthTask.cancel(true);
                    mButton.setText(getString(R.string.action_cancel2));
                    mButton.setEnabled(false);
                } else {

                    if(comprobar_campos())return;

                    String user = mUser.getText().toString();
                    String pass = mPassword.getText().toString();


                    mAuthTask = new UserLoginTask(user, pass);
                    mAuthTask.execute(user, pass);

                }
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        sharedpreferences=getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(user_log))
        {
            sharedpreferences=getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();

            System.out.println("existe usuario y es: "+user+": "+sharedpreferences.getString(user,null));
        }else if(sharedpreferences.contains(user)){
            mUser.setText(sharedpreferences.getString(user,null));
            mPassword.requestFocus();
        }
    }

    public void attemptLogin() {
        Toast o= Toast.makeText(getApplicationContext(), log_bool, Toast.LENGTH_SHORT);
        //o.show();
        if(log_bool.equalsIgnoreCase("true")){//
            SharedPreferences.Editor editor = sharedpreferences.edit();
            String u = mUser.getText().toString();
            editor.putString(user, u);
            editor.putBoolean(user_log, true);
            editor.commit();

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return;
        }

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        //alertDialog.setTitle("Reset...");
        alertDialog.setMessage("El usuario o contraseña ingresado es incorrecto.");
        alertDialog.setButton("Aceptar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //here you can add functions
            } });
        alertDialog.show();

        // Reset errors.
        mUser.setEnabled(true);
        mPassword.setEnabled(true);
        mPassword.setText("");
        mButton.setText(getString(R.string.action_sign_in));

    }

    public boolean comprobar_campos(){
        mUser.setError(null);
        mPassword.setError(null);

        String email = mUser.getText().toString();
        String password = mPassword.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPassword.setError(getString(R.string.error_field_required));
            mPassword.requestFocus();
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mUser.setError(getString(R.string.error_field_required));
            mUser.requestFocus();
            cancel = true;
        }
        return cancel;

    }

    public String login(String usuario,String pass) {
        MyConnection con = null;
        ResultSet rs = null;
        String bool="false";
        int log=0;

        try {
            con = new MyConnection();
            if (con.getActive()) {

                log = con.login(usuario,pass);
                System.out.println("Tipo de error de inicio de sesion: "+log);
                if(log==1){
                    bool="true";
                    runOnUiThread(new Runnable(){

                        @Override
                        public void run(){
                            //update ui here
                            Toast.makeText(getApplicationContext(), "Usuario identificado", Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Estamos cargando tu información...", Toast.LENGTH_LONG).show();

                        }
                    });
                    getProyectos(con,usuario);
                }
                else bool="false";
            }
        } catch (Exception e) {
            e.printStackTrace();
            bool ="false";


        }
        return bool;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, String> {

        private final String mEmail;
        private final String mPassword;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            is_log=true;
            mUser.setEnabled(false);
            LoginActivity.this.mPassword.setEnabled(false);
            mButton.setText("Cancelar");
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO: attempt authentication against a network service.
            mProgress.setProgress(0);
            log_bool = null;
            log_bool = login(params[0], params[1]);
            //log_bool="true";
            if(log_bool.equalsIgnoreCase("true"));



            for (int i = 0; i < 100; i++) {
                if (!this.isCancelled()) {
                    try {
                        Thread.sleep(20);
                        //System.out.println(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("una falla");
                    }
                    mProgress.setProgress(i);
                }
                else return "cancel";
            }
            mProgress.setProgress(0);
            return log_bool;
        }

        @Override
        protected void onPostExecute(String result) {
            log_bool=result;
            attemptLogin();
            System.out.println("si termino");
            if (log_bool.equalsIgnoreCase("true")) {
                finish();

            } else {
                LoginActivity.this.mPassword.setError(getString(R.string.error_incorrect_password));
                LoginActivity.this.mPassword.requestFocus();
            }

            is_log=false;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            mProgress.setProgress(0);
            mUser.setEnabled(true);
            LoginActivity.this.mPassword.setEnabled(true);
            mButton.setText(getString(R.string.action_sign_in));
            mButton.setEnabled(true);
            is_log=false;
        }
    }

    private void getProyectos(MyConnection con,String user){


        ResultSet rs=null;

        String q_urbanizaciones="select * from dbo.vw_user_urb where usuario= '&PV_USER&'";
        String q_proyectos=  "Select distinct codigo_proyecto,proyecto_libres from dbo.vw_user_proy where usuario='&PV_USER&' and codigo_urbanizacion='2';";
        String q_lotes= "Select codigo_lote,codigo_proyecto,manzana,lote,estado_construccion,area_terreno,plazo_entrada,plazo_entrega "+
                "from dbo.vw_lotes where codigo_urbanizacion='2'";
        String q_modelos="select * from dbo.vw_modelos_en_lot where codigo_lote='&PV_LOTE&'";
        try{

            if(con.getActive()) {

                /*rs=con.consulta(q_urbanizaciones.replace("&PV_USER&", user));
                DbUrbanizacion urb;
                urb=new DbUrbanizacion(this);
                while (rs.next()) {
                    //insertar(String id,String id_curso,String nombre_curso,String titulo,String contenido,Date fecha,String num_msgs)
                    urb.insertar(rs.getString(2),rs.getString(3));
                }*/

                rs=con.consulta(q_proyectos.replace("&PV_USER&", user));
                DbProyecto proy;
                proy=new DbProyecto(this);
                while (rs.next()) {
                    //insertar(String id,String id_curso,String nombre_curso,String titulo,String contenido,Date fecha,String num_msgs)
                    proy.insertar(rs.getString(1),rs.getString(2));
                }
                rs.close();
                rs=null;
                DbLote lote=new DbLote(this);
                DbModelo modelo=new DbModelo(this);
                rs=con.consulta(q_lotes);
                while (rs.next()) {
                    //ResultSet rs2=null;
                    lote.insertar(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));
                    /*rs2=con.consulta(q_modelos.replace("&PV_LOTE&", rs.getString(1)));
                    while(rs2.next()){
                        modelo.insertar(rs2.getString(2),rs2.getString(1),rs2.getString(3),rs2.getString(4),rs2.getString(5),rs2.getString(6),
                                rs2.getString(7),rs2.getString(8),rs2.getString(9),rs2.getString(10),rs2.getString(11),rs2.getString(12),rs2.getString(13)
                                ,rs2.getString(14),rs2.getString(15));
                    }
                    rs2.close();
                    rs2=null;*/
                }
                rs.close();
                rs=null;


                try {

                    Cursor dato = lote.consultarLotePorProy(null);

                    if (dato.moveToFirst()) {
                        //Recorremos el cursor hasta que no haya más registros
                        do {
                            System.out.println(dato.getString(0));
                            rs=con.consulta(q_modelos.replace("&PV_LOTE&", dato.getString(0)));
                            while (rs.next()) {

                                //db.insert(NOMBRE_TABLA,null,generarContentValues(modelo,id_lote,area,pisos,cuota_ent,cuota_ini,tasa,plazo1,
                                //plazo2,plazo3,precio,img_fach,img_pb,img_pa1,img_pa2));
                                modelo.insertar(rs.getString(2),rs.getString(1),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),
                                        rs.getString(7),rs.getString(8),rs.getString(9),rs.getString(10),rs.getString(11),rs.getString(12),rs.getString(13)
                                        ,rs.getString(14),rs.getString(15));

                            }
                            rs.close();
                            rs=null;

                        } while(dato.moveToNext());
                    }

                }catch (Exception e){
                    System.out.println(e.toString());
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {

                if(con!=null)
                    con.close();
                if(rs!=null)
                    rs.close();
            } catch (SQLException ex) {
                //Logger.getLogger(consulta.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
