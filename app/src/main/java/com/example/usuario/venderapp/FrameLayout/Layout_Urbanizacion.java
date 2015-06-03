package com.example.usuario.venderapp.FrameLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by USUARIO on 27-abr-15.
 */
public class Layout_Urbanizacion extends FrameLayout {
    private TextView nombre;
    private ImageView imagen;

    public Layout_Urbanizacion(Context context, AttributeSet attrs, TextView nombre) {
        super(context, attrs);
        this.nombre = nombre;
    }

    public Layout_Urbanizacion(Context context, AttributeSet attrs, int defStyleAttr, TextView nombre) {
        super(context, attrs, defStyleAttr);
        this.nombre = nombre;
    }


    public Layout_Urbanizacion(Context context) {
        super(context);
    }
}
