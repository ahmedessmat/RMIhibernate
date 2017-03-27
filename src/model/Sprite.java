

package model;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This is sprite class responsible to 
 * represent sprite persistant object.
 *
 */
@Entity
@Table(name="SPRITES")
public class Sprite implements Serializable{

	/**
	 * Generated UID.
	 */
	private static final long serialVersionUID = 1264746038881602296L;
	
	private int spriteID;
	private int x;
	private int y;
	private int size;
	private int r;
	private int g;
	private int b;
	private int dx;
	private int dy;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getSpriteID() {
		return spriteID;
	}
	public void setSpriteID(int spriteID) {
		this.spriteID = spriteID;
	}
	
	@Column
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	@Column
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Column
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	@Column
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	
	@Column
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	
	@Column
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	
	@Column
	public int getDx() {
		return dx;
	}
	public void setDx(int dx) {
		this.dx = dx;
	}
	
	@Column
	public int getDy() {
		return dy;
	}
	public void setDy(int dy) {
		this.dy = dy;
	}
	
	/**
	 * Helper method for setting sprite color
	 * @param c - new sprite color
	 */
	@Transient
	public void setColor(Color c){
		setR(c.getRed());
		setG(c.getGreen());
		setB(c.getBlue());
	}
	
	/**
	 * Helper method for getting sprite color
	 * @return sprite color
	 */
	@Transient
	public Color getColor(){
		return new Color(getR(), getG(), getB());
	}
	
	public void move(Dimension panelSize){
		// check for bounce and make the ball bounce if necessary
        //
        if (x < 0 && dx < 0){
            //bounce off the left wall 
            x = 0;
            dx = -dx;
        }
        if (y < 0 && dy < 0){
            //bounce off the top wall
            y = 0;
            dy = -dy;
        }
        if (x > panelSize.getWidth() - size && dx > 0){
            //bounce off the right wall
        	x = (int)panelSize.getWidth() - size;
        	dx = - dx;
        }       
        if (y > panelSize.getHeight() - size && dy > 0){
            //bounce off the bottom wall
        	y = (int)panelSize.getHeight() - size;
        	dy = -dy;
        }

        //make the ball move
		
		x += dx;
		y += dy;
	}
}
