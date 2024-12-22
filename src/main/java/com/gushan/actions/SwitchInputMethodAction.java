/*
 * Copyright (c) 2024 gushan (https://github.com/gushan1898/just-code)
 */

package com.gushan.actions;

import com.gushan.settings.InputMethodSettings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;

import java.awt.im.InputContext;
import java.util.Locale;

/**
 * 输入法切换动作
 * 提供手动切换输入法的快捷键支持
 * 默认快捷键：
 * - Windows/Linux: Ctrl + Alt + I
 * - macOS: Cmd + Alt + I
 *
 * @author gushan
 * @since 1.0.0
 */
public class SwitchInputMethodAction extends AnAction {
    /** 当前是否使用默认输入法 */
    private boolean isDefaultInputMethod = false;

    /**
     * 执行输入法切换动作
     * 在代码输入法和系统默认输入法之间切换
     *
     * @param e 动作事件
     */
    @Override
    public void actionPerformed(AnActionEvent e) {
        InputMethodSettings settings = ServiceManager.getService(InputMethodSettings.class);
        InputContext context = InputContext.getInstance();
        
        isDefaultInputMethod = !isDefaultInputMethod;
        if (isDefaultInputMethod) {
            // 切换到系统默认输入法
            context.selectInputMethod(new Locale(settings.defaultInputMethod));
        } else {
            // 切换到代码输入法（英文）
            context.selectInputMethod(new Locale(settings.codeInputMethod));
        }
    }

    /**
     * 更新动作状态
     * 根据当前上下文决定是否启用该动作
     *
     * @param e 动作事件
     */
    @Override
    public void update(AnActionEvent e) {
        // 始终保持启用状态
        e.getPresentation().setEnabled(true);
        // 根据当前输入法状态更新显示文本
        e.getPresentation().setText(isDefaultInputMethod ? 
            "Switch to Code Input" : "Switch to Default Input");
    }
} 