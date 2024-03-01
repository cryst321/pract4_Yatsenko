import java.util.*;

public class LetterTree {

    private static final int ALPHABET_SIZE = 26;

    private TreeNode root;

    private static class TreeNode {
        TreeNode[] children = new TreeNode[ALPHABET_SIZE];
        Set<Integer> indices = new HashSet<>();
    }

    public LetterTree() {
        root = new TreeNode();
    }

    public void insertWord(String word, int fileIndex) {
        TreeNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                current.children[index] = new TreeNode();
            }
            current = current.children[index];
        }
        current.indices.add(fileIndex);
    }

    public List<String> getAllWords() {
        List<String> words = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        collectWords(root, currentWord, words);
        return words;
    }

    private void collectWords(TreeNode node, StringBuilder currentWord, List<String> words) {
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            TreeNode child = node.children[i];
            if (child != null) {
                char c = (char) ('a' + i);
                currentWord.append(c);
                if (!child.indices.isEmpty()) {
                    words.add(currentWord.toString());
                }
                collectWords(child, currentWord, words);
                currentWord.deleteCharAt(currentWord.length() - 1);
            }
        }
    }

    public int size() {
        return countWords(root);
    }

    private int countWords(TreeNode node) {
        int count = 0;
        if (!node.indices.isEmpty()) {
            count++;
        }
        for (TreeNode child : node.children) {
            if (child != null) {
                count += countWords(child);
            }
        }
        return count;
    }

    public void printTree() {
        printTree(root, new StringBuilder());
    }

    private void printTree(TreeNode node, StringBuilder prefix) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            TreeNode child = node.children[i];
            if (child != null) {
                char c = (char) ('a' + i);
                StringBuilder currentPrefix = new StringBuilder(prefix);
                currentPrefix.append(c);
                if (!child.indices.isEmpty()) {
                    System.out.println(currentPrefix + " " + child.indices);
                }
                printTree(child, currentPrefix);
            }
        }
    }

    public Set<Integer> getWordIndices(String word) {
        TreeNode current = root;
        for (char c : word.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                return new HashSet<>();
            }
            current = current.children[index];
        }
        return current.indices;
    }

    public List<String> getAllWordsStartingWith(String prefix) {
        List<String> words = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();
        TreeNode node = findNodeForPrefix(prefix);
        if (node != null) {
            collectWords(node, new StringBuilder(prefix), words);
        }
        return words;
    }

    private TreeNode findNodeForPrefix(String prefix) {
        TreeNode current = root;
        for (char c : prefix.toCharArray()) {
            int index = c - 'a';
            if (current.children[index] == null) {
                return null;
            }
            current = current.children[index];
        }
        return current;
    }



}
