<template>
  <div>
    <span>当前图片总数：{{pic_num}}张</span><br>
    <vue-markdown :source="markdownText"></vue-markdown>
  </div>
</template>

<script>
import VueMarkdown from 'vue-markdown'
import {getAboutMe, getPicCount} from "../../api/pic_api";

export default {
  components: {
    VueMarkdown
  },
  data () {
    return {
      markdownText: '# Hello, world!',
      pic_num: 0,
    }
  },
  created() {
    getAboutMe().then(res=>{
      this.markdownText=res.data.data
    })
    getPicCount().then((res) => {
      this.pic_num = res.data.data;
    });
  }
}
</script>
