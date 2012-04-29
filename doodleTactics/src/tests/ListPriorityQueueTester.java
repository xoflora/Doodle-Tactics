package tests;

import org.junit.*;

import util.ListPriorityQueue;

public class ListPriorityQueueTester {

	@Test
	public void testListPriorityQueue() {
		ListPriorityQueue<Integer> q = new ListPriorityQueue<Integer>();
		assert(q.isEmpty());
		assert(q.get(0) == null);
		assert(!q.remove(7));
		
		q.add(5);
		assert(q.size() == 1);
		assert(q.get(0) == 5);
		assert(!q.remove(3));
		
		q.add(3);
		assert(q.size() == 2);
		assert(q.get(0) == 5);
		assert(q.get(1) == 3);
		assert(q.get(2) == null);
		
		q.add(4);
		assert(q.size() == 3);
		assert(q.get(0) == 5);
		assert(q.get(1) == 4);
		assert(q.get(2) == 3);
		assert(q.get(3) == null);
		
		q.add(18);
		assert(q.size() == 4);
		assert(q.get(0) == 18);
		assert(q.get(1) == 5);
		assert(q.get(2) == 4);
		assert(q.get(3) == 3);
		assert(q.get(4) == null);
		
		q.add(-7);
		assert(q.size() == 5);
		assert(q.get(0) == 18);
		assert(q.get(1) == 5);
		assert(q.get(2) == 4);
		assert(q.get(3) == 3);
		assert(q.get(4) == -7);
		assert(q.get(5) == null);
		
		assert(!q.remove(8));
		assert(!q.remove(-243));
		assert(!q.remove(19));
		
		assert(q.remove(5));
		assert(q.size() == 4);
		assert(q.get(0) == 18);
		assert(q.get(1) == 4);
		assert(q.get(2) == 3);
		assert(q.get(3) == -7);
		assert(q.get(4) == null);
		
		assert(q.remove(18));
		assert(q.size() == 3);
		assert(q.get(0) == 4);
		assert(q.get(1) == 3);
		assert(q.get(2) == -7);
		assert(q.get(3) == null);
		
		assert(q.remove(-7));
		assert(q.size() == 2);
		assert(q.get(0) == 4);
		assert(q.get(1) == 3);
		assert(q.get(2) == null);
	}
}
