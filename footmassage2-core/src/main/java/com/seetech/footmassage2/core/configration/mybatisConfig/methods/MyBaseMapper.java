package com.seetech.footmassage2.core.configration.mybatisConfig.methods;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface MyBaseMapper<T> extends BaseMapper<T> {


    int insertAllBatch(List<T> list);


}
