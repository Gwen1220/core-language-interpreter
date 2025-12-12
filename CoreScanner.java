
/**
 * A lexical scanner for the Core language.
 * Converts input text into tokens (IDs, CONST, keywords, symbols).
 *
 * @author Jiaqing Guan
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

class CoreScanner {
    BufferedReader input;
    StringBuilder token;
    Core core;
    String id = "[a-zA-Z][a-zA-Z0-9]*";
    String constant = "[0-9]|[1-9][0-9]*";
    HashMap<String, Core> keywords, symbols;

    // Initialize the scanner
    private void initializeKeywords() {
        this.keywords = new HashMap<String, Core>();
        this.keywords.put("and", Core.AND);
        this.keywords.put("begin", this.core.BEGIN);
        this.keywords.put("case", this.core.CASE);
        this.keywords.put("do", this.core.DO);
        this.keywords.put("else", this.core.ELSE);
        this.keywords.put("end", this.core.END);
        this.keywords.put("for", this.core.FOR);
        this.keywords.put("if", this.core.IF);
        this.keywords.put("in", this.core.IN);
        this.keywords.put("integer", this.core.INTEGER);
        this.keywords.put("is", this.core.IS);
        this.keywords.put("new", this.core.NEW);
        this.keywords.put("not", this.core.NOT);
        this.keywords.put("object", this.core.OBJECT);
        this.keywords.put("or", this.core.OR);
        this.keywords.put("print", this.core.PRINT);
        this.keywords.put("procedure", this.core.PROCEDURE);
        this.keywords.put("read", this.core.READ);
        this.keywords.put("return", this.core.RETURN);
        this.keywords.put("then", this.core.THEN);
    }

    private void initializeSymbols() {
        this.symbols = new HashMap<String, Core>();
        this.symbols.put("+", Core.ADD);
        this.symbols.put("-", Core.SUBTRACT);
        this.symbols.put("*", Core.MULTIPLY);
        this.symbols.put("/", Core.DIVIDE);
        this.symbols.put("=", Core.ASSIGN);
        this.symbols.put("<", Core.LESS);
        this.symbols.put(":", Core.COLON);
        this.symbols.put(";", Core.SEMICOLON);
        this.symbols.put(".", Core.PERIOD);
        this.symbols.put(",", Core.COMMA);
        this.symbols.put("(", Core.LPAREN);
        this.symbols.put(")", Core.RPAREN);
        this.symbols.put("[", Core.LSQUARE);
        this.symbols.put("]", Core.RSQUARE);
        this.symbols.put("{", Core.LCURL);
        this.symbols.put("}", Core.RCURL);
    }

    CoreScanner(String fileName) {
        try {
            this.input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Cannot find the file");
        }
        this.initializeKeywords();
        this.initializeSymbols();
        this.core = this.nextToken();
    }

    // Skip the white space
    private int skipWhitespace(int c) throws Exception {
        while (c != -1 && Character.isWhitespace(c)) {
            c = this.input.read();
        }
        return c;
    }

    // Advance to the next token
    public Core nextToken() {
        try {
            int nextToken = this.input.read();
            nextToken = this.skipWhitespace(nextToken);
            // If this is the end of the stream
            if (nextToken == -1) {
                this.core = Core.EOS;
            } else {
                // If the current character is '=', the scanner decides between ASSIGN ("=") and EQUAL ("==").
                if ((char) nextToken == '=') {
                    this.core = Core.ASSIGN; // assume "=" by default
                    this.input.mark(1);
                    int nextChar = this.input.read();
                    if (nextChar == '=') {
                        this.core = Core.EQUAL; // upgrade to "==" if match
                    } else {
                        this.input.reset(); // rollback if not '='
                    }
                    return this.core;
                }

                // If this is '
                if ((char) nextToken == '\'') {
                    boolean endquote = false;
                    this.token = new StringBuilder();
                    this.core = Core.STRING;
                    nextToken = this.input.read();
                    // if the first character is digit, stop until non-digit
                    while (nextToken != -1 && nextToken != '\'') {
                        this.token.append((char) nextToken);
                        this.input.mark(1);
                        nextToken = this.input.read();
                        if (nextToken == '\'') {
                            endquote = true;
                        }
                    }
                    if (!endquote) {
                        // this is invalid input including missing ending quote.
                        System.out.println(
                                "ERROR: Missing ending quote. " + this.token.toString());
                        throw new Exception();
                    }
                    return this.core;
                }

                if (this.symbols.containsKey((char) nextToken + "")) {

                    this.core = this.symbols.get((char) nextToken + "");
                    return this.core;
                }

                // The scanner decides how to build the token based on the first character.
                this.token = new StringBuilder();
                // If the first character is a letter, the scanner reads an identifier or a keyword.
                if (Character.isLetter((char) nextToken)) {
                    for (;;) {
                        this.token.append((char) nextToken);
                        this.input.mark(1);
                        nextToken = this.input.read();
                        if (nextToken == -1
                                || !Character.isLetterOrDigit((char) nextToken)) {
                            this.input.reset();
                            break;
                        }
                    }
                }
                // If the first character is a digit, the scanner reads a number.
                else if (Character.isDigit((char) nextToken)) {
                    for (;;) {
                        this.token.append((char) nextToken);
                        this.input.mark(1);
                        nextToken = this.input.read();
                        if (nextToken == -1 || !Character.isDigit((char) nextToken)) {
                            this.input.reset();
                            break;
                        }
                    }
                }

                // If the first character is neither a digit nor a letter, the scanner reads a single character.
                else {
                    this.token.append((char) nextToken);
                }

                // The scanner checks if the token is a keyword, an identifier, or a constant.
                if (this.keywords.containsKey(this.token.toString())) {
                    this.core = this.keywords.get(this.token.toString());
                    return this.core;
                }
                String word = this.token.toString();
                // If the token matches the constant pattern, classify it as a constant.
                if (word.matches(this.constant)) {
                    int num = Integer.parseInt(word);
                    if (num >= 0 && num <= 8191) {
                        this.core = Core.CONST;
                    } else {
                        throw new Exception("Constant out of range: " + num);
                    }
                }
                // If the token matches the identifier pattern, classify it as an identifier.
                else if (word.matches(this.id)) {
                    this.core = Core.ID;
                }
                // If the token does not match any valid pattern, throw an error.
                else {
                    throw new Exception("Invalid token: " + word);
                }
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                System.out.println("ERROR: " + e.getMessage());
            } else {
                System.out.println(
                        "ERROR: Cannot identify the input " + this.token.toString());
            }
            this.core = Core.ERROR;
        }
        return this.core;
    }

    // Return the current token
    public Core currentToken() {
        return this.core;
    }

    // Return the string value of ID
    public String getId() {
        return this.token.toString();
    }

    // Return the constant value
    public int getConst() {
        return Integer.parseInt(this.token.toString());
    }

    // Return the character string
    public String getString() {
        return this.token.toString();
    }

}
