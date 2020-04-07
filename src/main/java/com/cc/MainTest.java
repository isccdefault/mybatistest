package com.cc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainTest {
    public static void main(String[] args) {
        String email = "zhangsan@qq.com";
        String regEx = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(email);
        boolean test = matcher.matches();
        System.out.println(test);
    }
}
