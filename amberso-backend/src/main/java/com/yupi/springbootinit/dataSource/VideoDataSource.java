package com.yupi.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

/**
 * @author Amber
 * @create 2023-06-12 22:12
 * 还需要一个service层来对接数据参数----适配器模式
 */
@Slf4j
@Service
public class VideoDataSource implements DataSource<T>{
    @Override
    public Page doSearch(String searchText, long pageNum, long pageSize) {
        return null;
    }
}
