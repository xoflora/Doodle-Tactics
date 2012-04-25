package controller.combatController;

import java.util.List;

import character.Character;

/**
 * implemented by combat controllers that rely on a UnitPool
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
	public void addCharacterToPool(Character c);
	
}