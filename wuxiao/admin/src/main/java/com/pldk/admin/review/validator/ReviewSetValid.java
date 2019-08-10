package com.pldk.admin.review.validator;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.Digits;

/**
 * @author Pldk
 * @date 2019/07/12
 */
@Data
public class ReviewSetValid implements Serializable {
    @Digits(integer = 12, fraction = 2, message = "权重最低分不是数字")
    private Integer wiLowest;
    @Digits(integer = 12, fraction = 2, message = "权重最高分不是数字")
    private Integer wlHighest;
}