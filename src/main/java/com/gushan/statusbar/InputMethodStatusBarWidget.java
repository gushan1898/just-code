/*
 * Copyright (c) 2024 gushan
 */

package com.gushan.statusbar;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;

/**
 * 输入法状态栏组件
 * 显示当前输入法状态，支持点击切换
 *
 * @author gushan
 * @since 1.0.0
 */
public class InputMethodStatusBarWidget extends EditorBasedWidget implements StatusBarWidget.TextPresentation {
    private String currentInputMethod = "EN";

    public InputMethodStatusBarWidget(@NotNull Project project) {
        super(project);
    }

    @Override
    public @NotNull String ID() {
        return "JustCode.InputMethod";
    }

    @Override
    public @Nullable WidgetPresentation getPresentation() {
        return this;
    }

    @Override
    public @NotNull String getText() {
        return "IM: " + currentInputMethod;
    }

    @Override
    public float getAlignment() {
        return 0;
    }

    @Override
    public @Nullable String getTooltipText() {
        return "Current Input Method";
    }

    @Override
    public @Nullable Consumer<MouseEvent> getClickConsumer() {
        return null;
    }

    public void updateInputMethod(String method) {
        this.currentInputMethod = method;
        if (myStatusBar != null) {
            myStatusBar.updateWidget(ID());
        }
    }
} 