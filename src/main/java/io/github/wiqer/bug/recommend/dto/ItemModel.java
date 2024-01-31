package io.github.wiqer.bug.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 业务项
 *
 * @author TARZAN
 * @version 1.0
 * @date 2020/7/31$ 15:02$
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemModel {
    /** 主键 */
    private Integer id;
    /** 名称 */
    private String name;
    /** 日期 */
    private String date;
    /** 链接 */
    private String link;

}
