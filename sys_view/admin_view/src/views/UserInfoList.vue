<template>
    <div>
        <el-table ref="table" :data="tableData" style="width: 100%">
            <el-table-column prop="uid" label="UID"  sortable></el-table-column>
            <el-table-column prop="uuid" label="UUID"></el-table-column>
            <el-table-column prop="name" label="名字" sortable></el-table-column>
            <el-table-column prop="createTime" label="创建时间" sortable></el-table-column>
            <el-table-column prop="ip" label="IP" sortable></el-table-column>
            <el-table-column prop="address" label="地区" sortable></el-table-column>
            <el-table-column prop="browser" label="浏览器" sortable></el-table-column>
            <el-table-column prop="os" label="操作系统" sortable></el-table-column>
        </el-table>

    </div>
</template>

<script>


import {GetLogList, GetUserInfoList} from "@/api/api";


export default {
    name: "UserInfoList",
    data() {
        return {
            tableData: []
        }
    },
    created() {
        this.getData()
    },
    methods: {
        getData() {
            GetUserInfoList().then((res) => {
                this.tableData = res.data.data
                for(let i = 0; i < this.tableData.length; i++){
                    this.tableData[i].createTime = this.tableData[i].createTime.replace("T", " ").substr(0, 16);
                }
            })
        }
    }
}
</script>

<style scoped>

</style>