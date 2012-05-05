package controller.combatController.player;

import java.util.List;


import character.Character;

/**
 * implemented by controllers that rely on a UnitPool
 * @author rroelke
 *
 */
public interface PoolDependent {
	
	public UnitPool getPool();
	
	public List<Character> getUnits();
	
	/**
	 * the unit pool associated with the object pushes a character to it
	 * @param c the character to send to the object
	 */
	public void getCharacterFromPool(Character c);
	
	/**
	 * removes a unit from the character pool
	 * @param c the unit to remove
	 */
	public void removeUnitFromPool(Character c);
	
	/**
	 * adds a unit to the character pool
	 * @param c the unit to add
	 */
	public void addUnitToPool(Character c);
	
	/**
	 * performs an alternate action given a character
	 */
	public void alternateAction(Character c);
	
	/**
	 * performs an action for clicking on the unit pool
	 * @param type
	 */
	public void unitPoolClicked(int type);
	
	/**
	 * begins use of the unit pool 
	 */
	public void initialize();
	
	/**
	 * finalizes actions with the unit pool to indicate that it is no longer needed
	 */
	public void finalize();
}
