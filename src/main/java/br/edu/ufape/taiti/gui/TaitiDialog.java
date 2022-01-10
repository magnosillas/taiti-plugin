package br.edu.ufape.taiti.gui;

import br.edu.ufape.taiti.service.PivotalTracker;
import br.edu.ufape.taiti.settings.TaitiSettingsState;
import br.edu.ufape.taiti.tool.TaitiTool;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Esta classe é responsável por mostrar a tela principal da aplicação. Ela possui um objeto MainPanel,
 * responsável por toda a interface gráfica da aplicação, possui um objeto PivotalTracker, responsável por toda a
 * comunicação com o Pivotal Tracker e possui um objeto TaitiTool, responsável por rodar a ferramenta TAITI.
 */
public class TaitiDialog extends DialogWrapper {

    private final MainPanel mainPanel;

    private PivotalTracker pivotalTracker;
    private TaitiTool taiti;

    private final Project project;

    public TaitiDialog(Project project) {
        super(true);

        this.project = project;
        prepareServices();

        this.mainPanel = new MainPanel(project, taiti, pivotalTracker);

        setTitle("TAITIr - Test Analyzer for Inferring Task Interface and Conflict Risk");
        setSize(1100,630);
        init();
    }

    /**
     * Neste método é inicializado os objetos responsáveis pelo Pivotal Tracker e por rodar TAITI,
     * para isso é necessário obter o token e o link do Pivotal Tracker onde o usuário adicionou nas configurações,
     * assim, é criado um objeto que guarda os estados das configurações do plugin.
     */
    private void prepareServices() {
        TaitiSettingsState settings = TaitiSettingsState.getInstance(project);
        settings.retrieveStoredCredentials(project);

        taiti = new TaitiTool(project);
        pivotalTracker = new PivotalTracker(settings.getToken(), settings.getPivotalURL(), project);
    }

    /**
     * Este método deve retornar um Componente responsável por mostrar toda a parte gráfica do plugin,
     * por isso, é retornado o painel raiz do MainPanel.
     */
    @Override
    protected @Nullable JComponent createCenterPanel() {
        return mainPanel.getRootPanel();
    }

    /**
     * Este método é responsável por mostrar os botões na janela da aplicação, mas como os nomes e ações dos botões mudam
     * de uma tela para outra, é retornado um array vazio aqui para que não mostre nenhum botão na tela e esse botões
     * serão adicionado dinamicamente nas classes responsáveis pela interface gráfica.
     */
    @Override
    protected Action @NotNull [] createActions() {
        return new Action[]{};
    }
}
