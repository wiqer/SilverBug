package io.github.wiqer.bug.statistics;

import java.util.Arrays;
import java.util.Comparator;

/**
 * ：Gini
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 21 / 2024/2/21  10:27
 * @description：
 * @modified By：
 */
public class Gini {

    public static final double GoldenRatio = 1.618;


    public static void main(String[] args) {
        // 基尼系数
        double G = 0.86;
        // 总的年收入（亿元）
        double totalIncome = 1210207;
        // 总人口数量
        double totalPopulation = 14e8;
        acquiringClassDisparities(totalPopulation, totalIncome, G);

    }

    private static void acquiringClassDisparities(double totalPopulation, double totalIncome, double G) {
        // 每个阶层间的收入差距
        double incomeRatio = GoldenRatio;
        // 阶层数量
        int numClasses = 3;
        while (true){
            
            // 计算每个阶层的年收入
            double[] classIncomes = new double[numClasses];
            classIncomes[0] = 1.0;
            for (int k = 1; k < numClasses; k++) {
                classIncomes[k] = classIncomes[k -1] * incomeRatio;
            }

            // 计算每个阶层的人口数量
            Double[] classPopulations = new Double[numClasses];
            Double totalClassPopulations = (double) 0;
            for (int i = 0; i < numClasses; i++) {
                classPopulations[i] = Math.abs(totalPopulation * Math.pow(1 - incomeRatio, numClasses - i - 1)) ;
                totalClassPopulations += classPopulations[i];
            }
            // 调整人口数量，确保不超过总人口数量
            double scale = totalPopulation / totalClassPopulations;
            for (int i = 0; i < numClasses; i++) {
                classPopulations[i] *= scale;
            }
            // 将double数组转换为Double对象数组
            Double[] doubleArr = new Double[classIncomes.length];
            for (int i = 0; i < classIncomes.length; i++) {
                doubleArr[i] = classIncomes[i];
            }

            // 使用自定义的Comparator对象进行逆序排序
            Arrays.sort(doubleArr, new Comparator<Double>() {
                @Override
                public int compare(Double o1, Double o2) {
                    // 逆序排序
                    return Double.compare(o2, o1);
                }
            });
            Arrays.sort(doubleArr, Comparator.reverseOrder());
            for (int i = 0; i < classIncomes.length; i++) {
                classIncomes[i] = doubleArr[i];
            }
            for (int i = 0; i < classIncomes.length; i++) {
                classIncomes[i]  =  classIncomes[i] * classPopulations[i];
            }
            //抽象总年收入
            double sum = 0.0;
            for (Double income : classIncomes) {
                sum += income;
            }
            //抽象总年收入比
            double scaleIncomes = totalIncome * 10000 * 10000 / sum;

            for (int i = 0; i < classIncomes.length; i++) {
                classIncomes[i]  =  classIncomes[i] * scaleIncomes;
            }
            double[] weights = new double[numClasses];
            for (int i = 0; i < numClasses; i++) {
                weights[i] = classPopulations[i]/ totalPopulation;
            }
            double GINI = calculateWeightedGini(classIncomes,weights);
            if(G > GINI){
                numClasses ++;
                continue;
            }
            // 打印每个阶层的年收入
            System.out.println("每个阶层的年收入：");
            for (int i = 0; i < numClasses; i++) {
                System.out.printf("阶层%d: %.2f 元%n", i + 1, classIncomes[i]);
            }

            // 打印每个阶层的人口数量
            System.out.println("\n每个阶层的人口数量：");
            for (int i = 0; i < numClasses; i++) {
                System.out.printf("阶层%d: %.0f 人%n", i + 1, classPopulations[i]);
            }
            System.out.println("\n每个阶层的人口年收入：");
            for (int i = 0; i < numClasses; i++) {
                System.out.printf("阶层%d: %.0f 元%n", i + 1, classIncomes[i]/classPopulations[i]  );
            }
            System.out.println("\nGINI：" + GINI);
            break;
        }
    }

    private static double calculateWeightedGini(double[] data, double[] weights) {
        // 对数据和权重进行排序
        sortDataAndWeights(data, weights);

        // 计算带权重的基尼系数的分子
        double n = data.length;
        double sum = 0;
        for (int i = 1; i <= data.length; i++) {
            sum += (2 * i - n - 1) * data[i - 1] * weights[i - 1];
        }

        // 计算带权重的基尼系数的分母
        double denominator = Arrays.stream(data).sum() * Arrays.stream(weights).sum();

        // 计算带权重的基尼系数
        double weightedGiniCoefficient = sum / denominator;

        return weightedGiniCoefficient;
    }

    // 对数据和权重进行排序的方法
    private static void sortDataAndWeights(double[] data, double[] weights) {
        // 创建一个二维数组，用于保存数据和对应的权重
        double[][] pairs = new double[data.length][2];
        for (int i = 0; i < data.length; i++) {
            pairs[i][0] = data[i];
            pairs[i][1] = weights[i];
        }

        // 对二维数组按照数据进行排序
        Arrays.sort(pairs, (a, b) -> Double.compare(a[0], b[0]));

        // 将排序后的数据和权重重新赋值给原数组
        for (int i = 0; i < data.length; i++) {
            data[i] = pairs[i][0];
            weights[i] = pairs[i][1];
        }
    }
}
