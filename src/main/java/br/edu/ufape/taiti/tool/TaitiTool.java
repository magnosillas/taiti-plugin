package br.edu.ufape.taiti.tool;

import br.ufpe.cin.tan.analysis.itask.ITest;
import br.ufpe.cin.tan.analysis.task.TodoTask;
import br.ufpe.cin.tan.util.CsvUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

/**
 * Esta classe implementa tudo relacionado a ferramenta TAITI.
 */
public class TaitiTool {

    private final Project project;

    private static final String FILE_NAME = "scenarios.csv";

    public TaitiTool(Project project) {
        this.project = project;
    }

    // Esse método ainda possui erros ao executar TAITI.
    public void createTestI(@NotNull ArrayList<File> scenarioFiles) {
        //Configurando as dependências
        String language = "ruby";
        String gemsPath = "C:"+ File.separator+"Ruby30-x64"+File.separator+"lib"+File.separator+"ruby"+
                File.separator+"gems" + File.separator+"3.0.0"+File.separator+"gems";
        String frameworkPath = "C:"+File.separator+"jruby-9.2.19.0";

        for (File f : scenarioFiles) {
            System.out.println("TaskID: " + f.getName().replaceAll("[\\D]", ""));
        }

        for (File f : scenarioFiles) {
            ArrayList<LinkedHashMap<String, Serializable>> tests = prepareScenariosFromFile(readTaitiFile(f));

            TodoTask task;
            ITest itest;
            try {
                // o nome do arquivo é no estilo file-123456.csv, onde o 123456 é o id da tarefa
                int taskID = Integer.parseInt(f.getName().replaceAll("[\\D]", ""));
                String projectPath = getProjectPath();

                task = new TodoTask(language, gemsPath, frameworkPath, projectPath, taskID, tests);
                itest = task.computeTestBasedInterface();

                /* Exibindo o conjunto de arquivos no console */
                Set<String> files = itest.findAllFiles();
                System.out.printf("TestI(%d): %d%n", taskID, files.size());
                for (String file : files) {
                    System.out.println(file);
                }

                /* Salvando o conjunto de arquivos num arquivo csv */
                List<String[]> content = new ArrayList<>();
                content.add(new String[]{"Path", "ID", "TestI"});
                content.add(new String[]{projectPath, String.valueOf(taskID), files.toString()});
                CsvUtil.write("exemplo_resultado_testi_" + taskID + ".csv", content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        deleteTaitiDirectory();
    }

    public File createScenariosFile(ArrayList<ScenarioTestInformation> scenarios) throws IOException {
        String[] header = {"path", "lines"};
        ArrayList<String[]> rows = new ArrayList<>();

        ArrayList<LinkedHashMap<String, Serializable>> scenariosPrepared = prepareScenariosFromUI(scenarios);

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

    private void deleteTaitiDirectory() {
        File tempTaitiDirectory = new File(getProjectPath() + File.separator + "temp_taiti");
        if (tempTaitiDirectory.exists() && tempTaitiDirectory.isDirectory()) {
            for (File f : Objects.requireNonNull(tempTaitiDirectory.listFiles())) {
                f.delete();
            }
            tempTaitiDirectory.delete();
        }
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

    public ArrayList<String[]> readTaitiFile(File file) {
        BufferedReader br;
        String csvDivisor = ";";
        ArrayList<String[]> contentFile = new ArrayList<>();

        try {
            br = new BufferedReader(new FileReader(file.getPath()));
            String l = br.readLine();
            // Read the file line by line
            while (l != null) {
                // Split each line into an array of fields
                String[] line = l.split(csvDivisor);
                // Add the array of fields to the list of records
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

    private ArrayList<LinkedHashMap<String, Serializable>> prepareScenariosFromUI(ArrayList<ScenarioTestInformation> scenarios) {
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

    private String getProjectPath() {
        String projectPath = "";

        VirtualFile projectDir = ProjectUtil.guessProjectDir(project);
        if (projectDir != null) {
            projectPath = projectDir.getPath();
        }

        return projectPath;
    }
}
