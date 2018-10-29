package fuga.app.invaderdetector.components.loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Class used to load file
 */
@Component
@Slf4j
public class MatrixLoaderComponent {

	/**
	 * Load file by given file path
	 * @param filePath String representing base location of image (invader or radar)
	 * @return Lines from file
	 */
	public List<String> loadFromFile(final String filePath) {

		try {
			log.info("Loading file: {}", filePath);
			return Files.readAllLines(new File(filePath).toPath());
		} catch (IOException e) {
			log.error("Error while reading file: {}", filePath);
		}

		return Collections.emptyList();
	}
}
