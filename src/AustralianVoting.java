import java.util.*;

class AustralianBallot {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfCases = Integer.parseInt(scanner.nextLine().trim());
        scanner.nextLine(); // Read the blank line after the number of cases

        for (int caseNumber = 1; caseNumber <= numberOfCases; caseNumber++) {
            if (caseNumber > 1) System.out.println(); // Print a blank line between cases

            // Read number of candidates
            int numberOfCandidates = Integer.parseInt(scanner.nextLine().trim());

            // Read candidates' names
            String[] candidates = new String[numberOfCandidates];
            for (int i = 0; i < numberOfCandidates; i++) {
                candidates[i] = scanner.nextLine().trim();
            }

            // Read ballots
            List<List<Integer>> ballots = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) break; // Blank line indicates the end of a case
                List<Integer> ballot = new ArrayList<>();
                for (String rank : line.split(" ")) {
                    ballot.add(Integer.parseInt(rank) - 1); // Store zero-based indices for candidates
                }
                ballots.add(ballot);
            }

            // Determine the winner
            boolean[] eliminated = new boolean[numberOfCandidates];
            int[] votes = new int[numberOfCandidates];
            int majority = ballots.size() / 2 + 1;

            while (true) {
                Arrays.fill(votes, 0); // Reset votes count

                // Count votes for non-eliminated candidates
                for (List<Integer> ballot : ballots) {
                    for (int choice : ballot) {
                        if (!eliminated[choice]) {
                            votes[choice]++;
                            break;
                        }
                    }
                }

                // Check if a candidate has more than 50% of the votes
                int maxVotes = Arrays.stream(votes).max().getAsInt();
                if (maxVotes >= majority) {
                    for (int i = 0; i < numberOfCandidates; i++) {
                        if (votes[i] == maxVotes) {
                            System.out.println(candidates[i]);
                        }
                    }
                    break;
                }

                // Find the minimum number of votes for non-eliminated candidates
                int minVotes = Integer.MAX_VALUE;
                for (int i = 0; i < numberOfCandidates; i++) {
                    if (!eliminated[i] && votes[i] < minVotes) {
                        minVotes = votes[i];
                    }
                }

                // Find candidates with the minimum votes
                List<Integer> candidatesWithMinVotes = new ArrayList<>();
                for (int i = 0; i < numberOfCandidates; i++) {
                    if (!eliminated[i] && votes[i] == minVotes) {
                        candidatesWithMinVotes.add(i);
                    }
                }

                // Count the number of non-eliminated candidates
                int nonEliminatedCount = numberOfCandidates - (int) Arrays.stream(toBooleanArray(eliminated)).filter(e -> e).count();

                // If all remaining candidates are tied, all of them win
                if (candidatesWithMinVotes.size() == nonEliminatedCount) {
                    for (int i : candidatesWithMinVotes) {
                        System.out.println(candidates[i]);
                    }
                    break;
                }

                // Eliminate candidates with minimum votes
                for (int i : candidatesWithMinVotes) {
                    eliminated[i] = true;
                }
            }
        }
        scanner.close();
    }

    private static Boolean[] toBooleanArray(boolean[] boolArray) {
        Boolean[] result = new Boolean[boolArray.length];
        for (int i = 0; i < boolArray.length; i++) {
            result[i] = boolArray[i];
        }
        return result;
    }

}
