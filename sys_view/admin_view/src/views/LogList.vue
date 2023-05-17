<template>
    <div>
        <el-table ref="table" :data="tableData" style="width: 100%">
            <el-table-column prop="id" label="ID" sortable></el-table-column>
            <el-table-column prop="uuid" label="UUID"></el-table-column>
            <el-table-column prop="log" label="log"></el-table-column>
            <el-table-column prop="url" label="URL" sortable></el-table-column>
            <el-table-column prop="time" label="时间" sortable></el-table-column>
            <el-table-column prop="type" label="类型" sortable></el-table-column>
        </el-table>

    </div>
</template>

<script>
import {GetLogList} from "@/api/api";

export default {
    name: "LogList",
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
            GetLogList().then((res) => {
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