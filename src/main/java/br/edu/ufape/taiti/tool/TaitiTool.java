package br.edu.ufape.taiti.tool;

import br.ufpe.cin.tan.analysis.task.TodoTask;
import br.ufpe.cin.tan.analysis.taskInterface.TestI;
import br.ufpe.cin.tan.util.CsvUtil;

import java.io.Serializable;
import java.util.*;

public class TaitiTool {

    private String githubURL;
    private int taskID;

    public TaitiTool(String githubURL, int taskID) {
        this.githubURL = githubURL;
        this.taskID = taskID;
    }

    public void run() {
//        /* Configurando uma tarefa diretamente no código. Uma alternativa seria ler de um arquivo csv.*/
//        String url = "https://github.com/diaspora/diaspora";
//
//        int id = 1;//identificador da tarefa, pode ser definido livremente

        LinkedHashMap<String, Serializable> map = new LinkedHashMap<>(2);
        map.put("path", "features/desktop/help.feature");
        map.put("lines", new ArrayList<Integer>(Arrays.asList(4)));
        ArrayList<LinkedHashMap<String, Serializable>> tests = new ArrayList<>(Arrays.asList(map));

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
}
