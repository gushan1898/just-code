/*
 * Copyright (c) 2024 gushan (https://github.com/gushan1898/just-code)
 */

package com.gushan.rules;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * 输入法切换规则接口
 * 定义了判断是否需要切换输入法的规则
 *
 * @author gushan
 * @since 1.0.0
 */
public interface InputMethodRule {
    /**
     * 判断在指定位置是否需要切换输入法
     *
     * @param editor 当前编辑器
     * @param offset 光标位置
     * @return true 如果需要切换输入法
     */
    boolean shouldSwitchInputMethod(@NotNull Editor editor, int offset);

    /**
     * 获取目标输入法
     *
     * @return 输入法的locale字符串
     */
    String getTargetInputMethod();
} 