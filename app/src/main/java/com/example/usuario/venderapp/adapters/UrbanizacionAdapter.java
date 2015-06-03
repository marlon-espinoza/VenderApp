package com.example.usuario.venderapp.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.usuario.venderapp.MainActivity;
import com.example.usuario.venderapp.R;
import com.example.usuario.venderapp.Tab2;
import com.example.usuario.venderapp.Urbanizacion;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by USUARIO on 24-abr-15.
 */
public class UrbanizacionAdapter extends ArrayAdapter<Urbanizacion> {
    Context context;
    Activity a;
    String imageHttpAddress;
    boolean error=false;

    public UrbanizacionAdapter(Context context, ArrayList<Urbanizacion> urbs) {
        super(context, 0, urbs);
        this.context=context;
        a=(Activity)context;
        error=false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Urbanizacion urbanizacion = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.urbanizacion_view,parent,false);
        }
        //return super.getView(position, convertView, parent);
        TextView nombre = (TextView) convertView.findViewById(R.id.nombre_urb);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.img_urb);
        nombre.setText(urbanizacion.getNombre());
        imageHttpAddress=urbanizacion.ruta();
        System.out.println(imageHttpAddress);
        CargarImagenes nT= new CargarImagenes(imageView);
        nT.execute(imageHttpAddress);
        if(error)Toast.makeText(context.getApplicationContext(), "Error cargando imágenes", Toast.LENGTH_LONG).show();
        return convertView;

    }
    
    public class CargarImagenes extends AsyncTask<String,Void,Bitmap> {
        ProgressDialog pDialog;
        ImageView imgageView;



        public CargarImagenes(ImageView imgageView) {
            this.imgageView = imgageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(a);
            pDialog.setMessage("Cargando...");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Log.i("doInBackground", "Entra en background");
            String url = params[0];
            Bitmap image;
            image = downloadFile(url);

            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgageView.setImageBitmap(bitmap);
            pDialog.dismiss();


        }
        private Bitmap downloadFile (String imageHttpAddress){

            Bitmap imagen=null;
            try{
                URL imageUrl = new URL(imageHttpAddress);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                conn.connect();
                imagen = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException e) {
                //Toast.makeText(context.getApplicationContext(), "Error cargando imagen", Toast.LENGTH_LONG).show();

                error=true;
                e.printStackTrace();
            }

            return imagen;
        }
    }
}
