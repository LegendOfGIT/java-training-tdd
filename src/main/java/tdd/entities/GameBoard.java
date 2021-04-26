package tdd.entities;

import tdd.enums.PlayerSymbol;
import tdd.util.GameWinnerResolver;

public class GameBoard {
    private PlayerSymbol activePlayerSymbol = PlayerSymbol.X;
    private PlayerSymbol[] fields = new PlayerSymbol[] {};

    private final GameWinnerResolver gameWinnerResolver;

    public GameBoard(GameWinnerResolver gameWinnerResolver) {
        setGameBoardFields(new PlayerSymbol[]{
            PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
            PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
            PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
        });

        this.gameWinnerResolver = gameWinnerResolver;
    }

    public PlayerSymbol[] getFields() {
        return this.fields;
    }

    /**
     * @param fields new game board content
     */
    public void setGameBoardFields(PlayerSymbol[] fields) {
        this.fields = fields;
    }

    private void toggleActivePlayer() {
        this.activePlayerSymbol = PlayerSymbol.X == activePlayerSymbol ? PlayerSymbol.O : PlayerSymbol.X;
    }

    /**
     * @param fieldPosition the position of the field which should be checked
     */
    public void checkFieldOnPosition(int fieldPosition) {
        var fieldPositionInArray = fieldPosition -1;
        if (fieldPositionInArray < 0 || fieldPositionInArray > 7) {
            throw new RuntimeException("game board field is invalid");
        }

        if (PlayerSymbol.BLANK != fields[fieldPositionInArray]) {
            throw new RuntimeException("game board field has already been picked");
        }

        gameWinnerResolver.resolveWinner(this.fields);

        fields[fieldPositionInArray] = activePlayerSymbol;
        toggleActivePlayer();
    }

    public PlayerSymbol getActivePlayerSymbol() {
        return this.activePlayerSymbol;
    }
}
