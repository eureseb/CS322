package com.pl;

import org.junit.jupiter.api.Test;

import java.util.List;

public class ScannerTests {

    @Test
    void getTokens_from_string_source(){
        String source = "VAR + - / & % START STOP\n" +
                " INT AS";
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        for(Token tok: tokens){
            System.out.println(tok + ", ");
        }
    }
}
