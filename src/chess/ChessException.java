package chess;

import boardGame.BoardException;

public class ChessException extends BoardException { // Essa classe personalizada ChessException está herdando da
                                                       // classe-mãe RuntimeException, localizada na biblioteca do Java
    private static final long servialVersionUID = 1L;

    public ChessException(String msg){
        super(msg);
    }
}
