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

import com.gushan.rules.InputMethodRule;
import com.gushan.settings.InputMethodSettings;
import com.gushan.statusbar.InputMethodStatusBarWidget;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.concurrency.EdtExecutorService;
import org.jetbrains.annotations.NotNull;

import java.awt.im.InputContext;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * 输入法切换监听器
 * 负责监听编辑器鼠标移动事件，并根据上下文自动切换输入法
 *
 * @author gushan
 * @since 1.0.0
 */
public class EditorInputMethodListener implements EditorMouseMotionListener {
    private final List<InputMethodRule> rules;
    private final InputMethodSettings settings;
    private final Project project;
    private long lastCheckTime = 0;
    private static final long CHECK_INTERVAL = 100; // 检查间隔，单位：毫秒

    public EditorInputMethodListener(Project project, List<InputMethodRule> rules, InputMethodSettings settings) {
        this.project = project;
        this.rules = rules;
        this.settings = settings;
    }

    @Override
    public void mouseMoved(@NotNull EditorMouseEvent e) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckTime < CHECK_INTERVAL) {
            return;
        }
        lastCheckTime = currentTime;

        Editor editor = e.getEditor();
        int offset = editor.getCaretModel().getOffset();

        // 延迟执行，避免频繁切换
        EdtExecutorService.getScheduledExecutorInstance().schedule(() -> {
            checkAndSwitchInputMethod(editor, offset);
        }, 200, TimeUnit.MILLISECONDS);
    }

    private void checkAndSwitchInputMethod(Editor editor, int offset) {
        InputContext context = InputContext.getInstance();
        String currentLocale = context.getLocale().toString();

        for (InputMethodRule rule : rules) {
            if (rule.shouldSwitchInputMethod(editor, offset)) {
                String targetMethod = rule.getTargetInputMethod();
                if (!currentLocale.equals(targetMethod)) {
                    context.selectInputMethod(new Locale(targetMethod));
                    updateStatusBar(targetMethod);
                }
                break;
            }
        }
    }

    private void updateStatusBar(String method) {
        if (project.isDisposed()) {
            return;
        }

        WindowManager windowManager = WindowManager.getInstance();
        if (windowManager == null) {
            return;
        }

        InputMethodStatusBarWidget widget = (InputMethodStatusBarWidget) 
            windowManager.getStatusBar(project).getWidget(InputMethodStatusBarWidget.WIDGET_ID);
        
        if (widget != null) {
            widget.updateInputMethod(method.equals(settings.codeInputMethod) ? "EN" : "CN");
        }
    }
} 