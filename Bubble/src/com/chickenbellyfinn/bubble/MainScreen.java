package com.chickenbellyfinn.bubble;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class MainScreen implements Screen{
	
	private static final float TITLE_WIDTH = 0.85f;
	private static final float TITLE_POSITION = 0.85f;
	
	private static final float PLAY_WIDTH = 0.5f;	
	private static final float RATE_WIDTH = 0.8f;	
	
	private static final float MORE_WIDTH = 0.85f;
	
	private static final float SCALE = 1.25f;
	
	private static final float MARGIN = 0.08f;
	

	private static final float PI2 = (float) (2 * Math.PI);
	private static final int WAVE_FREQ = 4;
	private static final float MIN_SPEED = .25f;
	private static final float MAX_SPEED = .04f;	
	private static final double BUBBLE_CHANCE = 0.02;	
	private static final float MIN_BUBBLE = 0.01f;
	private static final float MAX_BUBBLE = 0.025f;
	
	private float WIDTH;
	private float HEIGHT;
	
	private BubbleGame _game;

	private ArrayList<Bubble>_bubbles;
	
	private Sprite _titleSprite;
	private Sprite _playSprite;
	private Sprite _rateSprite;
	private Sprite _moreSprite;
	
	private boolean _isDown = false;
	private float _downX;
	private float _downY;
	
	public MainScreen(BubbleGame game){
		_game = game;
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
		_bubbles = new ArrayList<Bubble>();
		
		_titleSprite = new Sprite(Resources.title);
		_titleSprite.setOrigin(_titleSprite.getWidth()/2, _titleSprite.getHeight()/2);
		
		_playSprite = new Sprite(Resources.play);
		_rateSprite = new Sprite(Resources.rate);		
		_moreSprite = new Sprite(Resources.more);
		
		init();
	}
	
	private void init(){
		_bubbles.clear();
		_titleSprite.setSize(WIDTH*TITLE_WIDTH, ((WIDTH*TITLE_WIDTH)/_titleSprite.getWidth())*_titleSprite.getHeight());
		_titleSprite.setPosition((WIDTH-_titleSprite.getWidth())/2, (TITLE_POSITION*HEIGHT)-_titleSprite.getHeight()/2);
		if(_titleSprite.getY()+_titleSprite.getHeight() > HEIGHT ){
			_titleSprite.setY(HEIGHT-_titleSprite.getHeight()- HEIGHT*0.06f);
		}
		
		_playSprite.setSize(WIDTH*PLAY_WIDTH, ((WIDTH*PLAY_WIDTH)/_playSprite.getWidth())*_playSprite.getHeight());
		_playSprite.setPosition((WIDTH-_playSprite.getWidth())/2, _titleSprite.getY()-(_playSprite.getHeight()+MARGIN*HEIGHT));
		_playSprite.setOrigin(_playSprite.getWidth()/2, _playSprite.getHeight()/2);

		_rateSprite.setSize(WIDTH*RATE_WIDTH, ((WIDTH*RATE_WIDTH)/_rateSprite.getWidth())*_rateSprite.getHeight());
		_rateSprite.setPosition((WIDTH-_rateSprite.getWidth())/2, _playSprite.getY()-(_rateSprite.getHeight()+MARGIN*HEIGHT));
		_rateSprite.setOrigin(_rateSprite.getWidth()/2, _rateSprite.getHeight()/2);
		
		_moreSprite.setSize(WIDTH*MORE_WIDTH, ((WIDTH*MORE_WIDTH)/_moreSprite.getWidth())*_moreSprite.getHeight());
		_moreSprite.setPosition((WIDTH-_moreSprite.getWidth())/2, _rateSprite.getY()-(_moreSprite.getHeight()+MARGIN*HEIGHT));
		_moreSprite.setOrigin(_moreSprite.getWidth()/2, _moreSprite.getHeight()/2);
	}
	
	private void update(float delta){
		
		if(Math.random() < BUBBLE_CHANCE){
			float minBubbleSize = WIDTH * MIN_BUBBLE;
			float maxBubbleSize = WIDTH * MAX_BUBBLE;
			float size = (float) (Math.random()*(maxBubbleSize - minBubbleSize) + minBubbleSize);
			float speed = (float) (Math.random()*(MAX_SPEED-MIN_SPEED) + MIN_SPEED)*WIDTH;
			_bubbles.add(new Bubble((float)Math.random()*WIDTH,-size, speed, size));
		}
		
		Iterator<Bubble> bi = _bubbles.iterator();
		while(bi.hasNext()){
			Bubble b = bi.next();			
			b._y += b._dy*delta;
			b._x += Math.cos(PI2 * (WAVE_FREQ*b._y/HEIGHT)  + b._phase) * b._size * 10 * delta;
			b._sprite.setPosition(b._x, b._y);
			if(b._y > HEIGHT){
				bi.remove();
			}			
		}
		
		float mx = Gdx.input.getX();
		float my = HEIGHT - Gdx.input.getY();
		if(!_isDown && Gdx.input.isTouched()){
			_isDown = true;
			_downX = mx;
			_downY = my;
		} else if (_isDown && !Gdx.input.isTouched()){
			_isDown = false;

			_playSprite.setScale(1);
			_rateSprite.setScale(1);
			_moreSprite.setScale(1);
			if(_playSprite.getBoundingRectangle().contains(mx, my)){
				_game.setScreen(new GameScreen(_game));
			}
			
			if(_rateSprite.getBoundingRectangle().contains(mx, my)){
				_game.startRateApp();
			}
			
			if(_moreSprite.getBoundingRectangle().contains(mx, my)){
				_game.startMoreApps();
			}
			
		}

		
		if(_isDown){
			if(_playSprite.getBoundingRectangle().contains(mx, my)){
				_playSprite.setScale(SCALE);
			} else {
				_playSprite.setScale(1);
			}
			
			if(_rateSprite.getBoundingRectangle().contains(mx, my)){
				_rateSprite.setScale(SCALE);
			} else {
				_rateSprite.setScale(1);
			}
			
			if(_moreSprite.getBoundingRectangle().contains(mx, my)){
				_moreSprite.setScale(SCALE);
			} else {
				_moreSprite.setScale(1);
			}
		}
	}

	@Override
	public void render(float delta) {
		update(delta);
        _game.batch.setProjectionMatrix(_game._camera.combined);
        _game.batch.begin();
		for(Bubble b:_bubbles){
			b._sprite.draw(_game.batch);
		}
        _titleSprite.draw(_game.batch);
        _playSprite.draw(_game.batch);
        _rateSprite.draw(_game.batch);
        _moreSprite.draw(_game.batch);
        _game.batch.end();
		
	}

	@Override
	public void resize(int width, int height) {		
		WIDTH = width;
		HEIGHT = height;
	
		System.out.println("size:"+WIDTH+", "+HEIGHT);
		init();		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
