package net.megafox.items;

import java.util.TimerTask;

import net.megafox.entities.Empty;
import net.megafox.game.GameMapServerSide;
import net.megafox.game.GameSimulation;
import net.megafoxhunt.server.PlayerConnection;
import net.megafoxhunt.shared.KryoNetwork.AddBomb;
import net.megafoxhunt.shared.KryoNetwork.ChangeTilesTypes;
import net.megafoxhunt.shared.KryoNetwork.Move;
import net.megafoxhunt.shared.KryoNetwork.RemoveEntity;

public class Bomb extends Item{
	
	private int x;
	private int y;

	public Bomb(GameSimulation gameSimulation) {
		super(gameSimulation);
	}
	public void explode(){		
		ChangeTilesTypes changeTilesTypes = new ChangeTilesTypes();
		
		GameMapServerSide gameMap = gameSimulation.gameMap;
		
		if (gameMap.isBlocked(x - 1, y)) {
			gameMap.setEmpty(x - 1, y);
			changeTilesTypes.addTile(x - 1, y, 13);
		}
		if (gameMap.isBlocked(x + 1, y)) {
			gameMap.setEmpty(x + 1, y);
			changeTilesTypes.addTile(x + 1, y, 13);
		}
		if (gameMap.isBlocked(x, y - 1)) {
			gameMap.setEmpty(x, y - 1);
			changeTilesTypes.addTile(x, y - 1, 13);
		}
		if (gameMap.isBlocked(x, y + 1)) {
			gameMap.setEmpty(x, y + 1);
			changeTilesTypes.addTile(x, y + 1, 13);
		}
		
		if (!changeTilesTypes.getTiles().isEmpty()) {			
			gameSimulation.playerContainer.sendObjectToAll(changeTilesTypes);
		}
	}
	@Override
	public void activate(int x, int y, PlayerConnection player) {		
		this.x = x;
		this.y = y;
		plant(x, y, player);
	}
	public void plant(int x, int y, PlayerConnection player){		
		if (gameSimulation.gameMap.getEntity(x, y) instanceof Empty) {			
			net.megafox.entities.Bomb bomb = new net.megafox.entities.Bomb(x, y, gameSimulation.idHandler.getFreeID());
			gameSimulation.playerContainer.sendObjectToAll(new AddBomb(bomb.getId(), bomb.getX(), bomb.getY()));				
			gameSimulation.timer.schedule(new BombExplodeTimerTask(bomb), BombExplodeTimerTask.EXPLODE_DELAY);
		}
	}
	class BombExplodeTimerTask extends TimerTask {
		
		private static final int EXPLODE_DELAY = 5000;

		private net.megafox.entities.Bomb bomb;
		
		public BombExplodeTimerTask(net.megafox.entities.Bomb bomb) {
			this.bomb = bomb;
		}
		@Override
		public void run() {
			explode();

			RemoveEntity removeEntity = new RemoveEntity();
			removeEntity.id = bomb.getId();
			gameSimulation.idHandler.freeID(bomb.getId());
			gameSimulation.playerContainer.sendObjectToAll(removeEntity);
		}
	}
}
