package br.edu.ufape.taiti.gui.configuretask.fileview;

import java.io.File;
import java.util.ArrayList;

/**
 * Esta classe representa um repositório de arquivos features já abertos.
 */
public class RepositoryOpenFeatureFile {

    private ArrayList<OpenFeatureFile> openFeatureFiles;

    public RepositoryOpenFeatureFile() {
        this.openFeatureFiles = new ArrayList<>();
    }

    public void addFeatureFile(OpenFeatureFile openFeatureFile) {
        this.openFeatureFiles.add(openFeatureFile);
    }

    public boolean exists(File file) {
        boolean e = false;
        for (OpenFeatureFile f : openFeatureFiles) {
            if (f.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                e = true;
                break;
            }
        }

        return e;
    }

    public OpenFeatureFile getFeatureFile(File file) {
        OpenFeatureFile openFeatureFile = null;
        for (OpenFeatureFile f : openFeatureFiles) {
            if (f.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                openFeatureFile = f;
                break;
            }
        }

        return openFeatureFile;
    }
}
