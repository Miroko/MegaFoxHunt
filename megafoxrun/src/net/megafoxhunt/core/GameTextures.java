package net.megafoxhunt.core;
import com.badlogic.gdx.graphics.Texture;

public class GameTextures {

	public static Texture DEBUG_TEXTURE;
	
	public static void init(){
		 DEBUG_TEXTURE = new Texture("data/libgdx.png");
	}

	public static void dispose(){
		DEBUG_TEXTURE.dispose();
	}
	
}
