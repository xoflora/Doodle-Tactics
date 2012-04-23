package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {
	
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
	
	
}
