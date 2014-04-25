package net.megafox.entities;
import java.awt.Point;
import java.util.ArrayList;
import java.util.TimerTask;

import net.megafox.game.GameSimulation;
import net.megafox.gameroom.PlayerContainer;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.Shared;
import net.megafoxhunt.shared.KryoNetwork.AddChaser;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;

public class Chaser extends Entity{
	
	public static final long RESPAWN_DELAY_MS = 4000;
	
	private boolean isEated = false;

	public Chaser(int x, int y, int id, PlayerConnection player) {
		super(x, y, id, Visibility.BOTH, player);
	}

	public boolean isEaten() {
		return isEated;
	}

	public void collisionWithChased(GameSimulation gameSimulation) {
		this.isEated = true;		
		getPlayer().removeItem();
		
		RemoveEntity removeEntity = new RemoveEntity();
		removeEntity.id = getId();	
		gameSimulation.playerContainer.sendObjectToAll(removeEntity);
		
		class RespawnChaserTask extends TimerTask{			
			
			private Chaser chaser;
			private GameSimulation simulation;

			public RespawnChaserTask(GameSimulation gameSimulation, Chaser chaser){
				this.chaser = chaser;
				this.simulation = gameSimulation;
			}	
			@Override
			public void run() {				
				chaser.isEated = false;
				simulation.reSpawnChaser(chaser);			
			}
			
		}
		gameSimulation.timer.schedule(new RespawnChaserTask(gameSimulation, this), RESPAWN_DELAY_MS);		
	}

	public void respawn(Point p, GameSimulation gameSimulation) {
		gameSimulation.move(this, p.x, p.y, Shared.DIRECTION_STOP, true);			

		AddChaser addChaser = new AddChaser();
		addChaser.id = getId();
		addChaser.x = getX();
		addChaser.y = getY();
		gameSimulation.playerContainer.sendObjectToAll(addChaser);
		
		/*
		move(chaser, p.x, p.y, Shared.DIRECTION_STOP, true);		
		playerContainer.sendObjectToAll(new Move(chaser.getId(), Shared.DIRECTION_STOP, chaser.getX(), chaser.getY(), true), Visibility.BOTH);
		*/		
	}

}
