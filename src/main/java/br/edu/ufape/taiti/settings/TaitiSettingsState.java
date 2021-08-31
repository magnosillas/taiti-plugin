package br.edu.ufape.taiti.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "br.edu.ufape.taiti.settings.TaitiSettingsState",
        storages = {@Storage("TaitiSettingsPlugin.xml")}
)
public class TaitiSettingsState  implements PersistentStateComponent<TaitiSettingsState> {
    public String githubURL = "";
    public String pivotalURL = "";
    public String token = "";

    public static TaitiSettingsState getInstance(Project project) {
        return project.getService(TaitiSettingsState.class);
    }

    @Nullable
    @Override
    public TaitiSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull TaitiSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
