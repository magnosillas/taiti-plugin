package br.edu.ufape.taiti.tool;

import br.ufpe.cin.tan.analysis.task.TodoTask;
import br.ufpe.cin.tan.analysis.itask.ITest;
import br.ufpe.cin.tan.util.CsvUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class TaitiTool {

    private final String githubURL;
    private final int taskID;
    private final ArrayList<ScenarioTestInformation> scenarios;
    private final Project project;

    public TaitiTool(String githubURL, int taskID, ArrayList<ScenarioTestInformation> scenarios, Project project) {
        this.githubURL = githubURL;
        this.taskID = taskID;
        this.scenarios = scenarios;
        this.project = project;
    }

    public void run() {
        ArrayList<LinkedHashMap<String, Serializable>> tests = prepareTests();

//        for (LinkedHashMap<String, Serializable> m : tests) {
//            System.out.println(m.get("path"));
//            System.out.println(m.get("lines"));
//        }

        TodoTask task;
        ITest itest;
        try {
            task = new TodoTask(githubURL, taskID, tests);
            itest = task.computeTestBasedInterface();

            // --------- pode apagar isso?
            Set<String> files = itest.findAllFiles();
            System.out.printf("TestI(%d): %d%n", taskID, files.size());
            for (String file : files) {
                System.out.println(file);
            }

            List<String[]> content = new ArrayList<>();
            content.add(new String[]{"Url", "ID", "TestI"});
            content.add(new String[]{githubURL, String.valueOf(taskID), files.toString()});
            CsvUtil.write("exemplo_resultado_testi.csv", content);
            // ------------------------------
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<LinkedHashMap<String, Serializable>> prepareTests() {
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
