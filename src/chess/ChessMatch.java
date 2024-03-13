package chess;

import java.util.ArrayList;
import java.util.List;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

// Classe coração do jogo responsável por controlar o fluxo do jogo e a impressão 
//do tabuleiro será responsável por exibir o tabuleiro no console.
public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private Board board; // Associação com a classe Board para ter um tabuleiro

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() { // Construtor responssável por saber qual é a dimensão do tabuleiro
        board = new Board(8, 8); // Tamanho do tabuleiro
        turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup(); // Vai chamar as peças no construtor
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    // Método responsável por retornar uma matriz correspondente a partida do
    // ChessMatch
    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; // Instanciando a matriz do tabuleiro
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    // Método responsável por performar o movimento do xadrez
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition(); // Colocando a varíavel na matriz
        Position target = targetPosition.toPosition(); // Colocando a varíavel na matriz
        validateSourcePosition(source); // Validação da posição de origem da peça
        validadeTargetPosition(source, target); // Validação da posição de destino da peça
        Piece capturedPiece = makeMove(source, target); // Ação de mover a peça
        nextTurn();
        return (ChessPiece) capturedPiece;
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position. ");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) { // Se não tiver nenhum movimento possível....
            throw new ChessException("There is no possible moves for the chosen piece. ");
        }
    }

    private void validadeTargetPosition(Position source, Position target) {
        // Se a peça de origem não tiver uma posição possível, não pode mover
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position. ");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Piece makeMove(Position source, Position target) {
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        return capturedPiece;
    }

    // Método responsável por receber as coordenadas do xadrez
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        // Com o toPosition, converte a operação para matriz
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece); // Adicionando a lista
    }

    // Método responsável por iniciar a partida de xadrez colocando as peças no
    // tabuleiro
    private void initialSetup() {
        placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
    }

}
