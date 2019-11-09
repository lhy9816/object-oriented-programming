package com.calvin.oohw3;

import com.calvin.oohw3.allterms.LinkTerms;
import com.calvin.oohw3.enums.ErrorType;
import com.calvin.oohw3.parser.AsTree;
import com.calvin.oohw3.parser.Lexer;
import com.calvin.oohw3.parser.Parser;

import java.util.Scanner;
import java.util.regex.Pattern;

public class OoMain {
    // main entrance of the whole program
    public static void main(String[] args) {
        String validInput = preCheck();
        Parser parser = new Parser(validInput);
        Lexer lex = parser.getLexer();
        parser.beginParse();
    
        // get astree
        AsTree asTree = parser.getAsTree();
        // link tree
        LinkTerms linkTerms = new LinkTerms(asTree);
        linkTerms.beginLink();
        String res = linkTerms.beginDerivative();
        System.out.println(res.replace(" ", ""));
    
    }
    
    // get input and pre-check before re
    public static String preCheck() {
        String userInput;
        
        // set scanner and get userinput
        Scanner scanner = new Scanner(System.in);
        if (scanner.hasNextLine()) {
            userInput = scanner.nextLine();
        } else {
            userInput = "";
        }
        
        // filter invisible char
        if (!filterUnChar(userInput)) {
            errorManipulate(ErrorType.INVALIDINPUT);
            System.exit(0);
        }
        
        // convert \t to SPACE
        userInput = userInput.replace('\t', ' ');
        
        // eliminate <space> & '\t' at the front / back of input
        userInput = userInput.trim();
        
        // check whether the input is empty
        if (userInput.isEmpty()) {
            errorManipulate(ErrorType.EMPTYINPUT);
            System.exit(0);
        }
        return "(" + userInput + ")";
    }
    
    // output refine
    public final String refineOtpt(String out) {
        Pattern p1 = Pattern.compile("\\(\\)");
        return "";
    }
    
    // pattern to filter unchar
    public static boolean filterUnChar(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < ' ' && s.charAt(i) != '\t') {
                return false;
            }
        }
        return true;
    }
    
    // error manipulate set
    public static void errorManipulate(ErrorType error) {
        // first output general wrong information
        System.out.println("WRONG FORMAT!");
        // then output specific wrong type
        switch (error) {
            case EMPTYINPUT:
                System.out.println("    EMPTY INPUT!");
                break;
            case INVALIDINPUT:
                System.out.println("    INVALID INPUT!");
                break;
            case WRONGSEG:
                System.out.println("    WRONG SEGMENT IN ITEMS!");
                break;
            case INVALIDCHAR:
                System.out.println("    INVALID CHAR IN INPUT!");
                break;
            case SPACEINSIGNEDNUM:
                System.out.println("    SPACE OCCURS BETWEEN NUMBER!");
                break;
            case BRASYMISMATCH:
                System.out.println("    SMALL BRASSY MISMATCH!");
                break;
            case LACKOPERATION:
                System.out.println("    LACK OPERATIONS IN EXPRESSION!");
                break;
            case WRONGFORMATNUMBER:
                System.out.println("    WRONG FORMAT IN NUMBER!");
                break;
            case QUITTOOEARLY:
                System.out.println("    QUIT TOO EARLY!");
                break;
            case WRONGGRAMMAR:
                System.out.println("    UNKNOWN GRAMMAR FOUND!");
                break;
            case WRONGTERMTYPEINLINKTERMS:
                System.out.println("    WRONG TERMTYPE IN LINK TERMS!");
                break;
            default:
                break;
        }
        System.exit(0);
    }
}
