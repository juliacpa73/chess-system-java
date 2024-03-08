package chess;

import boardGame.Board;
import boardGame.Piece;

public abstract class ChessPiece extends Piece { // subclasse da classe Piece

    private Color color; // Associação com a classe enum Color

    public ChessPiece(Board board, Color color) {
        super(board); // Está repassando o construtor da classe-mãe Piece
        this.color = color;
    }

    public Color getColor() { // Tem apenas o método get, pois a cor da peça será acessada e não modificada,
                              // como aconteceria com o método set
        return color;
    }

}
