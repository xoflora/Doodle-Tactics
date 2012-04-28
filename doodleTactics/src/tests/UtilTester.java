package tests;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
	
	@Test
	/**
	 * tests the static union method of two lists
	 */
	public void testUnion() {
		List<Integer> test1 = new LinkedList<Integer>();
		List<Integer> test2 = new LinkedList<Integer>();
		List<Integer> cmp;
		
		assert(Util.union(test1, test2).isEmpty());
		
		test1.add(2);
		cmp = Util.union(test1, test2);
		assert(cmp.size() == 1 && cmp.get(0) == 2);
		
		test2.add(3);
		cmp = Util.union(test1, test2);
		assert(cmp.size() == 2 && cmp.contains(2) && cmp.contains(3));
		
		test1.add(3);
		cmp = Util.union(test1, test2);
		assert(cmp.size() == 2 && cmp.contains(2) && cmp.contains(3));
		
		for (int i = 0; i < 8; i++) {
			test1.add(4*i);
			test2.add(i*i);
		}
		cmp = Util.union(test1, test2);
		assert(cmp.size() == (test1.size() + test2.size() - 4));
		assert(cmp.containsAll(test1));
		assert(cmp.contains(1));
		assert(cmp.contains(9));
		assert(cmp.contains(25));
		assert(cmp.contains(49));
		
		
		List<List<Integer>> multiple = new LinkedList<List<Integer>>();
		assert(Util.union(multiple).isEmpty());
		
		multiple.add(test1);
		assert(Util.union(multiple).containsAll(test1) && test1.containsAll(Util.union(multiple)));
		
		multiple.add(test2);
		assert(Util.union(multiple).containsAll(Util.union(test1, test2)) &&
				Util.union(test1, test2).containsAll(Util.union(multiple)));

		for (int i = 0; i < 5; i++)
			multiple.add(new LinkedList<Integer>());
		assert(Util.union(multiple).containsAll(Util.union(test1, test2)) &&
				Util.union(test1, test2).containsAll(Util.union(multiple)));
		
		List<Integer> test3 = new LinkedList<Integer>();
		test3.add(97);
		test3.add(102);
		test3.add(25);
		test3.add(20);
		test3.add(4);
		multiple.add(test3);
		assert(Util.union(multiple).size() == test1.size() + test2.size() + test3.size() - 2 - 2 - 4 + 1 &&
				Util.union(multiple).containsAll(test1) &&
				Util.union(multiple).containsAll(test2) &&
				Util.union(multiple).containsAll(test3));
	}
	
	@Test
	/**
	 * tests list cloning
	 */
	public void testListCloning() {
		List<Integer> test = new LinkedList<Integer>();
		List<Integer> cloned = Util.clone(test);
		assert(cloned.isEmpty());
		
		test.add(9);
		cloned = Util.clone(test);
		assert(cloned.size() == 1 && cloned.get(0) == 9);
		
		test.clear();
		for (int i = 0; i < 23; i++)
			test.add(i);
		cloned = Util.clone(test);
		assert(cloned.size() == test.size());
		for (int i = 0; i < 23; i++)
			assert(cloned.get(i) == i);
	}
	
	@Test
	/**
	 * tests list zipping
	 */
	public void testListZip() {
		List<Integer> test = new LinkedList<Integer>();
		List<List<Integer>> source = new LinkedList<List<Integer>>();
		source.add(test);
		
		List<Integer> result = Util.zip(source);
		assert(result.isEmpty());
		
		for (int i = 0; i < 6; i++)
			test.add(i + 5);
		result = Util.zip(source);
		assert(result.containsAll(test) && test.containsAll(result) && result.size() == test.size());
		
		for (int i = 0; i < 5; i++)
			source.add(new LinkedList<Integer>());
		result = Util.zip(source);
		assert(result.containsAll(test) && test.containsAll(result) && result.size() == test.size());
		
		source.get(3).add(86);
		source.get(3).add(94);
		int[] cmp = {5, 86, 6, 94, 7, 8, 9, 10};
		result = Util.zip(source);
		for (int i = 0; i < cmp.length; i++)
			assert(result.get(i).equals(cmp[i]));
		assert(result.size() == cmp.length);
		
		source.get(4).add(271);
		source.get(2).add(189);
		source.get(2).add(-17);
		source.get(2).add(-5);
		source.get(2).add(180);
		for (int i = 0; i < 7; i++)
			source.get(3).add(-10*i);
		int[] cmp2 = {5, 189, 86, 271, 6, -17, 94, 7, -5, 0, 8, 180, -10, 9, -20, 10, -30, -40, -50, -60};
		result = Util.zip(source);
		for (int i = 0; i < cmp2.length; i++)
			assert(result.get(i).equals(cmp2[i]));
		assert(result.size() == cmp2.length);
	}
}
