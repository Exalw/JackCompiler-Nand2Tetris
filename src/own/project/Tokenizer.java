package own.project;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {

    private List<String> orgString;
    private List<String> tokens;
    private String toCheck;

    public Tokenizer(List<String> orgString){
        this.orgString = orgString;
        tokens = new ArrayList<String>();
    }

    public List<String> tokenizeList(){
        System.out.println();
        tokens = new ArrayList<String>();
        for(int oSI=0;oSI<orgString.size();oSI++){
            toCheck = orgString.get(oSI);
            while(toCheck.length()>0){
                if(toCheck.charAt(0)==' ') toCheck = toCheck.substring(1);
                else if(toCheck.charAt(0)=='\t') toCheck = toCheck.substring(1);
                else if(substring(toCheck,2).equals("//")){
                        toCheck = "";       //skip Line
                }
                else if(substring(toCheck,2).equals("/*")) {
                    scanComment();
                }
                else if(equalsNext("{")||equalsNext("}")||equalsNext("[")|| equalsNext("(")||
                    equalsNext(")")||equalsNext("]")||equalsNext(".")||equalsNext(",")||
                    equalsNext(";")||equalsNext("+")||equalsNext("-")||equalsNext("*")||
                    equalsNext("/")||equalsNext("&")||equalsNext("|")||equalsNext("<")||
                    equalsNext(">")||equalsNext("=")||equalsNext("~")||
                    equalsNext("class")||  equalsNext("constructor")||equalsNext("function")||
                    equalsNext("method")|| equalsNext("field")||      equalsNext("static")||
                    equalsNext("var")||    equalsNext("int")||        equalsNext("char")||
                    equalsNext("boolean")||equalsNext("void")||       equalsNext("true")||
                    equalsNext("false")||  equalsNext("null")||       equalsNext("this")||
                    equalsNext("let")||    equalsNext("do")||         equalsNext("if")||
                    equalsNext("else")||   equalsNext("while")||      equalsNext("return")){

                }
                else if(Character.isDigit(toCheck.charAt(0))){
                    scanNumber();
                }
                else if(toCheck.charAt(0)=='"'){
                    toCheck = toCheck.substring(1);
                    scanString();
                }
                else if(equalsNextIdentifier()){

                }
                else{
                    System.err.println("Problem with \""+toCheck.charAt(0)+"\" or \""+toCheck+"\" at Line "+oSI);
                    return new ArrayList<String>();
                }
            }
        }
        return tokens;
    }

    public String substring(String s, int endI){
        int end = endI;
        if(s.length()<endI) end = s.length();
        return s.substring(0,end);
    }

    public boolean equalsNext(String toEqual){
        if(substring(toCheck,toEqual.length()).equals(toEqual)){
            tokens.add(toEqual);
            toCheck = toCheck.substring(toEqual.length());
            return true;
        }
        return false;
    }

    public void scanNumber(){
        String number = "";
        boolean stillDigits = true;
        while(toCheck.length()>0 && stillDigits){
            if(Character.isDigit(toCheck.charAt(0))){
                number = number + toCheck.charAt(0);
                toCheck = toCheck.substring(1);
            }
            else stillDigits = false;
        }
        tokens.add(number);
    }

    public void scanString(){
        String s = "\"";
        boolean stillString = true;
        while(toCheck.length()>0 && stillString){
            if(toCheck.charAt(0)!='"'){
                s = s + toCheck.charAt(0);
                toCheck = toCheck.substring(1);
            }
            else{
                stillString = false;
                toCheck = toCheck.substring(1);
            }
        }
        s = s + '"';
        tokens.add(s);
    }

    public void scanComment(){
        boolean stillComment = true;
        while(toCheck.length()>0 && stillComment){
            if(!substring(toCheck,2).equals("*/")){
                toCheck = toCheck.substring(1);
            }
            else{
                toCheck = toCheck.substring(2);
                stillComment = false;
            }
        }
    }

    public boolean equalsNextIdentifier(){
        String identifier = "";
        boolean stillIdentifier = true;
        if(Character.isAlphabetic(toCheck.charAt(0))||toCheck.charAt(0)=='_'){
            while(toCheck.length()>0 && stillIdentifier){
                Character c = toCheck.charAt(0);
                if(Character.isDigit(c)||Character.isAlphabetic(c)||c=='_'){
                    identifier = identifier + c;
                    toCheck = toCheck.substring(1);
                }
                else stillIdentifier = false;
            }
            tokens.add(identifier);
            return true;
        }
        return false;
    }
}
