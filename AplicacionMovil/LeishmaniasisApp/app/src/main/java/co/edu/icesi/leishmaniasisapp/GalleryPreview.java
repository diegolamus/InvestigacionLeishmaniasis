package co.edu.icesi.leishmaniasisapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import co.edu.icesi.modelo.ProcesamientoImagen;


public class GalleryPreview extends AppCompatActivity {

    ImageView imagenPrevia;
    public static String path;
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
                .load(new File(path)) // ruta de la imagen
                .into(imagenPrevia);

    }


    public void onClick_Boton2(View v){

        //variable con la imagen

        Bitmap prueba =BitmapFactory.decodeFile(path);
        recuperaImagen=prueba;

        Intent intentRecortar = new Intent(GalleryPreview.this, MostrarResultados.class);
        intentRecortar.putExtra("actividad","seleccionarFoto" );
        startActivity(intentRecortar);



    }

}
