package analyzer.interfaces;

import analyzer.Token.Token;
import analyzer.exceptions.ScannerException;
import java.util.List;

public interface IScanner {
    List<Token> scanTokens(String source) throws ScannerException;
    Token getNextToken() throws ScannerException;
    void reset();
    boolean hasMoreTokens();
}
