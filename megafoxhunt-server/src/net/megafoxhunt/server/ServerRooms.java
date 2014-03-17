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
		GameRoom room = new GameRoom(this);	
		rooms.add(room);			
		return room;
	}
	public synchronized void removeRoom(GameRoom room){	
		room.removePlayers();
		rooms.remove(room);
	}
	public void switchToLobby(GameRoom room){
		room.endMatch();
		room.switchState(GameRoom.STATE_LOBBY);
	}
	public void startGame(GameRoom room){	
		if(room.getRoomState() == GameRoom.STATE_LOBBY){
			room.changeMap(GameMapSharedConfig.DEBUG_MAP);	
			room.startSimulation();		
			room.setChasedsAndChasers();
			room.generateBerries(GameMapServerSide.TOTAL_BERRIES, gameServer.idHandler);
			room.generateHoles(GameMapServerSide.TOTAL_HOLES, gameServer.idHandler);
			room.switchState(GameRoom.STATE_GAME);
			room.startClock(GameRoom.MATCH_LENGHT_SECONDS_DEFAULT);
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<GameRoom> getAllRoomsConcurrentSafe() {
		return (ArrayList<GameRoom>) rooms.clone();
	}
	public void printInfo() {
		System.out.println("Rooms: " + rooms.size());		
	}
}
