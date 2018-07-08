package co.edu.icesi.leishmaniasisapp;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MostrarResultados extends AppCompatActivity {

    ImageView fotoDisplay;
    byte[] img;
    ProgressBar progressBarProbabilidad;
    TextView textoProbabilidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_resultados);
        //Obtener la foto de la actividad anterior
        if(getIntent().getStringExtra("actividad").equals("tomarfoto"))
            img = TomarFoto.img;
        else
            img=null; //TODO recuperar foto de SeleccionarFoto
        //Desplegar la foto en pantalla
        fotoDisplay = findViewById(R.id.fotoDisplay);
        Bitmap imageMap = BitmapFactory.decodeByteArray(img, 0,img.length);
        fotoDisplay.setImageBitmap(imageMap);
        //Ajustar la barra de progreso y texto de probabilidad
        progressBarProbabilidad = findViewById(R.id.progressBar);
        textoProbabilidad = findViewById(R.id.textoProbabilidad);
        //Ingresar probabilidad a barra de progreso y texto
        double probabilidad = obtenerProbabilidad();
        progressBarProbabilidad.setProgress((int)probabilidad);
        textoProbabilidad.setText("Probabilidad de infección: "+ Math.round(probabilidad*100)/100.0+ "%");
    }

    //Retornar a inicio
    public void onClick_Inicio(View v){
        onBackPressed();
    }

    public void onClick_Guardar(View v){
        //TODO guadar foto
    }

    private double obtenerProbabilidad(){
        //TODO obtener predicción
        return Math.random()*100;
    }
}
