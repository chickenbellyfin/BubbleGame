package com.chickenbellyfinn.bubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Bubble";
		cfg.useGL20 = false;
		cfg.width = 640;
		cfg.height = 960;
		
		ScoreManager bc = new ScoreManager(){
			@Override
			public void saveScore(int score) {
				Gdx.app.getPreferences(PREF).putInteger(PREF_SCORE, score);
				Gdx.app.getPreferences(PREF).flush();				
			}
			@Override
			public int getScore() {
				return Gdx.app.getPreferences(PREF).getInteger(PREF_SCORE);
			}			
		};
		
		StoreService ss = new StoreService(){
			@Override
			public void startMoreApps() {				
			}
			@Override
			public void startRateApp() {
			}			
		};
				
		new LwjglApplication(new BubbleGame(bc, ss), cfg);
	}
}
