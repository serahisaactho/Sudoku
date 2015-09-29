/*
 * Write a program in Java to generate a typical Sodoku puzzle with a 9x9 grid. 
 * In Sodoku each column, each row, and each of the nine 3×3 sub-grids that compose the grid contains all of the digits from 1 to 9.
 * The program should randomly generate partially filled grid which could be filled either by a user. 
 * Keep Sodoku difficulty configurable.
 * 
 */
import java.io.*;
import java.util.*;
/**
 * @author Serah Isaac
 *
 */

public class SodokuGenerator {
	
	int num_givens=0; // Indicates the number of visible numbers in the Sodoku.
	
	public static void main(String[] arg) throws IOException
	{
		/*
		 * difficulty_level - holds the user's choice of an easy (1), medium (2) or difficult(3)
		 * sodoku.
		 * obj1- the current puzzle generator.
		 * puzzle1 - the created puzzle
		 * solved_puzzle - the solution to the created puzzle.
		 */
		BufferedReader keybd = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(" 1.Easy \n 2.Medium \n 3.Difficult");
		System.out.println("Please Enter the required difficulty level as a number");
		int difficulty_level=find_num_givens(keybd.readLine()); 
		
		if (difficulty_level!=0) //If the input was valid
		{
			SodokuGenerator obj1=new SodokuGenerator(difficulty_level);
			int[][] puzzle1=new int[9][9];
			int[][] solved_puzzle=new int[9][9];
			puzzle1= obj1.create_puzzle(0, new int [9][9]);
		
			//Deep Copying the solved puzzle.
			for (int i=0;i<9;i++)
			{
				for (int j=0;j<9;j++)
				{
					solved_puzzle[i][j]=puzzle1[i][j];
				}
			}
				
			puzzle1= obj1.add_blanks(puzzle1);
			obj1.print_puzzle(puzzle1); //Displaying the unsolved puzzle
		
		//To display the solution:-
		 System.out.println("The Solution :-");
		 obj1.print_puzzle(solved_puzzle);
		 
		}
}
		
	public int[][] add_blanks(int[][] puzzle)
	{
		/*
		 * Adds the required number of spaces to the given Sodoku.
		 */
		int count=1;
		int x,y;
		while (count<=(81-num_givens))
		{
			x=get_random_value(8,0);
			y=get_random_value(8,0);
			if (puzzle[x][y]!=0) //In case, the current position is not already a blank space
			{
				puzzle[x][y]=0;
				count++;
			}
			else
			{
				continue;
			}
		}
		return puzzle;
	}
	
	public void print_puzzle(int[][] puzzle)
	{
		/*
		 * Displays the puzzle with proper formatting to ensure readability.
		 */
		for (int i=0;i<9;i++)
		{
			for (int j=0;j<9;j++)
			{
				if (puzzle[i][j]!=0)
				{
					System.out.print(puzzle[i][j]+"  ");
				}
				else
				{
					System.out.print("_  ");
				}
			}
			System.out.println();
			System.out.println();
		}
	}
	
	public SodokuGenerator(int num_givens)
	{
		/*
		 * Constructor for the puzzle generator class.
		 */
		this.num_givens=num_givens;
	}
	
	public static int get_random_value(int high, int low)
	{
		//Returns a random value between the given maximum and minimum values(both inclusive)
		Random r=new Random();
		return (r.nextInt(high+1-low) + low);
	}
	
	public int[][] create_puzzle(int position, int[][] puzzle)
	{
		/*
		 * Creates a random Sodoku puzzle.
		 */
		int index=position;
		
		List<Integer> possible_values = new ArrayList<Integer>();
        for (int i=1;i<=9;i++)
        {
        	possible_values.add(i);
        }
        Collections.shuffle(possible_values); //Shuffling an entire row of the Sodoku.
		
		if (index > 80)
		{
            return puzzle;
		}
		
		int y = position%9;
        int x = position/9; //Getting row and column coordinates within the puzzle
        
        while (possible_values.size() > 0) 
        {
            int number = next_random_number(puzzle, x, y, possible_values);
            if (number == -1)
            {
            	return null;
            }
            puzzle[x][y] = number;
            
            //RECURSIVE CALL
            int[][] tempPuzzle = create_puzzle(position+1, puzzle);
            if (tempPuzzle!=null)
            {
            	return tempPuzzle;
            }
            puzzle[x][y] = 0;
        }
        return null;
}
		
	public int next_random_number(int[][] puzzle, int x, int y, List<Integer> possible_values)
	{
		/*
		 * Returns the next random number between 0 and 9 that fulfills all the Sodoku 
		 * requirements and -1 if there exists no such number.
		 */
		  while (possible_values.size()>0) 
		  {
              int curr_value = possible_values.remove(0);
              if (check_row(x,y,curr_value,puzzle) && check_column(x,y,curr_value,puzzle)
              && check_block(x,y,curr_value,puzzle))
              {
            	  return curr_value; //Returns the value without a conflict.
              }
		  }
		  return -1; //In case no number is a fit.
	}
	
	
	public static boolean check_column(int x, int y, int curr_value, int[][] puzzle)
	{
		//Checks whether the current value is already present in its column.
		for (int i=0;i<9;i++)
		{
			if (puzzle[i][y]==curr_value)
			{
				return false;
			}
		}
		return true;
	}
	
	
	public boolean check_row(int x, int y, int curr_value, int[][] puzzle)
	{
		//Checks whether the current value is already present in its row.
		for (int j=0;j<9;j++)
		{
			if (puzzle[x][j]==curr_value)
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean check_block(int x, int y, int curr_value, int[][] puzzle)
	{
		//Checks whether the current value is already present in its block.
		int block_row_start=0, block_row_end=0,block_col_start=0, block_col_end=0;
		
		if (x==0 || x==3 || x==6)
		{
			block_row_start=x;
			block_row_end=x+3-1;
		}
		else if (x==2 || x==5 || x==8)//At the end of a block
		{
			block_row_start=x-3+1; 
			block_row_end=x;
		}
		else if (x==1 || x==4 || x==7)
		{
			block_row_start=x-1;
			block_row_end=x+1;
		}
		
		if (y==0 || y==3 || y==6)
		{
			block_col_start=y;
			block_col_end=y+3-1;
		}
		else if (y==2 || y==5 || y==8)//At the end of a block
		{
			block_col_start=y-3+1; //both bounds are inclusive
			block_col_end=y;
		}
		else if (y==1 || y==4 || y==7)
		{
			block_col_start=y-1;
			block_col_end=y+1;
		}
		//Established the bounds of the block based on the current position
		for (int i=block_row_start;i<=block_row_end;i++)
		{
			for (int j=block_col_start;j<=block_col_end;j++)
			{
				if (puzzle[i][j]==curr_value)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	
	public static int find_num_givens(String difficulty_level)
	{
		/* Returns a random number which specifies the number of given clues in the Sodoku
		 * based on the desired difficulty level.
		 * ASSUMPTION: The user enters 1, 2 or 3.
		 */
		Random r=new Random();
		try
		{
		switch(Integer.parseInt(difficulty_level))
		{
		case 1:return r.nextInt(50-36) + 36;
		case 2:return r.nextInt(36-32) + 32;
		case 3:return r.nextInt(31-28) + 28;
		default:System.out.println("Invalid Input");
		return 0;
		}
		}
		catch (NumberFormatException e1)
		{
			System.out.println("Invalid Input");
		}
		return 0;
	}

}