package org.example.mislugares;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by C.G Ledezma on 13/04/14.
 */
public class EdicionLugar extends Activity {
   private long id;
   private Lugar lugar;
   private EditText nombre;
   private Spinner tipo;
   private EditText direccion;
   private EditText telefono;
   private EditText url;
   private EditText comentario;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.edicion_lugar);

      Bundle extras = getIntent().getExtras();
      id = extras.getLong("id", -1);
      lugar = Lugares.elemento((int) id);

      nombre = (EditText) findViewById(R.id.nombre);
      nombre.setText(lugar.getNombre());

      if(lugar.getDireccion() == null || lugar.getDireccion().equals("")) {
         findViewById(R.id.direccion).setVisibility(View.GONE);
         findViewById(R.id.logo_direccion).setVisibility(View.GONE);
      } else {
         direccion = (EditText) findViewById(R.id.direccion);
         direccion.setText(lugar.getDireccion());
      }

      if(lugar.getTelefono() == 0){
         findViewById(R.id.telefono).setVisibility(View.GONE);
         findViewById(R.id.logo_telefono).setVisibility(View.GONE);
      } else {
         telefono = (EditText) findViewById(R.id.telefono);
         telefono.setText(Integer.toString(lugar.getTelefono()));
      }

      if(lugar.getUrl() == null || lugar.getUrl().equals("")){
         findViewById(R.id.url).setVisibility(View.GONE);
         findViewById(R.id.logo_url).setVisibility(View.GONE);
      } else {
         url = (EditText) findViewById(R.id.url);
         url.setText(lugar.getUrl());
      }

      if(lugar.getComentario() == null || lugar.getComentario().equals("")){
         findViewById(R.id.comentario).setVisibility(View.GONE);
         findViewById(R.id.logo_comentarios).setVisibility(View.GONE);
      } else {
         comentario = (EditText) findViewById(R.id.comentario);
         comentario.setText(lugar.getComentario());
      }

      tipo = (Spinner) findViewById(R.id.tipo);
      ArrayAdapter<String> adaptador =
         new ArrayAdapter<String>(this,
                                  android.R.layout.simple_spinner_item,
                                  TipoLugar.getNombres());
      adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      tipo.setAdapter(adaptador);
      tipo.setSelection(lugar.getTipo().ordinal());
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.edicion_lugar, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch(item.getItemId()) {
         case R.id.cancelar:
            return true;
         case R.id.guardar_lugar:
            lugar.setNombre(nombre.getText().toString());
            lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
            lugar.setDireccion(direccion.getText().toString());
            lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
            lugar.setUrl(url.getText().toString());
            lugar.setComentario(comentario.getText().toString());
            finish();
         default:
            super.onOptionsItemSelected(item);
      }
      return true;
   }
}
