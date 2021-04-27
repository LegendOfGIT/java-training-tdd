package tdd.entities;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tdd.constants.BoardPositions;
import tdd.enums.PlayerSymbol;
import tdd.util.GameWinnerResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameBoardTest {
    public static final int FIELD_POSITION_TOP_RIGHT = 3;
    public static final int FIELD_POSITION_TOP_LEFT = 1;
    public static final int FIELD_POSITION_TOP_CENTER = 2;
    private GameBoard gameBoard;

    @Mock
    private GameWinnerResolver gameWinnerResolver;

    @BeforeEach
    public void setup() {
        this.gameBoard = new GameBoard(gameWinnerResolver);
    }

    @Test
    public void testGameBoardHasAllExpectedFields() {
        Assertions.assertArrayEquals(
            GameBoard.EMPTY_BOARD,
            gameBoard.getFields()
        );
    }

    @Test
    public void testGameBoardIsInitializedWithExpectedStartPlayer() {
        Assertions.assertEquals(PlayerSymbol.X, gameBoard.getActivePlayerSymbol());
    }

    private static Stream<Arguments> testGameBoardHasExpectedFieldsSetAfterPlayerCheckedAField() {
        return Stream.of(
                Arguments.of(
                    new PlayerSymbol[]{
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK
                    },
                    3,
                    new PlayerSymbol[]{
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.X,
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK
                    }
                ),
                Arguments.of(
                    new PlayerSymbol[]{
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
                        PlayerSymbol.BLANK, PlayerSymbol.X, PlayerSymbol.BLANK,
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.O
                    },
                    2,
                    new PlayerSymbol[]{
                        PlayerSymbol.BLANK, PlayerSymbol.X, PlayerSymbol.BLANK,
                        PlayerSymbol.BLANK, PlayerSymbol.X, PlayerSymbol.BLANK,
                        PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.O
                    }
                )
        );
    }

    @ParameterizedTest
    @MethodSource
    public void testGameBoardHasExpectedFieldsSetAfterPlayerCheckedAField(PlayerSymbol[] givenGameBoardFields,
                                                                          int fieldPositionToCheck,
                                                                          PlayerSymbol[] expectedGameBoardFields
    ) {
        when(gameWinnerResolver.resolveWinner(any())).thenReturn(PlayerSymbol.BLANK);

        gameBoard.setGameBoardFields(givenGameBoardFields);
        gameBoard.checkFieldOnPosition(fieldPositionToCheck);

        Assertions.assertArrayEquals(expectedGameBoardFields, gameBoard.getFields());
        Assertions.assertEquals(PlayerSymbol.O, gameBoard.getActivePlayerSymbol());
    }

    @Test
    public void testGameBoardCallsGameWinnerResolverWithExpectedArgumentsWhenFieldIsChecked()
    {
        when(gameWinnerResolver.resolveWinner(any())).thenReturn(PlayerSymbol.BLANK);

        var currentBoard = new PlayerSymbol[]{
            PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
            PlayerSymbol.BLANK, PlayerSymbol.X, PlayerSymbol.BLANK,
            PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.O
        };
        gameBoard.setGameBoardFields(currentBoard);
        gameBoard.checkFieldOnPosition(FIELD_POSITION_TOP_RIGHT);

        var gameBoardFromArgument = ArgumentCaptor.forClass(PlayerSymbol[].class);
        Mockito.verify(gameWinnerResolver).resolveWinner(gameBoardFromArgument.capture());

        currentBoard[FIELD_POSITION_TOP_RIGHT] = PlayerSymbol.X;
        Assertions.assertArrayEquals(
            currentBoard,
            gameBoardFromArgument.getValue()
        );
    }

    private static Stream<Arguments> testCheckFieldThrowsAnExceptionWhenPickedFieldIsInvalid() {
        return Stream.of(
            Arguments.of(
                "negative out of range",
                0,
                "game board field is invalid"
            ),
            Arguments.of(
                "positive out of range",
                10,
                "game board field is invalid"
            ),
            Arguments.of(
                "game board field is already picked",
                FIELD_POSITION_TOP_CENTER,
                "game board field has already been picked"
            )
        );
    }

    @ParameterizedTest(name="{0}")
    @MethodSource
    public void testCheckFieldThrowsAnExceptionWhenPickedFieldIsInvalid(String testName,
                                                                   Integer pickedPosition,
                                                                   String expectedExceptionMessage)
    {
        when(gameWinnerResolver.resolveWinner(any())).thenReturn(PlayerSymbol.BLANK);

        var exception = Assertions.assertThrows(RuntimeException.class, () -> {
            gameBoard.checkFieldOnPosition(FIELD_POSITION_TOP_LEFT);
            gameBoard.checkFieldOnPosition(FIELD_POSITION_TOP_CENTER);
            gameBoard.checkFieldOnPosition(pickedPosition);
        });

        Assertions.assertEquals(expectedExceptionMessage, exception.getMessage());
    }

    private static Stream<Arguments> testCheckFieldResetsBoardAndThrowsWinnerExceptionWhenThereWasAGameWinner() {
        return Stream.of(
            Arguments.of(
                "Player X won the game",
                PlayerSymbol.X,
                "Player X has won the game. Congratulations! Game over... Board is reset."
            ),
            Arguments.of(
                "Player O won the game",
                PlayerSymbol.O,
                "Player O has won the game. Congratulations! Game over... Board is reset."
            )
        );
    }

    @ParameterizedTest(name="{0}")
    @MethodSource
    public void testCheckFieldResetsBoardAndThrowsWinnerExceptionWhenThereWasAGameWinner(String testName,
                                                                           PlayerSymbol winnerSymbol,
                                                                           String expectedExceptionMessage) {
        when(gameWinnerResolver.resolveWinner(any())).thenReturn(winnerSymbol);
        var givenBoard = GameBoard.EMPTY_BOARD.clone();
        givenBoard[BoardPositions.POSITION_BOTTOM] = PlayerSymbol.O;
        gameBoard.setGameBoardFields(givenBoard);

        var exception = Assertions.assertThrows(RuntimeException.class, () -> {
            gameBoard.checkFieldOnPosition(BoardPositions.POSITION_CENTER);
        });

        Assertions.assertEquals(expectedExceptionMessage, exception.getMessage());
        Assertions.assertArrayEquals(GameBoard.EMPTY_BOARD, gameBoard.getFields());
    }
}
