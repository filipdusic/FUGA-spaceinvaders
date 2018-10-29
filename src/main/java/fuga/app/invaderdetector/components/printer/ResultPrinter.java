package fuga.app.invaderdetector.components.printer;

import java.util.List;

import org.springframework.stereotype.Component;

import fuga.app.invaderdetector.model.DetectionResult;
import fuga.app.invaderdetector.model.Matrix;

/**
 * Class used to print detection process results
 */
@Component
public class ResultPrinter {

	private static final String ANSI_YELLOW = "\u001B[33m";
	private static final String ANSI_WHITE = "\u001B[37m";
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_CYAN = "\u001B[36m";

	/**
	 * Printing results to console output
	 * @param detectionResults List of results of detection process
	 * @param radarMatrix Radar Matrix, loaded from radar image file
	 * @param invaderMatrix, Space Invader Matrix, loaded from invader image file
	 */
	public void print(final List<DetectionResult> detectionResults, final Matrix radarMatrix, final Matrix invaderMatrix) {

		System.out.println();
		System.out.println(ANSI_CYAN + "Space invader image:");
		for (int i = 0; i < invaderMatrix.getHeight(); i++) {
			System.out.println();
			for (int j = 0; j < invaderMatrix.getWidth(); j++) {
				System.out.print(ANSI_RESET + invaderMatrix.getElements()[i][j]);
			}
		}
		System.out.println();
		System.out.println();
		System.out.println(ANSI_CYAN + "Matching results highlighted on radar image:");

		detectionResults.forEach(detectionResult -> {
			System.out.println(ANSI_RESET + detectionResult);
			for (int i = 0; i < radarMatrix.getHeight(); i++) {
				System.out.println();
				for (int j = 0; j < radarMatrix.getWidth(); j++) {

					if (i >= detectionResult.getRowStart() && i <= detectionResult.getRowEnd()
						&& j >= detectionResult.getColumnStart() && j <= detectionResult.getColumnEnd()) {
						System.out.print(ANSI_YELLOW + radarMatrix.getElements()[i][j]);
					} else {
						System.out.print(ANSI_WHITE + radarMatrix.getElements()[i][j]);
					}
				}
			}

			System.out.println();
			System.out.println();
			System.out.println(ANSI_RESET + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		});
	}
}
