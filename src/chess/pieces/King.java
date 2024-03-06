package chess.pieces;

import boardGame.Board;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece { // Aqui a subclasse King está herdando características da subclasse-mãe
                                       // ChessPiece

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K"; // Esse "K" é a peça que aparecerá na hora de imprimir o tabuleiro
    }

}
