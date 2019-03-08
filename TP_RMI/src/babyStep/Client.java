package babyStep;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
	
	public static void main(String[] args) {
		
		try {
			Registry registry = LocateRegistry.getRegistry(1099);
			IPrinter stub = (IPrinter) registry.lookup("printer");
			stub.printLine("Ca marche bien !!!");
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

}
