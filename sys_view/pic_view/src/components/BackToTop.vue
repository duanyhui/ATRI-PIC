<template>
  <button class="back-to-top" :class="{ show: showButton }" @click="scrollToTop">
    <i class="fas fa-arrow-up"></i>
  </button>
</template>

<script>
export default {
  name: 'BackToTop',
  data() {
    return {
      showButton: false
    }
  },
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    handleScroll() {
      this.showButton = window.pageYOffset > 100
    },
    scrollToTop() {
      window.scrollTo({ top: 0, behavior: 'smooth' })
    }
  }
}
</script>

<style scoped>
.back-to-top {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 60px;
  height: 60px;
  border: none;
  border-radius: 50%;
  background-color: #007bff;
  color: #fff;
  font-size: 2rem;
  text-align: center;
  line-height: 60px;
  cursor: pointer;
  transition: opacity 0.2s, right 0.2s, bottom 0.2s; /* 添加right和bottom属性的过渡 */
  opacity: 0;
  z-index: 1000; /* 提高组件的层级 */
}

.back-to-top:hover {
  opacity: 0.8;
  right: 25px; /* 鼠标悬停时将按钮位置向左移动一点 */
  bottom: 25px; /* 鼠标悬停时将按钮位置向上移动一点 */
}
.back-to-top.show {
  opacity: 1;
}

</style>
