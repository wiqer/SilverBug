package io.github.wiqer.bug.recommend.gateway;

import io.github.wiqer.bug.recommend.dto.ItemModel;
import io.github.wiqer.bug.recommend.dto.UserModel;
import io.github.wiqer.bug.recommend.dto.RelateModel;

import java.util.List;

/**
 * @author tarzan
 */
public interface FileDataSource {


    /**
     * 方法描述: 读取基础数据
     *
     * @Return {@link List< RelateModel >}
     * @author tarzan
     * @date 2020年07月31日 16:53:40
     */
    List<RelateModel> getData() ;
    /**
     * 方法描述: 读取用户数据
     *
     * @Return {@link List< UserModel >}
     * @author tarzan
     * @date 2020年07月31日 16:54:51
     */
    List<UserModel> getUserData() ;


    /**
     * 方法描述: 读取电影数据
     *
     * @Return {@link List< ItemModel >}
     * @author tarzan
     * @date 2020年07月31日 16:54:22
     */
    List<ItemModel> getItemData() ;


}

