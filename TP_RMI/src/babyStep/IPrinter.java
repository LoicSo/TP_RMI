package babyStep;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrinter extends Remote {

	void printLine (String line) throws RemoteException;
}
