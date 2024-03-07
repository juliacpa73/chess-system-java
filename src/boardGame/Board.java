package boardGame;

public class Board {

    private int rows; // Quantidade de linhas do tabuleiro
    private int columns; // Quantidade de colunas do tabuleiro
    private Piece[][] pieces; // Declaração de uma matriz de objetos Piece chamada 'pieces' para representar
                              // as peças no tabuleiro.

    public Board(int rows, int columns) { // Construtor da classe Board, que recebe a quantidade de linhas e colunas
                                          // como parâmetros.
        if (rows < 1 || columns < 1) { // Verificação de erro
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns]; // Inicializa a matriz 'pieces' com o tamanho especificado pelas linhas e
                                           // colunas.
    }

    public int getRows() { // Não pode ter o método setRows para não haver alteração na quantidade de
                           // linhas
        return rows;
    }

    public int getColumns() { // Não pode ter o método setColumns para não haver alteração na quantidade de
                              // colunas
        return columns;
    }

    public Piece piece(int row, int column) { // Método para retornar a matriz de linha e coluna
        if (!positionExists(row, column)) { // Se a posição não existe na linha e coluna, será enviada uma mensagem
                                            // indicando que não tem a posição no tabuleiro
            throw new BoardException("Position not on the board");
        }
        return pieces[row][column];
    }

    public Piece piece(Position position) { // Método para retornar a peça pela a sua posição no tabuleiro
        if (!positionExists(position)) {
            throw new BoardException("Position not on the board");
        }
        return pieces[position.getRow()][position.getColumn()];
    }

    public void placePiece(Piece piece, Position position) { // Método para encontrar a posição da peça no tabuleiro

        pieces[position.getRow()][position.getColumn()] = piece; // O método vai percorrer a matriz do tabuleiro
                                                                 // (instanciada no construtor Board) para achar a peça
                                                                 // por linhas e colunas
        piece.position = position; // Aqui a peça não está mais nula como no construtor Board
    }

    private boolean positionExists(int row, int column) { // Método auxiliar do método positionExists
        return row >= 0 && row < rows && column >= 0 && column < columns; // Condição para ver se a posição existe
    }

    public boolean positionExists(Position position) { // Método para informar de tem alguma posição
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position) {
        if (thereIsAPiece(position)) {// Antes de colocar uma peça na posição, é preciso testar se já existe uma peça
            // nessa posição para não colocar outra no lugar
            throw new BoardException("There is already a piece on position " + position);
        }
        return piece(position) != null;// Para saber se tem uma peça nessa posição, basta testas de a peça é diferente
                                       // de nulo
    }
}
