/*
 * Copyright (c) 2024 gushan
 */

package com.gushan.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目级输入法配置
 * 管理项目特定的输入法切换规则和配置
 *
 * @author gushan
 * @since 1.0.0
 */
@State(
    name = "ProjectInputMethodSettings",
    storages = @Storage("justcode.xml")
)
public class ProjectInputMethodSettings implements PersistentStateComponent<ProjectInputMethodSettings.State> {
    private State myState = new State();

    public static ProjectInputMethodSettings getInstance(@NotNull Project project) {
        return project.getService(ProjectInputMethodSettings.class);
    }

    public static class State {
        public boolean useProjectSettings = false;
        public Map<String, String> fileTypeInputMethods = new HashMap<>();
        public Map<String, String> customRules = new HashMap<>();
    }

    @Override
    public @Nullable State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }
} 