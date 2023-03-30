package br.edu.ufape.taiti.gui.configuretask.fileview;

/**
 * Esta classe representa uma linha de um arquivo feature representado na tabela.
 */
public class FileLine {

    private Boolean checkbox;
    private String line;
    private int lineNumber;

    public FileLine(Boolean checkbox, String line, int lineNumber) {
        this.checkbox = checkbox;
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public String getLine() {
        return line;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setCheckbox(Boolean checkbox) {
        this.checkbox = checkbox;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}
