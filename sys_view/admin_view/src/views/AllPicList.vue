<template>
    <div>
        <el-table ref="table" :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection"></el-table-column>
            <el-table-column prop="pid" sortable label="PID"></el-table-column>
            <el-table-column label="图片">
                <template slot-scope="scope">
                    <el-image :src="scope.row.miniurl" :fit="'contain'" style="width: 200px; height: 120px;"></el-image>
                </template>
            </el-table-column>
            <el-table-column prop="info" label="标题"></el-table-column>
            <el-table-column prop="author" label="作者"></el-table-column>
            <el-table-column prop="source" label="来源"></el-table-column>
            <el-table-column prop="updatetime" sortable label="上传时间"></el-table-column>
            <el-table-column prop="status" sortable label="状态"></el-table-column>
            <el-table-column prop="tags" label="Tag">
                <template slot-scope="scope">
                    <span v-for="(item, index) in scope.row.tags" :key="index">{{ item }}、</span>
                </template>
            </el-table-column>
        </el-table>
        <el-button type="primary" @click="Forbid">禁止</el-button>
        <el-button type="primary" @click="Delete">删除</el-button>
    </div>
</template>

<script>
import {DeletePic, ForbidPic, GetPicList} from "@/api/api";

export default {
    name: "AllPicList",
    data() {
        return {
            tableData: [],
            selectedPids: []
        }
    },
    created() {
        this.getData()
    },
    methods:{
        getData() {
            GetPicList().then((res) => {
                this.tableData = res.data.data
                for(let i = 0; i < this.tableData.length; i++){
                    this.tableData[i].updatetime = this.tableData[i].updatetime.replace("T", " ").substr(0, 16);
                }

            })
        },
        Delete(){
            //传一个pid数组
            DeletePic(this.selectedPids).then((res) => {
                if (res.data.code === 200) {
                    this.$message({
                        message: '操作成功',
                        type: 'success'
                    })
                    this.getData()
                } else {
                    this.$message({
                        message: '操作失败',
                        type: 'error'
                    })
                }
            })
        },
        Forbid(){
            //传一个pid数组
            ForbidPic(this.selectedPids).then((res) => {
                if (res.data.code === 200) {
                    this.$message({
                        message: '操作成功',
                        type: 'success'
                    })
                    this.getData()
                } else {
                    this.$message({
                        message: '操作失败',
                        type: 'error'
                    })
                }
            })
        },
        handleSelectionChange(selections) {
            //插入选中的pid
            this.selectedPids = []
            for (let i = 0; i < selections.length; i++) {
                this.selectedPids.push(selections[i].pid)
            }
        }
    }

}
</script>

<style scoped>

</style>