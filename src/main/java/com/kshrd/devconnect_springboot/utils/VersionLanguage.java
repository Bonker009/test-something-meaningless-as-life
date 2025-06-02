package com.kshrd.devconnect_springboot.utils;

public class VersionLanguage {
    public static String getVersion(String language) {
        switch (language) {
            case "python":
                return "3.10.0";
            case "java":
                return "15";
            case "javascript":
                return "18.15.0";
//            case "c", "cpp":
//                return "10.2.0";
            case "csharp":
                return "6.12.0";
            case "php":
                return "8.2.3";
            default:
                return "this language is not available supported";
        }
    }
}
