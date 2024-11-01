import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import tester.*;

// represents a pair of two general types
class Pair<L, R> {
  L left;
  R right;

  Pair(L left, R right) {
    this.left = left;
    this.right = right;
  }
  
}

//
class ZipStrictIterator<X, Y> implements Iterator<Pair<X, Y>> {
  
  // define variables
  ArrayList<X> arr1;
  ArrayList<Y> arr2;
  int index;
  
  //
  public ZipStrictIterator(ArrayList<X> arr1, ArrayList<Y> arr2){
    this.arr1 = arr1;
    this.arr2 = arr2;
    this.index = 0;
  }
  
  //
  @Override
  public boolean hasNext() {
    return index < arr1.size();
  }

  //
  @Override
  public Pair<X, Y> next() {
    if (!this.hasNext()) {
      throw new NoSuchElementException();
    }
    Pair<X, Y> p = new Pair<>(arr1.get(index), arr2.get(index));
    index++;
    return p;
  }
}

//
class ZipListsIterator<X, Y> implements Iterator<Pair<X, Y>> {
  
  // define variables
  ArrayList<X> arr1;
  ArrayList<Y> arr2;
  int index;
  int minSize;
  
  //
  public ZipListsIterator(ArrayList<X> arr1, ArrayList<Y> arr2){
    this.arr1 = arr1;
    this.arr2 = arr2;
    this.index = 0;
    this.minSize = Math.min(this.arr1.size(), this.arr2.size());
  }
  
  //
  @Override
  public boolean hasNext() {
    return index < this.minSize;
  }

  //
  @Override
  public Pair<X, Y> next() {
    if (!this.hasNext()) {
      throw new NoSuchElementException();
    }
    Pair<X, Y> p = new Pair<>(arr1.get(index), arr2.get(index));
    index++;
    return p;
  }
}

//
class ConcatIterator<T> implements Iterator<T> {
  
  // define variables
  Iterator<T> iter1;
  Iterator<T> iter2;
  Iterator<T> currentIter;
  
  //
  public ConcatIterator(Iterator<T> iter1, Iterator<T> iter2){
    this.iter1 = iter1;
    this.iter2 = iter2;
    this.currentIter = this.iter1;
  }

  //
  @Override
  public boolean hasNext() {
    while (!this.currentIter.hasNext()) {
      if(currentIter.equals(this.iter1)) {
        currentIter = this.iter2;
      } else {
        return false;
      }
    }
    return true;
  }

  //
  @Override
  public T next() {
    if (!this.hasNext()) {
      throw new NoSuchElementException();
    }
    return this.currentIter.next();
  }
}

//
class CycleIterator<T> implements Iterator<T> {
  
  // define variables
  Iterable<T> base;
  Iterator<T> iter;
  int index;
  boolean hasValues = false;
  
  //
  public CycleIterator(Iterable<T> iter){
    this.base = iter;
    this.iter = iter.iterator();
    index = 0;
    if (this.iter.hasNext()) {
      hasValues = true;
    }
  }

  //
  @Override
  public boolean hasNext() {
    if(!this.hasValues) {
      return false;
    }
    return true;
  }

  //
  @Override
  public T next() {
    if (!this.hasNext()) {
      throw new NoSuchElementException();
    }
    
    // if there is nothing left in iter, reset it
    if (!iter.hasNext()) {
      this.iter = this.base.iterator();
    } 
    return this.iter.next();
  }
}

class IteratorUtils {
  
  //
  public <X, Y> Iterator<Pair<X, Y>> zipStrict(ArrayList<X> arr1, ArrayList<Y> arr2) {
      return new ZipStrictIterator<X, Y>(arr1, arr2);
  }
  
  //
  public <X, Y> Iterator<Pair<X, Y>> zipLists(ArrayList<X> arr1, ArrayList<Y> arr2) {
    return new ZipListsIterator<X, Y>(arr1, arr2);
  }
  
  //
  public <T> Iterator<T> concat(Iterator<T> iter1, Iterator<T> iter2) {
    return new ConcatIterator<T>(iter1, iter2);
  }
  
  //
  public <T> Iterator<T> cycle(Iterable<T> iterable) {
    return new CycleIterator<T>(iterable);
  }
}

class ExamplesIterators {
  
  // example ArrayLists
  ArrayList<String> abcs = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g",
      "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
  ArrayList<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
      15 ,16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26));
  ArrayList<Integer> shorterNums = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
  ArrayList<String> shorterabcs = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
  ArrayList<String> mt = new ArrayList<>();
  
  // create iterators
  ZipStrictIterator<String, Integer> zsi;
  ZipStrictIterator<String, Integer> shorterZsi;
  ZipListsIterator<String, Integer> zli;
  ConcatIterator<Pair<String, Integer>> cci;
  CycleIterator<Integer> ci;
  CycleIterator<String> mtci;
  
  void init() {
    // initialize iterators
    this.zsi = new ZipStrictIterator<>(this.abcs, this.numbers);
    this.shorterZsi = new ZipStrictIterator<>(this.shorterabcs, this.shorterNums);
    this.zli = new ZipListsIterator<>(this.abcs, this.shorterNums);
    this.cci = new ConcatIterator<>(this.zsi, this.zli);
    this.ci = new CycleIterator<>(this.shorterNums);
    this.mtci = new CycleIterator<>(this.mt);
  }
  
  void reset() {
    this.zsi = null;
    this.shorterZsi = null;
    this.zli = null;
    this.cci = null;
    this.ci = null;
  }
  
  void testZipStrict(Tester t) {
    
    // reset test cases
    reset();
    
    //initialize test cases
    init();
    
    // test next
    t.checkExpect(zsi.next(), new Pair<String, Integer>("a", 1));
    t.checkExpect(zsi.next(), new Pair<String, Integer>("b", 2));
    t.checkExpect(zsi.next(), new Pair<String, Integer>("c", 3));
    t.checkExpect(zsi.next(), new Pair<String, Integer>("d", 4));
    t.checkExpect(zsi.next(), new Pair<String, Integer>("e", 5));
    
    // test hasNext
    t.checkExpect(shorterZsi.hasNext(), true);
    shorterZsi.next();
    shorterZsi.next();
    shorterZsi.next();
    shorterZsi.next();
    shorterZsi.next();
    t.checkExpect(shorterZsi.hasNext(), false);
  }
  
  void testZipLists(Tester t) {
    
    // reset test cases
    reset();
    
    //initialize test cases
    init();
    
    t.checkExpect(zli.next(), new Pair<String, Integer>("a", 1));
    t.checkExpect(zli.next(), new Pair<String, Integer>("b", 2));
    t.checkExpect(zli.next(), new Pair<String, Integer>("c", 3));
    t.checkExpect(zli.next(), new Pair<String, Integer>("d", 4));
    t.checkExpect(zli.next(), new Pair<String, Integer>("e", 5));
    
    // reset test cases
    reset();
    
    //initialize test cases
    init();
    
    // test hasNext
    t.checkExpect(zli.hasNext(), true);
    zli.next();
    zli.next();
    zli.next();
    zli.next();
    zli.next();
    t.checkExpect(zli.hasNext(), false);
  }
  
  void testCycleIterator(Tester t) {
    
    // reset test cases
    reset();
    
    //initialize test cases
    init();
    
    // test cycle if it cycles
    t.checkExpect(ci.next(), 1);
    t.checkExpect(ci.next(), 2);
    t.checkExpect(ci.next(), 3);
    t.checkExpect(ci.next(), 4);
    t.checkExpect(ci.next(), 5);
    t.checkExpect(ci.next(), 1);
    t.checkExpect(ci.next(), 2);
    t.checkExpect(ci.next(), 3);
    t.checkExpect(ci.next(), 4);
    t.checkExpect(ci.next(), 5);
    
    // reset test cases
    reset();
    
    //initialize test cases
    init();
    
    // test hasNext
    t.checkExpect(ci.hasNext(), true);
    zli.next();
    zli.next();
    zli.next();
    zli.next();
    zli.next();
    t.checkExpect(ci.hasNext(), true);
    
    // test empty
    t.checkExpect(mtci.hasNext(), false);
  }
}