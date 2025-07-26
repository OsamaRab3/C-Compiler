package analyzer.exceptions;

public class ScannerException extends Exception {
    private final int line;
    private final int column;

    public ScannerException(String message, int line, int column) {
        super(String.format("Scanner error at line %d, column %d: %s", line, column, message));
        this.line = line;
        this.column = column;
    }

    public ScannerException(String message) {
        super(message);
        this.line = -1;
        this.column = -1;
    }

    public int getLine() { return line; }
    public int getColumn() { return column; }
}
