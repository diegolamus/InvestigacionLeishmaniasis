package co.edu.icesi.leishmaniasisapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MostrarResultados extends AppCompatActivity {

    ImageView fotoDisplay;
    byte[] img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_resultados);
        //Obtener la foto de la actividad anterior
        if(getIntent().getStringExtra("actividad").equals("tomarfoto"))
            img = TomarFoto.img;
        else
            img=null; //TODO recuperar foto de SeleccionarFoto
        fotoDisplay = findViewById(R.id.fotoDisplay);
        Bitmap imageMap = BitmapFactory.decodeByteArray(img, 0,img.length);
        fotoDisplay.setImageBitmap(imageMap);
    }
}
