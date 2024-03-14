package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;
import java.security.InvalidParameterException;

// Classe principal do jogo de xadrez, responsável por controlar o fluxo do jogo e a impressão do tabuleiro.
public class ChessMatch {
    private int turn; // Variável para controlar o turno atual.
    private Color currentPlayer; // Variável para controlar o jogador atual.
    private Board board; // Associação com a classe Board para ter um tabuleiro.
    private boolean check; // Variável para verificar se o rei está em cheque.
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    private List<Piece> piecesOnTheBoard = new ArrayList<>(); // Lista de peças no tabuleiro.
    private List<Piece> capturedPieces = new ArrayList<>(); // Lista de peças capturadas.

    // Construtor da partida de xadrez, define o tamanho do tabuleiro e inicializa a
    // configuração do jogo.
    public ChessMatch() {
        board = new Board(8, 8); // Define o tamanho do tabuleiro.
        turn = 1; // Define o turno inicial.
        currentPlayer = Color.WHITE; // Define o jogador inicial.
        initialSetup(); // Chama o método para iniciar a configuração do jogo.
    }

    // Métodos getters para retornar o turno atual, o jogador atual e se o rei está
    // em cheque e chequemate.
    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    // Método para retornar uma matriz correspondente à partida de xadrez.
    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()]; // Instancia a matriz do tabuleiro.
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j); // Preenche a matriz com as peças do tabuleiro.
            }
        }
        return mat;
    }

    // Método para retornar os movimentos possíveis de uma peça.
    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition(); // Converte a posição de xadrez para uma posição de matriz.
        validateSourcePosition(position); // Valida a posição de origem da peça.
        return board.piece(position).possibleMoves(); // Retorna os movimentos possíveis da peça.
    }

    // Método para realizar um movimento no xadrez.
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition(); // Converte a posição de origem para uma posição de matriz.
        Position target = targetPosition.toPosition(); // Converte a posição de destino para uma posição de matriz.
        validateSourcePosition(source); // Valida a posição de origem da peça.
        validadeTargetPosition(source, target); // Valida a posição de destino da peça.
        Piece capturedPiece = makeMove(source, target); // Realiza o movimento da peça.

        // Verifica se o movimento coloca o jogador atual em cheque.
        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece); // Desfaz o movimento.
            throw new ChessException("You can't put yourself in check"); // Lança uma exceção.
        }

        ChessPiece movedPiece = (ChessPiece) board.piece(target);

        // #specialmove promotion
        promoted = null;
        if (movedPiece instanceof Pawn) {
            if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0)
                    || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
                promoted = (ChessPiece) board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
        }

        // Verifica se o movimento coloca o adversário em cheque.
        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn(); // Passa para o próximo turno.
        }

        // #specialmove en passant
        if (movedPiece instanceof Pawn
                && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece; // Retorna a peça capturada, se houver.
    }

    public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
			throw new InvalidParameterException("Invalid type for promotion");
		}

		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);

		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);

		return newPiece;
	}

	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color);
		if (type.equals("N")) return new Knight(board, color);
		if (type.equals("Q")) return new Queen(board, color);
		return new Rook(board, color);
	}

    // Método para validar a posição de origem da peça.
    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) { // Verifica se há uma peça na posição de origem.
            throw new ChessException("There is no piece on source position. "); // Lança uma exceção se não houver.
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) { // Verifica se a peça é do jogador
                                                                                // atual.
            throw new ChessException("The chosen piece is not yours"); // Lança uma exceção se não for.
        }
        if (!board.piece(position).isThereAnyPossibleMove()) { // Verifica se a peça tem algum movimento possível.
            throw new ChessException("There is no possible moves for the chosen piece. "); // Lança uma exceção se não
                                                                                           // tiver.
        }
    }

    // Método para validar a posição de destino da peça.
    private void validadeTargetPosition(Position source, Position target) {
        // Verifica se a peça na posição de origem pode se mover para a posição de
        // destino.
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position. "); // Lança uma exceção se não
                                                                                          // puder.
        }
    }

    // Método para passar para o próximo turno.
    private void nextTurn() {
        turn++; // Incrementa o turno.
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE; // Alterna o jogador atual.
    }

    // Método para realizar o movimento de uma peça.
    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source); // Remove a peça da posição de origem.
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target); // Remove a peça da posição de destino, se houver.
        board.placePiece(p, target); // Coloca a peça na posição de destino.

        // Se houver uma peça capturada, a remove da lista de peças no tabuleiro e a
        // adiciona à lista de peças capturadas.
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        // #specialmove castling kingside rook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #specialmove castling queenside rook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #specialmove en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }
        return capturedPiece; // Retorna a peça capturada, se houver.
    }

    // Método para desfazer um movimento.
    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target); // Remove a peça da posição de destino.
        p.decreaseMoveCount();
        board.placePiece(p, source); // Coloca a peça de volta na posição de origem.

        // Se houver uma peça capturada, a coloca de volta na posição de destino, a
        // remove da lista de peças capturadas e a adiciona à lista de peças no
        // tabuleiro.
        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
        // #specialmove castling kingside rook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.increaseMoveCount();
        }

        // #specialmove castling queenside rook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.increaseMoveCount();
        }

        // #specialmove en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }
    }

    // Método para colocar uma nova peça no tabuleiro.
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        // Converte a posição de xadrez para uma posição de matriz e coloca a peça na
        // posição.
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece); // Adiciona a peça à lista de peças no tabuleiro.
    }

    // Método para retornar o oponente do jogador atual.
    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // Método para retornar o rei do jogador atual.
    private ChessPiece king(Color color) {
        // Filtra a lista de peças no tabuleiro para obter as peças do jogador atual.
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
                .collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) { // Verifica se a peça é um rei.
                return (ChessPiece) p; // Retorna o rei.
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board"); // Lança uma exceção se não
                                                                                        // houver um rei.
    }

    // Método para verificar se o rei do jogador atual está em cheque.
    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition(); // Obtém a posição do rei.
        // Filtra a lista de peças no tabuleiro para obter as peças do oponente.
        List<Piece> opponentPieces = piecesOnTheBoard.stream()
                .filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves(); // Obtém os movimentos possíveis da peça.
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { // Verifica se a peça pode se mover para a
                                                                        // posição do rei.
                return true; // Retorna verdadeiro se puder.
            }
        }
        return false; // Retorna falso se não puder.
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
                .collect(Collectors.toList());
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // Método responsável por iniciar a partida de xadrez colocando as peças no
    // tabuleiro
    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }
}
