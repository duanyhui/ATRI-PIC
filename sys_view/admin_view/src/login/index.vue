e<template>
  <div>
    <el-container>
      <el-header >
        <div style="text-align: center; font-size: 25px; font-weight: bolder">
          <i class="el-icon-s-home" style="margin-right: 25px"></i>

        </div>
      </el-header>
      <el-main>
        <el-form :model="form" :rules="rules" ref="form" label-width="80px" style="width: 20%; margin: auto auto">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="form.password" type="password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="login">登录</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </el-main>

    </el-container>
  </div>
</template>

<script>

import {Login} from "@/api/api";
import router from "@/router";

export default {
  name: "index",
  data() {
    return {
      form: {
        username: null,
        password: null,
        type: null
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" }
        ]
      }
    };
  },
  methods:{
    login() {
      const that=this;
      this.$refs.form.validate(valid => {
          sessionStorage.removeItem("token")
          Login(that.form.username,that.form.password).then(res=>{
            if(res.data.code===200){
              sessionStorage.setItem("satoken",res.data.data.token)
                router.push({path:"/home"})
            }else{
              this.$message.error(res.data.msg)
            }
          })


      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

  }
}
</script>

<style scoped>

</style>