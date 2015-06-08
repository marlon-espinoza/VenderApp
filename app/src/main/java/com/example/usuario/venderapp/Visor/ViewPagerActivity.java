package com.example.usuario.venderapp.Visor;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.example.usuario.venderapp.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends ActionBarActivity {
	ArrayList<String> myPaths;
    String IMAGEN_1="fachada";
    String IMAGEN_2="planta1";
    String IMAGEN_3="planta2";

	/**
	 * Step 1: Download and set up v4 support library: http://developer.android.com/tools/support-library/setup.html
	 * Step 2: Create ExtendedViewPager wrapper which calls TouchImageView.canScrollHorizontallyFroyo
	 * Step 3: ExtendedViewPager is a custom view and must be referred to by its full package name in XML
	 * Step 4: Write TouchImageAdapter, located below
	 * Step 5. The ViewPager in the XML should be ExtendedViewPager
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPaths=new ArrayList<String>();
        ArrayList<Bitmap>bitmaps=new ArrayList<Bitmap>();
        String modeloFile= this.getIntent().getStringExtra("nombre_file");
        String index= this.getIntent().getStringExtra("index");
        ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
        File dirImages = cw.getDir(modeloFile, Context.MODE_PRIVATE);
        if (dirImages.exists()){
            File[] ficheros = dirImages.listFiles();
            for (int x=0;x<ficheros.length;x++){
                myPaths.add(ficheros[x].getAbsolutePath());
                BufferedInputStream buf;
                FileInputStream fis;
                try{
                    fis = new FileInputStream(ficheros[x].getAbsolutePath());
                    buf = new BufferedInputStream(fis);
                    bitmaps.add(BitmapFactory.decodeStream(buf));


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
            }
        }

        setContentView(R.layout.activity_viewpager_example);
        ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
        //int[] r={R.drawable.imagen_no_encontrada,R.drawable.arena_a_14fa};
        mViewPager.setAdapter(new TouchImageAdapter(myPaths));
        mViewPager.setCurrentItem(getIndex(index));

    }
    private int getIndex(String tipo_imagen){
        if (tipo_imagen.equals(IMAGEN_1))
            return 0;
        else if(tipo_imagen.equals(IMAGEN_2))
            return 1;
        else if(tipo_imagen.equals(IMAGEN_3))
            return 2;
        else
            return 0;

    }

    static class TouchImageAdapter extends PagerAdapter {

        private static int[] images = {};

        private static ArrayList<Bitmap> bitmaps;
        private static ArrayList<String> paths;

        public TouchImageAdapter(ArrayList<String> Paths) {
            paths=Paths;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
        	return paths.size();
        }


        @Override
        public View instantiateItem(ViewGroup container, int position) {
            TouchImageView img = new TouchImageView(container.getContext());
            String myPath= paths.get(position);
            BufferedInputStream buf;
            FileInputStream fis;
            Bitmap bmp;
            try{
                fis = new FileInputStream(myPath);
                buf = new BufferedInputStream(fis);
                bmp = BitmapFactory.decodeStream(buf);
                img.setImageBitmap(bmp);

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


            //img.setImageResource(images[position]);

            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

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
