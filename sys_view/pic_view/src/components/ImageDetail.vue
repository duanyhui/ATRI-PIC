<template>
  <div>
    <h2>{{ image.info }}</h2>
    <img :src="image.miniurl" alt="image" />
  </div>
</template>

<script>
import {getPic} from "../../api/pic_api";

export default {
  props: {
    pid: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      image: {
        info: '',
        miniurl: '',
        pid: 0
      }
    };
  },
  mounted() {
    this.getImageData();
  },
  methods: {
    getImageData() {
      getPic(156).then(res => {
        this.image.info = res.data.data.info;
        this.image.miniurl = res.data.data.miniurl;
      });

      // 发送 API 请求，获取图片数据
      // 省略...
      // 假设数据返回 json 对象 { "title": "图片标题", "url": "图片 URL" }
      this.image.title = '图片标题';
      this.image.url = '图片 URL';
    }
  }
};
</script>

<style scoped>
img {
  max-width: 100%;
  height: auto;
}
</style>
