import java.util.*;

public class ErdosNumber {

    // This class represents each author (cell)
    static class Author {
        Map<String, Author> coAuthors = new HashMap<>();
        int erdosNumber = Integer.MAX_VALUE;  // Initially, everyone has "infinity" (maximum value)
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int t = sc.nextInt();  // Number of test cases
        sc.nextLine();  // Consume newline

        for (int test = 1; test <= t; test++) {
            int p = sc.nextInt();  // Number of papers
            int n = sc.nextInt();  // Number of names to query
            sc.nextLine();  // Consume newline

            // A map from author names to their corresponding cell (author data)
            Map<String, Author> authorMap = new HashMap<>();

            // To avoid reallocation, reuse the list for each paper
            List<String> authors = new ArrayList<>();

            for (int i = 0; i < p; i++) {
                String line = sc.nextLine();
                String[] parts = line.split(":")[0].split(", ");

                // Clear the authors list for reuse
                authors.clear();

                // Extract authors from the paper
                for (int j = 0; j < parts.length; j += 2) {
                    String author = parts[j] + ", " + parts[j + 1];
                    authors.add(author);
                    // Add to the map if not already present
                    authorMap.putIfAbsent(author, new Author());
                }

                // For each author in the paper, connect them to their co-authors
                for (String author : authors) {
                    Author currentAuthor = authorMap.get(author);
                    for (String coAuthor : authors) {
                        if (!author.equals(coAuthor)) {
                            currentAuthor.coAuthors.putIfAbsent(coAuthor, authorMap.get(coAuthor));
                        }
                    }
                }
            }

            // BFS to calculate Erdős numbers
            if (authorMap.containsKey("Erdos, P.")) {
                bfs(authorMap);
            }

            // Output the results for the queried authors
            System.out.println("Scenario " + test);
            for (int i = 0; i < n; i++) {
                String name = sc.nextLine();
                Author author = authorMap.get(name);
                if (author == null || author.erdosNumber == Integer.MAX_VALUE) {
                    System.out.println(name + " infinity");
                } else {
                    System.out.println(name + " " + author.erdosNumber);
                }
            }
        }

        sc.close();
    }

    // BFS to compute Erdős numbers starting from "Erdos, P."
    private static void bfs(Map<String, Author> authorMap) {
        Queue<Author> queue = new LinkedList<>();
        Author erdos = authorMap.get("Erdos, P.");
        erdos.erdosNumber = 0;
        queue.add(erdos);

        // Set to keep track of visited authors to avoid revisiting
        Set<Author> visited = new HashSet<>();
        visited.add(erdos);

        while (!queue.isEmpty()) {
            Author current = queue.poll();
            int currentNumber = current.erdosNumber;

            for (String coAuthorName : current.coAuthors.keySet()) {
                Author coAuthor = current.coAuthors.get(coAuthorName);
                if (!visited.contains(coAuthor)) {  // Check if already visited
                    coAuthor.erdosNumber = currentNumber + 1;
                    queue.add(coAuthor);
                    visited.add(coAuthor);  // Mark as visited
                }
            }
        }
    }
}
