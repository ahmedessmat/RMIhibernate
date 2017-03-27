package interfaces;

import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import model.Sprite;

/**
 *	Interface represents SpriteServer remote functionality;
 */
public interface SpriteServerInterface extends Remote{
	/**
	 * This method used to request window size from server;
	 * @return client window dimensions;
	 * @throws RemoteException
	 */
	Dimension getWindowSize() throws RemoteException;
	/**
	 * This methods used to request actual sprites list
	 * @return sprites list
	 * @throws RemoteException
	 */
	List<Sprite> getSpritesList() throws RemoteException;
	/**
	 * Method for creating new sprites
	 * @param position - sprite position
	 * @throws RemoteException
	 */
	void addSprite(Point position) throws RemoteException;
}
