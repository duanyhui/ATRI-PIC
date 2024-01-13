<template>
  <div class="main-content">
    <div class="search-result">
        <h3>除了是否AI和图片其他都为可选项</h3>
    </div>
  <el-form :model="form" :rules="rules" ref="form" label-width="70px" class="my-form">
    <el-form-item label="标题" prop="info">
      <el-input v-model="form.info"></el-input>
    </el-form-item>
<!--    <el-form-item label="标签" prop="tags">-->
<!--      <el-checkbox-group v-model="form.tags">-->
<!--        <el-checkbox v-for="tag in tagList" :key="tag" :label="tag">{{ tag }}</el-checkbox>-->
<!--        <el-input v-model="customTag" placeholder="自定义标签"/>-->
<!--        <el-button @click="addTag">添加</el-button>-->
<!--      </el-checkbox-group>-->
<!--    </el-form-item>-->
    <el-form-item label="标签" prop="tags" class="select-content">

      <el-select v-model="form.tags" multiple filterable allow-create placeholder="选择或输入标签（可多选）">
        <el-option
          v-for="item in tagOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

    </el-form-item>

    <el-form-item label="是否AI" prop="isAi"  class="is-ai">
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
  </div>
</template>
<script>
import {upload} from "../../api/pic_api";
import {SetUrlLog} from "../../api/utils";


export default {
  computed: {
    filteredTags() {
      // 假设 tagOptions 是所有可能的标签列表
      const query = this.customTag.toLowerCase();
      return this.tagOptions.filter(tag => tag.label.toLowerCase().includes(query));
    },
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
      tagList:['亚托莉','GINKA','表情包','头像','弔图','游戏截图','CG原画','插画'],
      tagOptions: [
        { label: '亚托莉', value: '亚托莉' },
        { label: 'GINKA', value: 'GINKA' },
        { label: '表情包', value: '表情包' },
        { label: '头像', value: '头像' },
        { label: '弔图', value: '弔图' },
        { label: '游戏截图', value: '游戏截图' },
        { label: 'CG原画', value: 'CG原画' },
        { label: '插画', value: '插画' },
        // ...其他标签
      ],
      customTag: '',
    };
  },
  methods: {
    // addTag() {
    //   console.log(this.customTag);
    //
    //   const tag = this.customTag.trim();
    //   if (tag && !this.form.tags.includes(tag)) {
    //     this.form.tags.push(tag);
    //     this.customTag = '';
    //     //提示添加成功
    //     this.$message({
    //       message: '添加成功',
    //       type: 'success'
    //     });
    //     //刷新标签列表
    //     this.tagList.push(tag);
    //   }
    //   console.log(this.form.tags);
    // },
    handleCreateTag(newTag) {
      const newOption = {
        label: newTag,
        value: newTag
      };
      if (!this.tagOptions.find(option => option.value === newTag)) {
        this.tagOptions.push(newOption);
        this.form.tags.push(newTag);
      }
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
:root {
  --main-bg-color: #f8f8f8;
  --button-color: #409eff;
  --button-hover-color: #66b1ff;
  --border-radius: 4px;
  --form-padding: 10px;
  --input-height: 40px;
  --box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.my-form {
  max-width: 100%; /* 使表单在所有屏幕尺寸上都是100% */
  margin: 0 auto;
  padding: var(--form-padding);
  background-color: var(--main-bg-color);
  border-radius: var(--border-radius);
  box-shadow: var(--box-shadow);
}

@media (min-width: 601px) {
  .my-form {
    max-width: 600px; /* 在大屏幕上限制最大宽度 */
  }
}

.el-form-item__label {
  font-size: 1rem; /* 使用相对单位 */
  font-weight: bold;
  color: #333;
}

.el-input, .el-input__inner, .el-select, .el-radio__inner, .el-button {
  border-radius: var(--border-radius);
  height: var(--input-height);
}

.el-button {
  background-color: var(--button-color);
  border-color: var(--button-color);
  color: #fff;
  padding: var(--form-padding) 20px;
  margin-top: var(--form-padding);
  transition: background-color 0.3s, border-color 0.3s;
}

.el-button:hover {
  background-color: var(--button-hover-color);
  border-color: var(--button-hover-color);
}

.el-checkbox-group {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: var(--form-padding);
}

.el-upload {
  border: 2px dashed #d9d9d9;
  border-radius: var(--border-radius);
  padding: var(--form-padding);
  background-color: #fafafa;
  transition: border-color 0.3s;
}

.el-upload:hover {
  border-color: var(--button-color);
}

.el-input__inner::placeholder {
  color: #aaa;
}

/* 使用媒体查询来移除小屏幕上的阴影 */
@media (max-width: 600px) {
  .my-form {
    box-shadow: none;
  }
}

.el-radio-button__orig-radio:checked + .el-radio-button__inner {
  background-color: var(--button-color);
  border-color: var(--button-color);
  color: #fff;
}
/* 必选项样式 */
.is-ai .el-form-item__label::before {
  content: '*';
  color: #ff4d4f; /* 红色强调必选项 */
  margin-right: 4px;
  font-weight: bold;
  font-size: 1.2em; /* 确保星号大小足够引起注意 */
  vertical-align: -0.2em; /* 微调星号垂直位置 */
}


/* 鼠标悬停时的样式 */
.el-radio:hover .el-radio__label {
  color: var(--button-color); /* 鼠标悬停时的字体颜色 */
}
/* 选中状态的样式 */
.el-radio.is-checked .el-radio__label {
  color: var(--button-color); /* 选中状态的字体颜色 */
}
/* 当单选按钮组未选择时的样式 */
.el-radio-group.is-required:not(.is-filled) .el-radio__input.is-focus .el-radio__inner,
.el-radio-group.is-required:not(.is-filled) .el-radio__input:hover .el-radio__inner {
  border-color: #ff4d4f; /* 未选择时的边框颜色 */
}
/* 当单选按钮组选择后的样式 */
.el-radio-group.is-required.is-filled .el-radio__input .el-radio__inner {
  border-color: var(--button-color); /* 选择后的边框颜色 */
}

.el-checkbox-group {
  display: flex; /* 使用flex布局 */
  flex-wrap: wrap; /* 允许子元素换行 */
  align-items: center; /* 垂直居中对齐子元素 */
  justify-content: flex-start; /* 从行的开始位置对齐子元素 */
}

.el-select .el-select__tags {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.el-select .el-tag {
  margin-right: 8px;
  margin-bottom: 8px;
  flex-shrink: 0; /* 防止标签在容器太小的情况下被压缩 */
}
.el-form-item__label {
  width: 100px; /* 或者您希望的任何固定宽度 */
  flex: none; /* 如果您在 flex 容器中，防止标签缩放 */
}

.main-content {
  min-height: 150vh; /* 视口高度 */
}


</style>

