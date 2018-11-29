package co.edu.icesi.modelo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import co.edu.icesi.leishmaniasisapp.MostrarResultados;


public class ProcesamientoImagen
{
    public static Bitmap recortarImagen(Bitmap image, int startX, int startY, int width, int height){
        try {
            Bitmap salida = Bitmap.createBitmap(image, startX, startY, width, height);
            return salida;
            // androids falla por dimenciones de camara no compatible con view de recorte
        }catch (IllegalArgumentException ex){
            return image;
        }
    }

    public static Bitmap rotarImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static double obtenerProbabilidad(final Context context, Bitmap image) throws Exception {
        // Variable para guardar probabilidad
        final double[] probabilidad = new double[1];
        final boolean[] fallo = new boolean[1];
        // Convertir bitmap a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        // Convertir a base64
        final String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        // Realizar prediccion
        final CyclicBarrier barrier = new CyclicBarrier(2);
        Thread t = new Thread(){
                public void run() {
                    Gson gson = new Gson();
                    PredictJson jsonn = new PredictJson();
                    jsonn.data = encoded;
                    String data = gson.toJson(jsonn);
                    try {
                        final String respuesta = WEBUtil.JsonByPOSTrequest("http://104.198.153.58:80/predict", data);
                        final String[] resultados = respuesta.split("\"");
                        probabilidad[0] = Double.parseDouble(resultados[resultados.length-2])*100;
                        Log.e("DEBU3", probabilidad[0]+"" );
                    } catch (IOException e) {
                        fallo[0] = true;
                    }
                    try {
                        barrier.await();
                    } catch (Exception e){
                        fallo[0] = true;
                    }
                }
        };
        t.start();
        barrier.await();
        if (fallo[0]){
            throw new Exception("Error al conectarse con el servidor");
        }
        return probabilidad[0];
    }


    public static Bitmap loadBitmapFromView(View v) {
        Bitmap bitmap;
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return bitmap;
    }

}

class PredictJson{
    String data;
}

