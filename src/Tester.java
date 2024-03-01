import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Tester {

    public static void main(String[] args) {

        String directoryPath = "C:\\Library";

        Dictionary dictionary = new Dictionary(directoryPath);

        System.out.println("---------------------Normal tree:-----------------------------");
        dictionary.normalTree.printTree();

        System.out.println("---------------------Reverse tree:-----------------------------");
        dictionary.reverseTree.printTree();

        dictionary.writeDictionary();

        System.out.println("-------------------------");


        Scanner scanner = new Scanner(System.in);
      SingleJokerSearch singleJokerSearch = new SingleJokerSearch(dictionary.normalTree,dictionary.reverseTree);

        while (true) {
            System.out.println("Searching options: 1 - single joker");
            String answer = scanner.nextLine();
            if (answer.equals("1")) {
                System.out.println("Enter your query: ");
                String query = scanner.nextLine();
                Set<Integer> result = singleJokerSearch.search(query);
                System.out.println("Result of query " + query + ": " + result);

            }


        }
    }
}
