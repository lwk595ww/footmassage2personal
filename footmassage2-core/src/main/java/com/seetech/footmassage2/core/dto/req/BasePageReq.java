package com.seetech.footmassage2.core.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分页属性
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasePageReq implements Serializable {

    private static final long serialVersionUID = -1215305737793693817L;
    /**
     * 请求页
     */
    @ApiModelProperty("当前页")
    private Integer page;

    /**
     * 每页行数
     */
    @ApiModelProperty("每页行数")
    private Integer rows;

    /**
     * 关键字
     */
    @ApiModelProperty("关键字查询 特定多个字段模糊查询")
    private String searchText;

    /**
     * 排序字段，以及排序类型(code desc,name desc)
     * ，多个字段排序用,隔开
     */
    @ApiModelProperty("自定义字段排序")
    private String orderBy;
}
