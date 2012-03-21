package tests;

import java.util.Comparator;

import org.junit.Test;

import util.*;

/**
 * 
 * @author rroelke
 * tests the classes and methods of the utility package
 */
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
	
	@Test
	/**
	 * tests the heap data structure
	 */
	public void testHeap() {
		Comparator<Integer> cmp =
			new Comparator<Integer>() {

				@Override
				public int compare(Integer o1, Integer o2) {
					int v1 = o1;
					int v2 = o2;
					if (v1 < v2)
						return -1;
					else if (v1 == v2)
						return 0;
					else
						return 1;
				}
			};
		assert(cmp.compare(0, 1) == -1);
		assert(cmp.compare(1, 0) == 1);
		assert(cmp.compare(0, 0) == 0);
		
		Heap<Integer> test = new Heap<Integer>(1, cmp);
        assert(test.extractMin() == null);

        for (int i = 0; i < 12; i++)
        	test.insert(i);
        assert(test.extractMin() == 0);
        assert(test.extractMin() == 1);
        assert(test.extractMin() == 2);
        assert(test.extractMin() == 3);
        
        test.insert(13);
        test.insert(7);
        
        assert(test.remove(7));
        assert(test.remove(7));
        assert(!test.remove(7));
        
        assert(test.remove(10));
        assert(test.remove(8));
        assert(test.remove(13));
        
        int[] elts = {4, 5, 6, 9, 11};
        for (int i = 0; !test.isEmpty(); i++)
        	assert(test.extractMin() == elts[i]);
        
        assert(test.isEmpty());
    }
}
