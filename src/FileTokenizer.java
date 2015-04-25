/**
 * @author Ariel Ancer
 * @date 28 February 2015
 * 
 * #### NOTE: 'ÿ' is the EOF symbol for txt files on my system. 
 * The symbol may vary for others, replace with your version if 
 * endless looping occurs. ####
 * 
 * Please replace 'code.txt' with your relative path to your 
 * file containing code to be tokenized on your system if preferred.
 */

package prser;

import java.io.*;
import java.util.ArrayList;

public class FileTokenizer extends Token {

	PushbackInputStream input;
    public String tok = "";
    public static ArrayList<Token> tokens = new ArrayList<Token>();
    public static int tokCnt;

    public FileTokenizer() throws IOException{
        super();
        input = new PushbackInputStream( new FileInputStream("code.txt"));
        
        build();
    }

    public static void main( String args[] ) throws IOException{

        FileTokenizer ft = new FileTokenizer();

        ft.printTokenList();
    }
    
    /**
     * This  method build's our token list by calling the appropriate methods 
     * on consecutive characters of the file.
     *  
     * @throws IOException
     */
    
    public void build() throws IOException{
    	
    	 while( ((int) nextChar() ) <= 127 ){
             buildTok(getChar());
         }
    }
   
    
    /**
     * The method printTokenList iterates through the list of tokens and 
     * prints each token with it's position in the ArrayList, it's value and
     * an arrow pointing to the token value.
     * 
     */
    public void printTokenList() {
        System.out.println("\nTokens\n=======");
        for(int i = 0; i < tokens.size(); i++ ){
            Token currToken = getToken();
            System.out.println("[" + i + "] : " + currToken.type + "\t" + " -> " + currToken.value);
        }

    }

    /**
     * Distinguishes between an INTEGER token, INDENT token or a symbol token.
     * THe token is then added to an ArrayList of tokens for further use.
     *
     * @param c
     * @throws IOException
     */
    public void buildTok(char c) throws IOException {

        int nc = (int) nextChar();
        int ic = (int) c;

        if( ( ( ic >= 32 ) && ( ic <= 47 ) ) ||
                ( ( ic >= 58 ) && ( ic <= 64 ) ) ||
                ( ( ic >= 91 ) && ( ic <= 94 ) ) ||
                ( ( ic >= 123 ) && ( ic <= 126 ) ) ){
            symbolCheck(c);
        }else if( (( nc >= 97) && (nc <= 122) ) ||
                ( (nc >= 65) && (nc <= 90) ) ||
                ( (nc >= 48) && (nc <= 57) ) ||
                ( ic == 95 )){
        	if( ic != 10 && ic != 9 ){
            tok += c;
        	}
        }else {
            tok += c;
            if( Character.isDigit(tok.charAt(0))){
                this.tokens.add(new Token(lex.INTEGER,tok));
            }else{
                if( !checkIdent(tok) && !tok.equals("\n") && !tok.equals("\t")  && !tok.equals("\r") ){
                    this.tokens.add(new Token(lex.IDENT, tok));
                }
                    }
            tok = "";
        }

    }

    /**
     * The checkIdent method is fed the current ch built string that has been classified
     * as an IDENT token and checks if that token is rather a keyword, if so the token is
     * added to the ArrayList of tokens.
     *
     * @param tok
     * @return
     * @throws IOException 
     */
    public boolean checkIdent(String tok) throws IOException {


        switch(tok){
            case "if":
                this.tokens.add( new Token(lex.IF,tok));
                break;
            case "while":
                this.tokens.add( new Token(lex.WHILE,tok));
                break;
            case "int":
                this.tokens.add( new Token(lex.INT,tok));
                break;
            case "cin":
                this.tokens.add( new Token(lex.CIN,tok));
                break;
            case "cout":
                this.tokens.add( new Token(lex.COUT,tok));
                break;
            case "char":
                this.tokens.add( new Token(lex.CHAR,tok));
                break;
            case "void":
                this.tokens.add( new Token(lex.VOID,tok));
                break;
            default:
                return false;
        }
 
        return true;
    }

    /**
     * The method symbolCheck is called when a potential does not begin with
     * an alphanumeric character. We classify this character as one of many
     * potential symbols listed in the method's switch statement. The method
     * will add the correct token for the symbol found.
     *
     * @param c
     * @throws IOException
     */
    private void symbolCheck(char c) throws IOException {

        String ch = "" + c;

        switch(c){
            case ',':
                this.tokens.add( new Token(lex.COMMA,ch));
                break;
            case'(':
                this.tokens.add( new Token(lex.LBRACKET,ch));
                break;
            case')':
                this.tokens.add( new Token(lex.RBRACKET,ch));
                break;
            case'{':
                this.tokens.add( new Token(lex.LSBRACKET,ch));
                break;
            case'}':
                this.tokens.add( new Token(lex.RSBRACKET,ch));
                break;
            case ';':
                this.tokens.add( new Token(lex.SEMICOLON,ch));
                break;
            case '>':
                if( nextChar() == c ){
                    getChar();
                    if( nextChar() == c ){
                        this.tokens.add( new Token(lex.GREATERX3,ch + c + getChar()));
                    }else{
                        this.tokens.add( new Token(lex.GREATERGREATER,ch + c));
                    }
                } else if( nextChar() == '='){
                    this.tokens.add( new Token(lex.GREATEREQ,ch + getChar()));
                }else{
                    this.tokens.add( new Token(lex.GREATER,ch));
                }
                break;
            case '<':
                if( nextChar() == c ){
                    this.tokens.add( new Token(lex.LESSLESS,ch + getChar()));
                } else if( nextChar() == '='){
                    this.tokens.add( new Token(lex.LESSEQ,ch + getChar()));
                }else{
                    this.tokens.add( new Token(lex.LESS,ch));
                }
                break;
            case '=':
                if( nextChar() == c ){
                    this.tokens.add( new Token(lex.EQUIVALENCE,ch + getChar()));
                }else{
                    this.tokens.add( new Token(lex.EQUAL,ch));
                }
                break;
            case '!':
                if( nextChar() == '=' ){
                    this.tokens.add( new Token(lex.NOTEQUIV,ch + getChar()));
                }else{
                    this.tokens.add( new Token(lex.EXCL,ch));
                }
                break;
            case '*':
                this.tokens.add( new Token(lex.MULTIPLY,ch));
                break;
            case '/':
                this.tokens.add( new Token(lex.DIVIDE,ch));
                break;
            case '%':
                this.tokens.add( new Token(lex.PERCENT,ch));
                break;
            case '~':
                this.tokens.add( new Token(lex.TILDE,ch));
                break;
            case '&':
                if( nextChar() == '&' ){
                    this.tokens.add( new Token(lex.LEXAND,ch + getChar()));
                }else{
                    this.tokens.add( new Token(lex.AND,ch));
                }
                break;
            case '|':
                if( nextChar() == '|' ){
                    this.tokens.add( new Token(lex.LEXOR,ch + getChar()));
                }else{
                    this.tokens.add( new Token(lex.OR,ch));
                }
                break;
            case '+':
                if( nextChar() == '+' ){
                    this.tokens.add( new Token(lex.PLUSPLUS,"" + getChar() + c));
                }else{
                    this.tokens.add( new Token(lex.PLUS,ch));
                }
                break;
            case '-':
                if( nextChar() == '-' ){
                    this.tokens.add( new Token(lex.MINUSMINUS,"" + getChar() + c));
                }else{
                    this.tokens.add( new Token(lex.MINUS,ch));
                }
                break;
            case ' ':
                break;
            default:
                break;
        }
    }

    /**
     * The getChar method reads in a single character from
     * the file specified above and returns the character if
     * no read errors occur, otherwise an IOException is thrown.
     *
     * @return
     * @throws IOException
     */
    public char getChar() throws IOException{
        return (char) input.read();
    }

    /**
     * The nextChar method reads in a single character from
     * the file specified above, saves the character to variable c
     * and the character is unread from the file ensuring that it
     * is the next character available from method getChar then nextChar
     * returns the variable c if no read errors occur, otherwise an IOException
     * is thrown.
     *
     * @return
     * @throws IOException
     */
    public char nextChar() throws IOException{

       int  c = input.read();
        this.ungetChar((char) c);
        return (char) c;
    }

    /**
     * The ungetChar method unreads a character from the file and allows the specified
     * character to be read again the next time the read method is called.
     *
     * @param c
     * @throws IOException
     */
    public void ungetChar(char c) throws IOException{
        input.unread(c);
    }

    /**
     * The getToken method returns the next token in the ArrayList,
     * if we have reached the end of the list we return the last token in the list.
     *
     * @return the next token in the ArrayList
     */
    public static Token getToken(){
        if( (tokCnt + 1) <= (tokens.size()-1)){
            return tokens.get(tokCnt++);
        }
        return tokens.get(tokCnt);
    }
    
    /**
     * The ungetToken method reduces the tokCnt counter in order to get to the
     * previous token in the ArrayList.
     * 
     */
    public static void ungetToken(){
        tokCnt--;
    }
    
    /**
     * The currToken method returns the current token in the ArrayList,
     * if we have reached the end of the list we return the last token in the list.
     *
     * @return the current token in the ArrayList
     */
    public static Token currToken(){
        return tokens.get(tokCnt);
    }
    
    
    /**
     * The getToken method peaks at the next token's value in the ArrayList,
     * if we have reached the end of the list we return the value of the last token in the list.
     *
     * @return the next token's value in the ArrayList
     */
    public static Token nextToken(){
        if( (tokCnt + 1) <= (tokens.size()-1)){
        	Token n = tokens.get(tokCnt+1);
            return tokens.get(tokCnt+1);
        }
        return tokens.get(tokCnt);
    }
}
