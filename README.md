# AmberSearchPlatform
一个聚合式搜索平台，也是一个简化版的企业中台。可以在一个页面同时搜索图片，文章以及视频(待扩展)。

对用户来说，使用该平台，**可以在同一个入口页面搜索出不同来源、不同类型的内容，提升用户检索的效率和搜索体验**。

对企业来说，**当企业内部有多个项目的数据都存在搜索需求时，无需针对每个项目都单独开发搜素功能，可以直接将各个项目的数据源接入搜索中台，从而提升开发效率**。



#### 项目设计



##### 前端

- VUE
- Ant Design VUE
- Lodash



##### 后端

- Spring Boot
- Mysql
- Elasticsearch（Elastic Stack）搜索引擎
- 数据抓取
- 数据同步
  - 4种不同的方式
  - logstash
  - Canal

- Guava Retrying
- 怎么保证API的稳定性？



##### 业务流程

1.先得到各种不同分类的数据；

2.提供一个搜索页面（单一搜索 + 聚合搜索），支持搜索；

3.（可以做一些优化）比如关键词高亮、搜索建议、防抖节流；

项目架构图： 

![image-20230606151025521](C:\Users\Amber\AppData\Roaming\Typora\typora-user-images\image-20230606151025521.png)



##### 前端初始化

步骤：

1.参考Ant Design组件库的官方文档来搭建初始化项目（[Ant Design Vue (antdv.com)](https://2x.antdv.com/docs/vue/getting-started-cn)），并整合组件库。

2.删减不需要的页面和路由。



##### 后端初始化

步骤：

1.先构建用户增删改查与注册的基本系统功能。

2.直接使用swagger文档在线测试接口。



##### 前端开发

组件库使用方式：从上到下依次在组件库文档中找到对应组件，完成基本页面开发。



##### 记录搜索状态

目标：用url记录页面搜索状态，当用户刷新页面时，能够从url还原之前的搜索状态

**因为十分有可能出现---你的url状态改变了但是页面状态还未改变，或者相反。**状态不同步的问题解决方式---由双向同步改为单向同步

需要双向同步：url <=>页面状态

核心小技巧---把同步状态改成单向，即只允许url来改变页面状态，不允许反向



分步骤来实现，思路更加清晰：

1.让用户在操作的时候，改变url地址（点击搜索框，搜索内容填充到url上，切换tab时，也需要填充）

2.当url改变时，去改变页面状态（监听url的改变）



##### 前后端联调

使用Axios向后端发送请求。

步骤（请参考官方文档：[起步 | Axios 中文文档 | Axios 中文网 (axios-http.cn)](https://www.axios-http.cn/docs/intro)）：

​		1.前端整合Axios

​		2.自定义Axios实例

​		3.发送请求



创建的项目为ambersou-fro



##### 版本二：

1.获取多种不同类型的数据源----爬虫

- 文章（内部）
- 用户（内部）
- 图片（外部爬取）		

2.前后端单一接口联调

3.聚合接口优化开发









