package net.megafoxhunt.server;

import java.util.ArrayList;
import java.util.Scanner;

import net.megafox.gameroom.GameRoom;


public class ServerDebugInput extends Thread{

	private GameServer server;
	
	private boolean running;
	
	public ServerDebugInput(GameServer server) {
		this.server = server;
		running = true;
	}
	
	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("type help to get list of commands");
		while (running) {
			System.out.println("Next command: ");
			newCommand(scanner.nextLine());
		}
		
		scanner.close();
	}
	
	private void printCommands() {
		System.out.println("r/run: starts all rooms");
		System.out.println("rooms.size: prints number of rooms active");
		System.out.println("start_room 2: starts room at index 2 on list");
		System.out.println("room_players 2: show players in room 2");
	}
	
	private void newCommand(String command) {
		ArrayList<GameRoom> rooms = server.getRoomHandler().getAllRoomsConcurrentSafe();
		String[] commands = command.split(" ");
		
		if (commands.length <= 0) return;
		
		if (commands[0].equals("run") ||
			commands[0].equals("r")) {
			for (GameRoom room : rooms) {
				room.startGame();
			}
		} else if (commands[0].equals("rooms.size")) {
			System.out.println(rooms.size());
		} else if (commands[0].equals("start_room")) {
			try {
				int id = Integer.parseInt(commands[1]);
				
				if (id >= 0 && id < rooms.size()) {
					rooms.get(id).startGame();
					System.out.println("room: " + id + " started.");
				}
			} catch (NumberFormatException ex) {
				
			}
		} else if (commands[0].equals("room_players")) {
			try {
				int id = Integer.parseInt(commands[1]);
				
				if (id >= 0 && id < rooms.size()) {
					ArrayList<PlayerConnection> players = rooms.get(id).getPlayerContainer().getPlayersConcurrentSafe();
					System.out.println("Room " + id + " has " + players.size() + " players.");
					for (int i = 0; i < players.size(); i++) {
						System.out.println(players.get(i).getName() + ": con id" + players.get(i).getMyId() + " list id: " + i);
					}
				}
			} catch (NumberFormatException ex) {
				
			}
		} else if (commands[0].equals("help")) {
			printCommands();
		}
	}
	
	public void shutdown() {
		running = false;
	}
}
