package analyzer.interfaces;

import analyzer.Token.TokenType;

public interface ITokenValidator {
    boolean isKeyword(String text);
    boolean isValidIdentifier(String text);
    boolean isValidNumber(String text);
    TokenType getKeywordType(String text);
}


