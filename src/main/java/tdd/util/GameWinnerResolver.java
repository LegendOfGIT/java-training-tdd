package tdd.util;

import tdd.enums.PlayerSymbol;

import java.util.Arrays;
import java.util.List;

public class GameWinnerResolver {
    private static final int BOARD_POSITION_TOP_LEFT = 0;
    private static final int BOARD_POSITION_TOP = 1;
    private static final int BOARD_POSITION_TOP_RIGHT = 2;
    private static final int BOARD_POSITION_LEFT = 3;
    private static final int BOARD_POSITION_CENTER = 4;
    private static final int BOARD_POSITION_RIGHT = 5;
    private static final int BOARD_POSITION_BOTTOM_LEFT = 6;
    private static final int BOARD_POSITION_BOTTOM = 7;
    private static final int BOARD_POSITION_BOTTOM_RIGHT = 8;

    public static List<List<Integer>> winningConfigurations = List.of(
        //  vertically
        List.of(BOARD_POSITION_TOP_LEFT, BOARD_POSITION_LEFT, BOARD_POSITION_BOTTOM_LEFT),
        List.of(BOARD_POSITION_TOP, BOARD_POSITION_CENTER, BOARD_POSITION_BOTTOM),
        List.of(BOARD_POSITION_TOP_RIGHT, BOARD_POSITION_RIGHT, BOARD_POSITION_BOTTOM_RIGHT),
        //  horizontally
        List.of(BOARD_POSITION_TOP_LEFT, BOARD_POSITION_TOP, BOARD_POSITION_TOP_RIGHT),
        List.of(BOARD_POSITION_LEFT, BOARD_POSITION_CENTER, BOARD_POSITION_RIGHT),
        List.of(BOARD_POSITION_BOTTOM_LEFT, BOARD_POSITION_BOTTOM, BOARD_POSITION_BOTTOM_RIGHT),
        //  diagonally
        List.of(BOARD_POSITION_TOP_LEFT, BOARD_POSITION_CENTER, BOARD_POSITION_BOTTOM_RIGHT),
        List.of(BOARD_POSITION_TOP_RIGHT, BOARD_POSITION_CENTER, BOARD_POSITION_BOTTOM_LEFT)
    );

    private static boolean isPlayersTurn(PlayerSymbol[] gameBoard, PlayerSymbol playerSymbol) {
        var playersTurns = Arrays.stream(gameBoard).filter(field -> field == playerSymbol).count();

        var opponentSymbol = PlayerSymbol.X == playerSymbol ? PlayerSymbol.O : PlayerSymbol.X;
        var opponentTurns = Arrays.stream(gameBoard).filter(field -> field == opponentSymbol).count();

        return (opponentTurns + 1) == playersTurns;
    }

    private static PlayerSymbol resolveWinner(PlayerSymbol[] gameBoard, List<Integer> winningConfiguration) {
        var matchingSymbolsForPlayerX = 0;
        var matchingSymbolsForPlayerY = 0;
        for (Integer configurationField: winningConfiguration) {
            matchingSymbolsForPlayerX += PlayerSymbol.X == gameBoard[configurationField] ? 1 : 0;
            if (3 == matchingSymbolsForPlayerX && isPlayersTurn(gameBoard, PlayerSymbol.X)) {
                return PlayerSymbol.X;
            }

            matchingSymbolsForPlayerY += PlayerSymbol.O == gameBoard[configurationField] ? 1 : 0;
            if (3 == matchingSymbolsForPlayerY && isPlayersTurn(gameBoard, PlayerSymbol.O)) {
                return PlayerSymbol.O;
            }
        }

        return PlayerSymbol.BLANK;
    }

    private static boolean hasOnePlayerTakenTooManyTurns(PlayerSymbol[] gameBoard) {
        var playersTurns = Arrays.stream(gameBoard).filter(field -> field == PlayerSymbol.X).count();

        var opponentSymbol = PlayerSymbol.O;
        var opponentTurns = Arrays.stream(gameBoard).filter(field -> field == opponentSymbol).count();
        var playerTurnDifference = playersTurns - opponentTurns;

        return playerTurnDifference > 1 || playerTurnDifference < -1;
    }

    public PlayerSymbol resolveWinner(PlayerSymbol[] gameBoard) throws RuntimeException {
        if (hasOnePlayerTakenTooManyTurns(gameBoard)) {
            throw new RuntimeException("One player has taken too many turns");
        }

        for (List<Integer> winningConfiguration: winningConfigurations) {
            PlayerSymbol winnerSymbol = resolveWinner(gameBoard, winningConfiguration);
            if (PlayerSymbol.BLANK != winnerSymbol) {
                return winnerSymbol;
            }
        }

        return PlayerSymbol.BLANK;
    }
}
