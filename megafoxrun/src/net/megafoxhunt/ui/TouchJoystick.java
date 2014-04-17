package net.megafoxhunt.ui;

import net.megafoxhunt.core.GameNetwork;

import net.megafoxhunt.core.GameResources;
import net.megafoxhunt.core.MyGdxGame;

import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.shared.Shared;
import net.megafoxhunt.shared.KryoNetwork.ActivateItem;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TouchJoystick {
	
	private static final int WIDTH = 64;
	private static final int HEIGHT = 64;
	private static final int PAD_X = 100;
	private static final int PAD_Y = 100;
	private static final int SPACING = WIDTH / 3;
	
	private static final int BTN1_X = Gdx.graphics.getWidth() - WIDTH - SPACING;
	private static final int BTN1_Y = PAD_Y;
	
	private static final int BTN2_X = BTN1_X - WIDTH - SPACING;
	private static final int BTN2_Y = PAD_Y;
	
	private int actionBtnPressed = 0;
	
	private GameNetwork network;
	
	private int direction;
	
	public TouchJoystick(GameNetwork network) {
		this.network = network;
	}
	
	public void draw(SpriteBatch batch) {
		/*
		batch.draw(direction == Shared.DIRECTION_LEFT ? MyGdxGame.resources.btnPressed : MyGdxGame.resources.btnNormal, PAD_X - SPACING - WIDTH, PAD_Y - (HEIGHT / 2), WIDTH, HEIGHT);
		batch.draw(direction == Shared.DIRECTION_RIGHT ? MyGdxGame.resources.btnPressed : MyGdxGame.resources.btnNormal, PAD_X + SPACING, PAD_Y - (HEIGHT / 2), WIDTH, HEIGHT);
		batch.draw(direction == Shared.DIRECTION_UP ? MyGdxGame.resources.btnPressed : MyGdxGame.resources.btnNormal, PAD_X - (WIDTH / 2), PAD_Y + SPACING, WIDTH, HEIGHT);
		batch.draw(direction == Shared.DIRECTION_DOWN ? MyGdxGame.resources.btnPressed : MyGdxGame.resources.btnNormal, PAD_X - (WIDTH / 2), PAD_Y - SPACING - HEIGHT, WIDTH, HEIGHT);
		*/
		
		batch.draw(actionBtnPressed == 1 ? MyGdxGame.resources.btnPressed : MyGdxGame.resources.btnNormal, BTN1_X - (WIDTH / 2), BTN1_Y - (HEIGHT / 2), WIDTH, HEIGHT);
		batch.draw(actionBtnPressed == 2 ? MyGdxGame.resources.btnPressed : MyGdxGame.resources.btnNormal, BTN2_X - (WIDTH / 2), BTN2_Y - (HEIGHT / 2), WIDTH, HEIGHT);
	}
	
	public void mouseDown(int x, int y) {
		y = Gdx.graphics.getHeight() - y;
		
		double shortestDistance = 0;
		int direction = 0;
		
		if (x > 0 && x < Gdx.graphics.getWidth() / 3 && y > 0 && y < Gdx.graphics.getHeight() / 2) {
			/*
			// UP
			double dist = getDistance(PAD_X, PAD_Y + SPACING + (HEIGHT / 2), x, y);
			shortestDistance = dist;
			direction = Shared.DIRECTION_UP;
			
			// DOWN
			dist = getDistance(PAD_X, PAD_Y - SPACING - (HEIGHT / 2), x, y);
			if (dist < shortestDistance) {
				shortestDistance = dist;
				direction = Shared.DIRECTION_DOWN;
			}
			
			// LEFT
			dist = getDistance(PAD_X - SPACING - (WIDTH / 2), PAD_Y, x, y);
			if (dist < shortestDistance) {
				shortestDistance = dist;
				direction = Shared.DIRECTION_LEFT;
			}
			
			// RIGHT
			dist = getDistance(PAD_X + SPACING + (WIDTH / 2), PAD_Y, x, y);
			if (dist < shortestDistance) {
				shortestDistance = dist;
				direction = Shared.DIRECTION_RIGHT;
			}
			if (shortestDistance < WIDTH) sendDirection(direction);
			*/
		} else {
			
		}
	}
	
	public void tap(float x, float y) {
		y = Gdx.graphics.getHeight() - y;
		if (getDistance(BTN1_X, BTN1_Y, x, y) < WIDTH) {
			network.getKryoClient().sendTCP(new ActivateItem());
		}
		
		/*
		if (getDistance(BTN2_X, BTN2_Y, x, y) < WIDTH) {
			network.getKryoClient().sendTCP(new GoInHole());
		}
		*/
	}
	
	private double getDistance(int centerX, int centerY, int x, int y) {
	    double dist = Math.sqrt((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y));
	    return dist;
	}
	
	private double getDistance(int centerX, int centerY, float x, float y) {
	    double dist = Math.sqrt((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y));
	    return dist;
	}
	
	public void mouseUp(int x, int y) {
		direction = 0;
		actionBtnPressed = 0;
	}
	
	private void sendDirection(int direction){
		// IF DIRECTION HAS CHANGED
		EntityMovable entity = network.getLocalUser().getControlledEntity();
		this.direction = direction;
		if (entity != null) entity.setDirection(direction);
	}
}
