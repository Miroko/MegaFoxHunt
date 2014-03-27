package net.megafoxhunt.ui;

import net.megafoxhunt.core.GameNetwork;

import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.shared.Shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TouchJoystick {
	
	private static final int WIDTH = 256;
	private static final int HEIGHT = 256;
	private static final int XPOS = Gdx.graphics.getWidth() - WIDTH - 30;
	private static final int YPOS = 30;
	
	private Texture pad;
	
	private GameNetwork network;
	
	public TouchJoystick(GameNetwork network) {
		this.network = network;
		
		pad = new Texture(Gdx.files.internal("data/pad.png"));
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(pad, XPOS, YPOS, WIDTH, HEIGHT);
	}
	
	public void mouseDown(int x, int y) {
		y = Gdx.graphics.getHeight() - y;
		
		double shortestDistance = 0;
		int direction = 0;
		
		// UP
		double dist = getDistance(XPOS + 128, YPOS + 208, x, y);
		shortestDistance = dist;
		direction = Shared.DIRECTION_UP;
		
		// DOWN
		dist = getDistance(XPOS + 128, YPOS + 48, x, y);
		if (dist < shortestDistance) {
			shortestDistance = dist;
			direction = Shared.DIRECTION_DOWN;
		}
		
		// LEFT
		dist = getDistance(XPOS + 48, YPOS + 128, x, y);
		if (dist < shortestDistance) {
			shortestDistance = dist;
			direction = Shared.DIRECTION_LEFT;
		}
		
		// RIGHT
		dist = getDistance(XPOS + 208, YPOS + 128, x, y);
		if (dist < shortestDistance) {
			shortestDistance = dist;
			direction = Shared.DIRECTION_RIGHT;
		}
	
		if (shortestDistance < 80) sendDirection(direction);
	}
	
	private double getDistance(int centerX, int centerY, int x, int y) {
	    double dist = Math.sqrt((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y));
	    return dist;
	}
	
	public void mouseUp(int x, int y) {
		sendDirection(EntityMovable.DIRECTION_STOP);
	}
	
	private void sendDirection(int direction){
		// IF DIRECTION HAS CHANGED
		EntityMovable entity = network.getLocalUser().getControlledEntity();
		if (entity != null) entity.setDirection(direction);
	}
}
