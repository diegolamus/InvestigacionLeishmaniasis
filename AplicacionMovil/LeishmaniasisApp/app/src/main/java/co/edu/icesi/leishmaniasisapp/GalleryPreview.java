package co.edu.icesi.leishmaniasisapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;

import co.edu.icesi.modelo.Area;


public class GalleryPreview extends AppCompatActivity {

    ImageView imagenPrevia;
    String path;
    public static Bitmap recuperaImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();

        setContentView(R.layout.gallery_preview);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        imagenPrevia = (ImageView) findViewById(R.id.GalleryPreviewImg);
        Glide.with(GalleryPreview.this)
                .load(new File(path)) // Uri de la imagen
                .into(imagenPrevia);

        /////////////////////////////////////////////////////////////////////////////////////////
        Area box = new Area(this);
        addContentView(box, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        //////////////////////////////////////////////////////////////////////////////////////////

    }


    public void onClick_Boton2(View v){
        //variable con la imagen
        Bitmap bit= BitmapFactory.decodeResource(getResources(),R.id.GalleryPreviewImg);
       // ByteArrayOutputStream temp= new ByteArrayOutputStream();
//        bit.compress(Bitmap.CompressFormat.PNG,100,temp);
        recuperaImagen=bit;

        Intent intentRecortar = new Intent(GalleryPreview.this, MostrarResultados.class);
        intentRecortar.putExtra("actividad","seleccionarFoto" );
        startActivity(intentRecortar);

    }
}
