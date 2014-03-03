package net.megafoxhunt.server;
import java.util.Scanner;

public class ServerDebugInput extends Thread{

	private GameServer server;
	
	private boolean running;
	
	public ServerDebugInput(GameServer server) {
		this.server = server;
		running = true;
	}
	
	@Override
	public void run() {
		System.out.println("Type help to get list of commands");
		Scanner scanner = new Scanner(System.in);			
		while (running) {
			System.out.println("\nNext command: ");
			newCommand(scanner.nextLine());
		}		
		scanner.close();
	}
	
	private void printCommands() {
		System.out.println("'(i)nfo' Show server information");
		System.out.println("'(s)tress' Stress test server");
		/*
		System.out.println("r/run: starts all rooms");
		System.out.println("rooms.size: prints number of rooms active");
		System.out.println("start_room 2: starts room at index 2 on list");
		System.out.println("room_players 2: show players in room 2");
		*/
	}
	private void debugStressTest() {
		int rooms = 1000;
		for(int a = rooms; a > 0; a--){
			server.roomHandler.createNewRoom();		
		}
	}
	private void newCommand(String command) {	
		String trimmedCommand = command.trim();
		if(trimmedCommand.equals("info") || trimmedCommand.equals("i")){
			server.printInfo();
		}
		else if (trimmedCommand.equals("stress") || trimmedCommand.equals("s")) {
			debugStressTest();
		}
		else if (trimmedCommand.equals("help")) {
			printCommands();
		}
		/*
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
		*/
	}
	
	public void shutdown() {
		running = false;
	}
}
