<template>
  <div>
    <h2>{{ image.info }}</h2>
    <img :src="image.localurl" alt="image" />

    <div class="image-info">
      <div class="info-row">
        <div class="info-label">作者：</div>
        <div class="info-value">{{ image.author }}</div>
      </div>
      <div class="info-row">
        <div class="info-label">来源：</div>
        <div class="info-value">{{ image.source }}</div>
      </div>
      <div class="info-row">
        <div class="info-label">浏览量：</div>
        <div class="info-value">{{ image.seenum }}</div>
      </div>
      <div class="info-row">
        <div class="info-label">更新时间：</div>
        <div class="info-value">{{ image.updatetime }}</div>
      </div>
      <div class="info-row">
        <div class="info-label">尺寸：</div>
        <div class="info-value">{{ image.size }}KB</div>
      </div>
    </div>

    <div class="tag-list">
      <div class="info-label">标签</div>
      <span v-for="(tag, index) in image.tags" :key="index" :class="`tag tag-${index % 5}`"
            @click="searchByTag(tag)">
        {{ tag }}
      </span>
    </div>

    <div class="button-group">
      <el-button @click="download">下载（{{ image.loadnum }}）</el-button>

      <el-button type="primary" @click="voteClick">喜欢（{{ image.likenum }}）</el-button>
      <el-button type="danger" @click="unLikeVoteClick">不喜欢（{{ image.unlikenum }}）</el-button>
    </div>
  </div>
</template>

<script>
import { getPic, votePic } from "../../api/pic_api";

export default {
  props: {
    pid: {
      type: Number,
      required: false,
    },
  },

  data() {
    return {
      image: {
        info: "",
        localurl: "",
        pid: "",
        author: "",
        likenum: "",
        seenum: "",
        loadnum: "",
        tags: [],
        filename: "",
        updatetime: "",
        size: "",
        unlikenum: "",
        source: "",
      },
      haslikenum: 0,
    };
  },
  mounted() {},
  methods: {
    voteClick() {
      if (this.haslikenum > 2) {
        this.$message({
          message: "大爷别点啦",
          type: "warning",
        });
        return;
      }
      votePic(this.image.pid, 1).then((res) => {
        if (res.data.code === 200) {
          this.$message({
            message: "投票成功",
            type: "success",
          });
          this.image.likenum++;
          this.haslikenum++;
        }
      });
    },
    unLikeVoteClick() {
      if (this.haslikenum > 2) {
        this.$message({
          message: "大爷别点啦",
          type: "warning",
        });
        return;
      }
      votePic(this.image.pid, -1).then((res) => {
        if (res.data.code === 200) {
          this.$message({
            message: "投票成功",
            type: "success",
          });
          this.image.unlikenum++;
          this.haslikenum++;
        }
      });
    },
    download(filename) {
      votePic(this.image.pid, 0).then((res) => {
        if (res.data.code === 200) {
          this.$message({
            message: "下载成功",
            type: "success",
          });
          this.image.loadnum++;
        }
      });
      // 下载链接
      const downloadLink = document.createElement("a");
      downloadLink.href = this.image.localurl;
      downloadLink.download = filename;
      document.body.appendChild(downloadLink);
      downloadLink.click();
      document.body.removeChild(downloadLink);

      this.$message({
        message: "下载成功",
        type: "success",
      });
      this.image.loadnum++;
    },
    searchByTag(tag) {
      // this.$store.state.cachedImages = [];
      this.$store.commit('setCachedImages', []);
      this.$router.push({
        name: "search",
        params: { tag: tag },
      });
    },
  },
  created() {
    const pid = this.$route.params.pid;
    // console.log(pid);
    getPic(pid).then((res) => {
      this.image = res.data.data;
      //日期格式化，从2023-04-29T15:43:31.8到2023-04-29 15:43
      this.image.updatetime = this.image.updatetime.replace("T", " ").substr(0, 16);
    });
  },
};
</script>

<style scoped>
/* 全局图像样式 */
img {
  max-width: 100%;
  height: auto;
  border-radius: 4px; /* 圆角 */
  object-fit: cover; /* 图片填充容器 */
}

/* 图片信息卡片样式 */
.image-info {
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  font-size: 16px;
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 15px;
  margin-bottom: 20px;
  transition: box-shadow 0.3s ease-in-out;
}

.image-info:hover {
  box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}

/* 信息行样式 */
.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.info-label {
  font-weight: bold;
  margin-right: 10px;
  color: #333; /* 为所有标签统一颜色 */
}

/* 标签列表样式 */
.tag-list {
  margin-top: 10px;
}

/* 标签样式 */
.tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 5px 15px;
  border-radius: 15px;
  font-size: 14px;
  margin-right: 10px;
  cursor: pointer;
  background-color: #eef1f6;
  color: #333;
  transition: background-color 0.2s;
}

.tag:hover {
  background-color: #dde1e7;
}

/* 使用颜色循环为标签设置不同背景颜色 */
.tag:nth-child(5n+1) {
  background-color: #61affe;
}

.tag:nth-child(5n+2) {
  background-color: #f2b600;
}

.tag:nth-child(5n+3) {
  background-color: #ff5959;
}

.tag:nth-child(5n+4) {
  background-color: #8f47b3;
}

.tag:nth-child(5n) {
  background-color: #00b19d;
}

/* 按钮组样式 */
.button-group {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

/* Element UI 按钮样式调整 */
.el-button {
  border-radius: 20px; /* 统一按钮圆角 */
}
</style>
