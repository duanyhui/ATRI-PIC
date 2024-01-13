<template>

  <div class="image-list" ref="imageList">

<!--    <span>当前图片总数：{{pic_num}}张</span><br>-->
<!--    <span></span>-->
<!--    <br><br>-->
    <span>当前图片总数：{{pic_num}}张</span><br>
    <HomeAbout></HomeAbout>
<!--    <h2>图片点击即可进入详情页</h2>-->
<!--    <h3>图片较少，可能会出现重复图片的情况，欢迎投稿</h3>-->
<!--    <h2>Tag是可以点击的</h2>-->
    <image-item v-for="image in images" :pid="String(image.pid)" :miniurl="String(image.miniurl)"/>
    <div v-if="isLoading" class="loading">加载中...</div>
    <BackToTop/>
  </div>
</template>

<script>
import {getPicCount, getRandPic} from "../../api/pic_api";
import ImageItem from "@/components/ImageItem.vue";
import BackToTop from "@/components/BackToTop.vue";
import { mapState, mapMutations } from 'vuex';
import HomeAbout from "@/components/HomeAbout.vue";

export default {
  components: {
    HomeAbout,
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
      pic_num: 0,
    };
  },
  created() {
    if (this.$store.state.cachedImages.length === 0) {
      this.loadImages();
    } else {
      this.images = this.$store.state.cachedImages;
    }
    getPicCount().then((res) => {
      this.pic_num = res.data.data;
    });
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
.image-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  margin: 0 -10px;
  background-color: #f4f4f4;
}

.image-item {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  width: 300px;
  margin: 10px;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ddd;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  position: relative; /* 为放大效果设置相对定位 */
  transition: transform 0.3s, box-shadow 0.3s; /* 平滑转换效果 */
}

.image-item img {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
  transition: transform 0.3s; /* 平滑转换效果 */
}

.image-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.image-item:hover img {
  transform: scale(1.05); /* 鼠标悬停时放大图片 */
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
    width: calc(100% - 20px); /* 在移动端减少边缘间隙 */
    margin: 10px 10px; /* 移动端的间隙减少一半 */
    padding: 5px; /* 移动端内边距减少 */
  }

  .image-item img {
    height: auto; /* 允许图片自适应高度 */
    object-fit: contain; /* 避免图片裁剪，完整显示 */
  }
}







</style>
