package tdd.util;

import tdd.constants.BoardPositions;
import tdd.enums.PlayerSymbol;

import java.util.Arrays;
import java.util.List;

public class GameWinnerResolver {
    public static List<List<Integer>> winningConfigurations = List.of(
        //  vertically
        List.of(BoardPositions.POSITION_TOP_LEFT, BoardPositions.POSITION_LEFT, BoardPositions.POSITION_BOTTOM_LEFT),
        List.of(BoardPositions.POSITION_TOP, BoardPositions.POSITION_CENTER, BoardPositions.POSITION_BOTTOM),
        List.of(BoardPositions.POSITION_TOP_RIGHT, BoardPositions.POSITION_RIGHT, BoardPositions.POSITION_BOTTOM_RIGHT),
        //  horizontally
        List.of(BoardPositions.POSITION_TOP_LEFT, BoardPositions.POSITION_TOP, BoardPositions.POSITION_TOP_RIGHT),
        List.of(BoardPositions.POSITION_LEFT, BoardPositions.POSITION_CENTER, BoardPositions.POSITION_RIGHT),
        List.of(BoardPositions.POSITION_BOTTOM_LEFT, BoardPositions.POSITION_BOTTOM, BoardPositions.POSITION_BOTTOM_RIGHT),
        //  diagonally
        List.of(BoardPositions.POSITION_TOP_LEFT, BoardPositions.POSITION_CENTER, BoardPositions.POSITION_BOTTOM_RIGHT),
        List.of(BoardPositions.POSITION_TOP_RIGHT, BoardPositions.POSITION_CENTER, BoardPositions.POSITION_BOTTOM_LEFT)
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
        var opponentTurns = Arrays.stream(gameBoard).filter(field -> field == PlayerSymbol.O).count();
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
