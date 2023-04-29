<template>
  <div id="app">
    <nav>
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>
    </nav>
    <router-view/>
  </div>
</template>

<script>
import axios from "axios";
import {HasPermission} from "../api/user_api";

export default {
  created() {
    if(localStorage.getItem('satoken')===null){
      axios.get('/utils/token').then(res => {
        console.log(res.data);
        localStorage.setItem('satoken', res.data.data.token);
        localStorage.setItem('uuid', res.data.data.uuid);
      })
    }
    else {
      HasPermission(localStorage.getItem('satoken')).then(res=>{
        if(res.data.code===400){
          axios.get('/utils/token').then(res => {
            console.log(res.data);
            localStorage.setItem('satoken', res.data.data.token);
            localStorage.setItem('uuid', res.data.data.uuid);
          })
        }
      })
    }
  },
  name: 'app'
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>
