package co.edu.icesi.leishmaniasisapp;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.List;


public class TomarFoto extends AppCompatActivity {

    private static final int FOCUS_AREA_SIZE = 300;

    Camera camera;
    FrameLayout layoutCamera;
    MostrarCamara mostrarCamara;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomar_foto);
        layoutCamera = findViewById(R.id.layoutCamera);
        camera = Camera.open();
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
    }

    public void onClick_tomarFoto(View v) {
        //Tomar la foto
        if (camera != null) {
            camera.takePicture(null, null, pictureCallBack);
        }
    }

    //Recuperar y guardar la foto
    Camera.PictureCallback pictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            //TODO convertir bytes a foto y enviar a resultados
        }
    };

    //Enfocar
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







