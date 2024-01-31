package io.github.wiqer.bug.recommend.service;


import io.github.wiqer.bug.recommend.core.ItemContentBasedFiltering;
import io.github.wiqer.bug.recommend.core.ItemContentFiltering;
import io.github.wiqer.bug.recommend.core.UserContentFiltering;
import io.github.wiqer.bug.recommend.dto.ItemModel;
import io.github.wiqer.bug.recommend.dto.RelateModel;
import io.github.wiqer.bug.recommend.gateway.FileDataSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 推荐服务
 *
 * @author TARZAN
 * @version 1.0
 * @date 2020/7/31$ 16:18$
 * @since JDK1.8
 */
public class Recommend {

    /**
     * 方法描述: 猜你喜欢
     *
     * @param userId 用户id
     * @Return {@link List< ItemModel >}
     * @author tarzan
     * @date 2020年07月31日 17:28:06
     */
    public static List<ItemModel>  userCfRecommend(int userId, FileDataSource fileDataSource){
        List<RelateModel> data= fileDataSource.getData();
        List<Integer> recommendations = UserContentFiltering.recommend(userId, data);
        return fileDataSource.getItemData().stream().filter(e->recommendations.contains(e.getId())).collect(Collectors.toList());
    }


    /**
     * 方法描述: 猜你喜欢
     *
     * @param itemId 物品id
     * @Return {@link List< ItemModel >}
     * @author tarzan
     * @date 2020年07月31日 17:28:06
     */
    public static List<ItemModel>  itemCfRecommend(int itemId, FileDataSource fileDataSource){
        List<RelateModel> data= fileDataSource.getData();
        List<Integer> recommendations = ItemContentFiltering.recommend(itemId, data);
        return fileDataSource.getItemData().stream().filter(e->recommendations.contains(e.getId())).collect(Collectors.toList());
    }

    /**
     * 方法描述:  用户偏好
     * @param userId
     * @param topN
     * @param itemFeatures 物品特征向量化
     * @param userRatings 用户历史评分数据
     * @return
     */
    public static List<Integer>  ItemContentBasedFiltering(int userId, int topN, Map<Integer, Map<Integer, Double>> itemFeatures , Map<Integer, Map<Integer, Double>> userRatings){

        // 计算物品相似度
        Map<Integer, Map<Integer, Double>> itemSimilarities = ItemContentBasedFiltering.calculateItemSimilarities(itemFeatures);


        // 用户偏好模型构建
        Map<Integer, Map<Integer, Double>> userPreferences = ItemContentBasedFiltering.buildUserPreferences(userRatings, itemSimilarities);

        // 相似物品推荐
        List<Integer> recommendedItems = ItemContentBasedFiltering.recommendItems(userId, topN, userPreferences,userPreferences);
        return recommendedItems;
    }


}
