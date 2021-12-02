package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.exceptions.HttpException;
import br.edu.ufape.taiti.gui.configuretask.TaskConfigurePanel;
import br.edu.ufape.taiti.gui.configuretask.table.TableDialog;
import br.edu.ufape.taiti.gui.configuretask.table.TablePanel;
import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.settings.TaitiSettingsState;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogEarthquakeShaker;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.util.registry.Registry;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jsoup.internal.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TaitiDialog extends DialogWrapper {

    private final MainPanel mainPanel;
    private TaskConfigurePanel taskConfigurePanel;
    private TablePanel tablePanelDialog;

    private PivotalTracker pivotalTracker;
    private TaitiTool taiti;

    private final Project project;

    public TaitiDialog(Project project) {
        super(true);

        this.mainPanel = new MainPanel(project);
        this.taskConfigurePanel = this.mainPanel.getTaskConfigurePanel();
        this.tablePanelDialog = this.mainPanel.getTablePanelDialog();

        this.project = project;

        prepareServices();

        setTitle("TAITIr - Test Analyzer for Inferring Task Interface and Conflict Risk");
        setSize(1300,810);
        init();
    }

    private void prepareServices() {
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.retrieveStoredCredentials(project);

        taiti = new TaitiTool(project);
        pivotalTracker = new PivotalTracker(settings.getToken(), settings.getPivotalURL(), project);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel.getRootPanel();
    }

    @Override
    protected @Nullable ValidationInfo doValidate() {
        ValidationInfo validationInfo;

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

    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{getOKAction(), new SaveAction(), getCancelAction()};
    }

    //TODO: o que fazer na ação de OK?
    @Override
    protected void doOKAction() {
        super.doOKAction();

        String taskID = taskConfigurePanel.getTextTaskID().getText().replace("#", "");

        try {
            File file = taiti.createScenariosFile(taskConfigurePanel.getScenarios());
            pivotalTracker.saveScenarios(file, taskID);
            taiti.deleteScenariosFile();
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo!");
        } catch (HttpException e) {
            System.out.println(e.getStatusText() + " - " + e.getStatusNumber());
            taiti.deleteScenariosFile();
        }
    }


    protected class SaveAction extends DialogWrapperAction {
        protected SaveAction() {
            super("Save");
            putValue(Action.NAME, "Save");
        }

        private boolean isInplaceValidationToolTipEnabled() {
            return Registry.is("ide.inplace.validation.tooltip", true);
        }

        //TODO: qual a melhor maneira de colocar os botões?
        @Override
        protected void doAction(ActionEvent e) {
            /* Esse trecho de código onde valida os dados está presente na classe DialogWrapperAction (classe Pai da SaveAction),
            nas linhas 1899 até 1916.*/
            ValidationInfo vi = doValidate();
            if (vi != null) {
                if (vi.component != null && vi.component.isVisible()) {
                    IdeFocusManager.getInstance(null).requestFocus(vi.component, true);
                }

                if (!isInplaceValidationToolTipEnabled()) {
                    DialogEarthquakeShaker.shake(getPeer().getWindow());
                }
                List<ValidationInfo> infoList = Collections.singletonList(vi);
                updateErrorInfo(infoList);
                startTrackingValidation();

                if (ContainerUtil.exists(infoList, info1 -> !info1.okEnabled)) return;
            }
            String taskID = taskConfigurePanel.getTextTaskID().getText().replace("#", "");
            TableDialog tableDialog = new TableDialog(tablePanelDialog, taiti, pivotalTracker, taskConfigurePanel.getScenarios(), taskID);

            if (tableDialog.showAndGet()) {
                mainPanel.updateContent();
                taskConfigurePanel = mainPanel.getTaskConfigurePanel();
                tablePanelDialog = mainPanel.getTablePanelDialog();
            }
        }
    }
}
