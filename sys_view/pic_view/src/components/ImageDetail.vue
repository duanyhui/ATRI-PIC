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
img {
  max-width: 100%;
  height: auto;
}

.image-info {
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  font-size: 16px;
}

.info-row {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.info-label {
  font-weight: bold;
  margin-right: 10px;
}

.tag-list {
  margin-top: 10px;
}

.tag {
  display: inline-block;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 14px;
  margin-right: 10px;
  cursor: pointer;
}

.tag-0 {
  background-color: #61affe;
  color: #fff;
}

.tag-1 {
  background-color: #f2b600;
  color: #fff;
}

.tag-2 {
  background-color: #ff5959;
  color: #fff;
}

.tag-3 {
  background-color: #8f47b3;
  color: #fff;
}

.tag-4 {
  background-color: #00b19d;
  color: #fff;
}

.button-group {
  margin-top: 20px;
}
</style>
