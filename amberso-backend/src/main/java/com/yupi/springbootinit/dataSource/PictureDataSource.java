package com.yupi.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Amber
 * @create 2023-06-08 17:18
 * 图片服务实现类
 */
@Service
@Slf4j
public class PictureDataSource implements DataSource<Picture> {
    @Resource
    private PictureService pictureService;

    @Override
    public Page<Picture> doSearch(String searchText, long pageNum, long pageSize) {
        Page<Picture> picturePage = pictureService.searchPicture(searchText, pageNum, pageSize);

        return picturePage;
    }
}
