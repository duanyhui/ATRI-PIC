
# ATRI 图片维基
## [项目演示地址](https://pic.atri.wiki)

> 基于 Springboot + Vue2 的图片收集网站

本项目致力于收集和分享亚托莉的图片。

### 🚀 最新更新

- **2023/5/6** - 增加搜索和 tag 选项，方便大家筛选图片。
- **2024/1/13** - 美化首页和投稿页，使界面更加友好。新增邮件通知审核通过功能，及时获取更新信息。

### 🛠️ 开源地址

项目的代码已开源，欢迎查看和贡献： [GitHub - ATRI-PIC](https://github.com/duanyhui/ATRI-PIC)

### 📢 公告

- 服务器带宽有限，图片加载可能稍有延迟，请耐心等待。
- 图库目前图片数量有限，可能会出现重复，我们期待你的投稿来丰富我们的集合。

- 如果您在使用过程中遇到问题，或有任何建议，欢迎通过 QQ **783809979** 反馈给我。

### 📥 投稿须知

我们鼓励用户投稿，分享更多关于亚托莉的图片。投稿前，请确保图片清晰、内容健康。

### 📜 TODO

- 重写首页推荐图片算法，减少重复出现的图片。
- 上CDN加快图片加载速度
- 图库去重

## 运行环境：JDK 1.8、MySQL 5.6、Node.js 10.0+、Redis
## 七牛云存储暂时未配置
### 项目结构
```
├─atri-pic
│  ├─sys_api // 后端
│  │  ├─src
│  │  │  ├─main
│  │  │  │  ├─java
│  │  │  │  │  ├─duan
│  │  │  │  │  │  ├——ServerApplication.java // 启动类
│  │  │  │——resources
│  │  │  │  ├─application.yml // 配置文件
│  ├─sys_view 
│  │  ├─admin_view // 管理员前端
│  │     ——src/main.js // 在此配置请求地址
│  │     package.json // 项目启动入口
│  │  └─pic_view // 用户前端
│  │     ——src/main.js // 在此配置请求地址
│  │     package.json // 项目启动入口
```
![](https://qiniu-pic.atri.wiki/img/202401132227060.png)

