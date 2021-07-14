package br.edu.ufape.taiti.gui;

public class RowFile {

    private Boolean checkbox;
    private String fileLine;

    public RowFile (Boolean checkbox, String fileLine) {
        this.checkbox = checkbox;
        this.fileLine = fileLine;
    }

    public Boolean getCheckbox() {
        return checkbox;
    }

    public String getFileLine() {
        return fileLine;
    }

    public void setCheckbox(Boolean checkbox) {
        this.checkbox = checkbox;
    }

    public void setFileLine(String fileLine) {
        this.fileLine = fileLine;
    }
}
