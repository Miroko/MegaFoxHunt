package net.megafoxhunt.ui;

import net.megafoxhunt.core.GameMapClientSide;
import net.megafoxhunt.core.MyGdxGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameUI {
	
	private BitmapFont font;
	
	public GameUI() {
		font = new BitmapFont();
	}
	
	public void draw(SpriteBatch batch) {
		int berryCount = MyGdxGame.mapHandler.currentMap.getBerryCount();
		int screenWidth = Gdx.graphics.getWidth();
		int screenHeight = Gdx.graphics.getHeight();
		
		
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 25, 25);
        font.draw(batch, "Ms: " + MyGdxGame.network.getCurrentPing(), 100, 25);
        font.draw(batch, "Berries: " + berryCount, screenWidth - screenWidth / 2, screenHeight - 50);
	}
}
