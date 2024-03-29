package boardGame;

public abstract class Piece { // Classe-mäe Piece (abstrata) para todas as peças do jogo.

    protected Position position; // Está protegida para a classe Position é uma classe de matriz, não pode
                                 // aparecer no terminal ao rodar o sistema
    private Board board; // Associação com a classe Board

    public Piece(Board board) {
        this.board = board;
        position = null; // A posição inicial da peça é nula
    }

    public Position getPosition() {
        return position;
    }

    protected Board getBoard() { // Não há setter para o tabuleiro, pois uma vez que a peça é colocada no
                                 // tabuleiro, isso não pode ser alterado
        return board;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position) {
        return possibleMoves()[position.getRow()][position.getColumn()]; // (rook methods - gancho com a subclasse)
    }

    public boolean isThereAnyPossibleMove() {
        boolean[][] mat = possibleMoves();
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat.length; j++) {
                if (mat[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

}
