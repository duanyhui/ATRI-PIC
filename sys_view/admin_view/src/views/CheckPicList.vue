<template>
    <div>
        <el-table ref="table" :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection"></el-table-column>
            <el-table-column prop="pid" label="PID"></el-table-column>
            <el-table-column label="图片">
                <template slot-scope="scope">
                    <el-image :src="scope.row.miniurl" :fit="'contain'" style="width: 200px; height: 120px;"></el-image>
                </template>
            </el-table-column>
            <el-table-column prop="info" label="标题"></el-table-column>
            <el-table-column prop="author" label="作者"></el-table-column>
            <el-table-column prop="source" label="来源"></el-table-column>
            <el-table-column prop="mail" label="邮箱"></el-table-column>
            <el-table-column prop="tags" label="Tag">
                <template slot-scope="scope">
                    <span v-for="(item, index) in scope.row.tags" :key="index">{{ item }}、</span>
                </template>
            </el-table-column>
        </el-table>
        <el-button type="primary" @click="Accept">通过</el-button>
        <el-button type="primary" @click="Delete">删除</el-button>
        <el-button type="primary" @click="AllAccept">全部通过</el-button>
    </div>
</template>

<script>
import axios from 'axios'
import {AcceptAllPic, AcceptPic, DeletePic, GetCheckPicList} from "@/api/api";

export default {
    data() {
        return {
            tableData: [],
            selectedPids: []
        }
    },
    created() {
        this.getData()
    },
    methods: {
        getData() {
            GetCheckPicList().then((res) => {
                this.tableData = res.data.data
            })
        },

        onSubmit() {
            console.log(this.selectedPids)
        },
        Accept(){
            AcceptPic(this.selectedPids).then((res) => {
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
        AllAccept(){
            // 二次确认
            this.$confirm('此操作将通过所有图片，是否继续？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                AcceptAllPic().then((res) => { // 加上括号
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
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消操作'
                });
            });
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