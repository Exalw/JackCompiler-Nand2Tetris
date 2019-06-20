package own.project;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private List<String> toParse;
    private int pI;
    private List<String> output;
    private int tab;

    public Parser(List<String> toParse){
        this.toParse = toParse;
        pI = 0;
        output = new ArrayList<String>();
        tab = 0;
    }

    public String getToken(){
        if(pI<toParse.size())
            return toParse.get(pI);
        else
            return "";
    }

    public String getNextToken(){
        if(pI<toParse.size()-1)
            return toParse.get(pI+1);
        else
            return "";
    }

    private void compileVarName(){
        write(getToken(),"identifier");
    }

    private void compileClassName() {
        write(getToken(), "identifier");
    }

    private void compileSubroutineName() {
        write(getToken(), "identifier");
    }

    private void compileConstant(){
        write(getToken(),"integerConstant");
    }

    private void compileStringConstant() {
        write(getToken().substring(1,getToken().length()-1), "stringConstant");
    }

    public void compileClass(){
        if (is("class")){
            write("<class>");
            tab++;
            write("class","keyword");
            compileClassName();
            if(is("{")){
                write("{","symbol");
                while(is("static")||is("field")) {
                    compileClassVarDec();
                }
                while(is("constructor")||is("function")||is("method")){
                    compileSubroutineDec();
                }
                if(is("}")){
                    write("}","symbol");
                    tab--;
                    write("</class>");
                }
                else error("Class","ClassEndBracket");
            }
            else error("Class","ClassStartBracket");
        }
        else error("Class","ClassWord");
    }

    public void compileClassVarDec(){
        write("<classVarDec>");
        tab++;
        write(getToken(),"keyword");
        compileType();
        compileVarName();
        while(is(",")){
            write(",","symbol");
            compileVarName();
        }
        if(is(";")){
            write(";","symbol");
        }
        else error("classVarDec","semicolonSign");
        tab--;
        write("</classVarDec>");
    }

    private void compileDoStatement() {
        write("<doStatement>");
        tab++;
        write("do","keyword");
        compileSubroutineCall();
        if(is(";")){
            write(";","symbol");
        }
        else error("doStatement","semicolonSign");
        tab--;
        write("</doStatement>");
    }

    private void compileExpression() {
        write("<expression>");
        tab++;
        compileTerm();
        if(isOperator()){
            if(is("<")) write("&lt;","symbol");
            else if(is(">")) write("&gt;","symbol");
            else write(getToken(),"symbol");
            compileTerm();
        }
        tab--;
        write("</expression>");
    }

    private void compileExpressionList() {
        write("<expressionList>");
        tab++;
        if(!is(")")) {
            compileExpression();
            while (is(",")){
                write(",", "symbol");
                if (!(is(")")||is(","))) compileExpression();
                else error("expressionList", "commaNotFollowed");
            }
        }
        tab--;
        write("</expressionList>");
    }

    public void compileIfStatement(){
        write("<ifStatement>");
        tab++;
        write("if","keyword");
        if (is("(")){
            write("(","symbol");
            compileExpression();
            if(is(")")){
                write(")","symbol");
                if(is("{")){
                    write("{","symbol");
                    compileStatements();
                    if(is("}")){
                        write("}","symbol");
                        if(is("else")){
                            write("else","keyword");

                            if(is("{")){
                                write("{","symbol");
                                compileStatements();
                                if(is("}")){
                                    write("}","symbol");
                                }
                                else error("ifStatement","elseStatementsEndBracket");
                            }
                            else error("ifStatement","elseStatementsStartBracket");
                        }
                    }
                    else error("ifStatement","ifStatementsEndBracket");
                }
                else error("ifStatement","ifStatementsStartBracket");
            }
            else error("ifStatement","expressionEndBracket");
        }
        else error("ifStatement","expressionStartBracket");
        tab--;
        write("</ifStatement>");
    }

    public void compileLetStatement(){
        write("<letStatement>");
        tab++;
        write("let","keyword");
        compileVarName();
        if(is("[")){
            write("[","symbol");
            compileExpression();
            if(is("]")){
                write("]","symbol");
            }
            else error("letStatement","expressionEndBracket");
        }
        if (is("=")){
            write("=","symbol");
            compileExpression();
            if(is(";")){
                write(";","symbol");
            }
            else error("letStatement","semicolonSign");
        }
        else error("letStatement","equalSign");
        tab--;
        write("</letStatement>");
    }

    public void compileParameterList(){
        write("<parameterList>");
        tab++;
        if(isType()){
            compileType();
            compileVarName();
            while(is(",")){
                write(",","symbol");
                if(isType()){
                    compileType();
                    compileVarName();
                }
                else error("parameterList", "commaNotFollowed");
            }
        }
        tab--;
        write("</parameterList>");
    }

    private void compileReturnStatement() {
        write("<returnStatement>");
        tab++;
        write("return","keyword");
        if(!is(";")) compileExpression();
        if(is(";")){
            write(";","symbol");
        }
        else error("returnStatement","semicolonSign");
        tab--;
        write("</returnStatement>");
    }

    public void compileStatement(){
        if(is("while")) compileWhileStatement();
        else if(is("if")) compileIfStatement();
        else if(is("let")) compileLetStatement();
        else if(is("do")) compileDoStatement();
        else if(is("return")) compileReturnStatement();
        else error("statement","start");
    }

    public void compileStatements(){
        write("<statements>");
        tab++;
        while(is("while")||is("if")||is("let")||is("do")||is("return")){
            compileStatement();
        }
        tab--;
        write("</statements>");
    }

    private void compileSubroutineCall() {
        if(getNextToken().equals(".")) {
            write(getToken(), "identifier");
            if (is(".")) {
                write(".", "symbol");
            } else error("subroutineCall", "thisShouldNeverHaveBeenCalled");
        }
        compileSubroutineName();
        if (is("(")) {
            write("(","symbol");
            compileExpressionList();
            if (is(")")) {
                write(")","symbol");

            } else error("subroutineCall", "expressionEndBracket");
        } else error("subroutineCall", "expressionStartBracket");
    }

    public void compileSubroutineDec(){
        write("<subroutineDec>");
        tab++;
        write(getToken(),"keyword");
        if(is("void")) write("void","keyword");
        else compileType();
        compileSubroutineName();
        if(is("(")){
            write("(","symbol");
            compileParameterList();
            if(is(")")){
                write(")","symbol");
                compileSubroutineBody();
                tab--;
                write("</subroutineDec>");
            }
            else error("SubroutineDec","SubroutineDecEndBracket");
        }
        else error("SubroutineDec","SubroutineDecStartBracket");
    }

    public void compileSubroutineBody(){
        write("<subroutineBody>");
        tab++;
        if(is("{")){
            write("{","symbol");
            while(is("var")) {
                compileVarDec();
            }
            compileStatements();
            if(is("}")){
                write("}","symbol");

            }
            else error("subroutineBody","endBracket");
        }
        else error("subroutineBody","startBracket");
        tab--;
        write("</subroutineBody>");
    }

    private void compileTerm(){
        write("<term>");
        tab++;
        Character c = getToken().charAt(0);
        if(Character.isDigit(c)){
            compileConstant();
        }
        else if(c=='"') compileStringConstant();
        else if(c=='('){
            write("(","symbol");
            compileExpression();
            if(is(")")){
                write(")","symbol");
            }
            else error("term","expressionEndBrackets");
        }
        else if(isUnOperator()){
            write(getToken(),"symbol");
            compileTerm();
        }
        else if(isKeywordConstant()) write(getToken(),"keyword");
        else{
            if(getNextToken().equals("(")||getNextToken().equals(".")) compileSubroutineCall();
            else if(getNextToken().equals("[")){
                compileVarName();
                if(is("[")){
                    write("[","symbol");
                    compileExpression();
                    if(is("]")){
                        write("]","symbol");
                    }
                    else error("term","arrayEndBracket");
                }
            }
            else compileVarName();
        }
        tab--;
        write("</term>");
    }

    public void compileType(){
        if(is("int")||is("char")||is("boolean")){
            write(getToken(),"keyword");
        }
        else compileClassName();
    }

    public void compileVarDec(){
        write("<varDec>");
        tab++;
        write("var","keyword");
        compileType();
        compileVarName();
        while(is(",")){
            write(",","symbol");
            compileVarName();
        }
        if(is(";")){
            write(";","symbol");
        }
        else error("classVarDec","semicolonSign");
        tab--;
        write("</varDec>");
    }

    public void compileWhileStatement(){
        write("<whileStatement>");
        tab++;
        write("while","keyword");
        if (is("(")){
            write("(","symbol");
            compileExpression();
            if(is(")")){
                write(")","symbol");
                if(is("{")){
                    write("{","symbol");
                    compileStatements();
                    if(is("}")){
                        write("}","symbol");
                    }
                    else error("whileStatement","StatementsEndBracket");
                }
                else error("whileStatement","StatementsStartBracket");
            }
            else error("whileStatement","expressionEndBracket");
        }
        else error("whileStatement","expressionStartBracket");
        tab--;
        write("</whileStatement>");
    }

    private boolean isType(){
        return (is("int")||is("boolean")||is("char")||isName());
    }

    private boolean isOperator(){
        return (is("+")||is("-")||is("=")||is(">")||is("<")||is("*")
            ||is("/")||is("&")||is("|"));
    }

    private boolean isUnOperator(){
        return (is("~")||is("-"));
    }

    private boolean isKeywordConstant(){
        return (is("true")||is("false")||is("null")||is("this"));
    }

    public boolean isName(){
        Character c = getToken().charAt(0);
        boolean isName = (Character.isAlphabetic(c)||c=='_');
        for(int i=1;i<getToken().length();i++){
            c = getToken().charAt(i);
            if(!(Character.isAlphabetic(c)||c=='_'||Character.isDigit(c))) isName = false;
        }
        return isName;
    }

    public boolean is(String toEqual){
        if(getToken().equals(toEqual)){
            return true;
        }
        return false;
    }

    public void error(String zone, String problem){
        System.err.println("Error at "+problem+" of "+zone+" with "+getToken()+" in Line "+pI);
    }

    public void write(String toAdd){
        String s = new String(toAdd);
        for(int i=0;i<tab;i++) s = "  " + s;
        output.add(s);
    }

    public void write(String toAdd, String brackets){           //increases pI!
        String s = "<"+brackets+"> "+toAdd+" </"+brackets+">";
        pI++;
        write(s);
    }

    public List<String> getOutput(){
        return output;
    }

}
