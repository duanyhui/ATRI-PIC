<template>
  <div>
    <el-button slot="reference" type="danger" round style=" "  @click="out()">
      登出
    </el-button>
<!--    <el-divider direction="vertical" style="color: #333333"></el-divider>-->
<!--    <i class="el-icon-user" style="margin-right: 18px"></i>-->
<!--    <span>{{ name }}</span>-->

  </div>
</template>

<script>
import Logout from "@/components/logout";
export default {
  name: "r-header",
  components: {Logout},
  data() {
    return {
      name: null,
      currentTerm: null
    }
  },
  created() {
    this.name = sessionStorage.getItem("name")
    this.term = sessionStorage.getItem("term")
  },
  methods: {
    out() {
      sessionStorage.clear();
      this.$router.push('/')
    }
  }
}
</script>

<style scoped>

</style>