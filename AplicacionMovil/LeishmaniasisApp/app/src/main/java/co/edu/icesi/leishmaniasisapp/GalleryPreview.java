package co.edu.icesi.leishmaniasisapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;

import co.edu.icesi.modelo.ProcesamientoImagen;


public class GalleryPreview extends AppCompatActivity {

    ImageView imagenPrevia;
    public static String path;
    public static Bitmap recuperaImagen;
    ZoomageView GalleryPreviewImg;
    TextView squa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_preview);

        squa = findViewById(R.id.squa);
        GalleryPreviewImg = findViewById(R.id.GalleryPreviewImg);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        imagenPrevia = (ImageView) findViewById(R.id.GalleryPreviewImg);


        Bitmap prueba = BitmapFactory.decodeFile(path);
        if (prueba.getWidth() < prueba.getHeight()) {
            prueba = ProcesamientoImagen.rotarImage(prueba, 90);
        }
        double prop = prueba.getHeight() / prueba.getWidth();

        if (prueba.getWidth() < GalleryPreviewImg.getWidth()) {
            prueba = Bitmap.createScaledBitmap(prueba, (int) (1.2 * GalleryPreviewImg.getWidth()), (int) (1.2 * GalleryPreviewImg.getWidth() * prop), false);
        }
        GalleryPreviewImg.setImageBitmap(prueba);

        /*
        Glide.with(GalleryPreview.this)
                .load(new File(path)) // ruta de la imagen
                .into(imagenPrevia);
        */

    }


    public void onClick_Boton2(View v){

        //variable con la imagen

        Bitmap prueba = BitmapFactory.decodeFile(path);

        //En el caso que se cargue una imagen donde el alto sea mayor al ancho, entonces se rota 90 grados.
        if (prueba.getWidth() < prueba.getHeight()) {
            prueba = ProcesamientoImagen.rotarImage(prueba, 90);
        }
        double prop = prueba.getHeight() / prueba.getWidth();
        // si la imagen es mas hancha que el view e imagen previa se recalcula la escala deacuerdo a la proporcion.
        if (prueba.getWidth() < GalleryPreviewImg.getWidth()) {
            prueba = Bitmap.createScaledBitmap(prueba, (int) (1.2 * GalleryPreviewImg.getWidth()), (int) (1.2 * GalleryPreviewImg.getWidth() * prop), false);
        }


        float lado = 1;
        float startX = 0;
        double startY = 0;

        double factor_conversion = 1;

        startX = squa.getX();
        factor_conversion = (double) prueba.getWidth() / GalleryPreviewImg.getWidth();
        double alto_px = prueba.getHeight() / factor_conversion;
        double A = GalleryPreviewImg.getHeight() - alto_px;
        startY = squa.getY() - A / 2;
        lado = squa.getWidth();


        Log.e("prueba", "" + factor_conversion);
        Log.e("prueba", "" + lado);
        Log.e("prueba", "" + startY);
        Log.e("prueba", "" + prueba.getWidth());


        int startX_convertido = (int) (startX * factor_conversion);
        int startY_convertido = (int) (startY * factor_conversion);
        int width_convertido = (int) (lado * factor_conversion);
        int height_convertido = (int) (lado * factor_conversion);


        Bitmap recortada = ProcesamientoImagen.recortarImagen(prueba, startX_convertido, startY_convertido, width_convertido, height_convertido);
        imagenPrevia.setImageBitmap(recortada);

        //variable con la imagen recortada
        recuperaImagen = recortada;

        Intent intentRecortar = new Intent(GalleryPreview.this, MostrarResultados.class);
        intentRecortar.putExtra("actividad", "seleccionarFoto");
        startActivity(intentRecortar);

    }

}
