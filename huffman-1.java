import java.util.*;
import tester.*;

// represents a huffman code tree
class Huffman {
  ArrayList<String> strArr;
  ArrayList<Integer> intArr;
  Node root;
  
  // constructs a huffman, which takes 2 arrays and makes the tree
  Huffman(ArrayList<String> strArr, ArrayList<Integer> intArr) {
    
    // check if both arrays are not same size, throw exception
    if (strArr.size() != intArr.size()) {
      throw new IllegalArgumentException("Array sizes are not equal");
    }
    
    // check if the arrays are less than 2 in size, throw exception
    if (strArr.size() < 2) {
      throw new IllegalArgumentException("Array sizes are too small");
    }
    
    this.strArr = strArr;
    this.intArr = intArr;
    
    // build the tree with the given arrays
    buildTree();
    
  }
  
  // builds a tree using both arrays
  public void buildTree() {
    ArrayList<Node> tree = new ArrayList<>();
    
    // create an ArrayList of nodes that has all the information
    for (int i = 0; i < this.strArr.size(); i++) {
      tree.add(new Node(this.strArr.get(i), this.intArr.get(i)));
    }
    
    // sort the full tree using insertion sort
    iSort(tree);
    
    // build the nodes up into the full tree
    while (tree.size() > 1) {
      
      // get the smallest two freq nodes
      Node zero = tree.remove(0);
      Node one = tree.remove(0);
      
      // create and add a new parent node with the right children
      Node parent = new Node(zero, one);
      tree.add(parent);
      
      // sort the tree again
      iSort(tree);
    }
    
    // set the root to the tree's only node
    this.root = tree.get(0);
  }
  
  // insertion sort for a arraylist of nodes
  public void iSort(ArrayList<Node> tree) {
    for (int i = 1; i < tree.size(); i++) {
      Node key = tree.get(i);
      int j = i - 1;
      
      // get the index of where the key node is the smallest
      while (j >= 0 && tree.get(j).freq > key.freq) {
        j--;
      }
      
      // remove the key
      tree.remove(i);
      
      // add the key back in the new location
      tree.add(j + 1, key);
    }
  }
  
  // encodes a given string using the huffman code
  public ArrayList<Boolean> encode(String input) {
    ArrayList<Boolean> ans = new ArrayList<>();
    ArrayList<Character> charArr = new ArrayList<>();
    
    // make the input into an array of char
    for (int i = 0; i < input.length(); i++) {
      charArr.add(input.charAt(i));
    }
    
    for (char c : charArr) {
      
      // create and get the binary code of a char
      String binaryCode = "";
      
      // get the code of the given char
      binaryCode = getCode(this.root, c, "");
      
      // if it is null, return null;
      if (binaryCode == null) {
        throw new IllegalArgumentException("Tried to encode " 
            + c +  " but that is not part of the language.");
      }
      
      // make the boolean arraylist with the new code
      for (int i = 0; i < binaryCode.length(); i++) {
        ans.add(binaryCode.charAt(i) != '0');
      }
    }
    
    return ans;
  }
  
  // a depth first search for char c
  public String getCode(Node node, char c, String ans) {
    
    // if the node is null, then
    if (node == null) {
      return null;
    }
    
    // if the node contains the letter, return the code finished code
    if (node.letter != null && node.letter.charAt(0) == c) {
      return ans;
    }
    
    // go to the zero node, if null then go to right node
    String leftAns = getCode(node.zero, c, ans + '0');
    if (leftAns != null) {
      return leftAns;
    }
    
    // go to the one node after the left one
    return getCode(node.one, c, ans + '1');
  }
  
  // decodes a given arraylist of boolean using the huffman code tree
  public String decode(ArrayList<Boolean> encoded) {
    String ans = "";
    Node currentNode = this.root;
    
    // depending on the true or false, use the letter on the left or right
    for (int i = 0; i < encoded.size(); i++) {
      if (!encoded.get(i)) {
        currentNode = currentNode.zero;
      } else {
        currentNode = currentNode.one;
      }
      
      // if the node has a letter, add it to the answer then reset the current node
      if (currentNode.letter != null) {
        ans += currentNode.letter;
        currentNode = this.root;
      }
      
      // if we are in a node that is outside the tree, add the question mark
      if (currentNode != null && currentNode.letter == null 
          && i == encoded.size() - 1 && !currentNode.equals(this.root)) {
        ans += '?';
      }
    }
    return ans;
  }
  
}

// represents a single node or tree of nodes
class Node {
  String letter;
  int freq;
  Node zero;
  Node one;
  
  // represents a node that has a letter, essentially no children nodes
  Node(String letter, int freq) {
    this.letter = letter;
    this.freq = freq;
  }
  
  // represents a parent node, without a letter
  Node(Node zero, Node one) {
    this.letter = null;
    this.freq = zero.freq + one.freq;
    this.zero = zero;
    this.one = one;
  }
}

// represents examples of huffmans, string arrays, node arrays, and tests
class ExamplesHuffmans {
  
  // given example in problem
  ArrayList<String> strArr1 = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f"));
  ArrayList<Integer> intArr1 = new ArrayList<>(Arrays.asList(12, 45, 5, 13, 9, 16));
  
  // different example with some equal freqs and letters
  ArrayList<String> strArr2 = new ArrayList<>(Arrays.asList("a", "b", "c", "e", "e", "f"));
  ArrayList<Integer> intArr2 = new ArrayList<>(Arrays.asList(45, 45, 5, 13, 9, 9));
  
  // examples with an arraylist of nodes
  ArrayList<Node> nodeArr1 = new ArrayList<Node>(Arrays.asList(new Node("a", 12), 
      new Node("b", 45), new Node("c", 5), new Node("d", 13), 
      new Node("e", 9), new Node("f", 16)));
  ArrayList<Node> nodeArr2 = new ArrayList<Node>(Arrays.asList(new Node("a", 45), 
      new Node("b", 45), new Node("c", 5), new Node("e", 13), 
      new Node("e", 9), new Node("f", 9)));
  
  // huffmans for both examples
  Huffman h1;
  Huffman h2;
  
  // initializes the examples
  void init() {
    this.h1 = new Huffman(this.strArr1, this.intArr1);
    this.h2 = new Huffman(this.strArr2, this.intArr2);
    this.nodeArr1 = new ArrayList<Node>(Arrays.asList(new Node("a", 12), new Node("b", 45), 
        new Node("c", 5), new Node("d", 13), new Node("e", 9), new Node("f", 16)));
    this.nodeArr2 = new ArrayList<Node>(Arrays.asList(new Node("a", 45), new Node("b", 45), 
        new Node("c", 5), new Node("e", 13), new Node("e", 9), new Node("f", 9)));
  }
  
  // resets the examples so mutation is testable
  void reset() {
    this.h1 = null;
    this.h2 = null;
    this.nodeArr1 = null;
    this.nodeArr2 = null;
  }
  
  // tests huffman constructor exceptions
  void testHuffmanCons(Tester t) {
    
    // initialize the examples
    init();
    
    // tests array sizes that are not equal
    t.checkConstructorException(new IllegalArgumentException("Array sizes are not equal"), 
        "Huffman", 
        new ArrayList<String>(Arrays.asList("a", "b", "c", "d")), 
        new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
    
    // tests array sizes that are too short
    t.checkConstructorException(new IllegalArgumentException("Array sizes are too small"), 
        "Huffman", 
        new ArrayList<String>(Arrays.asList("a")), 
        new ArrayList<Integer>(Arrays.asList(1)));
  }
  
  // tests buildTree using initialize
  void testBuildTree(Tester t) {
    
    // reset the huffman examples to null
    reset();
    
    // check that huffman is null
    t.checkExpect(this.h1, null);
    t.checkExpect(this.h2, null);
    
    // use init to use buildTree
    init();
    
    // replicate the h1 and h2 tree
    Node repH1 = new Node(new Node("b", 45), new Node(new Node(new Node("a", 12), 
        new Node("d", 13)), new Node(new Node(new Node("c", 5), 
            new Node("e", 9)), new Node("f", 16))));
    Node repH2 = new Node(new Node("b", 45), new Node(new Node(new Node(new Node("c", 5), 
        new Node("e", 9)), new Node(new Node("f", 9), 
            new Node("e", 13))), new Node("a", 45)));
    
    // test if new root has expected structure and values
    t.checkExpect(this.h1.root, repH1);
    t.checkExpect(this.h2.root, repH2);
    
  }
  
  // test insertion sort by mutating arraylists of nodes
  void testISort(Tester t) {
    
    // reset examples
    reset();
    
    // initialize examples
    init();
    
    // test unsorted nodes
    t.checkExpect(this.nodeArr1, new ArrayList<Node>(Arrays.asList(new Node("a", 12), 
        new Node("b", 45), new Node("c", 5), new Node("d", 13), 
        new Node("e", 9), new Node("f", 16))));
    t.checkExpect(this.nodeArr2, new ArrayList<Node>(Arrays.asList(new Node("a", 45), 
        new Node("b", 45), new Node("c", 5), new Node("e", 13), 
        new Node("e", 9), new Node("f", 9))));
    
    // sort first array
    this.h1.iSort(this.nodeArr1);
    
    // test the sorted Arr1
    t.checkExpect(this.nodeArr1, new ArrayList<Node>(Arrays.asList(new Node("c", 5), 
        new Node("e", 9), new Node("a", 12), new Node("d", 13), 
        new Node("f", 16), new Node("b", 45))));
    
    // sort second array
    this.h1.iSort(this.nodeArr2);
    
    // test the sorted Arr2
    t.checkExpect(this.nodeArr2, new ArrayList<Node>(Arrays.asList(new Node("c", 5), 
        new Node("e", 9), new Node("f", 9), new Node("e", 13), 
        new Node("a", 45), new Node("b", 45))));
    
  }
  
  // tests encode method
  void testEncode(Tester t) {
    
    // reset examples
    reset();
    
    // initialize examples
    init();
    
    // test regular case
    t.checkExpect(this.h1.encode("abc"), new ArrayList<Boolean>(Arrays.asList(true,
        false, false, false, true, true, false, false)));
    
    // test case with letter that isn't in nodes
    t.checkException(new IllegalArgumentException("Tried to "
        + "encode z but that is not part of the language."), 
        this.h1, "encode", "abz");
    
    // test case with duplicate letters
    t.checkExpect(this.h1.encode("aaa"), new ArrayList<Boolean>(Arrays.asList(true,
        false, false, true, false, false, true, false, false)));
    
    // test case with a lot of letters
    t.checkExpect(this.h1.encode("abcdefabcdef"), new ArrayList<Boolean>(Arrays.asList(true,
        false, false, false, true, true, false, false, true, false, true, true, true, false, 
        true, true, true, true, true, false, false, false, true, true, false, false, true, false,
        true, true, true, false, true, true, true, true)));
  }
  
  // test getCode method
  void testGetCode(Tester t) {
    
    // reset examples
    reset();
    
    // initialize examples
    init();
    
    // test all letters in h1
    t.checkExpect(this.h1.getCode(this.h1.root, 'a', ""), "100");
    t.checkExpect(this.h1.getCode(this.h1.root, 'b', ""), "0");
    t.checkExpect(this.h1.getCode(this.h1.root, 'c', ""), "1100");
    t.checkExpect(this.h1.getCode(this.h1.root, 'd', ""), "101");
    t.checkExpect(this.h1.getCode(this.h1.root, 'e', ""), "1101");
    t.checkExpect(this.h1.getCode(this.h1.root, 'f', ""), "111");
    
    // test all letters in h2
    t.checkExpect(this.h2.getCode(this.h2.root, 'a', ""), "11");
    t.checkExpect(this.h2.getCode(this.h2.root, 'b', ""), "0");
    t.checkExpect(this.h2.getCode(this.h2.root, 'c', ""), "1000");
    t.checkExpect(this.h2.getCode(this.h2.root, 'e', ""), "1001");
    t.checkExpect(this.h2.getCode(this.h2.root, 'f', ""), "1010");
    
  }
  
  // test decode method
  void testDecode(Tester t) {
    
    // reset examples
    reset();
    
    // initialize examples
    init();
    
    // test regular case
    t.checkExpect(this.h1.decode(new ArrayList<Boolean>(Arrays.asList(true,
        false, false, false, true, true, false, false))), "abc");
    
    // test case with binary that leads out of the tree
    t.checkExpect(this.h1.decode(new ArrayList<Boolean>(Arrays.asList(false, true, false))), "b?");
    
    // test case with duplicate letters
    t.checkExpect(this.h1.decode(new ArrayList<Boolean>(Arrays.asList(true, false, false, 
        true, false, false, true, false, false))), "aaa");
    
    // test case with a lot of letters
    t.checkExpect(this.h1.decode(new ArrayList<Boolean>(Arrays.asList(true, false, false, 
        false, true, true, false, false, true, false, true, true, true, false, true, true, 
        true, true, true, false, false, false, true, true, false, false, true, false, 
        true, true, true, false, true, true, true, true))), "abcdefabcdef");
    
    
  }
}
