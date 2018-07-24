package co.edu.icesi.leishmaniasisapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.edu.icesi.modelo.ProcesamientoImagen;


public class TomarFoto extends AppCompatActivity {

    private static final int FOCUS_AREA_SIZE = 300;
    public static Bitmap img;

    Camera camera;
    FrameLayout layoutCamera;
    MostrarCamara mostrarCamara;
    Button flash;
    Boolean flashEncendido;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto);
        layoutCamera = findViewById(R.id.layoutCamera);
        camera = Camera.open();
        flash = findViewById(R.id.flash);
        mostrarCamara = new MostrarCamara(this, camera);
        mostrarCamara.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    focusOnTouch(motionEvent);
                }
                return true;
            }
        });
        layoutCamera.addView(mostrarCamara);
        flashEncendido=false;
    }

    //Tomar foto
    public void onClick_tomarFoto(View v) {
        if (camera != null) {
            camera.takePicture(null, null, pictureCallBack);
        }
    }
    Camera.PictureCallback pictureCallBack= new Camera.PictureCallback() {
        @Override
        //Recuperar la foto que se tomo y guardarla
        public void onPictureTaken(byte[] bytes, Camera camera) {
            Bitmap imageMap = BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
            if(imageMap.getWidth()> imageMap.getHeight())
                imageMap = ProcesamientoImagen.rotarImage(imageMap,90);
            //TODO recortar imagen
            img=imageMap;
            //Cambiar de pantalla y agregar imagen a intent
            Intent intent = new Intent(TomarFoto.this, MostrarResultados.class);
            intent.putExtra("actividad","tomarfoto");
            startActivity(intent);
            finish();
        }
    };

    //Activar y desactivar flash-------------------------------
    public void activarFlash(View v){
        if(flashEncendido) {
            flash.setBackground(getResources().getDrawable(R.mipmap.flash_off_round));
            apagarFlash();
            flashEncendido=false;
        }else{
            flash.setBackground(getResources().getDrawable(R.mipmap.flash_on_round));
            encenderFlash();
            flashEncendido=true;
        }
    }
    private void encenderFlash(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            camera.stopPreview();
            Camera.Parameters p = camera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            camera.setParameters(p);
            camera.startPreview();
        }
    }
    private void apagarFlash(){
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            camera.stopPreview();
            Camera.Parameters p = camera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.startPreview();
        }
    }

    //Enfocar--------------------------------------------------
    private void focusOnTouch(MotionEvent motionEvent) {
        if (camera != null) {
            camera.cancelAutoFocus();
            Rect focusRect = calculateFocusArea(motionEvent.getX(), motionEvent.getY());
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getFocusMode().equals(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            if (parameters.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> mylist = new ArrayList<>();
                mylist.add(new Camera.Area(focusRect, 1000));
                parameters.setFocusAreas(mylist);
            }
            try {
                camera.cancelAutoFocus();
                camera.setParameters(parameters);
                camera.startPreview();
                camera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (camera.getParameters().getFocusMode().equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                            Camera.Parameters parameters = camera.getParameters();
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                            if (parameters.getMaxNumFocusAreas() > 0) {
                                parameters.setFocusAreas(null);
                            }
                            camera.setParameters(parameters);
                            camera.startPreview();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / mostrarCamara.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / mostrarCamara.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }
    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }
}

@SuppressLint("ViewConstructor")
class MostrarCamara extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder holder;

    public MostrarCamara(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        holder = getHolder();
        holder.addCallback(this);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Camera.Parameters params = camera.getParameters();
        //Establecer la resolucion optima de la camara
        List<Camera.Size> sizes = params.getSupportedPictureSizes();
        int max = Integer.MAX_VALUE;
        Camera.Size preferredSize = null;
        for (Camera.Size size:sizes) {
            if (Math.abs(size.width - params.getPreviewSize().width) < max) {
                preferredSize = size;
                max=Math.abs(size.width - params.getPreviewSize().width);
            }
        }
        //Cambiar la orientacion de la camara
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            camera.setDisplayOrientation(90);
            params.setRotation(90);
        } else {
            params.set("orientation", "landscape");
            camera.setDisplayOrientation(0);
            params.setRotation(0);
        }
        //Establecer parametros
        if (preferredSize != null)
            params.setPictureSize(preferredSize.width, preferredSize.height);
        camera.setParameters(params);
        //Iniciar preview
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

}






