Name: Jiaqing Guan

Project: CSE 3341 Project 1 – Core Scanner

Submitted Files:
1. CoreScanner.java  
   - Function: Implements the lexical scanner for the Core language.  
   - Provides methods:
     * CoreScanner(String filename) : constructor, opens the input file and initializes scanner
     * Core nextToken() : advance to the next token
     * Core currentToken() : returns the current token without advancing
     * String getId() : return the string value of ID
     * int getConst() : returns the integer value 
     * String getString() : returns the string literal value  
   - Handles keywords, symbols, identifiers, constants, and strings according to the project core.  
   - Includes HashMaps for keywords and symbols for efficient lookup.  
   - Implements error handling for:
     * Missing string quote
     * Constants out of the abound [0,8191]
     * Invalid or unsupported characters
     * Invalid input file

2. Core.java 
   - Function: Enum represents our tokens

3. Main.java 
   - Function: Driver program that invokes the scanner, reads an input file, and prints tokens to standard output.  


Special Features:
- The scanner always reads the longest possible token (for example, "==" is one token EQUAL, "123" is one CONST).  
- If a word is a keyword, it is treated as a keyword, otherwise it is an identifier (for example, treat "begin" as BEGIN, and treat "bEgIn" as ID).  
- Strings must be written inside single quotes.  
- Negative numbers like "-11", it is treated as SUBTRACT and a constant 11. 
- When something is wrong, the scanner prints an error message.  


Known Bugs:
- The scanner can finish all the rules in the project requirement. But some parts may still feel unclear. For example, "/" is treated as DIVIDE symbol, but the words "divide" or "DIVIDE" are not special are just IDs. 
- Strings do not support special escape characters (like \n or \t).  
- The scanner stops when it finds an error. It gives Core.ERROR and ends the program.  
