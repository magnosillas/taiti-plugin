package br.edu.ufape.taiti;

import br.ufpe.cin.tan.analysis.itask.ITest;
import br.ufpe.cin.tan.analysis.task.TodoTask;
import br.ufpe.cin.tan.util.CsvUtil;
import br.ufpe.cin.tan.util.Util;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class MainTodoTask {
    public static void main(String[] args) {



//
        /****************** CONFIGURACAO DE TAITI (NAO PRECISA FAZER, SO SE NÃO USAR OS VALORES DEFAULT)***************/
        String scenariosFolder = "features";
        String stepDefinitionsFolder = "features/step_definitions";
        String unityTestsFolder = "spec";
        Util.configureEnvironment(scenariosFolder, stepDefinitionsFolder, unityTestsFolder);
        /**************************************************************************************************************/


        /*********************** CONFIGURACAO DA TAREFA NO CODIGO. ELA PODERIA SER LIDA DE UM CSV. ********************/
        String url = "https://github.com/diaspora/diaspora.git";
        int id = 1; //identificador da tarefa, que seria o ID do PivotalTracker

        LinkedHashMap<String, Serializable> map = new LinkedHashMap<String, Serializable>(2);
        map.put("path", "features/desktop/help.feature");
        map.put("lines", new ArrayList<Integer>(Arrays.asList(4)));

        /* conjunto de testes da tarefa, organizado em uma lista onde cada valor é um mapa (conjunto de dados
        identificado por chave e valor. Seria um mapa por arquivo de teste. */
        ArrayList<LinkedHashMap<String, Serializable>> tests = new ArrayList<LinkedHashMap<String, Serializable>>(Arrays.asList(map));
        /**************************************************************************************************************/


        /************ CALCULO DE TESTI (O CONJUNTO DE ARQUIVOS QUE TAITI PREVE QUE SERA ALTERADO PELA TAREFA) *********/
        TodoTask task; //o objeto que representa a tarefa
        ITest itest; //interface da tarefa. costumo chamar de TestI, o código ainda usa o vocabulário antigo
        try {
            task = new TodoTask(url, id, tests);

            itest = task.computeTestBasedInterface();

            /* Exibindo o conjunto de arquivos no console */
            Set<String> files = itest.findAllFiles();
            System.out.printf("TestI(%d): %d%n", id, files.size());
            for (String file : files) {
                System.out.println(file);
            }

            /* Salvando o conjunto de arquivos em um arquivo csv */
            List<String[]> content = new ArrayList<>();
            content.add(new String[]{"Url", "ID", "TestI"});
            content.add(new String[]{url, String.valueOf(id), files.toString()});
            CsvUtil.write("exemplo_resultado_testi.csv", content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**************************************************************************************************************/
    }

}
