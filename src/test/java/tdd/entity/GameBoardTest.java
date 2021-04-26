package tdd.entity;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GameBoardTest {
    private GameBoard gameBoard;

    @BeforeEach
    public void setup() {
        this.gameBoard = new GameBoard();
    }

    @Test
    public void testBoardCanBeInstantiated() {
        Assertions.assertTrue(gameBoard instanceof GameBoard);
    }

    @Test
    public void testGameBoardHasAllExpectedFields() {
        Assertions.assertArrayEquals(
            new char[]{
                ' ', ' ', ' ',
                ' ', ' ', ' ',
                ' ', ' ', ' '
            },
            gameBoard.getFields()
        );
    }

    @Test
    public void testGameBoardIsInitializedWithExpectedStartPlayer() {
        Assertions.assertEquals('X', gameBoard.getActivePlayerSymbol());
    }

    private static Stream<Arguments> testGameBoardHasExpectedFieldsSetAfterPlayerCheckedAField() {
        return Stream.of(
                Arguments.of(
                    new char[]{
                        ' ', ' ', ' ',
                        ' ', ' ', ' ',
                        ' ', ' ', ' '
                    },
                    3,
                    new char[]{
                        ' ', ' ', 'X',
                        ' ', ' ', ' ',
                        ' ', ' ', ' '
                    }
                ),
                Arguments.of(
                    new char[]{
                        ' ', ' ', ' ',
                        ' ', 'X', ' ',
                        ' ', ' ', 'O'
                    },
                    2,
                    new char[]{
                        ' ', 'X', ' ',
                        ' ', 'X', ' ',
                        ' ', ' ', 'O'
                    }
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testGameBoardHasExpectedFieldsSetAfterPlayerCheckedAField(
        char[] givenGameBoardFields,
        int fieldPositionToCheck,
        char[] expectedGameBoardFields
    ) {
        gameBoard.setGameBoardFields(givenGameBoardFields);
        gameBoard.checkFieldOnPosition(fieldPositionToCheck);

        Assertions.assertArrayEquals(expectedGameBoardFields, gameBoard.getFields());
        Assertions.assertEquals('O', gameBoard.getActivePlayerSymbol());
    }
}
