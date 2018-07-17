package co.edu.icesi.modelo;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ProcesamientoImagen
{
    public static Bitmap recortarImagen(Bitmap image){
        //TODO
        return null;
    }

    public static Bitmap rotarImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static double obtenerProbabilidad(Bitmap image){
        //TODO obtener predicción
        return Math.random()*100;
    }
}
