package io.github.wiqer.bug.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户对象
 *
 * @author TARZAN
 * @version 1.0
 * @company 洛阳图联科技有限公司
 * @copyright (c) 2019 LuoYang TuLian Co'Ltd Inc. All rights reserved.
 * @date 2020/7/31$ 14:55$
 * @since JDK1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {
    /** 主键 */
    private Integer id;
    /** 年纪 */
    private Integer age;
    /** 性别 */
    private String sex;
    /** 职业 */
    private String profession;
    /** 邮编 */
    private String postcode;

}
