package chatServer;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {

	IParticipant participant;
	Scanner sc;
	Registry reg;
	TerminatedThread t;
	
	public Client() {
		sc = new Scanner(System.in);
	}
	
	private void run() {
		
		String nameChat = null;
		IChatRoom stub = null;
		try {
			System.out.println("What is your Participant name?");
			String namePart = sc.nextLine();
			participant = new Participant(namePart);
			
			reg = LocateRegistry.getRegistry(1099);
			reg.bind(participant.name(), participant);
			
			System.out.println("To which ChatRoom do you want to connect? (chatRoom + nb)");
			nameChat = sc.nextLine();
			
			stub = (IChatRoom) reg.lookup(nameChat);
			stub.connect(participant);
			
		} catch (RemoteException e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
			System.exit(-1);
			
		} catch (NotBoundException e) {
			System.err.println("This chatRoom does not exist");
			System.exit(-1);
			
		} catch (AlreadyBoundException e) {
			System.err.println("Participant created twice");
			System.exit(-1);
		}
		
		System.out.println("Connected to " + nameChat);
		
		t = new TerminatedThread(participant, stub, reg);
		Runtime.getRuntime().addShutdownHook(t);
		
		chat(stub);
	}
	
	private void chat(IChatRoom stub) {
		
		String msg = null;
		while(sc.hasNext()) {
			try {
				msg = sc.nextLine();
				stub.send(participant, msg);
			} catch (RemoteException e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

}
