package br.edu.ufape.taiti.gui;

public class ScenarioTestInformaiton {
    private String filePath;
    private int lineNumber;

    public ScenarioTestInformaiton(String filePath, int lineNumber) {
        this.filePath = filePath;
        this.lineNumber = lineNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScenarioTestInformaiton) {
            ScenarioTestInformaiton scenario = (ScenarioTestInformaiton) o;
            return scenario.getFilePath().equals(this.filePath) && scenario.getLineNumber() == this.lineNumber;
        }

        return false;
    }
}
