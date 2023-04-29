<template>
    <div class="image-item" @click="handleClick">
        <img :src=this.miniurl alt="image" />
    </div>
</template>

<script>
import axios from "axios";

export default {
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
    methods: {
        handleClick() {
            this.$router.push(`/image/${this.pid}`);
        },
      getImageUrl(url) {
          console.log("777"+url);
        if(url.startsWith("http")) {
          console.log("到达");
          return url; // already a full URL
        } else {
          // prepend the base URL if it's not already included in the URL
          return axios.defaults.baseURL + "/" + url;
        }
      }

    },
  created() {
      // console.log("666"+this.miniurl);
  }
};
</script>

<style scoped>
img {
    max-width: 100%;
    height: auto;
}
</style>
