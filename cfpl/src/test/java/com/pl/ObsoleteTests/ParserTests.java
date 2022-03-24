// package com.pl;

// import org.junit.jupiter.api.Test;

// import java.util.List;

// public class ParserTests {

//     @Test
//     void advance_goes_through_all_tokens_from_list(){
//         String source = "VAR sadasd AS INT + -";
//         Scanner scanner = new Scanner(source);
//         List<Token> tokens = scanner.scanTokens();
//         Parser parser = new Parser(tokens);
//         for(int i = 0; i < tokens.size(); i++){
//             System.out.println(parser.advanceToken());
//         }
//     }
// }
