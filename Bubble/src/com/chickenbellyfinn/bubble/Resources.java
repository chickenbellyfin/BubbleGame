package com.chickenbellyfinn.bubble;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Resources {
	
	public static Texture bubble;
	public static Texture spike;
	
	public static Texture titleTexture;
	public static TextureRegion title;
	public static TextureRegion gameOver;
	
	public static Texture buttonTexture;
	public static TextureRegion play;
	public static TextureRegion rate;
	public static TextureRegion more;
	public static TextureRegion pause;
	
	
	static void loadResources(){
		bubble = new Texture("data/bubble.png");
		bubble.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		spike = new Texture("data/spike.png");
		spike.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		titleTexture = new Texture("data/title.png");
		titleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		gameOver = new TextureRegion(titleTexture, 0,0,512,300);
		title = new TextureRegion(titleTexture, 0, 300, 512,120);
		
		buttonTexture = new Texture("data/buttons.png");
		buttonTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		play = new TextureRegion(buttonTexture, 60, 20, 400, 105);
		rate = new TextureRegion(buttonTexture, 0, 180, 512, 55);
		more = new TextureRegion(buttonTexture, 0, 275, 512, 55);
		pause = new TextureRegion(buttonTexture, 90, 370, 330, 60);
		
	}
	
	
	static void dispose(){
		bubble.dispose();
		spike.dispose();
		titleTexture.dispose();
	}

}
