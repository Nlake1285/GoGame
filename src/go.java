//-------------------
// go.java
// By: Zenith Lake
//-------------------
//Tasks:
    // Create a program that will simulate a game of Go
    // Create a two-dimentional array representation of a go Board
    // Create a method that will print the goBoard
    // Create a method that will allow the user to input their move
    // Create a method that will allow the user to input their opponent's move or make the opponent move on their own
    // Create a method that will check if the move is valid
    // Create a method that will detect if a stone is out of bounds

// Importing the Scanner class
import java.util.Scanner;

// Creating the Go class
public class go {

    // Creating the goBoard array!
        static String[][] goBoard = new String[9][9];

    // Main method
        public static void main(String[] args) throws Exception {

            while(true){

                // Time to create the Scanner object
                Scanner myObj = new Scanner(System.in);

                // Ask for user input:
                System.out.println("Enter the x and y coordinates of your move: (Must be between 0 and 8) ");
                int moveX = myObj.nextInt();
                int moveY = myObj.nextInt();
                
                // Set the move on the board:
                goBoard[moveX][moveY] = "-o";

                // Set the opponent's move on the board:
                System.out.println("Enter the x and y coordinates of your opponent's move: (Must be between 0 and 8)");
                int opponentMoveX = myObj.nextInt();
                int opponentMoveY = myObj.nextInt();

                // Set the opponent's move on the board:
                goBoard[opponentMoveX][opponentMoveY] = "-*";
                    //TODO - Create a way for the opponent to make their own move without player inputs

                // Verify that the move is valid:
                    if(goBoard[moveX][moveY] == "-o" || goBoard[opponentMoveX][opponentMoveY] == "-*"){
                    System.out.println("Invalid Move");
                }

                // Detects if a stone is out of bounds
            for (int i = 0; i < goBoard.length; i++){
                for (int j = 0; j < goBoard[i].length; j++){
                    if(goBoard[i][j] == "-o" || goBoard[i][j] == "-*"){
                        if(i < 0 || i > 8 || j < 0 || j > 8){
                            System.out.println("Invalid Move");
                        }
                    }
                }
            }
            
                // Prints the numbers on top of the board
                System.out.println(" 1 2 3 4 5 6 7 8 9");

                // Actually prints the board
                for (int i = 0; i < goBoard.length; i++){

                    //Vertical numbers get printed
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