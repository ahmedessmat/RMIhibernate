

package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import interfaces.SpriteServerInterface;
import model.Sprite;

/**
 * This class is responsible for making JPanel GUI to this program
 * This is the main GUI part of application.
 * This class query server for sprites list, and draw it.
 */

@SuppressWarnings("serial")
public class SpritePanel extends JPanel {
	SpriteServerInterface ss;
	
	List<Sprite> sprites=new ArrayList<>();
	 
	public SpritePanel(SpriteServerInterface ss){
		addMouseListener(new Mouse());
		this.ss = ss;
	}
	
	/**
	 * This method uses RMI to create sprite on server.
	 */
	@SuppressWarnings("unchecked")
	private void newSprite (MouseEvent event) throws RemoteException{
		Point position = event.getPoint();
		
		ss.addSprite(position);
	}
	
	/**
	 * This method handles main panel loop, querying sprites from server and draw it;
	 */
	public void loop() throws RemoteException{
		while (true){
			sprites = ss.getSpritesList();
			repaint();
			try{
				Thread.sleep(40);
			}catch(InterruptedException e){
				
			}
		}
	}
	
	/**
	 * This method draw the multiple sprites on GUi.
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		for(Sprite s : sprites){
				//TODO set color;
				g.setColor(s.getColor());
				g.fillOval(s.getX(), s.getY(), s.getSize(), s.getSize());;
		}
	}
	
	/**
	 * This inner class responsible for adding mouse event
	 */
	public class Mouse extends MouseAdapter {
		@Override
	    public void mousePressed( final MouseEvent event ) {
	        try {
				newSprite(event);
			} catch (RemoteException e) {
				System.out.println("Remote exception while creating new sprite");
				return;
			}
	        System.out.println("Sprite created");
	    }
	}
}
