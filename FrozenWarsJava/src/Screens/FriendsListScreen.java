package Screens;

import java.util.Iterator;
import java.util.Vector;

import Application.Assets;
import Application.Desktop;
import Application.LaunchFrozenWars;
import Server.SmartFoxServer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class FriendsListScreen implements Screen{


	private static FriendsListScreen instance;

	public static FriendsListScreen getInstance() {
		if (instance == null) instance = new FriendsListScreen();
		return instance;
	}
	
	private class ConnectedInfo{
		private String userName;
		private boolean playing;
		public boolean isPlaying() {
			return playing;
		}
		public String getUserName() {
			return userName;
		}
		public ConnectedInfo(String userName, boolean playing) {
			this.userName = userName;
			this.playing = playing;
		}
	}
	
	    /** The gui cam. */
	private OrthographicCamera guiCam;	
	
	/** Se utiliza para dibujar y optimizar las imagenes en el renderizado de la pantalla. */
	private SpriteBatch batcher;
	private Vector3 touchPoint;
	private Game game;
	private SmartFoxServer sfsClient;
	private BitmapFont font;
	     
    private BoundingBox MultiplayerClick; 
    private  BoundingBox backButtonClick;
    private  BoundingBox addFriendClick;
	private BoundingBox userClick;
	private BoundingBox scrollDownConnectedClick;
    private BoundingBox scrollUpConnectedClick;
    private BoundingBox scrollDownDisconnectedClick;
    private BoundingBox scrollUpDisconnectedClick;
    
	private int connectedScroll;
	private int disconnectedScroll;
    
    private Vector<String> disconnectedFriends;
    
    private Vector<ConnectedInfo> drawConnected;
    
    private BoundingBox[] unfriendCon;
    private BoundingBox unfriendConAll;
    private BoundingBox[] unfriendDisc;
    private BoundingBox unfriendDiscAll;
    
    private String user;	
	private String userShown;	
	private LaunchFrozenWars proc;
	

    public FriendsListScreen() {
		instance = this;
    	this.game = LaunchFrozenWars.getGame();
	    sfsClient = SmartFoxServer.getInstance();
    	
		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);
		
	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();
	    font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);


	    disconnectedFriends = new Vector<String>();
	    
	    drawConnected = new Vector<ConnectedInfo>();
	    updateFriends();
	    
	    user = userShown = "";
	    
	    connectedScroll = 0;
	    disconnectedScroll = 0;

	    backButtonClick = new BoundingBox(new Vector3(320,20,0), new Vector3(560,60,0));
	    addFriendClick = new BoundingBox(new Vector3(615,110,0), new Vector3(660,145,0));
	    MultiplayerClick = new BoundingBox(new Vector3(30,540,0), new Vector3(510,605,0));
	    userClick = new BoundingBox(new Vector3(310,105,0), new Vector3(605,140,0));
	    
	    scrollDownConnectedClick = new BoundingBox(new Vector3(423,183,0), new Vector3(475,225,0));
	    scrollUpConnectedClick = new BoundingBox(new Vector3(426,416,0), new Vector3(475,454,0));
	    scrollDownDisconnectedClick = new BoundingBox(new Vector3(926,185,0), new Vector3(974,224,0));
	    scrollUpDisconnectedClick = new BoundingBox(new Vector3(925,417,0), new Vector3(977,456,0));
	    
	    unfriendCon = new BoundingBox[6];
	    unfriendCon[0] = new BoundingBox(new Vector3(72,415,0), new Vector3(119,454,0));
	    unfriendCon[1] = new BoundingBox(new Vector3(72,370,0), new Vector3(116,408,0));
	    unfriendCon[2] = new BoundingBox(new Vector3(72,326,0), new Vector3(121,363,0));
	    unfriendCon[3] = new BoundingBox(new Vector3(73,278,0), new Vector3(119,317,0));
	    unfriendCon[4] = new BoundingBox(new Vector3(73,234,0), new Vector3(119,272,0));
	    unfriendCon[5] = new BoundingBox(new Vector3(72,189,0), new Vector3(114,226,0));
	    unfriendConAll =  new BoundingBox(new Vector3(69,186,0), new Vector3(117,454,0));
	    
	    unfriendDisc = new BoundingBox[6];
	    unfriendDisc[0] = new BoundingBox(new Vector3(572,415,0), new Vector3(615,454,0));
	    unfriendDisc[1] = new BoundingBox(new Vector3(572,370,0), new Vector3(615,408,0));
	    unfriendDisc[2] = new BoundingBox(new Vector3(572,326,0), new Vector3(615,363,0));
	    unfriendDisc[3] = new BoundingBox(new Vector3(572,278,0), new Vector3(615,317,0));
	    unfriendDisc[4] = new BoundingBox(new Vector3(572,234,0), new Vector3(615,272,0));
	    unfriendDisc[5] = new BoundingBox(new Vector3(572,189,0), new Vector3(615,226,0));
	    unfriendDiscAll =  new BoundingBox(new Vector3(572,186,0), new Vector3(615,454,0));
	    
	    proc = (LaunchFrozenWars) Gdx.input.getInputProcessor();
	}

	
	@Override
	public void dispose() {
		batcher.dispose();
		Assets.music.dispose();
		System.exit(0);	
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	
	@Override
	public void render(float arg0) {
		//detectamos si se ha tocado la pantalla
		if (Gdx.input.justTouched()){
			guiCam.unproject(touchPoint.set(Gdx.input.getX(),Gdx.input.getY(),0));
      		//System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));
			//compruebo si he tocado play (se abre ventana de introduccion de usuario si no esta logeado)
			
      		if (backButtonClick.contains(touchPoint)){
      			proc.setInfoPressed(0);
      			game.setScreen(InitialScreen.getInstance());
      		 } else if (MultiplayerClick.contains(touchPoint)){
      			proc.setInfoPressed(0);
      			this.game.setScreen(MultiplayerScreen.getInstance());
      		 } else if (userClick.contains(touchPoint)){
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			proc.setInfoPressed(21);
      		 } else if (addFriendClick.contains(touchPoint)){
      			proc.setInfoPressed(0);
      			SmartFoxServer.getInstance().sendFriendRequest(user);
       		 } else if (scrollDownConnectedClick.contains(touchPoint)){
       			proc.setInfoPressed(0);
       			if (connectedScroll < (drawConnected.size()) - 6) connectedScroll++;
       		 } else if (scrollUpConnectedClick.contains(touchPoint)){
       			proc.setInfoPressed(0);
       			if (connectedScroll != 0) connectedScroll--;     	
 			 } else if (scrollDownDisconnectedClick.contains(touchPoint)){
 				proc.setInfoPressed(0);
 				if (disconnectedScroll < (disconnectedFriends.size()) - 6) disconnectedScroll++;
        	 } else if (scrollUpDisconnectedClick.contains(touchPoint)){
        		 proc.setInfoPressed(0);
        		if (disconnectedScroll != 0) disconnectedScroll--;     	
        	 } else if (unfriendConAll.contains(touchPoint)){
        		 proc.setInfoPressed(0);
        		 unfriendConnected(); 
  			 } else if (unfriendDiscAll.contains(touchPoint)){
  				proc.setInfoPressed(0);
  				 unfriendDisconnected();	
  			 } else {
      			proc.setInfoPressed(0);
      		 }
		}
		
		
		//crear solamente un batcher por pantalla y eliminarlo cuando no se use
	
      		GL10 gl = Gdx.graphics.getGL10(); //referencia a OpenGL 1.0
            gl.glClearColor(0,1,0,1);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
           
            guiCam.update();
            batcher.setProjectionMatrix(guiCam.combined);
             
            //Dibujando el Background
            batcher.disableBlending();
            //se elimina graficamente la transparencia ya que es un fondo
            batcher.begin();
            batcher.draw(Assets.backGrey,0,0,1024,630);
            batcher.end();

            //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();
            batcher.draw(Assets.backButton, 320, 20); 
            batcher.draw(Assets.multiplayerButtonUnpressed, 27, 540);
            batcher.draw(Assets.inviteFriendsButtonPressed, 521, 540);
            
            batcher.draw(Assets.statusCancel, 610, 20);
            font.drawWrapped(batcher, "Click there to delete a friend", 650,50, 360);
            
            drawConnected();
            drawDisconnected(); 
            drawInfo();
            
            batcher.draw(Assets.addFriend, 290, 90); 
            
            userShown = user;
            if (user.equals("") && proc.getInfoPressed() != 21){
            	font.drawWrapped(batcher, "Add friends here", 320,140, 260);
            } else {
            
            if (font.getBounds(userShown).width > 250){
				int j = 0;
				while (font.getBounds(userShown.substring(j, userShown.length())).width > 250) j++;
					userShown = userShown.substring(j, userShown.length());
			}

            
            if (proc.getInfoPressed() != 21) font.drawWrapped(batcher, userShown, 320,140, 260);
            else { //Draw with Cursor
            	if (System.currentTimeMillis()%1000 > 500) font.drawWrapped(batcher, userShown+"|", 320,140, 260);
            	else font.drawWrapped(batcher, userShown, 320,140, 260);
            }
            }
 
            batcher.end();
            
            ConfirmScreen.getInstance().createConfirmIfNeeded();
            AcceptScreen.getInstance().createAcceptIfNeeded();
	}

	
	public void setMessage(char c){
		if (proc.getInfoPressed() == 21) user += c;
	}
	
	public void del(){
		if (proc.getInfoPressed() == 21 && user.length()>0){
	        user = (String)user.subSequence(0, user.length()-1);    
        }
	}
	
	public void enter(){
		if (proc.getInfoPressed() == 21) {
			proc.setInfoPressed(0);
  			SmartFoxServer.getInstance().sendFriendRequest(user);
		}
	}
	
	public void updateFriends(){
		sfsClient.getMyFriendsRequest();
	}
	
	public void updateFriends(Vector<String> playingFriends, Vector<String> connectedNotPlayingFriends, Vector<String> disconnectedFriends) {
		drawConnected.clear();
		
		Iterator<String> itNotPlaying = connectedNotPlayingFriends.iterator();
		while (itNotPlaying.hasNext()) drawConnected.add(new ConnectedInfo(itNotPlaying.next(),false));
		
		Iterator<String> itPlaying = playingFriends.iterator();
		while (itPlaying.hasNext()) drawConnected.add(new ConnectedInfo(itPlaying.next(),true));
		
		this.disconnectedFriends = disconnectedFriends;
	}
	
	private void drawConnected() {
		batcher.draw(Assets.listOfPeopleOn, 60, 180);  
		for (int i = 0; i < Math.min(drawConnected.size(), 6); i++){
			String name = drawConnected.elementAt(i+connectedScroll).getUserName();
			
			batcher.draw(Assets.statusCancel, 75, (415-45*i));
			Color oldColor = font.getColor();
			if (!drawConnected.elementAt(i+connectedScroll).isPlaying()) 
				font.setColor(Color.GREEN);
			else if (drawConnected.elementAt(i+connectedScroll).isPlaying()) 
				font.setColor(Color.valueOf("9400D3"));
			font.draw(batcher, name, 125,(447-45*i));
			font.setColor(oldColor);
		}
	}
	
	private void drawDisconnected() {
		batcher.draw(Assets.listOfPeopleOff, 560, 180); 
		
		for (int i = 0; i < Math.min(disconnectedFriends.size(), 6); i++){
			String name = disconnectedFriends.elementAt(i+disconnectedScroll);
			Color oldColor = font.getColor();
			font.setColor(Color.RED);
			font.draw(batcher, name, 615,(447-45*i));
			font.setColor(oldColor);
			batcher.draw(Assets.statusCancel, 575, (415-45*i));
		}
	}
	
	private void drawInfo() {
		Color oldColor = font.getColor();
		font.setColor(Color.GREEN);
		font.draw(batcher, "Available", 155 , 160);
		font.setColor(Color.valueOf("9400D3"));
		font.draw(batcher, "In game", 170 , 120);
		font.setColor(Color.RED);
		font.draw(batcher, "Disconnected", 690 , 160);		
		font.setColor(oldColor);
	}
	
	private void unfriendConnected() {
		for (int i = 0; i < Math.min(drawConnected.size(), 6); i++){
			if (unfriendCon[i].contains(touchPoint)){
				String player = drawConnected.elementAt(i+connectedScroll).getUserName();
				ConfirmScreen.getInstance().setNewConfirmScreen("Unfriend", player);
			}
		}
	}
	
	
	private void unfriendDisconnected() {
		for (int i = 0; i < Math.min(disconnectedFriends.size(), 6); i++){
			if (unfriendDisc[i].contains(touchPoint)){
				String player = disconnectedFriends.elementAt(i+disconnectedScroll);
				ConfirmScreen.getInstance().setNewConfirmScreen("Unfriend", player);
			}
		}
	}
	
	@Override
	public void resize(int arg0, int arg1) {
		if (arg0!=1024 || arg1!=630) Desktop.resetScreenSize();

	}

	@Override
	public void resume() {
		
	}

	@Override
	public void show() {
		
	}
}