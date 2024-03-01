import java.io.*;
import java.util.*;

public class Dictionary {

    public int allWords;

    private final String size;
    public int numFiles;

    /**1. нормальний інверт. індекс**/
    public LetterTree normalTree;
    /**1. обернений інверт. індекс**/
    public LetterTree reverseTree;


    /**2. перестановочний індекс**/


    public Dictionary(String directoryPath) {
        allWords = 0;


        /**1. нормальний інверт. індекс**/
        normalTree = new LetterTree();
        /**1. обернений інверт. індекс**/
        reverseTree = new LetterTree();


        numFiles = countTxtFiles(directoryPath);
        size = createDictionary(directoryPath);

    }

    /**
     * створюємо словник
     * @param directoryPath шлях до "бібліотеки" тхт файлів
     * @return розмір словника
     */
    private String createDictionary(String directoryPath) {
        long size = 0;
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }
        File[] files = directory.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("Unable to list files in the directory.");
        }
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                size += files[i].length();
                System.out.println("parsing file " + files[i].getName());
                parseFile(files[i], i);
            }
        }
        long bytes = size % 1024;
        size /= 1024;
        long kb = size % 1024;
        size /= 1024;
        return size + " Mb " + kb + " Kb " + bytes + " bytes";
    }

    /**
     * аналізуємо файл
     * @param file файл
     * @param fileIndex індекс файлу
     */
    /**
     * аналізуємо файл
     * @param file файл
     * @param fileIndex індекс файлу
     */
    private void parseFile(File file, int fileIndex) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z\\s]", "");
                String[] words = line.split("\\s+");

                for (String word : words) {
                    if(!word.isEmpty()) {
                        word = word.trim().toLowerCase(Locale.ROOT);


                        /**1. додаєм до дерев**/
                        normalTree.insertWord(word, fileIndex);
                        String reversedWord = new StringBuilder(word).reverse().toString();
                        reverseTree.insertWord(reversedWord, fileIndex);

                        this.allWords++;


                        /**1. додаєм до перестановочного індексу**/

                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getName());
            e.printStackTrace();
        }
    }







    /**
     * виводимо дерева
     */
    public void printTree(TreeMap<String, Set<Integer>> tree) {
        int count = 0;
        for (Map.Entry<String, Set<Integer>> entry : tree.entrySet()) {
            System.out.print(entry.getKey() + " [");
            Set<Integer> indices = entry.getValue();
            int size = indices.size();
            int i = 0;
            for (Integer index : indices) {
                System.out.print(index);
                if (++i < size) {
                    System.out.print(",");
                }
            }
            System.out.println("]");
            count++;
            if (count == 100000) {
                break;
            }
        }
    }


    /**
     * записуємо словник у файл
     */
    public void writeDictionary() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dictionary.txt"))) {
            for (String word : normalTree.getAllWords()) {
                writer.write(word + "\n");
            }
            System.out.println("Size of collection: " + size +
                    "\nWords in collection: " + allWords +
                    "\nSize of dictionary: " + prettySizeOfFile("dictionary.txt") +
                    "\nWords in dictionary: " + normalTree.size());
        } catch (IOException e) {
            System.out.println("Error writing dictionary to file.");
            e.printStackTrace();
        }
    }

    /**
     * виводимо розмір файлу красиво
     * @param path шлях до файлу
     * @return стрінг з розміром файлу красиво
     */
    public String prettySizeOfFile(String path) {
        File file = new File(path);
        long fileSizeInBytes = file.length();
        long fileSizeInKB = fileSizeInBytes / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;
        fileSizeInKB %= 1024;
        fileSizeInBytes %= 1024;
        return fileSizeInMB + " Mb " + fileSizeInKB + " Kb " + fileSizeInBytes + " bytes";
    }

    /**
     * рахуєм кількість файлів у бібліотеці
     * @param directoryPath посилання на бібліотеку
     * @return кількість файлів
     */
    private int countTxtFiles(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a directory.");
        }
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        return files != null ? files.length : 0;
    }







}
