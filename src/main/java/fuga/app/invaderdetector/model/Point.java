package fuga.app.invaderdetector.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Class represents points, X, Y coordinates
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Point {
	private int x;
	private int y;
}
