package chess;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;

public abstract class ChessPiece extends Piece { // subclasse da classe Piece

    private Color color; // Associação com a classe enum Color
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board); // Está repassando o construtor da classe-mãe Piece
        this.color = color;
    }

    // Tem apenas o método get, pois a cor da peça será acessada e não modificada,
    // como aconteceria com o método set
    public Color getColor() {
        return color;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void increaseMoveCount(){
        moveCount++;
    }

    public void decreaseMoveCount(){
        moveCount--;
    }

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

    // Método responsável por verificar se tem alguma peça oponente
    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color; // Ele vai identificar a diferença pela cor da peça
    }

}
