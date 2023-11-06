package com.test.pack1;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
public class SysUser extends BaseEntity {


    private static final long serialVersionUID = 1L;
    /** 用户账号 */
    private String userName;

    /** 用户昵称 */
    private String nickName;

    /** 用户邮箱 */
    @SensitivityEncrypt(type = SensitivityTypeEnum.EMAIL)
    private String email;

    /** 手机号码 */
    @SensitivityEncrypt(type = SensitivityTypeEnum.PHONE)
    private String phoneNumber;

    /** 用户性别 */
    private String sex;

    /** 用户头像 */
    private String avatar;


}
