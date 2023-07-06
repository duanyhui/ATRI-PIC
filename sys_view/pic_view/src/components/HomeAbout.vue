<template>
  <div>
    <vue-markdown :source="markdownText"></vue-markdown>
  </div>
</template>

<script>
import VueMarkdown from 'vue-markdown'
import {getAboutMe} from "../../api/pic_api";
import {GetHomeAbout} from "../../api/utils";

export default {
  name: 'HomeAbout',
  components: {
    VueMarkdown
  },
  data () {
    return {
      markdownText: '# Hello, world!'
    }
  },
  created() {
    GetHomeAbout().then(res=>{
      this.markdownText=res.data.data
    })
  }
}
</script>
