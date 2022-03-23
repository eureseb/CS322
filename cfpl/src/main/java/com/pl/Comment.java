package com.pl;

public class Comment {
    public Comment() {
    }

    String Comment(String lines){

        String line[] = lines.split("\n");

        int input = 0;

        for (String string : line) {
            if(string.startsWith("*")){
               input = 1;
               System.out.println(string);//prints out the comments
            }
        }
        return input == 1 ? "Comment/s found" : "No Comment/s found";
    }

}
