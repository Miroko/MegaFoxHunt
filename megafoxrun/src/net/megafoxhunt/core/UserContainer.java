package net.megafoxhunt.core;

import java.util.ArrayList;

public class UserContainer {
	private User user = new User(0, null);
	private ArrayList<User> users = new ArrayList<User>();
	
	public User getUser(){
		return user;
	}
	public ArrayList<User> getUsers() {
		return (ArrayList<User>)users.clone();
	}	
	public void addUser(User user) {
		users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
	}
	
	public void removeUserById(int id) {
		for(User user : users) {
			if (user.getId() == id) {
				users.remove(user);
				break;
			}
		}
	}
}
