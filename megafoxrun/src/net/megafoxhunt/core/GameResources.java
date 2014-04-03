package net.megafoxhunt.core;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameResources {

	public static final int DEFAULT_ANIMATION = 0;
	public static final int FRONT_ANIMATION = 1;
	public static final int BACK_ANIMATION = 2;
	
	public Texture DEBUG_TEXTURE;
	public Texture FOX_TEXTURE;
	public Texture DOG_TEXTURE;
	public Texture BERRY_TEXTURE;
	public Texture HOLE_TEXTURE;
	public Texture BARRICADE_TEXTURE;
	public Texture POWERRUP_TEXTURE;
	
	public Animation[] BERRY_ANIMATIONS = new Animation[10];
	public Animation[] HOLE_ANIMATIONS = new Animation[10];
	public Animation[] BARRICADE_ANIMATIONS = new Animation[10];
	public Animation[] FOX_ANIMATIONS = new Animation[10];
	public Animation[] DOG_ANIMATIONS  = new Animation[10];
	public Animation[] POWERRUP_ANIMATIONS = new Animation[10];
	
	public Texture connectButtonTexture;
	public Texture quitButtonTexture;
	public Texture preferDogButtonTexture;
	public Texture preferFoxButtonTexture;
	public Texture readyButtonTexture;
	public Texture btnNormal;
	public Texture btnPressed;
	
	public void init(){
		 DEBUG_TEXTURE = new Texture("data/libgdx.png");
		 
		 POWERRUP_TEXTURE = new Texture("data/powerup.png");
		 
		 FOX_TEXTURE = new Texture("data/fox.png");
		 FOX_TEXTURE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 DOG_TEXTURE = new Texture("data/dog.png");
		 DOG_TEXTURE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 BERRY_TEXTURE = new Texture("data/berry.png");
		 BERRY_TEXTURE.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		 
		 HOLE_TEXTURE = new Texture("data/hole.png");
		 
		 BARRICADE_TEXTURE = new Texture("data/barricade.png");
		 
		 POWERRUP_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(POWERRUP_TEXTURE, 0.025f, 1, 1);
		 
		 BERRY_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(BERRY_TEXTURE, 1f, 1, 1);
		 HOLE_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(HOLE_TEXTURE, 1f, 1, 1);
		 BARRICADE_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(BARRICADE_TEXTURE, 1f, 1, 1);
		 
		 FOX_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(FOX_TEXTURE, 0.025f, 0, 0, 104, 66 , 1, 10);
		 FOX_ANIMATIONS[FRONT_ANIMATION] = generateAnimation(FOX_TEXTURE, 0.025f, 0, 66, 64, 99 , 1, 10);
		 FOX_ANIMATIONS[BACK_ANIMATION] = generateAnimation(FOX_TEXTURE, 0.025f, 0, 166, 64, 106, 1, 10);
		 
		 DOG_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(DOG_TEXTURE, 0.042f, 0, 0, 187, 114, 1, 8);
		 DOG_ANIMATIONS[FRONT_ANIMATION] = generateAnimation(DOG_TEXTURE, 0.042f, 0, 114, 79, 150, 1, 8);
		 DOG_ANIMATIONS[BACK_ANIMATION] = generateAnimation(DOG_TEXTURE, 0.042f, 0, 114, 79, 150, 1, 8);
		 
		 connectButtonTexture = new Texture("data/connect.png"); 
		 quitButtonTexture = new Texture("data/quit.png");
		 preferDogButtonTexture = new Texture("data/preferDog.png");
		 preferFoxButtonTexture = new Texture("data/preferFox.png");
		 readyButtonTexture = new Texture("data/ready.png");
		 
		 btnNormal = new Texture("data/btn_normal.png");
		 btnPressed = new Texture("data/btn_pressed.png");
	}
	
	private Animation generateAnimation(Texture texture, float frameDuration, int startX, int startY, int width, int height, int rows, int cols) {
		Animation animation = null;
		TextureRegion[] frames = new TextureRegion[rows * cols];
		
		int framesIndex = 0;
		
		for (int y = 0; y < rows; y++) {
			for (int x = 0; x < cols; x++) {
				frames[framesIndex] = new TextureRegion(texture, x * width + startX, y * height + startY, width, height);
				framesIndex++;
			}
		}
		
		animation = new Animation(frameDuration, frames);
        return animation;
	}
	
	private Animation generateAnimation(Texture texture, float frameDuration, int cols, int rows){
		Animation animation = null;
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows); 
        TextureRegion[] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                        frames[index++] = tmp[i][j];
                }
        }
        animation = new Animation(frameDuration, frames);
        return animation;
	}
	
	public void dispose(){
		DEBUG_TEXTURE.dispose();
		FOX_TEXTURE.dispose();
		DOG_TEXTURE.dispose();
		BERRY_TEXTURE.dispose();
		HOLE_TEXTURE.dispose();
		BARRICADE_TEXTURE.dispose();
		POWERRUP_TEXTURE.dispose();
		
		connectButtonTexture.dispose();
		quitButtonTexture.dispose();
		preferDogButtonTexture.dispose();
		preferFoxButtonTexture.dispose();
		readyButtonTexture.dispose();
		
		btnNormal.dispose();
		btnPressed.dispose();
	}
	
	
}
