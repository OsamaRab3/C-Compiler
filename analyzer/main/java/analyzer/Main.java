
package analyzer;

import analyzer.interfaces.IScanner;
import analyzer.interfaces.ITokenValidator;
import analyzer.interfaces.ISymbolTable;
import analyzer.scanner.CScanner;
import analyzer.scanner.TokenValidator;
import analyzer.scanner.SymbolTable;
import analyzer.Token.Token;
import analyzer.exceptions.ScannerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final ITokenValidator validator = new TokenValidator();
    private static final ISymbolTable symbolTable = new SymbolTable();
    private static final IScanner scanner = new CScanner(validator, symbolTable);

    public static void main(String[] args) {
        if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) {
        try {
            String source = Files.readString(Path.of(path));
            scan(source);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(74);
        }
    }

    private static void runPrompt() {
        Scanner input = new Scanner(System.in);

        System.out.println("C Scanner REPL");
        System.out.println("Enter C code to tokenize (type 'exit' to quit):");

        while (true) {
            System.out.print("> ");
            String line = input.nextLine();

            if ("exit".equals(line)) break;
            if (line.trim().isEmpty()) continue;

            scan(line);
        }

        input.close();
    }

    private static void scan(String source) {
        try {
            List<Token> tokens = scanner.scanTokens(source);

            System.out.println("\n=== TOKENS ===");
            for (Token token : tokens) {
                System.out.println(token);
            }

            System.out.println("\n=== SYMBOL TABLE ===");
            System.out.println("Symbols found: " + ((SymbolTable) symbolTable).size());

        } catch (ScannerException e) {
            System.err.println("Scanner Error: " + e.getMessage());
        }
    }
}
//
//// ================================
//// 7. Test Class
//// ================================
//
//// CScannerTest.java
//package com.compiler.scanner;
//
//import com.compiler.scanner.interfaces.IScanner;
//import com.compiler.scanner.interfaces.ITokenValidator;
//import com.compiler.scanner.interfaces.ISymbolTable;
//import com.compiler.scanner.scanner.CScanner;
//import com.compiler.scanner.scanner.TokenValidator;
//import com.compiler.scanner.scanner.SymbolTable;
//import com.compiler.scanner.tokens.Token;
//import com.compiler.scanner.tokens.TokenType;
//import com.compiler.scanner.exceptions.ScannerException;
//
//import java.util.List;
//
//public class CScannerTest {
//    public static void main(String[] args) {
//        CScannerTest test = new CScannerTest();
//        test.runAllTests();
//    }
//
//    private void runAllTests() {
//        System.out.println("Running C Scanner Tests...");
//
//        testBasicTokens();
//        testKeywords();
//        testOperators();
//        testLiterals();
//        testComments();
//        testComplexProgram();
//
//        System.out.println("All tests completed!");
//    }
//
//    private void testBasicTokens() {
//        System.out.println("\n--- Testing Basic Tokens ---");
//        String source = "int main() { return 0; }";
//        scanAndPrint(source);
//    }
//
//    private void testKeywords() {
//        System.out.println("\n--- Testing Keywords ---");
//        String source = "if else while for int char float double void";
//        scanAndPrint(source);
//    }
//
//    private void testOperators() {
//        System.out.println("\n--- Testing Operators ---");
//        String source = "+ - * / % = == != < <= > >= && || ! ++ --";
//        scanAndPrint(source);
//    }
//
//    private void testLiterals() {
//        System.out.println("\n--- Testing Literals ---");
//        String source = "42 3.14 'a' \"hello world\"";
//        scanAndPrint(source);
//    }
//
//    private void testComments() {
//        System.out.println("\n--- Testing Comments ---");
//        String source = "// Line comment\n/* Block comment */ int x;";
//        scanAndPrint(source);
//    }
//
//    private void testComplexProgram() {
//        System.out.println("\n--- Testing Complex Program ---");
//        String source = """
//            #include <stdio.h>
//
//            int main() {
//                int x = 10;
//                float y = 3.14;
//                char c = 'A';
//
//                if (x > 5) {
//                    printf("x is greater than 5\\n");
//                }
//
//                for (int i = 0; i < x; i++) {
//                    y += 1.5;
//                }
//
//                return 0;
//            }
//            """;
//        scanAndPrint(source);
//    }
//
//    private void scanAndPrint(String source) {
//        try {
//            ITokenValidator validator = new TokenValidator();
//            ISymbolTable symbolTable = new SymbolTable();
//            IScanner scanner = new CScanner(