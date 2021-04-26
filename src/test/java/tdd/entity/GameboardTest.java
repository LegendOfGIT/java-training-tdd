package tdd.entity;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GameboardTest {
    private Gameboard gameboard;

    @BeforeEach
    public void setup() {
        this.gameboard = new Gameboard();
    }

    @Test
    public void testBoardCanBeInstantiated() {
        assertTrue(gameboard instanceof Gameboard);
    }

    @Test
    public void testGameboardHasAllExpectedFields() {
        assertArrayEquals(
                new char[]{
                    ' ', ' ', ' ',
                    ' ', ' ', ' ',
                    ' ', ' ', ' '
                },
                gameboard.getFields()
        );
    }

    @Test
    public void testGameboardIsInitializedWithExpectedStartPlayer() {
        assertEquals(1, gameboard.getActivePlayerId());
    }

    private static Stream<Arguments> testGameboardHasExpectedFieldsSetAfterPlayerCheckedAField() {
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
    public void testGameboardHasExpectedFieldsSetAfterPlayerCheckedAField(
            char[] givenGameboardFields,
            int fieldPositionToCheck,
            char[] expectedGameboardFields
    ) {
        gameboard.setGameboardFields(givenGameboardFields);
        gameboard.checkFieldOnPosition(fieldPositionToCheck);
        assertArrayEquals(expectedGameboardFields, gameboard.getFields());
        assertEquals(2, gameboard.getActivePlayerId());
    }
}
