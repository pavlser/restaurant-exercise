package com.restaurant;

public class Table {

	private final int size; // number of chairs
	
	private ClientsGroup groupOccupied;

	public Table(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public boolean isFree() {
		return groupOccupied == null;
	}
	
	public void setGroupOccupied(ClientsGroup group) {
		this.groupOccupied = group;
	}
	
	public ClientsGroup getGroupOccupied() {
		return groupOccupied;
	}
	
	@Override
	public String toString() {
		return "Size " + size + (isFree()? " (free)" : " (busy)");
	}

}
