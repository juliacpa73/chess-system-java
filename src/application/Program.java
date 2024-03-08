package application;

import java.util.Scanner;
import java.util.InputMismatchException;
import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();
        while (true) { // True para ficar repetindo várias vezes, uma vez que a estrutura while
                       // funciona enquanto algo for verdadeiro
            try { // Tratamento de erro
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces()); // Imprime o tabuleiro na tela
                System.out.println();
                System.out.print("Source: "); // Origem da peça
                ChessPosition source = UI.readChessPosition(sc);

                System.out.println();
                System.out.print("Target: "); // Destino
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
            } catch (ChessException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        }
    }
}
