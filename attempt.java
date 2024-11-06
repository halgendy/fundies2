import tester .*;
import java.util.ArrayList;
import java.util.Arrays;

// REPRESENTS
class Huffman {
  ArrayList<String> arr1;
  ArrayList<Integer> arr2;
  ILeaf root;
  
  //CONSTRUCTOR
  Huffman(ArrayList<String> arr1, ArrayList<Integer> arr2) {
    
    // ENSURE
    if (arr1.size() != arr2.size()) {
      throw new IllegalArgumentException("ARRAYS MUST BE SAME LENGTH");
    }
    
    // ENSURE
    if (arr1.size() < 2) {
      throw new IllegalArgumentException("ARRAYS MUST HAVE MORE THAN ONE ELEMENT");
    }
    
    this.arr1 = arr1;
    this.arr2 = arr2;
    
    // Call upon to build the tree
    buildForest();
  }
  
  void buildForest() {
    this.root = new HuffmanLeaf(this.arr1.get(0), this.arr2.get(0));
  }
}


// REPRESENTS
interface ILeaf {
  
}

// REPRESENTS
class HuffmanLeaf implements ILeaf {
  String character;
  int frequency;
  
  //CONSTRUCTOR
  HuffmanLeaf(String character, int frequency) {
    this.character = character;
    this.frequency = frequency;
  }
  
}

// REPRESENTS
class TotalLeaf implements ILeaf{
  int total;
  ILeaf left;
  ILeaf right;
  
  //CONSTRUCTOR
  TotalLeaf(int total, ILeaf left, ILeaf right) {
    this.total = total;
    this.left = left;
    this.right = right;
  }
  
}

// REPRESENTS
class ExamplesHuffman {
  
  ArrayList<Integer> arr = new ArrayList<Integer>();
  
  ArrayList<Integer> arr0 = new ArrayList<Integer>(Arrays.asList(0));
  
  Huffman<Integer> h = new Huffman<Integer>(this.arr, this.arr);
  
  // ENSURES
  boolean testNameGoesHere(Tester t) {
    // TESTING
    return t.checkExpect(true , true)
    // TESTING
    && t.checkExpect(false , false)
    // TESTING
    && t.checkExpect(true , false)
    //TESTING
    && t.checkExpect(false , true);
  }

}
