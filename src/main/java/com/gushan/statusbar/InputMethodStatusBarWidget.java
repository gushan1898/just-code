/*
 * Copyright (c) 2024 gushan (https://github.com/gushan1898/just-code)
 */

package com.gushan.statusbar;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
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
    public static final String WIDGET_ID = "JustCode.InputMethod";
    private String currentInputMethod = "EN";

    public InputMethodStatusBarWidget(@NotNull Project project) {
        super(project);
    }

    @Override
    public @NotNull String ID() {
        return WIDGET_ID;
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
        return e -> {
            // 处理点击事件
        };
    }

    public void updateInputMethod(String method) {
        this.currentInputMethod = method;
        if (myStatusBar != null) {
            myStatusBar.updateWidget(ID());
        }
    }
} 