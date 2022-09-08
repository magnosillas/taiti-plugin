package br.edu.ufape.taiti.tool;

/**
 * Esta classe representa um scenario selecionado.
 */
public class ScenarioTestInformation {
    private String filePath;
    private int lineNumber;

    public ScenarioTestInformation(String filePath, int lineNumber) {
        this.filePath = filePath;
        this.lineNumber = lineNumber;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScenarioTestInformation) {
            ScenarioTestInformation scenario = (ScenarioTestInformation) o;
            return scenario.getFilePath().equals(this.filePath) && scenario.getLineNumber() == this.lineNumber;
        }

        return false;
    }
}
