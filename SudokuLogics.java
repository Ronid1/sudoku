import java.util.Random;

/*
 * SudokuLogics represents a sudoku board of size*size and implements all of the games rules
 */

public class SudokuLogics {
	
	private int board[][];
	private int size;
	private int blocks[][];
	private int blockSize;
	private static final int MIN_PRESET_CELLS = 8;
	
	public SudokuLogics(int i)
	{
		size = i;
		board = new int[size][size];
		blocks = new int[size][size];
		blockSize = (int) Math.sqrt(size);
		setBlocks();
	}
	
	//check if given number in location (r,c) is between 1-9 & in a legal location on the board
	public boolean legalNumber(int num, int r, int c)
	{
		if (num < 1 || num > 9) //number must be 1-9
			return false;
		
		//check if number already exists in column
		for (int i = 0; i < size; i++)
		{
			if (i == r) // skip location (r,c)
				continue;
			
			if (board[i][c] == num)
				return false;
		}
		
		//check if number already exists in row
		for (int i = 0; i < size; i++)
		{
			if (i == c) // skip location (r,c)
				continue;
			
			if (board[r][i] == num)
				return false;
		}
		
		//check if number already exists in block
		int blockNum = getBlock(r,c);
		int up = r - (r % blockSize);
		int left = c -(c % blockSize);
		
		for (int i = up; i < up + blockSize; i++)
		{
			for (int j = left; j < left + blockSize; j++)
			{
				if (i == r && j == c)
					continue;
				
				else if (blocks[i][j] == blockNum && board[i][j] == num)
					return false;	
				
			}
		}
		
		return true;
	}
	
	//add a number to the board in location r,c
	public void addNumber(int num, int r, int c)
	{
		if (r < size && c < size)
			board[r][c] = num;
	}

	//get value from cell
	public int getValue (int i, int j)
	{
		return board[i][j];
	}
	
	//set block number
	public void setBlocks()
	{
		int blockNum = -1;
		
		for (int i = 0; i < size; i++)
		{
			if (i % blockSize != 0 ) //same block as last Row
				blockNum -= blockSize;
			
			for (int j = 0; j < size; j++)
			{
				if (j % blockSize == 0)
					blockNum++;

				blocks[i][j] = blockNum;
				
			}
		}
	}
	
	//return block number of current location
	public int getBlock(int i, int j)
	{
		return blocks[i][j];
	}
	
	//return size of a block
	public int getBlockSize()
	{
		return blockSize;
	}
	
	//build a new random board
	public void presetBoard()
	{
		Random random = new Random();
		int maxPresetCells =  random.nextInt(size*size);
		while (maxPresetCells < MIN_PRESET_CELLS)
			maxPresetCells =  random.nextInt(size*size);
		
		int row, column, number;
		
		for (int i = 0 ;  i < maxPresetCells; i++)
		{
			row = random.nextInt(size);
			column = random.nextInt(size);
			//if this cell has already been set, skip it
			if (getValue(row, column) != 0)
				continue;
			
			number = random.nextInt(size);
			//if the number and location are legal, add to board
			if (legalNumber(number, row, column))
				addNumber(number, row, column);
		}
	}
}
