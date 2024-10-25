//-------------------
// go.java
// By: Zenith Lake
// Date: 10/28/2024
//-------------------

//Tasks:
    // Create a program that will simulate the game of Go
    // Create a two-dimentional array representation of a go Board
    // Create a method that will print the goBoard
    // Create a method that will allow the user to input their move
    // Create a method that will allow the user to input their opponent's move or make the opponent move on their own
    // TODO Create a method that will check if the move is valid
    // TODO Create a method that will correctly detect if a stone is out of bounds
    // TODO Create a method that will keep track of the score
    // TODO Create a method that will keep track of the captured stones
    // TODO Clean up the spaghetti code!!!!!!!!!!!!

// Importing the Scanner class
import java.util.Scanner;

// Creating the Go class
public class go {

    // Creating the goBoard array!
    static String[][] goBoard = new String[9][9];

    // Main method
    public static void main(String[] args) throws Exception {

        while(true){

            // Prints the board
            System.out.println(" 1 2 3 4 5 6 7 8 9");
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

            // Time to create the Scanner object
            Scanner myObj = new Scanner(System.in);

            // Player's move:
            System.out.println("Enter the x and y coordinates of your move: (Must be between 0 and 8) ");
            int moveX = myObj.nextInt();
            int moveY = myObj.nextInt();
            goBoard[moveX][moveY] = "-o"; // Player's stone

            // Opponent's move:
            System.out.println("Enter the x and y coordinates of your opponent's move: (Must be between 0 and 8)");
            int opponentMoveX = myObj.nextInt();
            int opponentMoveY = myObj.nextInt();
            goBoard[opponentMoveX][opponentMoveY] = "-*"; // Opponent's stone

            // Move verification:
            if(goBoard[moveX][moveY] == "-o" || goBoard[opponentMoveX][opponentMoveY] == "-*"){
                //TODO - Check if this works...
                for (int i = 0; i < goBoard.length; i++){
                    for (int j = 0; j < goBoard[i].length; j++){
                        if(goBoard[i][j] == "-o" || goBoard[i][j] == "-*"){
                            if(i < 0 || i > 8 || j < 0 || j > 8){
                                System.out.println("Hey chucklenuts, your move is out of bounds!");
                            }
                        }
                    }
                }
            }

            // insert new stuff here...
        }
    }
}
// Expected Output:

// Enter the coordinates of your move:
// 1 1
// Enter the coordinates of your opponent's move:
// 2 2
//  1 2 3 4 5 6 7 8 9
// 1|-|-|-|-|-|-|-|-|
// 2|-o-|-|-|-|-|-|-|
// 3|-|-*-|-|-|-|-|-|
// 4|-|-|-|-|-|-|-|-|
// 5|-|-|-|-|-|-|-|-|
// 6|-|-|-|-|-|-|-|-|
// 7|-|-|-|-|-|-|-|-|
// 8|-|-|-|-|-|-|-|-|
// 9|-|-|-|-|-|-|-|-|

// Command line argument:
// java Go.java



//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
// Stuff that doesn't work yet:

// // Creating a new go test board array!

// static String[][] goTestBoard1 = {
//     {null, null, "-*", "-*", null, null, null, null, null},
//     {null, "-*", "-o", "-o", "-*", null, null, null, null},
//     {null, "-*", "-o", null, "-o", "-*", null, null, null},
//     {null, "-*", "-o", "-o", "-o", "-*", null, null, null},
//     {null, "-*", "-o", null, "-o", "-*", null, null, null},
//     {null, null, "-*", "-o", "-o", "-*", null, null, null},
//     {null, null, null, "-*", "-*", null, null, null, null},
//     {null, null, null, null, null, null, null, null, null},
//     {null, null, null, null, null, null, null, null, null}
//                                 };

// // Creating other arrays for the game
// static boolean[][] lives = new boolean[9][9];
// static boolean[][] territory = new boolean[9][9];
// static boolean[][] beenChecked = new boolean[9][9];

// // Pseudocode for keeping the pieces alive
// boolean[][] canBreathe(int x, int y) {
            
//     beenChecked[x][y] = true;

//     if (goBoard[x][y] == null) {
//         return true;}
//     if (goBoard[x+1][y] == null) {
//         return true;}
//     if (goBoard[x-1][y] == null) {
//         return true;}
//     if (goBoard[x][y+1] == null) {
//         return true;}
//     if (goBoard[x][y-1] == null) {
//         return true;}
    
//     return false;
    
// }


// and...

// // Detects if a stone is out of bounds
//             // I don't think this works...
//             //TODO - Fix this
//             for (int i = 0; i < goBoard.length; i++){
//                 for (int j = 0; j < goBoard[i].length; j++){
//                     if(goBoard[i][j] == "-o" || goBoard[i][j] == "-*"){
//                         if(i < 0 || i > 8 || j < 0 || j > 8){
//                             System.out.println("This message appears when the out of bounds works!");
//                         }
//                     }
//                 }
//             }

// and...

// There are a few different levels to this problem of Capture though.
            // You not only need to check if a piece just placed is alive,
            // but you also need to check if in placing that piece you have captured the opposing pieces or if you have been captured yourself.
            // This will come into play especially when working with the Go concept of "eyes" which are empty spaces left inside of solid blocks of stones.

            // Method for score keeping:
                //TODO - Create a method to keep track of the score
                // ?????????????????????????????????????????????
            
            // Method to keep track of captured stones:
                //TODO - Create a method to keep track of captured stones
                // ?????????????????????????????????????????????
