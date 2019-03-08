package babyStep;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {

	IPrinter printer;
	
	public Server() {
		printer = new Printer();
	}
	
	private void run() {
		try {
			IPrinter stub = (IPrinter) UnicastRemoteObject.exportObject(printer, 0);
			
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("printer", stub);
			
			System.err.println("Server ready");
			
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.run();
	}

}
