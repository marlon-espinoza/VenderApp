package com.example.usuario.venderapp.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.usuario.venderapp.DataBase.DbParametros;
import com.example.usuario.venderapp.R;
import com.example.usuario.venderapp.Visor.SingleTouchImageViewActivity;
import com.example.usuario.venderapp.Visor.ViewPagerActivity;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USUARIO on 03-jun-15.
 */
//    String url="http://200.93.200.156/sitio1/Imagenes/";
//      Esta clase recibe el id de la urbanización para consultar en la tabla parámetros
//      su correspondiente localización.

public class ImagenModeloAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String[]>Lista;
    private ImageView mImageView;
    private String NOMBRE_MODELOS_FILE="ImagenesModelo";
    String id_urb;
    String path=null;
    String id_modelo,nombre_modelo,imagen_modelo,tipo_imagen;
    String url;
    public ImagenModeloAdapter(Context context, ArrayList<String[]> Lista,String id_urb) {
        this.Lista=Lista;
        this.context=context;
        this.id_urb=id_urb;

    }

    @Override
    public int getCount() {
        return Lista.size();
    }

    @Override
    public Object getItem(int position) {
        return Lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String[] data=(String[])getItem(position);
        id_modelo=data[0];
        nombre_modelo=data[1];
        imagen_modelo=data[2];
        tipo_imagen=data[3];

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.imagen_modelo_adapter,parent,false);
        }
        DbParametros dbParametros=null;
        try{
            dbParametros= new DbParametros(context);
            Cursor dato = dbParametros.consultar("0");
            if (dato.moveToFirst())
                url = dato.getString(0);
        }catch (Exception e){
            System.out.println(e.toString());
        }finally {
            if(dbParametros!=null)
                dbParametros.close();
        }
        mImageView=(ImageView)convertView.findViewById(R.id.imgModelo);
        convertView.findViewById(R.id.loading_spinner_modelo).setVisibility(View.GONE);
        path=abrirImagen(context.getApplicationContext(),nombre_modelo,tipo_imagen,mImageView);
        if(path==null) {
            //mLoadingView.setVisibility(View.VISIBLE);
            CargarImagenes nT = new CargarImagenes(mImageView,tipo_imagen,convertView);
            System.out.println(url + imagen_modelo);
            if(imagen_modelo!=null)
            nT.execute(url + imagen_modelo.replace(" ","%20"));
            else
                nT.execute(url + imagen_modelo);
        }


        return convertView;
    }
    public class CargarImagenes extends AsyncTask<String,Void,Bitmap> {

        ImageView imgageView;
        //private boolean mContentLoaded;
        private View mLoadingView;
        //private int mShortAnimationDuration;
        private String tipo_imagen;
        public CargarImagenes(ImageView imgageView,String tipo_imagen,View convertView) {
            this.imgageView = imgageView;
            this.tipo_imagen=tipo_imagen;
            mLoadingView=convertView.findViewById(R.id.loading_spinner_modelo);
            //mLoadingView.setVisibility(View.VISIBLE);
            //mShortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingView.setVisibility(View.VISIBLE);
            //showContentOrLoadingIndicator(mContentLoaded);

            /*pDialog = new ProgressDialog(context);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();*/
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.i("doInBackground", "Entra en background");
            String url = params[0];
            final Bitmap image;
            image = downloadFile(url);
            //Después de descargar la imagen se la guarda en la memoria privada
            //y se asigna el listener sobre imageview para abrir el visor.
            if (image!=null) {
                path = guardarImagen(context.getApplicationContext(),nombre_modelo,this.tipo_imagen, image);
                imgageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context.getApplicationContext(), ViewPagerActivity.class);

                        intent.putExtra("nombre_file", NOMBRE_MODELOS_FILE+nombre_modelo);
                        intent.putExtra("index",tipo_imagen);
                        context.startActivity(intent);

                    }
                });
            }
            //Toast.makeText(getApplicationContext(), ruta, Toast.LENGTH_LONG).show();

            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mLoadingView.setVisibility(View.GONE);
            //mContentLoaded = !mContentLoaded;
            //showContentOrLoadingIndicator(mContentLoaded);
            if (bitmap!=null)
                imgageView.setImageBitmap(bitmap);
            else{
                Drawable myDrawable = context.getResources().getDrawable(R.drawable.imagen_no_encontrada);
                bitmap=((BitmapDrawable)myDrawable).getBitmap();
                imgageView.setImageBitmap(bitmap);
            }

            //pDialog.dismiss();

        }
        private Bitmap downloadFile (String imageHttpAddress){

            Bitmap imagen=null;

            try{

                HttpURLConnection.setFollowRedirects(false);
                URL imageUrl = new URL(imageHttpAddress);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                System.out.println(conn.getResponseCode());
                conn.connect();
                imagen = BitmapFactory.decodeStream(conn.getInputStream());


            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Error cargando imagen"+e.getMessage(),Toast.LENGTH_LONG).show();

                //Toast.makeText(context.getApplicationContext(), "No hay conexion", Toast.LENGTH_SHORT).show();



                e.printStackTrace();
            }
            return imagen;
        }

    }


    private String guardarImagen (Context context, String nombre_modelo,String tipo_foto, Bitmap imagen){
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir(NOMBRE_MODELOS_FILE+nombre_modelo, Context.MODE_PRIVATE);
        if(!dirImages.exists()){
            dirImages.mkdir();
        }
        File myPath = new File(dirImages, tipo_foto + ".png");

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
    private String abrirImagen(final Context context, final String nombre_modelo, final String tipo_foto, ImageView imageView){
        String proceso=null;
        ContextWrapper cw = new ContextWrapper(context);
        File dirImages = cw.getDir(NOMBRE_MODELOS_FILE+nombre_modelo, Context.MODE_PRIVATE);
        File myPath = new File(dirImages, tipo_foto + ".png");
        try{
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(myPath);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < 200
                        || height_tmp / 2 < 200)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(myPath);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            //bmp = BitmapFactory.decodeStream(buf);
            imageView.setImageBitmap(bitmap);
            proceso=myPath.getAbsolutePath();




        }catch (FileNotFoundException ex){

            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        if(proceso!=null) {
            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ViewPagerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("nombre_file", NOMBRE_MODELOS_FILE+nombre_modelo);
                    intent.putExtra("index",tipo_foto);
                    context.startActivity(intent);

                }
            });
        }
        return proceso;
    }


}
