import java.util.*;


/***
 * Булевий пошук потрібен для фразових запитів з більше ніж 2 словами, щоб об'єднати їх через AND
 */
public class BooleanSearch {

    public static final Map<String, Integer> precedence = Map.of(
            "AND", 2,
            "OR", 1
    );



    /**
     * переводимо в зворотній польский запис
     * @param query запит користувача
     * @return оброблений список аргументів
     */

    public static List<String> parseQuery(String query) {
        List<String> tokens = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        String[] splitQuery = query.split("\\s+");
        for (String token : splitQuery) {
            if (isOperand(token)) {
                tokens.add(token.replace("$", " "));
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && precedence.getOrDefault(token, 0) <= precedence.getOrDefault(stack.peek(), 0)) {
                    tokens.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            tokens.add(stack.pop());
        }
        System.out.println("reverse polish: " + tokens);
        return tokens;
    }






    /**
     * операція об'єднання інвертованих індексів
     * @param set1 список 1
     * @param set2 список 2
     * @return
     */
    public static Set<Integer> union(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> unionSet = new HashSet<>(set1);
        unionSet.addAll(set2);
        System.out.println("result of OR: "+ unionSet);
        return unionSet;
    }
    /**
     * операція перетину інвертованих індексів
     * @param set1 список 1
     * @param set2 список 2
     * @return
     */
    public static Set<Integer> intersect(Set<Integer> set1, Set<Integer> set2) {
        Set<Integer> intersectSet = new HashSet<>(set1);
        intersectSet.retainAll(set2);
        System.out.println("result of AND: "+ intersectSet);
        return intersectSet;
    }

    public static boolean isOperand(String token) {
        return token.matches("\".*\"") || token.startsWith("-");
    }

    public static boolean isOperator(String token) {
        return precedence.containsKey(token);
    }


    /**
     * Булевий пошук по інвертованому індексу
     * @param parsedQuery запит користувача в зворотньому польскому записі
     * @param invertedIndex інвертований індекс
     * @param numDocuments кількість файлів
     * @return
     */
    public static Set<Integer> evaluateQueryInvertedIndex(List<String> parsedQuery, Map<String, Set<Integer>> invertedIndex, int numDocuments) {
        Deque<Set<Integer>> stack = new ArrayDeque<>();

        for (String token : parsedQuery) {
            if (isOperand(token)) {
                if (token.startsWith("-")) {
                    token = token.substring(1);
                    Set<Integer> negatedSet = new HashSet<>();
                    for (int i = 0; i < numDocuments; i++) {
                        negatedSet.add(i);
                    }
                    negatedSet.removeAll(invertedIndex.getOrDefault(token.replaceAll("\"", "").toLowerCase(), Collections.emptySet()));
                    stack.push(negatedSet);
                    System.out.println (token + " negated set: " + negatedSet);
                } else {
                    Set<Integer> operandSet = invertedIndex.getOrDefault(token.replaceAll("\"", "").toLowerCase(), Collections.emptySet());
                    stack.push(operandSet);
                    System.out.println(token + " operand set: " + operandSet);
                }
            } else if (isOperator(token)) {
                Set<Integer> operand2 = stack.pop();
                Set<Integer> operand1 = stack.pop();
                Set<Integer> result = new HashSet<>();

                switch (token) {
                    case "AND":
                        result = intersect(operand1, operand2);
                        break;
                    case "OR":
                        result = union(operand1, operand2);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid operator: " + token);
                }

                stack.push(result);
            }
        }

        if (stack.isEmpty()) {
            throw new IllegalArgumentException("Empty stack");
        }

        return stack.pop();
    }



}
