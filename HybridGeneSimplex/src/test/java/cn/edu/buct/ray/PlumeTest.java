package cn.edu.buct.ray;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlumeTest {

	private List<Sensor> sensors;

	@Before
	public void generateData() {
		double[][] posOfSensors = { { 120.0, 5.0, 5.0 }, { 120.0, 6.0, 5.0 },
				{ 160.0, 7.0, 5.0 }, { 160.0, 8.0, 5.0 }, { 180.0, 9.0, 5.0 },
				{ 180.0, 10.0, 5.0 }, { 200.0, 11.0, 5.0 },
				{ 200.0, 12.0, 5.0 }, { 200.0, 13.0, 5.0 },
				{ 200.0, 14.0, 5.0 }, { 220.0, 20.0, 5.0 },
				{ 220.0, 23.0, 5.0 }, { 240.0, 24.0, 5.0 },
				{ 240.0, 25.0, 5.0 }, { 260.0, 24.0, 5.0 },
				{ 260.0, 20.0, 5.0 }, { 280.0, 22.0, 5.0 } };
		PGPlumeModel lm1 = new PGPlumeModel(3000, 10, 6.5, 5, 0, 2);
		PGPlumeModel lm2 = new PGPlumeModel(4100, 25, 7.3, 5, 0, 2);
		sensors = new ArrayList<Sensor>();
		for (int i = 0; i < posOfSensors.length; i++) {
			sensors.add(new Sensor(posOfSensors[i][0], posOfSensors[i][1],
					posOfSensors[i][2], lm1.getDensity(posOfSensors[i][0],
							posOfSensors[i][1], posOfSensors[i][2])
							+ lm2.getDensity(posOfSensors[i][0],
									posOfSensors[i][1], posOfSensors[i][2])));
		}
		for (int i = 0; i < sensors.size(); i++) {
			// System.out.println(sensors.get(i).toString());
		}
	}

	@Ignore
	@Test
	public void sigleSourceTest() {
		double[][] posOfSensors = { { 240.0, 5.0, 0.0 }, { 242.0, 10.0, 0.0 },
				{ 244.0, 20.0, 0.0 }, { 246.0, 23.0, 0.0 },
				{ 248.0, 24.0, 0.0 }, { 250.0, 25.0, 0.0 },
				{ 252.0, 24.0, 0.0 }, { 254.0, 20.0, 0.0 },
				{ 256.0, 22.0, 0.0 }, { 258.0, 10.0, 0.0 } };
		PGPlumeModel lm3 = new PGPlumeModel(2000, 15, 10, 5, 0, 2);
		sensors = new ArrayList<Sensor>();
		for (int i = 0; i < posOfSensors.length; i++) {
			sensors.add(new Sensor(posOfSensors[i][0], posOfSensors[i][1],
					posOfSensors[i][2], lm3.getDensity(posOfSensors[i][0],
							posOfSensors[i][1], posOfSensors[i][2])));
		}
		for (int i = 0; i < sensors.size(); i++) {
			// System.out.println(sensors.get(i).toString());
		}
		PlumeChromosome finalPlumeChromosome = new PlumeChromosome(2700, 8, 5,
				sensors, 5, 0, 2);
		long startTime = System.currentTimeMillis();

		System.out.println("Performing genetic algorithm ...");
		GAPlumeSolver gs = new GAPlumeSolver(sensors, 5, 0, 2);
		gs.setMinQ0(20.0);
		gs.setMaxQ0(8000.0);
		gs.setMinY0(2.0);
		gs.setMaxY0(50.0);
		gs.setMinZ0(0);
		gs.setMaxZ0(20.0);
		gs.setStopE(1E-10);
		gs.setCrossOverRate(0.9);
		gs.setMutationRate(0.05);
		gs.setSizePopulation(500);
		gs.setGenerationBound(20);
		finalPlumeChromosome = gs.GASolve();
		System.out
				.println("Result: " + finalPlumeChromosome.toStringWithName());

		System.out.println("Applying Nelder Mead simplex method ...");
		NelderMeadPlumeSolver nms = new NelderMeadPlumeSolver(sensors,
				finalPlumeChromosome.getQ0(), finalPlumeChromosome.getY0(),
				finalPlumeChromosome.getZ0(), 5, 0, 2);
		double[] h1 = { 90, 3, 2 };
		nms.setH(h1);
		nms.setAlfa(1);
		nms.setBeta(0.5);
		nms.setGama(2);
		nms.setDelta(0.5);
		nms.setStopE(1E-10);
		nms.setMaxiIterationsNumber(9999);
		finalPlumeChromosome = nms.NelderMeadSolve();
		System.out
				.println("Result: " + finalPlumeChromosome.toStringWithName());

		long timeConsumed = System.currentTimeMillis() - startTime;
		System.out.println("time consumed:" + Long.toString(timeConsumed, 10)
				+ " (ms)");

	}

	@Ignore
	@Test
	public void sensorTest() {
		PGPlumeModel lm1 = new PGPlumeModel(3000, 10, 6.5, 5, 0, 2);
		PGPlumeModel lm2 = new PGPlumeModel(4100, 25, 7.3, 5, 0, 2);
		for (int i = 0; i < sensors.size(); i++) {
			assertEquals(
					lm1.getDensity(sensors.get(i).getxPos(), sensors.get(i)
							.getyPos(), sensors.get(i).getzPos())
							+ lm2.getDensity(sensors.get(i).getxPos(), sensors
									.get(i).getyPos(), sensors.get(i).getzPos()),
					sensors.get(i).getValueMeasured(), 1E-6);
		}
	}

	@Ignore
	@Test
	public void multiSourceSolverTest() {

		MultiSourcePlumeChromosome finalPlumeChromosome = new MultiSourcePlumeChromosome(
				1000, 10, 8, 6000, 50, 23, sensors, 5, 0, 2);
		long startTime = System.currentTimeMillis();

		System.out.println("Performing genetic algorithm ...");
		MultiSourceGAPlumeSolver gs = new MultiSourceGAPlumeSolver(sensors, 5,
				0, 2);
		gs.setMinQ0(20.0);
		gs.setMaxQ0(6000.0);
		gs.setMinY0(2.0);
		gs.setMaxY0(50.0);
		gs.setMinZ0(4);
		gs.setMaxZ0(20.0);
		gs.setStopE(1E-7);
		gs.setCrossOverRate(0.8);
		gs.setMutationRate(0.05);
		gs.setSizePopulation(500);
		gs.setGenerationBound(100);
		finalPlumeChromosome = gs.GASolve();
		System.out
				.println("Result: " + finalPlumeChromosome.toStringWithName());

		System.out.println("Applying Nelder Mead simplex method ...");
		MultiSourceNelderMeadPlumeSolver nms = new MultiSourceNelderMeadPlumeSolver(
				sensors, finalPlumeChromosome.getQ01(),
				finalPlumeChromosome.getY01(), finalPlumeChromosome.getZ01(),
				finalPlumeChromosome.getQ02(), finalPlumeChromosome.getY02(),
				finalPlumeChromosome.getZ02(), 5, 0, 2);
		double[] h1 = { 11, 0.5, 0.2, 11, 0.5, 0.2 };
		nms.setH(h1);
		nms.setAlfa(1);
		nms.setBeta(0.5);
		nms.setGama(2);
		nms.setDelta(0.5);
		nms.setStopE(1E-9);
		nms.setMaxiIterationsNumber(3999);
		finalPlumeChromosome = nms.NelderMeadSolve();

		System.out
				.println("Result: " + finalPlumeChromosome.toStringWithName());

		long timeConsumed = System.currentTimeMillis() - startTime;
		System.out.println("time consumed:" + Long.toString(timeConsumed, 10)
				+ " (ms)");
	}

}
