<template>
  <el-form :model="form" :rules="rules" ref="form" label-width="70px" class="my-form">
    <el-form-item label="标题" prop="info">
      <el-input v-model="form.info"></el-input>
    </el-form-item>
    <el-form-item label="标签" prop="tags">
      <el-checkbox-group v-model="form.tags">
        <el-checkbox v-for="tag in tagList" :key="tag" :label="tag">{{ tag }}</el-checkbox>
        <el-input v-model="customTag" placeholder="自定义标签"/>
        <el-button @click="addTag">添加</el-button>
      </el-checkbox-group>
    </el-form-item>
    <el-form-item label="是否AI" prop="isAi">
      <el-radio-group v-model="form.isAi">
        <el-radio label="ai">AI</el-radio>
        <el-radio label="nonAi">非AI</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="作者" prop="author">
      <el-input v-model="form.author" placeholder="留个名吧"></el-input>
    </el-form-item>
    <el-form-item label="来源" prop="source">
      <el-input v-model="form.source" placeholder="图片出处"></el-input>
    </el-form-item>
    <el-form-item label="邮箱" prop="mail">
      <el-input v-model="form.mail" placeholder="接收审核通过通知(不会在前台显示)"></el-input>
    </el-form-item>
    <el-form-item label="上传图片（一次最多40张）" prop="files" >
      <el-upload :model="files" :ref="form.file"
        action=""
        :auto-upload="false"
        :show-file-list="true"
        :limit="1000"
        :on-change="fileChange"
        multiple
        list-type="picture"
      >
        <el-button size="small" type="primary">点击上传</el-button>
      </el-upload>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">提交</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
import {upload} from "../../api/pic_api";
import {SetUrlLog} from "../../api/utils";


export default {
  computed: {
  },
  data() {
    return {
      form: {
        info: '',
        tags: [],
        author: '',
        source: '',
        isAi: '',
        mail: '',
      },
      files:[],
      rules: {
        // info: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        // tags: [{ required: true, type: 'array', min: 1, message: '请选择标签', trigger: 'change' }],
        isAi: [{ required: true, message: '是否AI', trigger: 'change' }],
        mail: [
          { type: 'email', message: '请输入有效的邮箱地址', trigger: ['blur', 'change'] }
        ],
        // author: [{ required: true, message: '留个名吧', trigger: 'blur' }],
        // source: [{ required: true, message: '', trigger: 'blur' }],
      },
      tagList:['亚托莉','表情包','头像','弔图','游戏截图','CG原画','插画'],
      customTag: '',
    };
  },
  methods: {
    addTag() {
      console.log(this.customTag);

      const tag = this.customTag.trim();
      if (tag && !this.form.tags.includes(tag)) {
        this.form.tags.push(tag);
        this.customTag = '';
        //提示添加成功
        this.$message({
          message: '添加成功',
          type: 'success'
        });
        //刷新标签列表
        this.tagList.push(tag);
      }
      console.log(this.form.tags);
    },
    fileChange(files) {
      const typeArr = ['image/png', 'image/gif', 'image/jpeg', 'image/jpg'];
      // console.log(files)
        const file = files;
        const isJPG = typeArr.indexOf(file.raw.type) !== -1;
        const isLt10M = file.size / 1024 / 1024 < 20;

        if (!isJPG) {
          this.$message.error('只能是图片!');
          this.files = [];
          this.clearFiles();
          return;
        }
        if (!isLt10M) {
          this.$message.error('上传图片大小不能超过 20MB!');
          this.files = [];
          this.clearFiles();
          return;
        }
        // console.log("a" + this.files);
        this.files.push(file.raw);
        // console.log("aaa" + this.files);
    },
    submitForm() {
      if (this.files.length === 0) {
        this.$message.error('请选择图片!');
        return;
      }

      if (this.form.isAi === 'ai') {
        const index = this.form.tags.indexOf('非ai');
        if (index !== -1) {
          this.form.tags.splice(index, 1);
        }
        this.form.tags.push('ai');
      } else if (this.form.isAi === 'nonAi') {
        const index = this.form.tags.indexOf('ai');
        if (index !== -1) {
          this.form.tags.splice(index, 1);
        }
        this.form.tags.push('非ai');
      }

      this.$refs.form.validate(valid => {
        if (valid) {
          this.$message({
            message: '正在上传，请勿重复点击，图片较大可能需要较长时间',
            type: 'success'
          });
          const formData = new FormData();
          Object.keys(this.form).forEach(key => {
            formData.append(key, this.form[key]);
          });
          for (let i = 0; i < this.files.length; i++) {
            formData.append('file', this.files[i]);
          }

          upload(formData).then(res => {
            if (res.data.code === 200) {
              this.$message({
                message: '上传成功,通过审核后即可展示，五秒后刷新界面',
                type: 'success'
              });
              //延迟2秒刷新页面
              setTimeout(() => {
                window.location.reload();
              }, 5000);
            } else {
              this.$message({
                message: res.data.msg,
                type: 'error'
              });
              //延迟2秒刷新页面
              setTimeout(() => {
                window.location.reload();
              }, 5000);
            }
          });
        }
      });
    },
    setUrlLog() {
      SetUrlLog("/upload")
    },
  },
  created() {
    this.setUrlLog();
  }
};
</script>
<style>
.my-form {
  /*max-width: 600px;*/
  margin: 0 auto;
}
.el-checkbox-group {
  margin-top: 10px;
}
.el-form-item__label {
  font-size: 16px;
  font-weight: bold;
}
.el-input__suffix {
  cursor: pointer;
}

</style>
