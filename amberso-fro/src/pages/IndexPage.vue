<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchText"
      placeholder="input search text"
      enter-button="Search"
      size="large"
      @search="onSearch"
    />
    <!--    {{ JSON.stringify(postList) }}-->
    <MyDivider />
    <a-tabs v-model:activeKey="activeKey" @change="onTabChange">
      <a-tab-pane key="post" tab="文章">
        <PostList :post-list="postList" />
      </a-tab-pane>
      <a-tab-pane key="picture" tab="图片">
        <PictureList :picture-list="pictureList" />
      </a-tab-pane>
      <a-tab-pane key="user" tab="用户">
        <UserList :user-list="userList" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue";
import PostList from "@/components/PostList.vue";
import PictureList from "@/components/PictureList.vue";
import UserList from "@/components/UserList.vue";
import MyDivider from "@/components/MyDivider.vue";
import { useRoute, useRouter } from "vue-router";
import _default from "ant-design-vue/es/vc-slick/inner-slider";
import data = _default.data;
import MyAxios from "@/plugins/MyAxios";
import { message } from "ant-design-vue";

const pictureList = ref([]);
const postList = ref([]);
const userList = ref([]);

// const searchText = ref("");

const router = useRouter();
const route = useRoute();
const activeKey = route.params.category;

const searchText = ref(route.query.text || "");

const initSearchParams = {
  // text: "",//是否增加？？
  type: activeKey,
  text: "",
  pageSize: 10,
  pageNum: 1,
};
// alert(route.params.category);
const searchParams = ref(initSearchParams);

/*
 * 加载数据第一种版本---以实现点击search就实现查询的功能
 *
 * */
const loadDataOld = (params: any) => {
  const postQuery = {
    ...params,
    searchText: params.text,
  };
  MyAxios.post("/post/list/page/vo", postQuery).then((res: any) => {
    //都还没有传递参数
    postList.value = res.records;
  });

  const userQuery = {
    ...params,
    userName: params.text, //这里要对应后端接收的参数
    // userProfile: params.text, //这里要对应后端接收的参数
  };
  MyAxios.post("/user/list/page/vo", userQuery).then((res: any) => {
    userList.value = res.records;
  });
  const pictureQuery = {
    ...params,
    searchText: params.text,
  };
  MyAxios.post("/picture/list/page/vo", pictureQuery).then((res: any) => {
    pictureList.value = res.records;
  });
};

/*
 * (加载聚合数据)加载数据第二种版本---以实现点击search就实现查询的功能
 *
 * */
const loadDataAll = (params: any) => {
  const query = {
    ...params,
    searchText: params.text,
  };
  MyAxios.post("/search/all", query).then((res: any) => {
    //query已经传递参数了
    postList.value = res.postList;
    userList.value = res.userList;
    pictureList.value = res.pictureList;
  });
};

/*
<<<<<<< HEAD
 * 加载数据第三种版本---获取单类数据
=======
 * (根据type来获取加载数据)加载数据第三种版本---加载单类数据
>>>>>>> c1a0d7a4fe69eefeb623d23cd849b7628c2769bf
 *
 * */
const loadData = (params: any) => {
  const { type } = params;
  if (!type) {
    message.error("类别为空"); //首次访问点击localhost:8080/的时候，会出现类别为0的错误提示，想办法处理调记得
    return;
  }
  const query = {
    ...params,
    searchText: params.text,
  };
  MyAxios.post("/search/all", query).then((res: any) => {
    //query传递参数了   根据type来查询单类参数
    // alert("type=" + type);
    if (type === "post") {
      postList.value = res.dataList;
    } else if (type === "user") {
      userList.value = res.dataList;
    } else if (type === "picture") {
      pictureList.value = res.dataList;
    }
  });
};

//首次请求
// loadData(initSearchParams);
// loadData(initSearchParams);//直接在watchEffect里面监听就好了，不需要首次请求了
watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
    //只要watchEffect发生了修改，就会触发重新执行
    type: route.params.category,
  } as any;
  loadData(searchParams.value);
});
const onSearch = (value: string) => {
  // alert(value);onSearch会改变页面的路由
  console.log(value);
  router.push({
    query: {
      ...searchParams.value,
      text: value,
    },
  });
};
const onTabChange = (key: string) => {
  router.push({
    path: key,
    query: searchParams.value,
  });
};
</script>
