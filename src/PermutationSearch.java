import javax.sound.midi.SysexMessage;
import java.util.*;

public class PermutationSearch {
    private final TreeMap<String, Set<Integer>> permutationIndex;

    public PermutationSearch(TreeMap<String, Set<Integer>> permutationIndex) {
        this.permutationIndex = permutationIndex;
    }
    public Set<Integer> search(String query) {


        String permutedQuery = query + "$";
        int jokerIndex1 = permutedQuery.indexOf('*');
        int jokerIndex2 = permutedQuery.lastIndexOf('*');

        /**один джокер**/
        if (jokerIndex1 == jokerIndex2) {

            int jokerIndex = permutedQuery.indexOf('*');
            String movedQuery = permutedQuery.substring(jokerIndex + 1) + permutedQuery.substring(0, jokerIndex);
            String jokerRemoved = movedQuery.replace("*", "");
            System.out.println("Searching for " + jokerRemoved);
            Set<Integer> result = new HashSet<>();
            for (Map.Entry<String, Set<Integer>> entry : permutationIndex.entrySet()) {
                String term = entry.getKey();
                if (term.startsWith(jokerRemoved)) {
                    result.addAll(entry.getValue());
                }
            }

            return result;
        }
        /**два джокери**/
        else {
            String letters = permutedQuery.substring(jokerIndex1 + 1, jokerIndex2);

            String removedJokersQuery = permutedQuery.substring(jokerIndex2 + 1) + permutedQuery.substring(0, jokerIndex1);

            String jokerRemoved = removedJokersQuery.replace("*", "");
            System.out.println("Searching for " + jokerRemoved + " and post-filtering " + letters);

            return searchWithJokerAndLetters(jokerRemoved, letters);

        }
    }

    private Set<Integer> searchWithJokerAndLetters(String query, String letters) {
        Set<Integer> result = new HashSet<>();
        for (Map.Entry<String, Set<Integer>> entry : permutationIndex.entrySet()) {
            String term = entry.getKey();
            if (term.startsWith(query) && term.contains(letters)) {
                result.addAll(entry.getValue());
            }
        }
        return result;
    }

}
