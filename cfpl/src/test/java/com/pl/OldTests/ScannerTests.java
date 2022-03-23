// package com.pl;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.Test;

// import java.util.List;

// public class ScannerTests {

//     @Test
//     void getTokens_from_string_source(){
//         String source = "VAR idenIto AS INT\nSTART\nSTOP\n" +
//                 " INT AS";
//         Scanner scanner = new Scanner(source);
//         List<Token> tokens = scanner.scanTokens();
//         for(Token tok: tokens){
//             System.out.println(tok + ", ");
//         }
//     }

//     @Test
//     void getTokens_one_line_from_source(){
//         String source = "VAR dassda AS INT START OUTPUT STOP";
//         Scanner scanner = new Scanner(source);
//         List<Token> tokens = scanner.scanTokens();
//         for(Token tok: tokens){
//             System.out.println(tok + ", ");
//         }
//     }
//     @Test
//     void getTokens_from_keywords_and_identifiers(){
//         String source = "VAR abc, sda, a, s, AS INT";
//         Scanner scanner = new Scanner(source);
//         List<Token> tokens = scanner.scanTokens();
//         for(Token tok: tokens){
//             System.out.println(tok + ", ");
//         }
//     }
//     @Test
//     void getTokens_types_from_numbers(){
//         String source = "2312 AS INT, 2323.2322 AS FLOAT";
//         Scanner scanner = new Scanner(source);
//         List<Token> tokens = scanner.scanTokens();
//         for(Token tok: tokens){
//             System.out.println(tok + ", ");
//         }
//     }
// }
