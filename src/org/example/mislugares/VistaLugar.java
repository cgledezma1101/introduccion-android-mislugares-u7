package org.example.mislugares;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by C.G Ledezma on 25/03/14.
 */
public class VistaLugar extends Activity {
   private long id;
   private ImageView imageView;
   private Lugar lugar;
   private Uri uriFoto;
   final static int RESULTADO_EDITAR = 1;
   final static int RESULTADO_GALERIA = 2;
   final static int RESULTADO_FOTO = 3;

   protected void actualizarVistas() {
      TextView nombre = (TextView) findViewById(R.id.nombre);
      nombre.setText(lugar.getNombre());

      ImageView logo_tipo = (ImageView) findViewById(R.id.logo_tipo);
      logo_tipo.setImageResource(lugar.getTipo().getRecurso());

      TextView tipo = (TextView) findViewById(R.id.tipo);
      tipo.setText(lugar.getTipo().getTexto());

      if(lugar.getDireccion() == null || lugar.getDireccion().equals("")) {
         findViewById(R.id.direccion).setVisibility(View.GONE);
         findViewById(R.id.logo_direccion).setVisibility(View.GONE);
      } else {
         TextView direccion = (TextView) findViewById(R.id.direccion);
         direccion.setText(lugar.getDireccion());
      }

      if(lugar.getTelefono() == 0){
         findViewById(R.id.telefono).setVisibility(View.GONE);
         findViewById(R.id.logo_telefono).setVisibility(View.GONE);
      } else {
         TextView telefono = (TextView) findViewById(R.id.telefono);
         telefono.setText(Integer.toString(lugar.getTelefono()));
      }

      if(lugar.getUrl() == null || lugar.getUrl().equals("")){
         findViewById(R.id.url).setVisibility(View.GONE);
         findViewById(R.id.logo_url).setVisibility(View.GONE);
      } else {
         TextView url = (TextView) findViewById(R.id.url);
         url.setText(lugar.getUrl());
      }

      if(lugar.getComentario() == null || lugar.getComentario().equals("")){
         findViewById(R.id.comentario).setVisibility(View.GONE);
         findViewById(R.id.logo_comentarios).setVisibility(View.GONE);
      } else {
         TextView comentario = (TextView) findViewById(R.id.comentario);
         comentario.setText(lugar.getComentario());
      }

      TextView fecha = (TextView) findViewById(R.id.fecha);
      fecha.setText(DateFormat.getDateInstance().format(
            new Date(lugar.getFecha())));

      TextView hora = (TextView) findViewById(R.id.hora);
      hora.setText(DateFormat.getTimeInstance().format(
            new Date(lugar.getFecha())));

      RatingBar valoracion = (RatingBar) findViewById(R.id.valoracion);
      valoracion.setRating(lugar.getValoracion());
      valoracion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
         @Override
         public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
            lugar.setValoracion(v);
         }
      });

      ponerFoto(imageView, lugar.getFoto());
   }

   protected void confirmarEliminacion(int id) {
      final int idLugar = id;
      new AlertDialog.Builder(this)
            .setTitle("Confirmación de eliminación")
            .setMessage("¿Estás seguro que quieres eliminar este lugar?")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                  Lugares.borrar((int) idLugar);
                  finish();
               }
            })
            .setNegativeButton("Cancelar", null)
            .show();
   }

   public void eliminarFoto(View view) {
      lugar.setFoto(null);
      ponerFoto(imageView, null);
   }

   public void galeria(View view) {
      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.addCategory((Intent.CATEGORY_OPENABLE));
      intent.setType("image/*");
      startActivityForResult(intent, RESULTADO_GALERIA);
   }

   public void llamadaTelefono(View view) {
      startActivity(new Intent(Intent.ACTION_DIAL,
                               Uri.parse("tel:" + lugar.getTelefono())));
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if(requestCode == RESULTADO_EDITAR) {
         actualizarVistas();
         findViewById(R.id.scrollView1).invalidate();
      } else if (requestCode == RESULTADO_GALERIA &&
                 resultCode == Activity.RESULT_OK) {
         lugar.setFoto(data.getDataString());
         ponerFoto(imageView, lugar.getFoto());
      } else if (requestCode == RESULTADO_FOTO &&
                 resultCode == Activity.RESULT_OK &&
                 lugar != null && uriFoto != null) {
         lugar.setFoto(uriFoto.toString());
         ponerFoto(imageView, lugar.getFoto());
      }
   }

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.vista_lugar);

      Bundle extras = getIntent().getExtras();
      id = extras.getLong("id", -1);
      lugar = Lugares.elemento((int) id);
      imageView = (ImageView) findViewById(R.id.foto);

      actualizarVistas();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu){
      getMenuInflater().inflate(R.menu.vista_lugar, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item){
      Intent intent;
      switch(item.getItemId()) {
         case R.id.accion_compartir:
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,
                            lugar.getNombre() + " - " + lugar.getUrl());
            startActivity(intent);
            return true;
         case R.id.accion_llegar:
            verMapa(null);
            return true;
         case R.id.accion_editar:
            intent = new Intent(this, EdicionLugar.class);
            intent.putExtra("id", id);
            startActivityForResult(intent, RESULTADO_EDITAR);
            return true;
         case R.id.accion_borrar:
            confirmarEliminacion((int) id);
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }

   public void pgWeb(View view) {
      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(lugar.getUrl())));
   }

   protected void ponerFoto(ImageView imageView, String uri) {
      if(uri != null) {
         imageView.setImageURI(Uri.parse(uri));
      } else {
         imageView.setImageBitmap(null);
      }
   }

   public void tomarFoto(View view) {
      Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
      uriFoto = Uri.fromFile(
                       new File(Environment.getExternalStorageDirectory() +
                                File.separator + "img_" +
                                (System.currentTimeMillis() / 1000) + ".jpg"));
      intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
      startActivityForResult(intent, RESULTADO_FOTO);
   }

   public void verMapa(View view) {
      Uri uri;
      double lat = lugar.getPosicion().getLatitud();
      double lon = lugar.getPosicion().getLongitud();
      if(lat != 0 || lon != 0) {
         uri = Uri.parse("geo:" + lat + "," + lon);
      } else {
         uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
      }
      Intent intent = new Intent(Intent.ACTION_VIEW, uri);
      startActivity(intent);
   }
}