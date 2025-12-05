package com.yupi.yuoj.utils;

import com.alibaba.excel.util.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 格式校验类（无日志版本）
 *

 */
@Component
public class FormatCodeUtils {

    /**
     * 格式化代码，特别处理Java代码
     * （修改为public：Spring组件需对外提供访问接口，原private无法被外部调用）
     *
     * @param code     原始代码
     * @param language 编程语言
     * @return 格式化后的代码
     */
    // 移除 google-java-format 依赖，使用纯 Java 实现
    public String formatCode(String code, String language) {
        if ("java".equalsIgnoreCase(language)) {
            return simpleFormatCode(code, "java");
        }
        return simpleFormatCode(code, language);
    }

    /**
     * 简单格式化非Java代码
     */
    private String simpleFormatCode(String code, String language) {
        if (StringUtils.isBlank(code) || code.contains("\n")) {
            // 如果代码已经有换行，不做过多处理
            return code;
        }

        // 根据语言类型进行基本格式化
        switch (language.toLowerCase()) {
            case "python":
                return formatPythonCode(code);
            case "javascript":
            case "js":
                return formatJavaScriptCode(code);
            case "c++":
            case "cpp":
                return formatCppCode(code);
            case "c":
                return formatCCode(code);
            default:
                return formatGenericCode(code);
        }
    }

    /**
     * 通用代码格式化（针对不支持的语言）
     */
    private String formatGenericCode(String code) {
        if (StringUtils.isBlank(code) || code.contains("\n")) {
            return code;
        }

        // 基本的代码格式化规则（保持原逻辑不变）
        return code
                .replace("{", "{\n    ")
                .replace("}", "\n}")
                .replace(";", ";\n    ")
                .replace("    }", "}") // 修复格式化后多余空格的问题
                .replace("if(", "if (")
                .replace("for(", "for (")
                .replace("while(", "while (")
                .replaceAll("\\n\\s*\\n", "\n") // 移除空行
                .trim();
    }

    /**
     * Python代码简单格式化（补充基础实现）
     * 处理缩进、换行（Python对缩进敏感，仅做基础换行处理）
     */
    private String formatPythonCode(String code) {
        if (StringUtils.isBlank(code) || code.contains("\n")) {
            return code;
        }

        // Python基础格式化：冒号后换行+4空格缩进，if/for/while后加空格
        return code
                .replace(":", ":\n    ")
                .replace("if(", "if (")
                .replace("for(", "for (")
                .replace("while(", "while (")
                .replace("def ", "def ") // 保持函数定义格式
                .replace("class ", "class ") // 保持类定义格式
                .replaceAll("\\n\\s*\\n", "\n")
                .trim();
    }

    /**
     * JavaScript代码简单格式化（补充基础实现）
     */
    private String formatJavaScriptCode(String code) {
        if (StringUtils.isBlank(code) || code.contains("\n")) {
            return code;
        }

        // 复用通用格式化规则，额外处理JS特有语法
        String formatted = formatGenericCode(code);
        // 处理箭头函数、对象字面量等
        return formatted
                .replace("=>", " => ")
                .replace(":", ": ")
                .replaceAll("\\n\\s*\\n", "\n")
                .trim();
    }

    /**
     * C++代码简单格式化（补充基础实现）
     */
    private String formatCppCode(String code) {
        if (StringUtils.isBlank(code) || code.contains("\n")) {
            return code;
        }

        // 复用通用格式化规则，额外处理C++特有语法
        String formatted = formatGenericCode(code);
        // 处理模板、引用等
        return formatted
                .replace("<", " < ")
                .replace(">", " > ")
                .replace("&", " & ")
                .replace("*", " * ")
                .replaceAll("\\s{2,}", " ") // 移除多余空格
                .replaceAll("\\n\\s*\\n", "\n")
                .trim();
    }

    /**
     * C代码简单格式化（复用C++逻辑，保持一致性）
     */
    private String formatCCode(String code) {
        // C和C++格式化规则相近，直接复用
        return formatCppCode(code);
    }
}