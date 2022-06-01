package sk.tuke.gamestudio.game.taptiles.core;

import java.io.Serializable;

public class Tile implements Serializable {
    private TileState tileState;
    private final int value;

    public Tile(int value, TileState state){
        this.value = value;
        this.tileState = state;
    }

    public void setTileState(TileState tileState) {
        this.tileState = tileState;
    }

    public int getValue() {
        return value;
    }


    public TileState getTileState() {
        return tileState;
    }


    @Override
    public String toString() {
        return "Tile{" +
                "tileState=" + tileState +
                ", value=" + value +
                '}';
    }
}
