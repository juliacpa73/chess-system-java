package boardGame;

public class Board {

    private int rows; // Quantidade de linhas do tabuleiro
    private int columns; // Quantidade de colunas do tabuleiro
    private Piece[][] pieces; // Declaração de uma matriz de objetos Piece chamada 'pieces' para representar
                              // as peças no tabuleiro.

    public Board(int rows, int columns) { // Construtor da classe Board, que recebe a quantidade de linhas e colunas
                                          // como parâmetros.
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns]; // Inicializa a matriz 'pieces' com o tamanho especificado pelas linhas e
                                           // colunas.
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Piece piece(int row, int column) { // Método para retornar a matriz de linha e coluna
        return pieces[row][column];
    }

    public Piece piece(Position position) { // Método para retornar a peça pela a sua posição no tabuleiro
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) { // Método para encontrar a posição da peça no tabuleiro
        pieces[position.getRow()][position.getColumn()] = piece; // O método vai percorrer a matriz do tabuleiro
                                                                 // (instanciada no construtor Board) para achar a peça
                                                                 // por linhas e colunas
        piece.position = position; // Aqui a peça não está mais nula como no construtor Board
    }
}
