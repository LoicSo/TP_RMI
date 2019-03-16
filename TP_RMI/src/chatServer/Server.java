package chatServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.Scanner;

public class Server {
	
	IChatRoom room;
	Scanner sc;
	
	public Server() {
		sc = new Scanner(System.in);
	}
	
	void run() {
		
		Registry reg = null;
		try {	
			reg = LocateRegistry.createRegistry(1099);			
		} catch (ExportException e) {
			try {
				reg = LocateRegistry.getRegistry(1099);
			} catch (RemoteException e1) {
				System.err.println("Server exception: " + e.toString());
				e.printStackTrace();
				System.exit(-1);
			}
		} catch (RemoteException e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
			System.exit(-1);
		}
		
		try {
			create_room(reg);
		} catch (RemoteException e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
			System.exit(-1);
		}

		System.err.println("Server ready");
	}

	private void create_room(Registry reg) throws RemoteException {
		
		boolean roomCorr = false;
		
		while(!roomCorr) {
			System.out.println("What is the room's name?");
			String name = sc.nextLine();
			room = new ChatRoom(name);
			
			try {
				reg.bind(name, room);
				roomCorr = true;
			} catch (AlreadyBoundException e) {
				System.err.println("This name is already used choose an other one");
			}
		}

		
	}

	public static void main(String[] args) {
		Server serv = new Server();
		serv.run();
	}

}
