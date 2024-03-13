package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {

    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    // Cores do texto

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Cores do fundo

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static void clearScreen() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    // Esse Scanner abaixo vai ser instanciado na classe principal e usado aqui no
    // método com argumento
    public static ChessPosition readChessPosition(Scanner sc) { // Método responsável para ler uma posição do usuário
        try {
            String s = sc.nextLine();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1)); // Aqui a string está sendo recortada e convertida para inteira
            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8.");
        }
    }

    public static void printMatch(ChessMatch chessMatch, List<ChessPiece> captured) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(captured);
        System.out.println();
        System.out.println("Turn: " + chessMatch.getTurn());
        if (!chessMatch.getCheck()) {
            System.out.println("Waiting player: " + chessMatch.getCurrentPlayer());
            if (chessMatch.getCheck()) {
                System.out.println("CHECK!");
            } else {
                System.out.println("CHECKMATE!");
                System.out.println("Wineer: " + chessMatch.getCurrentPlayer());
            }
        }
    }

    public static void printBoard(ChessPiece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " "); // Não pode ser println, pois aconteceria uma quebra de linha que deixaria
                                             // o tabuleiro desalinhado
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], false);
            }
            System.out.println(); // Quebra de Linha
        }
        System.out.println("  a b c d e f g h");
    }

    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pieces.length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    private static void printPiece(ChessPiece piece, boolean background) { // Método auxiliar para imprimir uma peça
        if (background) {
            System.out.print(ANSI_PURPLE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET); // Não pode ser println, pois aconteceria uma quebra de linha que
                                                // deixaria o
            // tabuleiro desalinhado
        } else {
            if (piece.getColor() == Color.WHITE) { // If para testar se a cor da peça vai ser branca ou preta
                System.out.print(ANSI_WHITE + piece + ANSI_RESET);
            } else { // Para não ficar adicionando colunas a mais na hora de imprimir
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET); // Não pode ser println, pois aconteceria uma quebra
                                                                    // de linha que deixaria o
                // tabuleiro desalinhado
            }
        }
        System.out.print(" "); // Não pode ser println, pois aconteceria uma quebra de linha que deixaria o
                               // tabuleiro desalinhado
    }

    private static void printCapturedPieces(List<ChessPiece> captured) { // Método responsável por imprimir a lista de
                                                                         // peças capturadas
        List<ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE)
                .collect(Collectors.toList());
        List<ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK)
                .collect(Collectors.toList());
        System.out.println("Captured pieces: ");
        System.out.print("White: " + ANSI_WHITE);
        System.out.print(Arrays.toString(white.toArray()));
        System.out.println(ANSI_RESET);
        System.out.print("Black: " + ANSI_YELLOW);
        System.out.print(Arrays.toString(black.toArray()));
        System.out.println(ANSI_RESET);
    }
}
