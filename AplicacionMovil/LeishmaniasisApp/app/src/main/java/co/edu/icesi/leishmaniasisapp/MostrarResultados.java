package co.edu.icesi.leishmaniasisapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private static final int INTERNET_REQUEST = 108;

    ImageView fotoDisplay;
    Bitmap img;
    TextView textoProbabilidad;
    String nameCarpeta; //nombre del directorio
    File carpeta;
    EditText ed_nombreImg;
    double probabilidad=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_resultados);
        // Pedir permisos de camara
        // Check if permisions are granted
        int checkPermisos = ContextCompat.checkSelfPermission(MostrarResultados.this, Manifest.permission.INTERNET);
        // if permision is not granted we should ask for it
        if(checkPermisos != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MostrarResultados.this,
                    new String[]{Manifest.permission.INTERNET},
                    INTERNET_REQUEST);
        } else {
            cargarActividad();
        }
    }

    private void cargarActividad(){
        nameCarpeta = "leishApp";
        carpeta = new File(Environment.getExternalStorageDirectory().toString() + "/" + nameCarpeta);
        if (!carpeta.exists()) carpeta.mkdirs();

        //Obtener la foto de la actividad anterior
        if (getIntent().getStringExtra("actividad").equals("tomarfoto"))
            img = TomarFoto.img;
        else
            img = GalleryPreview.recuperaImagen;
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


    // Permisions callback
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case INTERNET_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cargarActividad();
                } else {
                    // permission denied, return
                    onBackPressed();
                }
                return;
            }
        }
    }

    //pide el nombre de la imagen
    public void onClick_Aceptar(View v){

        AlertDialog.Builder informacion= new AlertDialog.Builder(MostrarResultados.this);
        View aView=getLayoutInflater().inflate(R.layout.activity_alert,null);
        ed_nombreImg = findViewById(R.id.ed_text);
        Button btn_aceptar=aView.findViewById(R.id.btn_aceptar);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_Guardar(v);
            }
        });
        informacion.setView(aView);
        AlertDialog dialog=informacion.create();
        dialog.show();
    }

    //Guadar foto
    public void onClick_Guardar(View v) {
        if(carpeta.exists()) {
            try {
                // String name= ed_nombreImg.getText().toString();
                String fotoName = probabilidad + UUID.randomUUID().toString() + ".png";
                String path = carpeta + "/" + fotoName;
                FileOutputStream foto = new FileOutputStream(new File(path));
                img.compress(Bitmap.CompressFormat.PNG, 100, foto);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
                Toast.makeText(MostrarResultados.this, "Imagen guardada", Toast.LENGTH_SHORT).show();
                notifyMediaStoreScanner(new File(path));
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



    public final void notifyMediaStoreScanner(final File file) {
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), file.getName(), null);
            this.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}

