package io.github.wiqer.bug.recommend.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ：ItemContentBasedFiltering
 *
 * @author ：李岚峰、lilanfeng、
 * @device name ：user
 * @date ：Created in 26 / 2024/1/26  14:55
 * @description：
 * @modified By：
 */
public class ItemContentBasedFiltering {

    public static List<Integer> recommendItems(int userId, int topN, Map<Integer, Map<Integer, Double>> userPreferences, Map<Integer, Map<Integer, Double>> itemSimilarities) {
        // 获取用户的偏好模型
        Map<Integer, Double> preferences = userPreferences.get(userId);

        // 保存物品的推荐得分
        Map<Integer, Double> itemScores = new HashMap<>();

        // 遍历用户的偏好模型
        for (Map.Entry<Integer, Double> entry : preferences.entrySet()) {
            int itemId = entry.getKey();
            double preference = entry.getValue();

            // 获取与物品相似的物品及相似度
            Map<Integer, Double> similarItems = itemSimilarities.get(itemId);

            // 计算推荐得分
            for (Map.Entry<Integer, Double> similarEntry : similarItems.entrySet()) {
                int similarItemId = similarEntry.getKey();
                double similarity = similarEntry.getValue();

                if (!preferences.containsKey(similarItemId)) {
                    double score = preference * similarity;

                    // 累加推荐得分
                    itemScores.put(similarItemId, itemScores.getOrDefault(similarItemId, 0.0) + score);
                }
            }
        }

        // 根据推荐得分降序排序
        List<Map.Entry<Integer, Double>> sortedScores = new ArrayList<>(itemScores.entrySet());
        sortedScores.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // 获取Top N推荐物品
        List<Integer> recommendedItems = new ArrayList<>(sortedScores.size());
        for (int i = 0; i < topN && i < sortedScores.size(); i++) {
            recommendedItems.add(sortedScores.get(i).getKey());
        }

        return recommendedItems;
    }
    public static Map<Integer, Map<Integer, Double>> calculateItemSimilarities(Map<Integer, Map<Integer, Double>> itemFeatures) {
        Map<Integer, Map<Integer, Double>> itemSimilarities = new HashMap<>();

        for (int itemId1 : itemFeatures.keySet()) {
            for (int itemId2 : itemFeatures.keySet()) {
                if (itemId1 != itemId2) {
                    double similarity = calculateCosineSimilarity(itemFeatures.get(itemId1), itemFeatures.get(itemId2));
                    // 将相似度存储到结果Map中
                    itemSimilarities.computeIfAbsent(itemId1, k -> new HashMap<>()).put(itemId2, similarity);
                }
            }
        }

        return itemSimilarities;
    }

    public static double calculateCosineSimilarity(Map<Integer, Double> vector1, Map<Integer, Double> vector2) {
        // 计算向量的点积
        double dotProduct = calculateDotProduct(vector1, vector2);

        // 计算向量的范数（模）
        double normVector1 = calculateVectorNorm(vector1);
        double normVector2 = calculateVectorNorm(vector2);

        // 计算余弦相似度
        if (normVector1 > 0 && normVector2 > 0) {
            return dotProduct / (normVector1 * normVector2);
        } else {
            return 0.0;  // 避免除以零的情况
        }
    }

    // 计算向量的点积
    private static double calculateDotProduct(Map<Integer, Double> vector1, Map<Integer, Double> vector2) {
        double dotProduct = 0.0;

        for (int featureIndex : vector1.keySet()) {
            if (vector2.containsKey(featureIndex)) {
                dotProduct += vector1.get(featureIndex) * vector2.get(featureIndex);
            }
        }

        return dotProduct;
    }

    // 计算向量的范数（模）
    private static double calculateVectorNorm(Map<Integer, Double> vector) {
        double norm = 0.0;

        for (double value : vector.values()) {
            norm += Math.pow(value, 2);
        }

        return Math.sqrt(norm);
    }
    // 构建用户偏好模型的示例实现
    public static Map<Integer, Map<Integer, Double>> buildUserPreferences(
            Map<Integer, Map<Integer, Double>> userRatings,
            Map<Integer, Map<Integer, Double>> itemSimilarities) {

        Map<Integer, Map<Integer, Double>> userPreferences = new HashMap<>();

        // 遍历用户评分数据
        for (int userId : userRatings.keySet()) {
            Map<Integer, Double> preferences = new HashMap<>();

            // 遍历用户评分的物品
            for (int itemId : userRatings.get(userId).keySet()) {
                // 获取物品相似度信息
                Map<Integer, Double> similarItems = itemSimilarities.get(itemId);

                // 遍历与当前物品相似的物品
                for (int similarItemId : similarItems.keySet()) {
                    // 如果用户没有对相似物品评分过，则考虑相似度加权
                    if (!userRatings.get(userId).containsKey(similarItemId)) {
                        double similarity = similarItems.get(similarItemId);
                        double rating = userRatings.get(userId).get(itemId);

                        // 使用相似度加权更新用户对相似物品的偏好
                        preferences.merge(similarItemId, similarity * rating, Double::sum);
                    }
                }
            }

            userPreferences.put(userId, preferences);
        }

        return userPreferences;
    }

}
