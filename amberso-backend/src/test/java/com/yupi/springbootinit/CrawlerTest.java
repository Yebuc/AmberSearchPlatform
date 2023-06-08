package com.yupi.springbootinit;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Amber
 * @create 2023-06-08 11:42
 * 爬虫实践测试
 */
@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;//注意@Autowired和@Resource的区别与联系

    @Test
    void testFetchPicture() throws IOException {//爬的是bing上的在线图片
        int current = 1;
        String url = String.format("https://cn.bing.com/images/search?q=小黑子&form=HDRSC2&cw=1177&ch=922&first=%s",current);
        Document doc = Jsoup.connect(url).get();
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
        }
//        System.out.println(pictureList);
//        Elements newsHeadlines = doc.select("#mp-itn b a");
//        for (Element headline : newsHeadlines) {
//
//        }
    }

    @Test
    void testFetchPassage(){
        //1.获取数据
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result2 = HttpRequest
                .post(url)
                .body(json)
                .execute()
                .body();
//        System.out.println(result2);//返回得到的是json数据
        //2.json 转对象
        Map<String,Object> map = JSONUtil.toBean(result2,Map.class);
        //todo 先校验得到的返回数据中，code是否为0，判断是否请求成功！ 后期可扩展
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record: records) {//解析数据格式
            //TODO 每个取值的过程中，需要判断每个值是否为空，如果为空则会赋值失败出现错误，所以应该后期扩展解决隐患！！
            JSONObject tempRecord = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));//转换为String类型
            post.setUserId(1L);

            postList.add(post);
        }
//        System.out.println(postList);
//        System.out.println(records);
//        System.out.println(map);

        //3.将抓取的数据写入数据库等存储
        boolean b = postService.saveBatch(postList);//批量插入
        Assertions.assertTrue(b);
    }
}
