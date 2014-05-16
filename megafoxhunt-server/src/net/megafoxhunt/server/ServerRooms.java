package net.megafoxhunt.server;

import java.util.ArrayList;
import net.megafox.game.GameMapServerSide;
import net.megafox.gameroom.GameRoom;
import net.megafoxhunt.shared.GameMapSharedConfig;

public class ServerRooms {

	private ArrayList<GameRoom> rooms;	
	
	private GameServer gameServer;
		
	public ServerRooms(GameServer gameServer) {
		this.gameServer = gameServer;
		rooms = new ArrayList<GameRoom>();		
	}	
	/**
	 * Add player to room on server
	 * @param playerConnection
	 * @return room player was added
	 */
	public synchronized GameRoom addPlayer(PlayerConnection playerConnection){			
		GameRoom roomAddedTo = null;
		for(GameRoom room : rooms) {
			if(room.getRoomState() == GameRoom.STATE_LOBBY){
				if(room.hasRoom()){				
					room.addPlayerToRoom(playerConnection);				
					roomAddedTo = room;					
					break;
				}
			}
		}	
		return roomAddedTo;		
	}
	public synchronized GameRoom createNewRoom(){
		System.out.println();
		System.out.println("creating new room");
		GameRoom room = new GameRoom(this);	
		System.out.println("New room created: " + room.toString());
		rooms.add(room);			
		return room;
	}
	public synchronized void removeRoom(GameRoom room){
		System.out.println("Room removed: " + room.toString());
		room.removePlayers();
		rooms.remove(room);
	}
	public void switchToLobby(GameRoom room){
		room.endMatch();
		room.switchState(GameRoom.STATE_LOBBY);
	}
	public void startGame(GameRoom room){	
		if(room.getRoomState() == GameRoom.STATE_LOBBY){
			room.changeMap(GameMapSharedConfig.MAP2, gameServer.idHandler);	
			room.startSimulation(gameServer.idHandler);		
			room.setChasedsAndChasers();
			room.generateBerries(GameMapServerSide.TOTAL_BERRIES, gameServer.idHandler);
			room.addInitialPowerups(GameMapSharedConfig.MAP1.getMaxPowerups(), gameServer.idHandler);
			room.addInitialPickupItems(GameMapSharedConfig.MAP1.getMaxPickupItems(), gameServer.idHandler);
			room.switchState(GameRoom.STATE_GAME);
			room.startClock(GameRoom.MATCH_LENGHT_SECONDS_DEFAULT);
			room.startRespawner(5000, gameServer.idHandler);
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<GameRoom> getAllRoomsConcurrentSafe() {
		return (ArrayList<GameRoom>) rooms.clone();
	}
	public void printInfo() {
		System.out.println("Rooms: " + rooms.size());		
	}
	
	public void printRooms() {
		System.out.println();
		printInfo();
		for (GameRoom room : rooms) {
			System.out.println("-");
			System.out.println(room.toString() + " has " + room.getPlayerContainer().getPlayersConcurrentSafe().size() + " players.");
			System.out.println("-");
		}
		System.out.println();
	}
}
