package co.edu.icesi.modelo;

import android.content.Context;
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
import android.util.Log;
import android.view.View;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

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
        double probability = 0;
        Log.e("obtener prob", context.getFilesDir().getPath() );
        try {
            String path = context.getFilesDir().getPath() + "/" + "modelo.zip";
            InputStream is = new FileInputStream(new File(path));
            // Restore yourModel
            MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(is);
            //Use the nativeImageLoader to convert to numerical matrix and then to INDArray
            INDArray imag = new NativeImageLoader(224, 224, 3).asMatrix(image);
            //values need to be scale
            DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
            //then cal that scalar on the image dataset
            scalar.transform(imag);
            // Use yourModel and store resuly in INDArray
            INDArray results = model.output(imag);
            // Get probability from results
            probability = results.getDouble(0);
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
