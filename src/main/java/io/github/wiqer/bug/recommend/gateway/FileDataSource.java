package io.github.wiqer.bug.recommend.gateway;

import io.github.wiqer.bug.recommend.dto.ItemDTO;
import io.github.wiqer.bug.recommend.dto.UserDTO;
import io.github.wiqer.bug.recommend.dto.RelateDTO;

import java.util.List;

/**
 * @author tarzan
 */
public interface FileDataSource {


    /**
     * 方法描述: 读取基础数据
     *
     * @Return {@link List<RelateDTO>}
     * @author tarzan
     * @date 2020年07月31日 16:53:40
     */
    List<RelateDTO> getData() ;
    /**
     * 方法描述: 读取用户数据
     *
     * @Return {@link List<UserDTO>}
     * @author tarzan
     * @date 2020年07月31日 16:54:51
     */
    List<UserDTO> getUserData() ;


    /**
     * 方法描述: 读取电影数据
     *
     * @Return {@link List<ItemDTO>}
     * @author tarzan
     * @date 2020年07月31日 16:54:22
     */
    List<ItemDTO> getItemData() ;


}

