package chess;

import boardGame.Board;

public class ChessMatch { // Classe coração do jogo responsável por controlar o fluxo do jogo e a impressão do tabuleiro será responsável por exibir o tabuleiro no console.

    private Board board; // Associação com a classe Board para ter um tabuleiro

    public ChessMatch() { // Construtor responssável por saber qual é a dimensão do tabuleiro
        board = new Board(8, 8); // Tamanho do tabuleiro
    }

    public ChessPiece[][] getPieces() { // Método responsável por retornar uma matriz correspondente a partida do ChessMatch
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; // instanciando a matriz do tabuleiro
        for (int i = 0; i < board.getRows(); i++){
            for(int j = 0; j < board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.piece(i,j);
            }
        }
        return mat;
    }
}
