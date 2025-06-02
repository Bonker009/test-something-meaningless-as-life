package com.kshrd.devconnect_springboot.utils;

import com.kshrd.devconnect_springboot.exception.BadRequestException;
import com.kshrd.devconnect_springboot.model.JSONBTemplate.TestCase;

import java.util.List;
import java.util.stream.Collectors;

public class CodeGenerator {

    public static String generate(String lang, String header, String body, String fnName, List<TestCase> testCases) {
        body = cleanBody(body, header, fnName);
        return switch (lang.toLowerCase().trim()) {
            case "java" -> generateJava(header, body, fnName, testCases);
            case "python" -> generatePython(body, fnName, testCases);
            case "cpp" -> generateCpp(header, body, fnName, testCases);
            case "javascript", "js" -> generateJavaScript(header, body, fnName, testCases);
            case "csharp", "cs" -> generateCSharp(header, body, fnName, testCases);
            case "php" -> generatePHP(header, body, fnName, testCases);
            default -> throw new BadRequestException("Unsupported language: " + lang);
        };
    }

    private static String cleanBody(String body, String header, String fnName) {
        if (body != null && body.contains(fnName + "(")) {
            int fnPos = body.indexOf(fnName + "(");
            int openBracePos = body.indexOf("{", fnPos);
            if (openBracePos != -1) {
                int closeBracePos = findMatchingCloseBrace(body, openBracePos);
                if (closeBracePos != -1 && closeBracePos > openBracePos) {
                    return body.substring(openBracePos + 1, closeBracePos).trim();
                } else {
                    return body.substring(openBracePos + 1).trim();
                }
            }
        }
        return body;
    }

    private static int findMatchingCloseBrace(String text, int openBracePos) {
        int depth = 1;
        for (int i = openBracePos + 1; i < text.length(); i++) {
            if (text.charAt(i) == '{') depth++;
            else if (text.charAt(i) == '}') {
                depth--;
                if (depth == 0) return i;
            }
        }
        return -1;
    }

    private static String generateJava(String header, String body, String fnName, List<TestCase> tests) {
        StringBuilder sb = new StringBuilder();
        sb.append("public class Solution {\n");
        sb.append("    ").append(header).append(" {\n");
        sb.append("        ").append(body).append("\n");
        sb.append("    }\n\n");
        sb.append("    public static void main(String[] args) {\n");
        for (TestCase t : tests) {
            String argsList = t.getInput().stream()
                    .map(CodeGenerator::formatJavaLiteral)
                    .collect(Collectors.joining(", "));
            sb.append("        System.out.println(").append(fnName).append("(").append(argsList).append("));\n");
        }
        sb.append("    }\n}");
        return sb.toString();
    }

    private static String generatePython(String body, String fnName, List<TestCase> tests) {
        StringBuilder sb = new StringBuilder();
        sb.append(body).append("\n");
        for (TestCase t : tests) {
            String argsList = t.getInput().stream()
                    .map(CodeGenerator::formatPythonLiteral)
                    .collect(Collectors.joining(", "));
            sb.append("print(").append(fnName).append("(").append(argsList).append("))\n");
        }
        return sb.toString();
    }

    private static String generateCpp(String header, String body, String fnName, List<TestCase> tests) {
        StringBuilder sb = new StringBuilder();
        sb.append("#include <iostream>\nusing namespace std;\n");
        sb.append(header).append(" {\n    ").append(body).append("\n}\n");
        sb.append("int main() {\n");
        for (TestCase t : tests) {
            String argsList = t.getInput().stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            sb.append("    cout << ").append(fnName).append("(").append(argsList).append(") << endl;\n");
        }
        sb.append("    return 0;\n}");
        return sb.toString();
    }

    private static String generateJavaScript(String header, String body, String fnName, List<TestCase> tests) {
        StringBuilder sb = new StringBuilder();
        String params = extractParamsFromHeader(header);
        sb.append("function ").append(fnName).append("(").append(params).append(") {\n");
        sb.append("    ").append(body).append("\n}\n\n");
        for (TestCase t : tests) {
            String argsList = t.getInput().stream()
                    .map(CodeGenerator::formatJavaScriptLiteral)
                    .collect(Collectors.joining(", "));
            sb.append("console.log(").append(fnName).append("(").append(argsList).append("));\n");
        }
        return sb.toString();
    }

    private static String generateCSharp(String header, String body, String fnName, List<TestCase> tests) {
        StringBuilder sb = new StringBuilder();
        sb.append("using System;\n\nclass Program {\n");
        sb.append("    ").append(header).append("\n    {\n        ").append(body).append("\n    }\n\n");
        sb.append("    static void Main(string[] args) {\n");
        for (TestCase t : tests) {
            String argsList = t.getInput().stream()
                    .map(CodeGenerator::formatCSharpLiteral)
                    .collect(Collectors.joining(", "));
            sb.append("        Console.WriteLine(").append(fnName).append("(").append(argsList).append("));\n");
        }
        sb.append("    }\n}");
        return sb.toString();
    }

    private static String generatePHP(String header, String body, String fnName, List<TestCase> tests) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?php\n\n");
        String params = extractParamsFromHeader(header);
        sb.append("function ").append(fnName).append("(").append(params).append(") {\n");
        sb.append("    ").append(body).append("\n}\n\n");
        for (TestCase t : tests) {
            String argsList = t.getInput().stream()
                    .map(CodeGenerator::formatPHPLiteral)
                    .collect(Collectors.joining(", "));
            sb.append("echo ").append(fnName).append("(").append(argsList).append(") . \"\\n\";\n");
        }
        sb.append("?>");
        return sb.toString();
    }

    private static String extractParamsFromHeader(String header) {
        if (header == null) return "";
        int start = header.indexOf('(');
        int end = header.lastIndexOf(')');
        if (start != -1 && end != -1 && end > start) {
            return header.substring(start + 1, end).trim();
        }
        return "";
    }

    private static String formatJavaLiteral(Object val) {
        return val instanceof String ? "\"" + val + "\"" : val.toString();
    }

    private static String formatPythonLiteral(Object val) {
        return val instanceof String ? "\"" + val + "\"" : val.toString();
    }

    private static String formatJavaScriptLiteral(Object val) {
        return val instanceof String ? "\"" + val + "\"" : val.toString();
    }

    private static String formatCSharpLiteral(Object val) {
        if (val instanceof String) return "\"" + val + "\"";
        if (val instanceof Boolean) return val.toString().toLowerCase();
        return val.toString();
    }

    private static String formatPHPLiteral(Object val) {
        if (val instanceof String) return "\"" + val + "\"";
        if (val instanceof Boolean) return val.toString().toLowerCase();
        return val.toString();
    }

    public static ExtractedFunction extractParts(String fullFunction, String language) {
        fullFunction = fullFunction.trim();

        if (language.equalsIgnoreCase("python")) {
            int start = fullFunction.indexOf("def ") + 4;
            int end = fullFunction.indexOf('(', start);
            if (start == 3 || end == -1) throw new BadRequestException("Invalid Python function.");
            String fnName = fullFunction.substring(start, end).trim();
            return new ExtractedFunction("", fullFunction, fnName);
        }

        if (language.equalsIgnoreCase("javascript") || language.equalsIgnoreCase("js")) {
            String fnName = "anonymous", header = "", body = "";
            if (fullFunction.contains("function ")) {
                int start = fullFunction.indexOf("function ") + 9;
                int end = fullFunction.indexOf('(', start);
                if (end == -1) throw new BadRequestException("Invalid JS function.");
                fnName = fullFunction.substring(start, end).trim();
                int brace = fullFunction.indexOf('{', end);
                int close = findMatchingCloseBrace(fullFunction, brace);
                header = fullFunction.substring(0, brace).trim();
                body = fullFunction.substring(brace + 1, close).trim();
            } else if (fullFunction.contains("=>")) {
                int arrow = fullFunction.indexOf("=>");
                header = fullFunction.substring(0, arrow).trim();
                int eq = header.indexOf('=');
                fnName = eq != -1 ? header.substring(0, eq).trim() : "anonymous";
                body = fullFunction.substring(arrow + 2).trim();
            } else {
                throw new BadRequestException("Invalid JS function format.");
            }
            return new ExtractedFunction(header, body, fnName);
        }

        // For Java, C++, C#, PHP - assume first line is header
        int headerEnd = fullFunction.indexOf('{');
        if (headerEnd == -1) throw new BadRequestException("Invalid function: missing '{'");
        String header = fullFunction.substring(0, headerEnd).trim();
        int bodyStart = headerEnd + 1;
        int bodyEnd = findMatchingCloseBrace(fullFunction, headerEnd);
        if (bodyEnd == -1) throw new BadRequestException("Invalid function: unmatched braces");
        String body = fullFunction.substring(bodyStart, bodyEnd).trim();

        // Extract function name from header (last word before '(')
        int fnStart = header.lastIndexOf(' ', header.indexOf('('));
        if (fnStart == -1) fnStart = 0; else fnStart++;
        int fnEnd = header.indexOf('(');
        if (fnEnd == -1) throw new BadRequestException("Invalid function header.");
        String fnName = header.substring(fnStart, fnEnd).trim();

        return new ExtractedFunction(header, body, fnName);
    }

    public record ExtractedFunction(String header, String body, String functionName) {}
}