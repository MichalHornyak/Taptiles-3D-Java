package sk.tuke.gamestudio.game.taptiles.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;


public class Board {
    private final int size;
    private int score;
    private long startingMillis;
    private long endingMillis;
    private long comboMillis;
    private boolean shuffle = true;
    private int remainingShuffles;
    private final Tile[][][] tiles;
    private final int variantCount;
    private GameState gameState;
    private int unsolvedPairCount;
    private int accessibility[][][];
    private View view;
    private List<accessiblePosition> accessiblePositions = new ArrayList<accessiblePosition>();
    private List<Tile> markedTile = new ArrayList<Tile>();
    private List<Tile> shuffleTiles = new ArrayList<Tile>();

    class accessiblePosition {
        int row;
        int column;
        int layer;

        public accessiblePosition(int row, int column, int layer) {
            this.row = row;
            this.column = column;
            this.layer = layer;
        }
    }

    public Board(int size) {
        this.size = size;
        this.tiles = new Tile[size][size][size];
        this.accessibility = new int[size][size][size];
        this.remainingShuffles = 3;
        this.score = 0;
        gameState = GameState.PLAYING;
        view = View.FRONT;
        variantCount = size * size;
        unsolvedPairCount = 0;
        generateBoard();
    }

    // Method creates board, fills it with random tiles and starts timer.
    private void generateBoard() {
        entryAccessibility();
        fillPairs();
        shuffle = false;
        comboMillis = System.currentTimeMillis();
        startingMillis = System.currentTimeMillis();
        startingAccessibility();
    }


    /*
       Board is a cube full of tiles according to SIZE variable (size^3).
       In order to always create even count of tiles starting accessibility
       disables (puts -2) in the middle of cube when SIZE is odd.
       Result of this method is how tiles with same value will be put in the board.
    */
    private void startingAccessibility() {
        for (int layer = 0; layer < getSize(); layer++) {
            for (int row = 0; row < getSize(); row++) {
                for (int column = 0; column < getSize(); column++) {
                    if ((getSize() % 2 == 1 && (row == getSize() / 2 && column == getSize() / 2))) {
                        accessibility[layer][row][column] = -2;
                    } else
                        accessibility[layer][row][column] = 2;
                }
            }
        }

        for (int layer = 0; layer < getSize(); layer++) {
            for (int row = 0; row < getSize(); row++) {
                for (int column = 0; column < getSize(); column++) {
                    if (isCornerTile(row, column, layer)) {
                        accessibility[layer][row][column] = 1;
                        tiles[layer][row][column].setTileState(TileState.ACCESSIBLE);
                    }
                }
            }
        }
    }


    //After filling pairs this method sets accessibility to starting position
    private void entryAccessibility() {
        for (int layer = 0; layer < getSize(); layer++) {
            for (int row = 0; row < getSize(); row++) {
                for (int column = 0; column < getSize(); column++) {
                    if ((getSize() % 2 == 1 && (row == getSize() / 2 && column == getSize() / 2))) {
                        accessibility[layer][row][column] = -2;
                        tiles[layer][row][column] = new Tile(-2, TileState.INACCESSIBLE);
                    } else {
                        if ((row == 0 || row == getSize() - 1) && (column == 0 || column == getSize() - 1)) {
                            accessibility[layer][row][column] = 1;
                            accessiblePosition coordinates = new accessiblePosition(row, column, layer);
                            accessiblePositions.add(coordinates);
                        } else {
                            accessibility[layer][row][column] = 2;
                        }
                    }
                }
            }
        }
    }


    /*
        Method called in the 2nd part of creating board.
        It fills empty accessible places until there are accessiblePositions available
        For every pair it generates, it increases unsolvedPairCount.
    */
    private void fillPairs() {
        while (!accessiblePositions.isEmpty()) {
            createPair();
            unsolvedPairCount++;
        }
    }

    /*
        According to variantCount, puts 2 tiles with same value to ACCESSIBLE place on board, and updates
        places next to it (in real game if these 2 tiles would be solved, player would be able to access position next to it).
        This method guarantee 100% winnable board.
    */
    private void createPair() {
        Random rand = new Random();
        int pairValue = rand.nextInt(variantCount);
        for (int i = 0; i < 2; i++) {
            if (!accessiblePositions.isEmpty()) {
                accessiblePosition coordinates = accessiblePositions.get(rand.nextInt(accessiblePositions.size()));
                tiles[coordinates.layer][coordinates.row][coordinates.column] = new Tile(pairValue, TileState.INACCESSIBLE);
                accessibility[coordinates.layer][coordinates.row][coordinates.column] = 0;
                updateAccessibleTiles(coordinates.row, coordinates.column, coordinates.layer);
                accessiblePositions.remove(coordinates);
            }
        }
    }

    /*
        If there are any remaining shuffles, method reorganizes tiles on board.
        1. Creates a copy of which places were accessible.
        2. All tiles from tiles[][][] except SOLVED are put into list of Tiles.
        3. Tiles from the list with the same values are put back on the board.
        4. Tile accessibility is updated according to copy created at the beginning.
    */
    public boolean makeShuffle() {
        if (getGameState() == GameState.PLAYING) {
            if (remainingShuffles > 0) {
                if (!markedTile.isEmpty())
                    markedTile.remove(0);
                shuffle = true;
                int tempAccessibility[][][] = new int[getSize()][getSize()][getSize()];
                for (int layer = 0; layer < getSize(); layer++) {
                    for (int row = 0; row < getSize(); row++) {
                        for (int column = 0; column < getSize(); column++) {
                            int value = accessibility[layer][row][column];
                            tempAccessibility[layer][row][column] = value;
                        }
                    }
                }
                accessiblePositions.clear();
                cleanPositions();
                fillShuffledPairs();
                accessibility = tempAccessibility;
                updateShuffledTiles();
                shuffle = false;
                remainingShuffles--;
                return true;
            }
        }
        return false;
    }


    //Method called to put shuffled pairs back on the board.
    private void fillShuffledPairs() {
        while (!accessiblePositions.isEmpty()) {
            putShuffledPair();
        }
    }


    /*
        Method that saves tiles before shuffling and remove it from tiles[][][] temporary.
        It also create first accessible coordinates.
    */
    private void cleanPositions() {
        for (int layer = 0; layer < getSize(); layer++) {
            for (int row = 0; row < getSize(); row++) {
                for (int column = 0; column < getSize(); column++) {
                    if (accessibility[layer][row][column] > 0) {
                        shuffleTiles.add(tiles[layer][row][column]);
                        tiles[layer][row][column] = null;
                    }
                    if (accessibility[layer][row][column] == 1) {
                        accessiblePosition coordinates = new accessiblePosition(row, column, layer);
                        accessiblePositions.add(coordinates);
                    }
                }
            }
        }
    }


    /*
        Function similiar to createPair
        It gets first tile from shuffled list of tiles, get its value and put it on random accessible position.
        In second iteration tile with same value from list of tiles is found and put it on 2nd random accessible position.
    */
    private void putShuffledPair() {
        Random rand = new Random();
        Tile tilePair = null;
        for (int pair = 0; pair < 2; pair++) {
            if (!shuffleTiles.isEmpty()) {
                accessiblePosition coordinates = accessiblePositions.get(rand.nextInt(accessiblePositions.size()));
                if (pair == 0) {
                    tilePair = shuffleTiles.get(0);
                    tiles[coordinates.layer][coordinates.row][coordinates.column] = tilePair;
                    shuffleTiles.remove(tilePair);
                } else {
                    int position = 0;
                    for (int i = 0; i < shuffleTiles.size(); i++) {
                        if (tilePair.getValue() == shuffleTiles.get(i).getValue()) {
                            position = i;
                            break;
                        }
                    }
                    tiles[coordinates.layer][coordinates.row][coordinates.column] = shuffleTiles.get(position);
                    shuffleTiles.remove(position);
                }
                accessibility[coordinates.layer][coordinates.row][coordinates.column] = 0;
                updateAccessibleTiles(coordinates.row, coordinates.column, coordinates.layer);
                accessiblePositions.remove(coordinates);
            }
        }
    }


    /*
        Method updates accessibility of shuffled tiles with stored accessibility before shuffling
    */
    private void updateShuffledTiles() {
        for (int layer = 0; layer < getSize(); layer++) {
            for (int row = 0; row < getSize(); row++) {
                for (int column = 0; column < getSize(); column++) {
                    if (accessibility[layer][row][column] == 1) {
                        tiles[layer][row][column].setTileState(TileState.ACCESSIBLE);
                    } else if (accessibility[layer][row][column] == 2) {
                        tiles[layer][row][column].setTileState(TileState.INACCESSIBLE);
                    }
                }
            }
        }
    }

    //Returns tile according to its coordinates on board. If not found return null
    public Tile getTile(int row, int column, int layer) {
        if ((row >= 0 && row < getSize()) && (column >= 0 && column < getSize()) && (layer >= 0 && layer < getSize())) {
            return tiles[layer][row][column];
        }
        return null;
    }

    /*
        Method is called always when tile gets solved or programme is generating  board.
        It set tile to ACCESSIBLE state and updates accessibility[][][] array.
    */
    private void updateAccessibleTiles(int row, int column, int layer) {
        int inDirection = 1;
        for (int i = 0; i < 2; i++) {
            if (row + inDirection > 0 && row + inDirection < getSize() - 1 && accessibility[layer][row + inDirection][column] == 2) {
                if (isCornerTile(row + inDirection, column, layer)) {
                    if (shuffle) {
                        accessiblePositions.add(new accessiblePosition(row + inDirection, column, layer));
                    } else {
                        tiles[layer][row + inDirection][column].setTileState(TileState.ACCESSIBLE);
                    }
                    accessibility[layer][row + inDirection][column] = 1;
                }
            }
            if (column + inDirection > 0 && column + inDirection < getSize() - 1 && accessibility[layer][row][column + inDirection] == 2) {
                if (isCornerTile(row, column + inDirection, layer)) {
                    if (shuffle) {
                        accessiblePositions.add(new accessiblePosition(row, column + inDirection, layer));
                    } else {
                        tiles[layer][row][column + inDirection].setTileState(TileState.ACCESSIBLE);
                    }
                    accessibility[layer][row][column + inDirection] = 1;
                }
            }
            inDirection = -1;
        }
    }


    /*
        Method returns true or false depending on tile, if it is in corner of the board.
        Tile is in corner if there is NO tiles or tiles are SOLVED on 2 sides (up + left/right or down +left/right)

    */
    private boolean isCornerTile(int row, int column, int layer) {
        int horizontalValue = 1;
        int corner;
        for (int i = 0; i < 2; i++) {
            corner = 0;
            if ((row + horizontalValue < 0 || row + horizontalValue >= getSize())) {
                corner++;
            } else if (row + horizontalValue >= 0 && row + horizontalValue < getSize() && (accessibility[layer][row + horizontalValue][column] == 0)) {
                corner++;
            }
            int verticalValue = 1;
            for (int j = 0; j < 2; j++) {
                if ((column + verticalValue < 0 || column + verticalValue >= getSize())) {
                    corner++;
                } else if (column + verticalValue >= 0 && column + verticalValue < getSize() && (accessibility[layer][row][column + verticalValue] == 0)) {
                    corner++;
                }
                verticalValue = -1;
            }
            horizontalValue = -1;
            if (corner == 2)
                return true;
        }
        return false;
    }

    /*
        Method updates Tiles tileState to MARKED and put it in marked tile list.
        If there are 2 tiles compareTiles method is called in order to compare marked tiles.
    */
    public boolean markTile(int row, int column, int layer) {
        if (getGameState() != GameState.PLAYING) {
            if (markedTile.size() != 0)
                markedTile.remove(0);
            return false;
        } else {
            if (getTile(row, column, layer).getTileState() == TileState.INACCESSIBLE) {
                return false;
            }
            if (getTile(row, column, layer).getTileState() == TileState.MARKED) {
                getTile(row, column, layer).setTileState(TileState.ACCESSIBLE);
                markedTile.remove(getTile(row, column, layer));
            } else if (getTile(row, column, layer).getTileState() == TileState.ACCESSIBLE) {
                getTile(row, column, layer).setTileState(TileState.MARKED);
                markedTile.add(getTile(row, column, layer));
            }
            if (markedTile.size() == 2) {
                compareTiles();
            }
            return true;
        }
    }

    /*
        If values of marked tiles are equal, their tile state is set to Solved, and score is counted according to
        time of combo.
        If values are are different tiles dont match and are set to tile state ACCESSIBLE again.
        In both situations tiles are deleted from list of marked tiles.
    */
    private void compareTiles() {
        if (markedTile.get(0).getValue() == markedTile.get(1).getValue()) {
            removeTile(getCoordinates(markedTile.get(1))[0], getCoordinates(markedTile.get(1))[1], getCoordinates(markedTile.get(1))[2]);
            removeTile(getCoordinates(markedTile.get(0))[0], getCoordinates(markedTile.get(0))[1], getCoordinates(markedTile.get(0))[2]);
            if ((System.currentTimeMillis() - comboMillis) / 1000 < 3) {
                score = score + 4;
            } else if ((System.currentTimeMillis() - comboMillis) / 1000 < 6) {
                score = score + 2;
            } else
                score = score + 1;
            comboMillis = System.currentTimeMillis();
            unsolvedPairCount--;
        } else {
            markedTile.get(0).setTileState(TileState.ACCESSIBLE);
            markedTile.get(1).setTileState(TileState.ACCESSIBLE);
        }
        for (int i = 1; i >= 0; i--) {
            markedTile.remove(0);
        }
    }

    /*
        Method updates accessibility[][][], set tile state of matching pair to solved.
        Solved places on board update places next to it
    */
    private void removeTile(int row, int column, int layer) {
        if ((row >= 0 && row < getSize()) && (column >= 0 && column < getSize()) && (layer >= 0 && layer < getSize())) {
            getTile(row, column, layer).setTileState(TileState.SOLVED);
            accessibility[layer][row][column] = 0;
            updateAccessibleTiles(row, column, layer);
        }
    }


    /*
        Function finds coordinates of tile
    */
    private int[] getCoordinates(Tile tile) {
        int coordinates[] = {0, 0, 0};
        for (int layer = 0; layer < getSize(); layer++) {
            for (int row = 0; row < getSize(); row++) {
                for (int column = 0; column < getSize(); column++) {
                    if (tiles[layer][row][column] == tile) {
                        coordinates[2] = layer;
                        coordinates[0] = row;
                        coordinates[1] = column;
                    }
                }
            }
        }
        return coordinates;
    }

    /*
        Tiles are set in 3d array -> tiles[][][] and according to view are printed on the screen.
        Layers are always the same but rows and columns switch with different view.
        This leads programme to calculate which tile is being printed in 2d form and find its real coordinates
    */
    public boolean coordinatesToMark(int index, int layer) {
        int row = -1;
        int column = -1;
        if ((index >= 0 && index < getSize()) && (layer >= 0 && layer < getSize())) {
            switch (getView()) {
                case FRONT:
                    row = 0;
                    column = index;
                    while (row < getSize()) {
                        if (getTile(row, column, layer).getTileState() != TileState.SOLVED) {
                            break;
                        }
                        row++;
                    }
                    break;
                case BACK:
                    row = getSize() - 1;
                    column = abs(getSize() - 1 - index);
                    while (row >= 0) {
                        if (getTile(row, column, layer).getTileState() != TileState.SOLVED) {
                            break;
                        }
                        row--;
                    }
                    break;
                case LEFT:
                    row = abs(getSize() - 1 - index);
                    column = 0;
                    while (column < getSize()) {
                        if (getTile(row, column, layer).getTileState() != TileState.SOLVED) {
                            break;
                        }
                        column++;
                    }
                    break;
                case RIGHT:
                    row = index;
                    column = getSize() - 1;
                    while (column >= 0) {
                        if (getTile(row, column, layer).getTileState() != TileState.SOLVED) {
                            break;
                        }
                        column--;
                    }
                    break;
            }
        }
        if ((row >= 0 && row < getSize()) && (column >= 0 && column < getSize()) && (layer >= 0 && layer < getSize())) {
            return markTile(row, column, layer);

        } else {
            return false;
        }
    }


    /*
        Method control changing view on Board
        Game is in 3d form. In order to print it to 2d form variable view is crucial
    */
    public void changeView(int value) {
        if (getGameState() == GameState.PLAYING) {
            if (value > 0) {
                setView(rightView(getView()));
            } else if (value < 0) {
                setView(leftView(getView()));
            }
        }
    }

    /*
        Changes view 1 position to right depends on current view -> Front Right Back Left
    */
    private View rightView(View view) {
        int index = view.ordinal();
        int nextIndex = index + 1;
        View[] views = View.values();
        nextIndex = nextIndex % views.length;
        return views[nextIndex];
    }

    /*
        Changes view 1 position to left depends on current view -> Front Right Back Left
    */
    private View leftView(View view) {
        int index = view.ordinal();
        int nextIndex = index - 1;
        View[] views = View.values();
        nextIndex = nextIndex % views.length;
        if (nextIndex < 0) {
            nextIndex = views.length - 1;
        }
        return views[nextIndex];
    }

    /*
        Method updates game state according to time left or unsolvedpair count
    */
    public void updateGameState() {
        if (unsolvedPairCount == 0 && getStartingMillis() > 0 && getGameState() == GameState.PLAYING) {
            gameState = GameState.VICTORY;
            score = getScore() + getStartingMillis();
        } else if (unsolvedPairCount >= 0 && getStartingMillis() <= 0) {
            gameState = GameState.LOSE;
        }
    }


    public GameState getGameState() {
        return gameState;
    }

    public int getScore() {
        return score;
    }

    public int getStartingMillis() {
        if (size * size * size + size - (int) (System.currentTimeMillis() - startingMillis) / 1000 <= 0) {
            return 0;
        } else
            return size * size * size + size - (int) (System.currentTimeMillis() - startingMillis) / 1000;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getSize() {
        return size;
    }

    public int getUnsolvedPairCount() {
        return unsolvedPairCount;
    }

    public int getShuffleCount() {
        return remainingShuffles;
    }

    public List<Tile> getMarkedTile() {
        return markedTile;
    }

}
