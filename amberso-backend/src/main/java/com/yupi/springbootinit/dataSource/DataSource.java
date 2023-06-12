package com.yupi.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 定义一个统一的接口规范----来规定新接入的数据源应该以怎么样的方式接入系统
 * @author Amber
 * @create 2023-06-12 21:17
 */
public interface DataSource <T> {

    /*
    * 搜索----后面每一个接入本系统的数据源，都必须实现这个接口以及接口中的方法
    * */
    Page<T> doSearch(String searchText,long pageNum,long pageSize);
}
