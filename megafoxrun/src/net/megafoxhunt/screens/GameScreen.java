package net.megafoxhunt.screens;

import java.awt.List;
import java.util.Collection;

import net.megafoxhunt.core.GameInputProcessor;
import net.megafoxhunt.core.GameKeys;
import net.megafoxhunt.core.MyGdxGame;
import net.megafoxhunt.core.PlayerHandler;
import net.megafoxhunt.entities.AliveEntity;
import net.megafoxhunt.entities.EntityContainer;
import net.megafoxhunt.entities.StaticEntity;
import net.megafoxhunt.server.KryoNetwork.Move;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GameScreen implements Screen {

	public static final float UNIT_SCALE = 1 / 32f;
	
	private MyGdxGame game;
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private EntityContainer entityContainer;
	
	public GameScreen(final MyGdxGame game, EntityContainer entityContainer) {
		this.game = game;
		this.entityContainer = entityContainer;
		
		map = new TmxMapLoader().load("data/testmap.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, UNIT_SCALE);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 30, 20);
		camera.update();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        // TODO: (process input, collision detection, position update)
        AliveEntity myEntity = PlayerHandler.getPlayerEntity();
        if (myEntity != null) {
        	int direction = -1;
        	if (GameKeys.isPressed(GameKeys.UP)) {
        		direction = AliveEntity.DIRECTION_UP;
        	} else if (GameKeys.isPressed(GameKeys.RIGHT)) {
        		direction = AliveEntity.DIRECTION_RIGHT;
        	} else if (GameKeys.isPressed(GameKeys.DOWN)) {
        		direction = AliveEntity.DIRECTION_DOWN;
        	} else if (GameKeys.isPressed(GameKeys.LEFT)) {
        		direction = AliveEntity.DIRECTION_LEFT;
        	}
        	
        	if (direction != -1) {
        		myEntity.setDirection(direction);
        		
        		Move move = new Move(myEntity.getId(), direction, (int)myEntity.getX(), (int)myEntity.getY());
        		MyGdxGame.getNetwork().getClient().sendTCP(move);
        	}
        }
		
		
        // TODO: SET CAMERA POSITION TO FOLLOW TARGET
        camera.update();
        
        renderer.setView(camera);
        renderer.render();
        
        Collection<StaticEntity> entities = entityContainer.getValues();
        
        for (StaticEntity entity : entities) {
        	entity.update(delta);
        }
        
        Batch batch = renderer.getSpriteBatch();
        batch.begin();
        for (StaticEntity entity : entities) {
        	entity.render(batch);
        }
        batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {		
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

}
