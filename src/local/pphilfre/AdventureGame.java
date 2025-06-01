package local.pphilfre;

import java.io.IOException;
import java.util.Scanner;

/**
 * Main application class for the text adventure game.
 */
public class AdventureGame {

    /**
     * Entry point for the application.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        GameController gameController = new GameController();
        Scanner scanner = new Scanner(System.in);
        
        String worldFilePath = "world.json";
        
        // Allow specifying a different world file as a command-line argument
        if (args.length > 0) {
            worldFilePath = args[0];
        }
        
        try {
            gameController.startGame(worldFilePath);
            
            // Main game loop
            while (!gameController.isGameOver()) {
                System.out.print("\n> ");
                String input = scanner.nextLine();
                
                // Special case for quitting
                if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                    gameController.processCommand(input);
                } else {
                    gameController.processCommand(input);
                }
            }
            
            System.out.println("\nThanks for playing! Goodbye!");
        } catch (IOException e) {
            System.out.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
