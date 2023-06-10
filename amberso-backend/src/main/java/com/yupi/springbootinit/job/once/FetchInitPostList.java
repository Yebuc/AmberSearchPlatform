package com.yupi.springbootinit.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.springbootinit.esdao.PostEsDao;
import com.yupi.springbootinit.model.dto.post.PostEsDTO;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.Assertions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
//import org.junit.jupiter.api.Assertions;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取初始帖子列表
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// todo 取消注释后，每次启动一次spring boot会执行一次run方法
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;

    @Resource
    private PostEsDao postEsDao;

    @Override
    public void run(String... args) {
        //1.获取数据
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";//所需的参数格式
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result2 = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
//        System.out.println(result2);//返回得到的是json数据
        //2.json 转对象
        Map<String,Object> map = JSONUtil.toBean(result2, Map.class);
        //todo 先校验得到的返回数据中，code是否为0，判断是否请求成功！ 后期可扩展
        JSONObject data = (JSONObject) map.get("data");//解析数据
        JSONArray records = (JSONArray) data.get("records");//解析数据
        List<Post> postList = new ArrayList<>();
        for (Object record: records) {//解析数据格式
            //TODO 每个取值的过程中，需要判断每个值是否为空，如果为空则会赋值失败出现错误，所以应该后期扩展解决隐患！！
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            //TODO 如果抓取的数据已经在数据库中了，这里可以处理一下，首先判断一下抓取的content是否已经存在数据库中，若已经存在数据库中，则不将其加入postList列表中即不再存入数据库---一定要在第一位置判断！！！  已经实现
            QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("content",tempRecord.getStr("content"));
            Long count = postService.getBaseMapper().selectCount(queryWrapper);

            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));//转换为String类型

            post.setUserId(1L);
            if(count == 0){//如果数据库中没有重复
                postList.add(post);
            }



        }
//        System.out.println(postList);
//        System.out.println(records);
//        System.out.println(map);

        //3.将抓取的数据写入数据库等存储
        boolean b = postService.saveBatch(postList);//批量插入
//        Assertions.assertTrue(b);
        if(b){
            log.info("FetchInitPostList初始化帖子列表成功,本次获取条数 = {}",postList.size());
        }else{
            log.error("初始化FetchInitPostList获取帖子失败，请检查单次定时任务状态");
        }

    }
}
