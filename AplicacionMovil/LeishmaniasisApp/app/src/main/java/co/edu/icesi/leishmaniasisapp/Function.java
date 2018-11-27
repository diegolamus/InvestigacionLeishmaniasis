package co.edu.icesi.leishmaniasisapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

//Esta clase contiene metodos estaticos, que realizan procesos de conversion, manejar formatos y estructura de datos

public class Function {


    static final String KEY_ALBUM = "album_name";
    static final String KEY_PATH = "path";
    static final String KEY_TIMESTAMP = "timestamp";
    static final String KEY_TIME = "date";
    static final String KEY_COUNT = "date";


    public static  boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static HashMap<String, String> mappingInbox(String album, String path, String timestamp, String time, String count)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(KEY_ALBUM, album);
        map.put(KEY_PATH, path);
        map.put(KEY_TIMESTAMP, timestamp);
        map.put(KEY_TIME, time);
        map.put(KEY_COUNT, count);
        return map;
    }


//metodo que se encarga de contar el numero de fotos en un album, retornando el numero total de archivos contenidos
    public static String getCount(Context c, String album_name)
    {
        Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
        Cursor cursorExternal = c.getContentResolver().query(uriExternal, projection, "bucket_display_name = \""+album_name+"\"", null, null);
        Cursor cursorInternal = c.getContentResolver().query(uriInternal, projection, "bucket_display_name = \""+album_name+"\"", null, null);
        Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});


        return cursor.getCount()+" Photos";
    }

    public static String converToTime(String timestamp)
    {
        if(timestamp == null) return "13/11 18:16";
        long datetime = Long.parseLong(timestamp);
        Date date = new Date(datetime);
        DateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        return formatter.format(date);
    }

    //permite deacuerdo a la densidad de pantalla redimencionar los componenetes visuales en la cantidad de pixeles que soporta
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

}
