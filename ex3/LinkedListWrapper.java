import java.util.Iterator;
import java.util.LinkedList;

/**
* this is a linkedListWrapper Class that wraps the linkedList class
 *  */
public class LinkedListWrapper{
    LinkedList<String> linkedList;


    public LinkedListWrapper(){
        //initializations:
        this.linkedList = new LinkedList<String>();
    }
    public boolean myContains(java.lang.String searchVal){
        return linkedList.contains(searchVal);
    }
    public boolean myDelete(java.lang.String toDelete){
        return linkedList.remove(toDelete);
    }

    public boolean myAdd(java.lang.String newValue){
        return linkedList.add(newValue);
    }
    public Iterator<String> iterator(){
        return linkedList.iterator();
    }
    public int size(){return linkedList.size();}
    public void printLinkedList(){
            System.out.println(linkedList.toString());
    }

}
