package com.example.tictactoe;

import static com.example.tictactoe.Board.board;

public class MovePicker {

    static class ScoreXY {
        long score;
        BoardXY xy;

        ScoreXY() {
            this.score = -Long.MAX_VALUE;
            this.xy = BoardXY.of(2, 2);
        }
    }

    public static BoardXY bestMove(int availableCount) {
        return countBestScore(-Long.MAX_VALUE, Player.AI.getValue(), availableCount, false).xy;
    }

    private static ScoreXY countBestScore(long initialScore, int playerValue, int availableCount, boolean isMaximazing) {
        long bestScore = initialScore;
        ScoreXY scoreXY = new ScoreXY();


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].getValue() == Player.NONE.getValue()) {
                    board[i][j].setValue(playerValue);
                    availableCount--;

                    long score = miniMax(availableCount, isMaximazing);

                    board[i][j].setValue(Player.NONE.getValue());
                    availableCount++;

                    bestScore = isMaximazing ? Math.min(score, bestScore) : Math.max(score, bestScore);
                    if (scoreXY.score != bestScore) {
                        scoreXY.score = bestScore;
                        scoreXY.xy = BoardXY.of(i, j);
                    }
                }
            }
        }
        return scoreXY;
    }

    private static long miniMax(int availableCount, boolean isMaximazing) {
        Integer winValue = GameStatus.checkWinCondition(availableCount);
        if (winValue != null) {
            return winValue;
        }

        long initialScore = isMaximazing ? -Long.MAX_VALUE : Long.MAX_VALUE;
        int playerValue = isMaximazing ? Player.AI.getValue() : Player.HUMAN.getValue();

        return countBestScore(initialScore, playerValue, availableCount, !isMaximazing).score;
    }
}
