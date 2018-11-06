package co.edu.icesi.leishmaniasisapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.UUID;

import co.edu.icesi.modelo.ProcesamientoImagen;

public class MostrarResultados extends AppCompatActivity {

    ImageView fotoDisplay;
    Bitmap img;
    TextView textoProbabilidad;
    String nameCarpeta; //nombre del directorio
    File carpeta;
    double probabilidad=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_resultados);

        nameCarpeta = "leishApp";
        carpeta = new File(Environment.getExternalStorageDirectory().toString() + "/" + nameCarpeta);
        if (!carpeta.exists()) carpeta.mkdirs();

        //Obtener la foto de la actividad anterior
        if (getIntent().getStringExtra("actividad").equals("tomarfoto"))
            img = TomarFoto.img;
        else
            img = GalleryPreview.recuperaImagen; //TODO recuperar foto de SeleccionarFoto
        //Desplegar la foto en pantalla
        fotoDisplay = findViewById(R.id.fotoDisplay);
        fotoDisplay.setImageBitmap(img);
        //Ajustar la barra de progreso y texto de probabilidad
        // progressBarProbabilidad = findViewById(R.id.progressBar);
        textoProbabilidad = findViewById(R.id.textoProbabilidad);
        //Ingresar probabilidad a barra de progreso y texto
        probabilidad = ProcesamientoImagen.obtenerProbabilidad(getApplicationContext(), img);
        // progressBarProbabilidad.setProgress((int)probabilidad);
        textoProbabilidad.setText("" + Math.round(probabilidad * 100) / 100.0 + "%");
    }


    //Guadar foto
    public void onClick_Guardar(View v) {
        if(carpeta.exists()) {
            try {
                String fotoName = probabilidad + UUID.randomUUID().toString() + ".png";
                String path = carpeta + "/" + fotoName;
                FileOutputStream foto = new FileOutputStream(new File(path));
                img.compress(Bitmap.CompressFormat.PNG, 100, foto);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
                Toast.makeText(MostrarResultados.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MostrarResultados.this);
        View mView=getLayoutInflater().inflate(R.layout.activity_alert_dialog,null);
        Button btn_inicio= mView.findViewById(R.id.btn_inicio);
        btn_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog=mBuilder.create();
        dialog.show();
    }


}

