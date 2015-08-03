package com.chickenbellyfinn.bubble;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bubble{
	
	public float _x;
	public float _y;
	public float _dy;
	public float _size;
	public Sprite _sprite;
	public float _phase;

	
	
	public Bubble(float x, float y, float speed, float size){
		_x = x;
		_y = y;
		_dy = speed;
		_size = size;
		_sprite = new Sprite(Resources.bubble);
		_sprite.setSize(size, size);
		_sprite.setOrigin(size/2,size/2);
		_sprite.setPosition(x, y);

		//phase for wave movement
		_phase = (float) (Math.random() * 2* Math.PI);
	}
}
