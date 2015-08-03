package com.chickenbellyfinn.bubble;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication implements ScoreManager, StoreService{
	
	private static final String APP_URL = "market://details?id=com.chickenbellyfinn.bubble";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new BubbleGame(this, this), cfg);
    }

	@Override
	public void saveScore(int score) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.edit().putInt(PREF_SCORE, score).commit();		
	}

	@Override
	public int getScore() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		return prefs.getInt(PREF_SCORE, 0);
	}

	@Override
	public void startMoreApps() {
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://search?q=pub:Chickenbellyfinn"));
        startActivity(intent);				
	}

	@Override
	public void startRateApp() {	
        Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(APP_URL));
        startActivity(intent);
	}
}