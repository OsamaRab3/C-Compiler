package analyzer.interfaces;

import analyzer.Token.Token;

public interface ISymbolTable {
    void addSymbol(String name, Token token);
    Token getSymbol(String name);
    boolean containsSymbol(String name);
    void clear();
}