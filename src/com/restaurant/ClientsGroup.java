package com.restaurant;

public class ClientsGroup {
	
	private final int size; // number of clients
	
	public ClientsGroup(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return this.size;
	}
	
	@Override
	public String toString() {
		return "Group size: " + size;
	}

}
