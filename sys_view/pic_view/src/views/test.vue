<template>
  <div className="image-list" ref="imageList">
    <image-item v-for="image in images" :key="image.pid" :pid="image.pid" :miniurl="image.miniurl"/>
    <div v-if="isLoading" className="loading">
      <i className="fas fa-spinner fa-spin"></i>
    </div>
  </div>
</template>

<script>
import {getRandPic} from "../../api/pic_api";
import ImageItem from "@/components/ImageItem.vue";

export default {
  components: {
    "image-item": ImageItem,
  },
  data() {
    return {
      images: [],
      isLoading: false,
    };
  },
  created() {
    this.loadImages();
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
        this.isLoading = false;
      });
    },
    handleScroll() {
      const list = this.$refs.imageList;
      if (list && !this.isLoading) {
        const rect = list.getBoundingClientRect();
        if (rect.bottom <= window.innerHeight) {
          this.loadImages();
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
  margin: 0 -20px;
}

.image-item {
  min-width: 200px;
  margin: 20px;
  padding: 10px;
  background-color: #f4f4f4;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
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
}

.loading i {
  font-size: 2rem;
  color: #333;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@media screen and (max-width: 768px) {
  .image-item {
    width: 100%;
  }
}

</style>
