package logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;

import interfaces.SpriteServerInterface;
import model.Sprite;
/**
 * This is the main server.
 * Server handle creating sprites, storing it in database and send information about sprites to client. 
 */
public class SpriteServer extends RemoteServer implements SpriteServerInterface{
	/**
	 * Generated UID
	 */
	private static final long serialVersionUID = 8516642394894216608L;
	
	SessionFactory sessionFactory;
	Color[] spriteColors = new Color[]{Color.RED, Color.BLUE, Color.GREEN};
	int lastColor = 0;
	List<Sprite> sprites;
	final Random random = new Random();
	
	final Dimension panelDimensions = new Dimension(500, 500);
	final int maxSpeed = 5;
	
	
	/**
	 * Main server method
	 * This method initialize 
	 * @param args
	 */
	public static void main(String [] args) {
		int portNum = 8082;
		if(args.length > 0){
			portNum = Integer.parseInt(args[0]);
		}
		
		try {
			SpriteServer ss = new SpriteServer();
			LocateRegistry.createRegistry(portNum);
			System.out.println( "Registry created" );
			UnicastRemoteObject.exportObject(ss,0);
			System.out.println( "Exported" );
			Naming.rebind("//localhost:" + portNum + "/SpriteServer", ss);
			
			ss.loop();
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Main server loop.
	 * This loop handles sprite animation.
	 */
	private void loop(){
		while(true){
			Session session = sessionFactory.getCurrentSession();
			session.beginTransaction();
			//if it's first loop we need to retrieve information about sprites;
			if (sprites == null){
				sprites = (List<Sprite>)session.createQuery("from Sprite").list();
			}
			
			for (Sprite s: sprites){
				s.move(panelDimensions);
				session.update(s);
			}
			
			
			session.getTransaction().commit();
			
			try{
				Thread.sleep(5);
			}catch (InterruptedException e){
				
			}
		}

	}
	
	/**
	 * Server constructor. 
	 * Initialize Hibernate session factory.
	 */
	public SpriteServer(){
		// A SessionFactory is set up once for an application!
    	final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
    			.configure() // configures settings from hibernate.cfg.xml
    			.build();
    	try {
    		MetadataImplementor meta = (MetadataImplementor) new MetadataSources( registry ).addAnnotatedClass(Sprite.class).buildMetadata();
    		sessionFactory = meta.buildSessionFactory();
    	}
    	catch (Exception e) {
    		// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
    		// so destroy it manually.
    		StandardServiceRegistryBuilder.destroy( registry );
    	}
	}

	
	/**
	 * Remote interface implementation
	 * Look {@link SpriteServerInterface#getSpritesList}
	 */
	@Override
	public List<Sprite> getSpritesList() {
		return sprites;
	}

	
	/**
	 * Remote interface implementation
	 * Look {@link SpriteServerInterface#addSprite(Point)}
	 */
	@Override
	public void addSprite(Point position) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Sprite sprite = new Sprite();
		sprite.setX(position.x);
		sprite.setY(position.y);
		sprite.setSize(10);
		sprite.setDx(ThreadLocalRandom.current().nextInt(10) - 5);
		sprite.setDy(ThreadLocalRandom.current().nextInt(10) - 5);
		
		synchronized (this) {
			sprite.setColor(spriteColors[lastColor]);
			lastColor = ++lastColor % spriteColors.length;
		}
		
		session.save(sprite);
		session.getTransaction().commit();
		session.close();
		
		sprites.add(sprite);
		
		System.out.println("Sprite created at " + position.toString());
	}

	/**
	 * Remote interface implementation
	 * Look {@link SpriteServerInterface#getWindowSize()}
	 */
	@Override
	public Dimension getWindowSize() {
		return panelDimensions;
	}
}
