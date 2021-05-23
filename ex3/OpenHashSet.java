import java.util.Iterator;


/**
 * This is a OpenHashSet class that extends SimpleHashSet.
 */
public class OpenHashSet extends SimpleHashSet {

    /* Number of total current elements in the table*/
    private int elements = 0 ;
    /* Number of cells of the table.*/
    private int capacity;
    /* Determine how full the table is allowed to get before its capacity is increased*/
    private double upperLoadFactor;
    /* Determine how empty the table is allowed to get before its capacity is decreased*/
    private double lowerLoadFactor;
    /* the list of "buckets"*/
    private LinkedListWrapper[] mainList;

    //Constants:
    private final int defaultCapacity = 16;
    private final double defaultULF = 0.75;
    private final double defaultLLF = 0.25;
    private final int changeFactor = 2;

    //Constructors:

    /**A default constructor. Constructs a new, empty table with default initial capacity (16),
     *upper load factor (0.75) and lower load factor (0.25).*/
    public OpenHashSet(){
        this.capacity = defaultCapacity;
        this.upperLoadFactor = defaultULF;
        this.lowerLoadFactor = defaultLLF;
        this.mainList = new LinkedListWrapper[capacity];
    }

    /** Constructs a new, empty table with the specified load factors, and the default initial capacity (16).
     * @param upperLoadFactor The upper load factor of the hash table.
     * @param lowerLoadFactor The lower load factor of the hash table.
     */
    public OpenHashSet(float upperLoadFactor, float lowerLoadFactor){
        this.capacity = defaultCapacity;
        this.upperLoadFactor = upperLoadFactor;
        this.lowerLoadFactor = lowerLoadFactor;
        this.mainList = new LinkedListWrapper[capacity];
    }

    /**Data constructor - builds the hash set by adding the elements one by one.
     * Duplicate values should be ignored. The new table has the default values of initial capacity (16),
     * upper load factor (0.75), and lower load factor (0.25).
     * @param data Values to add to the set.
     */
    public OpenHashSet(java.lang.String[] data){
        this.capacity = defaultCapacity;
        this.upperLoadFactor = defaultULF;
        this.lowerLoadFactor = defaultLLF;
        this.mainList = new LinkedListWrapper[capacity];
        this.elements = data.length;
    }

    //Methods:

    public void printTable(){
        for (int i =0 ; i< mainList.length; i++ ) {
            System.out.println("cell "+i+':');
            if  (mainList[i] != null) {
                mainList[i].printLinkedList();
            }
        }System.out.println("************");
    }

    /**
     * Creates a linkedListWrapper object.
     * @return  linkedListWrapper object.
     */
    private LinkedListWrapper createBucket (){
        return new LinkedListWrapper();
    }

    /**
     *get and adapt the string's hashCode to the table size.
     * @param value value to find hash code.
     * @return the adapted value's hashcode.
     */
    private int getAdaptedHashCode(String value){
        return value.hashCode()&(capacity-1);
    }

    /**
     * Rehashes the contents of the receiver into a new table with a smaller or larger capacity.
     * This method is called automatically when the number of keys in the receiver exceeds the upper load factor
     * or falls below the lower load factor.
     * @param newCapacity size fot new table.
     * @param newValue New value to add to the set
     * @return true if we rehashed (including the newValue) successfully, false otherwise.
     */
    private boolean rehash(int newCapacity, String newValue, int action){
        int oldCapacity = capacity;
        capacity = newCapacity ;
        LinkedListWrapper[] oldMainList = mainList;
        mainList = new LinkedListWrapper[capacity];
        int oldElements = elements;
        elements = 0;
        for (int listIdx = 0; listIdx < oldCapacity; listIdx ++ ){
            if (oldMainList[listIdx]!= null){
                Iterator <String> bucketIterator = oldMainList[listIdx].iterator();
                while (bucketIterator.hasNext()){
                    add(bucketIterator.next());
                }
            }
            if (oldElements == elements){
                break;
            }
        }
        if (action == 1) {
            add(newValue);
        }if (action == 0) {
            delete(newValue);
        }
        return true;
    }

    /**
     * Add a specified element to the set if it's not already in it.
     * @param newValue New value to add to the set
     * @return False iff newValue already exists in the set
     */
    public boolean add(String newValue) {
        if (!contains(newValue)) {
            if (((float)(elements + 1) / capacity) > upperLoadFactor) {
                elements += 1;
                return (rehash((changeFactor * capacity), newValue, 1));// mathematical calculation: new table size.
            }
            int index = getAdaptedHashCode(newValue);
            if (mainList[index] == null) {
                mainList[index] = createBucket();
            }
            elements += 1;
            return mainList[index].myAdd(newValue);
        }
        return false;
    }

    /**
     * Look for a specified value in the set.
     * @param searchVal Value to search for
     * @return True iff searchVal is found in the set
     */
    public boolean contains(java.lang.String searchVal){
        int index = getAdaptedHashCode(searchVal);
        LinkedListWrapper bucket= mainList[index];
        return (bucket != null && bucket.myContains(searchVal));
    }

    /**
     * Remove the input element from the set.
     * @param toDelete Value to delete
     * @return True iff toDelete is found and deleted
     */
    public boolean delete(String toDelete){
        if (contains(toDelete)){
            if (((float)(elements-1)/capacity) < lowerLoadFactor){
                int newCapacity = capacity/changeFactor;
                return rehash(newCapacity, toDelete, 0);
            }int index = getAdaptedHashCode(toDelete);
            elements -=1;
            return mainList[index].myDelete(toDelete);
        }
        return false;
    }
    /**
     * @return The number of elements currently in the set.
     */
    public int size(){
        return elements;
    }

    /**
     * @return The current capacity (number of cells) of the table.
     */
    public int capacity(){
        return capacity;
    }

    public static void main(String [ ] args){
        String [] data = {"abs", "c", "ddd", "dr", "DRG", "sf", "ffe", "456", "w5v", "d4", "s3", "%Fd", "DRF"};
        OpenHashSet myOpen = new OpenHashSet();
        for (int i =0 ; i< data.length; i++ ) {
            System.out.println(myOpen.capacity());
            System.out.println(myOpen.size());
            System.out.println("value to add "+ '\"'+data[i]+'\"');
            myOpen.add(data[i]);
            System.out.println(myOpen.capacity());
            System.out.println(myOpen.size());
            myOpen.printTable();
        }
        for (int i =0 ; i< 6; i++ ) {
            System.out.println(myOpen.capacity());
            System.out.println(myOpen.size());
            System.out.println("value to remove " + '\"' + data[i] + '\"');
            myOpen.delete(data[i]);
            System.out.println(myOpen.capacity());
            System.out.println(myOpen.size());
            myOpen.printTable();
        }


    }

    }




