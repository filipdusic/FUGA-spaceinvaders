package fuga.app.invaderdetector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class represents result of space invader detection process
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectionResult {

	private String segment;
	private String description;
	private int noise;
	private int junk;
	private float matchingPercentage;

	private int rowStart;
	private int rowEnd;
	private int columnStart;
	private int columnEnd;


	@Override
	public String toString() {
		return "\n" +
			   "Segment: " + segment + "\n" +
			   "Description: " + description + "\n" +
			   "Matching percentage: " + String.format("%.2f", matchingPercentage) + "%\n" +
			   "Noise: " + noise + "\n" +
			   "Junk: " + junk + "\n";
	}
}
