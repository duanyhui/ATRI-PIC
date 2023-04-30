<template>
  <div class="image-item" @click="handleClick">
    <img :src="miniurl" alt="image" />
  </div>
</template>

<script>
import axios from "axios";
import {votePic} from "../../api/pic_api";

export default {
  components: {

  },
  props: {
    miniurl: {
      type: String,
      required: true
    },
    pid: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
    }
  },
  methods: {
    handleClick() {
      this.$router.push(`/art/${this.pid}`);
    },
  },
  created() {
  }
};
</script>

<style scoped>
img {
  max-width: 100%;
  height: auto;
}


/*.vote-btn {*/
/*  position: absolute;*/
/*  bottom: 10px;*/
/*  right: 10px;*/
/*  display: flex;*/
/*  justify-content: center;*/
/*  align-items: center;*/
/*  width: 40px;*/
/*  height: 40px;*/
/*  background-color: rgba(255, 255, 255, 0.8);*/
/*  border-radius: 50%;*/
/*  box-shadow: 0px 0px 5px 0px rgba(0, 0, 0, 0.2);*/
/*  cursor: pointer;*/
/*  transition: transform 0.2s;*/
/*}*/

/*.vote-btn:hover {*/
/*  transform: scale(1.1);*/
/*}*/

/*.iconfont {*/
/*  font-size: 24px;*/
/*  color: #888;*/
/*}*/
</style>
