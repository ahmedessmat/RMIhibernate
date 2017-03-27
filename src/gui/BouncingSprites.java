

package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

import javax.swing.JFrame;

import interfaces.SpriteServerInterface;

/**
 * This is the main class responsible 
 * for to initialize the frames, get panel size from server
 * and add the SpritePanel to frame.
 */
public class BouncingSprites {
	
	 private static SpritePanel panel;
	 private static JFrame frame;

    /**
     * main method to start the program
     */
    public static void main(String[] args) {
    	int portNum = 8082;
		if(args.length > 0){
			portNum = Integer.parseInt(args[0]);
		}
    	
    	System.out.println("Attempting to connect to rmi://localhost:" + portNum + "/SpriteServer");
    	try{
    		SpriteServerInterface ss = (SpriteServerInterface) 
				Naming.lookup("rmi://localhost:" + portNum + "/SpriteServer");
    	
    		Dimension frameSize = ss.getWindowSize();
    		
    		panel = new SpritePanel(ss);
    		
	    	frame = new JFrame("RMI HIBERNATE");
	        frame.setSize(frameSize.width, frameSize.height);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.add(panel,BorderLayout.CENTER);
	        frame.setVisible(true);
	        
	        panel.loop();
	        
    	}catch(RemoteException e){
    		System.out.println("Something went wrong on connecting to RMI");
    		e.printStackTrace();
    	}catch(MalformedURLException e){
    		System.out.println("Malformed RMI ULR");
    		e.printStackTrace();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
}
