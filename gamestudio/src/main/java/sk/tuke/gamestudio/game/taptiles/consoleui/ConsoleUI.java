package sk.tuke.gamestudio.game.taptiles.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.taptiles.core.Board;
import sk.tuke.gamestudio.game.taptiles.core.GameState;
import sk.tuke.gamestudio.game.taptiles.core.TileState;
import sk.tuke.gamestudio.game.taptiles.entity.Comment;
import sk.tuke.gamestudio.game.taptiles.entity.Rating;
import sk.tuke.gamestudio.game.taptiles.entity.Score;
import sk.tuke.gamestudio.game.taptiles.service.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private Board board;
    Boolean beginning = true;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private int[] charConstValues = {400, 989};
    private int charConst;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CommentService commentService;

    public ConsoleUI(Board board, ScoreService scoreService, RatingService ratingService, CommentService commentService) {
        Random rand = new Random();
        this.scoreService = scoreService;
        this.ratingService = ratingService;
        this.commentService = commentService;
        this.board = board;
        charConst = charConstValues[rand.nextInt(charConstValues.length)];
    }

    public ConsoleUI(Board board) {
        Random rand = new Random();
        this.board = board;
        charConst = charConstValues[rand.nextInt(charConstValues.length)];
    }

    private final Pattern INPUT_PATTERN
            = Pattern.compile("(0[1-9]|[1-9][0-9]|[1-9])( )(0[1-9]|[1-9][0-9]|[1-9])");

    public void run() {
        Boolean playing = mainMenu();
        if (!playing)
            return;
        do {
            if (!playing)
                return;
            printBoard();
            printFoot();
            playing = processInput();
            board.updateGameState();

        } while (board.getGameState() == GameState.PLAYING);
        printBoard();

        if (board.getGameState() == GameState.LOSE) {
            System.out.println("    TIME LEFT: " + RED + board.getStartingMillis() + ANSI_RESET + "s");
            System.out.println("  TILES TO GO: " + RED + board.getUnsolvedPairCount() + ANSI_RESET + "x");
            System.out.println(ANSI_BLACK + "      YOU LOST" + ANSI_RESET);
            System.out.println("  TOTAL SCORE: " + ANSI_BLACK + board.getScore() + ANSI_RESET);
        } else {
            System.out.println("    TIME LEFT: " + CYAN_BOLD + board.getStartingMillis() + ANSI_RESET + "s");
            System.out.println("  TILES TO GO: " + CYAN_BOLD + board.getUnsolvedPairCount() + ANSI_RESET + "x");
            System.out.println(ANSI_BLACK + "     YOU WON" + ANSI_RESET);
            System.out.println("  TOTAL SCORE: " + ANSI_BLACK + board.getScore() + ANSI_RESET);
            scoreService.addScore(new Score(System.getProperty("user.name"), "taptiles " + board.getSize(), board.getScore(), new Date()));
            System.out.println("Score has been added to HighScores! ;)");
        }

        readLine();
        mainMenu();
    }


    private Boolean mainMenu() {
        String line;
        if (beginning) {
            beginning = false;
        }

        System.out.println(ANSI_BLACK + BLACK_BACKGROUND_BRIGHT + "         Main Menu        " + ANSI_RESET);
        System.out.println("Type Number " + ANSI_BLACK + "1-6" + ANSI_RESET + ", ? or " + ANSI_BLACK + "X/x" + ANSI_RESET);
        System.out.println(
                ANSI_BLACK + "1" + ANSI_RESET + "  Play game\n" +
                        ANSI_BLACK + "2" + ANSI_RESET + "  Print HighScores.\n" +
                        ANSI_BLACK + "3" + ANSI_RESET + "  Print last comments.\n" +
                        ANSI_BLACK + "4" + ANSI_RESET + "  Print ratings.\n" +
                        ANSI_BLACK + "5" + ANSI_RESET + "  Add comment to game\n" +
                        ANSI_BLACK + "6" + ANSI_RESET + "  Add rating to game\n" +
                        ANSI_BLACK + "?" + ANSI_RESET + "  Print rules\n" +
                        ANSI_BLACK + "x" + ANSI_RESET + "  Exit\n"
        );
        line = readLine();
        switch (line) {
            case ("1"):
                return true;
            case ("2"):
                printHighScores();
                break;
            case ("3"):
                printComments();
                break;
            case ("4"):
                printRatings();
                break;
            case ("5"):
                System.out.println("Adding new " + ANSI_BLACK + "COMMENT" + ANSI_RESET);
                System.out.println("Your name? max 15symbols");
                String name = readLine();
                while (name.length() <= 0 || name.length() > 15) {
                    System.out.println("Name must have between 0 and 15 characters");
                    name = readLine();
                }
                System.out.println("Your comment?");
                String commentText = readLine();
                while (commentText.length() <= 0 || commentText.length() > 250) {
                    System.out.println("Comment must have between 0 and 250 characters");
                    commentText = readLine();
                }
                Comment comment = new Comment(name, "taptiles", commentText, new Date());
                commentService.addComment(comment);
                System.out.println("New comment added");
                break;
            case ("6"):
                System.out.println("Adding new " + ANSI_BLACK + "RATING" + ANSI_RESET);
                System.out.println("Your name? max 15symbols");
                name = readLine();
                while (name.length() <= 0 || name.length() > 15) {
                    System.out.println("Name must have between 0 and 15 characters");
                    name = readLine();
                }
                Scanner in = new Scanner(System.in);
                int ratingValue = -1;
                while (ratingValue < 0 || ratingValue > 5) {
                    System.out.println("How do you rate Taptiles? < 0 - 5 >");
                    while (!in.hasNextInt()) {
                        System.out.println("Type number from interval < 0 - 5 >");
                        in.nextLine();
                    }
                    ratingValue = in.nextInt();
                }
                Rating rating = new Rating(name, "taptiles", ratingValue, new Date());
                ratingService.setRating(rating);
                break;
            case ("?"):
                printRules();
                break;
            case ("x"):
                return false;
            case ("X"):
                return false;
        }
        mainMenu();
        System.out.println();
        return true;
    }


    private void printHighScores() {
        String gameName = "taptiles ";
        int sizeValue = -1;
        Scanner input = new Scanner(System.in);
        System.out.println("Highscore for what size do you want to print? < 2 - 10 >");
        while (sizeValue < 2 || sizeValue > 10) {
            while (!input.hasNextInt()) {
                System.out.println("Type number from interval < 2 - 10 >");
                input.nextLine();
            }
            sizeValue = input.nextInt();
        }

        List<Score> scores = scoreService.getTopScores(gameName + sizeValue);
        System.out.println(ANSI_BLACK + CYAN_BACKGROUND + "       Highscores " + gameName + " board size " + sizeValue + "     " + ANSI_RESET);
        System.out.println(ANSI_BLACK + BLACK_BACKGROUND_BRIGHT + "NAME         POINTS  DATE                   " + ANSI_RESET);
        for (int i = 0; i < scores.size(); i++) {
            System.out.print(ANSI_BLACK + scores.get(i).getPlayer() + ANSI_RESET);
            for (int j = 0; j < 13 - scores.get(i).getPlayer().length(); j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI_BLACK + scores.get(i).getPoints() + ANSI_RESET);
            for (int j = 0; j < 8 - intLength(scores.get(i).getPoints()); j++) {
                System.out.print(" ");
            }
            System.out.print(ANSI_BLACK + scores.get(i).getPlayedOn() + ANSI_RESET);
            System.out.println();
        }
        System.out.println("Type anything to continue");
        readLine();
    }

    private void printComments() {
        String gameName = "taptiles";
        List<Comment> comments = commentService.getComments(gameName);
        System.out.println(ANSI_BLACK + CYAN_BACKGROUND + "         Comments for game " + gameName + "         " + ANSI_RESET);
        System.out.println(ANSI_BLACK + BLACK_BACKGROUND_BRIGHT + "NAME         COMMENT                        " + ANSI_RESET);
        if (comments != null) {
            for (int i = 0; i < comments.size(); i++) {
                System.out.print(ANSI_BLACK + comments.get(i).getPlayer() + ANSI_RESET);
                for (int j = 0; j < 13 - comments.get(i).getPlayer().length(); j++) {
                    System.out.print(" ");
                }
                System.out.print(ANSI_BLACK + comments.get(i).getComment() + ANSI_RESET);
                System.out.println();
            }
        }
        System.out.println("Type anything to continue");
        readLine();
    }

    private void printRatings() {
        String gameName = "taptiles";
        int rating = ratingService.getRating(gameName, System.getProperty("user.name"));
        System.out.println(ratingService.getRating(gameName, System.getProperty("user.name")));
        System.out.println(ANSI_BLACK + CYAN_BACKGROUND + "     Ratings for game " + gameName + "    " + ANSI_RESET);
        System.out.println(ANSI_BLACK + BLACK_BACKGROUND_BRIGHT + "  NAME       RATING               " + ANSI_RESET);

        System.out.print(ANSI_BLACK + System.getProperty("user.name") + ANSI_RESET);
        for (int j = 0; j < 15 - System.getProperty("user.name").length(); j++) {
            System.out.print(" ");
        }
        System.out.print(ANSI_BLACK + rating + ANSI_RESET);
        System.out.print("       ");
        for (int j = 0; j < rating; j++) {
            System.out.print(ANSI_CYAN + "⬤ " + ANSI_RESET);
        }
        for (int j = 0; j < 5 - rating; j++) {
            System.out.print(ANSI_BLACK + "◌ " + ANSI_RESET);
        }
        System.out.println();
        int averageRating = ratingService.getAverageRating(gameName);
        System.out.print(ANSI_BLACK + "AVERAGE RATING " + averageRating + "     " + ANSI_RESET);
        for (int j = 0; j < averageRating; j++) {
            System.out.print(ANSI_CYAN + "⬤ " + ANSI_RESET);
        }
        int circlesLeft = 5 - averageRating;
        for (int j = 0; j < circlesLeft; j++) {
            System.out.print(ANSI_BLACK + "◌ " + ANSI_RESET);
        }
        System.out.println();

        System.out.println("Type anything to continue");
        readLine();
    }

    private int intLength(int value) {
        int temp = 1;
        int length = 0;
        while (temp <= value) {
            temp *= 10;
            length++;
        }
        if (length == 0) {
            length = 1;
        }
        return length;
    }

    private boolean processInput() {
        String line = readLine();
        if (line.equals("R") | line.equals("r")) {
            System.out.println(CYAN_BOLD + "ROTATION RIGHT" + ANSI_RESET);
            board.changeView(1);
        } else if (line.equals("L") | line.equals("l")) {
            System.out.println(CYAN_BOLD + "ROTATION LEFT" + ANSI_RESET);
            board.changeView(-1);
        } else if (line.equals("X") | line.equals("x")) {
            return false;
        } else if (line.equals("S") | line.equals("s")) {
            if (board.makeShuffle()) {
                System.out.println(CYAN_BOLD + "TILES SHUFFLED" + ANSI_RESET);
            } else {
                System.out.println(RED + "NO AVAILABLE SHUFFLES" + ANSI_RESET);
            }
        } else if (line.equals("M") | line.equals("m")) {
            return mainMenu();
        } else {
            Matcher m = INPUT_PATTERN.matcher(line);
            if (m.matches()) {
                String rowTemp = m.group(1);
                String columnTemp = m.group(3);
                int row = 0;
                int column = 0;
                for (int i = 0; i < rowTemp.length(); i++) {
                    row = row * 10 + (int) rowTemp.charAt(i) - 48;
                }
                for (int i = 0; i < columnTemp.length(); i++) {
                    column = column * 10 + (int) columnTemp.charAt(i) - 48;
                }
                row = row - 1;
                column = column - 1;
                if (board.coordinatesToMark(row, column)) {
                    System.out.println(CYAN_BOLD + "TILE MARKED" + ANSI_RESET);
                } else
                    System.out.println(RED + "WRONG COORDINATES" + ANSI_RESET);
            }
        }
        return true;
    }


    private String readLine() {
        try {
            return br.readLine();
        } catch (IOException e) {
            System.err.println("Wrong input. Try again!");
            return "";
        }
    }

    private void printRules() {

        System.out.println(
                "                 Rules\n" +
                        "1. Match 2 tiles with the same symbol \n" +
                        "2. You can  mark only tile  in corner\n" +
                        "     undercolored with cyan color " + ANSI_BLACK + CYAN_BACKGROUND + " # " + ANSI_RESET + "\n" +
                        "3. Type 2 numbers separated with space\n" +
                        "    to mark desired tile from interval\n" +
                        ANSI_BLACK + "              < 01 - " + board.getSize() + " >\n" + ANSI_RESET +
                        "     Valid Input e.g. /01 " + board.getSize() + "/ /" + (board.getSize() - board.getSize() / 2) + " " + ((board.getSize() - board.getSize() / 2) + (board.getSize() / 4)) + "/\n" +
                        "4. You can rotate board typing R/r L/l\n" +
                        ANSI_BLACK + "    R - rotate right  L - rotate left\n" + ANSI_RESET +
                        "5. If you can't find matching tiles\n" +
                        ANSI_BLACK + "         type S/s for shuffle\n" + ANSI_RESET +
                        "   You have limited shuffles per game\n" +
                        "6. Tiles with X symbol are unavailable\n" +
                        "   You don't need to match them to win\n" +
                        "7. Tiles with X symbol are unavailable\n" +
                        "   You don't need to match them to win\n" +
                        "8. Marked tile changes color to white\n" +
                        "9. If you matched tiles correctly you\n" +
                        "      will be able to see new tiles\n" +
                        "10. You get combo points if you open\n" +
                        "            within x seconds\n" +
                        "11.  Your final score is equal to \n"
                        + ANSI_BLACK + "          time left + score\n" +

                        "   After you match all tiles you WIN!\n" +
                        "              GOOD LUCK ;)\n" + ANSI_RESET
        );

    }


    private void printFoot() {
        System.out.println();
        System.out.println(" Current view: " + ANSI_BLACK + board.getView() + ANSI_RESET);
        System.out.println("Shuffles left: " + ANSI_BLACK + board.getShuffleCount() + ANSI_RESET);
        System.out.println("        SCORE: " + ANSI_BLACK + board.getScore() + ANSI_RESET);
        System.out.println("    TIME LEFT: " + ANSI_BLACK + board.getStartingMillis() + ANSI_RESET + "s");
        System.out.println("  PAIRS TO GO: " + ANSI_BLACK + board.getUnsolvedPairCount() + ANSI_RESET + "x");

        System.out.print("  Marked Tile: ");
        for (int markedTileIndex = 0; markedTileIndex < board.getMarkedTile().size(); markedTileIndex++) {
            System.out.print(BLACK_BACKGROUND_BRIGHT + ANSI_BLACK + " " + (char) (board.getMarkedTile().get(markedTileIndex).getValue() + charConst) + " " + ANSI_RESET);
            System.out.print(" ");
        }
        System.out.println("\nM/m - main menu X/x - exit\nType column + row");


    }

    private void printBoard() {
        printColumns();
        printTiles();
        printColumns();
    }

    private void printColumns() {
        System.out.print(" +");
        for (int column = 1; column <= board.getSize(); column++) {
            if (column >= 10) {
                System.out.print(" " + column + " ");
            } else
                System.out.print("  " + column + " ");
        }
        System.out.print(" +");
        System.out.println();

    }

    private void printRows(int layer, String side) {
        if (side == "right") {
            side = " ";
        } else
            side = "";
        if (layer >= 9) {
            System.out.print(side + (layer + 1));
        } else {
            System.out.print(" " + (layer + 1));
        }
    }

    private void printTiles() {
        switch (board.getView()) {
            case FRONT:
                for (int layer = 0; layer < board.getSize(); layer++) {
                    printRows(layer, "left");
                    int lastLayer = 0;
                    for (int column = 0; column < board.getSize(); column++) {
                        int row = 0;
                        boolean printed = false;
                        while (row < board.getSize()) {
                            if (board.getTile(row, column, layer).getTileState() != TileState.SOLVED) {
                                printTile(row, column, layer, newLayer(lastLayer, row, column, true));
                                printed = true;
                                lastLayer = row;
                            } else if (board.getTile(row, column, layer).getTileState() == TileState.SOLVED) {
                                row++;
                            }
                            if (!printed && row == board.getSize()) {
                                System.out.print("    ");
                                lastLayer = board.getSize() - 1;
                                break;
                            } else if (printed) {
                                break;
                            }
                        }
                    }
                    printRows(layer, "right");
                    System.out.println();
                }
                break;
            case BACK:
                for (int layer = 0; layer < board.getSize(); layer++) {
                    printRows(layer, "left");
                    int lastLayer = board.getSize() - 1;
                    for (int column = board.getSize() - 1; column >= 0; column--) {
                        int row = board.getSize() - 1;
                        boolean printed = false;
                        while (row >= 0) {
                            if (board.getTile(row, column, layer).getTileState() != TileState.SOLVED) {
                                printTile(row, column, layer, newLayer(lastLayer, row, column, false));
                                printed = true;
                                lastLayer = row;
                            } else if (board.getTile(row, column, layer).getTileState() == TileState.SOLVED) {
                                row--;
                            }
                            if (!printed && row == -1) {
                                System.out.print("    ");
                                lastLayer = 0;
                                break;
                            } else if (printed) {
                                break;
                            }
                        }
                    }
                    printRows(layer, "right");
                    System.out.println();
                }
                break;
            case LEFT:
                for (int layer = 0; layer < board.getSize(); layer++) {
                    printRows(layer, "left");
                    int lastLayer = 0;
                    for (int row = board.getSize() - 1; row >= 0; row--) {
                        int column = 0;
                        boolean printed = false;
                        while (column < board.getSize()) {
                            if (board.getTile(row, column, layer).getTileState() != TileState.SOLVED) {
                                printTile(row, column, layer, newLayer(lastLayer, column, row, false));
                                printed = true;
                                lastLayer = column;
                            } else if (board.getTile(row, column, layer).getTileState() == TileState.SOLVED) {
                                column++;
                            }
                            if (!printed && column == board.getSize()) {
                                System.out.print("    ");
                                lastLayer = board.getSize() - 1;
                                break;
                            } else if (printed) {
                                break;
                            }
                        }
                    }
                    printRows(layer, "right");
                    System.out.println();
                }
                break;
            case RIGHT:
                for (int layer = 0; layer < board.getSize(); layer++) {
                    printRows(layer, "left");
                    int lastLayer = board.getSize() - 1;
                    for (int row = 0; row < board.getSize(); row++) {
                        int column = board.getSize() - 1;
                        boolean printed = false;
                        while (column >= 0) {
                            if (board.getTile(row, column, layer).getTileState() != TileState.SOLVED) {
                                printTile(row, column, layer, newLayer(lastLayer, column, row, true));
                                printed = true;
                                lastLayer = column;
                            } else if (board.getTile(row, column, layer).getTileState() == TileState.SOLVED) {
                                column--;
                            }
                            if (!printed && column == -1) {
                                System.out.print("    ");
                                lastLayer = 0;
                                break;
                            } else if (printed) {
                                break;
                            }
                        }
                    }
                    printRows(layer, "right");
                    System.out.println();
                }
                break;
        }
    }


    private boolean newLayer(int lastLayer, int thisLayer, int iteration, boolean positive) {
        boolean isNewLayer;
        if (lastLayer == thisLayer) {
            isNewLayer = false;
        } else if ((positive && iteration == 0) || (!positive && iteration == board.getSize() - 1)) {
            isNewLayer = false;
        } else
            isNewLayer = true;
        return isNewLayer;
    }

    private void printTile(int row, int column, int layer, boolean newLayer) {
        String border;
        if (newLayer == true) {
            border = "┊";
        } else
            border = " ";
        if (board.getTile(row, column, layer).getTileState() == TileState.MARKED) {
            System.out.print(border + BLACK_BACKGROUND + CYAN_BOLD + " " + (char) (board.getTile(row, column, layer).getValue() + charConst) + " " + ANSI_RESET);
        } else {
            if (board.getTile(row, column, layer).getValue() >= 0) {
                if (board.getTile(row, column, layer).getTileState() == TileState.ACCESSIBLE) {
                    System.out.print(border + ANSI_BLACK + CYAN_BACKGROUND + " " + (char) (board.getTile(row, column, layer).getValue() + charConst) + " " + ANSI_RESET);
                } else
                    System.out.print(border + BLACK_BACKGROUND_BRIGHT + ANSI_WHITE + " " + (char) (board.getTile(row, column, layer).getValue() + charConst) + " " + ANSI_RESET);
            } else
                System.out.print("  X ");
        }
    }


    private static final String ANSI_BLACK = "\033[0;97m";
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String BLACK_BACKGROUND_BRIGHT = "\033[0;100m";// BLACK
    public static final String CYAN_BACKGROUND = "\033[46m";   // CYAN
    private static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String BLACK_BACKGROUND = "\033[40m";  // BLACK
    public static final String RED = "\033[0;31m";
    public static final String ANSI_CYAN = "\u001B[36m";

}

