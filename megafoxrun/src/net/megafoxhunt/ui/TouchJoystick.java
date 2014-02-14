package net.megafoxhunt.ui;

import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.entities.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TouchJoystick {

	private static final int CIRCLE_SIZE = 200;
	private static final int JOYSTICK_SIZE = 50;
	
	private Vector2 circlePos;
	private Vector2 joystickPos;
	
	private Texture circle;
	private Texture joystick;
	
	public TouchJoystick() {
		circlePos = new Vector2(200, 200);
		joystickPos = new Vector2();
		
		circle = new Texture(Gdx.files.internal("data/circle.png"));
		joystick = new Texture(Gdx.files.internal("data/joystick.png"));
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(circle, circlePos.x - (CIRCLE_SIZE / 2), circlePos.y - (CIRCLE_SIZE / 2), CIRCLE_SIZE, CIRCLE_SIZE);
		batch.draw(joystick, (joystickPos.x + circlePos.x) - (JOYSTICK_SIZE / 2), (joystickPos.y + circlePos.y) - (JOYSTICK_SIZE / 2), JOYSTICK_SIZE, JOYSTICK_SIZE);
	}
	
	public void mouseDown(int x, int y) {
		y = Gdx.graphics.getHeight() - y;

		joystickPos.x = x - circlePos.x;
		joystickPos.y = y - circlePos.y;
		
		joystickPos.clamp(0, 55);
		
		float distance = circlePos.dst(x, y);
		
		float angle = joystickPos.angle();
		if (angle < 45 && angle > 0 || angle < 360 && angle > 315) {
			sendDirection(Entity.DIRECTION_RIGHT);
		} else if (angle > 45 && angle < 135) {
			sendDirection(Entity.DIRECTION_UP);
		} else if (angle > 135 && angle < 225) {
			sendDirection(Entity.DIRECTION_LEFT);
		} else if (angle > 225 && angle < 315) {
			sendDirection(Entity.DIRECTION_DOWN);
		}
	}
	
	public void mouseUp(int x, int y) {
		joystickPos.x = 0;
		joystickPos.y = 0;
		
		sendDirection(Entity.DIRECTION_STOP);
	}
	
	private void sendDirection(int direction){
		// IF DIRECTION HAS CHANGED
		Entity entity = GameNetwork.getUser().getControlledEntity();
		entity.setDirection(direction);
	}
}
