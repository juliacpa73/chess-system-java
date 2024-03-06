package application;

import chess.ChessPiece;

public class UI {
    public static void printBoard(ChessPiece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " "); // Não pode ser println, pois aconteceria uma quebra de linha que deixaria o tabuleiro desalinhado
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j]);
            }
            System.out.println(); // Quebra de Linha
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece) { // Método auxiliar para imprimir uma peça
        if (piece == null) {
            System.out.print("-");
        } else {
            System.out.println(piece);
        }
        System.out.print(" "); // Não pode ser println, pois aconteceria uma quebra de linha que deixaria o tabuleiro desalinhado
    }
}
