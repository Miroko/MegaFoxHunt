package net.megafoxhunt.core;

import java.util.ArrayList;

public class UserContainer {
	
	private static ArrayList<User> USERS = new ArrayList<User>();
	
	public static int numberOfUsers(){
		return getUsers().size();
	}
	public static ArrayList<User> getUsers() {
		return (ArrayList<User>)USERS.clone();
	}	
	public static void addUser(User user) {
		USERS.add(user);
	}	
	public static void removeUser(User user) {
		USERS.remove(user);
	}
	
	public static void removeUserById(int id) {
		for(User user : USERS) {
			if (user.getId() == id) {
				USERS.remove(user);
				break;
			}
		}
	}
}
