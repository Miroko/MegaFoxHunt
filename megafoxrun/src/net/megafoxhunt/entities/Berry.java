package net.megafoxhunt.entities;

import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.core.GameTextures;

import com.badlogic.gdx.graphics.Texture;

public class Berry extends StaticObject{
	
	private static final Texture TEXTURE_BERRY = GameTextures.DEBUG_TEXTURE;

	public Berry(int id, float x, float y) {
		super(id, x, y, TEXTURE_BERRY);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(float delta, GameNetwork network) {
		// TODO Auto-generated method stub
		
	}

}
