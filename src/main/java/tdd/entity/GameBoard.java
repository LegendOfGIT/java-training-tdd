package tdd.entity;

public class GameBoard {
    private char activePlayerSymbol = 'X';
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
        this.activePlayerSymbol = 'X' == activePlayerSymbol ? 'O' : 'X';
    }

    public void checkFieldOnPosition(int fieldPosition) {
        fields[fieldPosition - 1] = activePlayerSymbol;
        toggleActivePlayer();
    }

    public int getActivePlayerSymbol() {
        return this.activePlayerSymbol;
    }
}
