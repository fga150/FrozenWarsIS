package Screens;
import Application.MatchManager.Direction;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;


public class PenguinAnimation {
	private Animation walkAnimationD; 
	private Animation walkAnimationL; 
	private Animation walkAnimationR; 
	private Animation walkAnimationU; 
    private Texture penguin;              
    private TextureRegion[] walkFramesD;
    private TextureRegion[] walkFramesL;
    private TextureRegion[] walkFramesR;
    private TextureRegion[] walkFramesU;
    private TextureRegion currentFrame;
    private Vector3 position;
    private Direction lookAt;
    
	public PenguinAnimation(FileHandle dir, Vector3 position,Direction lookAt) {
		penguin= new Texture(dir);
		this.lookAt = lookAt;
		this.position = position;
		TextureRegion[][] tmp = TextureRegion.split(penguin, penguin.getWidth() / 4, penguin.getHeight() / 4);
		walkFramesD = new TextureRegion[3];
        walkFramesL = new TextureRegion[3];
        walkFramesR = new TextureRegion[3];
        walkFramesU = new TextureRegion[3];
        int index = 0;
        int i=0;
        for (int j = 0; j < 3; j++) {
        	walkFramesD[index] = tmp[i][j];
            walkFramesL[index]= tmp[i+1][j];
            walkFramesR[index]= tmp[i+2][j];
            walkFramesU[index]= tmp[i+3][j];
            index++;
                }
        
       walkAnimationD = new Animation(0.25f, walkFramesD);
       walkAnimationL = new Animation(0.25f, walkFramesL);
       walkAnimationR = new Animation(0.25f, walkFramesR);
       walkAnimationU = new Animation(0.25f, walkFramesU);
       this.currentFrame = getwalkAnimation().getKeyFrame(0,true);
	}


	private Animation getwalkAnimation() {
		Animation animation = null;
		if (Direction.left.equals(lookAt)){
			animation = walkAnimationL;
		} else if (Direction.right.equals(lookAt)){
			animation = walkAnimationR;
		} else if (Direction.up.equals(lookAt)){
			animation = walkAnimationU;
		} else if (Direction.down.equals(lookAt)){
			animation = walkAnimationD;
		}
		return animation;
	}


	public Animation getwalkAnimationR() {
		return walkAnimationR;
	}
	
	public Animation getwalkAnimationL() {
		return walkAnimationL;
	}
	
	public Animation getwalkAnimationD() {
		return walkAnimationD;
	}
	
	public Animation getwalkAnimationU() {
		return walkAnimationU;
	}

	public TextureRegion getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(TextureRegion currentFrame) {
		this.currentFrame = currentFrame;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 newPosition) {
		this.position.x = newPosition.x;
		this.position.y = newPosition.y;
	}


	public void setlookAt(Direction dir) {
		this.lookAt = dir;		
	}
	
  
}
