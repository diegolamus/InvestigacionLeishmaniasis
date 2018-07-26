package co.edu.icesi.modelo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Area extends View {

    private Paint paint = new Paint();

    public Area(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //stroke dibuja solo el contorno de la figura
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);

        //center
        int x0 = canvas.getWidth()/2;
        int y0 = canvas.getHeight()/2;
        int dx = canvas.getWidth()/3;
        int dy = canvas.getHeight()/3;
        //draw guide box
        canvas.drawRect(250, 250, 400, 400, paint);

    }
}
