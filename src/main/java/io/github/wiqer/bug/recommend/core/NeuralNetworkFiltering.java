package io.github.wiqer.bug.recommend.core;

import java.util.Arrays;
import java.util.Random;

/**
 * ：NeuralNetworkFiltering
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 31 / 2024/1/31  11:46
 * @description：
 * @modified By：
 */
public class NeuralNetworkFiltering {
    private int inputSize;
    private int hiddenSize;
    private int outputSize;

    /**
     * learningRate（学习率）：学习率控制模型在每次迭代中更新参数的步长。
     * 通常情况下，learningRate的取值范围在0.01到0.1之间。
     * 较小的学习率会使收敛速度较慢，但可能会更稳定；
     * 较大的学习率可能会导致模型在训练过程中发散或不稳定。
     * 可以通过尝试不同的学习率，并根据模型的收敛情况来选择合适的值。
     */
    private double learningRate;
    private double[][] weightsInputHidden;

    /**
     * weightsHiddenOutput 是指连接隐藏层和输出层之间的权重矩阵。在神经网络中，每个神经元都与前一层的所有神经元有连接，并且每个连接都有一个对应的权重。
     *
     * 对于一个具有 m 个隐藏层神经元和 n 个输出层神经元的神经网络，weightsHiddenOutput 是一个 n x m 的矩阵。其中，每一列代表一个隐藏层神经元与输出层神经元之间的权重连接。
     *
     * 每个隐藏层神经元与输出层神经元之间的权重连接，决定了隐藏层神经元对输出层神经元的影响程度。权重越大，表示该连接对输出结果的影响越大；权重越小，表示该连接对输出结果的影响越小。
     *
     * 在神经网络的训练过程中，权重矩阵 weightsHiddenOutput 会根据训练数据进行调整，以最小化神经网络的误差。这样，在预测阶段，神经网络就可以使用调整后的权重矩阵来进行准确的预测。
     */
    private double[][] weightsHiddenOutput;
    private Random random;

    private double[] hiddenLayerOutputs;


    public NeuralNetworkFiltering(int inputSize, int hiddenSize, int outputSize, double learningRate) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.learningRate = learningRate;
        this.weightsInputHidden = new double[inputSize][hiddenSize];
        this.weightsHiddenOutput = new double[hiddenSize][outputSize];
        this.random = new Random();

        initializeWeights();
    }

    private void initializeWeights() {
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsInputHidden[i][j] = random.nextDouble() - 0.5;
            }
        }

        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weightsHiddenOutput[i][j] = random.nextDouble() - 0.5;
            }
        }
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public double[] forwardPropagation(double[] inputs) {
        hiddenLayerOutputs = new double[hiddenSize];
        double[] outputLayerOutputs = new double[outputSize];

        for (int i = 0; i < hiddenSize; i++) {
            double sum = 0;
            for (int j = 0; j < inputSize; j++) {
                sum += inputs[j] * weightsInputHidden[j][i];
            }
            hiddenLayerOutputs[i] = sigmoid(sum);
        }

        for (int i = 0; i < outputSize; i++) {
            double sum = 0;
            for (int j = 0; j < hiddenSize; j++) {
                sum += hiddenLayerOutputs[j] * weightsHiddenOutput[j][i];
            }
            outputLayerOutputs[i] = sigmoid(sum);
        }

        return outputLayerOutputs;
    }

    public void train(double[] inputs, double[] targets) {
        double[] hiddenLayerOutputs = new double[hiddenSize];
        double[] outputLayerOutputs = new double[outputSize];

        for (int i = 0; i < hiddenSize; i++) {
            double sum = 0;
            for (int j = 0; j < inputSize; j++) {
                sum += inputs[j] * weightsInputHidden[j][i];
            }
            hiddenLayerOutputs[i] = sigmoid(sum);
        }

        for (int i = 0; i < outputSize; i++) {
            double sum = 0;
            for (int j = 0; j < hiddenSize; j++) {
                sum += hiddenLayerOutputs[j] * weightsHiddenOutput[j][i];
            }
            outputLayerOutputs[i] = sigmoid(sum);
        }

        // 反向传播更新权重
        double[] outputErrors = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            outputErrors[i] = (targets[i] - outputLayerOutputs[i]) * outputLayerOutputs[i] * (1 - outputLayerOutputs[i]);
        }

        double[] hiddenErrors = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            double error = 0;
            for (int j = 0; j < outputSize; j++) {
                error += outputErrors[j] * weightsHiddenOutput[i][j];
            }
            hiddenErrors[i] = error * hiddenLayerOutputs[i] * (1 - hiddenLayerOutputs[i]);
        }

        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < outputSize; j++) {
                weightsHiddenOutput[i][j] += learningRate * outputErrors[j] * hiddenLayerOutputs[i];
            }
        }

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsInputHidden[i][j] += learningRate * hiddenErrors[j] * inputs[i];
            }
        }
    }

    public double[] getErrorsPro(double[] nextLayerErrors, double[] actual) {
        double[] errors = new double[hiddenSize];
        assert weightsHiddenOutput[0] != null;
       int hiddenOutputSize = weightsHiddenOutput[0].length;
        if(nextLayerErrors.length > hiddenOutputSize){
            nextLayerErrors[hiddenOutputSize-1] =  calculateLoss(Arrays.copyOfRange(nextLayerErrors, hiddenOutputSize, nextLayerErrors.length), actual);
        }
        for (int i = 0; i < hiddenSize; i++) {
            double error = 0;
            for (int j = 0; j < hiddenOutputSize; j++) {
                error += nextLayerErrors[j] * weightsHiddenOutput[i][j];
            }
            errors[i] = error * hiddenLayerOutputs[i] * (1 - hiddenLayerOutputs[i]);
        }

        return errors;
    }

    public double[] getSigmoidErrors(double[] nextLayerErrors) {

        double[] errors = new double[hiddenSize];

        for (int i = 0; i < hiddenSize; i++) {
            double error = 0;
            for (int j = 0; j < nextLayerErrors.length; j++) {
                error += nextLayerErrors[j] * weightsHiddenOutput[i][j];
            }

            // Apply the derivative of the activation function
            errors[i] = error * derivativeActivation(hiddenLayerOutputs[i]);
        }

        return errors;
    }

    /**
     * Sigmoid激活函数
     * @param x
     * @return
     */
    public double derivativeActivation(double x) {
        double sigmoid = 1 / (1 + Math.exp(-x));
        return sigmoid * (1 - sigmoid);
    }

    public double derivativeActivationFunction(double output) {
        // 对sigmoid函数的导数进行计算
        return output * (1 - output);
    }

    /**
     * 损失函数 交叉熵损失（Cross-Entropy Loss）
     * @param predicted
     * @param actual
     * @return
     */
    public static double calculateLoss(double[] predicted, double[] actual) {
        double loss = 0.0;

        for (int i = 0; i < predicted.length; i++) {
            loss += actual[i] * Math.log(predicted[i]);
        }

        return -loss;
    }
    public static void main(String[] args) {
        // 示例用法
        // 创建一个具有2个输入节点、2个隐藏节点、1个输出节点的神经网络
        NeuralNetworkFiltering neuralNetwork = new NeuralNetworkFiltering(2, 2, 1, 0.1);

        // 训练数据
        double[][] trainingData = {
                {0, 0, 0},
                {0, 1, 1},
                {1, 0, 1},
                {1, 1, 0}
        };

        // 进行训练
        for (int epoch = 0; epoch < 10000; epoch++) {
            for (double[] data : trainingData) {
                double[] inputs = {data[0], data[1]};
                double[] targets = {data[2]};
                neuralNetwork.train(inputs, targets);
            }
        }

        // 进行预测
        double[] inputs = {0, 1};
        double[] outputs = neuralNetwork.forwardPropagation(inputs);
        System.out.println("Predicted output: " + outputs[0]);
    }
}
