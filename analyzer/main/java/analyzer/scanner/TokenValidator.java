package analyzer.scanner;

import analyzer.interfaces.ITokenValidator;
import analyzer.Token.TokenType;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TokenValidator implements ITokenValidator {
    private static final Map<String, TokenType> KEYWORDS = new HashMap<>();
    private static final Set<String> C_KEYWORDS = Set.of(
            "auto", "break", "case", "char", "const", "continue", "default", "do",
            "double", "else", "enum", "extern", "float", "for", "goto", "if",
            "int", "long", "register", "return", "short", "signed", "sizeof", "static",
            "struct", "switch", "typedef", "union", "unsigned", "void", "volatile", "while"
    );

    static {
        KEYWORDS.put("auto", TokenType.AUTO);
        KEYWORDS.put("break", TokenType.BREAK);
        KEYWORDS.put("case", TokenType.CASE);
        KEYWORDS.put("char", TokenType.CHAR);
        KEYWORDS.put("const", TokenType.CONST);
        KEYWORDS.put("continue", TokenType.CONTINUE);
        KEYWORDS.put("default", TokenType.DEFAULT);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("double", TokenType.DOUBLE);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("enum", TokenType.ENUM);
        KEYWORDS.put("extern", TokenType.EXTERN);
        KEYWORDS.put("float", TokenType.FLOAT);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("goto", TokenType.GOTO);
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("int", TokenType.INT);
        KEYWORDS.put("long", TokenType.LONG);
        KEYWORDS.put("register", TokenType.REGISTER);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("short", TokenType.SHORT);
        KEYWORDS.put("signed", TokenType.SIGNED);
        KEYWORDS.put("sizeof", TokenType.SIZEOF);
        KEYWORDS.put("static", TokenType.STATIC);
        KEYWORDS.put("struct", TokenType.STRUCT);
        KEYWORDS.put("switch", TokenType.SWITCH);
        KEYWORDS.put("typedef", TokenType.TYPEDEF);
        KEYWORDS.put("union", TokenType.UNION);
        KEYWORDS.put("unsigned", TokenType.UNSIGNED);
        KEYWORDS.put("void", TokenType.VOID);
        KEYWORDS.put("volatile", TokenType.VOLATILE);
        KEYWORDS.put("while", TokenType.WHILE);
    }

    @Override
    public boolean isKeyword(String text) {
        return C_KEYWORDS.contains(text.toLowerCase());
    }

    @Override
    public boolean isValidIdentifier(String text) {
        if (text == null || text.isEmpty()) return false;
        if (!Character.isLetter(text.charAt(0)) && text.charAt(0) != '_') return false;

        for (int i = 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') return false;
        }
        return true;
    }

    @Override
    public boolean isValidNumber(String text) {
        if (text == null || text.isEmpty()) return false;
        try {
            if (text.contains(".")) {
                Double.parseDouble(text);
            } else {
                Integer.parseInt(text);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public TokenType getKeywordType(String text) {
        return KEYWORDS.get(text.toLowerCase());
    }
}
