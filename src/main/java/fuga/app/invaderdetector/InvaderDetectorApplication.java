package fuga.app.invaderdetector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import fuga.app.invaderdetector.components.detector.SpaceInvaderDetector;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@SpringBootApplication
@Slf4j
public class InvaderDetectorApplication implements CommandLineRunner {

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(InvaderDetectorApplication.class, args);
	}

	@Override
	public void run(final String... args) {

		float percentage;

		if (args.length == 0) {
			log.info("Input arguments are empty. Using default value {}%", environment.getProperty("defaultPercentage"));
			percentage = Float.parseFloat(environment.getProperty("defaultPercentage"));
		} else {
			final String requiredPercentageString = args[0];

			try {
				percentage = Float.parseFloat(requiredPercentageString);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Invalid input argument");
			}

			if (percentage > 100.0f || percentage < 0.0f) {
				throw new RuntimeException("Invalid matching percentage");
			}
		}

		SpaceInvaderDetector spaceInvaderDetector = context.getBean(SpaceInvaderDetector.class);
		spaceInvaderDetector.detect(getClass().getClassLoader().getResource(environment.getProperty("invader1ImagePath")).getPath(), getClass().getClassLoader().getResource(environment.getProperty("radarImagePath")).getPath(), percentage);
		spaceInvaderDetector.detect(getClass().getClassLoader().getResource(environment.getProperty("invader2ImagePath")).getPath(), getClass().getClassLoader().getResource(environment.getProperty("radarImagePath")).getPath(), percentage);
	}
}
