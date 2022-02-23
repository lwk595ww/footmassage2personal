package com.seetech.footmassage2.core.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 分页返回参数标准
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasePageRes<T> implements Serializable {

    private static final long serialVersionUID = -6478947219913646207L;
    /**
     * 当前页
     */
    private Long page;

    /**
     * 总页数
     */
    private Long totalPage;

    /**
     * 总记录条数
     */
    private long records;

    /**
     * 返回内容
     */
    private List<T> rows;

}
