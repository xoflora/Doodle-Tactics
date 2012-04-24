package util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import character.Character;

/**
 * contains utility methods of no particular grouping
 * @author rroelke
 *
 */
public class Util {
	
	/**
	 * performs the union of two lists
	 * @param <T> the parameterized type of the lists
	 * @param a the first list
	 * @param b the second list
	 * @return the union of the two lists
	 */
	public static <T> List<T> union(List<T> a, List<T> b) {
		
		HashMap<T, Boolean> include = new HashMap<T, Boolean>();
		
		for (T elt : a)
			include.put(elt, true);
		for (T elt : b)
			include.put(elt, true);
		
		List<T> toReturn = new ArrayList<T>();
		for (T elt : include.keySet())
			toReturn.add(elt);
		
		return toReturn;
	}
	
	/**
	 * performs the union of an arbitrary number of lists
	 * @param <T> the parameterized type of the lists
	 * @param collections the list of lists to union
	 * @return the union of all the given lists
	 */
	public static <T> List<T> union(List<List<T>> collections) {
		HashMap<T, Boolean> include = new HashMap<T, Boolean>();
		
		for (List<T> collection : collections)
			for (T elt : collection)
				include.put(elt, true);
		
		List<T> toReturn = new ArrayList<T>();
		for (T elt : include.keySet())
			toReturn.add(elt);
		
		return toReturn;
	}
	
	/**
	 * clones a list (by reference)
	 * @param <T> the parameterized type of the list
	 * @param a the list to clone
	 * @return a new list containing the same elements as a
	 */
	public static <T> List<T> clone(List<T> a) {
		List<T> newList = new ArrayList<T>();
		
		for (T elt : a)
			newList.add(elt);
		
		return newList;
	}
	
	/**
	 * mixes two colors
	 * @param first
	 * @param second
	 * @return the mixture of the two colors
	 */
	public static Color mixColors(Color first, Color second) {
		return new Color((first.getRed() + second.getRed())/2,
							(first.getGreen() + second.getGreen())/2,
							(first.getBlue() + second.getBlue())/2);
	}
}
