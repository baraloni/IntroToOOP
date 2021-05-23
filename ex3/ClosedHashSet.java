import java.util.Arrays;

/**
 * This is a ClosedHashSet class that extends SimpleHashSet.
 */
public class ClosedHashSet extends SimpleHashSet {
    /* Number of total current elements in the table*/
    private int elements = 0 ;
    /* Number of cells of the table.*/
    private int capacity;
    /* Determine how full the table is allowed to get before its capacity is increased*/
    private double upperLoadFactor;
    /* Determine how empty the table is allowed to get before its capacity is decreased*/
    private double lowerLoadFactor;
    /* the list we hash on*/
    private String[] mainList;
    /* a indication to an erased cell*/
    private String flag = new String ();
    private int oldCapacity;

    //Constants:
    private final int defaultCapacity = 16;
    private final double defaultULF = 0.75;
    private final double defaultLLF = 0.25;
    private final int changeFactor = 2;

    //Constructors:

    /**A default constructor. Constructs a new, empty table with default initial capacity (16),
     *upper load factor (0.75) and lower load factor (0.25).*/
    public ClosedHashSet(){
        this.capacity = defaultCapacity;
        this.upperLoadFactor = defaultULF;
        this.lowerLoadFactor = defaultLLF;
        this.mainList = new String[capacity];
        this.oldCapacity = capacity;
    }

    /** Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
     * @param upperLoadFactor The upper load factor of the hash table.
     * @param lowerLoadFactor The lower load factor of the hash table.
     */
    public ClosedHashSet(float upperLoadFactor, float lowerLoadFactor){
        this.capacity = defaultCapacity;
        this.upperLoadFactor = upperLoadFactor;
        this.lowerLoadFactor = lowerLoadFactor;
        this.mainList = new String[capacity];
        this.oldCapacity = capacity;

    }

    /**Data constructor - builds the hash set by adding the elements one by one.
     * Duplicate values should be ignored. The new table has the default values of initial capacity (16),
     * upper load factor (0.75), and lower load factor (0.25).
     * @param data Values to add to the set.
     */
    public ClosedHashSet(java.lang.String[] data) {
        this.capacity = defaultCapacity;
        this.upperLoadFactor = defaultULF;
        this.lowerLoadFactor = defaultLLF;
        this.elements = data.length;
        this.oldCapacity = capacity;

    }

    // Methods:

    public void printTable(){
        System.out.println(Arrays.toString(mainList));
        System.out.println("**********");
    }
    public int capacity(){
        return capacity;
    }
     public String[] getMainList(){
         return mainList;
     }

    /**
     * Rehashes the contents of the receiver into a new table with a smaller or larger capacity.
     * This method is called automatically when the number of keys in the receiver exceeds the upper load factor
     * or falls below the lower load factor.
     * @param newCapacity size fot new table.
     * @param newValue New value to add to the set
     * @return true if we rehashed (including the newValue) successfully, false otherwise.
     */
    private boolean rehash(int newCapacity, String newValue, int action) {
        capacity = newCapacity;
        String[] oldMainList = mainList;
        mainList = new String[newCapacity];
        int oldElements = elements;
        elements = 0;
        for (int listIdx = 0; listIdx < oldCapacity; listIdx++) {
            if ((oldMainList[listIdx] != null) & (oldMainList[listIdx] != flag)) { // so its occupied
                add(oldMainList[listIdx]);
            }
            if (elements == oldElements) {
                break;
            }
        }
        if (action == 1) {
            add(newValue);
        }
        if (action == 0) {
            delete(newValue);
        }oldCapacity = capacity;
        return true;
    }

    /**
     *get and adapt the string's hashCode to the table size.
     * @param value value to find hash code.
     * @return the adapted value's hashcode.
     */
    private int getAdaptedHashCode(String value, int iterNum, int capacity){
        return (value.hashCode() +(iterNum+iterNum*iterNum)/2)&(capacity-1);
    }

    /**
     * Add a specified element to the set if it's not already in it.
     * @param newValue New value to add to the set
     * @return False iff newValue already exists in the set
     */
    /* REMARK: notice that when the iteration is over, it has to be that we will
    find an appropriate cell: empty or flag.  the cell cant be all full
    because it will necessarily lead to rehash. so no need to take care of this case.
    */
    public boolean add(String newValue){
        if (!contains(newValue)) {
            if (((float)(elements+1)/capacity) <= upperLoadFactor){ // adding without hashing
                for (int iterNum = 0; iterNum < capacity; iterNum++) {
                    int index = getAdaptedHashCode(newValue, iterNum, capacity);
                    if ((mainList[index] == null) | (mainList[index] == flag)) {
                        // empty or flag. doesn't matter because newValue isn't already in table.
                        mainList[index] = newValue;
                        elements += 1;
                        return true;
                    } // no else because it will not happen, see REMARK
                }
            }else {
                return rehash((changeFactor*capacity),newValue, 1);
            }
        }
        return false;
    }
    /**
     * Look for a specified value in the set.
     * @param searchVal Value to search for
     * @return True iff searchVal is found in the set
     */
    public boolean contains(String searchVal) {
        for (int iterNum = 0; iterNum < oldCapacity; iterNum++) {
            int index = getAdaptedHashCode(searchVal, iterNum, capacity);
            if (mainList[index] == null) { //empty
                return false;
            }else { // if occupied
                if (mainList[index].equals(searchVal)) { // same value
                    return true;
                }
            }
        }return false; //wont get to this point because the table cant get full (an empty cell will be found).
    }

    /**
     * Remove the input element from the set.
     * @param toDelete Value to delete
     * @return True iff toDelete is found and deleted
     */
    public boolean delete(String toDelete){
        if(contains(toDelete)) {
            if((((float)elements-1)/capacity) >= lowerLoadFactor) {
                for (int iterNum = 0; iterNum < capacity; iterNum++) {
                    int index = getAdaptedHashCode(toDelete, iterNum, capacity);
                    if (mainList[index] == toDelete) {
                        mainList[index] = flag;
                        elements -= 1;
                        return true;
                    }
                }
            }return rehash((capacity/changeFactor),toDelete, 0);
        }return false;
    }

    /**
     * @return The number of elements currently in the set
     */
    public int size(){
        return elements;
    }

    public static void main(String [ ] args) {
        String[] data = {"abs", "c", "ddd", "dr", "DRG", "sf", "ffe", "456", "w5v", "d4", "s3", "%Fd","DFF"};
        ClosedHashSet myClosed = new ClosedHashSet();
        for (int i = 0; i < data.length; i++) {
            System.out.println(myClosed.capacity());
            System.out.println(myClosed.size());
            System.out.println("value to add " + '\"' + data[i] + '\"');
            myClosed.add(data[i]);
            System.out.println(myClosed.capacity());
            System.out.println(myClosed.size());
            myClosed.printTable();

        }
        for (int i = 0; i < 6; i++) {
            System.out.println(myClosed.capacity());
            System.out.println(myClosed.size());
            System.out.println("value to remove " + '\"' + data[i] + '\"');
            myClosed.delete(data[i]);
            System.out.println(myClosed.capacity());
            System.out.println(myClosed.size());
            myClosed.printTable();
        }
    }
    }


