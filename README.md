# C Language Analyzer

A lexical analyzer (scanner) for C programming language built with Java and Gradle.

## Project Structure

```
analyzer/
├── src/main/java/analyzer/
│   ├── Main.java                    # Main application
│   ├── Token/
│   │   ├── Token.java              # Token data structure
│   │   └── TokenType.java          # Token types enumeration
│   ├── interfaces/
│   │   ├── IScanner.java           # Scanner interface
│   │   ├── ITokenValidator.java    # Validator interface
│   │   └── ISymbolTable.java       # Symbol table interface
│   ├── scanner/
│   │   ├── CScanner.java           # Main scanner implementation
│   │   ├── TokenValidator.java     # Token validation
│   │   └── SymbolTable.java        # Symbol table implementation
│   └── exceptions/
│       └── ScannerException.java   # Exception handling
├── test.c                          # Sample C file for testing
└── README.md                       # This file
```

## Build & Usage 

### 1. Compile

From the `analyzer/` directory, run:

```sh
javac -d out $(find main/java -name "*.java")
```

### 2. Run

To scan a file (e.g., `main/java/analyzer/test.c`):

```sh
java -cp out analyzer.Main main/java/analyzer/test.c
```

Or to use the interactive REPL:

```sh
java -cp out analyzer.Main
```



## Usage Examples

### Sample C Code (test.c)
```c
#include <stdio.h>

int main() {
    int x = 42;
    float y = 3.14;
    
    if (x > 10) {
        printf("Hello, World!\n");
    }
    
    return 0;
}
```

### Expected Output
```
=== TOKENS ===
Token{type=PREPROCESSOR, lexeme='#include <stdio.h>', line=1, col=1, value=null}
Token{type=INT, lexeme='int', line=3, col=1, value=null}
Token{type=IDENTIFIER, lexeme='main', line=3, col=5, value=null}
Token{type=LEFT_PAREN, lexeme='(', line=3, col=9, value=null}
...

=== SYMBOL TABLE ===
Symbols found: 4
```

## Supported C Features

### Keywords
- All C89/C90 keywords: `auto`, `break`, `case`, `char`, `const`, `continue`, `default`, `do`, `double`, `else`, `enum`, `extern`, `float`, `for`, `goto`, `if`, `int`, `long`, `register`, `return`, `short`, `signed`, `sizeof`, `static`, `struct`, `switch`, `typedef`, `union`, `unsigned`, `void`, `volatile`, `while`

### Operators
- Arithmetic: `+`, `-`, `*`, `/`, `%`
- Assignment: `=`, `+=`, `-=`, `*=`, `/=`, `%=`
- Comparison: `==`, `!=`, `<`, `<=`, `>`, `>=`
- Logical: `&&`, `||`, `!`
- Bitwise: `&`, `|`, `^`, `~`, `<<`, `>>`
- Increment/Decrement: `++`, `--`
- Other: `->`, `.`, `?`

### Literals
- Integer: `42`, `0`, `-123`
- Float: `3.14`, `0.5`
- Character: `'a'`, `'\n'`
- String: `"hello"`, `"world\n"`

### Comments
- Line comments: `// comment`
- Block comments: `/* comment */`

## Architecture

This project follows SOLID principles:

- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: Extensible through interfaces
- **Liskov Substitution**: Implementations are interchangeable
- **Interface Segregation**: Focused interfaces
- **Dependency Inversion**: Depends on abstractions

## Development

### Adding New Features
1. Define interface if needed
2. Implement the interface
3. Update dependencies in `CScanner`
4. Add tests



