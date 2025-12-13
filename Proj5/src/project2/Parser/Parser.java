package project2.Parser;

import java.util.HashSet;
import java.util.Set;

import project2.Core;
import project2.CoreScanner;
import project2.ParserException;
import project2.Node.AssignNode;
import project2.Node.CallNode;
import project2.Node.CmprNode;
import project2.Node.CondNode;
import project2.Node.ConstantNode;
import project2.Node.DeclIntegerNode;
import project2.Node.DeclNode;
import project2.Node.DeclObjNode;
import project2.Node.DeclSeqNode;
import project2.Node.ExprNode;
import project2.Node.FactorNode;
import project2.Node.FunctionMap;
import project2.Node.FunctionNode;
import project2.Node.IfNode;
import project2.Node.LoopNode;
import project2.Node.ParameterNode;
import project2.Node.PrintNode;
import project2.Node.ProcedureNode;
import project2.Node.ReadNode;
import project2.Node.StmtNode;
import project2.Node.StmtSeqNode;
import project2.Node.TermNode;
import project2.Node.FunctionNode;
import project2.Node.ParameterNode;
import project2.Node.CallNode;



public class Parser {

    private CoreScanner scanner;
    private Core currentToken;

    public Parser(CoreScanner scanner) {
        this.scanner = scanner;
        this.currentToken = scanner.currentToken();
    }

    public TermNode parseTerm() throws ParserException {
        TermNode termNode = new TermNode();
        // firstFactor
        if (this.scanner.currentToken() == Core.ID
                || this.scanner.currentToken() == Core.CONST
                || this.scanner.currentToken() == Core.LPAREN) {

            FactorNode firstFactor = this.parseFactor();
            termNode.factors.add(firstFactor);
        } else {
            throw new ParserException("ERROR: expected factor at start of term");
        }
        // * or /
        while (this.scanner.currentToken() == Core.MULTIPLY
                || this.scanner.currentToken() == Core.DIVIDE) {
            termNode.ops.add(this.scanner.currentToken());
            this.scanner.nextToken(); // consume * or /
            // secondTerm
            if (this.scanner.currentToken() == Core.ID
                    || this.scanner.currentToken() == Core.CONST
                    || this.scanner.currentToken() == Core.LPAREN) {

                FactorNode nextFactor = this.parseFactor();
                termNode.factors.add(nextFactor);
            } else {
                throw new ParserException(
                        "ERROR: expected factor after operator in term");
            }

        }
        return termNode;
    }

    public FactorNode parseFactor() throws ParserException {
        FactorNode factorNode = new FactorNode();

        // const
        if (this.scanner.currentToken() == Core.CONST) {
            ConstantNode constantNode = new ConstantNode();
            constantNode.value = this.scanner.getConst();
            factorNode.constant = constantNode;
            this.scanner.nextToken(); // consume CONST
            return factorNode;
        }

        // ( expr )
        if (this.scanner.currentToken() == Core.LPAREN) {
            this.scanner.nextToken(); // consume (
            ExprNode exprNode = this.parseExpr(); // return a expr node
            factorNode.expr = exprNode;
            if (this.scanner.currentToken() == Core.RPAREN) {
                this.scanner.nextToken(); // consume )
            } else {
                throw new ParserException("ERROR: ')' expected");
            }
            return factorNode;
        }

        // id
        if (this.scanner.currentToken() == Core.ID) {
            factorNode.identifier = this.scanner.getId();
            this.scanner.nextToken(); // consume ID

            // id [ string ]
            if (this.scanner.currentToken() == Core.LSQUARE) {
                this.scanner.nextToken(); // consume [
                if (this.scanner.currentToken() == Core.STRING) {
                    factorNode.str = this.scanner.getString();
                    this.scanner.nextToken(); // consume string
                } else {
                    throw new ParserException("ERROR: string expected");
                }
                if (this.scanner.currentToken() == Core.RSQUARE) {
                    this.scanner.nextToken(); // consume ]
                } else {
                    throw new ParserException("ERROR: ']' expected");
                }
            }
            return factorNode;
        }
        throw new ParserException("ERROR: factor expected");

    }

    public ExprNode parseExpr() throws ParserException {
        ExprNode exprNode = new ExprNode();

        // firstTerm
        TermNode firstTerm = this.parseTerm();
        exprNode.term.add(firstTerm);

        // + or -
        if (this.scanner.currentToken() == Core.ADD
                || this.scanner.currentToken() == Core.SUBTRACT) {
            exprNode.ops.add(this.scanner.currentToken().toString());
            this.scanner.nextToken(); // consume +/-

            ExprNode secondExprNode = this.parseExpr();
            exprNode.term.addAll(secondExprNode.term);
            exprNode.ops.addAll(secondExprNode.ops);
        }
        return exprNode;

    }

    public CmprNode parseCmpr() throws ParserException {
        CmprNode cmprNode = new CmprNode();

        // first <expr>
        cmprNode.firstExprNode = this.parseExpr();

        // operator: must be == or <
        if (this.scanner.currentToken() == Core.EQUAL
                || this.scanner.currentToken() == Core.LESS) {
            cmprNode.op = this.scanner.currentToken();
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: comparison operator '==' or '<' expected");
        }

        // second <expr>
        cmprNode.secondExprNode = this.parseExpr();
        return cmprNode;
    }

    public CondNode parseCond() throws ParserException {
        CondNode condNode = new CondNode();

        // not <cond>
        if (this.scanner.currentToken() == Core.NOT) {
            condNode.is_not = true;
            this.scanner.nextToken();
            condNode.contained_cond = this.parseCond();
        }
        // [ <cond> ]
        else if (this.scanner.currentToken() == Core.LSQUARE) {
            this.scanner.nextToken(); // consume [
            condNode.is_bracket = true;
            condNode.contained_cond = this.parseCond();
            if (this.scanner.currentToken() != Core.RSQUARE) {
                throw new ParserException("ERROR: ']' expected");
            }
            this.scanner.nextToken(); // consume ]
        }
        // <cmpr>
        else {
            condNode.firstCmprNode = this.parseCmpr();
            // <cmpr> or <cond>
            if (this.scanner.currentToken() == Core.OR) {
                condNode.op = this.scanner.currentToken();
                this.scanner.nextToken();
                condNode.secondCondNode = this.parseCond();
            }
            // <cmpr> and <cond>
            else if (this.scanner.currentToken() == Core.AND) {
                condNode.op = this.scanner.currentToken();
                this.scanner.nextToken();
                condNode.secondCondNode = this.parseCond();
            }
        }

        return condNode;
    }

    public LoopNode parseLoop() throws ParserException {
        LoopNode loopNode = new LoopNode();

        // for
        if (this.scanner.currentToken() != Core.FOR) {
            throw new ParserException("ERROR: 'for' expected");
        }
        this.scanner.nextToken();

        // (
        if (this.scanner.currentToken() != Core.LPAREN) {
            throw new ParserException("ERROR: '(' expected");
        }
        this.scanner.nextToken();

        // id
        if (this.scanner.currentToken() != Core.ID) {
            throw new ParserException("ERROR: loop variable (ID) expected");
        }

        loopNode.loopId = this.scanner.getId();
        this.scanner.nextToken();

        // =
        if (this.scanner.currentToken() != Core.ASSIGN) {
            throw new ParserException("ERROR: '=' expected after loop variable");
        }
        this.scanner.nextToken();

        // <expr>
        loopNode.initExpr = this.parseExpr();

        // ;
        if (this.scanner.currentToken() != Core.SEMICOLON) {
            throw new ParserException("ERROR: ';' expected");
        }
        this.scanner.nextToken();

        // <cond>
        loopNode.cond = this.parseCond();

        // ;
        if (this.scanner.currentToken() != Core.SEMICOLON) {
            throw new ParserException("ERROR: ';' expected");
        }
        this.scanner.nextToken();

        // <expr>
        loopNode.updateExpr = this.parseExpr();

        // )
        if (this.scanner.currentToken() != Core.RPAREN) {
            throw new ParserException("ERROR: ')' expected");
        }
        this.scanner.nextToken();

        // do
        if (this.scanner.currentToken() != Core.DO) {
            throw new ParserException("ERROR: 'do' expected");
        }
        this.scanner.nextToken();

        // <stmt-seq>
        loopNode.body = this.parseStmtSeq();

        // end
        if (this.scanner.currentToken() != Core.END) {
            throw new ParserException("ERROR: 'end' expected after loop body");
        }
        this.scanner.nextToken();

        return loopNode;
    }

    public StmtSeqNode parseStmtSeq() throws ParserException {
        StmtSeqNode stmtSeqNode = new StmtSeqNode();
        // first stmt
        StmtNode firstStmt = this.parseStmt();
        stmtSeqNode.statements.add(firstStmt);
        // rest of the stmt
        if (this.scanner.currentToken() == Core.ID // <Assign>
                || this.scanner.currentToken() == Core.IF
                || this.scanner.currentToken() == Core.FOR // <loop>
                || this.scanner.currentToken() == Core.PRINT
                || this.scanner.currentToken() == Core.READ
                || this.scanner.currentToken() == Core.INTEGER
                || this.scanner.currentToken() == Core.OBJECT // <decl>
                || this.scanner.currentToken() == Core.BEGIN
        ) {
            StmtSeqNode restStmtSeq = this.parseStmtSeq();
            stmtSeqNode.statements.addAll(restStmtSeq.statements);
        }

        return stmtSeqNode;
    }

    public StmtNode parseStmt() throws ParserException {
        StmtNode stmtNode = new StmtNode();

        // assign: id = <expr> ;
        if (this.scanner.currentToken() == Core.ID) {
            stmtNode.assign = this.parseAssign();
        }
        // if
        else if (this.scanner.currentToken() == Core.IF) {
            stmtNode.ifStmt = this.parseIf();
        }
        // loop
        else if (this.scanner.currentToken() == Core.FOR) {
            stmtNode.loop = this.parseLoop();
        }
        // print
        else if (this.scanner.currentToken() == Core.PRINT) {
            stmtNode.print = this.parsePrint();
        }
        // read
        else if (this.scanner.currentToken() == Core.READ) {
            stmtNode.read = this.parseRead();
        }
        // decl
        else if (this.scanner.currentToken() == Core.INTEGER
                || this.scanner.currentToken() == Core.OBJECT) {
            stmtNode.decl = this.parseDecl();
        } 
        // call
        else if(this.scanner.currentToken() == Core.BEGIN){
            stmtNode.call = this.parseCall();
        }else {
            throw new ParserException("ERROR: unexpected token '"
                    + this.scanner.currentToken() + "' in statement");
        }   
        return stmtNode;
    }

    public IfNode parseIf() throws ParserException {
        IfNode ifNode = new IfNode();

        // consume 'if'
        if (this.scanner.currentToken() == Core.IF) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: 'if' expected");
        }
        ifNode.cond = this.parseCond(); // parse <cond>

        // consume 'then'
        if (this.scanner.currentToken() == Core.THEN) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: 'then");
        }

        // parse <stmt-seq>
        ifNode.thenPart = this.parseStmtSeq();

        // optional else
        if (this.scanner.currentToken() == Core.ELSE) {
            this.scanner.nextToken(); // consume ELSE
            ifNode.elsePart = this.parseStmtSeq(); // else
        }

        // consume 'end'
        if (this.scanner.currentToken() == Core.END) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: 'end' expected to close if-statement");
        }

        return ifNode;
    }

    public ReadNode parseRead() throws ParserException {
        ReadNode readNode = new ReadNode();
        if (this.scanner.currentToken() == Core.READ) {
            this.scanner.nextToken(); // consume READ
        } else {
            throw new ParserException("ERROR: 'read' expected");
        }
        // consume '('
        if (this.scanner.currentToken() == Core.LPAREN) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: '(' expected");
        }

        // parse id
        if (this.scanner.currentToken() == Core.ID) {
            readNode.identifier = this.scanner.getId();
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: identifier expected");
        }

        // consume ')'
        if (this.scanner.currentToken() == Core.RPAREN) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: ')' expected");
        }

        // consume ';'
        if (this.scanner.currentToken() == Core.SEMICOLON) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: ';' expected");
        }
        return readNode;
    }

    public PrintNode parsePrint() throws ParserException {
        PrintNode printNode = new PrintNode();
        // consume 'print'
        if (this.scanner.currentToken() == Core.PRINT) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: 'print' expected");
        }

        // consume '('
        if (this.scanner.currentToken() == Core.LPAREN) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: '(' expected after print");
        }

        // parse <expr>
        printNode.expr = this.parseExpr();

        // consume ')'
        if (this.scanner.currentToken() == Core.RPAREN) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: ')' expected after expression");
        }
        // consume ';'
        if (this.scanner.currentToken() == Core.SEMICOLON) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: ';' expected after print statement");
        }
        return printNode;
    }

    public AssignNode parseAssign() throws ParserException {
        AssignNode assignNode = new AssignNode();

        // First token must be an identifier
        if (this.scanner.currentToken() == Core.ID) {
            assignNode.identifier = this.scanner.getId();
            this.scanner.nextToken(); // consume ID
        } else {
            throw new ParserException("ERROR: 'identifier' expected");
        }
        // id = <expr>
        if (this.scanner.currentToken() == Core.ASSIGN) {
            this.scanner.nextToken(); // consume '='

            // id = new object( string, <expr> );
            if (this.scanner.currentToken() == Core.NEW) {
                this.scanner.nextToken(); // consume "new"

                if (this.scanner.currentToken() == Core.OBJECT) {
                    this.scanner.nextToken(); // consume "object"

                    if (this.scanner.currentToken() == Core.LPAREN) {
                        this.scanner.nextToken(); // consume '('
                    } else {
                        throw new ParserException("ERROR: '(' expected");
                    }

                    if (this.scanner.currentToken() == Core.STRING) {
                        assignNode.newObjectString = this.scanner.getString();
                        this.scanner.nextToken(); // consume string
                    } else {
                        throw new ParserException("ERROR: 'string' expected");
                    }

                    if (this.scanner.currentToken() == Core.COMMA) {
                        this.scanner.nextToken(); // consume ','
                    } else {
                        throw new ParserException("ERROR: ',' expected");
                    }

                    assignNode.newObjectExpr = this.parseExpr();

                    if (this.scanner.currentToken() == Core.RPAREN) {
                        this.scanner.nextToken(); // consume ')'
                    } else {
                        throw new ParserException("ERROR: ')' expected");
                    }
                    if (this.scanner.currentToken() == Core.SEMICOLON) {
                        this.scanner.nextToken(); // consume ';'
                    } else {
                        throw new ParserException("ERROR: ';' expected");
                    }
                    return assignNode;
                }

                else {
                    throw new ParserException("ERROR: 'object' expected");
                }
            }

            assignNode.expr = this.parseExpr();
            if (this.scanner.currentToken() == Core.SEMICOLON) {
                this.scanner.nextToken(); // consume ';'
            } else {
                throw new ParserException("ERROR: ';' expected");
            }
            return assignNode;

        }

        // Case 2: id [ string ] = <expr> ;
        else if (this.scanner.currentToken() == Core.LSQUARE) {
            this.scanner.nextToken(); // consume '['
            if (this.scanner.currentToken() == Core.STRING) {
                assignNode.index = this.scanner.getString();
                this.scanner.nextToken(); // consume string
            } else {
                throw new ParserException("ERROR: string expected");
            }
            if (this.scanner.currentToken() == Core.RSQUARE) {
                this.scanner.nextToken(); // consume ']'
            } else {
                throw new ParserException("ERROR: ']' expected");
            }

            if (this.scanner.currentToken() == Core.ASSIGN) {
                this.scanner.nextToken(); // consume '='
            } else {
                throw new ParserException("ERROR: '=' expected");
            }

            assignNode.expr = this.parseExpr();

            if (this.scanner.currentToken() == Core.SEMICOLON) {
                this.scanner.nextToken(); // consume ';'
            } else {
                throw new ParserException("ERROR: ';' expected after assignment");
            }
            return assignNode;
        }

        // Case 3: id : id ;
        else if (this.scanner.currentToken() == Core.COLON) {
            this.scanner.nextToken(); // consume ':'

            if (this.scanner.currentToken() == Core.ID) {
                assignNode.alias = this.scanner.getId();
                this.scanner.nextToken(); // consume ID
            } else {
                throw new ParserException("ERROR: identifier expected");
            }

            if (this.scanner.currentToken() == Core.SEMICOLON) {
                this.scanner.nextToken(); // consume ';'
            } else {
                throw new ParserException("ERROR: ';' expected");
            }

            return assignNode;
        } else {
            throw new ParserException(
                    "ERROR: assignment operator ('=', '[', or ':') expected");
        }

    }

    public DeclObjNode parseObjectDecl() throws ParserException {
        DeclObjNode node = new DeclObjNode();
        this.scanner.nextToken(); // consume "object"

        if (this.scanner.currentToken() == Core.ID) {
            node.id = this.scanner.getId();
            this.scanner.nextToken(); // consume ID
        } else {
            throw new ParserException("ERROR: identifier expected");
        }

        if (this.scanner.currentToken() == Core.SEMICOLON) {
            this.scanner.nextToken(); // consume ";"
        } else {
            throw new ParserException("ERROR: ';' expected");
        }
        return node;
    }

    public DeclIntegerNode parseDeclInteger() throws ParserException {
        DeclIntegerNode node = new DeclIntegerNode();

        if (this.scanner.currentToken() != Core.INTEGER) {
            throw new ParserException("ERROR: 'integer' expected");
        }
        this.scanner.nextToken(); // consume "integer"

        if (this.scanner.currentToken() == Core.ID) {
            node.identifier = this.scanner.getId();
            this.scanner.nextToken(); // consume ID
        } else {
            throw new ParserException("ERROR: identifier expected");
        }
        // consume ";"
        if (this.scanner.currentToken() == Core.SEMICOLON) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: ';' expected");
        }

        return node;
    }

    public DeclNode parseDecl() throws ParserException {
        if (this.scanner.currentToken() == Core.INTEGER) {
            return this.parseDeclInteger();
        } else if (this.scanner.currentToken() == Core.OBJECT) {
            return this.parseObjectDecl();
        } else {
            throw new ParserException(
                    "ERROR: declaration must start with 'integer' or 'object'");
        }
    }

    public DeclSeqNode parseDeclSeq() throws ParserException {
    DeclSeqNode declSeqNode = new DeclSeqNode();

    // Case 1: <decl>
    if (this.scanner.currentToken() == Core.INTEGER
            || this.scanner.currentToken() == Core.OBJECT) {
        DeclNode decl = this.parseDecl();
        declSeqNode.declare.add(decl);
    }
    // Case 2: <function>
    else if (this.scanner.currentToken() == Core.PROCEDURE) {
        FunctionNode func = this.parseFunction();
        declSeqNode.declare.add(func);
    }
    else {
        throw new ParserException("ERROR: expected declaration or function");
    }

    // Recursive case more <decl-seq> follows
    if (this.scanner.currentToken() == Core.INTEGER
            || this.scanner.currentToken() == Core.OBJECT
            || this.scanner.currentToken() == Core.PROCEDURE) {
        DeclSeqNode rest = this.parseDeclSeq();
        declSeqNode.declare.addAll(rest.declare);
    }

    return declSeqNode;
}


    public ProcedureNode parseProcedure() throws ParserException {
        ProcedureNode procedureNode = new ProcedureNode();

        // consume 'procedure'
        if (this.scanner.currentToken() == Core.PROCEDURE) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: 'procedure' expected");
        }

        // procedure ID
        if (this.scanner.currentToken() == Core.ID) {
            procedureNode.identifier = this.scanner.getId();
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: Procedure name (identifier) expected");
        }

        // consume 'is'
        if (this.scanner.currentToken() == Core.IS) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: 'is' expected");
        }
        // 
        // <decl-seq>
        if (this.scanner.currentToken() == Core.INTEGER
                || this.scanner.currentToken() == Core.OBJECT
                || this.scanner.currentToken() == Core.PROCEDURE) {
            procedureNode.declSeq = this.parseDeclSeq();
        }

        // consume 'begin'
        if (this.scanner.currentToken() == Core.BEGIN) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: 'begin' expected");
        }

        // parse <stmt-seq>
        procedureNode.stmtSeq = this.parseStmtSeq();

        // consume 'end'
        if (this.scanner.currentToken() == Core.END) {
            this.scanner.nextToken();
        } else {
            throw new ParserException("ERROR: 'end' expected");
        }

        if (this.scanner.currentToken() != Core.EOS) {
            throw new ParserException("ERROR: end of file expected");
        }

        return procedureNode;
    }

    public FunctionNode parseFunction() throws ParserException {
    FunctionNode funcNode = new FunctionNode();

    // procedure
    if (this.scanner.currentToken() == Core.PROCEDURE) {
        this.scanner.nextToken();
    } else {
        throw new ParserException("ERROR: 'procedure' expected");
    }

    // ID
    if (this.scanner.currentToken() == Core.ID) {
        funcNode.identifier = this.scanner.getId();
        this.scanner.nextToken();
    } else {
        throw new ParserException("ERROR: procedure name (identifier) expected");
    }

    // (
    if (this.scanner.currentToken() == Core.LPAREN) {
        this.scanner.nextToken();
    } else {
        throw new ParserException("ERROR: '(' expected");
    }

    // object
    if (this.scanner.currentToken() == Core.OBJECT) {
        this.scanner.nextToken();
    } else {
        throw new ParserException("ERROR: 'object' expected");
    }

    // <parameters>
    funcNode.parameters = this.parseParameters(true);

    // )
    if (this.scanner.currentToken() == Core.RPAREN) {
        this.scanner.nextToken();
    } else {
        throw new ParserException("ERROR: ')' expected");
    }

    // is
    if (this.scanner.currentToken() == Core.IS) {
        this.scanner.nextToken();
    } else {
        throw new ParserException("ERROR: 'is' expected");
    }

    if (this.scanner.currentToken() == Core.ID // <Assign>
        || this.scanner.currentToken() == Core.IF
        || this.scanner.currentToken() == Core.FOR // <loop>
        || this.scanner.currentToken() == Core.PRINT
        || this.scanner.currentToken() == Core.READ
        || this.scanner.currentToken() == Core.INTEGER
        || this.scanner.currentToken() == Core.OBJECT // <decl>
        || this.scanner.currentToken() == Core.BEGIN) {

        } else {
            throw new ParserException("ERROR: Function body is empty");
        }

    // <stmt-seq>
    funcNode.stmtSeq = this.parseStmtSeq();

    // end
    if (this.scanner.currentToken() == Core.END) {
        this.scanner.nextToken();
    } else {
        throw new ParserException("ERROR: 'end' expected");
    }

    FunctionMap.addFunction(funcNode.identifier, funcNode);

    return funcNode;
}

public ParameterNode parseParameters(Boolean functiondef) throws ParserException {
    ParameterNode paramNode = new ParameterNode();

    if (this.scanner.currentToken() == Core.ID) {
        paramNode.parameterList.add(this.scanner.getId());
        this.scanner.nextToken();
    } else {
        throw new ParserException("ERROR: parameter ID expected");
    }

    if (this.scanner.currentToken() == Core.COMMA) {
        this.scanner.nextToken();
        ParameterNode nextParams = this.parseParameters(functiondef);
        paramNode.parameterList.addAll(nextParams.parameterList);
    }
    if (functiondef) {
        Set<String> set = new HashSet<>(paramNode.parameterList);
        
        if (set.size() < paramNode.parameterList.size()) {
            throw new ParserException("ERROR: duplicate parameter names in function definition");
        }
        
    }

    return paramNode;
}

public CallNode parseCall() throws ParserException {
    CallNode callNode = new CallNode();

    // begin
    if (this.scanner.currentToken() == Core.BEGIN) {
        this.scanner.nextToken(); // consume 'begin'
    } else {
        throw new ParserException("ERROR: 'begin' expected at start of call");
    }

    // function ID
    if (this.scanner.currentToken() == Core.ID) {
        callNode.identifier = this.scanner.getId();
        this.scanner.nextToken(); // consume ID
    } else {
        throw new ParserException("ERROR: function name (ID) expected");
    }

    // (
    if (this.scanner.currentToken() == Core.LPAREN) {
        this.scanner.nextToken(); // consume '('
    } else {
        throw new ParserException("ERROR: '(' expected after function name");
    }

    // <parameters>
    callNode.parameters = this.parseParameters(false);

    // )
    if (this.scanner.currentToken() == Core.RPAREN) {
        this.scanner.nextToken(); // consume ')'
    } else {
        throw new ParserException("ERROR: ')' expected after parameters");
    }

    // ;
    if (this.scanner.currentToken() == Core.SEMICOLON) {
        this.scanner.nextToken(); // consume ';'
    } else {
        throw new ParserException("ERROR: ';' expected after call");
    }

    return callNode;
}



}
