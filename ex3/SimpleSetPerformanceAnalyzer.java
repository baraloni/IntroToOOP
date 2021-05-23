import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 */
public class SimpleSetPerformanceAnalyzer {
    private SimpleSet[] comparingArray;
    private String[] toProcess;
    int itemsToCompare = 5;

    //constructor:

    public SimpleSetPerformanceAnalyzer(OpenHashSet openHashSet, ClosedHashSet closedHashSet,
                                 TreeSet<String> treeHashSet, LinkedList<String> linkedList,
                                 HashSet<String> hashSet, String[] data) {
        this.comparingArray = new SimpleSet[itemsToCompare];
        comparingArray[0] = openHashSet;
        comparingArray[1] = closedHashSet;
        comparingArray[2] = new CollectionFacadeSet(treeHashSet);
        comparingArray[3] = new CollectionFacadeSet(linkedList);
        comparingArray[4] = new CollectionFacadeSet(treeHashSet);
        this.toProcess = data;
    }

    //Methods:

    private long addAllTiming(SimpleSet object) {
        long timeBefore = System.nanoTime();
        for (int arrayObj = 0; arrayObj < toProcess.length; arrayObj++) {
            object.add(toProcess[arrayObj]);
        }
        long difference = System.nanoTime() - timeBefore;
        return (difference / 1000000);
    }

    private long[] firstAndSecondTest() {
        long[] resultsArray = new long[comparingArray.length];
        for (int arrayIdx = 0; arrayIdx < comparingArray.length; arrayIdx++) {
            resultsArray[arrayIdx] = addAllTiming(comparingArray[arrayIdx]);
        }
        return resultsArray;
    }

    private long containsTiming(SimpleSet object, String newValue) {
        long timeBefore = System.nanoTime();
        object.contains(newValue);
        return System.nanoTime() - timeBefore;
    }

    private long[] allOtherTests(String newValue) {
        long[] resultsArray = new long[comparingArray.length];
        for (int arrayIdx = 0; arrayIdx < comparingArray.length; arrayIdx++) {
            resultsArray[arrayIdx] = containsTiming(comparingArray[arrayIdx], newValue);
        }
        return resultsArray;
    }
    public void runAndPrintTests(){
        System.out.println("First or Second test output:");
        System.out.println(java.util.Arrays.toString(this.firstAndSecondTest()));
        System.out.println("Third or Sixth test output:");
        System.out.println(java.util.Arrays.toString(this.allOtherTests("hi")));
        System.out.println("Fourth test output:");
        System.out.println(java.util.Arrays.toString(this.allOtherTests("-13170890158")));
        System.out.println("Fifth test output:");
        System.out.println(java.util.Arrays.toString(this.allOtherTests("23")));
    }
    public static void main(String[] args) {
        String openHashSetType = args[0];
        OpenHashSet openHashSet = new OpenHashSet();
        String closedHashSetType = args[1];
        ClosedHashSet closedHashSet = new ClosedHashSet();
        String fileName = args[2];
        String[] data = Ex3Utils.file2array(fileName);
        if (openHashSetType.equals("data")){
            openHashSet = new OpenHashSet(data);
        }     if (closedHashSetType.equals("data")) {
            closedHashSet = new ClosedHashSet(data);
        }
        TreeSet<String> treeSet = new TreeSet<String>();
        LinkedList<String> linkedList = new LinkedList<String>();
        HashSet<String> hashSet = new HashSet<String>();
        SimpleSetPerformanceAnalyzer myAnalyzer = new
                SimpleSetPerformanceAnalyzer(openHashSet, closedHashSet,treeSet, linkedList, hashSet, data);

        System.out.println("Test initialized with: "+fileName);
        myAnalyzer.runAndPrintTests();






    }
}