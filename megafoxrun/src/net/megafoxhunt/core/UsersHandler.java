package net.megafoxhunt.core;

import java.util.ArrayList;

public class UsersHandler {
	
	// User himself
	private static User myUser;

	// Other users
	private static ArrayList<User> users = new ArrayList<User>();
	
	public static ArrayList<User> getUsers() {
		return (ArrayList<User>)users.clone();
	}
	
	public static void setMyUser(User user) {
		myUser = user;
	}
	
	public static User getMyUser() {
		return myUser;
	}
	
	public static void addUser(User user) {
		users.add(user);
	}
	
	public static void removeUser(User user) {
		users.remove(user);
	}
	
	public static void removeUserById(int id) {
		for(User user : users) {
			if (user.getId() == id) {
				users.remove(user);
				break;
			}
		}
	}
}
