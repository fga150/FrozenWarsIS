package Screens;

import Application.Assets;
import Application.Desktop;
import Application.MatchManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LoadingScreen implements Screen{
	
	private OrthographicCamera textCam;
	private SpriteBatch batcher;
	private MatchManager manager;
	private BitmapFont font2;
	private boolean loaded;
	private long waitTime;
	private boolean send;
	
	
	public LoadingScreen(MatchManager manager){
		this.loaded = false;
		this.manager = manager;
		font2 =new BitmapFont(Gdx.files.internal("data/second.fnt"), Gdx.files.internal("data/second.png"), false);
		textCam = new OrthographicCamera(21*49,13*49);
		textCam.position.set((21*49)/2,(13*49)/2,0);
		batcher = new SpriteBatch();
		this.waitTime = System.currentTimeMillis();
	}
	
	@Override
	public void dispose() {
		batcher.dispose();
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		batcher.enableBlending();
		batcher.begin();
		batcher.setProjectionMatrix(textCam.combined);
		
		Color c = batcher.getColor();
        batcher.setColor(c.r, c.g, c.b, 0.40f); //set alpha to 1
        batcher.draw(Assets.getBackground(),0,0,21*49,13*49);
        //batcher.setColor(c.r, c.g, c.b, 1f);
        font2.draw(batcher, "LOADING...",3*49, 5*49);
		batcher.end();
		textCam.update();
		
		if (manager.getMyIdPlayer()==0 && !send && (System.currentTimeMillis()-waitTime>2500)){
				send = true;
				manager.sendAsign();
		}
		if (loaded && (System.currentTimeMillis()-waitTime>5000)){
				loaded = false;
				manager.changeGameScreen();
		}
		
	}
	
	@SuppressWarnings("unused")
	private void printText(String text,float scale,Color color,float posx,float posy){
		Color aux = font2.getColor();
		font2.setColor(color);
		font2.setScale(scale);
		font2.draw(batcher,text, posx*49 ,posy*49);
		font2.setColor(aux);
		font2.setScale(1);
	}

		
	public String cutName(String name){
		String playerName = name;
		if(playerName.length()>8){
			playerName = name.substring(0,8);
			playerName = playerName.concat("...");
		}
		return playerName;
		
	}	

	public MatchManager getManager() {
		return manager;
	}
	
	public void setManager(MatchManager manager) {
		this.manager = manager;
	}
	
	@Override
	public void resize(int width, int height) {
		if (width!=1024 || height!=630) Desktop.resetScreenSize();

	}

	@Override
	public void resume(){
		
	}

	@Override
	public void show(){
		
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	
}