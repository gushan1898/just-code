/*
 * Copyright (c) 2024 gushan (https://github.com/gushan1898/just-code)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gushan.listeners;

import com.gushan.rules.DefaultInputMethodRuleProvider;
import com.gushan.rules.InputMethodRule;
import com.gushan.settings.InputMethodSettings;
import com.gushan.statusbar.InputMethodStatusBarWidget;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.StatusBar;
import org.jetbrains.annotations.NotNull;

import java.awt.im.InputContext;
import java.util.Locale;

/**
 * 输入法切换监听器
 * 负责监听编辑器鼠标移动事件，并根据上下文自动切换输入法
 *
 * @author gushan
 * @since 1.0.0
 */
public class EditorInputMethodListener implements CaretListener, EditorMouseMotionListener {
    private static final Logger LOG = Logger.getInstance(EditorInputMethodListener.class);
    private final DefaultInputMethodRuleProvider ruleProvider;
    private final InputMethodSettings settings;
    private final Project project;
    private final WindowManager windowManager;
    private String lastInputMethod = null;
    
    public EditorInputMethodListener(@NotNull Project project) {
        this.project = project;
        this.ruleProvider = project.getService(DefaultInputMethodRuleProvider.class);
        this.settings = InputMethodSettings.getInstance();
        this.windowManager = WindowManager.getInstance();
        LOG.info("EditorInputMethodListener initialized");
    }

    @Override
    public void caretPositionChanged(@NotNull CaretEvent e) {
        checkAndSwitchInputMethod(e.getEditor(), e.getCaret().getOffset());
    }

    @Override 
    public void mouseMoved(@NotNull EditorMouseEvent e) {
        if (!settings.isAutoSwitch() || project.isDisposed()) {
            return;
        }
        checkAndSwitchInputMethod(e.getEditor(), e.getEditor().getCaretModel().getOffset());
    }

    private void checkAndSwitchInputMethod(Editor editor, int offset) {
        if (!settings.isAutoSwitch()) {
            return;
        }

        InputContext context = InputContext.getInstance();
        if (context == null) {
            LOG.warn("Input context is null");
            return;
        }

        String currentLocale = context.getLocale().toString();
        String targetMethod = ruleProvider.getTargetInputMethod(editor, offset);
        
        // 避免重复切换
        if (!currentLocale.equals(targetMethod) && !targetMethod.equals(lastInputMethod)) {
            LOG.info("Switching input method from " + currentLocale + " to " + targetMethod);
            context.selectInputMethod(new Locale(targetMethod));
            updateStatusBar(targetMethod);
            lastInputMethod = targetMethod;
        }
    }

    private void updateStatusBar(String method) {
        if (windowManager == null || project.isDisposed()) {
            LOG.warn("Window manager is null or project disposed");
            return;
        }

        StatusBar statusBar = windowManager.getStatusBar(project);
        if (statusBar == null) {
            LOG.warn("Status bar is null");
            return;
        }

        InputMethodStatusBarWidget widget = (InputMethodStatusBarWidget) 
            statusBar.getWidget(InputMethodStatusBarWidget.WIDGET_ID);
        
        if (widget != null) {
            String displayText = method.equals(settings.getDefaultCodeInputMethod()) ? "EN" : "CN";
            LOG.debug("Updating status bar: " + displayText);
            widget.updateInputMethod(displayText);
        } else {
            LOG.warn("Status bar widget not found");
        }
    }
} 