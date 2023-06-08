package com.yupi.springbootinit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.springbootinit.config.WxOpenConfig;
import javax.annotation.Resource;

import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 主类测试
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@SpringBootTest
class MainApplicationTests {

    @Resource
    private WxOpenConfig wxOpenConfig;
    @Resource
    private PostService postService;

    @Test
    void contextLoads() {
        System.out.println(wxOpenConfig);
    }
    @Test
    void testMybitsPlus(){
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id","166600428141473");
        Post post = postService.getBaseMapper().selectOne(queryWrapper);
        System.out.println(post);
    }

}
