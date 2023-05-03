<template>

  <div class="image-list" ref="imageList">
    <h2>图片点击即可进入详情页</h2>
    <h3>图片较少，可能会出现重复图片的情况，欢迎投稿</h3>
    <h2>Tag是可以点击的</h2>
    <h3>直接在贴吧里打开会出现无法投稿的问题，复制链接到浏览器打开即可解决</h3>
    <image-item v-for="image in images" :pid="String(image.pid)" :miniurl="String(image.miniurl)"/>
    <div v-if="isLoading" class="loading">加载中...</div>
    <BackToTop/>
  </div>
</template>

<script>
import {getRandPic} from "../../api/pic_api";
import ImageItem from "@/components/ImageItem.vue";
import BackToTop from "@/components/BackToTop.vue";
import { mapState, mapMutations } from 'vuex';

export default {
  components: {
    BackToTop,
    "image-item": ImageItem,
  },
  computed: {
    ...mapState(['cachedImages']),
  },
  data() {
    return {
      images: [],
      isLoading: false,
      requestCount: 0, // 添加计数器属性
    };
  },
  created() {
    if (this.$store.state.cachedImages.length === 0) {
      this.loadImages();
    } else {
      this.images = this.$store.state.cachedImages;
    }
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeDestroy() {
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    loadImages() {
      this.isLoading = true;
      getRandPic(20).then((res) => {
        this.images.push(...res.data.data);
        this.$store.commit('setCachedImages', this.images);
        // console.log(this.$store.state.cachedImages);
        this.isLoading = false;
        this.requestCount++; // 计数器加一
        if (this.requestCount === 5){
          //加大
          this.$message({
            message: "页面过大，即将刷新页面",
            type: "warning",
          });
        }
        if (this.requestCount >= 8) {
          window.location.reload(); // 刷新页面
        }
      });
    },
    handleScroll() {
      const list = this.$refs.imageList;
      if (list && !this.isLoading) {
        const rect = list.getBoundingClientRect();
        if (rect.bottom <= window.innerHeight) {
          const lastImage = list.lastChild;
          const lastImageRect = lastImage.getBoundingClientRect();
          if (lastImageRect.bottom <= window.innerHeight) {
            this.loadImages();
          }
        }
      }
    },
  },
};
</script>





<style scoped>
img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin: 0 -10px;
}

.image-item {
  min-width: 200px;
  margin: 10px;
  padding: 10px;
  background-color: transparent;
  box-shadow: none;
  transition: transform 0.2s;
  overflow: hidden;
}

.image-item:hover {
  transform: translateY(-5px);
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 50px;
  font-size: 1.2rem;
  color: #666;
}

@media screen and (max-width: 768px) {
  .image-item {
    width: 100%;
    margin: 10px 0;
  }
}
</style>
