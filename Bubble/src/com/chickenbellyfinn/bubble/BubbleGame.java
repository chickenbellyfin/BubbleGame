package com.chickenbellyfinn.bubble;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.Preferences;

public class BubbleGame extends Game {

	public static final String PREF = "bubble";
	public static final String PREF_SCORE = "highscore";
	
	private static final Color bg1 = new Color(0f, 0f, 92f/255f, 1f);
	private static final Color bg2 = new Color(0f, 210f/255f, 212f/255f, 1f);
	
	private static final float TEXT_HEIGHT = 0.05f;
	private static final float TEXT_MARGIN = 0.01f;

	
	public OrthographicCamera _camera;
	
	private float W;
	private float H;
	
	public ScoreManager _save;
	public StoreService _storeService;
	
	public SpriteBatch batch;
	public BitmapFont _font;
	private float _fontHeight;
	private float _fontMargin;
	public ShapeRenderer _shapeRenderer;
	
	public Preferences _prefs;
	
	public int _score;
	public int _highScore;
	
	public BubbleGame(ScoreManager a, StoreService s){
		_save = a;
		_storeService = s;		
	}
	
	
	@Override
	public void create() {		
		W = Gdx.graphics.getWidth();
		H = Gdx.graphics.getHeight();
		_camera = new OrthographicCamera(W, H);
		_camera.setToOrtho(false, W, H);
		Resources.loadResources();

		_shapeRenderer = new ShapeRenderer();
		_font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);
		_fontHeight = _font.getLineHeight();
		batch = new SpriteBatch();			
		_prefs = Gdx.app.getPreferences(PREF);
		_highScore = _save.getScore();
				
		setScreen(new MainScreen(this));
	}
	
	@Override
	public void render(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        _shapeRenderer.begin(ShapeType.FilledRectangle);
        _shapeRenderer.filledRect(0, 0, W, H, bg1, bg1, bg2, bg2);
        _shapeRenderer.end();
        batch.begin();
        _font.draw(batch, ""+_score, _fontMargin, H-_fontMargin);        
        _font.draw(batch, "HS: "+_highScore, W-(_font.getBounds("HS: "+_highScore).width+_fontMargin), H-_fontMargin);
        batch.end();
		super.render();		
	}
	
	public void saveScore(){
		_save.saveScore(_highScore);
	}
	
	public void startRateApp(){
		_storeService.startRateApp();		
	}
	
	public void startMoreApps(){
		_storeService.startMoreApps();
	}
	
	@Override
	public void resize(int w, int h){
		W = w;
		H = h;
		_font.setScale((W*TEXT_HEIGHT)/_fontHeight);
		_fontMargin = W*TEXT_MARGIN;
		_camera = new OrthographicCamera(W, H);
		_camera.setToOrtho(false, W, H);
		super.resize(w, h);
		_shapeRenderer.setProjectionMatrix(_camera.combined);
	}

	@Override
	public void dispose() {
		batch.dispose();
		Resources.dispose();
	}
}
