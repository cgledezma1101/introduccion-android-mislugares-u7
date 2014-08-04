package org.example.mislugares;

import android.app.ListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C.G Ledezma on 25/03/14.
 */
public class Lugares {
   protected static List<Lugar> vectorLugares = ejemploLugares();
   final static String TAG = "MisLugares";
   protected static GeoPunto posicionActual = new GeoPunto(0, 0);

   public Lugares() {
      vectorLugares = ejemploLugares();
   }

   static Lugar elemento(int id){
      return vectorLugares.get(id);
   }

   static void anyade(Lugar lugar){
      vectorLugares.add(lugar);
   }

   static int nuevo(){
      Lugar lugar = new Lugar();
      vectorLugares.add(lugar);
      return vectorLugares.size()-1;
   }

   static void borrar(int id){
      vectorLugares.remove(id);
   }

   public static int size() {
      return vectorLugares.size();
   }

   public static ArrayList<Lugar> ejemploLugares() {
      ArrayList<Lugar> lugares = new ArrayList<Lugar>();
      lugares.add(new Lugar("Escuela Politécnica Superior de Gandía",
            "C/ Paranimf, 1 46730 Gandia (SPAIN)", -0.166093, 38.995656,
            TipoLugar.EDUCACION,962849300, "http://www.epsg.upv.es",
            "Uno de los mejores lugares para formarse.", 3));

      lugares.add(new Lugar("Al de siempre",
            "P.Industrial Junto Molí Nou - 46722, Benifla (Valencia)",
            -0.190642, 38.925857, TipoLugar.BAR, 636472405, "",
            "No te pierdas el arroz en calabaza.", 3));

      lugares.add(new Lugar("androidcurso.com",
            "ciberespacio", 0.0, 0.0, TipoLugar.EDUCACION,
            962849300, "http://androidcurso.com",
            "Amplia tus conocimientos sobre Android.", 5));

      lugares.add(new Lugar("Barranco del Infierno",
            "Vía Verde del río Serpis. Villalonga (Valencia)",
            -0.295058, 38.867180, TipoLugar.NATURALEZA,

            0, "http://sosegaos.blogspot.com.es/2009/02/lorcha-villalonga-via-verde-del-rio.html",
            "Espectacular ruta para bici o andar", 4));

      lugares.add(new Lugar("La Vital",
            "Avda. de La Vital, 0 46701 Gandía (Valencia)",
            -0.1720092, 38.9705949, TipoLugar.COMPRAS,
            962881070, "http://www.lavital.es/",
            "El típico centro comercial", 2));

      return lugares;
   }

   static List<String> listaNombres() {
      ArrayList<String> resultado = new ArrayList<String>();
      for (Lugar lugar : vectorLugares) {
         resultado.add(lugar.getNombre());
      }
      return resultado;
   }
}
