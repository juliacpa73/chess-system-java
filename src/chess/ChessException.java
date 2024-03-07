package chess;

public class ChessException extends RuntimeException { // Essa classe personalizada ChessException está herdando da
                                                       // classe-mãe RuntimeException, localizada na biblioteca do Java
    private static final long servialVersionUID = 1L;

    public ChessException(String msg){
        super(msg);
    }
}
