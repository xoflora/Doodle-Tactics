package util;

import java.util.Comparator;

/**
 * A Heap is a priority data structure that stores comparable data
 * 
 * @author rroelke
 *
 * @param <T> the Comparable data type stored in the heap
 */

public class Heap<T> {
    private T[] heapArray_;
    private int lastElement_ = -1;
    private T worstElement_;
    private Comparator<T> comp;
    
    @SuppressWarnings("unchecked")
	public Heap(int initialSize, Comparator<T> comp) {
    	heapArray_ = (T[]) new Object[initialSize];
    	this.comp = comp;
    	worstElement_ = null;
    }
    
    /**
     * @return whether there are no elements in the heap
     */
    public boolean isEmpty() {
        return lastElement_ == -1;
    }
    
    /**
     * @return the number of elements in the heap
     */
    public int size() {
        return lastElement_ + 1;
    }
    
    /**
     * inserts an element into the heap
     * @param element the element to insert
     * @return the index in the heap into which the element was inserted
     */
    @SuppressWarnings("unchecked")
	public int insert(T element) {
        if (lastElement_ == heapArray_.length - 1) {
            T[] newArray = (T[]) new Object[heapArray_.length * 2 + 1];
            for (int i = 0; i < heapArray_.length; i++) {
                newArray[i] = heapArray_[i];
            }
            
            heapArray_ = newArray;
        }
        if (worstElement_ == null || comp.compare(worstElement_, element) < 0)
        	worstElement_ = element;
        heapArray_[++lastElement_] = element;

        return siftUp(lastElement_);
    }
    
    //halves the length of the heapArray to save space
    //will only be called when less than 1/4 of the array is used
    /**
     * halves the length of the heapArray to save space
     * precondition: the heapArray is only filled to 1/4 capacity
     */
    @SuppressWarnings("unchecked")
	private void shrink() {
    	T[] update = (T[]) new Object[heapArray_.length/2];
    	for (int i = 0; i < size(); i++)
    		update[i] = heapArray_[i];
    	heapArray_ = update;
    }
    
    /**
     * removes and returns the highest-priority element in the heap
     * @return the highest-priority element in the heap
     */
    public T extractMin() {
    	if (!isEmpty()) {
    		T item = heapArray_[0];
    		heapArray_[0] = heapArray_[lastElement_];
    		heapArray_[lastElement_] = null;
    		lastElement_--;
    		siftDown(0);
        	if (size() <= heapArray_.length/4)
        		shrink();
        	if (item == worstElement_)
        		worstElement_ = null;
    		return item;
    	}
    	return null;
    }
    
    /**
     * @return the lowest-priority item in the heap
     */
    public T getWorstElement() {
    	return worstElement_;
    }
    
    /**
     * removes an element from the heap
     * @param element the desired element to remove
     * @return whether or not the element was contained in the heap (removed successfully)
     */
    public boolean remove(T element) {
    	int index = -1;
    	for (int i = 0; i < size(); i++)
    		if (heapArray_[i].equals(element)) {
    			index = i;
    			break;
    		}
    	if (index == -1)
    		return false;
    	
    	heapArray_[index] = heapArray_[lastElement_];
    	heapArray_[lastElement_] = null;
    	lastElement_--;
    	siftDown(index);
    	if (size() <= heapArray_.length/4)
    		shrink();
    	if (element.equals(worstElement_))
    		worstElement_ = null;
    	return true;
    }
    
    /**
     * sifts an element up the heap (maintains heap invariant if a priority changes)
     * @param element the ocation element to sift up
     * @return the new location in the heap
     */
    public int siftUp(int element) {
    	int parent = (int) Math.floor((element - 1) / 2);
        
        T elementItem = heapArray_[element];
        T parentItem = heapArray_[parent];
        
        if (element != 0 && comp.compare(elementItem, parentItem) <= 0) {
            heapArray_[parent] = elementItem;
            heapArray_[element] = parentItem;
            
            return siftUp(parent);
        }
        return element;
    }
    
    /**
     * sifts an element down the heap
     * @param element the location of the element to sift down
     * @return the new location in the heap
     */
    public int siftDown(int element) {
    	if (isEmpty())
    		return -1;
    	
		int leftChild = 2*element + 1;
		int rightChild = 2*element + 2;
		
		if (rightChild <= lastElement_) {
    		if (comp.compare(heapArray_[leftChild], heapArray_[rightChild]) <= 0
    				&& comp.compare(heapArray_[leftChild], heapArray_[element]) <= 0)
    		// left is less than or equal to right, then element replaces the left
    		{
    			T item = heapArray_[element];
    			heapArray_[element] = heapArray_[leftChild];
    			heapArray_[leftChild] = item;
    			return siftDown(leftChild);
    		}
    		else if (comp.compare(heapArray_[rightChild], heapArray_[element]) <= 0) {
    			// right is less than left, so element replaces right
    			T item = heapArray_[element];
    			heapArray_[element] = heapArray_[rightChild];
    			heapArray_[rightChild] = item;
    			return siftDown(rightChild);
    		}
		}
		else if (leftChild <= lastElement_ &&
				comp.compare(heapArray_[leftChild], heapArray_[element]) <= 0) {
			// no right child - this means the leftChild is the last element in the heap
			T item = heapArray_[element];
			heapArray_[element] = heapArray_[leftChild];
			heapArray_[leftChild] = item;
			return leftChild;
		}
		return element;
    }
    
    /**
     * generates a string representation of the heap
     * lists elements in order of how they are stored in the array
     */
    public String toString() {
    	if (isEmpty())
    		return "This heap is empty.";
    	
    	String toReturn = "";
    	for (int i = 0; i < size(); i++) {
    		toReturn += heapArray_[i] + "\t";
    	}
    	return toReturn + "\n";
    }
}
