package sk.tuke.gamestudio.game.taptiles.core;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @org.junit.jupiter.api.Test
    void makeShuffle() {
        Board board = new Board(5);
        int shuffleCount = board.getShuffleCount();

        board.makeShuffle();
        assertEquals(board.getShuffleCount()+1, shuffleCount);

        board.makeShuffle();
        assertFalse(board.makeShuffle());
        assertEquals(0,board.getShuffleCount());
    }

    @org.junit.jupiter.api.Test
    void getTile() {
        Board board = new Board(5);
        assertNotNull(board.getTile(0 , 0, 0));
        assertNotNull(board.getTile(0 , 0, 4));
        assertNotNull(board.getTile(0 , 4, 0));
        assertNotNull(board.getTile(4 , 0, 0));
        assertNotNull(board.getTile(2, 3, 4));

        assertNull(board.getTile(0 , 0, -1));
        assertNull(board.getTile(0 , -1, 0));
        assertNull(board.getTile(-1 , 0, 0));
        assertNull(board.getTile(-1 , -1, -1));
        assertNull(board.getTile(5, 0, 0));
        assertNull(board.getTile(0, 5, 0));
        assertNull(board.getTile(0, 0, 5));
        assertNull(board.getTile(5, 5, 5));
        assertNull(board.getTile(-10, 10, 24));
    }

    @org.junit.jupiter.api.Test
    void coordinatesToMark() {
        Board board = new Board(3);
        board.setView(View.FRONT);
        assertTrue(board.coordinatesToMark(0,0));
        assertFalse(board.getMarkedTile().isEmpty());
        assertTrue(board.coordinatesToMark(0,1));
        assertTrue(board.getMarkedTile().isEmpty());

        assertTrue(board.coordinatesToMark(0,2));
        assertFalse(board.getMarkedTile().isEmpty());
        assertTrue(board.coordinatesToMark(2,0));
        assertTrue(board.getMarkedTile().isEmpty());

        assertTrue(board.coordinatesToMark(2,1));
        assertFalse(board.getMarkedTile().isEmpty());
        assertTrue(board.coordinatesToMark(2,2));
        assertTrue(board.getMarkedTile().isEmpty());

        assertFalse(board.coordinatesToMark(0,3));
        assertTrue(board.getMarkedTile().isEmpty());
        assertFalse(board.coordinatesToMark(0,-1));
        assertTrue(board.getMarkedTile().isEmpty());
        assertFalse(board.coordinatesToMark(1,3));
        assertTrue(board.getMarkedTile().isEmpty());
        assertFalse(board.coordinatesToMark(-1,0));
        assertTrue(board.getMarkedTile().isEmpty());
    }

    @org.junit.jupiter.api.Test
    void changeView() {
        Board board = new Board(3);
        board.setView(View.FRONT);
        assertEquals(View.FRONT, board.getView());


        board.changeView(1);
        assertEquals(View.RIGHT, board.getView());
        board.changeView(4);
        assertEquals(View.BACK, board.getView());
        board.changeView(5);
        assertEquals(View.LEFT, board.getView());

        board.setView(View.LEFT);
        assertEquals(View.LEFT, board.getView());
        board.changeView(-1);
        assertEquals(View.BACK, board.getView());
        board.changeView(5);
        assertEquals(View.LEFT, board.getView());
    }

}