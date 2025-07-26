package analyzer.scanner;

import analyzer.interfaces.IScanner;
import analyzer.interfaces.ITokenValidator;
import analyzer.interfaces.ISymbolTable;
import analyzer.Token.Token;
import analyzer.Token.TokenType;
import analyzer.exceptions.ScannerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CScanner implements IScanner {
    private final ITokenValidator validator;
    private final ISymbolTable symbolTable;
    private String source;
    private int current = 0;
    private int line = 1;
    private int column = 1;
    private final List<Token> tokens = new ArrayList<>();

    // Operator mapping for quick lookup
    private static final Map<String, TokenType> OPERATORS = new HashMap<>();
    static {
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.MULTIPLY);
        OPERATORS.put("/", TokenType.DIVIDE);
        OPERATORS.put("%", TokenType.MODULO);
        OPERATORS.put("=", TokenType.ASSIGN);
        OPERATORS.put("+=", TokenType.PLUS_ASSIGN);
        OPERATORS.put("-=", TokenType.MINUS_ASSIGN);
        OPERATORS.put("*=", TokenType.MULTIPLY_ASSIGN);
        OPERATORS.put("/=", TokenType.DIVIDE_ASSIGN);
        OPERATORS.put("%=", TokenType.MODULO_ASSIGN);
        OPERATORS.put("++", TokenType.INCREMENT);
        OPERATORS.put("--", TokenType.DECREMENT);
        OPERATORS.put("==", TokenType.EQUAL);
        OPERATORS.put("!=", TokenType.NOT_EQUAL);
        OPERATORS.put("<", TokenType.LESS_THAN);
        OPERATORS.put("<=", TokenType.LESS_EQUAL);
        OPERATORS.put(">", TokenType.GREATER_THAN);
        OPERATORS.put(">=", TokenType.GREATER_EQUAL);
        OPERATORS.put("&&", TokenType.LOGICAL_AND);
        OPERATORS.put("||", TokenType.LOGICAL_OR);
        OPERATORS.put("!", TokenType.LOGICAL_NOT);
        OPERATORS.put("&", TokenType.BITWISE_AND);
        OPERATORS.put("|", TokenType.BITWISE_OR);
        OPERATORS.put("^", TokenType.BITWISE_XOR);
        OPERATORS.put("~", TokenType.BITWISE_NOT);
        OPERATORS.put("<<", TokenType.LEFT_SHIFT);
        OPERATORS.put(">>", TokenType.RIGHT_SHIFT);
        OPERATORS.put("?", TokenType.CONDITIONAL);
        OPERATORS.put("->", TokenType.ARROW);
        OPERATORS.put(".", TokenType.DOT);
    }

    // Dependency Injection (Dependency Inversion Principle)
    public CScanner(ITokenValidator validator, ISymbolTable symbolTable) {
        this.validator = validator;
        this.symbolTable = symbolTable;
    }

    @Override
    public List<Token> scanTokens(String source) throws ScannerException {
        this.source = source;
        reset();

        while (!isAtEnd()) {
            Token token = scanToken();
            if (token != null) {
                tokens.add(token);

                // Add identifiers to symbol table
                if (token.getType() == TokenType.IDENTIFIER) {
                    symbolTable.addSymbol(token.getLexeme(), token);
                }
            }
        }

        tokens.add(new Token(TokenType.EOF, "", line, column));
        return new ArrayList<>(tokens);
    }

    @Override
    public Token getNextToken() throws ScannerException {
        if (isAtEnd()) {
            return new Token(TokenType.EOF, "", line, column);
        }
        return scanToken();
    }

    @Override
    public void reset() {
        current = 0;
        line = 1;
        column = 1;
        tokens.clear();
        symbolTable.clear();
    }

    @Override
    public boolean hasMoreTokens() {
        return !isAtEnd();
    }

    private Token scanToken() throws ScannerException {
        char c = advance();

        switch (c) {
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                return null;
            case '\n':
                line++;
                column = 1;
                return null;
            case '#':
                return preprocessor();
            case '/':
                if (match('/')) {
                    return lineComment();
                } else if (match('*')) {
                    return blockComment();
                } else if (match('=')) {
                    return new Token(TokenType.DIVIDE_ASSIGN, "/=", line, column - 2);
                } else {
                    return new Token(TokenType.DIVIDE, "/", line, column - 1);
                }
            case '"':
                return stringLiteral();
            case '\'':
                return charLiteral();
            case ';': return new Token(TokenType.SEMICOLON, ";", line, column - 1);
            case ',': return new Token(TokenType.COMMA, ",", line, column - 1);
            case '(': return new Token(TokenType.LEFT_PAREN, "(", line, column - 1);
            case ')': return new Token(TokenType.RIGHT_PAREN, ")", line, column - 1);
            case '{': return new Token(TokenType.LEFT_BRACE, "{", line, column - 1);
            case '}': return new Token(TokenType.RIGHT_BRACE, "}", line, column - 1);
            case '[': return new Token(TokenType.LEFT_BRACKET, "[", line, column - 1);
            case ']': return new Token(TokenType.RIGHT_BRACKET, "]", line, column - 1);
            case '+':
                if (match('+')) return new Token(TokenType.INCREMENT, "++", line, column - 2);
                if (match('=')) return new Token(TokenType.PLUS_ASSIGN, "+=", line, column - 2);
                return new Token(TokenType.PLUS, "+", line, column - 1);
            case '-':
                if (match('-')) return new Token(TokenType.DECREMENT, "--", line, column - 2);
                if (match('=')) return new Token(TokenType.MINUS_ASSIGN, "-=", line, column - 2);
                if (match('>')) return new Token(TokenType.ARROW, "->", line, column - 2);
                return new Token(TokenType.MINUS, "-", line, column - 1);
            case '*':
                if (match('=')) return new Token(TokenType.MULTIPLY_ASSIGN, "*=", line, column - 2);
                return new Token(TokenType.MULTIPLY, "*", line, column - 1);
            case '%':
                if (match('=')) return new Token(TokenType.MODULO_ASSIGN, "%=", line, column - 2);
                return new Token(TokenType.MODULO, "%", line, column - 1);
            case '=':
                if (match('=')) return new Token(TokenType.EQUAL, "==", line, column - 2);
                return new Token(TokenType.ASSIGN, "=", line, column - 1);
            case '!':
                if (match('=')) return new Token(TokenType.NOT_EQUAL, "!=", line, column - 2);
                return new Token(TokenType.LOGICAL_NOT, "!", line, column - 1);
            case '<':
                if (match('=')) return new Token(TokenType.LESS_EQUAL, "<=", line, column - 2);
                if (match('<')) return new Token(TokenType.LEFT_SHIFT, "<<", line, column - 2);
                return new Token(TokenType.LESS_THAN, "<", line, column - 1);
            case '>':
                if (match('=')) return new Token(TokenType.GREATER_EQUAL, ">=", line, column - 2);
                if (match('>')) return new Token(TokenType.RIGHT_SHIFT, ">>", line, column - 2);
                return new Token(TokenType.GREATER_THAN, ">", line, column - 1);
            case '&':
                if (match('&')) return new Token(TokenType.LOGICAL_AND, "&&", line, column - 2);
                return new Token(TokenType.BITWISE_AND, "&", line, column - 1);
            case '|':
                if (match('|')) return new Token(TokenType.LOGICAL_OR, "||", line, column - 2);
                return new Token(TokenType.BITWISE_OR, "|", line, column - 1);
            case '^': return new Token(TokenType.BITWISE_XOR, "^", line, column - 1);
            case '~': return new Token(TokenType.BITWISE_NOT, "~", line, column - 1);
            case '?': return new Token(TokenType.CONDITIONAL, "?", line, column - 1);
            case '.': return new Token(TokenType.DOT, ".", line, column - 1);
            default:
                if (isDigit(c)) {
                    return number();
                }
                if (isAlpha(c)) {
                    return identifier();
                }
                throw new ScannerException("Unexpected character: " + c, line, column - 1);
        }
    }

    private Token preprocessor() {
        while (peek() != '\n' && !isAtEnd()) advance();
        return new Token(TokenType.PREPROCESSOR,
                source.substring(current - (column - 1), current),
                line, column);
    }

    private Token lineComment() {
        int start = current - 2;
        while (peek() != '\n' && !isAtEnd()) advance();
        return new Token(TokenType.COMMENT,
                source.substring(start, current),
                line, column);
    }

    private Token blockComment() throws ScannerException {
        int start = current - 2;
        int startLine = line;
        int startColumn = column - 2;

        while (!isAtEnd()) {
            if (peek() == '*' && peekNext() == '/') {
                advance(); // consume '*'
                advance(); // consume '/'
                return new Token(TokenType.COMMENT,
                        source.substring(start, current),
                        startLine, startColumn);
            }
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            advance();
        }

        throw new ScannerException("Unterminated block comment", startLine, startColumn);
    }

    private Token stringLiteral() throws ScannerException {
        int start = current - 1;
        int startColumn = column - 1;

        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
                column = 1;
            }
            if (peek() == '\\') advance(); // Handle escape sequences
            advance();
        }

        if (isAtEnd()) {
            throw new ScannerException("Unterminated string", line, startColumn);
        }

        advance(); // Closing "
        String value = source.substring(start + 1, current - 1);
        return new Token(TokenType.STRING_LITERAL,
                source.substring(start, current),
                line, startColumn, value);
    }

    private Token charLiteral() throws ScannerException {
        int start = current - 1;
        int startColumn = column - 1;

        if (peek() == '\\') {
            advance(); // Skip escape character
        }

        if (!isAtEnd()) advance(); // Character

        if (peek() != '\'' || isAtEnd()) {
            throw new ScannerException("Unterminated character literal", line, startColumn);
        }

        advance(); // Closing '
        String lexeme = source.substring(start, current);
        char value = lexeme.charAt(1); // Get the actual character
        return new Token(TokenType.CHAR_LITERAL, lexeme, line, startColumn, value);
    }

    private Token number() {
        int start = current - 1;
        int startColumn = column - 1;

        while (isDigit(peek())) advance();

        boolean isFloat = false;
        if (peek() == '.' && isDigit(peekNext())) {
            isFloat = true;
            advance(); // consume '.'
            while (isDigit(peek())) advance();
        }

        String lexeme = source.substring(start, current);
        Object value = isFloat ? Double.parseDouble(lexeme) : Integer.parseInt(lexeme);
        TokenType type = isFloat ? TokenType.FLOAT_LITERAL : TokenType.INTEGER_LITERAL;

        return new Token(type, lexeme, line, startColumn, value);
    }

    private Token identifier() {
        int start = current - 1;
        int startColumn = column - 1;

        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = validator.isKeyword(text) ?
                validator.getKeywordType(text) : TokenType.IDENTIFIER;

        return new Token(type, text, line, startColumn);
    }

    // Utility methods
    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char advance() {
        column++;
        return source.charAt(current++);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;
        current++;
        column++;
        return true;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
