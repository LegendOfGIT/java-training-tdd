package tdd.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tdd.enums.PlayerSymbol;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GameWinnerResolverTest {
    private GameWinnerResolver gameWinnerResolver;

    @BeforeEach
    public void setup() {
        this.gameWinnerResolver = new GameWinnerResolver();
    }

    private static void addWinningConstellationTests(Stream.Builder<Arguments> streamBuilder) {
        for (PlayerSymbol playerSymbol: new PlayerSymbol[]{ PlayerSymbol.X, PlayerSymbol.O}) {
            var winningConfigurationIndex = 1;
            for (List<Integer> winningConfiguration : GameWinnerResolver.winningConfigurations) {
                var currentBoard = new PlayerSymbol[]{
                    PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
                    PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK,
                    PlayerSymbol.BLANK, PlayerSymbol.BLANK, PlayerSymbol.BLANK
                };

                for(Integer configurationField: winningConfiguration) {
                    currentBoard[configurationField] = playerSymbol;
                }

                var opponentSymbol = PlayerSymbol.X == playerSymbol ? PlayerSymbol.O : PlayerSymbol.X;
                for (int i=0;i<9;i++) {
                    currentBoard[i] = PlayerSymbol.BLANK == currentBoard[i] ? opponentSymbol : currentBoard[i];
                    if (Arrays.stream(currentBoard).filter(field -> opponentSymbol == field).count() == 2) {
                        break;
                    }
                }

                streamBuilder.add(Arguments.of(
                    String.format("%s wins the game. constellation #%s", playerSymbol, winningConfigurationIndex),
                    currentBoard,
                    playerSymbol
                ));

                winningConfigurationIndex++;
            }
        }
    }

    private static Stream<Arguments> testResolverReturnsExpectedGameWinnerSymbol() {
        Stream.Builder<Arguments> argumentsStream = Stream.builder();

        addWinningConstellationTests(argumentsStream);

        argumentsStream.add(Arguments.of(
            "no one wins",
            new PlayerSymbol[]{
                PlayerSymbol.O, PlayerSymbol.O , PlayerSymbol.BLANK,
                PlayerSymbol.O, PlayerSymbol.X, PlayerSymbol.BLANK,
                PlayerSymbol.BLANK, PlayerSymbol.X, PlayerSymbol.BLANK
            },
            PlayerSymbol.BLANK
        ));

        return argumentsStream.build();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource
    public void testResolverReturnsExpectedGameWinnerSymbol(String testName,
                                                            PlayerSymbol[] gameBoard,
                                                            PlayerSymbol expectedWinnerSymbol) {
        Assertions.assertEquals(expectedWinnerSymbol, gameWinnerResolver.resolveWinner(gameBoard));
    }

    @Test
    public void testResolverThrowsAnExceptionWhenOnePlayerTookASecondTurnInARow() {
        var exception = Assertions.assertThrows(RuntimeException.class, () -> {
            gameWinnerResolver.resolveWinner(new PlayerSymbol[]{
                PlayerSymbol.BLANK, PlayerSymbol.O, PlayerSymbol.X,
                PlayerSymbol.X, PlayerSymbol.X, PlayerSymbol.BLANK,
                PlayerSymbol.BLANK, PlayerSymbol.BLANK,PlayerSymbol.BLANK
            });
        });

        Assertions.assertEquals("One player has taken too many turns", exception.getMessage());
    }
}
