package br.edu.ufape.taiti.gui;

import java.io.File;
import java.util.ArrayList;

public class OpenFeatureFile {

    private File file;
    private ArrayList<FileLine> fileLines;

    public OpenFeatureFile(File file, ArrayList<FileLine> fileLines) {
        this.file = file;
        this.fileLines = fileLines;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ArrayList<FileLine> getFileLines() {
        return fileLines;
    }

    public void setFileLines(ArrayList<FileLine> fileLines) {
        this.fileLines = fileLines;
    }

    public int deselectLine(TestRow testRow) {
        int deselectedLine = -1;
        for (FileLine f : fileLines) {
            if (f.getLineNumber() != -1 && f.getLine().strip().equals(testRow.getTest())) {
                f.setCheckbox(!f.getCheckbox());
                deselectedLine = f.getLineNumber();
            }
        }

        return deselectedLine;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof OpenFeatureFile) {
            OpenFeatureFile f = (OpenFeatureFile) o;

            return this.file.getAbsolutePath().equals(f.getFile().getAbsolutePath());
        }

        return false;
    }
}
