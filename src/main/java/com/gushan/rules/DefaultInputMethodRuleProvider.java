/*
 * Copyright (c) 2024 gushan (https://github.com/gushan1898/just-code)
 */

package com.gushan.rules;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.ExtensionPointName;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 默认输入法规则提供者
 * 管理和应用所有输入法切换规则
 *
 * @author gushan
 * @since 1.0.0
 */
@Service(Service.Level.PROJECT)
public final class DefaultInputMethodRuleProvider {
    private static final Logger LOG = Logger.getInstance(DefaultInputMethodRuleProvider.class);
    public static final ExtensionPointName<InputMethodRule> EP_NAME = 
        ExtensionPointName.create("com.gushan.just-code.inputMethodRule");

    /** 规则列表，按优先级排序 */
    private final List<InputMethodRule> rules = new ArrayList<>();

    /**
     * 构造函数，初始化默认规则
     */
    public DefaultInputMethodRuleProvider() {
        // 从扩展点加载规则
        EP_NAME.getExtensionList().forEach(rules::add);
        LOG.info("DefaultInputMethodRuleProvider initialized with " + rules.size() + " rules");
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

    /**
     * 获取所有规则
     *
     * @return 规则列表
     */
    public List<InputMethodRule> getRules() {
        return Collections.unmodifiableList(rules);
    }
} 