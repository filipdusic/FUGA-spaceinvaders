package fuga.app.invaderdetector.components.detector;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import fuga.app.invaderdetector.components.loader.MatrixLoaderComponent;
import fuga.app.invaderdetector.components.printer.ResultPrinter;
import fuga.app.invaderdetector.components.transformer.FileToMatrixTransformer;
import fuga.app.invaderdetector.model.DetectionResult;
import fuga.app.invaderdetector.model.Matrix;
import fuga.app.invaderdetector.model.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Class used to detect space invaders in radar image
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SpaceInvaderDetector {

	private final MatrixLoaderComponent matrixLoaderComponent;
	private final FileToMatrixTransformer fileToMatrixTransformer;
	private final ResultPrinter resultPrinter;

	private static final char MATCHING_CHAR = 'o';

	/**
	 * Initialization of invader and radar matrices
	 * @param invaderFilePath String representing path to invader image file
	 * @param radarFilePath String representing path to radar image file
	 * @param requiredPercentage Minimum required match percentage
	 */
	public void detect(final String invaderFilePath, final String radarFilePath, final float requiredPercentage) {
		final Matrix radarMatrix = fileToMatrixTransformer.transform(matrixLoaderComponent.loadFromFile(radarFilePath));
		final Matrix invaderMatrix = fileToMatrixTransformer.transform(matrixLoaderComponent.loadFromFile(invaderFilePath));

		log.info("Start processing Matrices");
		final List<DetectionResult> detectionResults = processImages(invaderMatrix, radarMatrix, requiredPercentage);
		resultPrinter.print(detectionResults, radarMatrix, invaderMatrix);
	}

	/**
	 * Comparing two matrix objects. Detection of invader matrix inside radar matrix with given required match percentage
	 * @param invaderMatrix Invader matrix object
	 * @param radarMatrix Radar matrix object
	 * @param requiredPercentage Minimum required match percentage
	 * @return List of DetectionResult objects
	 */
	private List<DetectionResult> processImages(final Matrix invaderMatrix, final Matrix radarMatrix, final float requiredPercentage) {
		final List<DetectionResult> results = new ArrayList<>();

		IntStream.range(0, radarMatrix.getHeight() - invaderMatrix.getHeight() + 1).forEach(radarHeight -> {
			IntStream.range(0, radarMatrix.getWidth() - invaderMatrix.getWidth() + 1).forEach(radarWidth -> {

				final AtomicInteger exactMatches = new AtomicInteger(0);
				final AtomicInteger invaderOnly = new AtomicInteger(0);

				final AtomicInteger noise = new AtomicInteger(0);
				final AtomicInteger junk = new AtomicInteger(0);

				final List<Point> radarOnlyPoints = new ArrayList<>();

				IntStream.range(0, invaderMatrix.getHeight()).forEach(invaderHeight -> {
					IntStream.range(0, invaderMatrix.getWidth()).forEach(invaderWidth -> {
						final Character radarMatrixElement = radarMatrix.getElements()[radarHeight + invaderHeight][radarWidth + invaderWidth];
						final Character invaderMatrixElement = invaderMatrix.getElements()[invaderHeight][invaderWidth];

						if (radarMatrixElement.equals(MATCHING_CHAR) && invaderMatrixElement.equals(MATCHING_CHAR)) {
							exactMatches.getAndIncrement();
						} else if (radarMatrixElement.equals(MATCHING_CHAR)) {
							radarOnlyPoints.add(Point.builder().x(invaderHeight).y(invaderWidth).build());
						} else if (invaderMatrixElement.equals(MATCHING_CHAR)) {
							invaderOnly.getAndIncrement();
						}
					});
				});

				final int totalInvaders = invaderOnly.addAndGet(exactMatches.get());
				float percentage = 0;

				if (totalInvaders > 0) {
					percentage = (exactMatches.get() * 100.0f) / totalInvaders;
				}

				if (percentage >= requiredPercentage) {
					//Found matching invader
					//Check if 'o' characters on radar image sub-matrix represent noise or junk
					radarOnlyPoints.forEach(point -> {
						if (isNoise(invaderMatrix, point.getX(), point.getY())) {
							noise.getAndIncrement();
						} else {
							junk.getAndIncrement();
						}
					});

					results.add(DetectionResult.builder()
						.segment(String.format("row: [%2d, %2d], column: [%2d, %2d]", radarHeight, radarHeight + invaderMatrix.getHeight() - 1, radarWidth, radarWidth + invaderMatrix.getWidth() - 1))
						.matchingPercentage(percentage)
						.rowStart(radarHeight)
						.rowEnd(radarHeight + invaderMatrix.getHeight() - 1)
						.columnStart(radarWidth).columnEnd(radarWidth + invaderMatrix.getWidth() - 1)
						.description(String.format("Found matching invader in radar sub-matrix with matching accuracy of %.2f%%", percentage))
						.noise(noise.get())
						.junk(junk.get())
						.build());
				}

			});
		});

		return results;
	}

	/**
	 * Checks if 'o' found in sub-matrix of radar image is noise
	 * @param invaderMatrix Matrix object representing space invader matrix
	 * @param x X coordinate of 'o' character found on radar image rescaled to invader matrix coordinate
	 * @param y Y coordinate of 'o' character found on radar image rescaled to invader matrix coordinate
	 * @return True if 'o' is noise, False if 'o' is junk
	 */
	private boolean isNoise(final Matrix invaderMatrix, final int x, final int y) {

//		int startPositionX = (x - 1 >= 0) ? x - 1: x;
//		int endPositionX = (x + 1 < invaderMatrix.getHeight()) ? x + 1: x;
//
//		int startPositionY = (y - 1 >= 0) ? y - 1: y;
//		int endPositionY = (y + 1 < invaderMatrix.getWidth()) ? y + 1: y;
//
//
//		for (int rowNum = startPositionX; rowNum <= endPositionX; rowNum++) {
//			for (int colNum = startPositionY; colNum <= endPositionY; colNum++) {
//				if (invaderMatrix.getElements()[rowNum][colNum] == MATCHING_CHAR && rowNum != x && colNum != y) {
//
//				}
//			}
//		}

		//Check if point is not located in last row of matrix
		if (x + 1 < invaderMatrix.getHeight()) {
			//Check element below
			if (invaderMatrix.getElements()[x + 1][y] == MATCHING_CHAR)
				return true;

			//Check element below right
			if (y + 1 < invaderMatrix.getWidth() && invaderMatrix.getElements()[x + 1][y + 1] == MATCHING_CHAR)
				return true;

			//Check element below left
			if (y - 1 >= 0 && invaderMatrix.getElements()[x + 1][y - 1] == MATCHING_CHAR)
				return true;
		}

		//Check if point is not located in first row of matrix
		if (x - 1 >= 0) {
			//Check element above
			if (invaderMatrix.getElements()[x - 1][y] == MATCHING_CHAR)
				return true;

			//Check element above right
			if (y + 1 < invaderMatrix.getWidth() && invaderMatrix.getElements()[x - 1][y + 1] == MATCHING_CHAR)
				return true;

			//Check element above left
			if (y - 1 >= 0 && invaderMatrix.getElements()[x - 1][y - 1] == MATCHING_CHAR)
				return true;
		}

		//Check if point is not located in first column of matrix
		if (y - 1 >= 0) {
			//Check element left
			if (invaderMatrix.getElements()[x][y - 1] == MATCHING_CHAR)
				return true;

			//Check element left bottom
			if (x + 1 < invaderMatrix.getHeight() && invaderMatrix.getElements()[x + 1][y - 1] == MATCHING_CHAR)
				return true;

			//Check element left top
			if (x - 1 >= 0 && invaderMatrix.getElements()[x - 1][y - 1] == MATCHING_CHAR)
				return true;
		}

		//Check if point is not located in last column of matrix
		if (y + 1 < invaderMatrix.getWidth()) {
			//Check element right
			if (invaderMatrix.getElements()[x][y + 1] == MATCHING_CHAR)
				return true;

			//Check element right bottom
			if (x + 1 < invaderMatrix.getHeight() && invaderMatrix.getElements()[x + 1][y + 1] == MATCHING_CHAR)
				return true;

			//Check element right top
			if (x - 1 >= 0 && invaderMatrix.getElements()[x - 1][y + 1] == MATCHING_CHAR)
				return true;
		}

		return false;
	}
}
