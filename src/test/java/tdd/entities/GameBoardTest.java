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
import tdd.enums.PlayerSymbol;
import tdd.util.GameWinnerResolver;

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
            new PlayerSymbol[]{
                PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
                PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
                PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK
            },
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
        gameBoard.setGameBoardFields(givenGameBoardFields);
        gameBoard.checkFieldOnPosition(fieldPositionToCheck);

        Assertions.assertArrayEquals(expectedGameBoardFields, gameBoard.getFields());
        Assertions.assertEquals(PlayerSymbol.O, gameBoard.getActivePlayerSymbol());
    }

    @Test
    public void testGameBoardCallsGameWinnerResolverWithExpectedArgumentsWhenFieldIsChecked()
    {
        var currentBoard = new PlayerSymbol[]{
            PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
            PlayerSymbol.BLANK, PlayerSymbol.X, PlayerSymbol.BLANK,
            PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.O
        };
        gameBoard.setGameBoardFields(currentBoard);
        gameBoard.checkFieldOnPosition(FIELD_POSITION_TOP_RIGHT);

        var gameBoardFromArgument = ArgumentCaptor.forClass(PlayerSymbol[].class);
        Mockito.verify(gameWinnerResolver).resolveWinner(gameBoardFromArgument.capture());

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
                9,
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
        var exception = Assertions.assertThrows(RuntimeException.class, () -> {
            gameBoard.checkFieldOnPosition(FIELD_POSITION_TOP_LEFT);
            gameBoard.checkFieldOnPosition(FIELD_POSITION_TOP_CENTER);
            gameBoard.checkFieldOnPosition(pickedPosition);
        });

        Assertions.assertEquals(expectedExceptionMessage, exception.getMessage());
    }
}
