import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import tester.Tester;

class ArrayPractice {

  // filter
  <T> ArrayList<T> filter(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> ans = new ArrayList<T>();
    for (T obj : arr) {
      if (pred.test(obj)) {
        ans.add(obj);
      }
    }
    return ans;
  }

  // filter not
  <T> ArrayList<T> filterNot(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> ans = new ArrayList<T>();
    for (T obj : arr) {
      if (!pred.test(obj)) {
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
    for (int i = 0; i < arr.size(); i++) {
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
    for (int i = 0; i < max; i++) {
      if (arr1.size() > i) {
        ans.add(arr1.get(i));
      }
      if (arr2.size() > i) {
        ans.add(arr2.get(i));
      }

    }
    return ans;
  }

  <T> ArrayList<T> customInterweave2(ArrayList<T> arr1, ArrayList<T> arr2, int getFrom1, int getFrom2) {
    ArrayList<T> ans = new ArrayList<T>();
    int arr1Size = arr1.size();
    int arr2Size = arr2.size();
    int max = Math.max(arr1.size(), arr2.size());

    for (int i = 0; i < max; i++) {

      for (int j = i * getFrom1; j < ((i + 1) * getFrom1); j++) {
        if (j < arr1Size) {
          ans.addLast(arr1.get(j));
        } else {
          break;
        }
      }

      for (int j = i * getFrom2; j < ((i + 1) * getFrom2); j++) {
        if (j < arr2Size) {
          ans.addLast(arr2.get(j));
        } else {
          break;
        }
      }
    }
    return ans;
  }

}

class ExamplesArrayLists {

  ArrayPractice ap = new ArrayPractice();

  ArrayList<String> arr1 = new ArrayList<String>(Arrays.asList("A", "B", "C", "D", "E", "F"));
  ArrayList<String> arr2 = new ArrayList<String>(Arrays.asList("w", "x", "y", "z"));
  Predicate<String> pred = arr -> arr.contains("A");

  boolean testFilter(Tester t) {
    return t.checkExpect(ap.filter(arr1, pred), new ArrayList<String>(Arrays.asList("A")))
        && t.checkExpect(ap.filter(arr2, pred), new ArrayList<String>());
  }

  boolean testFilterNot(Tester t) {
    return t.checkExpect(ap.filterNot(arr1, pred), new ArrayList<String>(Arrays.asList("B", "C", "D", "E", "F")))
        && t.checkExpect(ap.filterNot(arr2, pred), new ArrayList<String>(Arrays.asList("w", "x", "y", "z")));
  }

  boolean testCustomFilter(Tester t) {
    return t.checkExpect(ap.customFilter(arr1, pred, true), new ArrayList<String>(Arrays.asList("A")))
        && t.checkExpect(ap.customFilter(arr2, pred, false), new ArrayList<String>(Arrays.asList("w", "x", "y", "z")));
  }

  boolean testInterweave(Tester t) {
    return t.checkExpect(ap.interweave(arr1, arr2),
        new ArrayList<String>(Arrays.asList("A", "w", "B", "x", "C", "y", "D", "z", "E", "F")))
        && t.checkExpect(ap.interweave(arr2, arr1), new ArrayList<String>(Arrays.asList("w", "A", "x", "B", "y", "C", "z", "D", "E", "F")));
  }

  boolean testCustomInterweave(Tester t) {
    // Negate by 0
    return t.checkExpect(this.ap.customInterweave2(this.arr1, this.arr2, 1, 1),
        new ArrayList<String>(Arrays.asList("A", "w", "B", "x", "C", "y", "D", "z", "E", "F")))
        && t.checkExpect(this.ap.customInterweave2(this.arr1, this.arr2, 3, 2),
            new ArrayList<String>(Arrays.asList("A", "B", "C", "w", "x", "D", "E", "F", "y", "z")));
  }

}
