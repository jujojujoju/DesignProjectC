package project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jhyeo on 2017-05-13.
 */
public class Combination <T> {
    public Set<Set<T>> getCombinationsFor(List<T> group, int subsetSize) {
        Set<Set<T>> resultingCombinations = new HashSet<Set<T>>();
        int totalSize=group.size();
        if (subsetSize == 0) {
            emptySet(resultingCombinations);
        } else if (subsetSize <= totalSize) {
            List<T> remainingElements = new ArrayList<T>(group);
            T X = popLast(remainingElements);

            Set<Set<T>> combinationsExclusiveX = getCombinationsFor(remainingElements, subsetSize);
            Set<Set<T>> combinationsInclusiveX = getCombinationsFor(remainingElements, subsetSize-1);
            for (Set<T> combination : combinationsInclusiveX) {
                combination.add(X);
            }
            resultingCombinations.addAll(combinationsExclusiveX);
            resultingCombinations.addAll(combinationsInclusiveX);
        }
        return resultingCombinations;
    }

    private void emptySet(Set<Set<T>> resultingCombinations) {
        resultingCombinations.add(new HashSet<T>());
    }

    private T popLast(List<T> elementsExclusiveX) {
        return elementsExclusiveX.remove(elementsExclusiveX.size()-1);
    }
}
