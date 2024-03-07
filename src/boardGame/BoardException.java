package boardGame;

public class BoardException extends RuntimeException { // Essa classe BoardException está herdando da classe-mãe
                                                       // RuntimeException, localizada na biblioteca do Java.
    private static final long serialVersionUID = 1L;

    public BoardException(String msg) { // Construtor de recebe mensagem
        super(msg);
    }
}
