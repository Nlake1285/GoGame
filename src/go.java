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
            System.out.println(" 1 2 3 4 5 6 7 8 9"); // Print the column numbers
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
// Excess of comments are for a few reasons
// 1. To make the code more readable
// 2. To make the code more understandable
// 3. To make the code more maintainable

    // Method for score keeping:
    public static int calculateScore(String[][] board, String playerStone) {
        int score = 0; // Initialize the score
        for (int i = 0; i < board.length; i++){ // Loop through the board
            for (int j = 0; j < board[i].length; j++){ // Loop through the board...again
                if(board[i][j] == playerStone){ // If the stone is the player's stone
                    score++; // Increment the score
                }
            }
        }
        return score;
    }

    // Method to calculate the territory
    public static int calculateTerritory(String[][] board, String playerStone) { // Calculate the territory
        boolean[][] visited = new boolean[board.length][board[0].length]; // Create a visited array
        int territory = 0; // Initialize the territory
        for (int i = 0; i < board.length; i++) { // Loop through the board
            for (int j = 0; j < board[i].length; j++) { // Loop through the board...again
                if (board[i][j] == null && !visited[i][j]) { // If the space is empty and not visited
                    int[] result = exploreTerritory(board, i, j, playerStone, visited); // Explore the territory
                    if (result[1] == 1) { // If the territory is owned by the player
                        territory += result[0]; // Increment the territory
                    }
                }
            }
        }
        return territory; // Return the territory
    }

    // Method to explore a territory
    public static int[] exploreTerritory(String[][] board, int x, int y, String playerStone, boolean[][] visited) { // Explore the territory
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || visited[x][y]) { // If the point is out of bounds or visited
            return new int[]{0, 1}; // Return 0 territory and 1 ownership
        }
        visited[x][y] = true; // Mark the point as visited
        if (board[x][y] != null) { // If the point is not empty
            return new int[]{0, board[x][y].equals(playerStone) ? 1 : -1}; // Return 0 territory and ownership based on the stone
        }
        int[] up = exploreTerritory(board, x - 1, y, playerStone, visited); // Explore the territory up
        int[] down = exploreTerritory(board, x + 1, y, playerStone, visited); // Explore the territory down
        int[] left = exploreTerritory(board, x, y - 1, playerStone, visited); // Explore the territory left
        int[] right = exploreTerritory(board, x, y + 1, playerStone, visited); // Explore the territory right
        int territory = 1 + up[0] + down[0] + left[0] + right[0]; // Calculate the territory
        int ownership = up[1] == down[1] && down[1] == left[1] && left[1] == right[1] ? up[1] : 0; // Calculate the ownership
        return new int[]{territory, ownership}; // Return the territory and ownership
    }

    // Method to check if a group of stones has any liberties:
    public static boolean hasLiberties(String[][] board, int x, int y, String stone, boolean[][] visited) { // Check if a group of stones has liberties
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) { // If the point is out of bounds, don't let a player place a piece there
            return false;
        }
        if (board[x][y] == null) { // If the point is empty, let a player place a stone there
            return true;
        }
        if (visited[x][y] || !board[x][y].equals(stone)) { // If the point is visited or not the player's stone, don't let a player place a piece there
            return false;
        }
        visited[x][y] = true; // Mark the point as visited
        return  hasLiberties(board, x + 1, y, stone, visited) || // Check for liberties up
                hasLiberties(board, x - 1, y, stone, visited) || // Check for liberties down
                hasLiberties(board, x, y + 1, stone, visited) || // Check for liberties left
                hasLiberties(board, x, y - 1, stone, visited);   // Check for liberties right
    }

    // Method for capturing stones:
    public static int captureStones(String[][] board, String opponentStone) {
        int capturedStones = 0; // Initialize the captured stones
        for (int i = 0; i < board.length; i++){ // Loop through the board
            for (int j = 0; j < board[i].length; j++){ // Loop through the board...again
                if(board[i][j] == opponentStone){ // If the stone is the opponent's stone
                    boolean[][] visited = new boolean[board.length][board[0].length]; // Create a visited array
                    if (!hasLiberties(board, i, j, opponentStone, visited)) { // If the group of stones has no liberties
                        board[i][j] = null; // Remove the stone
                        capturedStones++; // Increment the captured stones
                    }
                }
            }
        }
        return capturedStones;
    }

    // Method for removing stones from the board:
    public static int removeStones(String[][] board, int x, int y, String stone) {
        // If the point is out of bounds or empty or not the player's stone, return 0
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length || board[x][y] == null || !board[x][y].equals(stone)) {
            return 0;
        }
        board[x][y] = null; // Remove the stone
        return 1  + removeStones(board, x + 1, y, stone) + // Remove the stone up
                    removeStones(board, x - 1, y, stone) + // Remove the stone down
                    removeStones(board, x, y + 1, stone) + // Remove the stone left
                    removeStones(board, x, y - 1, stone);  // Remove the stone right
    }
    
    // Method to clone the board state
    public static void cloneBoard(String[][] source, String[][] destination) {
        for (int i = 0; i < source.length; i++) { // Loop through the board
            for (int j = 0; j < source[i].length; j++) { // Loop through the board...again
                destination[i][j] = source[i][j]; // Clone the board
            }
        }
    }

//------------------------------------------------------------------------------------------------
// Other Rules:

    // Method to check if a point is an eye
    public static boolean isEye(String[][] board, int x, int y, String playerStone) {
        if (board[x][y] != null) { // If a cell is not empty, flag it as
            return false;
        }
        boolean isEye = true; // Initialize the "eye"
        if (x > 0 && !board[x - 1][y].equals(playerStone)) isEye = false; // Check for the player's stone up
        if (x < board.length - 1 && !board[x + 1][y].equals(playerStone)) isEye = false; // Check for the player's stone down
        if (y > 0 && !board[x][y - 1].equals(playerStone)) isEye = false; // Check for the player's stone left
        if (y < board[0].length - 1 && !board[x][y + 1].equals(playerStone)) isEye = false; // Check for the player's stone right
        return isEye;
    }

    // Method to check for ko
    public static boolean isKo(String[][] currentBoard, String[][] previousBoard) {
        for (int i = 0; i < currentBoard.length; i++) { // Loop through the board
            for (int j = 0; j < currentBoard[i].length; j++) { // Loop through the board...again
                if (currentBoard[i][j] != previousBoard[i][j]) { // If the current board is different from the previous board, return false
                    return false;
                }
            }
        }
        return true;
    }

    // Method to check for seki
    public static boolean isSeki(String[][] board, String playerStone, String opponentStone) {
        boolean[][] visited = new boolean[board.length][board[0].length]; // Create a visited array
        for (int i = 0; i < board.length; i++) { // Loop through the board
            for (int j = 0; j < board[i].length; j++) { // Loop through the board...again
                if (board[i][j] != null && board[i][j].equals(playerStone)) { // If the cell is not empty and is the player's stone
                    if (hasSharedLiberties(board, i, j, playerStone, opponentStone, visited)) { // If the group of stones has shared liberties with the opponent
                        return true; // it is Seki!
                    }
                }
            }
        }
        return false;
    }
    
    // Method to check if a group has shared liberties with the opponent
    public static boolean hasSharedLiberties(String[][] board, int x, int y, String playerStone, String opponentStone, boolean[][] visited) {
        if (x < 0 || x >= board.length || y < 0 || y >= board[0].length) { // If the liberty is out of bounds, then discount it
            return false;
        }
        if (visited[x][y]) { // If the liberty is visited, then discount it
            return false;
        }
        visited[x][y] = true;
        if (board[x][y] == null) { // If the liberty is empty, then it is a shared liberty
            return true;
        }
        if (board[x][y].equals(opponentStone)) { // If the liberty is the opponent's stone, then discount it
            return false;
        }
        return  hasSharedLiberties(board, x + 1, y, playerStone, opponentStone, visited) || // Check for shared liberties up
                hasSharedLiberties(board, x - 1, y, playerStone, opponentStone, visited) || // Check for shared liberties down
                hasSharedLiberties(board, x, y + 1, playerStone, opponentStone, visited) || // Check for shared liberties left
                hasSharedLiberties(board, x, y - 1, playerStone, opponentStone, visited);   // Check for shared liberties right
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