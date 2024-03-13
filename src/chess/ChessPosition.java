package chess;

import boardGame.Position;

public class ChessPosition { // Classe responsáivel pela posição do xadrez (linha e coluna)
    private char column;
    private int row;

    public ChessPosition(char column, int row) {
        if (column < 'a' || column > 'h' || row < 1 || row > 8) { // Programação defensiva
            throw new ChessException("Error instantiating ChessPosition. Valid values are from a1 to h8. ");
        }
        this.column = column;
        this.row = row;
    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    protected Position toPosition(){ // Método responsável por converter ChessPosition para a classe Position normal
        // *Matriz da linha*
        // Matriz_row = 8 - chess_row
        // *Matriz da coluna* 
        // Posição de cada coluna no tabuleiro: a = 0, b = 1, c = 2, d = 3, e = 4, f = 5, g = 6, h = 7.
        // 'a' - 'a' = 0, 'b' - 'a' = 1..... Nesse caso, matriz_column = chess_column - 'a' ('a' referente um caractere da coluna a)
        return new Position(8 - row, column - 'a');
    }

    protected static ChessPosition fromPosition(Position position){
        return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow());
    }

    @Override
    public String toString() {
        return " " + column + row; // Concatenação de strings
    }

}

