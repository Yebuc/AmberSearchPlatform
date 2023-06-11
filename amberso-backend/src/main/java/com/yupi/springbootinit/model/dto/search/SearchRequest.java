package com.yupi.springbootinit.model.dto.search;

import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchRequest extends PageRequest implements Serializable {


    /**
     * 搜索词
     */
    private String searchText;

    /*
<<<<<<< HEAD
    * 搜索·类型
=======
    * 类型
    *
>>>>>>> c1a0d7a4fe69eefeb623d23cd849b7628c2769bf
    * */
    private String type;

    private static final long serialVersionUID = 1L;
}