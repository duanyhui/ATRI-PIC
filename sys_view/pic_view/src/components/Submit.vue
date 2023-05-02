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
    <el-form-item label="上传图片（一次最多5张）" prop="files" >
      <el-upload :model="files" :ref="form.file"
        action="https://jsonplaceholder.typicode.com/posts/"
        :auto-upload="false"
        :show-file-list="true"
        :on-change="fileChange"
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
      },
      files:null,
      rules: {
        info: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        // tags: [{ required: true, type: 'array', min: 1, message: '请选择标签', trigger: 'change' }],
        isAi: [{ required: true, message: '是否AI', trigger: 'change' }],
        // author: [{ required: true, message: '留个名吧', trigger: 'blur' }],
        // source: [{ required: true, message: '', trigger: 'blur' }],
      },
      tagList:['亚托莉','泳装'],
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
    fileChange(file) {
      const typeArr = ['image/png', 'image/gif', 'image/jpeg', 'image/jpg'];
      const isJPG = typeArr.indexOf(file.raw.type) !== -1;
      // image/png, image/jpeg, image/gif, image/jpg
      const isLt10M = file.size / 1024 / 1024 < 30;

      if (!isJPG) {
        this.$message.error('只能是图片!');
        this.files = null;
        this.clearFiles();
        return;
      }
      if (!isLt10M) {
        this.$message.error('上传图片大小不能超过 30MB!');
        this.files = null;
        this.clearFiles();
        return;
      }
      this.files = file.raw;
      console.log(this.files);
    },
    submitForm() {
      if(!this.files){
        this.$message.error('请选择图片!');
        return;
      }
      console.log(this.form);

      if (this.form.isAi === 'ai') {
        this.form.tags.push('ai');
      } else if (this.form.isAi === 'nonAi') {
        this.form.tags.push('非ai');
      }

      // 验证表单数据
      this.$refs.form.validate(valid => {
        if (valid) {
          const formData = new FormData();
          Object.keys(this.form).forEach(key => {
            formData.append(key, this.form[key]);
          });
          formData.append('file', this.files);
          console.log(formData);
          // 发送表单数据和图片文件给后端
          upload(formData).then(res => {
           if(res.data.code === 200){
             this.$message({
               message: '上传成功',
               type: 'success'
             });
          }
           else {
             this.$message({
               message: res.data.msg,
               type: 'error'
             });
           }
      });
        }
      });
    },
    clearFiles() {
      this.uploadFiles = [];
      this.uploadingFiles = [];
      this.uploadedFiles = [];
      this.uploadList = [];
      this.$refs.input.value = '';
    }
  },
};
</script>
<style>
.my-form {
  max-width: 600px;
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
