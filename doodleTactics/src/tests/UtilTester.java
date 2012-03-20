package tests;

import org.junit.Test;

import util.*;

public class UtilTester {

	@Test
	/**
	 * tests the Hashpairing data structure
	 * note that the structure is based upon a hashtable, which limits the need for wholly rigorous testing
	 */
	public void testHashpairing() {
		Hashpairing<Integer, String, Integer> table = new Hashpairing<Integer, String, Integer>();
		
		assert(table.put(null, null, 18) == null);
		assert(table.put(null, "stuff", 1) == null);
		assert(table.put(23, null, 98) == null);
		assert(table.get(null, null) == null);
		assert(table.get(null, "stuff") == null);
		assert(table.get(23, null) == null);
		assert(table.remove(null, null) == null);
		assert(table.remove(null, "stuff") == null);
		assert(table.remove(23, null) == null);
		
		assert(table.put(5, "hello", 8) == null);
		assert(table.get(5, "hello").equals(8));
		
		assert(table.put(5, "stuff", 7) == null);
		assert(table.get(5, "stuff").equals(7));
		
		assert(table.get(5, "crunch") == null);
		assert(table.get(7, "stuff") == null);
		
		assert(table.put(5, "stuff", 12).equals(7));
		assert(table.remove(5, "stuff").equals(12));
		assert(table.get(5, "stuff") == null);
		assert(table.remove(5, "stuff") == null);
	}
	
}
