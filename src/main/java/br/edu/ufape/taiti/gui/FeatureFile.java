package br.edu.ufape.taiti.gui;

import java.io.File;
import java.util.ArrayList;

public class FeatureFile {

    private File file;
    private ArrayList<RowFile> fileLines;

    public FeatureFile(File file, ArrayList<RowFile> fileLines) {
        this.file = file;
        this.fileLines = fileLines;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ArrayList<RowFile> getFileLines() {
        return fileLines;
    }

    public void setFileLines(ArrayList<RowFile> fileLines) {
        this.fileLines = fileLines;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FeatureFile) {
            FeatureFile f = (FeatureFile) o;

            return this.file.getAbsolutePath().equals(f.getFile().getAbsolutePath());
        }

        return false;
    }
}
