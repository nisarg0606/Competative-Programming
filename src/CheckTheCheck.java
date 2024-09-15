import java.util.Scanner;

class CheckTheCheck {

    private static final int SIZE = 8;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int gameNumber = 0;

        while (true) {
            char[][] board = new char[SIZE][SIZE];
            boolean isEmptyBoard = true;

            // Read the board
            for (int i = 0; i < SIZE; i++) {
                String line = scanner.nextLine();
                if (!line.equals("........")) {
                    isEmptyBoard = false;
                }
                board[i] = line.toCharArray();
            }

            // Break if the board is empty (end of input_CheckTheCheck)
            if (isEmptyBoard) break;

            gameNumber++;

            // Determine if either king is in check
            boolean blackKingInCheck = isKingInCheck(board, 'k');
            boolean whiteKingInCheck = isKingInCheck(board, 'K');

            if (blackKingInCheck) {
                System.out.println("Game #" + gameNumber + ": black king is in check.");
            } else if (whiteKingInCheck) {
                System.out.println("Game #" + gameNumber + ": white king is in check.");
            } else {
                System.out.println("Game #" + gameNumber + ": no king is in check.");
            }

            // Read the empty line between board configurations
            if (scanner.hasNextLine()) scanner.nextLine();
        }

        scanner.close();
    }

    // Method to check if the king is in check
    private static boolean isKingInCheck(char[][] board, char king) {
        int kingRow = -1, kingCol = -1;

        // Find the king's position
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == king) {
                    kingRow = i;
                    kingCol = j;
                    break;
                }
            }
        }

        // Check for pawns
        if (king == 'k') { // Black king
            if (isValid(kingRow + 1, kingCol - 1) && board[kingRow + 1][kingCol - 1] == 'P') return true;
            if (isValid(kingRow + 1, kingCol + 1) && board[kingRow + 1][kingCol + 1] == 'P') return true;
        } else { // White king
            if (isValid(kingRow - 1, kingCol - 1) && board[kingRow - 1][kingCol - 1] == 'p') return true;
            if (isValid(kingRow - 1, kingCol + 1) && board[kingRow - 1][kingCol + 1] == 'p') return true;
        }

        // Check for knights
        int[][] knightMoves = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};
        char knight = (king == 'k') ? 'N' : 'n';
        for (int[] move : knightMoves) {
            if (isValid(kingRow + move[0], kingCol + move[1]) && board[kingRow + move[0]][kingCol + move[1]] == knight)
                return true;
        }

        // Check for bishops and queens (diagonal)
        int[][] bishopMoves = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        char bishop = (king == 'k') ? 'B' : 'b';
        char queen = (king == 'k') ? 'Q' : 'q';
        for (int[] move : bishopMoves) {
            if (isThreatInDirection(board, kingRow, kingCol, move[0], move[1], bishop, queen)) return true;
        }

        // Check for rooks and queens (straight)
        int[][] rookMoves = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        char rook = (king == 'k') ? 'R' : 'r';
        for (int[] move : rookMoves) {
            if (isThreatInDirection(board, kingRow, kingCol, move[0], move[1], rook, queen)) return true;
        }

        return false;
    }

    // Helper method to check if a position is within bounds
    private static boolean isValid(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    // Helper method to check for threats in a specific direction
    private static boolean isThreatInDirection(char[][] board, int row, int col, int rowInc, int colInc, char piece1, char piece2) {
        row += rowInc;
        col += colInc;
        while (isValid(row, col)) {
            if (board[row][col] != '.') {
                if (board[row][col] == piece1 || board[row][col] == piece2) {
                    return true;
                }
                break;
            }
            row += rowInc;
            col += colInc;
        }
        return false;
    }
}
