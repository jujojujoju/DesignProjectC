package project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jhyeo on 2017-05-13.
 */
public class Combination {
    public static Set<Set<Author>> getCombinationsFor(List<Author> group, int subsetSize) {
        Set<Set<Author>> resultingCombinations = new HashSet<Set<Author>>();
        int totalSize=group.size();
        if (subsetSize == 0) {
            emptySet(resultingCombinations);
        } else if (subsetSize <= totalSize) {
            List<Author> remainingElements = new ArrayList<Author>(group);
            Author X = popLast(remainingElements);

            Set<Set<Author>> combinationsExclusiveX = getCombinationsFor(remainingElements, subsetSize);
            Set<Set<Author>> combinationsInclusiveX = getCombinationsFor(remainingElements, subsetSize-1);
            for (Set<Author> combination : combinationsInclusiveX) {
                combination.add(X);
            }
            resultingCombinations.addAll(combinationsExclusiveX);
            resultingCombinations.addAll(combinationsInclusiveX);
        }
        return resultingCombinations;
    }

    private static void emptySet(Set<Set<Author>> resultingCombinations) {
        resultingCombinations.add(new HashSet<Author>());
    }

    private static Author popLast(List<Author> elementsExclusiveX) {
        return elementsExclusiveX.remove(elementsExclusiveX.size()-1);
    }
}
