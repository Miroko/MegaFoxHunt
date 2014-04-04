package net.megafox.items;

import java.util.TimerTask;

import net.megafox.entities.Empty;
import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.Shared;
import net.megafoxhunt.shared.KryoNetwork.ChangeTilesTypes;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;

public class Barricade extends Item {	

	public Barricade(GameSimulation gameSimulation) {
		super(gameSimulation);
	}

	@Override
	public void activate(int x, int y, PlayerConnection player) {		
		int targetX = x;
		int targetY = y;
		
		int direction = player.getEntity().getFacingDirection();
		if (direction == Shared.DIRECTION_RIGHT) targetX++;
		else if (direction == Shared.DIRECTION_LEFT) targetX--;
		else if (direction == Shared.DIRECTION_UP) targetY++;
		else if (direction == Shared.DIRECTION_DOWN) targetY--;
		
		place(targetX, targetY, player);
	}
	private void place(int x, int y, PlayerConnection player){		
		if (gameSimulation.gameMap.getEntity(x, y) instanceof Empty) {
			ChangeTilesTypes changeTilesTypes = new ChangeTilesTypes();
			changeTilesTypes.addTile(x, y, 20);
			
			net.megafox.entities.Barricade barricade = new net.megafox.entities.Barricade(x, y, gameSimulation.idHandler.getFreeID());
			gameSimulation.gameMap.setWall(x, y);
			gameSimulation.playerContainer.sendObjectToAll(new Move(player.getMyId(), 0, x, y, true));
			gameSimulation.playerContainer.sendObjectToAll(changeTilesTypes);
			gameSimulation.timer.schedule(new RemoveBarricadeTimerTask(barricade), RemoveBarricadeTimerTask.REMOVE_DELAY);
		}
	}
	class RemoveBarricadeTimerTask extends TimerTask {
		
		private static final int REMOVE_DELAY = 5000;

		private net.megafox.entities.Barricade barricade;
		
		public RemoveBarricadeTimerTask(net.megafox.entities.Barricade barricade) {
			this.barricade = barricade;
		}
		@Override
		public void run() {
			gameSimulation.gameMap.setEmpty(barricade.getX(), barricade.getY());
			RemoveEntity removeEntity = new RemoveEntity();
			removeEntity.id = barricade.getId();
			gameSimulation.idHandler.freeID(barricade.getId());
			gameSimulation.playerContainer.sendObjectToAll(removeEntity);
			
			ChangeTilesTypes changeTilesTypes = new ChangeTilesTypes();
			changeTilesTypes.addTile(barricade.getX(), barricade.getY(), 13);
			gameSimulation.playerContainer.sendObjectToAll(changeTilesTypes);				
		}
	}
}
