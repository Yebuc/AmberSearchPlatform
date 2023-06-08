package com.yupi.springbootinit.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Amber
 * @create 2023-06-08 17:18
 * 图片服务实现类
 */
@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;//动态变换查询的参数
        String url = String.format("https://cn.bing.com/images/search?q=%s&first=%s",searchText,current);//%s为动态占位符
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据获取异常");//处理异常
        }
//        System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv");
        List<Picture> pictureList = new ArrayList<>();
        for (Element element:elements) {

            //取图片地址（murl）
            String m = element.select(".iusc").get(0).attr("m");
            Map<String,Object> map = JSONUtil.toBean(m,Map.class);
            String murl = (String) map.get("murl");
            //取标题
            String title = element.select(".inflnk").get(0).attr("aria-label");

//            System.out.println(murl);
//            System.out.println(title);
//            System.out.println(element);
            //封装进picture对象
            //TODO 这里也需要做一下去重，待扩展
            Picture picture = new Picture();
            picture.setUrl(murl);
            picture.setTitle(title);

            pictureList.add(picture);
            if (pictureList.size() >= pageSize){
                break;
            }


        }
//        System.out.println(pictureList);
        //用page封装picture列表，因为展示结果需要分页
        Page<Picture> picturePage = new Page<>(pageNum, pageSize);
        picturePage.setRecords(pictureList);

        return picturePage;
    }
}
