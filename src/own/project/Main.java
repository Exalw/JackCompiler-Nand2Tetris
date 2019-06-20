package own.project;

import java.util.List;

public class Main {


    public static void main(String[] args) {
        if (args.length <= 0) {
            System.out.println("Please enter a fileName.");
        } else {
            List tokenizedString;
            if (args[0].length()>3){
                if(args[0].substring(args[0].length() - 5, args[0].length()).equals(".jack")){
                    boolean withMain = true;
                    System.out.print("...");
                    FileManager fileManager = new FileManager(args[0]);
                    if(args.length>2) {
                        if (args[args.length - 2].equals("-n")) {
                            fileManager = new FileManager(args[0], args[2]);
                        }
                    }
                    if(args.length>1) {
                        if(args[1].equals("--ni")) withMain = false;
                    }

                    Tokenizer tokenizer = new Tokenizer(fileManager.getStringArray());
                    tokenizedString = tokenizer.tokenizeList();
                    fileManager.saveFile2(tokenizedString);

                    Parser parser = new Parser(tokenizedString);
                    parser.compileClass();

                    fileManager.saveFile(parser.getOutput());
                    System.out.println("\t\tProcess finished successfully!");
                } else {
                    System.out.println("Please enter a fileName with a \".jack\" ending.");
                }
            } else {
                System.out.println("Please enter a fileName with a \".jack\" ending.");
            }
        }
    }
}
