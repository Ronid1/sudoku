/*
 * Visual implication of a sudoku board
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board implements ActionListener{
	
	//visual components
	private JFrame frame = new JFrame();
	private JPanel board;
	private JPanel buttons = new JPanel();
	private JButton cells[][];
	private JButton cmdSet, cmdClear, cmdNew;
	
	private SudokuLogics logics;
	
	private int rowSize;
	private boolean doneSetting = false; //indicate if set button was pressed
	private boolean[][] setBoard; //indicate if a cell is set
	
	public Board(int size)
	{			
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		rowSize = size;
		cells = new JButton [rowSize][rowSize];
		logics = new SudokuLogics(rowSize);
		board = new JPanel(new GridLayout(rowSize, rowSize));
		setBoard = new boolean[size][size];
		
	}//end of contractor
	
	//set a new board
	public void newBoard()
	{
		//add buttons
		cmdSet = new JButton ("set");
		cmdSet.addActionListener(this);
		buttons.add(cmdSet);
		
		cmdClear = new JButton ("clear");
		cmdClear.addActionListener(this);
		buttons.add(cmdClear);
		
		cmdNew = new JButton ("new game");
		cmdNew.addActionListener(this);
		buttons.add(cmdNew);
		
		//add board and buttons to jframe
		frame.add(board,BorderLayout.CENTER);
		frame.add(buttons,BorderLayout.SOUTH);
		
		//set background color
		Color background;
		int curBlock; //current block
		
		for (int i = 0; i < rowSize; i++)
		{			
			for (int j = 0; j < rowSize; j++)
			{
				curBlock = logics.getBlock(i, j);

				//add cell to board
				cells[i][j] = new JButton();
				board.add(cells[i][j]);
				cells[i][j].addActionListener(this);
				
				if (curBlock % 2 == 0) //even block
					background = Color.LIGHT_GRAY;
				
				else //odd block
					background = Color.WHITE;
				
				cells[i][j].setBackground(background); //set background color

			}
		}	
		
		frame.pack();
		frame.setVisible(true);
	}
		
	
	//add a number from user input to cell[r][c]
	public void setCell(int r, int c)
	{
		JPanel dialogBox = new JPanel();
		JTextField input = new JTextField(5);
		String value; int result = -1; int num = -1;
		
		dialogBox.add(new JLabel ("Enter value"));
		dialogBox.add(input);
		
		result = JOptionPane.showConfirmDialog(null, dialogBox, "", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION)
		{
			value = input.getText();
			if (!legalNum(value))
			{
				JOptionPane.showMessageDialog(frame, "Illegal value");
				return;
			}
			
				num = Integer.valueOf(value);
			
			if (!logics.legalNumber(num, r, c))
				JOptionPane.showMessageDialog(frame, "Illegal value");
			
			else
				logics.addNumber(num, r, c);
				setText();
		}
	}
	
	
	//check if a string is a legal number
	public boolean legalNum(String s)
	{
		int num;
		for (int i = 0 ; i < s.length(); i++)
			if (!Character.isDigit(s.charAt(i)))
					return false;
		
		num = Integer.valueOf(s);
		if (num < 0 || num > rowSize)
			return false;
		
		return true;
	}
	
	
	//sets text on board
	public void setText()
	{
		int value;
		
		for (int i = 0; i < rowSize; i++)
		{
			for (int j = 0; j < rowSize; j++)
			{
				value = logics.getValue(i,j);
				
				if (value != 0)
					cells[i][j].setText(String.valueOf(value));
				
				else
					cells[i][j].setText("");
			}
		}
	}
	
	
	//if set button pressed - 
	public void set()
	{
		//board had already been set
		if (doneSetting == true)
		{
			JOptionPane.showMessageDialog(frame, "sudoku board has already been set.");
			return;
		}
		
		doneSetting = true;
		
		for (int i = 0; i < rowSize; i++)
		{
			for (int j = 0; j < rowSize; j++)
			{
				if (logics.getValue(i, j) != 0)
				{
					setBoard[i][j] = true; //mark set cell
					cells[i][j].setForeground(Color.RED); //change text color to red
				}
			}
		}
	}
	
	
	//if clear button pressed
	public void clear()
	{
		//if board is set only clear unset cells
		if (doneSetting == true)
		{
			for (int i = 0; i < rowSize; i++)
			{
				for (int j = 0; j < rowSize; j++)
				{
					if (setBoard[i][j] == false)
						logics.addNumber(0,i,j);
				}
			
			}
		}
		
		//if board isn't set, clear all cells
		else {
			for (int i = 0; i < rowSize; i++)
			{
				for (int j = 0; j < rowSize; j++)
				{
					logics.addNumber(0,i,j);
					
					if (setBoard[i][j] == true) //reset cell
					{
						cells[i][j].setForeground(Color.BLACK);
						setBoard[i][j] = false;
					}
				}
			}
		}
		
		setText();
	}
	
	//if new game button pressed - delete all board
	public void newGame()
	{
		doneSetting = false;
		for (int i = 0; i < rowSize; i++)
		{
			for (int j = 0; j < rowSize; j++)
			{
				logics.addNumber(0,i,j);
				
				if (setBoard[i][j] = true) //reset cell
				{
					cells[i][j].setForeground(Color.BLACK);
					setBoard[i][j] = false;
				}
			}
		}
		
		setText();
	}


	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		System.out.println(source);
		
		if (source == cmdSet)
			set();
		
		else if (source == cmdClear)
			clear();
		
		else if (source == cmdNew)
			newGame();
		
		else //a cell was selected
		{
			int row = -1, column = -1;
			
			//find button location
			for (int i = 0; i < rowSize; i++)
			{
				for (int j = 0; j < rowSize; j++)
				{
					System.out.println("cells["+i+"]["+j+"]=" + cells[i][j]);
					if (source == cells[i][j])
					{
						row = i; column = j;
					}
				}
			}
			
			//if cell has been set at beginning of game
			if (doneSetting == true && setBoard[row][column] == true)
			{	
				JOptionPane.showMessageDialog(frame, "can't change set cell");
				return;
			}

			setCell(row, column);			
		}
	}
}