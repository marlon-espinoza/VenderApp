package com.example.usuario.venderapp.Visor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.usuario.venderapp.R;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;


public class SingleTouchImageViewActivity extends ActionBarActivity {
	
	private TouchImageView image;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_touchimageview);
		//
		// DecimalFormat rounds to 2 decimal places.
		//

		image = (TouchImageView) findViewById(R.id.img);
        //Captura el path donde esta alojada la imagen del lote
        String myPath= this.getIntent().getStringExtra("ruta");
        BufferedInputStream buf;
        FileInputStream fis;
        Bitmap bmp;
        try{
            fis = new FileInputStream(myPath);
            buf = new BufferedInputStream(fis);
            bmp = BitmapFactory.decodeStream(buf);
            image.setImageBitmap(bmp);

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
        //Bitmap bitmap = intent.getParcelableExtra("BitmapImage");

		// Set the OnTouchImageViewListener which updates edit texts
		// with zoom and scroll diagnostics.
		//
		/*image.setOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
			
			@Override
			public void onMove() {
				PointF point = image.getScrollPosition();
				RectF rect = image.getZoomedRect();
				float currentZoom = image.getCurrentZoom();
				boolean isZoomed = image.isZoomed();
				scrollPositionTextView.setText("x: " + df.format(point.x) + " y: " + df.format(point.y));
				zoomedRectTextView.setText("left: " + df.format(rect.left) + " top: " + df.format(rect.top)
						+ "\nright: " + df.format(rect.right) + " bottom: " + df.format(rect.bottom));
				currentZoomTextView.setText("getCurrentZoom(): " + currentZoom + " isZoomed(): " + isZoomed);
			}
		});*/
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
        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
