package com.chickenbellyfinn.bubble;

public interface ScoreManager {
	
	public static final String PREF = "bubble";
	public static final String PREF_SCORE = "highscore";
	
	public void saveScore(int score);
	
	public int getScore();

}
