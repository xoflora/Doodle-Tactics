package util;

import java.util.HashMap;
import java.util.Iterator;

/**
 * 
 * @author rroelke
 * 
 * a Hashpairing instance is a hashing data structure that looks elements up using a pairing of keys
 *
 * @param <K> the first type of key
 * @param <T> the second type of key
 * @param <V> the value stored in the dat structure
 */
public class Hashpairing<K, T, V> {
	
	private class Key {
		
		private K _key1;
		private T _key2;
		
		public Key(K key1, T key2) throws InvalidKeyException {
			if (key1 == null || key2 == null)
				throw new InvalidKeyException();
			_key1 = key1;
			_key2 = key2;
		}
		
		@Override
		public int hashCode() {
			if (_key1.hashCode() < _key2.hashCode())
				return _key2.hashCode()*_key2.hashCode() + _key1.hashCode();
			else
				return _key1.hashCode()*_key1.hashCode() + _key2.hashCode();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object other) {
			try {
				Key k = (Key) other;
				return (_key1.equals(k._key1) && _key2.equals(k._key2));
			} catch(ClassCastException e) {
				return false;
			}
		}
	}
	
	private HashMap<Key, V> _table;
	
	public Hashpairing() {
		_table = new HashMap<Key, V>();
	}
	
	/**
	 * inserts a value into the hashtable with the given keys
	 * @param key1 the first key
	 * @param key2 the second key
	 * @param value the value to insert
	 * @return the value in the hashtable previously with those keys
	 */
	public V put(K key1, T key2, V value) {
		try {
			return _table.put(new Key(key1, key2), value);
		} catch (InvalidKeyException e) {
			return null;
		}
	}
	
	/**
	 * looks up the value in the table corresponding to the given keys
	 * @param key1 the first key
	 * @param key2 the second key
	 * @return the value in the table corresponding to the given keys
	 */
	public V get(K key1, T key2) {
		try {
			return _table.get(new Key(key1, key2));
		} catch (InvalidKeyException e) {
			return null;
		}
	}
	
	/**
	 * removes the datum corresponding to the given keys from the table
	 * @param key1 the first key 
	 * @param key2 the second key
	 * @return the removed element
	 */
	public V remove(K key1, T key2) {
		try {
			return _table.remove(new Key(key1, key2));
		} catch (InvalidKeyException e) {
			return null;
		}
	}
}
