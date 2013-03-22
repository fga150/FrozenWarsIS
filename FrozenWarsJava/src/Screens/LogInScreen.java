package Screens;

import Application.Assets;
import Application.LaunchFrozenWars;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class LogInScreen implements Screen {
	
	private static LogInScreen instance;

	public static LogInScreen getInstance() {
		if (instance == null) instance = new LogInScreen();
		return instance;
	}
	

	private OrthographicCamera guiCam;
	private SpriteBatch batcher; 
	private Vector3 touchPoint;
	private BoundingBox okClick;
	private BoundingBox userClick;
	private BoundingBox passClick;
	private BoundingBox backClick;
	private Game game;
	private BitmapFont font;
	private int infoPressed;
	private String user;
	private String pass;
	private String passShown;	
	private long time;
	
/*TODO : Poner singleton */
	
	public LogInScreen() {
		this.game = LaunchFrozenWars.getGame();
		this.infoPressed = 0;
		this.time = System.nanoTime();
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;
		
		this.user = this.pass = this.passShown = "";

		
		guiCam = new OrthographicCamera(1024,630);
		guiCam.position.set(512,315,0);

		
		font = new BitmapFont(Gdx.files.internal("data/first.fnt"), Gdx.files.internal("data/first.png"), false);;

	    batcher = new SpriteBatch();
	    touchPoint = new Vector3();

	    userClick = new BoundingBox(new Vector3(440,395,0), new Vector3(670,440,0));
	    passClick = new BoundingBox(new Vector3(435,325,0), new Vector3(700,370,0));      
	    okClick = new BoundingBox(new Vector3(335,240,0), new Vector3(500,300,0));  
	    backClick = new BoundingBox(new Vector3(540,245,0), new Vector3(700,300,0));  
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
      		System.out.println(Integer.toString((int)touchPoint.x).concat(",").concat(Integer.toString((int)touchPoint.y)));

      		//compruebo si he tocado yes 
      		if (userClick.contains(touchPoint)){
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			this.infoPressed = 1;
      			System.out.println("user");
      		} else if(passClick.contains(touchPoint)){ //compruebo si he tocado no
      			Gdx.input.setOnscreenKeyboardVisible(true);
      			this.infoPressed = 2;
      			System.out.println("pass");
      		} else if(okClick.contains(touchPoint)){ //compruebo si he tocado no
      			System.out.println("ok");
      		} else if(backClick.contains(touchPoint)){ //compruebo si he tocado no
      			game.setScreen(LogSignScreen.getInstance());
      			System.out.println("back");
      		} else{
      			this.infoPressed = 0;
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
            batcher.draw(Assets.backGrey,0,0, 1024, 630);
            batcher.end();

          //Dibujando elementos en pantalla activamos el Blending
            batcher.enableBlending();
            batcher.begin();    
            batcher.draw(Assets.logInWindow, 300, 200);

            font.drawWrapped(batcher, user, 450, 435, 240);
            font.drawWrapped(batcher, passShown, 450, 365, 240);
            batcher.end();	

            
            if ( this.infoPressed !=0 && ((System.nanoTime() - this.time) > 127000000)){
            	this.completeMessagePc();
            	this.time = System.nanoTime();
            }
            
            
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void show() {
	}
	
	
	private void completeMessagePc(){
		char aux = 0;
		boolean delete = false;
		
		if(Gdx.input.isKeyPressed(Keys.ANY_KEY)){
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)){
				
				if(Gdx.input.isKeyPressed(Keys.A)){
					aux = 'A';	
		        } else if (Gdx.input.isKeyPressed(Keys.B)){
		        	aux = 'B';	
		        } else if (Gdx.input.isKeyPressed(Keys.C)){
		        	aux = 'C';	
		        } else if (Gdx.input.isKeyPressed(Keys.D)){
		        	aux = 'D';	
		        } else if (Gdx.input.isKeyPressed(Keys.E)){
		        	aux = 'E';	
		        } else if (Gdx.input.isKeyPressed(Keys.F)){
		        	aux = 'F';	
		        } else if (Gdx.input.isKeyPressed(Keys.G)){
		        	aux = 'G';	
		        } else if (Gdx.input.isKeyPressed(Keys.H)){
		        	aux = 'H';	
		        } else if (Gdx.input.isKeyPressed(Keys.I)){
		        	aux = 'I';	
		        } else if (Gdx.input.isKeyPressed(Keys.J)){
		        	aux = 'J';	
		        } else if (Gdx.input.isKeyPressed(Keys.K)){
		        	aux = 'K';	
		        } else if (Gdx.input.isKeyPressed(Keys.L)){
		        	aux = 'L';	
		        } else if (Gdx.input.isKeyPressed(Keys.M)){
		        	aux = 'M';	
		        } else if (Gdx.input.isKeyPressed(Keys.N)){
		        	aux = 'N';	
		        } else if (Gdx.input.isKeyPressed(Keys.O)){
		        	aux = 'O';	
		        } else if (Gdx.input.isKeyPressed(Keys.P)){
		        	aux = 'P';	
		        } else if (Gdx.input.isKeyPressed(Keys.Q)){
		        	aux = 'Q';	
		        } else if (Gdx.input.isKeyPressed(Keys.R)){
		        	aux = 'R';	
		        } else if (Gdx.input.isKeyPressed(Keys.S)){
		        	aux = 'S';	
		        } else if (Gdx.input.isKeyPressed(Keys.T)){
		        	aux = 'T';	
		        } else if (Gdx.input.isKeyPressed(Keys.U)){
		        	aux = 'U';	
		        } else if (Gdx.input.isKeyPressed(Keys.V)){
		        	aux = 'V';	
		        } else if (Gdx.input.isKeyPressed(Keys.W)){
		        	aux = 'W';	
		        } else if (Gdx.input.isKeyPressed(Keys.X)){
		        	aux = 'X';	
		        } else if (Gdx.input.isKeyPressed(Keys.Y)){
		        	aux = 'Y';	
		        } else if (Gdx.input.isKeyPressed(Keys.Z)){
		        	aux = 'Z';	
		        } 
			
			} else {	
				
		        if(Gdx.input.isKeyPressed(Keys.A)){
					aux = 'a';	
		        } else if (Gdx.input.isKeyPressed(Keys.B)){
		        	aux = 'b';	
		        } else if (Gdx.input.isKeyPressed(Keys.C)){
		        	aux = 'c';	
		        } else if (Gdx.input.isKeyPressed(Keys.D)){
		        	aux = 'd';	
		        } else if (Gdx.input.isKeyPressed(Keys.E)){
		        	aux = 'e';	
		        } else if (Gdx.input.isKeyPressed(Keys.F)){
		        	aux = 'f';	
		        } else if (Gdx.input.isKeyPressed(Keys.G)){
		        	aux = 'g';	
		        } else if (Gdx.input.isKeyPressed(Keys.H)){
		        	aux = 'h';	
		        } else if (Gdx.input.isKeyPressed(Keys.I)){
		        	aux = 'i';	
		        } else if (Gdx.input.isKeyPressed(Keys.J)){
		        	aux = 'j';	
		        } else if (Gdx.input.isKeyPressed(Keys.K)){
		        	aux = 'k';	
		        } else if (Gdx.input.isKeyPressed(Keys.L)){
		        	aux = 'l';	
		        } else if (Gdx.input.isKeyPressed(Keys.M)){
		        	aux = 'm';	
		        } else if (Gdx.input.isKeyPressed(Keys.N)){
		        	aux = 'n';	
		        } else if (Gdx.input.isKeyPressed(Keys.O)){
		        	aux = 'o';	
		        } else if (Gdx.input.isKeyPressed(Keys.P)){
		        	aux = 'p';	
		        } else if (Gdx.input.isKeyPressed(Keys.Q)){
		        	aux = 'q';	
		        } else if (Gdx.input.isKeyPressed(Keys.R)){
		        	aux = 'r';	
		        } else if (Gdx.input.isKeyPressed(Keys.S)){
		        	aux = 's';	
		        } else if (Gdx.input.isKeyPressed(Keys.T)){
		        	aux = 't';	
		        } else if (Gdx.input.isKeyPressed(Keys.U)){
		        	aux = 'u';	
		        } else if (Gdx.input.isKeyPressed(Keys.V)){
		        	aux = 'v';	
		        } else if (Gdx.input.isKeyPressed(Keys.W)){
		        	aux = 'w';	
		        } else if (Gdx.input.isKeyPressed(Keys.X)){
		        	aux = 'x';	
		        } else if (Gdx.input.isKeyPressed(Keys.Y)){
		        	aux = 'y';	
		        } else if (Gdx.input.isKeyPressed(Keys.Z)){
		        	aux = 'z';	
		        } else if (Gdx.input.isKeyPressed(Keys.NUM_0)){
		        	aux = '0';	
		        }  else if (Gdx.input.isKeyPressed(Keys.NUM_1)){
		        	aux = '1';	
		        }  else if (Gdx.input.isKeyPressed(Keys.NUM_2)){
		        	aux = '2';	
		        }  else if (Gdx.input.isKeyPressed(Keys.NUM_3)){
		        	aux = '3';	
		        }  else if (Gdx.input.isKeyPressed(Keys.NUM_4)){
		        	aux = '4';	
		        }  else if (Gdx.input.isKeyPressed(Keys.NUM_5)){
		        	aux = '5';	
		        }   else if (Gdx.input.isKeyPressed(Keys.NUM_6)){
		        	aux = '6';	
		        }   else if (Gdx.input.isKeyPressed(Keys.NUM_7)){
		        	aux = '7';	
		        }   else if (Gdx.input.isKeyPressed(Keys.NUM_8)){
		        	aux = '8';	
		        }   else if (Gdx.input.isKeyPressed(Keys.NUM_9)){
		        	aux = '9';	
		        } else if (Gdx.input.isKeyPressed(Keys.DEL)){
		        	delete = true;
		        }
			}
        
		}

        if (aux != 0){
	        if (this.infoPressed == 1){
	        	user += aux;
	        	System.out.println("user: " + user);
	        } else if (this.infoPressed == 2){
	        	pass += aux;
	        	passShown +='$';
	        	System.out.println("pass: " + pass);
	        } 
        }  
        
        if (delete){
        	if (this.infoPressed == 1 && user.length()>0){
	        	user = (String)user.subSequence(0, user.length()-1);
	        } else if (this.infoPressed == 2 && pass.length()>0){
	        	pass = (String)pass.subSequence(0, pass.length()-1);
	        	passShown = (String)passShown.subSequence(0, passShown.length()-1);
	        }
        }
	}
}
