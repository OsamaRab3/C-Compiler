package analyzer.Token;
public class Token {
    private final TokenType type;
    private final String lexeme;
    private final int line;
    private final int column;
    private final Object value;

    public Token(TokenType type, String lexeme, int line, int column, Object value) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
        this.value = value;
    }

    public Token(TokenType type, String lexeme, int line, int column) {
        this(type, lexeme, line, column, null);
    }

    // Getters
    public TokenType getType() { return type; }
    public String getLexeme() { return lexeme; }
    public int getLine() { return line; }
    public int getColumn() { return column; }
    public Object getValue() { return value; }

    @Override
    public String toString() {
        return String.format("Token{type=%s, lexeme='%s', line=%d, col=%d, value=%s}",
                type, lexeme, line, column, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Token)) return false;
        Token token = (Token) obj;
        return type == token.type &&
                lexeme.equals(token.lexeme) &&
                line == token.line &&
                column == token.column;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, lexeme, line, column);
    }
}