package chatServer;

import java.rmi.RemoteException;

public class TerminatedThread extends Thread {

	private IChatRoom stub;
	private IParticipant participant;

	public TerminatedThread(IParticipant participant, IChatRoom stub) {
		this.participant = participant;
		this.stub = stub;
	}
	
	public void run() {
		try {
			stub.leave(participant);
		} catch (RemoteException e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public void changeRoom(IChatRoom stub) {
		this.stub = stub;
	}

}
