package br.edu.ufape.taiti.gui;

import java.io.File;
import java.util.ArrayList;

public class FeatureFileRepository {

    private ArrayList<FeatureFile> featureFiles;

    public FeatureFileRepository() {
        this.featureFiles = new ArrayList<>();
    }

    public void addFeatureFile(FeatureFile featureFile) {
        this.featureFiles.add(featureFile);
    }

    public boolean exists(File file) {
        boolean e = false;
        for (FeatureFile f : featureFiles) {
            if (f.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                e = true;
                break;
            }
        }

        return e;
    }

    public FeatureFile findFeatureFile(File file) {
        FeatureFile featureFile = null;
        for (FeatureFile f : featureFiles) {
            if (f.getFile().getAbsolutePath().equals(file.getAbsolutePath())) {
                featureFile = f;
                break;
            }
        }

        return featureFile;
    }
}
