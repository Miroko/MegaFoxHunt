package net.megafoxhunt.core;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GameResources {

	public Texture DEBUG_TEXTURE;
	
	public Texture FOX_TEXTURE;
	public Texture DOG_TEXTURE;
	public Texture BERRY_TEXTURE;
	public Texture HOLE_TEXTURE;
	
	public final int BERRY_DEFAULT_ANIMATION = 0;
	public Animation[] BERRY_ANIMATIONS = new Animation[10];
	
	public final int HOLE_DEFAULT_ANIMATION = 0;
	public Animation[] HOLE_ANIMATIONS = new Animation[10];
	
	public final int FOX_DEFAULT_ANIMATION = 0;
	public Animation[] FOX_ANIMATIONS = new Animation[10];
	
	public final int DOG_DEFAULT_ANIMATION = 0;
	public Animation[] DOG_ANIMATIONS  = new Animation[10];
	
	public void init(){
		 DEBUG_TEXTURE = new Texture("data/libgdx.png");
		 FOX_TEXTURE = new Texture("data/fox_run.png");
		 DOG_TEXTURE = new Texture("data/dog.png");
		 BERRY_TEXTURE = new Texture("data/berry.png");
		 HOLE_TEXTURE = new Texture("data/hole.png");
		 
		 BERRY_ANIMATIONS[BERRY_DEFAULT_ANIMATION] = generateAnimation(BERRY_TEXTURE, 0.025f, 1, 1);
		 HOLE_ANIMATIONS[HOLE_DEFAULT_ANIMATION] = generateAnimation(HOLE_TEXTURE, 0.025f, 1, 1);
		 FOX_ANIMATIONS[FOX_DEFAULT_ANIMATION] = generateAnimation(FOX_TEXTURE, 0.025f, 5, 5);
		 DOG_ANIMATIONS[DOG_DEFAULT_ANIMATION] = generateAnimation(DOG_TEXTURE, 0.025f, 1, 1); 
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
	}
	
	
}
