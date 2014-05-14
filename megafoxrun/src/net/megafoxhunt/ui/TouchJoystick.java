package net.megafoxhunt.ui;

import net.megafoxhunt.core.GameNetwork;
import net.megafoxhunt.core.GameResources;
import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.entities.EntityMovable;
import net.megafoxhunt.shared.Shared;
import net.megafoxhunt.shared.KryoNetwork.ActivateItem;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TouchJoystick {

	private static final int CIRCLE_SIZE = 50;
	
	private static final int WIDTH = 64;
	private static final int HEIGHT = 64;
	private static final int SPACING = WIDTH / 3;
	
	private static final int BTN1_X = Gdx.graphics.getWidth() - WIDTH - SPACING;
	private static final int BTN1_Y = 100;
	
	private Vector2 circlePos;
	private Vector2 joystickPos;
	
	private Vector2 center;
	
	private Sprite joystick;
	
	private int actionBtnPressed = 0;
	
	private GameNetwork network;
	
	public TouchJoystick(GameNetwork network) {
		this.network = network;
		
		center = new Vector2(120, 120);
		circlePos = new Vector2(0, 0);
		
		joystickPos = new Vector2();
		joystick = new Sprite(MyGdxGame.resources.joystick);
		joystick.setOrigin(joystick.getWidth() / 2, 0);
		joystick.setX(center.x - joystick.getWidth() / 2);
		joystick.setY(center.y);
		joystick.setScale(1, 0);
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
		
		if (Gdx.app.getType() == ApplicationType.Android) {
			//batch.draw(MyGdxGame.resources.circle, circlePos.x - (MyGdxGame.resources.circle.getWidth() / 2), circlePos.y - (MyGdxGame.resources.circle.getHeight() / 2), MyGdxGame.resources.circle.getWidth(), MyGdxGame.resources.circle.getHeight());
			joystick.draw(batch, 1);
			batch.draw(MyGdxGame.resources.circle, (circlePos.x + center.x) - (CIRCLE_SIZE / 2), (circlePos.y + center.y) - (CIRCLE_SIZE / 2), CIRCLE_SIZE, CIRCLE_SIZE);
		}
	}
	
	public void mouseDown(int x, int y) {
		if (Gdx.app.getType() != ApplicationType.Android) return;
		
		y = Gdx.graphics.getHeight() - y;

		circlePos.x = x - center.x;
		circlePos.y = y - center.y;
		circlePos.clamp(0, 100);
		
		float deltaY = x - center.y;
		float deltaX = y - center.x;
		
		double angleInDegrees = Math.atan2(deltaY, deltaX) * 180 / Math.PI;
		if (angleInDegrees < 0) angleInDegrees = 360 + angleInDegrees;
		
		angleInDegrees = 360 - angleInDegrees;
		joystick.setRotation((float)angleInDegrees);
		joystick.setScale(1, (circlePos.len() / 100));
		
		double distance = getDistance((int)circlePos.x, (int)circlePos.y, x, y);
		
		if (distance > Gdx.graphics.getWidth() / 2) {
			circlePos.x = 0;
			circlePos.y = 0;
			joystick.setScale(1, 0);
			return;
		}
		
		float angle = circlePos.angle();
		if (angle < 45 && angle > 0 || angle < 360 && angle > 315) {
			sendDirection(Shared.DIRECTION_RIGHT);
		} else if (angle > 45 && angle < 135) {
			sendDirection(Shared.DIRECTION_UP);
		} else if (angle > 135 && angle < 225) {
			sendDirection(Shared.DIRECTION_LEFT);
		} else if (angle > 225 && angle < 315) {
			sendDirection(Shared.DIRECTION_DOWN);
		}
	}
	
	public void mouseUp(int x, int y) {
		y = Gdx.graphics.getHeight() - y;
		
		if (getDistance(BTN1_X, BTN1_Y, x, y) < WIDTH) {
			network.getKryoClient().sendTCP(new ActivateItem());
		}
		actionBtnPressed = 0;
		
		joystickPos.x = 0;
		joystickPos.y = 0;
	}
	
	private double getDistance(int centerX, int centerY, int x, int y) {
	    double dist = Math.sqrt((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y));
	    return dist;
	}
	
	private void sendDirection(int direction){
		// IF DIRECTION HAS CHANGED
		EntityMovable entity = network.getLocalUser().getControlledEntity();
		if (entity == null) return;
		entity.setDirection(direction);
	}
}