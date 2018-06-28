package co.edu.icesi.leishmaniasisapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MostrarResultados extends AppCompatActivity {

    ImageView fotoDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_resultados);
        byte[] img = getIntent().getByteArrayExtra("imagen");
        fotoDisplay = findViewById(R.id.fotoDisplay);
        Bitmap imageMap = BitmapFactory.decodeByteArray(img, 0,img.length);
        fotoDisplay.setImageBitmap(imageMap);
    }
}
