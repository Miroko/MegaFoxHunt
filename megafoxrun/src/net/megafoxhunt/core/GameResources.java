package net.megafoxhunt.core;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameResources {

	public static final int DEFAULT_ANIMATION = 0;
	public static final int FRONT_ANIMATION = 1;
	public static final int BACK_ANIMATION = 2;
	
	public Texture DEBUG_TEXTURE;
	
	public Texture FOX_TEXTURE;
	public Texture FOX_BACK_TEXTURE;
	public Texture FOX_FRONT_TEXTURE;
	public Texture FOX_FRONT_BACK_TEXTURE;
	
	public Texture DOG_TEXTURE;
	public Texture DOG_BACK_TEXTURE;
	public Texture DOG_FRONT_TEXTURE;
	
	public Texture BERRY_TEXTURE;
	public Texture HOLE_TEXTURE;
	
	public Animation[] BERRY_ANIMATIONS = new Animation[10];
	
	public Animation[] HOLE_ANIMATIONS = new Animation[10];
	
	public Animation[] FOX_ANIMATIONS = new Animation[10];
	
	public Animation[] DOG_ANIMATIONS  = new Animation[10];
	
	public void init(){
		 DEBUG_TEXTURE = new Texture("data/libgdx.png");
		 
		 FOX_TEXTURE = new Texture("data/kettu_sivusta.png");
		 FOX_FRONT_TEXTURE = new Texture("data/kettu_edesta.png");
		 FOX_BACK_TEXTURE = new Texture("data/kettu_takaa.png");
		 FOX_FRONT_BACK_TEXTURE = new Texture("data/kettu_edesta_takaa.png");
		 
		 DOG_TEXTURE = new Texture("data/dog.png");
		 DOG_FRONT_TEXTURE = new Texture("data/dog.png");
		 DOG_BACK_TEXTURE = new Texture("data/dog.png");
		 
		 BERRY_TEXTURE = new Texture("data/berry.png");
		 HOLE_TEXTURE = new Texture("data/hole.png");
		 
		 BERRY_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(BERRY_TEXTURE, 0.025f, 1, 1);
		 HOLE_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(HOLE_TEXTURE, 0.025f, 1, 1);
		 
		 FOX_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(FOX_TEXTURE, 0.025f, 5, 5);
		 FOX_ANIMATIONS[FRONT_ANIMATION] = generateAnimation(FOX_FRONT_BACK_TEXTURE, 0.025f, 10, 5, 0, 5, 0, 5);
		 FOX_ANIMATIONS[BACK_ANIMATION] = generateAnimation(FOX_FRONT_BACK_TEXTURE, 0.025f, 10, 5, 5, 10, 0, 5);
		 
		 DOG_ANIMATIONS[DEFAULT_ANIMATION] = generateAnimation(DOG_TEXTURE, 0.025f, 1, 1);
		 DOG_ANIMATIONS[FRONT_ANIMATION] = generateAnimation(DOG_TEXTURE, 0.025f, 1, 1);
		 DOG_ANIMATIONS[BACK_ANIMATION] = generateAnimation(DOG_TEXTURE, 0.025f, 1, 1);
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
	
	private Animation generateAnimation(Texture texture, float frameDuration, int cols, int rows, int startX, int endX, int startY, int endY) {
		Animation animation = null;
		
		TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / cols, texture.getHeight() / rows);
        TextureRegion[] frames = new TextureRegion[(endX - startX) * (endY - startY)];
        int index = 0;
        for (int i = startY; i < endY; i++) {
                for (int j = startX; j < endX; j++) {
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
	}
	
	
}
