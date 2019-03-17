package chatServer;

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

	private void run(String[] args) {

		IChatRoom stub = null;

		try {
			System.out.println("What is your Participant name?");
			String namePart = sc.nextLine();
			participant = new Participant(namePart);

			reg = LocateRegistry.getRegistry(args[0],1099);

			stub = connect();
			System.out.println("Connected to " + stub.name());

		} catch (RemoteException e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
			System.exit(-1);

		}

		t = new TerminatedThread(participant, stub, reg);
		Runtime.getRuntime().addShutdownHook(t);

		chat(stub);
	}

	private void chat(IChatRoom stub) {

		String msg = null;
		while (sc.hasNext()) {
			try {
				msg = sc.nextLine();

				if (stub != null) {
					switch (msg) {
					case "who":
						print_participant(stub);
						break;
					case "leave":
						leave_room(stub);
						stub = null;
						break;
					case "connect":
						System.out.println("You must leave your current room before connect to an other");
						break;
					default:
						stub.send(participant, msg);
						break;
					}
				} else {
					switch (msg) {
					case "connect":
						stub = connect();
						break;
					default:
						System.out.println("You must be connected to room for chatting");
						break;
					}
				}

			} catch (RemoteException e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
				System.exit(-1);

			}
		}
	}

	private void leave_room(IChatRoom stub) throws RemoteException {
		stub.leave(participant);
	}

	private void print_participant(IChatRoom stub) throws RemoteException {
		String[] listP = stub.who();
		for (int i = 0; i < listP.length; i++) {
			System.out.println(listP[i]);
		}
	}

	private IChatRoom connect() {

		boolean roomCorr = false;
		IChatRoom stub = null;

		while (!roomCorr) {
			System.out.println("To which ChatRoom do you want to connect? \n Type 'list' to obtain the list of room's name");
			String nameChat = sc.nextLine();

			try {
				if (nameChat.equals("list")) {
					print_room();
				} else {
					stub = (IChatRoom) reg.lookup(nameChat);
					stub.connect(participant);

					if (t != null)
						t.changeRoom(stub);

					roomCorr = true;
				}
			} catch (RemoteException e) {
				System.err.println("Client exception: " + e.toString());
				e.printStackTrace();
				System.exit(-1);
			} catch (NotBoundException e) {
				System.err.println("This room does not exist");
			}
		}

		return stub;

	}

	private void print_room() throws RemoteException {
		String[] roomList = reg.list();
		for (int i = 0; i < roomList.length; i++) {
			System.out.println(roomList[i]);
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.run(args);
	}

}
