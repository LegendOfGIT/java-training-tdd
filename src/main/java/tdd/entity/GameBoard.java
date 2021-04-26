package tdd.entity;

public class GameBoard {
    private int activePlayerId = 1;
    private char[] fields = new char[] {};

    public GameBoard() {
        setGameBoardFields(new char[]{
            ' ', ' ', ' ',
            ' ', ' ', ' ',
            ' ', ' ', ' '
        });
    }

    public char[] getFields() {
        return this.fields;
    }

    public void setGameBoardFields(char[] fields) {
        this.fields = fields;
    }

    private void toggleActivePlayer() {
        this.activePlayerId = 1 == activePlayerId ? 2 : 1;
    }

    public void checkFieldOnPosition(int fieldPosition) {
        fields[fieldPosition - 1] = 1 == activePlayerId ? 'X' : 'O';
        toggleActivePlayer();
    }

    public int getActivePlayerId() {
        return this.activePlayerId;
    }
}
