package net.megafoxhunt.ui;

import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.User;
import net.megafoxhunt.core.UserContainer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class GameUI extends Table{
	
	public void draw(SpriteBatch batch, Camera camera) {
		int berryCount = MyGdxGame.mapHandler.currentMap.getBerryCount();
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();

		MyGdxGame.resources.BASIC_FONT.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25, 25);
		MyGdxGame.resources.BASIC_FONT.draw(batch, "Ms: " + MyGdxGame.network.getCurrentPing(), 100, 25);
		MyGdxGame.resources.BASIC_FONT.draw(batch, "Berries: " + berryCount, screenWidth - screenWidth / 2, screenHeight - 50);
		
		for (User user : UserContainer.getUsersConcurrentSafe()) {
			if (user != null && user.getControlledEntity() != null) {
	        	Vector3 v3 = new Vector3(user.getControlledEntity().getX(), user.getControlledEntity().getY(), 0);
	        	camera.project(v3);
	        	MyGdxGame.resources.BASIC_FONT.draw(batch, user.getName(), v3.x, v3.y);
			}
        }
	}
}
