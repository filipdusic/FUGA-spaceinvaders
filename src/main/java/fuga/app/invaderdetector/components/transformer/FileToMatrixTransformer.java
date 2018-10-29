package fuga.app.invaderdetector.components.transformer;

import java.util.List;

import org.springframework.stereotype.Component;

import fuga.app.invaderdetector.model.Matrix;
import lombok.extern.slf4j.Slf4j;

/**
 * Class used to transform file to 2D matrix
 */
@Component
@Slf4j
public class FileToMatrixTransformer {

	/**
	 * Transforms lines from file to Matrix object
	 * @param rows Lines from file
	 * @return Matrix object
	 */
	public Matrix transform(final List<String> rows) {

		log.info("Transforming file content to Matrix");

		final Matrix matrix = Matrix.builder().build();

		if (rows.isEmpty()) {
			//If there are no rows in file, return empty matrix
			matrix.setHeight(0);
			matrix.setWidth(0);
			return matrix;
		}

		//Rows are present, build matrix object
		matrix.setWidth(rows.get(0).length());
		matrix.setHeight(rows.size());
		matrix.setElements(new char[rows.size()][rows.get(0).length()]);

		for (int i = 0; i < rows.size(); i++) {
			for (int j = 0; j < rows.get(i).length(); j++) {
				matrix.setElement(i, j, rows.get(i).charAt(j));
			}
		}

		return matrix;
	}
}
