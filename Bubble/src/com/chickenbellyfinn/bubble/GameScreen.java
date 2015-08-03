package com.chickenbellyfinn.bubble;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class GameScreen implements Screen{
	
	private static final float PI2 = (float) (2 * Math.PI);
	
	private static final float MIN_SPEED = .25f;
	private static final float MAX_SPEED = .04f;
	private static final float SPIKE_SIZE = 1f/15f;
	private static final float PLAYER_SIZE = 1f/20f;
	private static final float PAUSE_SIZE = 0.8f;
	
	private static final double SPIKE_CHANCE = 0.012;
	private static final double BUBBLE_CHANCE = 0.0105;
	
	private static final float MIN_BUBBLE = 0.1f;
	private static final float MAX_BUBBLE = 0.3f;
	
	private static final int WAVE_FREQ = 4;
	
	private static final float FRICTION = 0.95f;
	
	private float WIDTH;
	private float HEIGHT;
	
	private BubbleGame _game;
	
	private float _minSpeed;
	private float _maxSpeed;
	
	private ArrayList<Spike> _spikes;
	private ArrayList<Bubble>_bubbles;
	private float _enemySize;
	
	private Sprite _playerSprite;
	private float _playerSize;
	private float _playerX;
	private float _playerY;
	private float _playerSpeedX;
	private float _playerSpeedY;
	
	private float _gyroX;
	private float _gyroY;
	
	private Sprite _gameOverSprite;
	private boolean _isGameOver = false;
	private float _gameOverY;	
	
	private Sprite _pauseSprite;
	private boolean _isPaused = false;

	private float _initialScore;
	private boolean _scoreSaved = false;
	
	private boolean _isDown = false;
	
	
	public GameScreen(BubbleGame game){
		_game = game;
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		System.out.println("size:"+WIDTH+", "+HEIGHT);
		_spikes = new ArrayList<Spike>();
		_bubbles = new ArrayList<Bubble>();
		_playerSprite = new Sprite(Resources.bubble);
		_playerSprite.setOrigin(_playerSprite.getWidth()/2, _playerSprite.getHeight()/2);
		
		_gameOverSprite = new Sprite(Resources.gameOver);
		_pauseSprite = new Sprite(Resources.pause);

		_minSpeed = WIDTH*MIN_SPEED;
		_maxSpeed = WIDTH*MAX_SPEED;
			
		_enemySize = WIDTH*SPIKE_SIZE;
		_playerSize = WIDTH*PLAYER_SIZE;
		_playerX =(WIDTH-_playerSize)/2;
		_playerY = 2*_playerSize;
		_playerSprite.setSize(_playerSize, _playerSize);
		_playerSprite.setPosition(_playerX,  _playerY);
		
		_pauseSprite.setSize(WIDTH*PAUSE_SIZE, PAUSE_SIZE*(WIDTH/_pauseSprite.getWidth())*_pauseSprite.getHeight());
		_pauseSprite.setOrigin(_pauseSprite.getWidth()/2, _pauseSprite.getHeight()/2);
		_pauseSprite.setPosition((WIDTH-_pauseSprite.getWidth())/2, (HEIGHT-_pauseSprite.getHeight())/2);
		
		_gameOverSprite.setSize(WIDTH, (WIDTH/_gameOverSprite.getWidth())*_gameOverSprite.getHeight());
		_gameOverSprite.setPosition(0, 0);
		_isGameOver = false;
		_gameOverY = 0;
		//_spikes.add(new Spike(WIDTH/2, HEIGHT/2, 0, _enemySize));
		_scoreSaved = false;
	    _initialScore = (int) Math.round(((_playerSize/WIDTH)*100)*((_playerSize/WIDTH)*100)*Math.PI);
	    
	    calibrateGyro();
	}
	
	private void calibrateGyro(){
		_gyroX = Gdx.input.getAccelerometerX();
		_gyroY = Gdx.input.getAccelerometerY();
		_playerSpeedX = 0;
		_playerSpeedY = 0;
	}  


	private void update(float delta) {
		
		if(_isGameOver){
			if(!_scoreSaved){
				_scoreSaved = true;
				_game.saveScore();
			}
			if(_gameOverY < (HEIGHT-_gameOverSprite.getHeight())/2){
				_gameOverY += 0.1*(HEIGHT-_gameOverSprite.getHeight())/2;
				_gameOverSprite.setPosition(0, _gameOverY);
			} else {
				if(_isDown && !Gdx.input.isTouched()){
					_game.setScreen(new MainScreen(_game));
				}
				_isDown = Gdx.input.isTouched();
			}
			return;
		}
		
		if(_isDown && !Gdx.input.isTouched()){
			_isPaused = !_isPaused;
			calibrateGyro();
		}
		_isDown = Gdx.input.isTouched();
		
		if(_isPaused){
			return;
		}
		
		if(Math.random() < SPIKE_CHANCE){
			float speed = (float) -(Math.random()*(_maxSpeed-_minSpeed) + _minSpeed);
			_spikes.add(new Spike((float)Math.random()*WIDTH,HEIGHT+_enemySize,speed,_enemySize));
		}
		
		if(Math.random() < BUBBLE_CHANCE){
			float minBubbleSize = _playerSize * MIN_BUBBLE;
			float maxBubbleSize = _playerSize * MAX_BUBBLE;
			float size = (float) (Math.random()*(maxBubbleSize - minBubbleSize) + minBubbleSize);
			float speed = (float) (Math.random()*(_maxSpeed-_minSpeed) + _minSpeed);
			_bubbles.add(new Bubble((float)Math.random()*WIDTH,-size, speed, size));
		}
		
		Iterator<Spike> si = _spikes.iterator();
		while(si.hasNext()){
			Spike s = si.next();
			
			if(collision(s._x, s._y, _enemySize)){
				_isGameOver = true;
			}
			
			s._y += s._dy*delta;			
			s._sprite.setY(s._y-_enemySize/2);
			s._sprite.rotate(s._drot*delta);
			if(s._y < -_enemySize){
				si.remove();
			}
		}
		
		Iterator<Bubble> bi = _bubbles.iterator();
		while(bi.hasNext()){
			Bubble b = bi.next();
			if(collision(b._x, b._y, b._size)){
				_playerSize += b._size/10;
				bi.remove();
				continue;
			}			
			b._y += b._dy*delta;
			b._x += Math.cos(PI2 * (WAVE_FREQ*b._y/HEIGHT)  + b._phase) * b._size * 10 * delta;
			b._sprite.setPosition(b._x, b._y);
			if(b._y > HEIGHT){
				bi.remove();
			}			
		}
		
		if( Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
			
		
		float accelX = Gdx.input.getAccelerometerX()-_gyroX;
	    float accelY = Gdx.input.getAccelerometerY()-_gyroY;
		
	    _playerSpeedX += -(accelX*12)*delta;
	    _playerSpeedY += -(accelY*12)*delta;
	    
	    _playerX += _playerSpeedX;
	    _playerY += _playerSpeedY;
	    
	    if(_playerX-_playerSize/2 < 0){
	    	_playerX = _playerSize/2;
	    	_playerSpeedX = 0;
	    } else if(_playerX+_playerSize/2 > WIDTH){
	    	_playerX = WIDTH-_playerSize/2;
	    	_playerSpeedX = 0;
	    }	    
	    if(_playerY-_playerSize/2 < 0){ 
	    	_playerY = _playerSize/2;
	    	_playerSpeedY = 0;
	    } else if(_playerY+_playerSize/2 > HEIGHT){
	    	_playerY = HEIGHT-_playerSize/2;
	    	_playerSpeedY = 0;
	    }
	    
	    _playerSpeedX *= FRICTION;
	    _playerSpeedY *= FRICTION;
		} else {
			_playerX = Gdx.input.getX();
			_playerY = HEIGHT-Gdx.input.getY();
		}

	    _playerSprite.setPosition((_playerX-_playerSize/2), _playerY-_playerSize/2);
	    _playerSprite.setSize(_playerSize, _playerSize);
	    
	    _game._score = (int) (Math.round(((_playerSize/WIDTH)*100)*((_playerSize/WIDTH)*100)*Math.PI) - _initialScore);
	    if(_game._highScore < _game._score){
	    	_game._highScore = _game._score;
	    }
	}
	
	private boolean collision(float x, float y, float r){
		return Math.sqrt((_playerX-x)*(_playerX-x)+(_playerY-y)*(_playerY-y)) < ((r/2)+_playerSize)/2;		
	}
	
	@Override
	public void render(float delta) {
		update(delta);	
        _game.batch.setProjectionMatrix(_game._camera.combined);
		_game.batch.begin();		
		_playerSprite.draw(_game.batch);
		for(Spike s:_spikes){
			s._sprite.draw(_game.batch);
		}
		for(Bubble b:_bubbles){
			b._sprite.draw(_game.batch);
			//_game.batch.draw(Resources.bubble, b._x, b._y, b._size, b._size);
		}
		if(_isPaused){
			_pauseSprite.draw(_game.batch);
		}
		if(_isGameOver){
			_gameOverSprite.draw(_game.batch);
		}
		_game.batch.end();		
	}



	@Override
	public void resize(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		System.out.println("size:"+WIDTH+", "+HEIGHT);	
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
		_isPaused = true;
		
	}

	@Override
	public void resume() {
		calibrateGyro();
	}

	@Override
	public void dispose() {
	}

}
