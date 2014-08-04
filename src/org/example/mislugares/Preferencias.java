package org.example.mislugares;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by C.G Ledezma on 25/03/14.
 */
public class Preferencias extends PreferenceActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preferencias);
   }
}
