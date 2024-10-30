import java.util.*;
import java.util.function.Predicate;
import tester.*;

// Abstract class for ANode
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;
  
  //
  abstract T remove();
  
  //
  void relink(Node<T> nextLink, Node<T> prevLink) {
    if (nextLink != null) {
      this.next = prevLink;
    }
    if (prevLink != null) {
      this.prev = nextLink;
    }
  }
  
}

// Class for Sentinel node
class Sentinel<T> extends ANode<T> {

  // sentinel constructor, with a next and previous node
  public Sentinel() {
    this.next = this;
    this.prev = this;
  }
  
  //
  T remove() {
    throw new RuntimeException("Deque is empty");
  }
}

// Class for Node containing data
class Node<T> extends ANode<T> {
  T data;

  // Constructor for Node with data included
  public Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  // Convenience constructor for Node with data and links
  public Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("next and prev cannot null");
    }
    
    this.data = data;
    this.next = next;
    this.prev = prev;
    
    next.relink(null, this);
    prev.relink(this, null); 
  }
  
  //
  T remove() {
    this.prev = null;
    this.next = null;
    
    return this.data;
  }
  
  // 
  void relink(Node<T> nextLink, Node<T> prevLink) {
    if (nextLink != null) {
      this.next = prevLink;
    }
    if (prevLink != null) {
      this.prev = nextLink;
    }
  }
}

// Class for Deque
class Deque<T> {
  Sentinel<T> header;

  // Constructor for Deque
  public Deque() {
    this.header = new Sentinel<>();
  }

  // Convenience constructor for Deque
  public Deque(Sentinel<T> header) {
    this.header = header;
  }

  // Method to get the size of the Deque
  public int size() {
    int count = 0;
    ANode<T> current = header.next;
    while (!current.equals(header)) {
      count++;
      current = current.next;
    }
    return count;
  }

  // Method to add an item at the head
  public void addAtHead(T value) {
    Node<T> newNode = new Node<>(value, header.next, header);
  }

  // Method to add an item at the tail
  public void addAtTail(T value) {
    ANode<T> newNode = new Node<>(value, header, header.prev);
  }

  // Method to remove an item from the head
  public T removeFromHead() {
    return header.next.remove();  
  }

  // Method to remove an item from the tail
  public T removeFromTail() {
    //T removed = header.prev.removeFromTailHelper();
    //header.prev = header.prev.prev;
    return header.prev.remove();
  }

  // Method to find a node based on a predicate
  public ANode<T> find(Predicate<T> predicate) {
    ANode<T> current = header.next;
    while (current != header) {
      if (predicate.test(((Node<T>) current).data)) {
        return current;
      }
      current = current.next;
    }
    return header; // Return header if not found
  }
}

// Example class to test the Deque
class ExamplesDeques {
  Deque<String> deque1 = new Deque<>(); // Empty list
  Deque<String> deque2 = new Deque<>(); // List with values
  Deque<String> deque3 = new Deque<>(); // Self made list

  // initializes all the deques
  void InitDeques() {

    // deque2 given examples
    this.deque2.addAtTail("abc");
    this.deque2.addAtTail("bcd");
    this.deque2.addAtTail("cde");
    this.deque2.addAtTail("def");

    // deque3 personal examples
    this.deque3.addAtTail("Zweihandler");
    this.deque3.addAtTail("Arbalest");
    this.deque3.addAtTail("Morning Star");
    this.deque3.addAtTail("Caestus");
    this.deque3.addAtTail("Splitleaf GS");
  }

  void ResetDeques() {
    this.deque1 = new Deque<>(); // Empty list
    this.deque2 = new Deque<>(); // List with values
    this.deque3 = new Deque<>(); // Self made list
  }

  boolean testSize(Tester t) {

    // resets the deque
    ResetDeques();

    // initializes the deque nodes
    InitDeques();

    return t.checkExpect(this.deque1.size(), 0) 
        && t.checkExpect(this.deque2.size(), 4)
        && t.checkExpect(this.deque3.size(), 5);
  }

  void testAddAtHead(Tester t) {
    
    // resets the deque
    ResetDeques();
    
    // initializes the deque nodes
    InitDeques();
    
    // check that the empty deque is empty
    t.checkExpect(this.deque1, new Deque<>());
    
    // add something at head
    deque1.addAtHead("xyz");
    
    // check if size increased by one
    t.checkExpect(this.deque1.size(), 1);
  }

//  boolean testAddAtTail(Tester t) {
//    System.out.println("Size of deque1: " + deque1.size()); // Should print 0  
//    System.out.println("Size of deque2: " + deque2.size()); // Should print 4  
//  }

  boolean testRemoveFromHead(Tester t) {
    
    // resets the deque
    ResetDeques();

    // initializes the deque nodes
    InitDeques();
    
    Deque<String> deque2Rh = new Deque<>();
    
    deque2Rh.addAtTail("bcd");
    deque2Rh.addAtTail("cde");
    deque2Rh.addAtTail("def");
    
    this.deque2.removeFromHead();
    
    return t.checkExpect(this.deque2, deque2Rh);
    
    //deque1.addAtHead("xyz");
    //System.out.println("Removed from deque1: " + deque1.removeFromHead()); // Should print "xyz"  
    //System.out.println("Size of deque1 after removal: " + deque1.size()); // Should print 0  
  }

//  boolean testRemoveFromTail(Tester t) {
//    System.out.println("Size of deque1: " + deque1.size()); // Should print 0  
//    System.out.println("Size of deque2: " + deque2.size()); // Should print 4  
//  }
//
//  boolean testFind(Tester t) {
//    deque1.addAtHead("xyz");
//    System.out.println("Removed from deque1: " + deque1.removeFromHead()); // Should print "xyz"  
//    System.out.println("Size of deque1 after removal: " + deque1.size()); // Should print 0  
//  }
}
