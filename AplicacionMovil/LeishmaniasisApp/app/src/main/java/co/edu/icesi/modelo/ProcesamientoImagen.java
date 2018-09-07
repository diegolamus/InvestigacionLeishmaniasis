package co.edu.icesi.modelo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class ProcesamientoImagen
{


    public static Bitmap recortarImagen(Bitmap image, int startX, int startY, int width, int height){
        Bitmap salida = Bitmap.createBitmap(image, startX,startY,width,height);
        return salida;
    }

    public static Bitmap recortarImagen_TomarFoto(Bitmap image, int startX, int startY, int width, int height){
        Bitmap salida = Bitmap.createBitmap(image, startX,startY,width,height);
        return salida;
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
