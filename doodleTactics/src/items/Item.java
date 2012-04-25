package items;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import character.Character;
public abstract class Item implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6745186650125253186L;
	protected boolean _removable;
	private BufferedImage _image;
	static int  numItems = 0;
	public final int _id;
	private String _description;
		
	public Item(BufferedImage image){
		//The Id makes each item unique
		_image = image;
		_id = numItems;
		numItems++;
	}
	
	/**
	 * Adds this Item to a Character's inventory
	 * @param c - the Character
	 * @throws ItemException if item is already on the Character
	 */
	public abstract void exert(Character c);
	
	public BufferedImage getImage() {
		return _image;
	}
	
	public String getDescription() {
		return _description;
	}
	
	public void setDescription(String desc) {
		_description = desc;
	}
	
	/**
	 * @author jeshapir
	 */
	public void paint(){
		//TODO
	}
	
}
