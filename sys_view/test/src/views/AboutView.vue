<template>
  <div class="about">
    <h1>This is an about page</h1>
<button @click="testPermisoon">test</button>
<button @click="login">login</button>
  </div>
</template>
<script>
import axios from "axios";

export default {
  name: 'AboutView',
  methods:{
    testPermisoon(){
      axios.get("/pic/testPermission").then(res=>{
        console.log(res)
      })
    },
      login(){
        axios.get("/utils/token").then(res=>{
          console.log(res)
        })
      }
  }
}
</script>
