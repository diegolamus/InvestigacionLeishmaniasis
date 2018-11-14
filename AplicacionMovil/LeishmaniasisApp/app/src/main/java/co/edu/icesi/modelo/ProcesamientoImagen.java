package co.edu.icesi.modelo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.View;


public class ProcesamientoImagen
{
    public static Bitmap recortarImagen(Bitmap image, int startX, int startY, int width, int height){
        Bitmap salida = Bitmap.createBitmap(image, startX,startY,width,height);
        return salida;
    }

    public static Bitmap rotarImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static double obtenerProbabilidad(Context context, Bitmap image) {
        // Variable to save probability
        double probability = Math.random()*100;
        try {

        } catch(Exception e) {
            Log.e("obtener prob", " no paso: " );
        }
        return probability;
    }


    public static Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }

}
