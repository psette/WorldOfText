package main;

import client.FactoryClient;
import server.FactoryServer;

public class Main {
	public static void main(String[] args) {
		new FactoryServer();
		new FactoryClient();
	}
}
