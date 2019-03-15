package chatServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class TerminatedThread extends Thread {

	private IChatRoom stub;
	private Registry reg;
	private IParticipant participant;

	public TerminatedThread(IParticipant participant, IChatRoom stub, Registry reg) {
		this.participant = participant;
		this.stub = stub;
		this.reg = reg;
				
	}
	
	public void run() {
		try {
			stub.leave(participant);
			reg.unbind(participant.name());
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	public void changeRoom(IChatRoom stub) {
		this.stub = stub;
	}

}
