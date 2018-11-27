package co.edu.icesi.leishmaniasisapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
    }

    public void onClick_TomarFoto(View v){
        Intent intent= new Intent(Inicio.this,TomarFoto.class);
        startActivity(intent);
    }

    public void onClick_SeleccionarFoto(View v){
        Intent intent= new Intent(Inicio.this,SeleccionarFoto.class);
        startActivity(intent);
    }

    public void onClick_Instrucciones(View v){
        Intent intent = new Intent(Inicio.this, Instrucciones.class);
        startActivity(intent);
    }


}





