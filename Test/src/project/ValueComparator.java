package project;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator<T> implements Comparator<T> {
	Map<T, Integer> base;

    public ValueComparator(Map<T, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.
    public int compare(T a, T b) {
        if (base.get(a) >= base.get(b)) { //반대로 하면 오름차순 <=
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }

}
