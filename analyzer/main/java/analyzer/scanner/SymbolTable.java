package analyzer.scanner;

import analyzer.interfaces.ISymbolTable;
import analyzer.Token.Token;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable implements ISymbolTable {
    private final Map<String, Token> symbols;

    public SymbolTable() {
        this.symbols = new HashMap<>();
    }

    @Override
    public void addSymbol(String name, Token token) {
        symbols.put(name, token);
    }

    @Override
    public Token getSymbol(String name) {
        return symbols.get(name);
    }

    @Override
    public boolean containsSymbol(String name) {
        return symbols.containsKey(name);
    }

    @Override
    public void clear() {
        symbols.clear();
    }

    public int size() {
        return symbols.size();
    }
}