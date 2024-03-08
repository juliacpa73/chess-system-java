package chess.pieces;

import boardGame.Board;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece { // Aqui a subclasse Rook está herdando características da subclasse-mãe
                                       // ChessPiece

    public Rook(Board board, Color color) { // Construtor que está informando quem é o tabuleiro e a cor da peça
        super(board, color);
    }

    @Override
    public String toString() {
        return "R"; // Esse "R" é a peça que aparecerá na hora de imprimir o tabuleiro
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        return mat;
    }
}
