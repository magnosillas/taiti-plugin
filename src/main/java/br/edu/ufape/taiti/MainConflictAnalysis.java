package br.edu.ufape.taiti;

import br.ufpe.cin.tan.analysis.task.TodoTask;
import br.ufpe.cin.tan.conflict.ConflictAnalyzer;
import br.ufpe.cin.tan.conflict.PlannedTask;
import br.ufpe.cin.tan.util.CsvUtil;

import java.io.Serializable;
import java.util.*;

public class MainConflictAnalysis {
    public static void main(String[] args) {
        /*********************** CONFIGURAÇÃO DA TAREFA 1 NO CÓDIGO. **************************************************/
        String url = "https://github.com/diaspora/diaspora";
        final Integer id1 = 1;

        /* conjunto de testes da tarefa, organizado em uma lista onde cada valor é um mapa (conjunto de dados
        identificado por chave e valor. Seria um mapa por arquivo de teste. */
        LinkedHashMap<String, Serializable> map = new LinkedHashMap<String, Serializable>(2);
        map.put("path", "features/desktop/help.feature");
        map.put("lines", new ArrayList<Integer>(Arrays.asList(4)));
        ArrayList<LinkedHashMap<String, Serializable>> tests = new ArrayList<LinkedHashMap<String, Serializable>>(Arrays.asList(map));
        /**************************************************************************************************************/


        /************************************** CALCULO DE TESTI PARA A TAREFA 1 **************************************/
        TodoTask task1;
        PlannedTask plannedTask1 = null;
        try {
            task1 = new TodoTask(url, id1, tests);
            plannedTask1 = task1.generateTaskForConflictAnalysis();

            Set<String> files = plannedTask1.getItest().findAllFiles();
            System.out.printf("TestI(%d): %d%n", id1, files.size());

            List<String[]> content = new ArrayList<>();
            content.add(new String[]{"Url", "ID", "TestI"});
            content.add(new String[]{url, String.valueOf(id1), files.toString()});
            CsvUtil.write("exemplo_resultado_testi.csv", content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**************************************************************************************************************/

        /************************************* CONFIGURAÇÃO DA TAREFA 2 NO CÓDIGO. ************************************/
        final Integer id2 = 2;
        //tests = [[path: "features/desktop/help.feature", lines: [4]]]
        LinkedHashMap<String, Serializable> map1 = new LinkedHashMap<String, Serializable>(2);
        map1.put("path", "features/desktop/mentions.feature");
        map1.put("lines", new ArrayList<Integer>(Arrays.asList(7)));
        tests = new ArrayList<LinkedHashMap<String, Serializable>>(Arrays.asList(map1));
        /**************************************************************************************************************/


        /******************************** CALCULO DE TESTI PARA A TAREFA 2 ********************************************/
        TodoTask task2;
        PlannedTask plannedTask2 = null;
        try {
            task2 = new TodoTask(url, id2, tests);
            plannedTask2 = task2.generateTaskForConflictAnalysis();

            Set<String> files = plannedTask2.getItest().findAllFiles();
            System.out.printf("TestI(%d): %d%n", id2, files.size());

            List<String[]> content = new ArrayList<>();
            content.add(new String[]{url, String.valueOf(id2), files.toString()});
            CsvUtil.append("exemplo_resultado_testi.csv", content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**************************************************************************************************************/

        /********* PREDICAO DE RISCO DE CONFLITO ENTRE UM PAR DE TAREFAS **********************************************/
        ConflictAnalyzer conflictAnalyzer = new ConflictAnalyzer();
        conflictAnalyzer.computeConflictRiskForPair(plannedTask1, plannedTask2);

        System.out.printf("Conflict prediction for tasks T(%d) and T(%d)%n", id1, id2);
        System.out.printf("Conflicting files (%d)%n", conflictAnalyzer.getConflictResult().getConflictingFiles().size());
        for(String file: conflictAnalyzer.getConflictResult().getConflictingFiles()){
            System.out.println(file);
        }
        System.out.printf("absolute conflict rate: %d%n", conflictAnalyzer.getConflictResult().getAbsoluteConflictRate());
        System.out.printf("relative conflict rate: %f%n", conflictAnalyzer.getConflictResult().getRelativeConflictRate());
        /**************************************************************************************************************/

        /*********************** CONFIGURAÇÃO DA TAREFA 3 NO CÓDIGO. **************************************************/
        final Integer id3 = 3;
        //tests = [[path: "features/desktop/help.feature", lines: [4]]]
        LinkedHashMap<String, Serializable> map2 = new LinkedHashMap<String, Serializable>(2);
        map2.put("path", "features/desktop/conversations.feature");
        map2.put("lines", new ArrayList<Integer>(Arrays.asList(12)));
        tests = new ArrayList<LinkedHashMap<String, Serializable>>(Arrays.asList(map2));
        /**************************************************************************************************************/


        /******************************** CALCULO DE TESTI PARA A TAREFA 3 ********************************************/
        TodoTask task3;
        PlannedTask plannedTask3 = null;
        try {
            task3 = new TodoTask(url, id3, tests);
            plannedTask3 = task3.generateTaskForConflictAnalysis();

            Set<String> files = plannedTask3.getItest().findAllFiles();
            System.out.printf("TestI(%d): %d%n", id3, files.size());

            List<String[]> content = new ArrayList<>();
            content.add(new String[]{url, String.valueOf(id3), files.toString()});
            CsvUtil.append("exemplo_resultado_testi.csv", content);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**************************************************************************************************************/


        /********* PREDICAO DE RISCO DE CONFLITO ENTRE CONJUNTO DE TAREFAS **********************************************/
        System.out.printf("Conflict prediction for planned task T(%d) and ongoing tasks T(%d) and T(%d)%n", id1, id2, id3);

        double conflictResult = conflictAnalyzer.sumAbsoluteConflictRiskForTasks(plannedTask1,
                new ArrayList<PlannedTask>(Arrays.asList(plannedTask2, plannedTask3)));
        System.out.printf("absolute sum rate of conflict risk = %f%n", conflictResult);

        conflictResult = conflictAnalyzer.meanAbsoluteConflictRiskForTasks(plannedTask1,
                new ArrayList<PlannedTask>(Arrays.asList(plannedTask2, plannedTask3)));
        System.out.printf("absolute mean rate of conflict risk = %f%n", conflictResult);

        conflictResult = conflictAnalyzer.sumRelativeConflictRiskForTasks(plannedTask1,
                new ArrayList<PlannedTask>(Arrays.asList(plannedTask2, plannedTask3)));
        System.out.printf("relative sum rate of conflict risk = %f%n", conflictResult);

        conflictResult = conflictAnalyzer.meanRelativeConflictRiskForTasks(plannedTask1,
                new ArrayList<PlannedTask>(Arrays.asList(plannedTask2, plannedTask3)));
        System.out.printf("relative mean rate of conflict risk = %f%n", conflictResult);

        /**************************************************************************************************************/
    }

}
