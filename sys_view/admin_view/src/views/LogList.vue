<template>
    <div>
        <div>
        <el-table ref="table" :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column prop="id" label="ID" sortable></el-table-column>
            <el-table-column prop="uuid" label="UUID"></el-table-column>
            <el-table-column prop="log" label="log"></el-table-column>
            <el-table-column prop="url" label="URL" sortable></el-table-column>
            <el-table-column prop="time" label="时间" sortable></el-table-column>
            <el-table-column prop="type" label="类型" sortable></el-table-column>
        </el-table>
        </div>
        <div class="pageselect">
        <!-- PageSize Selector -->
        <el-select v-model="pageSize" placeholder="选择每页数量" @change="handleSizeChange">
            <el-option
                v-for="item in sizeOptions"
                :key="item"
                :label="item + ' 条/页'"
                :value="item">
            </el-option>
        </el-select>
        <!-- Pagination -->
        <el-pagination
            @current-change="handlePageChange"
            :current-page="currentPage"
            :page-size="pageSize"
            layout="prev, pager, next"
            :total="total">
        </el-pagination>
    </div>
    </div>
</template>

<script>
import {GetLogList} from "@/api/api";

export default {
    name: "LogList",
    data() {
        return {
            tableData: [],
            total: 0,
            currentPage: 1,
            pageSize: 20,
            sizeOptions: [5, 10, 15, 20, 50,100,500,1000,5000],  // Options for page size
        }
    },
    created() {
        this.getData(this.currentPage, this.pageSize);

    },
    methods: {
        // getData() {
        //     GetLogList().then((res) => {
        //         this.tableData = res.data.data
        //         for(let i = 0; i < this.tableData.length; i++){
        //             this.tableData[i].createTime = this.tableData[i].createTime.replace("T", " ").substr(0, 16);
        //         }
        //     })
        // }
        getData(page, pageSize) {
            GetLogList(page, pageSize).then((res) => {
                this.total = res.data.data.total;
                this.tableData = res.data.data.records;
                 for(let i = 0; i < this.tableData.length; i++){
                     this.tableData[i].createTime = this.tableData[i].createTime.replace("T", " ").substr(0, 16);
                 }
            });
        },
        handlePageChange(page) {
            this.currentPage = page;
            this.getData(this.currentPage, this.pageSize);
        },
        handleSizeChange(newSize) {
            this.pageSize = newSize;
            this.currentPage = 1; // 重置为第一页
            this.getData(this.currentPage, this.pageSize);
        },

        updateCurrentTableData() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = this.currentPage * this.pageSize;
            this.currentTableData = this.tableData.slice(start, end);
        }
    }

}
</script>

<style scoped>
.pageselect {
    position: fixed; /* 或者使用 position: sticky; */
    bottom: 0; /* 控制距离底部的距离 */
    left: 0; /* 控制距离左侧的距离 */
    width: 100%; /* 控制宽度 */
    background: white; /* 背景颜色 */
    padding: 10px; /* 内边距 */
    box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}
.el-table{
    margin-bottom: 70px; /* 根据分页控件的高度来调整这个值 */
}
</style>