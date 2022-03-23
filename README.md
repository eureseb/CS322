# CS322 CFPL

| Members           |
| ----------------- |
| Eurese Bustamante |
| Paolo Brinquez    |
| Melvin Bulan      |
| Andrienne Cabaron |

| Interpreter | Use                                                                                              |
| ----------- | ------------------------------------------------------------------------------------------------ |
| visit()     | This allows for the traversal of the Abstract Syntax Tree(AST) that was generated via the parser |
| visit\*()   | Individual method to traverse a specific node or node-subtype                                    |

| Parser             | Use                                                                                                                       |
| ------------------ | ------------------------------------------------------------------------------------------------------------------------- |
| advance()          | Advances position.index . Stores currToken to tokenList.returns currToken                                                 |
| parse()            | runs program(). returns AST.                                                                                              |
| factor()           | for PLUS or MINUS. returns a Node. Can be Unary(eg. -1), Number(eg. 1, 3.1), or another Binary Node Expression(eg. 1 + 2) |
| term()             | for MULTIPLY or DIVIDE. returns a Node. Can be Binary Node Exp. (eg. 1 \* 2)                                              |
| expression()       | for PLUS or MINUS. returns a Node. Can be Binary Node Exp. (eg. 1 \+ 2)                                                   |
| declareStmt()      | for declaring statements. Can be AssignStmt(eg. x = 1) or OutputStmt(eg. OUTPUT: "Hello")                                 |
| declareMultStmts() | calls declareStmt() if a statement is identified.                                                                         |
| declareVar()       | for declaring variables. Accepted syntax: "\[KW_VAR\] \[IDENTIFIER\] \[KW_AS\] \[{DataType}\]"                            |
| declareMultVars()  | calls declareVar() when KW_VAR is present                                                                                 |
| program()          | generates an AST from the tokenList. returns ProgramNode aka AST head                                                     |

| Lexer                     | Use |
| ------------------------- | --- |
| scanTokens()              |     |
| scanToken()               |     |
| integerOrFloat()          |     |
| bool()                    |     |
| isAlphaNumeric()          |     |
| identifier()              |     |
| addToken()                |     |
| match()                   |     |
| character()               |     |
| peek()                    |     |
| peekNext()                |     |
| error()                   |     |
| nextCharacterFromSource() |     |
| getCurrentCharacter()     |     |
| isAtEnd()                 |     |
