package com.gushan.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Copyright (c) 2024 gushan (https://github.com/gushan1898/just-code)
 */
@State(
    name = "InputMethodSettings",
    storages = {@Storage("InputMethodSwitcher.xml")}
)
public class InputMethodSettings implements PersistentStateComponent<InputMethodSettings> {
    public boolean enableAutoSwitch = true;
    public boolean switchInStrings = true;
    public boolean switchInComments = true;
    public String defaultInputMethod = "";
    public String codeInputMethod = "en_US";

    @Override
    public @Nullable InputMethodSettings getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull InputMethodSettings state) {
        XmlSerializerUtil.copyBean(state, this);
    }
} 