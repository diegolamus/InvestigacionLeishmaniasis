package co.edu.icesi.leishmaniasisprediagnstico;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VistaInicial extends AppCompatActivity {

    Button boton_instrucciones;
    Button boton_seleccionar_foto;
    Button boton_tomar_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_inicial);
        boton_instrucciones = (Button) findViewById(R.id.instrucciones);
        boton_seleccionar_foto = (Button) findViewById(R.id.seleccionar_foto);
        boton_tomar_foto = (Button) findViewById(R.id.tomar_foto);
    }

    public void desplegarInstrucciones(View v){
        //TODO
    }

    public void tomarFoto(View v){
        //TODO
    }

    public void seleccionarFoto(View v){
        //TODO
    }
}
