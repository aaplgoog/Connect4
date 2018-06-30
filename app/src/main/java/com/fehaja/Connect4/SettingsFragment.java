package com.fehaja.connect4;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
* Created by fguo on 4/17/17.
*/
public class SettingsFragment extends PreferenceFragment {
   public void onCreate (Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       addPreferencesFromResource(R.xml.pref);
   }
}
