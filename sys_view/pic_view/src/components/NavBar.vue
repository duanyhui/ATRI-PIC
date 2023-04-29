<template>
  <div class="navbar">
    <div class="navbar__center">
      <form class="navbar__search-form">
        <input type="text" class="navbar__search-input" placeholder="搜索...">
        <button type="submit" class="navbar__search-btn">
          <i class="fas fa-search"></i>
        </button>
      </form>
    </div>
    <div class="navbar__left">
      <a href="#" class="navbar__item">关于</a>
      <a href="#" class="navbar__item navbar__item--submit">投稿</a>
    </div>

  </div>
</template>

<script>
export default {
  name: 'Navbar',
  mounted() {
    window.addEventListener('scroll', this.handleScroll)
  },
  beforeDestroy() {
    window.removeEventListener('scroll', this.handleScroll)
  },
  methods: {
    handleScroll() {
      const navbar = document.querySelector('.navbar')
      if (navbar) {
        navbar.classList.toggle('navbar--fixed', window.pageYOffset > 0)
      }
    }
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  padding: 10px 0;
  background-color: #fff;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  transition: all 0.2s;
}

.navbar--fixed {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 999;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.2);
}

.navbar__left,
.navbar__center {
  display: flex;
  align-items: center;
}

.navbar__item {
  margin: 0 10px;
  font-size: 32px;
  color: #333;
  text-decoration: none;
}

.navbar__item--submit {
  background-color: #4c80f1;
  color: #fff;
  padding: 10px 20px;
  border-radius: 20px;
}

.navbar__search-form {
  position: relative;
  display: flex;
  align-items: center;
}

.navbar__search-input {
  width: 200px;
  height: 30px;
  padding: 5px;
  border: none;
  border-radius: 5px;
  font-size: 14px;
}

.navbar__search-btn {
  position: absolute;
  top: 0;
  right: 0;
  width: 40px;
  height: 40px;
  border: none;
  background-color: transparent;
  cursor: pointer;
  transition: all 0.2s;
}

.navbar__search-btn:hover {
  transform: scale(1.1);
}

.fas.fa-search {
  font-size: 18px;
  color: #333;
}
</style>
