/*
 * Copyright (c) 2024 gushan (https://github.com/gushan1898/just-code)
 */

package com.gushan.rules;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认输入法规则提供者
 * 管理和应用所有输入法切换规则
 *
 * @author gushan
 * @since 1.0.0
 */
public class DefaultInputMethodRuleProvider {
    /** 规则列表，按优先级排序 */
    private final List<InputMethodRule> rules = new ArrayList<>();

    /**
     * 构造函数，初始化默认规则
     */
    public DefaultInputMethodRuleProvider() {
        // 添加默认规则，按优先级顺序添加
        rules.add(new StringLiteralRule());
        rules.add(new CommentRule());
        rules.add(new XmlAttributeRule());
        rules.add(new MarkdownRule());
    }

    /**
     * 获取指定位置应该使用的输入法
     *
     * @param editor 当前编辑器
     * @param offset 光标位置
     * @return 目标输入法的locale字符串
     */
    public String getTargetInputMethod(@NotNull Editor editor, int offset) {
        for (InputMethodRule rule : rules) {
            if (rule.shouldSwitchInputMethod(editor, offset)) {
                return rule.getTargetInputMethod();
            }
        }
        return "en_US"; // 默认返回英文
    }

    /**
     * 添加新的规则
     *
     * @param rule 要添加的规则
     */
    public void addRule(InputMethodRule rule) {
        rules.add(rule);
    }
} 