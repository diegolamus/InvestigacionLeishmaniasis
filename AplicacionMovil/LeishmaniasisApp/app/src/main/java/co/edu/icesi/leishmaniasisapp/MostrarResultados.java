package co.edu.icesi.leishmaniasisapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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

public class MostrarResultados extends AppCompatActivity implements View.OnClickListener{

    private static final int INTERNET_REQUEST = 108;

    ImageView fotoDisplay;
    Bitmap img;
    TextView textoProbabilidad;
    String nameCarpeta; //nombre del directorio
    File carpeta;
    double probabilidad=0.0;
    EditText ed_textNombre;
    EditText ed_textNumLesion;
    String nombre="";
    String numero="";

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


    //Retornar a inicio
    public void onClick_Inicio(View v){
        onBackPressed();
    }

    //pide informacion para guardar la imagen
    public void onClick_Guardar(View v){

        AlertDialog.Builder informacion= new AlertDialog.Builder(MostrarResultados.this);
        View dialog=getLayoutInflater().inflate(R.layout.activity_alert,null);
        Button btn_aceptar=dialog.findViewById(R.id.btn_aceptar);
        ed_textNombre=dialog.findViewById(R.id.ed_textNombre);
        ed_textNumLesion=dialog.findViewById(R.id.ed_textNumLesion);
        btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre = ed_textNombre.getText().toString();
                numero = ed_textNumLesion.getText().toString();
                if(nombre.isEmpty() || numero.isEmpty()){
                    Toast.makeText(MostrarResultados.this, "Debe ingresar la información requeridad", Toast.LENGTH_LONG).show();
                }else{
                    onClick_Aceptar(v);
                }

            }
        });
        informacion.setView(dialog);
        AlertDialog alertDialog=informacion.create();
        alertDialog.show();
    }

    //Guadar foto
    public void onClick_Aceptar(View v) {
        if(carpeta.exists()) {
            try {
                String fotoName = nombre+"-"+numero+"-"+ Math.round(probabilidad * 100)/ 100.0 + "%" +"-"+UUID.randomUUID().toString() + ".png";
                String path = carpeta + "/" + fotoName;
                FileOutputStream foto = new FileOutputStream(new File(path));
                img.compress(Bitmap.CompressFormat.PNG, 100, foto);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
                notifyMediaStoreScanner(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MostrarResultados.this);
        View mView=getLayoutInflater().inflate(R.layout.activity_alert_dialog,null);
        Button btn_inicio= mView.findViewById(R.id.btn_inicio);
        btn_inicio.setOnClickListener(this);

        mBuilder.setView(mView);
        AlertDialog alDialog=mBuilder.create();
        alDialog.show();
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

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btn_inicio){
            onBackPressed();
        }

    }
}

