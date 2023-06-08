<template>
  <div class="index-page">
    <a-input-search
      v-model:value="searchParams.text"
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

const postList = ref([]);

MyAxios.post("/post/list/page/vo", {}).then((res: any) => {
  //都还没有传递参数
  postList.value = res.records;
});

const userList = ref([]);
MyAxios.post("/user/list/page/vo", {}).then((res: any) => {
  userList.value = res.records;
});

const pictureList = ref([]);
MyAxios.post("/picture/list/page/vo", {}).then((res: any) => {
  pictureList.value = res.records;
});

const searchText = ref("");
const router = useRouter();
const route = useRoute();
const activeKey = route.params.category;
const initSearchParams = {
  // text: "",//是否增加？？
  text: "",
  pageSize: 10,
  pageNum: 1,
};

// alert(route.params.category);
const searchParams = ref(initSearchParams);
watchEffect(() => {
  searchParams.value = {
    ...initSearchParams,
    text: route.query.text,
  } as any;
});
const onSearch = (value: string) => {
  alert(value);
  router.push({
    query: searchParams.value,
  });
};
const onTabChange = (key: string) => {
  router.push({
    path: key,
    query: searchParams.value,
  });
};
</script>
