package fuga.app.invaderdetector.components.detector;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import fuga.app.invaderdetector.components.loader.MatrixLoaderComponent;
import fuga.app.invaderdetector.components.printer.ResultPrinter;
import fuga.app.invaderdetector.components.transformer.FileToMatrixTransformer;
import fuga.app.invaderdetector.model.DetectionResult;
import fuga.app.invaderdetector.model.Matrix;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class SpaceInvaderDetectorTest {

	private static final String ABSOLUTE_PATH = System.getProperty("user.dir");

	private static final String RADAR1_PATH = ABSOLUTE_PATH + "/src/test/resources/files/radarimages/radar1.txt";
	private static final String RADAR2_PATH = ABSOLUTE_PATH + "/src/test/resources/files/radarimages/radar2.txt";

	private static final String INVADER1_PATH = ABSOLUTE_PATH + "/src/test/resources/files/invaders/invader1.txt";
	private static final String INVADER2_PATH = ABSOLUTE_PATH + "/src/test/resources/files/invaders/invader2.txt";


	private static final float FULL_MATCH_PERCENTAGE = 100.0f;

	private static final List<String> invader1FileLines = Arrays.asList(
		"ooooooooooo",
		"o-ooooooo-o",
		"o-o-----o-o",
		"---oo-oo---"
	);

	private static final char[][] invader1MatrixElements = {
		{'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o'},
		{'o', '-', 'o', 'o', 'o', 'o', 'o', 'o', 'o', '-', 'o'},
		{'o', '-', 'o', '-', '-', '-', '-', '-', 'o', '-', 'o'},
		{'-', '-', '-', 'o', 'o', '-', 'o', 'o', '-', '-', '-'}
	};

	private static final List<String> radar1FileLines = Arrays.asList(
		"oooooooooooooooooooooooooooooooooooooooooooo",
		"o-ooooooo-oo-ooooooo-oo-ooooooo-oo-ooooooo-o",
		"o-o-----o-oo-o-----o-oo-o-----o-oo-o-----o-o",
		"---oo-oo------oo-oo------oo-oo------oo-oo---"
	);

	private static final char[][] radar1MatrixElements = {
		{'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o', 'o'},
		{'o', '-', 'o', 'o', 'o', 'o', 'o', 'o', 'o', '-', 'o', 'o', '-', 'o', 'o', 'o', 'o', 'o', 'o', 'o', '-', 'o', 'o', '-', 'o', 'o', 'o', 'o', 'o', 'o', 'o', '-', 'o', 'o', '-', 'o', 'o', 'o', 'o', 'o', 'o', 'o', '-', 'o'},
		{'o', '-', 'o', '-', '-', '-', '-', '-', 'o', '-', 'o', 'o', '-', 'o', '-', '-', '-', '-', '-', 'o', '-', 'o', 'o', '-', 'o', '-', '-', '-', '-', '-', 'o', '-', 'o', 'o', '-', 'o', '-', '-', '-', '-', '-', 'o', '-', 'o'},
		{'-', '-', '-', 'o', 'o', '-', 'o', 'o', '-', '-', '-', '-', '-', '-', 'o', 'o', '-', 'o', 'o', '-', '-', '-', '-', '-', '-', 'o', 'o', '-', 'o', 'o', '-', '-', '-', '-', '-', '-', 'o', 'o', '-', 'o', 'o', '-', '-', '-'}
	};


	private static final List<String> invader2FileLines = Arrays.asList(
		"-o",
		"o-"
	);

	private static final char[][] invader2MatrixElements = {
		{'-', 'o'},
		{'o', '-'}
	};

	private static final List<String> radar2FileLines = Arrays.asList(
		"oo-o---o-o",
		"o-o-o-o---"
	);

	private static final char[][] radar2MatrixElements = {
		{'o', 'o', '-', 'o', '-', '-', '-', 'o', '-', 'o'},
		{'o', '-', 'o', '-', 'o', '-', 'o', '-', '-', '-'}
	};

	@Captor
	private ArgumentCaptor<List<DetectionResult>> argumentCaptor;


	@Mock
	private ResultPrinter resultPrinter;

	@Mock
	private MatrixLoaderComponent matrixLoaderComponent;

	@Mock
	private FileToMatrixTransformer fileToMatrixTransformer;

	@InjectMocks
	private SpaceInvaderDetector spaceInvaderDetector;


	@Test
	public void testFullMatchFourTimes() {

		when(matrixLoaderComponent.loadFromFile(INVADER1_PATH)).thenReturn(invader1FileLines);
		when(matrixLoaderComponent.loadFromFile(RADAR1_PATH)).thenReturn(radar1FileLines);

		final Matrix invader1Matrix = Matrix.builder().elements(invader1MatrixElements).width(invader1MatrixElements[0].length).height(invader1MatrixElements.length).build();
		final Matrix radarMatrix = Matrix.builder().elements(radar1MatrixElements).width(radar1MatrixElements[0].length).height(radar1MatrixElements.length).build();

		when(fileToMatrixTransformer.transform(invader1FileLines)).thenReturn(invader1Matrix);
		when(fileToMatrixTransformer.transform(radar1FileLines)).thenReturn(radarMatrix);

		spaceInvaderDetector.detect(INVADER1_PATH, RADAR1_PATH, FULL_MATCH_PERCENTAGE);

		verify(resultPrinter).print(argumentCaptor.capture(), eq(radarMatrix), eq(invader1Matrix));

		final List<DetectionResult> detectionResults = argumentCaptor.getValue();

		assertEquals(4, detectionResults.size());
		assertTrue(detectionResults.get(0).getMatchingPercentage() == FULL_MATCH_PERCENTAGE);
		assertEquals(0, detectionResults.get(0).getNoise());
		assertEquals(0, detectionResults.get(0).getJunk());
	}

	@Test
	public void testNoise() {
		when(matrixLoaderComponent.loadFromFile(INVADER2_PATH)).thenReturn(invader2FileLines);
		when(matrixLoaderComponent.loadFromFile(RADAR2_PATH)).thenReturn(radar2FileLines);

		final Matrix invader2Matrix = Matrix.builder().elements(invader2MatrixElements).width(invader2MatrixElements[0].length).height(invader2MatrixElements.length).build();
		final Matrix radarMatrix = Matrix.builder().elements(radar2MatrixElements).width(radar2MatrixElements[0].length).height(radar2MatrixElements.length).build();

		when(fileToMatrixTransformer.transform(invader2FileLines)).thenReturn(invader2Matrix);
		when(fileToMatrixTransformer.transform(radar2FileLines)).thenReturn(radarMatrix);

		spaceInvaderDetector.detect(INVADER2_PATH, RADAR2_PATH, FULL_MATCH_PERCENTAGE);

		verify(resultPrinter).print(argumentCaptor.capture(), eq(radarMatrix), eq(invader2Matrix));

		final List<DetectionResult> detectionResults = argumentCaptor.getValue();

		assertTrue(detectionResults.size() == 3);
		assertEquals(1, detectionResults.get(0).getNoise());
	}

}
