<template>
  <div>
    <el-container>
      <el-header style="text-align: right; font-size: 18px; background-color: #409EFF; border-radius: 3px">
        <r-header></r-header>
      </el-header>
      <el-container>
        <right-nav></right-nav>
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import RightNav from "@/components/right-nav";
import RHeader from "@/components/rheader";
export default {
  name: "BaseLayOut",
  components: {RHeader, RightNav}
}
</script>

<style scoped>

</style>