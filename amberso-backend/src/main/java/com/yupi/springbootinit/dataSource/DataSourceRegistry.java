package com.yupi.springbootinit.dataSource;

import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Amber
 * @create 2023-06-13 15:00
 */
@Component
public class DataSourceRegistry {
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PictureDataSource pictureDataSource;

    private  Map<String, DataSource<T>> typeDataSourceMap;

    @PostConstruct//该注解在所有bean注册完成之后，会首先调用该方法,是一个方法注解     也可以使用静态代码块来替代
    public void doInit(){
        typeDataSourceMap = new HashMap(){{//注册器模式----也可以理解成一种单例模式
            put(SearchTypeEnum.POST.getValue(),postDataSource);
            put(SearchTypeEnum.USER.getValue(),userDataSource);
            put(SearchTypeEnum.PICTURE.getValue(),pictureDataSource);
        }};
    }

    public DataSource getDataSourceByType(String type){
        if(typeDataSourceMap == null){//保证系统初始化的时候被人调用了有出错误
            return null;
        }
        return typeDataSourceMap.get(type);
    }

}
