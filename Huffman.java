import java.util.*;
import tester.*;

// represents a huffman code tree
class Huffman {
  ArrayList<String> strArr;
  ArrayList<Integer> intArr;
  INode root;

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
    ArrayList<INode> tree = new ArrayList<>();

    // create an ArrayList of nodes that has all the information
    for (int i = 0; i < this.strArr.size(); i++) {
      tree.add(new LeafNode(this.strArr.get(i), this.intArr.get(i)));
    }

    // sort the full tree using insertion sort
    iSort(tree);

    // build the nodes up into the full tree
    while (tree.size() > 1) {

      // get the smallest two freq nodes
      INode zero = tree.remove(0);
      INode one = tree.remove(0);

      // create and add a new parent node with the right children
      TotalNode parent = new TotalNode(zero, one);
      tree.add(parent);

      // sort the tree again
      iSort(tree);
    }

    // set the root to the tree's only node
    this.root = tree.get(0);
  }

  // encodes a given string using the huffman code
  public ArrayList<Boolean> encode(String input) {
    ArrayList<Boolean> ans = new ArrayList<>();
    ArrayList<Character> charArr = new ArrayList<>();

    // make the input into an array of char
    for (int i = 0; i < input.length(); i++) {
      charArr.add(input.charAt(i));
    }

    for (char ch : charArr) {

      // create and get the binary code of a char
      String binaryCode = "";

      // get the code of the given char
      binaryCode = this.root.findCode(ch, "");

      // if it is null, return null;
      if (binaryCode.length() == 0) {
        throw new IllegalArgumentException(
            "Tried to encode " + ch + " but that is not part of the language.");
      }

      // make the boolean arraylist with the new code
      for (int i = 0; i < binaryCode.length(); i++) {
        ans.add(binaryCode.charAt(i) != '0');
      }
    }

    return ans;
  }

  // Decodes a given ArrayList of booleans using the Huffman code tree
  public String decode(ArrayList<Boolean> encoded) {
    String decoded = "";
    String remainingBits = "";

    // Convert the list of booleans to a string of '0' and '1'
    for (Boolean bit : encoded) {
      if (bit) {
        remainingBits += "1";
      }
      else {
        remainingBits += "0";
      }
    }

    // Decode the bit string one character at a time using findChar
    while (remainingBits.length() != 0) {
      String decodedChar = this.root.findChar(remainingBits);

      if (decodedChar.length() == 0) {
        // If findChar returns an empty string, we have leftover bits that do not decode
        // to a character
        decoded += "?";
        break;
      }
      else {
        // Append the decoded character and remove the corresponding bits
        decoded += decodedChar;
        remainingBits = remainingBits
            .substring(this.root.findCode(decodedChar.charAt(0), "").length());
      }
    }

    return decoded;
  }

  // insertion sort for a arraylist of nodes
  public void iSort(ArrayList<INode> tree) {
    for (int i = 1; i < tree.size(); i++) {
      INode key = tree.get(i);
      int j = i - 1;

      // get the index of where the key node is the smallest
      while (j >= 0 && tree.get(j).greaterThan(key)) {
        j--;
      }

      // remove the key
      tree.remove(i);

      // add the key back in the new location
      tree.add(j + 1, key);
    }
  }
}

interface INode {

  //
  Integer combineFreq(INode that);

  //
  Integer combineFreq(LeafNode thatLeaf);

  //
  Integer combineFreq(TotalNode thatLeaf);

  //
  String findCode(char ch, String ans);

  //
  String findChar(String codeLeft);

  //
  Boolean greaterThan(INode that);

  //
  Boolean greaterThan(LeafNode that);

  //
  Boolean greaterThan(TotalNode that);
}

// represents a single node or tree of nodes
class LeafNode implements INode {
  String letter;
  int freq;

  // represents a node that has a letter, essentially no children nodes
  LeafNode(String letter, int freq) {
    this.letter = letter;
    this.freq = freq;
  }

  //
  public Integer combineFreq(INode that) {
    return that.combineFreq(this);
  }

  //
  public Integer combineFreq(LeafNode thatLeaf) {
    return this.freq + thatLeaf.freq;
  }

  //
  public Integer combineFreq(TotalNode thatTotal) {
    return this.freq + thatTotal.left.combineFreq(thatTotal.right);
  }

  // a depth first search for char c
  public String findCode(char ch, String codeAcc) {

    // if the node contains the letter, return the code finished code
    if (this.letter.charAt(0) == ch) {
      return codeAcc;
    }
    return "";
  }

  public String findChar(String codeLeft) {
    return this.letter;
  }

  // REPRESENTS
  public Boolean greaterThan(INode that) {
    return that.greaterThan(this);
  }

  // REPRESENTS
  public Boolean greaterThan(LeafNode that) {
    return this.freq < that.freq;
  }

  // REPRESENTS
  public Boolean greaterThan(TotalNode that) {
    return this.freq < that.freq;
  }
}

//represents a single node or tree of nodes
class TotalNode implements INode {
  int freq;
  INode left;
  INode right;

  // represents a parent node, without a letter
  TotalNode(INode left, INode right) {
    this.left = left;
    this.right = right;
    this.freq = left.combineFreq(right);
  }

  // REPRESENTS
  public Integer combineFreq(INode that) {
    return that.combineFreq(this);
  }

  // REPRESENTS
  public Integer combineFreq(LeafNode thatLeaf) {
    return this.freq;
  }

  // REPRESENTS
  public Integer combineFreq(TotalNode thatTotal) {
    return this.left.combineFreq(this.right) + thatTotal.left.combineFreq(thatTotal.right);
  }

  // a depth first search for char c
  public String findCode(char ch, String ans) {
    // add 0 to accumulated answer if left and 1 if going right
    String left = this.left.findCode(ch, ans + "0");
    String right = this.right.findCode(ch, ans + "1");

    if (left.length() == 0) {
      return right;
    }
    else {
      return left;
    }

    // return this.left.findCode(ch, ans + '0') + this.right.findCode(ch, ans +
    // '1');
  }

  //
  public String findChar(String codeLeft) {
    if (codeLeft.length() == 0) {
      return "";
    }

    String nextCode = codeLeft.substring(1);

    if (codeLeft.charAt(0) == '0') {
      return this.left.findChar(nextCode);
    }
    else {
      return this.right.findChar(nextCode);
    }
  }

  //
  public Boolean greaterThan(INode that) {
    return that.greaterThan(this);
  }

  //
  public Boolean greaterThan(LeafNode that) {
    return this.freq < that.freq;
  }

  //
  public Boolean greaterThan(TotalNode that) {
    return this.freq < that.freq;
  }
}

// represents examples of huffmans, string arrays, node arrays, and tests
class ExamplesHuffman {

  // given example in problem
  ArrayList<String> strArr1 = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f"));
  ArrayList<Integer> intArr1 = new ArrayList<>(Arrays.asList(12, 45, 5, 13, 9, 16));

  // different example with some equal freqs and letters
  ArrayList<String> strArr2 = new ArrayList<>(Arrays.asList("a", "b", "c", "e", "e", "f"));
  ArrayList<Integer> intArr2 = new ArrayList<>(Arrays.asList(45, 45, 5, 13, 9, 9));

  // examples with an arraylist of nodes
  ArrayList<INode> nodeArr1 = new ArrayList<INode>(
      Arrays.asList(new LeafNode("a", 12), new LeafNode("b", 45), new LeafNode("c", 5),
          new LeafNode("d", 13), new LeafNode("e", 9), new LeafNode("f", 16)));
  ArrayList<INode> nodeArr2 = new ArrayList<INode>(
      Arrays.asList(new LeafNode("a", 45), new LeafNode("b", 45), new LeafNode("c", 5),
          new LeafNode("e", 13), new LeafNode("e", 9), new LeafNode("f", 9)));

  // huffmans for both examples
  Huffman h1;
  Huffman h2;

  // initializes the examples
  void init() {
    this.h1 = new Huffman(this.strArr1, this.intArr1);
    this.h2 = new Huffman(this.strArr2, this.intArr2);
    this.nodeArr1 = new ArrayList<INode>(Arrays.asList(new LeafNode("a", 12), new LeafNode("b", 45),
        new LeafNode("c", 5), new LeafNode("d", 13), new LeafNode("e", 9), new LeafNode("f", 16)));
    this.nodeArr2 = new ArrayList<INode>(Arrays.asList(new LeafNode("a", 45), new LeafNode("b", 45),
        new LeafNode("c", 5), new LeafNode("e", 13), new LeafNode("e", 9), new LeafNode("f", 9)));
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
        "Huffman", new ArrayList<String>(Arrays.asList("a", "b", "c", "d")),
        new ArrayList<Integer>(Arrays.asList(1, 2, 3)));

    // tests array sizes that are too short
    t.checkConstructorException(new IllegalArgumentException("Array sizes are too small"),
        "Huffman", new ArrayList<String>(Arrays.asList("a")),
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
    TotalNode repH1 = new TotalNode(new LeafNode("b", 45),
        new TotalNode(new TotalNode(new LeafNode("a", 12), new LeafNode("d", 13)), new TotalNode(
            new TotalNode(new LeafNode("c", 5), new LeafNode("e", 9)), new LeafNode("f", 16))));

    TotalNode repH2 = new TotalNode(new LeafNode("b", 45),
        new TotalNode(
            new TotalNode(new TotalNode(new LeafNode("c", 5), new LeafNode("e", 9)),
                new TotalNode(new LeafNode("f", 9), new LeafNode("e", 13))),
            new LeafNode("a", 45)));

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
    t.checkExpect(this.nodeArr1,
        new ArrayList<LeafNode>(
            Arrays.asList(new LeafNode("a", 12), new LeafNode("b", 45), new LeafNode("c", 5),
                new LeafNode("d", 13), new LeafNode("e", 9), new LeafNode("f", 16))));
    t.checkExpect(this.nodeArr2,
        new ArrayList<LeafNode>(
            Arrays.asList(new LeafNode("a", 45), new LeafNode("b", 45), new LeafNode("c", 5),
                new LeafNode("e", 13), new LeafNode("e", 9), new LeafNode("f", 9))));

    // sort first array
    this.h1.iSort(this.nodeArr1);

    // test the sorted Arr1
    t.checkExpect(this.nodeArr1,
        new ArrayList<LeafNode>(
            Arrays.asList(new LeafNode("c", 5), new LeafNode("e", 9), new LeafNode("a", 12),
                new LeafNode("d", 13), new LeafNode("f", 16), new LeafNode("b", 45))));

    // sort second array
    this.h1.iSort(this.nodeArr2);

    // test the sorted Arr2
    t.checkExpect(this.nodeArr2,
        new ArrayList<LeafNode>(
            Arrays.asList(new LeafNode("c", 5), new LeafNode("e", 9), new LeafNode("f", 9),
                new LeafNode("e", 13), new LeafNode("a", 45), new LeafNode("b", 45))));

  }

  // tests encode method
  void testEncode(Tester t) {

    // reset examples
    reset();

    // initialize examples
    init();

    // test regular case
    t.checkExpect(this.h1.encode("abc"),
        new ArrayList<Boolean>(Arrays.asList(true, false, false, false, true, true, false, false)));

    // test case with letter that isn't in nodes
    t.checkException(
        new IllegalArgumentException("Tried to encode z but that is not part of the language."),
        this.h1, "encode", "abz");

    // test case with duplicate letters
    t.checkExpect(this.h1.encode("aaa"), new ArrayList<Boolean>(
        Arrays.asList(true, false, false, true, false, false, true, false, false)));

    // test case with a lot of letters
    t.checkExpect(this.h1.encode("abcdefabcdef"),
        new ArrayList<Boolean>(
            Arrays.asList(true, false, false, false, true, true, false, false, true, false, true,
                true, true, false, true, true, true, true, true, false, false, false, true, true,
                false, false, true, false, true, true, true, false, true, true, true, true)));
  }

  // test findCode method
  void testFindCode(Tester t) {

    // reset examples
    reset();

    // initialize examples
    init();

    // test all letters in h1
    t.checkExpect(this.h1.root.findCode('a', ""), "100");
    t.checkExpect(this.h1.root.findCode('b', ""), "0");
    t.checkExpect(this.h1.root.findCode('c', ""), "1100");
    t.checkExpect(this.h1.root.findCode('d', ""), "101");
    t.checkExpect(this.h1.root.findCode('e', ""), "1101");
    t.checkExpect(this.h1.root.findCode('f', ""), "111");

    // test all letters in h2
    t.checkExpect(this.h2.root.findCode('a', ""), "11");
    t.checkExpect(this.h2.root.findCode('b', ""), "0");
    t.checkExpect(this.h2.root.findCode('c', ""), "1000");
    t.checkExpect(this.h2.root.findCode('e', ""), "1001");
    t.checkExpect(this.h2.root.findCode('f', ""), "1010");

  }
  
//test findCode method
 void testFindChar(Tester t) {

   // reset examples
   reset();

   // initialize examples
   init();

   // test all letters in h1
   t.checkExpect(this.h1.root.findChar("100"), "a");
   t.checkExpect(this.h1.root.findChar("0"), "b");
   t.checkExpect(this.h1.root.findChar("1100"), "c");
   t.checkExpect(this.h1.root.findChar("101"), "d");
   t.checkExpect(this.h1.root.findChar("1101"), "e");
   t.checkExpect(this.h1.root.findChar("111"), "f");

   // test all letters in h2
   t.checkExpect(this.h2.root.findChar("11"), "a");
   t.checkExpect(this.h2.root.findChar("0"), "b");
   t.checkExpect(this.h2.root.findChar("1000"), "c");
   t.checkExpect(this.h2.root.findChar("1001"), "e");
   t.checkExpect(this.h2.root.findChar("1010"), "f");

 }

  // test decode method
  void testDecode(Tester t) {

    // reset examples
    reset();

    // initialize examples
    init();

    // test regular case
    t.checkExpect(this.h1.decode(
        new ArrayList<Boolean>(Arrays.asList(true, false, false, false, true, true, false, false))),
        "abc");

    // test case with binary that leads out of the tree
    t.checkExpect(this.h1.decode(new ArrayList<Boolean>(Arrays.asList(false, true, false))), "b?");

    // test case with duplicate letters
    t.checkExpect(this.h1.decode(new ArrayList<Boolean>(
        Arrays.asList(true, false, false, true, false, false, true, false, false))), "aaa");

    // test case with a lot of letters
    t.checkExpect(
        this.h1.decode(new ArrayList<Boolean>(
            Arrays.asList(true, false, false, false, true, true, false, false, true, false, true,
                true, true, false, true, true, true, true, true, false, false, false, true, true,
                false, false, true, false, true, true, true, false, true, true, true, true))),
        "abcdefabcdef");

  }
}
