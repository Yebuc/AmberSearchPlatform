package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.search.SearchRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;

//聚合接口
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PostService postService;

    @Resource
    private PictureService pictureService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();


    //聚合搜索实现，一个请求，根据传入参数的不同，可以查询不同的内容
    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest,HttpServletRequest request){
        String searchText = searchRequest.getSearchText();
        String type = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(type);

        //TODO 感觉逻辑有错误
        ThrowUtils.throwIf(StringUtils.isBlank(type),ErrorCode.PARAMS_ERROR);
        if(searchTypeEnum == null){//搜索出所有数据
            //使用异步编排-----并发操作  多线程
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() -> {
                UserQueryRequest userQueryRequest = new UserQueryRequest();//用户参数解析
                userQueryRequest.setUserName(searchText);
                Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                return userVOPage;
            });

            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(() -> {
                PostQueryRequest postQueryRequest = new PostQueryRequest();//帖子参数解析
                postQueryRequest.setSearchText(searchText);
                Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                return postVOPage;
            });

            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(() -> {
                Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);//图片参数解析
                return picturePage;
            });

            CompletableFuture.allOf(userTask,postTask,pictureTask).join();//这行代码相当于打了一个断点，只有在userTask,postTask,pictureTask这三个任务执行完之后才会，接着执行下面的代码
            try{
                Page<UserVO> userVOPage = userTask.get();
                Page<PostVO> postVOPage = postTask.get();
                Page<Picture> picturePage = pictureTask.get();


                SearchVO searchVO = new SearchVO();
                searchVO.setPictureList(picturePage.getRecords());
                searchVO.setPostList(postVOPage.getRecords());
                searchVO.setUserList(userVOPage.getRecords());

                return ResultUtils.success(searchVO);//返回出去

            }catch (Exception e){
                log.error("查询异常",e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
            }

        }else{//根据type类别去查询每种数据
            SearchVO searchVO = new SearchVO();
            switch (searchTypeEnum){
                case POST:
                    PostQueryRequest postQueryRequest = new PostQueryRequest();//帖子参数解析
                    postQueryRequest.setSearchText(searchText);
                    Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                    searchVO.setPostList(postVOPage.getRecords());
                    break;
                case USER:
                    UserQueryRequest userQueryRequest = new UserQueryRequest();//用户参数解析
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                    searchVO.setUserList(userVOPage.getRecords());
                    break;
                case PICTURE:
                    Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);//图片参数解析
                    searchVO.setPictureList(picturePage.getRecords());
                    break;
                default://使用switch一般要使用default来表达规范
            }

            return ResultUtils.success(searchVO);//返回出去

        }


    }

    //todo 根据tab键的点击，每次查询一个请求，以此来减轻网站同一时间发送请求的个数，减少浏览器资源的消耗



}
