package br.edu.ufape.taiti.gui;

public class FileLine {

    private Boolean checkbox;
    private String line;

    public FileLine(Boolean checkbox, String line) {
        this.checkbox = checkbox;
        this.line = line;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public String getLine() {
        return line;
    }

    public void setCheckbox(Boolean checkbox) {
        this.checkbox = checkbox;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
