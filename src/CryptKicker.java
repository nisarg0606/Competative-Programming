import java.util.*;

public class CryptKicker {
    static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    // Function to check if the substitution dictionary is valid for the current encrypted word and dictionary word
    static boolean validSub(String enc, String word, Map<Character, Character> subs) {
        if (enc.length() != word.length()) return false;

        for (int i = 0; i < enc.length(); i++) {
            char e = enc.charAt(i); // encrypted character
            char w = word.charAt(i); // word character

            // Check if there is already a substitution and it's different
            Character mappedChar = subs.get(e);
            if (mappedChar != null && mappedChar != w) {
                return false; // Conflict in the mapping
            }
        }
        return true;
    }

    // Function to create a new substitution mapping for an encrypted word to a dictionary word
    static Map<Character, Character> createSub(String enc, String word, Map<Character, Character> subs) throws Exception {
        Map<Character, Character> newSubs = new HashMap<>(subs);

        for (int i = 0; i < enc.length(); i++) {
            char e = enc.charAt(i); // encrypted character
            char w = word.charAt(i); // dictionary word character

            Character mappedChar = newSubs.get(e);
            if (mappedChar != null && mappedChar == w) continue; // Already correctly mapped

            // If there is already a conflicting mapping or reverse mapping exists, throw exception
            if (mappedChar != null || newSubs.containsValue(w)) throw new Exception();

            newSubs.put(e, w); // Add new mapping
        }
        return newSubs;
    }

    // Recursive backtracking solution to decrypt the list of encrypted words
    static List<String> decrypt(String[] enc, Map<Integer, List<String>> wordsByLength, Map<Character, Character> subs) {
        if (subs == null) {
            // Initialize empty substitution dictionary
            subs = new HashMap<>();
            for (char c : ALPHABET.toCharArray()) subs.put(c, null);
        }

        // Check for dictionary words with matching length
        for (String word : wordsByLength.getOrDefault(enc[0].length(), new ArrayList<>())) {
            if (!validSub(enc[0], word, subs)) continue;

            Map<Character, Character> newSubs;
            try {
                newSubs = createSub(enc[0], word, subs); // Try to create a new mapping
            } catch (Exception e) {
                continue; // If mapping is invalid, try next word
            }

            // Base case: If we are at the last word, return the decrypted word
            if (enc.length == 1) return new ArrayList<>(Collections.singletonList(word));

            // Recursively decrypt the remaining words
            List<String> dec = decrypt(Arrays.copyOfRange(enc, 1, enc.length), wordsByLength, newSubs);
            if (dec != null) {
                dec.add(0, word); // Add current word to the decrypted list
                return dec;
            }
        }
        return null; // No valid decryption found
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Read number of words in the dictionary
        int nWords = Integer.parseInt(sc.nextLine().trim());

        // Dictionary words grouped by length
        Map<Integer, List<String>> wordsByLength = new HashMap<>();

        // Read dictionary words
        for (int i = 0; i < nWords; i++) {
            String word = sc.nextLine().trim().toLowerCase();
            wordsByLength.computeIfAbsent(word.length(), k -> new ArrayList<>()).add(word);
        }

        // Read encrypted lines and decrypt them
        while (sc.hasNextLine()) {
            String encLine = sc.nextLine().trim();
            if (encLine.isEmpty()) break;

            String[] encWords = encLine.split("\\s+");

            List<String> decryptedWords = decrypt(encWords, wordsByLength, null);
            if (decryptedWords != null) {
                // Print the decrypted line
                System.out.println(String.join(" ", decryptedWords));
            } else {
                // If no valid decryption, print asterisks
                StringBuilder sb = new StringBuilder();
                for (String encWord : encWords) {
                    sb.append(repeat(encWord.length())).append(" ");
                }
                System.out.println(sb.toString().trim());
            }
        }
        sc.close();
    }

    // Utility function to replace String.repeat() for Java 8 compatibility
    private static String repeat(int count) {
        return "*".repeat(Math.max(0, count));
    }
}
