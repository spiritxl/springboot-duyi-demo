package com.test.pack1;

import lombok.Getter;

/**
 * 脱敏类型枚举
 */
@Getter
public enum SensitivityTypeEnum {

  /**
   * 姓名
   */
  NAME,
 
  /**
   * 身份证号
   */
  ID_CARD,

  /**
   * 邮箱
   */
  EMAIL,
 
  /**
   * 手机号
   */
  PHONE,

  /**
   *  自定义（此项需设置脱敏的前置后置长度）
   */
  CUSTOMER,
}
