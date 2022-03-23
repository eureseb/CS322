package com.pl;

public class Comment {
    public Comment() {
    }

    String Comment(String lines){

        String line[] = lines.split("\\s+");

        int input = 0;

        for (String string : line) {
            if(string.startsWith("*")){
               input = 1;
            }
        }
        return input == 1 ? "Comment found" : "No Comment";
    }

}
