package sk.tuke.gamestudio.game.taptiles.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.game.taptiles.core.Board;
import sk.tuke.gamestudio.game.taptiles.core.GameState;
import sk.tuke.gamestudio.game.taptiles.core.TileState;
import sk.tuke.gamestudio.game.taptiles.entity.Score;
import sk.tuke.gamestudio.game.taptiles.service.ScoreService;

import java.util.Date;


@Controller
@RequestMapping("")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class TaptilesController {
    private Board board = new Board(6);

    @Autowired
    private UserController userController;

    @Autowired
    private ScoreService scoreService;

    @RequestMapping("/taptiles")
    public String taptiles(@RequestParam(required = false) String row, @RequestParam(required = false) String column, @RequestParam(required = false) String layer) {
        processCommand(row, column, layer);
        return "taptiles";
    }

    private void processCommand(String row, String column, String layer) {
        try {
            board.markTile(Integer.parseInt(row), Integer.parseInt(column), Integer.parseInt(layer));
            board.updateGameState();
            if (board.getGameState() == GameState.VICTORY &&  userController.isLogged())
                scoreService.addScore(new Score(userController.getLoggedUser().getLogin(), "taptiles "+board.getSize(), board.getScore(), new Date()));
        } catch (Exception e) {
            System.out.println("a problem here");
        }
    }


    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table class='taptiles-game-container'>\n");
        int row, column, layer, zIndex, id = 0;
        float rowPosition, xPosition, yPosition;
        switch (board.getView()) {
            case FRONT:
                zIndex = 1000;
                rowPosition = 0;
                for (row = 0; row < board.getSize(); row++) {
                    for (layer = 0; layer < board.getSize(); layer++) {
                        xPosition = (47 + rowPosition * (-3));
                        yPosition = (float) (16 + rowPosition * (-1.7) + (layer + 1) * (7.8));
                        sb.append("<tr>\n");
                        for (column = board.getSize() - 1; column >= 0; column--) {
                            printTile(row, column, layer, xPosition, yPosition, zIndex, sb);
                            xPosition = (float) (xPosition + 3);
                            yPosition = (float) (yPosition - 1.5);
                            zIndex = zIndex - 1;
                        }
                        sb.append("</tr>\n");
                    }
                    rowPosition++;
                }
                break;
            case BACK:
                zIndex = 1000;
                rowPosition = 0;
                for (row = board.getSize() - 1; row >= 0; row--) {
                    for (layer = 0; layer < board.getSize(); layer++) {
                        xPosition = (47 + rowPosition * (-3));
                        yPosition = (float) (16 + rowPosition * (-1.7) + (layer + 1) * (7.8));
                        sb.append("<tr>\n");
                        for (column = 0; column < board.getSize(); column++) {
                            printTile(row, column, layer, xPosition, yPosition, zIndex, sb);
                            xPosition = (float) (xPosition + 3);
                            yPosition = (float) (yPosition - 1.5);
                            zIndex = zIndex - 1;
                        }
                        sb.append("</tr>\n");
                    }
                    rowPosition++;
                }
                break;
            case RIGHT:
                zIndex = 1000;
                rowPosition = 0;
                for (column = board.getSize() - 1; column >= 0; column--) {
                    for (layer = 0; layer < board.getSize(); layer++) {
                        xPosition = (47 + rowPosition * (-3));
                        yPosition = (float) (16 + rowPosition * (-1.7) + (layer + 1) * (7.8));
                        sb.append("<tr>\n");
                        for (row = board.getSize() - 1; row >= 0; row--) {
                            printTile(row, column, layer, xPosition, yPosition, zIndex, sb);
                            xPosition = (xPosition + 3);
                            yPosition = (float) (yPosition - 1.5);
                            zIndex = zIndex - 1;
                        }
                        sb.append("</tr>\n");
                    }
                    rowPosition++;
                }
                break;
            case LEFT:
                rowPosition = 0;
                zIndex = 1000;
                for (column = 0; column < board.getSize(); column++) {
                    for (layer = 0; layer < board.getSize(); layer++) {
                        xPosition = (47 + rowPosition * (-3));
                        yPosition = (float) (16 + rowPosition * (-1.7) + (layer + 1) * (7.8));
                        sb.append("<tr>\n");
                        for (row = 0; row < board.getSize(); row++) {
                            printTile(row, column, layer, xPosition, yPosition, zIndex, sb);
                            xPosition = (xPosition + 3);
                            yPosition = (float) (yPosition - 1.5);
                            zIndex = zIndex - 1;
                        }
                        sb.append("</tr>\n");
                    }
                    rowPosition++;
                }
                break;
        }
        sb.append("<table>\n");
        return sb.toString();
    }

    @RequestMapping("/taptiles/new")
    public String newGame(@RequestParam(required = false) String size) {
        board = new Board(Integer.parseInt(size));
        return "taptiles";
    }


    @RequestMapping("/taptiles/left")
    public String rotateLeft() {
        board.changeView(-1);
        return "taptiles";
    }

    @RequestMapping("/taptiles/right")
    public String rotateRight() {
        board.changeView(1);
        return "taptiles";
    }

    @RequestMapping("/taptiles/shuffle")
    public String makeShuffle() {
        board.makeShuffle();
        return "taptiles";
    }

    public String getScore() {
        StringBuilder sb = new StringBuilder();
        sb.append("<h1>\n");
        sb.append(board.getScore());
        sb.append("</h1>\n");
        sb.append("<img src='/images/taptiles/trophy.svg'>");
        return sb.toString();
    }

    public int finalScore() {
        return board.getScore();
    }

    public String getMarkedTile() {
        if (board.getMarkedTile().size() == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("<img src='/images/taptiles/tile-").append(board.getMarkedTile().get(0).getValue()).append(".svg'>\n");
            return sb.toString();
        } else return "";
    }

    public GameState getGameState() {
        return board.getGameState();
    }

    public void updateGameState() {
        board.updateGameState();
    }

    public String getTime() {
        return (String.valueOf(board.getStartingMillis()));
    }

    private void printTile(int row, int column, int layer, float xPosition, float yPosition, int zIndex, StringBuilder sb) {
        if ((board.getSize() % 2 == 1 && (row == board.getSize() / 2 && column == board.getSize() / 2))) {
            sb.append("<td>\n");
            sb.append("</td>\n");
        } else {
            if (board.getTile(row, column, layer).getTileState() == TileState.ACCESSIBLE) {
                sb.append("<td class='tile-accessible-container' style='left:").append(xPosition).append("vw; top:").append(yPosition).append("vh; z-index:").append(zIndex).append("'>\n");
                sb.append(String.format("<a href='/taptiles?row=%d&column=%d&layer=%d' class='tile-container'>\n", row, column, layer));
                sb.append("<img src='/images/taptiles/tile-").append(board.getTile(row, column, layer).getValue()).append(".png' style='height: 10.5vh' class='accessible'>");
                sb.append("</a>\n");
            }
            if (board.getTile(row, column, layer).getTileState() == TileState.INACCESSIBLE) {
                sb.append("<td class='tile-inaccessible-container' style='left:").append(xPosition).append("vw; top:").append(yPosition).append("vh; z-index:").append(zIndex).append("'>\n");
                sb.append(String.format("<a href='/taptiles?row=%d&column=%d&layer=%d' class='tile-container'>\n", row, column, layer));
                sb.append("<img src='/images/taptiles/tile-").append(board.getTile(row, column, layer).getValue()).append(".png' style='height: 10.5vh'>");
                sb.append("</a>\n");
            }
            if (board.getTile(row, column, layer).getTileState() == TileState.MARKED) {
                sb.append("<td class='tile-marked-container' style='left:").append(xPosition).append("vw; top:").append(yPosition).append("vh; z-index:").append(zIndex).append("'>\n");
                sb.append(String.format("<a href='/taptiles?row=%d&column=%d&layer=%d' class='tile-container'>\n", row, column, layer));
                sb.append("<img src='/images/taptiles/tile-").append(board.getTile(row, column, layer).getValue()).append(".png' style='height:  10.5vh'>");
                sb.append("</a>\n");
            }
            sb.append("</td>\n");
        }

    }
}
