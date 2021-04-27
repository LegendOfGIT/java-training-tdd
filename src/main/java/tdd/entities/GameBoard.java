package tdd.entities;

import tdd.constants.BoardPositions;
import tdd.enums.PlayerSymbol;
import tdd.util.GameWinnerResolver;

public class GameBoard {
    private PlayerSymbol activePlayerSymbol = PlayerSymbol.X;
    private PlayerSymbol[] fields = new PlayerSymbol[] {};

    private final GameWinnerResolver gameWinnerResolver;
    public static final PlayerSymbol[] EMPTY_BOARD = new PlayerSymbol[]{
        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
    };

    public GameBoard(GameWinnerResolver gameWinnerResolver) {
        setGameBoardFields(EMPTY_BOARD.clone());

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
     * @param fieldPositionInArray the actual field position in field array
     * @return true when picked field position is not on the board
     */
    private boolean isFieldPositionOutOfBounds(int fieldPositionInArray) {
        return fieldPositionInArray < BoardPositions.POSITION_TOP_LEFT ||
            fieldPositionInArray > BoardPositions.POSITION_BOTTOM_RIGHT;
    }

    /**
     * @param fieldPosition the position of the field which should be checked
     */
    public void checkFieldOnPosition(int fieldPosition) {
        var fieldPositionInArray = fieldPosition -1;
        if (isFieldPositionOutOfBounds(fieldPositionInArray)) {
            throw new RuntimeException("game board field is invalid");
        }

        if (PlayerSymbol.BLANK != fields[fieldPositionInArray]) {
            throw new RuntimeException("game board field has already been picked");
        }

        fields[fieldPositionInArray] = activePlayerSymbol;
        toggleActivePlayer();

        var winner = gameWinnerResolver.resolveWinner(this.fields);
        if (PlayerSymbol.BLANK != winner) {
            fields = EMPTY_BOARD.clone();
            throw new RuntimeException(String.format("Player %s has won the game. Congratulations! Game over... Board is reset.", winner));
        }
    }

    public PlayerSymbol getActivePlayerSymbol() {
        return this.activePlayerSymbol;
    }
}
