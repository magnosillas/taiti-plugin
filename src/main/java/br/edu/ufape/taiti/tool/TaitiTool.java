package br.edu.ufape.taiti.tool;

import br.ufpe.cin.tan.analysis.task.TodoTask;
import br.ufpe.cin.tan.analysis.taskInterface.TestI;
import br.ufpe.cin.tan.util.CsvUtil;

import java.io.Serializable;
import java.util.*;

public class TaitiTool {

    private String githubURL;
    private int taskID;
    private ArrayList<ScenarioTestInformation> scenarios;

    public TaitiTool(String githubURL, int taskID, ArrayList<ScenarioTestInformation> scenarios) {
        this.githubURL = githubURL;
        this.taskID = taskID;
        this.scenarios = scenarios;
    }

    public void run() {
        ArrayList<LinkedHashMap<String, Serializable>> tests = prepareTests();

//        for (LinkedHashMap<String, Serializable> map : tests) {
//            System.out.println("path = " + map.get("path"));
//            System.out.println("lines = " + map.get("lines"));
//        }

        TodoTask task;
        TestI itest;
        try {
            task = new TodoTask(githubURL, taskID, tests);
            itest = task.computeTestBasedInterface();

            /* Exibindo o conjunto de arquivos no console */
            Set<String> files = itest.findAllFiles();
            System.out.printf("TestI(%d): %d%n", taskID, files.size());
            for (String file : files) {
                System.out.println(file);
            }

            /* Salvando o conjunto de arquivos em um arquivo csv */
            List<String[]> content = new ArrayList<>();
            content.add(new String[]{"Url", "ID", "TestI"});
            content.add(new String[]{githubURL, String.valueOf(taskID), files.toString()});
            CsvUtil.write("exemplo_resultado_testi.csv", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: melhorar esse algoritmo
    private ArrayList<LinkedHashMap<String, Serializable>> prepareTests() {
        ArrayList<LinkedHashMap<String, Serializable>> tests = new ArrayList<>();

        String path = scenarios.get(0).getFilePath();
        ArrayList<Integer> lines = new ArrayList<>();

        for (ScenarioTestInformation s : scenarios) {
            if (!s.getFilePath().equals(path)) {
                LinkedHashMap<String, Serializable> map = new LinkedHashMap<>(2);

                map.put("path", getRelativePath(path));
                map.put("lines", lines);
                tests.add(map);

                path = s.getFilePath();
                lines = new ArrayList<>();
            }
            lines.add(s.getLineNumber());
        }

        LinkedHashMap<String, Serializable> map = new LinkedHashMap<>(2);
        map.put("path", getRelativePath(path));
        map.put("lines", lines);
        tests.add(map);

        return tests;
    }

    private String getRelativePath(String absolutePath) {
        return absolutePath.substring(absolutePath.indexOf("features"));
    }
}
