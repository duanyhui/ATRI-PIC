<template>
  <div>
    <template v-for="(item,index) in tags">
      <div class='el-button primary el-button--mini is-plain newtag'
           @mouseenter="showCloseBtn(index)" @mouseleave="hideCloseBtn(index)">
        <div contenteditable="plaintext-only" class="newtag-content" :ref='`newTag${index}`' @blur="checkTag(index)">
          {{ item }}
        </div>
        <i class="close-i el-icon-circle-close" :ref="`closeI${index}`" @click="removeTag(index)"></i>
      </div>
    </template>
    <i class="el-icon-circle-plus-outline" @click="addTag()"></i>
  </div>
</template>


<script>
export default {
  name: "AddTag",
  data() {
    return {
      recForm:{
        tags:[]
      },
      tags:['川建国','奥观海'], //通过操作数组数据直接增减tag
    }
  },
  methods:{
    addTag() {
      this.recForm.tags.push('新标签');
      this.$nextTick(()=>{
        let len = this.recForm.tags.length;
        let newtag = this.$refs[`newTag${len-1}`][0]; //获得新增的标签，数组的最后一个
        let range = document.createRange();
        range.selectNodeContents(newtag);
        window.getSelection().removeAllRanges();
        window.getSelection().addRange(range); //自动全选
      });
    },
    showCloseBtn(index){
      this.$refs[`closeI${index}`][0].style.display = 'block'; //显示叉叉
    },
    hideCloseBtn(index){
      this.$refs[`closeI${index}`][0].style.display = 'none';  //叉叉消失
    },
    checkTag(index){ //判断tag有无内容
      let text = this.$refs[`newTag${index}`][0].innerText.trim();
      if(text.length==0){
        this.recForm.tags.pop();
      }else{
        this.recForm.tags[index] = text;
      }
    },
    removeTag(index){  //删除叉叉
      this.recForm.tags.splice(index,1);
    },
  }
}
</script>
<style scoped>
.close-i{
  position: absolute;
  top:-8px;
  right: -9px;
  display: none;
  cursor: pointer;
  font-size: 18px;
  background-color: white;
  border-radius: 10px;
}
.newtag{
  position: relative;
  cursor: text;
  overflow: visible;
  display: inline-block;

}
.newtag-content{
  margin: 7px 15px;
}
</style>


<style scoped>

</style>
