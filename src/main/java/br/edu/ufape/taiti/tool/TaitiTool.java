package br.edu.ufape.taiti.tool;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.*;
import java.util.*;

public class TaitiTool {

    private final String githubURL;
    private final int taskID;
    private final ArrayList<ScenarioTestInformation> scenarios;
    private final Project project;

    private static final String FILE_NAME = "scenarios.csv";

    public TaitiTool(String githubURL, int taskID, ArrayList<ScenarioTestInformation> scenarios, Project project) {
        this.githubURL = githubURL;
        this.taskID = taskID;
        this.scenarios = scenarios;
        this.project = project;
    }

    public void createTestI(ArrayList<File> files) {
        for (File f : files) {
            ArrayList<LinkedHashMap<String, Serializable>> tests = prepareScenariosFromFile(readTaitiFile(f));

            // TODO: rodar TAITI
            for (LinkedHashMap<String, Serializable> map : tests) {
                System.out.println(map.get("path"));
                System.out.println(map.get("lines"));
            }
        }
    }

    public File createScenariosFile() throws IOException {
        String[] header = {"path", "lines"};
        ArrayList<String[]> rows = new ArrayList<>();

        ArrayList<LinkedHashMap<String, Serializable>> scenariosPrepared = prepareScenariosFromUI();

        for (LinkedHashMap<String, Serializable> map : scenariosPrepared) {
            rows.add(new String[]{String.valueOf(map.get("path")), String.valueOf(map.get("lines"))});
        }

        String projectPath = "";
        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir != null) {
            projectPath = projectDir.getPath();
        }

        FileWriter writer = new FileWriter(projectPath + File.separator + FILE_NAME);
        writer.write(String.join(";", header));
        writer.write("\n");

        for (String[] s : rows) {
            writer.write(String.join(";", s));
            writer.write("\n");
        }

        writer.close();

        return new File(projectPath + File.separator + FILE_NAME);
    }

    public boolean deleteScenariosFile() {
        String projectPath = "";
        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir != null) {
            projectPath = projectDir.getPath();
        }

        File file = new File(projectPath + File.separator + FILE_NAME);
        return file.delete();
    }

    private ArrayList<String[]> readTaitiFile(File file) {
        BufferedReader br;
        String csvDivisor = ";";
        ArrayList<String[]> contentFile = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(file.getPath()));
            String l = br.readLine();
            while (l != null) {
                String[] line = l.split(csvDivisor);
                if (!(line[0].equals("path") && line[1].equals("lines"))) {
                    contentFile.add(line);
                }
                l = br.readLine();
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentFile;
    }

    private ArrayList<LinkedHashMap<String, Serializable>> prepareScenariosFromFile(ArrayList<String[]> contentFile) {
        ArrayList<LinkedHashMap<String, Serializable>> tests = new ArrayList<>();

        for (String[] lines : contentFile) {
            String path = lines[0];

            String[] numbers = lines[1].replace("[", "")
                         .replace("]", "")
                         .split(",");
            ArrayList<Integer> numberLines = new ArrayList<>();
            for (String number : numbers) {
                numberLines.add(Integer.parseInt(number.strip()));
            }

            LinkedHashMap<String, Serializable> map = new LinkedHashMap<>(2);
            map.put("path", path);
            map.put("lines", numberLines);

            tests.add(map);
        }

        return tests;
    }

    private ArrayList<LinkedHashMap<String, Serializable>> prepareScenariosFromUI() {
        ArrayList<LinkedHashMap<String, Serializable>> tests = new ArrayList<>();

        ArrayList<String> paths = new ArrayList<>();
        for (ScenarioTestInformation  s : scenarios) {
            paths.add(s.getFilePath());
        }
        Set<String> set = new HashSet<>(paths);
        paths.clear();
        paths.addAll(set);

        for (String path : paths) {
            ArrayList<Integer> lines = new ArrayList<>();
            for (ScenarioTestInformation s : scenarios) {
                if (path.equals(s.getFilePath())) {
                    lines.add(s.getLineNumber());
                }
            }
            LinkedHashMap<String, Serializable> map = new LinkedHashMap<>(2);
            map.put("path", getRelativePath(path));
            map.put("lines", lines);

            tests.add(map);
        }

        return tests;
    }

    private String getRelativePath(String absolutePath) {
        String projectName = "";

        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir != null) {
            projectName = projectDir.getName();
        }

        return absolutePath.substring(absolutePath.indexOf(projectName)).replace(projectName + File.separator, "");
    }
}
