import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;

import javax.swing.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();

        main.run();

        main.runTask2();
        main.runTask3();
        main.runTask4();
    }

    private void run() {
    }

    private void runTask2() {
        System.out.println("TASK 2");
        System.out.println("======");
        System.out.println();
        BigInteger m = BigInteger.valueOf(5702347);
        ArrayList<BigInteger> consecutiveValuesInSequence = new ArrayList<>();

        BigInteger a = BigInteger.valueOf(234135);
        BigInteger c = BigInteger.valueOf(8298345);
        BigInteger val1 = generateNextValue(BigInteger.valueOf(97812341), a, c, m);
        BigInteger val2 = generateNextValue(val1, a, c, m);
        BigInteger val3 = generateNextValue(val2, a, c, m);
        BigInteger val4 = generateNextValue(val3, a, c, m);
        consecutiveValuesInSequence.add(val1);
        consecutiveValuesInSequence.add(val2);
        consecutiveValuesInSequence.add(val3);
        consecutiveValuesInSequence.add(val4);
        HashMap<String, BigInteger> aAndC = this.findAAndC(m, consecutiveValuesInSequence);

        System.out.println("a = " + aAndC.get("a"));
        System.out.println("c = " + aAndC.get("c"));
    }

    private void runTask3() {
        System.out.println("TASK 3");
        System.out.println("======");
        System.out.println();
        BigInteger x = BigInteger.valueOf(100);
        BigInteger sequenceLength = BigInteger.valueOf(255);
        BigInteger a = BigInteger.valueOf(93);
        BigInteger c = BigInteger.valueOf(137);
        BigInteger m = BigInteger.valueOf(256);

        HashMap<BigInteger, BigInteger> sequence = this.generateSequence(x, sequenceLength, a, c, m);

        double[] xData = new double[128];
        double[] yData = new double[128];

        for (int i = 0; i < 127; i++) {
            BigInteger k = BigInteger.valueOf(2 * i);
            BigInteger k2 = BigInteger.valueOf(2 * i + 1);
            xData[i] = sequence.get(k).doubleValue();
            yData[i] = sequence.get(k2).doubleValue();
        }

        this.plotChart(xData, yData);
    }

    private void runTask4() {
        System.out.println("TASK 4");
        System.out.println("======");
        System.out.println();
        BigInteger x = BigInteger.valueOf(1111);
        BigInteger sequenceLength = BigInteger.valueOf(5);
        BigInteger a = BigInteger.valueOf(12345);
        BigInteger c = BigInteger.valueOf(12345);
        BigInteger m = BigInteger.valueOf(27182819);

        ArrayList<BigInteger> sequence = this.generateSequenceAsList(x, sequenceLength, a, c, m);

        System.out.println("Variant 1 (gdc):");
        System.out.println("----------");
        this.checkGCDForAllParallelogramAreas(sequence);
        System.out.println();
        System.out.println("Variant 2 (divisors):");
        System.out.println("----------");
        this.checkAllDivisorsForParallelogramArea(this.gcdForParallelogramAreas(getParallelogramAreasForSequence(sequence)), sequence);
    }

    private void checkGCDForAllParallelogramAreas(ArrayList<BigInteger> sequence) {
        BigInteger generatedM = gcdForParallelogramAreas(getParallelogramAreasForSequence(sequence));
        ArrayList<BigInteger> filteredSequence = this.filterSequenceValues(generatedM, sequence);
        HashMap<String, BigInteger> aAndC = this.findAAndC(generatedM, filteredSequence);

        System.out.println("m = " + generatedM);
        System.out.println("a = " + aAndC.get("a"));
        System.out.println("c = " + aAndC.get("c"));
    }

    private void checkAllDivisorsForParallelogramArea(BigInteger area, ArrayList<BigInteger> sequence) {
        ArrayList<BigInteger> divisors = this.getDivisors(area);

        for (BigInteger divisor : divisors) {
            ArrayList<BigInteger> filteredSequence = this.filterSequenceValues(divisor, sequence);
            HashMap<String, BigInteger> aAndC = this.findAAndC(divisor, filteredSequence);

            ArrayList<BigInteger> generatedSequence = this.generateSequenceAsList(sequence.get(0), BigInteger.valueOf(sequence.size()), aAndC.get("a"), aAndC.get("c"), divisor);

            if (generatedSequence.equals(sequence)) {
                System.out.println("One possible solution is:");
                System.out.println("m = " + divisor);
                System.out.println("a = " + aAndC.get("a"));
                System.out.println("c = " + aAndC.get("c"));
                System.out.println();
            }
        }
    }

    private BigInteger gcdForParallelogramAreas(HashSet<BigInteger> parallelogramAreas) {
        BigInteger gcd = null;
        for (BigInteger area : parallelogramAreas) {
            gcd = area.gcd(gcd == null ? area : gcd);
        }
        return gcd;
    }

    private HashSet<BigInteger> getParallelogramAreasForSequence(ArrayList<BigInteger> sequence) {
        HashSet<BigInteger> areas = new HashSet<>();

        for (int origin = 0; origin < sequence.size() - 1; origin++) {
            for (int i = 0; i < sequence.size() - 1; i++) {
                for (int j = 0; j < sequence.size() - 1; j++) {
                    if (origin == i || i == j || origin == j) {
                        continue;
                    }

                    BigInteger x0 = sequence.get(origin);
                    BigInteger x1 = sequence.get(origin + 1);
                    BigInteger xI = sequence.get(i);
                    BigInteger xI2 = sequence.get(i + 1);
                    BigInteger xJ = sequence.get(j);
                    BigInteger xJ2 = sequence.get(j + 1);

                    areas.add(this.calculateParallelogramArea(x0, x1, xI, xI2, xJ, xJ2));
                }
            }
        }

        return areas;
    }

    private BigInteger calculateParallelogramArea(BigInteger x0, BigInteger x1, BigInteger xI, BigInteger xI2, BigInteger xJ, BigInteger xJ2) {
        return xI.subtract(x0).multiply(xJ2.subtract(x1)).subtract(xJ.subtract(x0).multiply(xI2.subtract(x1))).abs();
    }

    private ArrayList<BigInteger> getDivisors(BigInteger x) {
        ArrayList<BigInteger> results = new ArrayList<>();

        for (int i = 1; i <= x.intValue(); i++) {
            if (x.mod(BigInteger.valueOf(i)).equals(BigInteger.ZERO)) {
                results.add(BigInteger.valueOf(i));

            }
        }
        return results;
    }


    private void plotChart(double[] xData, double[] yData) {
        final XYChart chart = new XYChartBuilder().width(1200).height(800).xAxisTitle("X").yAxisTitle("Y").build();
        final JFrame frame = new JFrame("Plotted chart");

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        chart.addSeries("a", xData, yData);


        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                JPanel chartPanel = new XChartPanel<>(chart);
                frame.add(chartPanel);

                frame.pack();
                frame.setVisible(true);
            }
        });
    }


    private HashMap<String, BigInteger> findAAndC(BigInteger m, ArrayList<BigInteger> consecutiveValuesInSequence) {
        ArrayList<BigInteger> filterSequenceValues = filterSequenceValues(m, consecutiveValuesInSequence);
        BigInteger x1 = filterSequenceValues.get(0);
        BigInteger x2 = filterSequenceValues.get(1);
        BigInteger x3 = filterSequenceValues.get(2);

        BigInteger a = x2.subtract(x1).modInverse(m).multiply(x3.subtract(x2)).mod(m);
        BigInteger c = x2.subtract(a.multiply(x1)).mod(m);

        HashMap<String, BigInteger> result = new HashMap<>();
        result.put("a", a);
        result.put("c", c);

        return result;
    }

    private ArrayList<BigInteger> generateSequenceAsList(BigInteger x, BigInteger sequenceLength, BigInteger a, BigInteger c, BigInteger m) {
        HashMap<BigInteger, BigInteger> sequence = this.generateSequence(x, sequenceLength, a, c, m);
        ArrayList<BigInteger> sequenceAsList = new ArrayList<>();
        for (int i = 0; i < sequence.size(); i++) {
            sequenceAsList.add(sequence.get(BigInteger.valueOf(i)));
        }
        return sequenceAsList;
    }

    private HashMap<BigInteger, BigInteger> generateSequence(BigInteger x, BigInteger sequenceLength, BigInteger a, BigInteger c, BigInteger m) {
        HashMap<BigInteger, BigInteger> results = new HashMap<>();
        BigInteger currentX = x;

        for (int i = 0; i < sequenceLength.intValue(); i++) {
            results.put(BigInteger.valueOf(i), currentX);
            currentX = this.generateNextValue(currentX, a, c, m);
        }

        return results;
    }


    private BigInteger generateNextValue(BigInteger x, BigInteger a, BigInteger c, BigInteger m) {
        return a.multiply(x).add(c).mod(m);
    }


    private ArrayList<BigInteger> filterSequenceValues(BigInteger m, ArrayList<BigInteger> consecutiveValuesInSequence) throws ValueException {
        for (int i = 0; i < consecutiveValuesInSequence.size() - 2; i++) {
            BigInteger x1 = consecutiveValuesInSequence.get(i);
            BigInteger x2 = consecutiveValuesInSequence.get(i + 1);
            BigInteger x3 = consecutiveValuesInSequence.get(i + 2);

            if (areRelativelyPrime(x2.subtract(x1), m)) {
                ArrayList<BigInteger> filteredSequenceValues = new ArrayList<>();
                filteredSequenceValues.add(x1);
                filteredSequenceValues.add(x2);
                filteredSequenceValues.add(x3);
                return filteredSequenceValues;
            }
        }
        throw new ValueException("The sequence is missing values for calculating a and c.");
    }

    private boolean areRelativelyPrime(BigInteger x, BigInteger m) {
        return m.gcd(x).intValue() == 1;
    }

}
