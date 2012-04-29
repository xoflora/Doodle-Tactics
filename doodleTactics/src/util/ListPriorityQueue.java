package util;

public class ListPriorityQueue<T extends Comparable<T>> {
	
	private static class ListNode<T> {
		public T _item;
		public ListNode<T> _next;
		public ListNode<T> _previous;
		
		public ListNode(T item) {
			_item = item;
			_next = null;
			_previous = null;
		}
	}
	
	private ListNode<T> _start;
	private int _size;
	
	public ListPriorityQueue() {
		_start = null;
		_size = 0;
	}

	public boolean add(T e) {
		if (_start == null) {
			_start = new ListNode<T>(e);
			_size++;
			return true;
		}
		
		ListNode<T> prev = null;
		ListNode<T> curr = _start;
		
		while (curr != null && e.compareTo(curr._item) < 0) {
			prev = curr;
			curr = curr._next;
		}
		if (prev == null) {
			_start = new ListNode<T>(e);
			_start._next = curr;
		}
		else {
			ListNode<T> n = new ListNode<T>(e);
			prev._next = n;
			n._next = curr;
		}
		
		_size++;
		
		return true;
	}

	public T get(int n) {
		ListNode<T> node = _start;
		while (n > 0 && node != null) {
			node = node._next;
			n--;
		}
		
		if (node == null)
			return null;
		else
			return node._item;
	}
	
	public int size() {
		return _size;
	}
	
	public boolean isEmpty() {
		return _size == 0;
	}
	
	public boolean remove(T item) {
		ListNode<T> prev = null;
		ListNode<T> node = _start;
		while (node != null) {
			if (node._item == item) {
				if (node == _start)
					_start = node._next;
				else
					prev._next = node._next;
				_size--;
				return true;
			}
			
			prev = node;
			node = node._next;
		}
		return false;
	}
}
