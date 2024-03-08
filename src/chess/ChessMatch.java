package chess;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch { // Classe coração do jogo responsável por controlar o fluxo do jogo e a
                          // impressão do tabuleiro será responsável por exibir o tabuleiro no console.

    private Board board; // Associação com a classe Board para ter um tabuleiro

    public ChessMatch() { // Construtor responssável por saber qual é a dimensão do tabuleiro
        board = new Board(8, 8); // Tamanho do tabuleiro
        initialSetup(); // Vai chamar as peças no construtor
    }

    public ChessPiece[][] getPieces() { // Método responsável por retornar uma matriz correspondente a partida do
                                        // ChessMatch
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; // instanciando a matriz do tabuleiro
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){ // Método responsável por performar o movimento do xadrez
        Position source = sourcePosition.toPosition(); // Colocando a varíavel na matriz
        Position target = targetPosition.toPosition(); // Colocando a varíavel na matriz
        validateSourcePosition(source); // Validação da posição de origem da peça
        validadeTargetPosition(source, target); // Validação da posição de destino da peça
        Piece capturedPiece = makeMove(source, target); // Ação de mover a peça 
        return (ChessPiece)capturedPiece;
    }

    private void validateSourcePosition(Position position){
        if (!board.thereIsAPiece(position)){
        throw new ChessException("There is no piece on source position. ");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){ // Se não tiver nenhum movimento possível....
            throw new ChessException("There is no possible moves for the chosen piece. ");
        }
    }

    private void validadeTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){ // Se a peça de origem não tiver uma posição possível, não pode mover
            throw new ChessException("The chosen piece can't move to target position. ");
        }
    }

    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        return capturedPiece;
    }

    private void placeNewPiece(char column, int row, ChessPiece piece){ // Método responsável por receber as coordenadas do xadrez
        board.placePiece(piece, new ChessPosition(column, row).toPosition()); // Com o toPosition, converte a operação para matriz
    }

    private void initialSetup() { // Método responsável por iniciar a partida de xadrez colocando as peças no
                                  // tabuleiro
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
