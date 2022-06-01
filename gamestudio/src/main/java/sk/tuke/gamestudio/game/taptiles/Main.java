package sk.tuke.gamestudio.game.taptiles;

import sk.tuke.gamestudio.game.taptiles.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.taptiles.core.Board;

import java.util.Scanner;

public class Main {
    private static Board board;

    public static void main(String[] args) {
        Boolean playing = true;
        Scanner input = new Scanner(System.in);
        while (playing) {
            int sizeValue = -1;
            System.out.println("Set size of your board < 2 - 20 >");
            while (sizeValue < 2 || sizeValue > 20) {
                while (!input.hasNextInt()) {
                    System.out.println("Type number from interval < 2 - 20 >");
                    input.nextLine();
                }
                sizeValue = input.nextInt();

            }


            Board board = new Board(sizeValue);
            ConsoleUI UI = new ConsoleUI(board);
            UI.run();
            int newGame = -1;
            System.out.println("Do you want to play again?\nType 0 for NO or 1 for YES");
            while (newGame != 1 && newGame != 0) {
                while (!input.hasNextInt()) {
                    System.out.println("Type 0 for NO or 1 for YES");
                    input.nextLine();
                }
                newGame = input.nextInt();
            }
            if (newGame == 0)
                playing = false;
        }
    }
}