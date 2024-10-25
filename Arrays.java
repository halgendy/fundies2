import java.util.ArrayList;
import java.util.Arrays;

//

import java.util.ArrayList;
import java.util.function.Predicate;
import tester.Tester;

class ArrayPractice {
  
  // CONSTRUCTOR
  ArrayPractice() {}
  
  // filter
  <T> ArrayList<T> filter(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> ans = new ArrayList<T>();
    for(T obj : arr) {
      if(pred.test(obj)) {
        ans.add(obj);
      }
    }
    return ans;
  }
  
  // filter not
  <T> ArrayList<T> filterNot(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> ans = new ArrayList<T>();
    for(T obj : arr) {
      if(!pred.test(obj)) {
        ans.add(obj);
      }
    }
    return ans;
  }
  
  // combined filter
  <T> ArrayList<T> customFilter(ArrayList<T> arr, Predicate<T> pred, boolean keepPassing) {
    if (keepPassing) {
      return filter(arr, pred);
    } else {
      return filterNot(arr, pred);
    }
  }
  
  // remove the failed pred
  <T> void removeFailing(ArrayList<T> arr, Predicate<T> pred) {
    for(int i = 0; i < arr.size(); i++) {
      if (pred.test(arr.get(i))) {
        continue;
      } else {
        arr.remove(i);
        i--;
      }
    }
  }
  
  <T> ArrayList<T> interweave(ArrayList<T> arr1, ArrayList<T> arr2) {
    ArrayList<T> ans = new ArrayList<T>();
    int max = Math.max(arr1.size(), arr2.size());
    
    for(int i = 0; i < max; i++) {
      
      if(i < arr1.size()) {
        ans.addLast(arr1.get(i));
      }
      if(i < arr2.size()) {
        ans.addLast(arr2.get(i));
      }
    }
    return ans;
  }
  
  <T> ArrayList<T> customInterweave(ArrayList<T> arr1, ArrayList<T> arr2, int getFrom1, int getFrom2) {
    ArrayList<T> ans = new ArrayList<T>();
    int arr1Size = arr1.size();
    int arr2Size = arr2.size();
    int max = Math.max(arr1.size(), arr2.size());
    for(int i = 0; i < max; i++) {
      
      if(arr1Size - getFrom1 < 0) {
        for(int j = 0; j < arr1Size; j++) {
          ans.addLast(arr1.get(j));
          arr1.remove(j);
        }
      } else {
        for(int j = 0; j < arr1Size - getFrom1; j++) {
          ans.addLast(arr2.get(j));
          arr2.remove(j);
          arr1Size -= getFrom1;
        }
      }
      
      // checks if getfrom returns the rest
      if(arr2Size - getFrom2 < 0) {
        
        // adds the rest
        for(int j = 0; j < arr2Size; j++) {
          ans.addLast(arr2.get(j));
          arr2.remove(j);
        }
        // checks if getfrom doesn't return the rest
      } else {
        
        // adds the rest, makes arr2Size lower
        for(int j = 0; j < arr2Size - getFrom2; j++) {
          ans.addLast(arr2.get(j));
          arr2.remove(j);
          arr2Size -= getFrom2;
        }
      }
      
    }
    return ans;
  }
  
  <T> ArrayList<T> customInterweave2(ArrayList<T> arr1, ArrayList<T> arr2, int getFrom1, int getFrom2) {
    ArrayList<T> ans = new ArrayList<T>();
    int arr1Size = arr1.size();
    int arr2Size = arr2.size();
    int max = Math.max(arr1.size(), arr2.size());
    
    for(int i = 0; i < max; i++) {
      
      for(int j = i * getFrom1; j < ((i + 1) * getFrom1); j++) {
        if (j < arr1Size) {
          ans.addLast(arr1.get(j)); 
        }
        else {
          break;
        }
      }
      
      for(int j = i * getFrom2; j < ((i + 1) * getFrom2); j++) {
        if (j < arr2Size) {
          ans.addLast(arr2.get(j)); 
        }
        else {
          break;
        }
      }
    }
    return ans;
  }
  
}

class ExamplesArrayLists {
  ArrayPractice ap = new ArrayPractice();
  
  ArrayList<String> arr1 = new ArrayList<String>(
      Arrays.asList("A", "B", "C", "D", "E", "F"));
  
  ArrayList<String> arrA = new ArrayList<String>(
      Arrays.asList("A"));
  
  ArrayList<String> arr2 = new ArrayList<String>(
      Arrays.asList("w", "x", "y", "z"));
  
  ArrayList<String> arr = new ArrayList<String>();
  
  ArrayList<String> arr12 = new ArrayList<String>(
      Arrays.asList("A", "B", "C", "w", "x", "D", "E", "F", "y", "z"));

  ArrayList<String> arr12_2 = new ArrayList<String>(
      Arrays.asList("A", "w", "B", "x", "C", "y", "D", "z", "E", "F"));
  
  Predicate<String> predA = str -> str.contains("A");
  
  // REPRESENTS testing filter
  boolean testFilter(Tester t) {
    // Negate by 0
    return t.checkExpect(this.ap.filter(this.arr1, this.predA), this.arrA)
        // Negate positive
        && t.checkExpect(this.ap.filter(this.arr2, this.predA), this.arr);
  }
  
  // REPRESENTS testing filter
  boolean testInterweave(Tester t) {
    // Negate by 0
    return t.checkExpect(this.ap.interweave(this.arr1, this.arr2), this.arr12);
  }
  
  // REPRESENTS testing filter
  boolean testCustomInterweave(Tester t) {
    // Negate by 0
    return t.checkExpect(this.ap.customInterweave2(this.arr1, this.arr2, 1, 1), this.arr12_2)
        && t.checkExpect(this.ap.customInterweave2(this.arr1, this.arr2, 3, 2), this.arr12);
  }
}
