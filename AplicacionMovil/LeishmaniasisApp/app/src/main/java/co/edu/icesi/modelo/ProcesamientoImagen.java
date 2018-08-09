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



    public static Bitmap recortarImagen(Bitmap image){
        //ruta de la imagen original
        //obtengo un mapa de bits y creo otro mapa de bits de salida con la forma de recorte
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        /////////////necesito q la ruta llegue!!!!
        //path = getIntent().getStringExtra("ruta");
        //Bitmap imagOriginal=BitmapFactory.
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imagen = stream.toByteArray();
        byte[] imagenString = Base64.encode(imagen, Base64.DEFAULT);
        //falla... cambiar la forma de decodificar
        image= BitmapFactory.decodeFile(imagenString.toString());
        Bitmap imagOriginal=image;
        int ancho=300;
        int alto=300;
        Bitmap salida= Bitmap.createBitmap(ancho,alto, Bitmap.Config.ARGB_8888);
        RectF recft= new RectF(40,70,300,300);//ubicacion en la pantalla
        Canvas canvas = new Canvas(salida);
        Path pt=new Path();
        pt.addRect(recft,Path.Direction.CW);
        canvas.clipPath(pt);
        canvas.drawBitmap(salida,new Rect(40,70,imagOriginal.getWidth(),imagOriginal.getHeight()),
                new Rect(0,0,ancho,alto),paint);

        Matrix matrix = new Matrix();
        matrix.postScale(1f, 1f);
        Bitmap resizedBitmap = Bitmap.createBitmap(salida, 0, 0, 300, 300, matrix, true);
        BitmapDrawable bd = new BitmapDrawable(resizedBitmap);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        return  bd.getBitmap();
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
