package boardGame; //pacote correspondente a camada tabuleiro

public class Position { // Classe-mãe Posição que indica a posição do tabuleiro
    private int row; // Linha
    private int column; // Coluna

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setValues(int row, int column){
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() { // toString para imprimir a posição na tela
        return row + ", " + column + ".";
    }

}
