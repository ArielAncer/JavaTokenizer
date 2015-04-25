/**
 * @uthor Ariel Ancer
 * @date 03/17/15 
 * 
 * Parser Project:
 * 
 * This class is the runnable class which will parse the 'code.txt' file specified in the FileTokenizer class,
 * listed in the Java project folder ( 'Ancer/Parser/code.txt' ) as is per usual with Eclipse.
 * 
 * I used JTree to display my parse tree, a prompt will ask you whether you would like all of the nodes to be expanded or 
 * not before viewing the parse tree so that you will not have to click through each node.
 * 
 */

package prser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileParser extends ParseTree {
	
	public HashMap<Token, String> symbolTable;
	public ParseTree pt;
	
	public FileParser() throws IOException {
		symbolTable = new HashMap<Token, String>();
		pt = new ParseTree();
		
		//
		this.program();
		//
	}

	public static void main(String[] args) throws IOException {

		System.out.println("Trace Table:\n");
		FileParser fp = new FileParser();
		
		System.out.println("\nSymbol Table:\n");
		fp.printSTable(fp.symbolTable);
		
		
		fp.ViewTree(fp.root);
        
 
	}

	public void program() {
		
		System.out.println("Enter <program>");
		
		DefaultMutableTreeNode c = this.root;
		
		  parse_class(c);
		  
		  while( nextToken().value.equals("class") ){
			  getToken();
			  parse_class(c);  
		  }
		  
		System.out.println("Exit <program>");

	}

	private void parse_class(DefaultMutableTreeNode c) {
		c = addChild(c , "<class>");
		System.out.println("Enter <class>");
		

		if ( getToken().value.equals("class") ) {
			Token t = getToken();
			if (t.type == lex.IDENT) {
				addSymbol(t);
				if (getToken().type == lex.LSBRACKET) {
					classbody(c);
					if (getToken().type == lex.RSBRACKET) { // After class is done.
						
						System.out.println("Exit <class>");
					} else {
						error();
					}
				} else {
					error();
				}
			} else {
				error();
			}
		} else {
			error();
		}

	}

	private void error() {
		System.out.println("-===Error!===-");
		System.exit(0);

	}

	private void classbody(DefaultMutableTreeNode c) {
		c = addChild(c,"<classbody>");
		System.out.println("Enter <classbody>");
		
		while( tokens.get(tokCnt + 2).type == lex.LBRACKET ){
			method(c);
		}
		
		while( tokens.get(tokCnt + 2).type == lex.COMMA ){
			declaration(c);
		}
		
		System.out.println("Exit <classbody>");

	}

	private void method(DefaultMutableTreeNode c) {
		c = addChild(c , "<method>");
		System.out.println("Enter <method>");
		
		header(c);
		if( getToken().type == lex.LSBRACKET ){
			body(c);
			if( getToken().type == lex.RSBRACKET ){
				System.out.println("Exit <method>");
			}else{
				error();
			}
		}else{
			error();
			}
	}

	private void body( DefaultMutableTreeNode c) {
		c = addChild(c , "<body>");
		System.out.println("Enter <body>");
		
		
		while( nextToken().type != lex.RSBRACKET && currToken().type != lex.RSBRACKET ){
		
		while( nextToken().type == lex.IDENT ){
			declaration(c);
		}
		
		while( nextToken().type != lex.IDENT && nextToken().type != lex.RSBRACKET && currToken().type != lex.RSBRACKET ){
			statement(c);
			
		}
	}
		System.out.println("Exit <body>");
		
	}

	private void statement( DefaultMutableTreeNode c ) {
		c = addChild(c, "<statement>");
		System.out.println("Enter <statement>");
		
		
		switch( currToken().value ){
		case "cin":
			cin(c);
			break;
		case "cout":
			cout(c);
			break;
		case "if":
			parse_if(c);
			break;
		case "while":
			parse_while(c);
			break;
		case "{":
			Token t = getToken();
			if(currToken().type != lex.RSBRACKET){
				statement(c);
			}
				break;
			default:
				Token v = nextToken();
				if(nextToken().type == lex.LBRACKET){
					call(c);
				}else{
					assign(c);
				}
				break;
		}
			
		System.out.println("Exit <statement>");
	}

	private void call( DefaultMutableTreeNode c ) {
		c = addChild(c, "<call>");
		System.out.println("Enter <call>");
		
		variable(c); 
		
		if( getToken().type == lex.LBRACKET ){
			arglist(c);
			if( getToken().type == lex.RBRACKET ){
				if( getToken().type == lex.SEMICOLON){
					System.out.println("Exit <call>");
				}else{
					error();
				}
			}else{
				error();
			}
		}
		
	}

	private void arglist( DefaultMutableTreeNode c ) {
		c = addChild(c, "<arglist>");
		

		System.out.println("Enter <arglist>");
		
		Enum t = nextToken().type;
		
		if( t == lex.EXCL || t == lex.MINUS || t == lex.TILDE || 
				t == lex.INTEGER || t == lex.IDENT || t == lex.CONSTCH ){
			
			exp(c);
			
			while( nextToken().type == lex.COMMA ){
				getToken();
				exp(c);
			}
		}
		
		System.out.println("Exit <arglist>");
		
	}

	private void assign( DefaultMutableTreeNode c ) {
		c = addChild(c, "<assign>");

		System.out.println("Enter <assign>");
		
		listofvariables(c);
		if( getToken().type == lex.EQUAL ){
			exp(c);
			if( getToken().type == lex.SEMICOLON ){
				System.out.println("Exit <assign>");
			}else{
				error();
			}
		}else{
			error();
		}
		
	}

	private void listofvariables( DefaultMutableTreeNode c ) {
		c = addChild(c, "<listofvariables>");

		System.out.println("Enter <listofvariables>");
		
		variable(c);
		
		while(nextToken().type == lex.COMMA){
			getToken();
			variable(c);
		}
		
		System.out.println("Exit <listofvariables>");
	
	}

	private void parse_while( DefaultMutableTreeNode c ) {
		c = addChild(c, "<while>");
		System.out.println("Enter <while>");

		if( getToken().type == lex.WHILE ){
			if( getToken().type == lex.LBRACKET ){
				exp(c);
				if( getToken().type == lex.RBRACKET ){
					statement(c);
					getToken();
					System.out.println("Exit <while>");
				}else{
					error();
				}
			}else{
				error();
			}
		}else{
			error();
		}
		
	}

	private void parse_if( DefaultMutableTreeNode c ) {
		c = addChild(c, "<if>");
		System.out.println("Enter <if>");

		if( getToken().type == lex.IF ){
			if( getToken().type == lex.LBRACKET ){
				exp(c);
				if( getToken().type == lex.RBRACKET ){
					statement(c);
					getToken();
					if( nextToken().type == lex.ELSE ){
						getToken();
						statement(c);
					}
					System.out.println("Exit <if>");
					}else{
						error();
					}
				}else{
					error();
				}
			}else{
				error();
			}
		
	}

	private void cout( DefaultMutableTreeNode c ) {
		c = addChild(c, "<cout>");

		System.out.println("Enter <cout>");

		if( getToken().value.equals("cout") ){
			if( getToken().type == lex.LESSLESS ){
				listofexpressions(c);
				if( getToken().type == lex.SEMICOLON ){
					System.out.println("Exit <cout>");
				}else{
					error();
				}
			}else{
				error();
			}
		}else{
			error();
		}
	}

	private void listofexpressions( DefaultMutableTreeNode c ) {
		c = addChild(c, "<listofexpressions>");
		
		System.out.println("Enter <listofexpressions>");

		exp(c);
		
		while(nextToken().type == lex.COMMA){
			getToken();
			exp(c);
		}
		
		System.out.println("Exit <listofexpressions>");
	}

	private void cin( DefaultMutableTreeNode c ) {
		c = addChild(c, "<cin>");

		System.out.println("Enter <cin>");

		if( getToken().value.equals("cin") ){
			if( getToken().type == lex.GREATERGREATER ){
				listofvariables(c);
				if( getToken().type == lex.SEMICOLON ){
					System.out.println("Exit <cin>");
				}else{
					error();
				}
			}else{
				error();
			}
		}else{
			error();
		}

	}

	private void header(DefaultMutableTreeNode c) {
		c = addChild(c , "<header>");
		System.out.println("Enter <header>");
		type(c);
		variable(c);
		if( getToken().type == lex.LBRACKET ){
			parmlist(c);
			if( getToken().type == lex.RBRACKET ){
				System.out.println("Exit <header>");
			}else{
				error();
			}
		}else{
			error();
		}
		
	}

	private void parmlist( DefaultMutableTreeNode c ) {
		c = addChild(c, "<parmlist>");

		System.out.println("Enter <parmlist>");
		
		if( nextToken().value.equals("int") ||
				nextToken().value.equals("char")
				|| nextToken().value.equals("void")){
			type(c);
			variable(c);
			commaparmlist(c);
		}
		
		System.out.println("Exit <parmlist>");
		
	}

	private void commaparmlist( DefaultMutableTreeNode c ) {
		c = addChild(c, "<commaparmlist>");
		
		System.out.println("Enter <commaparmlist>");
		
		while( nextToken().type == lex.COMMA ){
			getToken();
			type(c);
			variable(c);
		}
		
		System.out.println("Exit <commaparmlist>");
		
		
	}

	private void exp( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp>");

		System.out.println("Enter <exp>");
		
		if( currToken().type == lex.LBRACKET ){
			getToken();
			exp(c);
			if( currToken().type != lex.RBRACKET ){
				error();	
			}	
		}else{
			exp2(c);
			while( currToken().type == lex.LEXOR ){
				getToken();
				exp2(c);
			}
		}
		
		System.out.println("Exit <exp>");
		
		
	}

	private void exp2( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp2>");
		
		System.out.println("Enter <exp2>");
		
		exp3(c);
		while( currToken().type == lex.LEXAND){
			getToken();
			exp3(c);
		}
		
		System.out.println("Exit <exp2>");
	}

	private void exp3( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp3>");
		
		System.out.println("Enter <exp3>");
		
		exp4(c);
		while( currToken().type == lex.OR){
			getToken();
			exp4(c);
		}
		
		System.out.println("Exit <exp3>");
	}

	private void exp4( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp4>");
		
		System.out.println("Enter <exp4>");
		
		exp5(c);
		while( currToken().type == lex.AND){
			getToken();
			exp5(c);
		}
		
		System.out.println("Exit <exp4>");
		
	}

	private void exp5( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp5>");

		System.out.println("Enter <exp5>");
		
		exp6(c);
		while( currToken().type == lex.EQUIVALENCE ||
				 currToken().type == lex.NOTEQUIV){
			getToken();
			exp6(c);
		}
		
		System.out.println("Exit <exp5>");
		
	}

	private void exp6( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp6>");

		System.out.println("Enter <exp6>");
		
		exp7(c);
		while( currToken().type == lex.LESSEQ||
				 currToken().type == lex.LESS ||
				 currToken().type == lex.GREATER ||
				 currToken().type == lex.GREATEREQ){
			getToken();
			exp7(c);
		}
		
		System.out.println("Exit <exp6>");
			
	}

	private void exp7( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp7>");
	
		System.out.println("Enter <exp7>");
		
		exp8(c);
		while( currToken().type == lex.PLUS||
				 currToken().type == lex.MINUS ){
			getToken();
			exp8(c);
		}
		
		System.out.println("Exit <exp7>");
			
	}

	private void exp8( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp8>");

		System.out.println("Enter <exp8>");
		
		exp9(c);
		while( currToken().type == lex.MULTIPLY ||
				currToken().type == lex.DIVIDE ||
				 currToken().type == lex.PERCENT ){
			getToken();
			exp9(c);
		}
		
		System.out.println("Exit <exp8>");
			
	}

	private void exp9( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp9>");

		System.out.println("Enter <exp9>");
		
		exp10(c);
		while( currToken().type == lex.EXCL ||
				currToken().type == lex.MINUS ||
				 currToken().type == lex.TILDE ){
			getToken();
			exp10(c);
		}
		
		System.out.println("Exit <exp9>");
			
	}

	private void exp10( DefaultMutableTreeNode c ) {
		c = addChild(c, "<exp10>");

		System.out.println("Enter <exp10>");
		Token t = getToken();
		if( t.type == lex.INTEGER ||
				t.type == lex.IDENT ||
						t.type == lex.CONSTCH){
			
			addSymbol(t);
		}else{
			error();
		}
		
		System.out.println("Exit <exp10>");
			
	}
	
	private void variable( DefaultMutableTreeNode c ) {
		c = addChild(c, "<variable>");
		
		System.out.println("Enter <variable>");
		
		Token t = getToken();
		if (t.type == lex.IDENT) {
			addSymbol(t);
			System.out.println("Exit <variable>");
		}else{
			error();
		}
		
	}

	private void type( DefaultMutableTreeNode c ) {
		c = addChild(c, "<type>");
		
		System.out.println("Enter <type>");
		
		Token t = getToken();
		
		if( t.value.equals("int") || 
				t.value.equals("char") ||  
				t.value.equals("void") ){
			
		System.out.println("Exit <type>");
		
		}else{
			error();
		}
			
	}
	
	public void addSymbol(Token t){
		
		if( t.type == lex.IDENT && !symbolTable.containsValue(t.value) ){
			symbolTable.put(t, t.value);
			}
		
	}
	
	public void printSTable(HashMap<Token, String> map){
		
		 Iterator iterator = map.keySet().iterator();
		  
		while (iterator.hasNext()) {
		   Token key = (Token) iterator.next();
		   String value = map.get(key).toString();
		  
		   System.out.println("-> " + key.type + " : " + value);
		}
	}

	private void declaration( DefaultMutableTreeNode c ) {
		c = addChild(c, "<declaration>");
		
		System.out.println("Enter <declaration>");
		
		type(c);
		variable(c);
		
		while( nextToken().type == lex.COMMA){
			getToken();
			variable(c);
		}
		
		if( getToken().type == lex.SEMICOLON ){
			System.out.println("Exit <declaration>");
		}else{
			error();
		}

	}

}
