import java.util.*;

public class SingleJokerSearch {
    private final LetterTree normalTree;
    private final LetterTree reverseTree;

    public SingleJokerSearch(LetterTree normalTree, LetterTree reverseTree) {
        this.normalTree = normalTree;
        this.reverseTree = reverseTree;
    }

    public Set<Integer> search(String query) {
        if (!query.contains("*")) {
            return searchWithoutJoker(query);
        } else if (query.startsWith("*")) {
            return searchWithLeadingJoker(query);
        } else if (query.endsWith("*")) {
            return searchWithTrailingJoker(query);
        } else {
            return searchJokerInside(query);
        }
    }


    private Set<Integer> searchWithoutJoker(String query) {
        return normalTree.getWordIndices(query);
    }

    private Set<Integer> searchWithTrailingJoker(String query) {
        String prefix = query.substring(0, query.length() - 1);
        Set<Integer> result = new HashSet<>();
        List<String> words = normalTree.getAllWordsStartingWith(prefix);
        for (String word : words) {
            result.addAll(normalTree.getWordIndices(word));
        }
        return result;
    }

    private Set<Integer> searchWithLeadingJoker(String query) {
        String suffix = new StringBuilder(query.substring(1)).reverse().toString();
        Set<Integer> result = new HashSet<>();
        List<String> words = reverseTree.getAllWordsStartingWith(suffix);
        for (String word : words) {
            result.addAll(reverseTree.getWordIndices(word));
        }
        return result;
    }


    public Set<Integer> searchJokerInside(String query) {
        int jokerIndex = query.indexOf('*');
        String firstPart = query.substring(0, jokerIndex);
        String secondPart = query.substring(jokerIndex + 1);
        System.out.println("*" + secondPart);

        Set<Integer> firstSet = search(firstPart + "*");
        System.out.println(firstPart + " * search result: " + firstSet);
        Set<Integer> secondSet = search("*" + secondPart);
        System.out.println("*" + secondPart + " search result: " + secondSet );

        firstSet.retainAll(secondSet);

        return firstSet;
    }


}
