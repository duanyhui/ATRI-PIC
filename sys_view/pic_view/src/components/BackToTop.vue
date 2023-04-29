<template>
  <button class="back-to-top" @click="scrollToTop">
    <i class="fas fa-arrow-up"></i>
  </button>
</template>

<script>
export default {
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
  width: 50px;
  height: 50px;
  border: none;
  border-radius: 50%;
  background-color: #007bff;
  color: #fff;
  font-size: 1.5rem;
  text-align: center;
  line-height: 50px;
  cursor: pointer;
  transition: opacity 0.2s;
  opacity: 0;
}

.back-to-top:hover {
  opacity: 0.8;
}

.back-to-top.show {
  opacity: 1;
}
</style>
