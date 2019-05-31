package tictactoe;

import java.io.Serializable;
import java.util.Scanner;

public class Tictactoe implements Serializable {

	private static final long serialVersionUID = 1L;

	private char tabuleiro[][];
	
	private boolean hasWinner;

	public Tictactoe() {
		this.tabuleiro = new char[3][3];		
		
		hasWinner = false;
	}

	public void makeaPlay(char play) {
		boolean canExit = false;
		Integer column;
		Integer row;
		do {
			row = playRow();
			column = playColumn();

			if (this.tabuleiro[row][column] == 0) {
				this.tabuleiro[row][column] = play;
				canExit = true;
			} else {
				System.out.println("O local escohido é inválido, favor jogue novamente");
			}
		} while (!canExit);
		checkWinner(row, column, play);
	}

	public boolean hasWinner() {
		return this.hasWinner;
	}
	
	private Integer playRow() {
		Scanner entrada = new Scanner(System.in);
		Integer row;
		do {
			System.out.println("Escolha a linha que deseja jogar: ");
			row = entrada.nextInt();
			if (row < 1 || row > 3) {
				System.out.println("Por favor escolha um número entre 1 e 3");
			}
		} while (row < 1 || row > 3);
		row--;
		return row;
	}

	private Integer playColumn() {
		Scanner entrada = new Scanner(System.in);
		Integer column = null;
		do {
			System.out.println("Escolha a coluna que deseja jogar: ");
			column = entrada.nextInt();
			if (column < 1 || column > 3) {
				System.out.println("Por favor escolha um número entre 1 e 3");
			}
		} while (column < 1 || column > 3);
		column--;
		return column;
	}

	private void checkWinner(int row, int column, char play) {
		if (this.checkRow(row, play) || this.checkColumn(column, play) || this.checkDiagonal(play)) {
			hasWinner= true;
		}
	}

	private Boolean checkRow(int row, char play) {
		for (int i = 0; i < 3; i++) {
			if (this.tabuleiro[row][i] != play) {
				return false;
			}
		}
		return true;
	}

	private Boolean checkColumn(int column, char play) {
		for (int i = 0; i < 3; i++) {
			if (this.tabuleiro[i][column] != play) {
				return false;
			}
		}
		return true;
	}

	private Boolean checkDiagonal(char play) {
		if (this.tabuleiro[0][0] == play && this.tabuleiro[1][1] == play && this.tabuleiro[2][2] == play) {
			return true;
		} else if (this.tabuleiro[0][2] == play && this.tabuleiro[1][1] == play && this.tabuleiro[2][0] == play) {
			return true;
		}
		return false;
	}

	public void printMatrix() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(" | ");
				System.out.print(" " + this.tabuleiro[i][j]);
			}
			System.out.print(" | ");
			System.out.println();
		}
	}
}
