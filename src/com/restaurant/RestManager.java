package com.restaurant;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RestManager {

	private List<Table> tables;
	private List<ClientsGroup> waitingGroups;

	public RestManager(List<Table> tables) {
		if (tables == null) {
			throw new RuntimeException("Tables can't be null");
		}
		this.tables = new ArrayList<>(tables);
		this.tables.sort((a, b) -> a.getSize() - b.getSize());
		this.waitingGroups = new LinkedList<>();
	}

	// new client(s) show up
	synchronized public void onArrive(ClientsGroup group) throws Exception {
		if (lookup(group) == null) {
			Table table = findSuitableTableForGroup(group);
			if (table == null) {
				addGroupIntoWaitingQueue(group);
			} else {
				sitGroupAtTheTable(table, group);
			}
		} else {
			throw new Exception("Error: the group is already sit at a table");
		}
	}

	// client(s) leave, either served or simply abandoning the queue
	synchronized public void onLeave(ClientsGroup group) throws Exception {
		Table table = lookup(group);
		if (table != null) {
			table.setGroupOccupied(null);
			tryToSitNextGroup();
		} else {
			if (lookupWaitingGroup(group) != null) {
				waitingGroups.remove(group);
			}
		}
	}

	// return table where a given client group is seated,
	// or null if it is still queuing or has already left
	public Table lookup(ClientsGroup group) {
		Table res = null;
		for (Table table : tables) {
			if (table.getGroupOccupied() == group) {
				res = table;
				break;
			}
		}
		return res;
	}
	
	private ClientsGroup lookupWaitingGroup(ClientsGroup group) {
		ClientsGroup res = null;
		for (ClientsGroup g : waitingGroups) {
			if (g == group) {
				res = g;
				break;
			}
		}
		return res;
	}
	
	private void addGroupIntoWaitingQueue(ClientsGroup group) throws Exception {
		if (lookupWaitingGroup(group) != null) {
			throw new Exception("Can't add group into waiting: the group is already in the waiting queue");
		} else {
			this.waitingGroups.add(group);
		}
	}
	
	private Table findSuitableTableForGroup(ClientsGroup group) {
		Table res = null;
		for (Table table : tables) {
			if (table.isFree() && table.getSize() == group.getSize()) {
				res = table;
				break;
			}
		}
		return res;
	}
	
	private void sitGroupAtTheTable(Table table, ClientsGroup group) throws Exception {
		if (table.isFree()) {
			table.setGroupOccupied(group);
		} else {
			throw new Exception("Can't sit group at table: the table is occupied");
		}
	}
	
	private void tryToSitNextGroup() throws Exception {
		for (ClientsGroup group : waitingGroups) {
			Table table = findSuitableTableForGroup(group);
			if (table != null) {
				this.waitingGroups.remove(group);
				sitGroupAtTheTable(table, group);
				break;
			}
		}
	}
	
	protected void printStatus() {
		out("Tables: " + tables + ", Waiting: " + waitingGroups);
	}

	static void out(Object o) {
		System.out.println(o);
	}
	
	public static void main(String[] args) throws Exception {
		List<Table> tables = new ArrayList<Table>();
		tables.add(new Table(4));
		tables.add(new Table(6));
		tables.add(new Table(2));
		
		RestManager manager = new RestManager(tables);
		
		ClientsGroup group2_1 = new ClientsGroup(2);
		ClientsGroup group2_2 = new ClientsGroup(2);
		ClientsGroup group4_1 = new ClientsGroup(4);
		ClientsGroup group4_2 = new ClientsGroup(4);
		ClientsGroup group6_1 = new ClientsGroup(6);
		ClientsGroup group6_2 = new ClientsGroup(6);
		
		out("Init");
		manager.printStatus();
		
		out("\nArrive group 2(1): " + group2_1);
		manager.onArrive(group2_1);
		manager.printStatus();
		
		out("\nArrive group 2(2): " + group2_2);
		manager.onArrive(group2_2);
		manager.printStatus();
		
		out("\nArrive group 4(1): " + group4_1);
		manager.onArrive(group4_1);
		manager.printStatus();
		
		out("\nArrive group 4(2): " + group4_2);
		manager.onArrive(group4_2);
		manager.printStatus();
		
		out("\nArrive group 6(1): " + group6_1);
		manager.onArrive(group6_1);
		manager.printStatus();
		
		out("\nArrive group 6(2): " + group6_2);
		manager.onArrive(group6_2);
		manager.printStatus();
		
		out("\nLeave group 2(1): " + group2_1);
		manager.onLeave(group2_1);
		manager.printStatus();
		
		out("\nLeave group 4(2): " + group4_2);
		manager.onLeave(group4_2);
		manager.printStatus();
		
		out("\nLeave group 4(1): " + group4_1);
		manager.onLeave(group4_1);
		manager.printStatus();
		
		out("\nLeave group 6(1): " + group6_1);
		manager.onLeave(group6_1);
		manager.printStatus();
		
		out("\nLeave group 6(2): " + group6_2);
		manager.onLeave(group6_2);
		manager.printStatus();
	}
}
