package prser;

import java.io.IOException;

import javax.swing.JFrame;

public class Token extends JFrame implements Type{

	public String value;
	public Enum type;
	
	public Token( ) throws IOException{
			
	}
	
	public Token( Enum type , String value ) throws IOException{
		
		this.value = value;
		this.type = type;
		
	}
	
}
