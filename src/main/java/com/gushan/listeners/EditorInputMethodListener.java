/*
 * Copyright (c) 2024 gushan (https://github.com/yourusername/just-code)
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
import com.gushan.settings.InputMethodSettings;
import com.gushan.statusbar.InputMethodStatusBarWidget;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.event.EditorMouseMotionListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.util.concurrency.EdtExecutorService;

import java.awt.im.InputContext;
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
    private final DefaultInputMethodRuleProvider ruleProvider;
    private volatile String lastInputMethod = "";
    private volatile long lastSwitchTime = 0;
    private static final long SWITCH_DELAY = 200; // 切换延迟，避免频繁切换

    public EditorInputMethodListener() {
        this.ruleProvider = new DefaultInputMethodRuleProvider();
    }

    @Override
    public void mouseMoved(EditorMouseEvent e) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSwitchTime < SWITCH_DELAY) {
            return;
        }

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                Editor editor = e.getEditor();
                Project project = editor.getProject();
                if (project == null) return;

                int offset = editor.logicalPositionToOffset(
                    editor.xyToLogicalPosition(e.getMouseEvent().getPoint())
                );

                String targetInputMethod = ruleProvider.getTargetInputMethod(editor, offset);
                if (!targetInputMethod.equals(lastInputMethod)) {
                    lastInputMethod = targetInputMethod;
                    lastSwitchTime = System.currentTimeMillis();
                    
                    EdtExecutorService.getInstance().execute(() -> {
                        switchInputMethod(targetInputMethod);
                        updateStatusBar(project, targetInputMethod);
                    });
                }
            } catch (Exception ex) {
                // Log error
            }
        });
    }

    private void switchInputMethod(String locale) {
        InputContext.getInstance().selectInputMethod(new Locale(locale));
    }

    private void updateStatusBar(Project project, String method) {
        if (project.isDisposed()) return;
        
        WindowManager.getInstance().getStatusBar(project)
            .getWidget(InputMethodStatusBarWidget.class)
            .updateInputMethod(method);
    }
} 