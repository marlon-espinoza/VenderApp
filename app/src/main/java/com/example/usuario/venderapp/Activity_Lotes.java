package com.example.usuario.venderapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.venderapp.DataBase.DbLote;
import com.example.usuario.venderapp.Visor.SingleTouchImageViewActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/*
    Esta clase muestra los lotes de una urbanización y muestra el mapa de la misma urbanización
    para esto se hace una búsqueda en la base interna donde están almacendos los lotes de la misma y
    se dispone de una ruta donde están alojadas estas imágenes. Si la imagen no ha sido abierta, se
    la carga de la web y se la guarda internamente en una carpeta en modo privado.
    La búsqueda de la imagen lo realiza usando el id de la urbanización por ello tiene una dependencia
    directa con la ruta donde está almacenado el cual debe ser mantenido constantemente. La imagen deberá
    ser almacenada con el id de la urbanizacion y con extensión jpg.

*/
public class Activity_Lotes extends ActionBarActivity {
    ArrayList<String[]> list;
    private String id;
    private TextView fallido;
    private TextView nombreUrb;
    private String imageHttpAddress = "http://ciudadceleste.com/Apps/Images/arboleda.jpg";
    private String IMAGENES_LOTES = "ImagenesLotes";

    private boolean mContentLoaded;
    private ImageView mImageView;
    private View mLoadingView;
    private int mShortAnimationDuration;
    String path=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lotes);
        Intent intent= getIntent();
        id=intent.getStringExtra("id_urb");
        nombreUrb=(TextView)findViewById(R.id.nombre_urbanizacion);
        nombreUrb.setText(intent.getStringExtra("nombre_urb"));
        //lv=(ListView)findViewById(R.id.listLotesView);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mLoadingView = findViewById(R.id.loading_spinner);
        mLoadingView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        fallido=(TextView)findViewById(R.id.fallida);
        path=abrirImagen(getApplicationContext(),id,mImageView);
        if(path==null) {
            mLoadingView.setVisibility(View.VISIBLE);
            CargarImagenes nT = new CargarImagenes();
            nT.execute(imageHttpAddress);
        }

        if(path!=null) {
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SingleTouchImageViewActivity.class);


                    intent.putExtra("ruta", path);
                    startActivity(intent);

                }
            });
        }




        TableLayout tb=(TableLayout)findViewById(R.id.tablaLotes);
        list=new ArrayList<String[]>();
        DbLote dbLote=null;
        try {
            dbLote = new DbLote(this);
            Cursor dato = dbLote.consultarLotePorProy(id);

            if (dato.moveToFirst()) {
                findViewById(android.R.id.empty).setVisibility(View.GONE);
                int i=0;
                //Recorremos el cursor hasta que no haya más registros
                do {
                    final TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.table_row_lote, null);
                    final String idLote=dato.getString(0);
                    TextView tv;
                    if(i%2==1)tr.setBackgroundResource(R.drawable.selector_lista_con_azul);
                    tv = (TextView) tr.findViewById(R.id.mz);
                    tv.setText(dato.getString(1));
                    tv = (TextView) tr.findViewById(R.id.lt);
                    tv.setText(dato.getString(2));
                    tv = (TextView) tr.findViewById(R.id.plazoEntrada);
                    tv.setText(dato.getString(3));
                    tv = (TextView) tr.findViewById(R.id.plazoEntrega);
                    tv.setText(dato.getString(4));
                    tv = (TextView) tr.findViewById(R.id.area);
                    tv.setText(dato.getString(5));
                    tv = (TextView) tr.findViewById(R.id.estado);
                    tv.setText(dato.getString(6));
                    tb.addView(tr);
                    registerForContextMenu(tr);
                    tr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getApplicationContext(), Activity_Modelos.class);
                            intent.putExtra("id_lote", idLote);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(intent);                       }
                    });
                    i++;
                } while(dato.moveToNext());
            }

        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbLote!=null)
                dbLote.close();
        }
        //ListaLoteAdapter lotesAdp=new ListaLoteAdapter(this.getApplicationContext(),list);
        //lv.setAdapter(lotesAdp);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_lotes, menu);
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
        else if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class CargarImagenes extends AsyncTask<String,Void,Bitmap> {
        //ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showContentOrLoadingIndicator(mContentLoaded);

            /*pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();*/
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.i("doInBackground", "Entra en background");
            String url = params[0];
            Bitmap image;
            image = downloadFile(url);
            //Después de descargar la imagen se la guarda en la memoria privada
            //y se asigna el listener sobre imageview para abrir el visor.
            if (image!=null) {
                path = guardarImagen(getApplicationContext(), id, image);
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), SingleTouchImageViewActivity.class);


                        intent.putExtra("ruta", path);
                        startActivity(intent);

                    }
                });
            }
            //Toast.makeText(getApplicationContext(), ruta, Toast.LENGTH_LONG).show();

            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mContentLoaded = !mContentLoaded;
            showContentOrLoadingIndicator(mContentLoaded);
            if (bitmap!=null)
                mImageView.setImageBitmap(bitmap);
            else{
                Drawable myDrawable = getResources().getDrawable(R.drawable.imagen_no_encontrada);
                bitmap=((BitmapDrawable)myDrawable).getBitmap();
                mImageView.setImageBitmap(bitmap);
            }

            //pDialog.dismiss();

        }
        private Bitmap downloadFile (String imageHttpAddress){

            Bitmap imagen=null;

            try{
                if(exists(imageHttpAddress)) {

                    URL imageUrl = new URL(imageHttpAddress);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();
                    imagen = BitmapFactory.decodeStream(conn.getInputStream());
                }
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Error cargando imagen"+e.getMessage(),Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(),"No se pudo conectar",Toast.LENGTH_SHORT).show();
                        fallido.setVisibility(View.VISIBLE);
                    }
                });

                e.printStackTrace();
            }
            return imagen;
        }
    }
    private void showContentOrLoadingIndicator(boolean contentLoaded) {
        // Decide which view to hide and which to show.
        final View showView = contentLoaded ? mImageView : mLoadingView;
        final View hideView = contentLoaded ? mLoadingView : mImageView;

        // Set the "show" view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        showView.setAlpha(0f);
        showView.setVisibility(View.VISIBLE);

        // Animate the "show" view to 100% opacity, and clear any animation listener set on
        // the view. Remember that listeners are not limited to the specific animation
        // describes in the chained method calls. Listeners are set on the
        // ViewPropertyAnimator object for the view, which persists across several
        // animations.
        showView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the "hide" view to 0% opacity. After the animation ends, set its visibility
        // to GONE as an optimization step (it won't participate in layout passes, etc.)
        hideView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hideView.setVisibility(View.GONE);
                    }
                });
    }
    private String guardarImagen (Context context, String nombre, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir(IMAGENES_LOTES, Context.MODE_PRIVATE);
        File myPath = new File(dirImages, nombre + ".png");

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return myPath.getAbsolutePath();
    }
    private String abrirImagen(Context context, String nombre, ImageView imageView){
        String proceso=null;
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir(IMAGENES_LOTES, Context.MODE_PRIVATE);
        File myPath = new File(dirImages, nombre + ".png");
        BufferedInputStream buf;
        FileInputStream fis;
        Bitmap bmp;
        try{
            fis = new FileInputStream(myPath);
            buf = new BufferedInputStream(fis);
            bmp = BitmapFactory.decodeStream(buf);
            imageView.setImageBitmap(bmp);
            proceso=myPath.getAbsolutePath();

            if (fis != null) {
                fis.close();
            }
            if (buf != null) {
                buf.close();
            }
        }catch (FileNotFoundException ex){

            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return proceso;
    }
    public static boolean exists(String URLName){
        try {
            HttpURLConnection.setFollowRedirects(false);
            // note : you may also need
            //        HttpURLConnection.setInstanceFollowRedirects(false)
            HttpURLConnection con =
                    (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
