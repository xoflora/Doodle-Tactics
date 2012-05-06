package items;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import main.DoodleTactics;

import character.Character;
public abstract class Item implements Serializable{

	protected boolean _removable;
	private transient BufferedImage _image;
	private String _imagePath;
	static int  numItems = 0;
	public final int _id;
	private String _description;
	protected boolean _isEquip = false, _isWeapon = false, _isCuirass = false, _isFootgear = false, _isShield = false;
	private String _name;
		
	public Item(DoodleTactics dt, String imagePath, String name){
		//The Id makes each item unique
		_image = dt.importImage(imagePath);
		_imagePath = imagePath;
		_id = numItems;
		numItems++;
		_name = name;
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
	
//	public void setAsEquip(boolean bool) {
//		_isEquip = bool;
//	}
	
	public boolean isEquip() {
		return _isEquip;
	}
	
	public boolean isWeapon() {
		return _isWeapon;
	}
	
	public boolean isCuirass() {
		return _isCuirass;
	}
	
	public boolean isShield() {
		return _isShield;
	}
	
	public boolean isFootgear() {
		return _isFootgear;
	}
	
	public void setAsWeapon(boolean bool) {
		_isWeapon = bool;
	}
	
	public void loadItem(DoodleTactics dt){
		_image = dt.importImage(_imagePath);
	}
	
	/**
	 * @author jeshapir
	 */
	public void paint(){
		//TODO
	}
	
}
