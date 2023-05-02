<template>
  <div>
    <vue-markdown :source="markdownText"></vue-markdown>
  </div>
</template>

<script>
import VueMarkdown from 'vue-markdown'
import {getAboutMe} from "../../api/pic_api";

export default {
  components: {
    VueMarkdown
  },
  data () {
    return {
      markdownText: '# Hello, world!'
    }
  },
  created() {
    getAboutMe().then(res=>{
      this.markdownText=res.data.data
    })
  }
}
</script>
