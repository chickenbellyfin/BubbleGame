package com.chickenbellyfinn.bubble;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Spike {
	
	public float _drot;
	public float _x;
	public float _y;
	public float _dy;
	public Sprite _sprite;
	
	public Spike(float x, float y, float speed, float _size){
		float rot = (float) (Math.random()*360);
		_drot = (float) (Math.random() - 0.5)*200;
		_x = x;
		_y = y;
		_dy = speed;
		_sprite = new Sprite(Resources.spike);
		_sprite.setSize(_size, _size);
		_sprite.setOrigin(_size/2, _size/2);
		_sprite.setPosition(_x-_size/2, _y-_size/2);
		//_sprite.rotate(rot);
	}
}
