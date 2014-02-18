package net.megafoxhunt.core;
import com.badlogic.gdx.graphics.Texture;

public class GameTextures {

	public static Texture DEBUG_TEXTURE;
	
	public static Texture FOX_TEXTURE;
	public static Texture DOG_TEXTURE;
	public static Texture BERRY_TEXTURE;
	
	public static void init(){
		 DEBUG_TEXTURE = new Texture("data/libgdx.png");
		 FOX_TEXTURE = new Texture("data/fox.png");
		 DOG_TEXTURE = new Texture("data/dog.png");
		 BERRY_TEXTURE = new Texture("data/berry.png");
	}

	public static void dispose(){
		DEBUG_TEXTURE.dispose();
		FOX_TEXTURE.dispose();
		DOG_TEXTURE.dispose();
		BERRY_TEXTURE.dispose();
	}
	
}
