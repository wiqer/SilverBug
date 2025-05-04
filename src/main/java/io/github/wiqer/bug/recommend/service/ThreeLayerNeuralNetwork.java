package io.github.wiqer.bug.recommend.service;

import io.github.wiqer.bug.recommend.core.NeuralNetworkFiltering;

/**
 * ：ThreeLayerNeuralNetwork
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 31 / 2024/1/31  13:13
 * @description：
 * @modified By：
 */
public class ThreeLayerNeuralNetwork {

    private NeuralNetworkFiltering hiddenLayer1;
    private NeuralNetworkFiltering hiddenLayer2;
    private NeuralNetworkFiltering outputLayer;

    public ThreeLayerNeuralNetwork(int inputSize,int outputSize1,int outputSize2, int outputSize, int hiddenSize1, int hiddenSize2, int outputHiddenSize, double learningRate) {
        if(outputSize1 > hiddenSize2){
            throw new RuntimeException("outputSize1 > hiddenSize2");
        }
        if(outputSize1 > outputHiddenSize){
            throw new RuntimeException("outputSize2 > outputHiddenSize");
        }
        this.hiddenLayer1 = new NeuralNetworkFiltering(inputSize, hiddenSize1, outputSize1, learningRate);
        this.hiddenLayer2 = new NeuralNetworkFiltering(outputSize1, hiddenSize2, outputSize2, learningRate);
        this.outputLayer = new NeuralNetworkFiltering(outputSize2, outputHiddenSize, outputSize, learningRate);
    }

    public double[] forwardPropagation(double[] inputs) {
        double[] hiddenLayerOutputs1 = hiddenLayer1.forwardPropagation(inputs);
        double[] hiddenLayerOutputs2 = hiddenLayer2.forwardPropagation(hiddenLayerOutputs1);
        double[] outputLayerOutputs = outputLayer.forwardPropagation(hiddenLayerOutputs2);

        return outputLayerOutputs;
    }

    public void train(double[] inputs, double[] targets) {
        double[] hiddenLayerOutputs1 = hiddenLayer1.forwardPropagation(inputs);
        double[] hiddenLayerOutputs2 = hiddenLayer2.forwardPropagation(hiddenLayerOutputs1);
        double[] outputLayerOutputs = outputLayer.forwardPropagation(hiddenLayerOutputs2);

        // 反向传播更新权重
        outputLayer.train(hiddenLayerOutputs2, targets);
        hiddenLayer2.train(hiddenLayerOutputs1, outputLayer.getErrorsPro(hiddenLayerOutputs2, targets));
        hiddenLayer1.train(inputs, hiddenLayer2.getErrorsPro(hiddenLayerOutputs1, targets));
    }

    public static void main(String[] args) {
        // 示例用法
        // 创建一个具有2个输入节点、3个隐藏节点1、2个隐藏节点2、1个输出节点的三层神经网络
        ThreeLayerNeuralNetwork neuralNetwork = new ThreeLayerNeuralNetwork(4, 4,2, 2 ,4,4, 4,0.1);

        // 训练数据
        double[][] trainingData = {
                {0, 6, 6, 0, 0.8, 0.1},
                {0, 6, 6, 1, 0.4, 0.4},
                {0, 6, 6, 1, 0.4, 0.4},
                {5, 6, 6, 1, 0.7, 0.8},
                {3, 6, 6, 1, 0.5, 0.9},
                {1, 6, 6, 0, 0.1, 0.8},
                {0, 6, 6, 1, 0.4, 0.4},
                {1, 6, 6, 1, 0.2, 0.9}
        };

        // 进行训练
        for (int epoch = 0; epoch < 10000; epoch++) {
            for (double[] data : trainingData) {
                double[] inputs = {data[0], data[1], data[2], data[3]};
                double[] targets = {data[4],data[5]};
                neuralNetwork.train(inputs, targets);
            }
        }

        long time = System.currentTimeMillis();
        // 进行预测
        double[] inputs = {0, 6, 6, 1};
        double[] outputs = neuralNetwork.forwardPropagation(inputs);
        double[] inputs2 = {5, 6, 6, 1};
        double[] outputs2 = neuralNetwork.forwardPropagation(inputs);
        double[] inputs3 = {3, 6, 6, 1};
        double[] outputs3 = neuralNetwork.forwardPropagation(inputs);
        System.out.println("Predicted output: " + outputs[0] +"，Predicted output2: " + outputs2[0]+"，Predicted output3: " + outputs3[0] + ". used time: " + (System.currentTimeMillis() - time));
        System.out.println("Predicted output: " + outputs[1] +"，Predicted output2: " + outputs2[1]+"，Predicted output3: " + outputs3[1] + ". used time: " + (System.currentTimeMillis() - time));
    }
}
