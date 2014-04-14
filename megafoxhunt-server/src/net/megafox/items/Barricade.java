package net.megafox.items;

import java.util.TimerTask;

import net.megafox.entities.Empty;
import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.KryoNetwork.AddBarricade;
import net.megafoxhunt.shared.Shared;
import net.megafoxhunt.shared.KryoNetwork.ChangeTilesTypes;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;

public class Barricade extends Item {	

	public Barricade(GameSimulation gameSimulation) {
		super(gameSimulation);
	}

	@Override
	public boolean activate(int x, int y, PlayerConnection player) {		
		if (gameSimulation.gameMap.getEntity(x, y) instanceof Empty) {
			int id = gameSimulation.idHandler.getFreeID();
			AddBarricade addBarricade = new AddBarricade(id, x, y);
			
			net.megafox.entities.Barricade barricade = new net.megafox.entities.Barricade(x, y, id);
			gameSimulation.gameMap.setWall(x, y);
			gameSimulation.playerContainer.sendObjectToAll(addBarricade);
			gameSimulation.timer.schedule(new RemoveBarricadeTimerTask(barricade), RemoveBarricadeTimerTask.REMOVE_DELAY);
			return true;
		}
		return false;
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
		}
	}
}
