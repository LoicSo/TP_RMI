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
	IChatRoom stub;

	public Client() {
		sc = new Scanner(System.in);
	}

	private void run() {

		try {
			System.out.println("What is your Participant name?");
			String namePart = sc.nextLine();
			participant = new Participant(namePart, this);

			reg = LocateRegistry.getRegistry(1099);

			stub = connect();
			System.out.println("Connected to " + stub.name());

		} catch (RemoteException e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
			System.exit(-1);

		}

		t = new TerminatedThread(participant, stub);
		Runtime.getRuntime().addShutdownHook(t);

		chat();
	}

	private void chat() {

		String msg = null;
		while (sc.hasNext()) {
			try {
				msg = sc.nextLine();

				if (stub != null) {
					switch (msg) {
					case "who":
						print_participant();
						break;
					case "leave":
						leave_room();
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

	private void leave_room() throws RemoteException {
		stub.leave(participant);
		stub = null;
		t.changeRoom(stub);
	}

	public void forcedDisconnection() {
		System.err.println("The server shutdown, you were forcibly disconnected");
		stub = null;
		t.changeRoom(stub);
	}

	private void print_participant() throws RemoteException {
		String[] listP = stub.who();
		for (int i = 0; i < listP.length; i++) {
			System.out.println(listP[i]);
		}
	}

	private IChatRoom connect() {

		boolean roomCorr = false;
		IChatRoom room = null;

		while (!roomCorr) {
			System.out.println(
					"To which ChatRoom do you want to connect? \n Type 'list' to obtain the list of room's name");
			String nameChat = sc.nextLine();

			try {
				if (nameChat.equals("list")) {
					print_room();
				} else {
					room = (IChatRoom) reg.lookup(nameChat);
					room.connect(participant);

					if (t != null)
						t.changeRoom(stub);

					roomCorr = true;
				}
			} catch (RemoteException e) {
				System.err.println("The server will restart in a short time, thanks for your patience");
				break;
			} catch (NotBoundException e) {
				System.err.println("This room does not exist");
			}
		}

		return room;

	}

	private void print_room() throws RemoteException {
		String[] roomList;

		roomList = reg.list();
		for (int i = 0; i < roomList.length; i++) {
			System.out.println(roomList[i]);
		}

	}

	public static void main(String[] args) {
		Client client = new Client();
		client.run();
	}

}
