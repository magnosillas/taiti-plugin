package br.edu.ufape.taiti.gui.configuretask.fileview;

import java.io.File;
import java.util.ArrayList;

/**
 * Esta classe representa um arquivo feature já aberto, ela é usada para guardar os estados dos scenarios já selecionados.
 */
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

    public ArrayList<FileLine> getFileLines() {
        return fileLines;
    }

    public int deselectLine(String line) {
        int deselectedLine = -1;
        for (FileLine f : fileLines) {
            if (f.getLine().strip().equals(line)) {
                if (f.getCheckbox()) {
                    f.setCheckbox(!f.getCheckbox());
                }
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
