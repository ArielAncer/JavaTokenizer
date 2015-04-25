package prser;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
	 
	public class ParseTree extends FileTokenizer{
		
    	public DefaultMutableTreeNode root ;
	    private JTree tree;
	    
	    public ParseTree() throws IOException{
	    	super();
	    	root = new DefaultMutableTreeNode("<program>");
	    }
	    
		public DefaultMutableTreeNode addChild( DefaultMutableTreeNode c , String name ){
			
			DefaultMutableTreeNode n = new DefaultMutableTreeNode(name);
			c.add(n);
			return n;
			
		}
  
	    public void ViewTree( DefaultMutableTreeNode curr ){
	
	    	tree = new JTree(curr);
	        add(tree);
	         
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setTitle("Parse tree");       
	        this.pack();
	        this.setSize(600, 600);
	        add(new JScrollPane(tree));

		   String opt = JOptionPane.showInputDialog(null, "Would you like view the tree in expanded view? (Y/N)");
		   if( opt.equalsIgnoreCase("y")){
		       expandAllNodes(tree, 0, tree.getRowCount());
		   }
		       
	        this.setVisible(true);
	        
	    }
	    
	    private void expandAllNodes(JTree tree, int startingIndex, int rowCount){
	        for(int i=startingIndex;i<rowCount;++i){
	            tree.expandRow(i);
	        }

	        if(tree.getRowCount()!=rowCount){
	            expandAllNodes(tree, rowCount, tree.getRowCount());
	        }
	    }
	  
	       
}
