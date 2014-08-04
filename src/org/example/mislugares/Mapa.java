package org.example.mislugares;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by C.G Ledezma on 22/04/14.
 */
public class Mapa extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener {
   private GoogleMap mapa;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.mapa);

      mapa = ((SupportMapFragment) getSupportFragmentManager()
                                     .findFragmentById(R.id.mapa)).getMap();
      mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
      mapa.setMyLocationEnabled(true);
      mapa.getUiSettings().setZoomControlsEnabled(true);
      mapa.getUiSettings().setCompassEnabled(true);

      if(Lugares.vectorLugares.size() > 0) {
         GeoPunto p = Lugares.vectorLugares.get(0).getPosicion();
         mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(
               new LatLng(p.getLatitud(), p.getLongitud()), 12));
      }
      for(Lugar lugar : Lugares.vectorLugares) {
         GeoPunto p = lugar.getPosicion();

         if(p != null && p.getLatitud() != 0) {
            BitmapDrawable iconoDrawable =
               (BitmapDrawable) getResources().getDrawable(lugar.getTipo()
                     .getRecurso());
            Bitmap iGrande = iconoDrawable.getBitmap();
            Bitmap icono =
               Bitmap.createScaledBitmap(iGrande, iGrande.getWidth() / 7,
                                         iGrande.getHeight() / 7, false);
            mapa.addMarker(new MarkerOptions()
                                 .position(new LatLng(p.getLatitud(),
                                                      p.getLongitud()))
                                 .title(lugar.getNombre())
                                 .snippet(lugar.getDireccion())
                                 .icon(BitmapDescriptorFactory.fromBitmap(icono)));
         }
      }

      mapa.setOnInfoWindowClickListener(this);
   }

   @Override
   public void onInfoWindowClick(Marker marker) {
      for(int id = 0; id < Lugares.vectorLugares.size(); ++ id) {
         if(Lugares.vectorLugares.get(id).getNombre().equals(marker.getTitle())) {
            Intent intent = new Intent(this, VistaLugar.class);
            intent.putExtra("id", (long)id);
            startActivity(intent);
            break;
         }
      }
   }
}
