package prser;

import java.io.IOException;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

public class Interpreter extends FileParser {

	public Interpreter() throws IOException{
		super();
	}
	
	public static void main( String[]args ) throws IOException{
		
		FileParser fp = new FileParser();
		
		Enumeration e = fp.root.preorderEnumeration();
	    while(e.hasMoreElements()){   
	    	//Object elem = e.nextElement();
	    	DefaultMutableTreeNode elem = (DefaultMutableTreeNode) e.nextElement();
	    	
	    	if(elem.getUserObject().equals("<variable>")){
			      System.out.println( elem );
	    	}
	      
	    }
	
	}
}
