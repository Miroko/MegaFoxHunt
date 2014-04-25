package net.megafoxhunt.ui;

import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.core.GameResources;

import net.megafoxhunt.core.MyGdxGame;

import net.megafoxhunt.shared.KryoNetwork.ActivateItem;
import net.megafoxhunt.shared.Shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TouchJoystick {
	
	private static final int WIDTH = 64;
	private static final int HEIGHT = 64;
	private static final int SPACING = WIDTH / 3;
	
	private static final int BTN1_X = Gdx.graphics.getWidth() - WIDTH - SPACING;
	private static final int BTN1_Y = 100;
	
	private int actionBtnPressed = 0;
	
	private GameNetwork network;

	public TouchJoystick(GameNetwork network) {
		this.network = network;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(actionBtnPressed == 1 ? MyGdxGame.resources.btnPressed : MyGdxGame.resources.btnNormal, BTN1_X - (WIDTH / 2), BTN1_Y - (HEIGHT / 2), WIDTH, HEIGHT);
		int itemType = MyGdxGame.network.getLocalUser().getItemType();
		
		switch (itemType) {
			case Shared.ITEM_BARRICADE:
				batch.draw(MyGdxGame.resources.BARRICADE_ANIMATIONS[GameResources.DEFAULT_ANIMATION].getKeyFrame(0), BTN1_X - (WIDTH / 2), BTN1_Y - (HEIGHT / 2), WIDTH, HEIGHT);
				break;
			case Shared.ITEM_BOMB:
				batch.draw(MyGdxGame.resources.BOMB_ANIMATIONS[GameResources.DEFAULT_ANIMATION].getKeyFrame(0), BTN1_X - (WIDTH / 2), BTN1_Y - (HEIGHT / 2), WIDTH, HEIGHT);
				break;
		}
	}
	
	public void mouseDown(int x, int y) {
		y = Gdx.graphics.getHeight() - y;

		if (getDistance(BTN1_X, BTN1_Y, x, y) < WIDTH) {
			actionBtnPressed = 1;
		}
	}
	
	private double getDistance(int centerX, int centerY, int x, int y) {
	    double dist = Math.sqrt((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y));
	    return dist;
	}

	public void mouseUp(int x, int y) {
		y = Gdx.graphics.getHeight() - y;
		
		if (getDistance(BTN1_X, BTN1_Y, x, y) < WIDTH) {
			network.getKryoClient().sendTCP(new ActivateItem());
		}
		actionBtnPressed = 0;
	}
}
