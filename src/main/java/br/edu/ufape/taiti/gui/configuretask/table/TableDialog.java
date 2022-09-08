package br.edu.ufape.taiti.gui.configuretask.table;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.gui.configuretask.TaskConfigurePanel;
import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.tool.ScenarioTestInformation;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;
import org.jsoup.internal.StringUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TableDialog extends DialogWrapper {

    private final TablePanel tablePanel;
    private final TaskConfigurePanel taskConfigurePanel;
    private final TaitiTool taiti;
    private final PivotalTracker pivotalTracker;
    private final ArrayList<ScenarioTestInformation> scenarios;
    private final String taskID;

    /**
     * Classe responsável por mostrar a janela da tabela onde é mostrado todos os scenarios selecionados.
     */
    public TableDialog(TaskConfigurePanel taskConfigurePanel, TablePanel tablePanel, TaitiTool taiti, PivotalTracker pivotalTracker, ArrayList<ScenarioTestInformation> scenarios, String taskID) {
        super(true);

        this.tablePanel = tablePanel;
        this.taskConfigurePanel = taskConfigurePanel;
        this.taiti = taiti;
        this.pivotalTracker = pivotalTracker;
        this.scenarios = scenarios;
        this.taskID = taskID;

        setTitle("Review");
        setSize(400,400);
        init();
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return tablePanel.getPanel();
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        ValidationInfo validationInfo;

        if (tablePanel.getTable().getRowCount() == 0) {
            validationInfo = new ValidationInfo("Select at least one scenario.", tablePanel.getTable());
            return validationInfo;
        }

        if (StringUtil.isBlank(taskConfigurePanel.getTextTaskID().getText())) {
            validationInfo = new ValidationInfo("The Task ID can not be empty.", taskConfigurePanel.getTextTaskID());
            return validationInfo;
        }

        String regex = "#?\\d+$";
        if (!taskConfigurePanel.getTextTaskID().getText().matches(regex)) {
            validationInfo = new ValidationInfo("The task ID is wrong.", taskConfigurePanel.getTextTaskID());
            return validationInfo;
        }

        return null;
    }

    /**
     * Esse método é chamado quando o usuário clica em OK na tabela dos scenarios selecionados.
     */
    @Override
    protected void doOKAction() {
        super.doOKAction();

        try {
            File file = taiti.createScenariosFile(scenarios);
            pivotalTracker.saveScenarios(file, taskID);
            taiti.deleteScenariosFile();
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo!");
        } catch (HttpException e) {
            System.out.println(e.getStatusText() + " - " + e.getStatusNumber());
            taiti.deleteScenariosFile();
        }
    }
}
