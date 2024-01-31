package io.github.wiqer.bug.recommend.core;

import java.util.HashMap;
import java.util.Map;

/**
 * ：MatrixFactorizationFiltering
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 31 / 2024/1/31  11:04
 * @description：
 * @modified By：
 */
public class MatrixFactorizationFiltering {
    private int numUsers;
    private int numItems;
    private int numFactors;

    /**
     * learningRate（学习率）：学习率控制模型在每次迭代中更新参数的步长。
     * 通常情况下，learningRate的取值范围在0.01到0.1之间。
     * 较小的学习率会使收敛速度较慢，但可能会更稳定；
     * 较大的学习率可能会导致模型在训练过程中发散或不稳定。
     * 可以通过尝试不同的学习率，并根据模型的收敛情况来选择合适的值。
     */
    private double learningRate;
    /**
     * regularization（正则化参数）：正则化参数控制模型的复杂度，
     * 用于防止过拟合。通常情况下，regularization的取值范围在0.01到0.1之间。
     * 较小的正则化参数会使模型更容易过拟合，而较大的正则化参数则可能导致模型欠拟合。
     * 可以通过交叉验证等方法来选择合适的正则化参数，以在训练集和测试集上取得较好的性能。
     */
    private double regularization;

    private Map<Integer, double[]> userFactors;
    private Map<Integer, double[]> itemFactors;

    public MatrixFactorizationFiltering(int numUsers, int numItems, int numFactors, double learningRate, double regularization) {
        this.numUsers = numUsers;
        this.numItems = numItems;
        this.numFactors = numFactors;
        this.learningRate = learningRate;
        this.regularization = regularization;

        userFactors = new HashMap<>();
        itemFactors = new HashMap<>();

        // 初始化用户和物品的隐含因子矩阵
        for (int userId = 0; userId < numUsers; userId++) {
            userFactors.put(userId, new double[numFactors]);
        }
        for (int itemId = 0; itemId < numItems; itemId++) {
            itemFactors.put(itemId, new double[numFactors]);
        }
    }

    /**
     *
     * @param userId
     * @param itemId
     * @param rating 在矩阵分解推荐算法中，rating表示用户对物品的评分或者喜好程度。它是一个连续的数值，通常在一个预定义的范围内，
     * 例如1到5之间，表示用户对物品的喜好程度或者对物品的评价。
     * 在训练过程中，我们需要将用户对物品的真实评分与模型预测的评分进行比较，并根据评分误差来更新模型参数。
     * 这样，模型就能够通过不断地调整参数来逼近用户对物品的真实评分。
     * 取值方面，评分的范围可以根据具体的应用场景和数据集来确定。
     * 通常情况下，评分范围取决于用户对物品的评价方式，例如电影评分可以是1到5或者1到10，
     * 产品评价也可以是1到5或者1到7等等。在训练过程中，我们可以将用户对物品的真实评分作为输入，然后根据模型预测的评分与真实评分之间的差异来计算误差，从而进行模型的更新和优化。
     * 需要注意的是，评分数据可能存在一些问题，例如缺失值、异常值或者不均衡的分布等。
     * 在处理这些问题时，可以采用适当的数据清洗和预处理方法，以确保训练数据的质量和可靠性
     */
    public void train(int userId, int itemId, double rating) {
        double[] userFactor = userFactors.get(userId);
        double[] itemFactor = itemFactors.get(itemId);

        double prediction = predict(userId, itemId);
        double error = rating - prediction;

        // 更新用户和物品的隐含因子矩阵
        for (int i = 0; i < numFactors; i++) {
            double userValue = userFactor[i];
            double itemValue = itemFactor[i];

            userFactor[i] += learningRate * (error * itemValue - regularization * userValue);
            itemFactor[i] += learningRate * (error * userValue - regularization * itemValue);
        }
    }

    public double predict(int userId, int itemId) {
        double[] userFactor = userFactors.get(userId);
        double[] itemFactor = itemFactors.get(itemId);

        double prediction = 0.0;
        for (int i = 0; i < numFactors; i++) {
            prediction += userFactor[i] * itemFactor[i];
        }
        return prediction;
    }

    public static void main(String[] args) {
        int numUsers = 10;
        int numItems = 10;
        int numFactors = 5;
        double learningRate = 0.01;
        double regularization = 0.01;

        MatrixFactorizationFiltering mf = new MatrixFactorizationFiltering(numUsers, numItems, numFactors, learningRate, regularization);

        // 训练数据
        mf.train(0, 0, 4.0);
        mf.train(0, 1, 2.0);
        mf.train(1, 0, 3.0);
        mf.train(1, 1, 5.0);

        // 预测评分
        double prediction = mf.predict(2, 1);
        System.out.println("预测评分为: " + prediction);
    }
}
