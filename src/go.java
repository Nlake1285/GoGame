//-------------------
// go.java
// By: Zenith Lake
// Date: 10/28/2024
//-------------------
// Task:
// Create a simple game of Go

import java.util.Scanner;
public class go {
    public static void main(String[] args) throws Exception {
        Scanner myObj = new Scanner(System.in); // Time to create the Scanner object
        String[][] goBoard = new String[9][9]; // Creating the new go board array!
        String[][] previousBoard = new String[9][9]; // Creating the previous board array!
        int playerScore = 0; // Keeping track of the player's score
        int opponentScore = 0; // Keeping track of the opponent's score

        while(true){

            // Print the board
            System.out.println(" 1 2 3 4 5 6 7 8 9"); // print the column numbers
            for (int i = 0; i < goBoard.length; i++){
                System.out.print(i+1);
                for (int j = 0; j < goBoard[i].length; j++){
                    if(goBoard[i][j] == null){
                        if(j == 0){
                            System.out.print("|");
                        }
                        else{
                            System.out.print("-|");
                        }
                    }
                    else{
                        System.out.print(goBoard[i][j]);
                    }
                }
                System.out.println();
            }

            // Store the current board state;
            cloneBoard(goBoard, previousBoard);

//------------------------------------------------------------------------------------------------

            // Player's move:
            System.out.println("Enter the x and y coordinates of your move: (Must be between 0 and 8) ");
            int moveX = myObj.nextInt();
            int moveY = myObj.nextInt();

            // Opponent's move:
            System.out.println("Enter the x and y coordinates of your opponent's move: (Must be between 0 and 8)");
            int opponentMoveX = myObj.nextInt();
            int opponentMoveY = myObj.nextInt();

            // Move verification:
            boolean validMove = true;

            if (moveX < 0 || moveX >= goBoard.length || moveY < 0 || moveY >= goBoard[0].length) {
                System.out.println("Hey chucklenuts, your move is out of bounds!");
                validMove = false;
            } else if (opponentMoveX < 0 || opponentMoveX >= goBoard.length || opponentMoveY < 0 || opponentMoveY >= goBoard[0].length) {
                System.out.println("Hey chucklenuts, your opponent's move is out of bounds!");
                validMove = false;
            }
            if (validMove) {
                // Proceed with placing the moves if nothing seems fishy...
                if (goBoard[moveX][moveY] == null) {
                    goBoard[moveX][moveY] = "-o";
                } else {
                    System.out.println("Hey chucklenuts, you can't place a stone there!");
                }
                if (goBoard[opponentMoveX][opponentMoveY] == null) {
                    goBoard[opponentMoveX][opponentMoveY] = "-*";
                } else {
                    System.out.println("Hey chucklenuts, your opponent can't place a stone there!");
                }

                // Check for captures
                playerScore += captureStones(goBoard, "-*");
                opponentScore += captureStones(goBoard, "-o");

                //Check for ko
                if (isKo(goBoard, previousBoard)) {
                    System.out.println("Ko rule violation! The move is not allowed.");
                    // Undo the move:
                    cloneBoard(previousBoard, goBoard);
                }
            }

            // Score calculation:
            playerScore = calculateScore(goBoard, "-o");
            opponentScore = calculateScore(goBoard, "-*");

            // Score display:
            System.out.println("Player Score: " + playerScore);
            System.out.println("Opponent Score: " + opponentScore);
        }
    }

//------------------------------------------------------------------------------------------------

    // Method for score keeping:
    public static int calculateScore(String[][] board, String playerStone) {
        int score = 0;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                if(board[i][j] == playerStone){
                    score++;
                }
            }
        }
        return score;
    }

    // Method to calculate the territory
    public static int calculateTerritory(String[][] board, String playerStone) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        int territory = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == null && !visited[i][j]) {
                    int[] result = exploreTerritory(board, i, j, playerStone, visited);
                    if (result[1] == 1) {
                        territory += result[0];
                    }
                }
            }
        }
        return territory;
    }

    // Method to explore a territory
    public static int[] exploreTerritory(String[][] board, int x, int y, String playerStone, boolean[][] visited) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || visited[x][y]) {
            return new int[]{0, 1};
        }
        visited[x][y] = true;
        if (board[x][y] != null) {
            return new int[]{0, board[x][y].equals(playerStone) ? 1 : -1};
        }
        int[] up = exploreTerritory(board, x - 1, y, playerStone, visited);
        int[] down = exploreTerritory(board, x + 1, y, playerStone, visited);
        int[] left = exploreTerritory(board, x, y - 1, playerStone, visited);
        int[] right = exploreTerritory(board, x, y + 1, playerStone, visited);
        int territory = 1 + up[0] + down[0] + left[0] + right[0];
        int ownership = up[1] == down[1] && down[1] == left[1] && left[1] == right[1] ? up[1] : 0;
        return new int[]{territory, ownership};
    }

    // Method to check if a group of stones has any liberties:
    public static boolean hasLiberties(String[][] board, int x, int y, String stone, boolean[][] visited) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) {
            return false;
        }
        if (board[x][y] == null) {
            return true;
        }
        if (visited[x][y] || !board[x][y].equals(stone)) {
            return false;
        }
        visited[x][y] = true;
        return  hasLiberties(board, x + 1, y, stone, visited) ||
                hasLiberties(board, x - 1, y, stone, visited) ||
                hasLiberties(board, x, y + 1, stone, visited) ||
                hasLiberties(board, x, y - 1, stone, visited);
    }

    // Method for capturing stones:
    public static int captureStones(String[][] board, String opponentStone) {
        int capturedStones = 0;
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                if(board[i][j] == opponentStone){
                    boolean[][] visited = new boolean[board.length][board[0].length];
                    if (!hasLiberties(board, i, j, opponentStone, visited)) {
                        board[i][j] = null;
                        capturedStones++;
                    }
                }
            }
        }
        return capturedStones;
    }

    // Method for removing stones from the board:
    public static int removeStones(String[][] board, int x, int y, String stone) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] == null || !board[x][y].equals(stone)) {
            return 0;
        }
        board[x][y] = null;
        return 1  + removeStones(board, x + 1, y, stone) +
                    removeStones(board, x - 1, y, stone) +
                    removeStones(board, x, y + 1, stone) +
                    removeStones(board, x, y - 1, stone);
    }
    
    // Method to clone the board state
    public static void cloneBoard(String[][] source, String[][] destination) {
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                destination[i][j] = source[i][j];
            }
        }
    }

//------------------------------------------------------------------------------------------------
// Other Rules:

    // Method to check if a point is an eye
    public static boolean isEye(String[][] board, int x, int y, String playerStone) {
        if (board[x][y] != null) {
            return false;
        }
        boolean isEye = true;
        if (x > 0 && !board[x - 1][y].equals(playerStone)) isEye = false;
        if (x < board.length - 1 && !board[x + 1][y].equals(playerStone)) isEye = false;
        if (y > 0 && !board[x][y - 1].equals(playerStone)) isEye = false;
        if (y < board[0].length - 1 && !board[x][y + 1].equals(playerStone)) isEye = false;
        return isEye;
    }

    // Method to check for ko
    public static boolean isKo(String[][] currentBoard, String[][] previousBoard) {
        for (int i = 0; i < currentBoard.length; i++) {
            for (int j = 0; j < currentBoard[i].length; j++) {
                if (currentBoard[i][j] != previousBoard[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to check for seki
    public static boolean isSeki(String[][] board, String playerStone, String opponentStone) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null && board[i][j].equals(playerStone)) {
                    if (hasSharedLiberties(board, i, j, playerStone, opponentStone, visited)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    // Method to check if a group has shared liberties with the opponent
    public static boolean hasSharedLiberties(String[][] board, int x, int y, String playerStone, String opponentStone, boolean[][] visited) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) {
            return false;
        }
        if (visited[x][y]) {
            return false;
        }
        visited[x][y] = true;
        if (board[x][y] == null) {
            return true;
        }
        if (board[x][y].equals(opponentStone)) {
            return false;
        }
        return  hasSharedLiberties(board, x + 1, y, playerStone, opponentStone, visited) ||
                hasSharedLiberties(board, x - 1, y, playerStone, opponentStone, visited) ||
                hasSharedLiberties(board, x, y + 1, playerStone, opponentStone, visited) ||
                hasSharedLiberties(board, x, y - 1, playerStone, opponentStone, visited);
    }
}
//------------------------------------------------------------------------------------------------

// Expected Output:
/*
 1 2 3 4 5 6 7 8 9
1|-|-|-|-|-|-|-|-|
2|-|-|-|-|-|-|-|-|
3|-|-|-|-|-|-|-|-|
4|-|-|-|-|-|-|-|-|
5|-|-|-|-|-|-|-|-|
6|-|-|-|-|-|-|-|-|
7|-|-|-|-|-|-|-|-|
8|-|-|-|-|-|-|-|-|
9|-|-|-|-|-|-|-|-|
Enter the x and y coordinates of your move: (Must be between 0 and 8)
1 1
Enter the x and y coordinates of your opponent's move: (Must be between 0 and 8)
0 0
Player Score: 1
Opponent Score: 1
 1 2 3 4 5 6 7 8 9
1-*-|-|-|-|-|-|-|-|
2|-o-|-|-|-|-|-|-|
3|-|-|-|-|-|-|-|-|
4|-|-|-|-|-|-|-|-|
5|-|-|-|-|-|-|-|-|
6|-|-|-|-|-|-|-|-|
7|-|-|-|-|-|-|-|-|
8|-|-|-|-|-|-|-|-|
9|-|-|-|-|-|-|-|-|
Enter the x and y coordinates of your move: (Must be between 0 and 8)
8 8
Enter the x and y coordinates of your opponent's move: (Must be between 0 and 8)
0 1
Player Score: 2
Opponent Score: 2
 1 2 3 4 5 6 7 8 9
1-*-*-|-|-|-|-|-|-|
2|-o-|-|-|-|-|-|-|
3|-|-|-|-|-|-|-|-|
4|-|-|-|-|-|-|-|-|
5|-|-|-|-|-|-|-|-|
6|-|-|-|-|-|-|-|-|
7|-|-|-|-|-|-|-|-|
8|-|-|-|-|-|-|-|-|
9|-|-|-|-|-|-|-|-o

... Some moves later...

Player Score: 7
Opponent Score: 7
 1 2 3 4 5 6 7 8 9
1-*-*-*-|-|-|-|-|-|
2-*-o-|-|-|-|-|-|-|
3-*-*-*-|-|-|-|-|-|
4|-|-|-|-|-|-|-|-|
5|-|-|-|-|-|-|-|-|
6|-|-|-|-|-|-|-|-|
7|-|-|-|-|-|-|-|-|
8|-|-|-|-|-|-|-|-|
9|-|-|-o-o-o-o-o-o
Enter the x and y coordinates of your move: (Must be between 0 and 8)
8 2
Enter the x and y coordinates of your opponent's move: (Must be between 0 and 8)
1 2
Player Score: 7
Opponent Score: 8
 1 2 3 4 5 6 7 8 9
1-*-*-*-|-|-|-|-|-|
2-*-|-*-|-|-|-|-|-|
3-*-*-*-|-|-|-|-|-|
4|-|-|-|-|-|-|-|-|
5|-|-|-|-|-|-|-|-|
6|-|-|-|-|-|-|-|-|
7|-|-|-|-|-|-|-|-|
8|-|-|-|-|-|-|-|-|
9|-|-o-o-o-o-o-o-o
*/