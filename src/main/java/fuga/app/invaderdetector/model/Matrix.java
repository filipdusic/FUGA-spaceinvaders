package fuga.app.invaderdetector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Class represents Matrix, 2D array read from file
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Matrix {

	private int width;
	private int height;
	private char[][] elements;


	public void setElement(int posX, int posY, char value) {
		elements[posX][posY] = value;
	}

}
