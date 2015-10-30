package vandy.cs251;

import java.lang.ArrayIndexOutOfBoundsException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Provides a wrapper facade around primitive char arrays, allowing
 * for dynamic resizing.
 */
public class CharArray implements Comparable<CharArray>,
Cloneable {
    /**
     * The underlying array.
     */
    // TODO - you fill in here
    // @@ Please prefix class member variables with 'm'; e.g. mFoo or mBar
    private char[] mCharArray;
    
    /**
     * The current size of the array.
     */
    // TODO - you fill in here
    
    private int mSizeOfArray;
    
    /**
     * Default value for elements in the array.
     */
    // TODO - you fill in here
    
    private char mDefaultChar;
    
    /**
     * Constructs an array of the given size.
     *
     * @param size Non-negative integer size of the desired array.
     */
    
    public CharArray(int size) {
        // TODO - you fill in here
        mCharArray = new char[size];
        mSizeOfArray=size;
    }
    
    /**
     * Constructs an array of the given size, filled with the provided
     * default value.
     *
     * @param size Nonnegative integer size of the desired array.
     * @param mDefaultvalue A default value for the array.
     */
    public CharArray(int size,
                     char mDefaultvalue) {
        // TODO - you fill in here
        // @@ Make sure to delegate to the other constructor
        this(size);
        Arrays.fill(mCharArray, mDefaultvalue);
        mDefaultChar=mDefaultvalue;
        
    }
    
    /**
     * Copy constructor; creates a deep copy of the provided CharArray.
     *
     * @param s The CharArray to be copied.
     */
    public CharArray(CharArray s) {
        // TODO - you fill in here
        
        mSizeOfArray=s.mSizeOfArray;
        mDefaultChar=s.mDefaultChar;
        mCharArray = Arrays.copyOf(s.mCharArray, s.mSizeOfArray);
        
    }
    
    /**
     * Creates a deep copy of this CharArray.  Implements the
     * Prototype pattern.
     */
    @Override
    public Object clone(){
        // TODO - you fill in here (replace return null with right
        // implementation).
        
        CharArray cloneCharArray= new CharArray(this);
        return cloneCharArray;
    }
    
    /**
     * @return The current size of the array.
     */
    public int size() {
        // TODO - you fill in here (replace return 0 with right
        // implementation).
        
        return mSizeOfArray;
    }
    
    /**
     * @return The current maximum capacity of the array.
     */
    public int capacity() {
        // TODO - you fill in here (replace return 0 with right
        // implementation).
        return mCharArray.length;
    }
    
    /**
     * Resizes the array to the requested size.
     *
     * Changes the capacity of this array to hold the requested number of elements.
     * Note the following optimizations/implementation details:
     * <ul>
     *   <li> If the requests size is smaller than the current maximum capacity, new memory
     *   is not allocated.
     *   <li> If the array was constructed with a default value, it is used to populate
     *   uninitialized fields in the array.
     * </ul>
     * @param size Nonnegative requested new size.
     */
    public void resize(int size) {
        
        // TODO - you fill in here
        
        if (size == mSizeOfArray)    //optimization to reduce unnecessary work
            return;
        if (size > capacity()) {
            mCharArray = Arrays.copyOf(mCharArray, size);
            Arrays.fill(mCharArray, mSizeOfArray, size, mDefaultChar);
            mSizeOfArray = size;
            
        }  else { //size <= capacity()
            // @@ I'm not sure this is correct:
            mSizeOfArray=size; //don't want to allocate memory in that case
            Arrays.fill(mCharArray, mSizeOfArray, capacity(), mDefaultChar);
        }
        
    }
    
    /**
     * @return the element at the requested index.
     * @param index Nonnegative index of the requested element.
     * @throws ArrayIndexOutOfBoundsException If the requested index is outside the
     * current bounds of the array.
     */
    public char get(int index) throws ArrayIndexOutOfBoundsException {
        // TODO - you fill in here (replace return '\0' with right
        // implementation).
        rangeCheck(index);
        return mCharArray[index];
    }
    
    /**
     * Sets the element at the requested index with a provided value.
     * @param index Nonnegative index of the requested element.
     * @param value A provided value.
     * @throws ArrayIndexOutOfBoundsException If the requested index is outside the
     * current bounds of the array.
     */
    public void set(int index, char value) throws ArrayIndexOutOfBoundsException{
        // TODO - you fill in here
        rangeCheck(index);
        mCharArray[index]= value;
    }
    
    /**
     * Compares this array with another array.
     * <p>
     * This is a requirement of the Comparable interface.  It is used to provide
     * an ordering for CharArray elements.
     * @return a negative value if the provided array is "greater than" this array,
     * zero if the arrays are identical, and a positive value if the
     * provided array is "less than" this array. These arrays should be compred
     * lexicographically.
     */
    @Override
    public int compareTo(CharArray s) {
        // TODO - you fill in here (replace return 0 with right
        // implementation).
        
        for (int i = 0; i < Math.min(s.mSizeOfArray, mSizeOfArray); i++) {
            if (s.mCharArray[i] != mCharArray[i]) {
                return mCharArray[i] - s.mCharArray[i];
            }
        }
        -            return mSizeOfArray - s.mSizeOfArray;
        +        return mSizeOfArray - s.mSizeOfArray;
        
    }
    
    /**
     * Throws an exception if the index is out of bound.
     */
    private void rangeCheck(int index) throws ArrayIndexOutOfBoundsException{
        // TODO - you fill in here
        if (index < 0 || index >= this.size()){
            throw new ArrayIndexOutOfBoundsException("Index out of bounds.");
        }
    }
}
