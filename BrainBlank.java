/*********************************************************************
 * Kervon Gibson                                        Home Work #3 *
 * CISC 3120                                              06/20/2013 *
 *********************************************************************/

// This program simulates a compilier. It translates commands into C
// code.

import java.io.EOFException;
import java.util.Scanner;
import java.lang.String;
import java.lang.Character;

public class BrainBlank{
	public static void main(String args[]){
		SimpleComp mySimpleComp = new SimpleComp();
	}
}

class SimpleComp{
	protected compInterface []jumpComp = new compInterface[256];
	protected int compByte = 0;

	public SimpleComp(){

		String strInput = "";

		// Initialize jump array
		fillIt(255);
		jumpComp[(int)'>'] = new compIncrementPointer();
		jumpComp[(int)'<'] = new compDecrementPointer();
		jumpComp[(int)'+'] = new compIncrementByte();
		jumpComp[(int)'-'] = new compDecrementByte();
		jumpComp[(int)'.'] = new compOutputByte();
		jumpComp[(int)','] = new compInputByte();
		jumpComp[(int)'['] = new compJumpForward();
		jumpComp[(int)']'] = new compJumpBackward();
		jumpComp[(int)' '] = new compIgnoreSpace();

		// Get iput from user
		System.out.print("Enter commands: ");
		getInput(strInput);
	}

	// Recursive function initializes jump array to compError();
	protected void fillIt(int fillcnt){
		if(fillcnt<0) 
			return;
		jumpComp[fillcnt] = new compError();
		fillIt(fillcnt-1);
	}

	// Recursive function runs compiler till end of file
	protected void getInput(String strComp){
		Scanner input = new Scanner(System.in);
		try{
			if(input.hasNext()){
				strComp = strComp.concat(input.nextLine());
				getInput(strComp);
			}
			else throw new EOFException();
		}
		catch(EOFException e){
			System.out.println("End of File detected. Starting Copilier");
			compileIt(strComp,0, strComp.length());
			return;
		}
	}

	// Function takes a string then translte each command to C
	protected void compileIt(String compstring, int compstart, int compcnt){
		if(compstart==compcnt)
			return;
		jumpComp[(int)compstring.toCharArray()[compstart]].doComp();
		compileIt(compstring,compstart+1, compcnt);
	}

	// The interface for the compiler
	protected interface compInterface{	
		public void doComp();
	}

	// Error message for wrong command
	protected class compError implements compInterface{
		public void doComp(){
			System.out.println("Syntax Error: can't translate symbol");
		}
	}

	// Ignore whitespace
	protected class compIgnoreSpace implements compInterface{
		public void doComp(){
			return;
		}
	}

	// Increment the pointer
	protected class compIncrementPointer implements compInterface{
		public void doComp(){
			System.out.println("++p;");
		}
	}

	// Decrement the pointer
	protected class compDecrementPointer implements compInterface{
		public void doComp(){
			System.out.println("--p;");
		}
	}

	// Increment the byte at the pointer
	protected class compIncrementByte implements compInterface{
		public void doComp(){
			System.out.println("++*p;");
			compByte = compByte+1;
		}
	}

	// Decrement the byte at the pointer
	protected class compDecrementByte implements compInterface{
		public void doComp(){
			System.out.println("--*p;");
		}
	}

	// Output the byte at the pointer
	protected class compOutputByte implements compInterface{
		public void doComp(){
			System.out.println("putchar(*p);");
			System.out.println(compByte);
		}
	}

	// Input byte and store it at the pointer
	protected class compInputByte implements compInterface{
		public void doComp(){
			System.out.println("*p = getchar();");
		}
	}

	// If byte is zero jump forward past matching ]
	protected class compJumpForward implements compInterface{
		public void doComp(){
			System.out.println("while (*p) {");
		}
	}

	// If byte is not zero, jump backward to matching [
	protected class compJumpBackward implements compInterface{
		public void doComp(){
			System.out.println("}");
		}
	}
}