package org.example.mislugares;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class MainActivity extends ListActivity implements LocationListener {
   public BaseAdapter adaptador;
   private MediaPlayer mp;
   private LocationManager manejador;
   private Location mejorLocaliz;
   private static final long DOS_MINUTOS = 2 * 60 * 1000;

   private void activarProveedores() {
      if(manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
         manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20 * 1000, 5, this);
      }

      if(manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
         manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10 * 1000, 10, this);
      }
   }

   private void actualizarMejorLocaliz(Location localiz) {
      if(mejorLocaliz == null ||
         localiz.getAccuracy() < 2 * mejorLocaliz.getAccuracy() ||
         localiz.getTime() - mejorLocaliz.getTime() > DOS_MINUTOS) {
         Log.d(Lugares.TAG, "Nueva mejor localización");
         mejorLocaliz = localiz;
         Lugares.posicionActual.setLatitud(localiz.getLatitude());
         Lugares.posicionActual.setLongitud(localiz.getLongitude());
      }
   }

   /**
    * Called when we click on the About button
    * @param view The view that was clicked
    */
   public void lanzarAcercaDe(View view) {
      Intent i = new Intent(this, AcercaDe.class);
      startActivity(i);
   }

   /**
    * Called to start the preference dialog
    * @param view The view element that requested the action
    */
   public void lanzarPreferencias(View view) {
      Intent i = new Intent(this, Preferencias.class);
      startActivity(i);
   }

   /**
    * Called to show the information of a particular place
    * @param view The view element that requested the action
    */
   public void lanzarVistaLugar(View view) {
      final EditText entrada = new EditText(this);
      entrada.setText("0");
      new AlertDialog.Builder(this)
            .setTitle("Selección de lugar")
            .setMessage("indica su id:")
            .setView(entrada)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                  long id = Long.parseLong(entrada.getText().toString());
                  Intent intent = new Intent(MainActivity.this, VistaLugar.class);
                  intent.putExtra("id", id);
                  startActivity(intent);
               }
            })
            .setNegativeButton("Cancelar", null)
            .show();
   }

   /**
    * Shows the user's preferences
    */
   public void mostrarPreferencias(){
      SharedPreferences pref =
         PreferenceManager.getDefaultSharedPreferences(this);
      String s = "Notificaciones: " + pref.getBoolean("notificaciones", true) +
                 ". Distancia mínima: " + pref.getString("distancia", "?");
      Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
   }
   /**
    * Called when the activity is first created.
    */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      adaptador = new AdaptadorLugares(this);
      setListAdapter(adaptador);

      mp = MediaPlayer.create(this, R.raw.audio);
      mp.start();

      manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
      if(manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
         actualizarMejorLocaliz(manejador.getLastKnownLocation(
                                  LocationManager.GPS_PROVIDER));
      }

      if(manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            actualizarMejorLocaliz(manejador.getLastKnownLocation(
                                     LocationManager.NETWORK_PROVIDER));
      }

      /*
      These are events associated to the buttons on the alternative main
      activity view
      // Associate click event to the About button
      Button bAcercaDe = (Button) findViewById(R.id.buttonAbout);
      bAcercaDe.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            lanzarAcercaDe(null);
         }
      });

      // Associate click event to the Exit button
      Button bSalir = (Button) findViewById(R.id.buttonExit);
      bSalir.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            finish();
         }
      });

      // Associate click event to the Preferences button
      Button bPreferencias = (Button) findViewById(R.id.buttonPreferences);
      bPreferencias.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            lanzarPreferencias(null);
         }
      });

      // Associate click event to the Show places button
      Button bShowPlaces = (Button) findViewById(R.id.buttonShowPlaces);
      bShowPlaces.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            lanzarVistaLugar(null);
         }
      });*/
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main, menu);
      return true; /** true -> El menú ya estará visible */
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
   }

   @Override
   public void onListItemClick(ListView listView,
                               View vista,
                               int posicion,
                               long id) {
      super.onListItemClick(listView, vista, posicion, id);
      Intent intent = new Intent(this, VistaLugar.class);
      intent.putExtra("id", id);
      startActivity(intent);
   }

  @Override
  public void onLocationChanged(Location location) {
     Log.d(Lugares.TAG, "Nueva localización: " + location);
     actualizarMejorLocaliz(location);
  }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()){
         case R.id.acercaDe:
            lanzarAcercaDe(null);
            break;
         case R.id.config:
            lanzarPreferencias(null);
            break;
         case R.id.menu_mapa:
            Intent i = new Intent(this, Mapa.class);
            startActivity(i);
            break;
      }
      return true; /** true -> Consumimos el item, no se propaga */
   }

   @Override
   protected void onPause() {
      mp.pause();
      super.onPause();
      manejador.removeUpdates(this);
   }

   @Override
   public void onProviderDisabled(String proveedor) {
      Log.d(Lugares.TAG, "Se deshabilita: " + proveedor);
      activarProveedores();
   }

   @Override
   public void onProviderEnabled(String proveedor) {
      Log.d(Lugares.TAG, "Se habilita: " + proveedor);
      activarProveedores();
   }

   @Override
   protected void onRestart() {
      super.onRestart();
   }

   @Override
   protected void onResume() {
      super.onResume();
      mp.start();
      activarProveedores();
   }

   @Override
   protected void onStart() {
      super.onStart();
   }

   @Override
   public void onStatusChanged(String proveedor, int estado, Bundle extras) {
      Log.d(Lugares.TAG, "Cambia estado: " + proveedor);
      activarProveedores();
   }

   @Override
   protected void onStop() {
      super.onStop();
   }

   @Override
   protected void onSaveInstanceState(Bundle state) {
      super.onSaveInstanceState(state);
      if(mp != null) {
         int pos = mp.getCurrentPosition();
         state.putInt("posicion_media", pos);
         Toast.makeText(this, "Saved state: " + pos, Toast.LENGTH_SHORT);
      }
   }

   @Override
   protected void onRestoreInstanceState(Bundle state) {
      if(state != null && mp != null) {
         int pos = state.getInt("posicion_media");
         mp.seekTo(pos);
      }
   }
}
