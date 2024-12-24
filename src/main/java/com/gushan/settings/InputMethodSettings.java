package com.gushan.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.application.ApplicationManager;

@State(
    name = "InputMethodSettings",
    storages = @Storage("just-code.xml")
)
@Service(Service.Level.APP)
public class InputMethodSettings implements PersistentStateComponent<InputMethodSettings> {
    private boolean autoSwitch = true;
    private String defaultCodeInputMethod = "en_US";
    private String defaultCommentInputMethod = "zh_CN";

    public static InputMethodSettings getInstance() {
        return ApplicationManager.getApplication().getService(InputMethodSettings.class);
    }

    @Override
    public @Nullable InputMethodSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull InputMethodSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public boolean isAutoSwitch() {
        return autoSwitch;
    }

    public void setAutoSwitch(boolean autoSwitch) {
        this.autoSwitch = autoSwitch;
    }

    public String getDefaultCodeInputMethod() {
        return defaultCodeInputMethod;
    }

    public void setDefaultCodeInputMethod(String defaultCodeInputMethod) {
        this.defaultCodeInputMethod = defaultCodeInputMethod;
    }

    public String getDefaultCommentInputMethod() {
        return defaultCommentInputMethod;
    }

    public void setDefaultCommentInputMethod(String defaultCommentInputMethod) {
        this.defaultCommentInputMethod = defaultCommentInputMethod;
    }
} 