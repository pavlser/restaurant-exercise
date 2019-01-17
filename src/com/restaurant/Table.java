package com.restaurant;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Table {

	private final int size; // number of chairs
	
	private List<ClientsGroup> clientGroups;

	public Table(int size) {
		this.size = size;
		this.clientGroups = new LinkedList<ClientsGroup>();
	}
	
	public int getSize() {
		return this.size;
	}
	
	public boolean isEmpty() {
		return this.clientGroups.size() == 0;
	}
	
	public boolean hasFreeSpaceForGroupSize(int groupSize) {
		int totalSit = clientGroups.stream().mapToInt(g -> g.getSize()).sum();
		return this.size - totalSit >= groupSize;
	}
	
	public void sitGroup(ClientsGroup group) {
		this.clientGroups.add(group);
	}
	
	public void removeGroup(ClientsGroup group) {
		this.clientGroups.remove(group);
	}
	
	public boolean hasGroup(ClientsGroup group) {
		return this.clientGroups.contains(group);
	}
	
	@Override
	public String toString() {
		return "Size " + size + (isEmpty() ? " (free)" : " (busy " + listGroups() + ")");
	}
	
	String listGroups() {
		return clientGroups.stream()
			.map(ClientsGroup::getSize)
			.map(String::valueOf)
			.collect(Collectors.joining("+"));
	}

}
