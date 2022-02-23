package com.seetech.footmassage2.core.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasePageDto implements Serializable {
    private static final long serialVersionUID = -6759765862745068056L;

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
}
