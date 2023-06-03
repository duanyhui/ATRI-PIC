<template>
    <div>
        <el-table ref="table" :data="currentTableData" style="width: 100%" @selection-change="handleSelectionChange">
            <el-table-column type="selection"></el-table-column>
            <el-table-column prop="pid" sortable label="PID"></el-table-column>
            <el-table-column label="图片">
                <template slot-scope="scope">
                    <el-image :src="scope.row.miniurl" :fit="'contain'" style="width: 200px; height: 120px;"></el-image>
                </template>
            </el-table-column>
            <el-table-column prop="info" label="标题"></el-table-column>
            <el-table-column prop="updatetime" sortable label="上传时间"></el-table-column>
            <el-table-column prop="tags" label="Tag">
                <template slot-scope="scope">
                    <span v-for="(item, index) in scope.row.tags" :key="index">{{ item }}、</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="400px">
                <template slot-scope="scope">
                    <el-input v-model="inputTag" placeholder="Enter new tag"></el-input>
                    <div>
                        <el-tag v-for="(tag, index) in commonTags" :key="index" @click="addCommonTag(tag)">{{ tag }}</el-tag>
                    </div>
                    <el-button type="primary" @click="addTag(scope.row, inputTag)">添加标签</el-button>
                    <el-button type="danger" @click="deleteTag(scope.row, inputTag)">删除标签</el-button>
                </template>
            </el-table-column>
        </el-table>
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
                :total="tableData.length">
        </el-pagination>
    </div>
</template>

<script>
import {AddPicTag, DeletePic, DeletePicTag, ForbidPic, GetPicList} from "@/api/api";

export default {
    name: "OpPicTag",
    data() {
        return {
            tableData: [],
            currentTableData: [],
            selectedPids: [],
            currentPage: 1,
            pageSize: 5,
            sizeOptions: [5, 10, 15, 20, 50],  // Options for page size
            inputTag: '',
            commonTags: ['亚托莉', '表情包', 'CG原画','动漫','插画','手绘','游戏截图']
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
                this.updateCurrentTableData();
            })
        },
        handleSelectionChange(selections) {
            //插入选中的pid
            this.selectedPids = []
            for (let i = 0; i < selections.length; i++) {
                this.selectedPids.push(selections[i].pid)
            }
        },
        addTag(row, tag) {
           AddPicTag(row.pid, tag).then((res) => {
               this.getData()
               if(res.data.code === 200){
                   this.$message({
                       message: res.data.msg,
                       type: 'success'
                   })
           }
               else {
                   this.$message({
                       message: res.data.msg,
                       type: 'error'
                   })
               }
           }
               )
        },
        deleteTag(row, tag) {
            // 在这里实现删除标签的函数
            DeletePicTag(row.pid, tag).then((res) => {
                this.getData()
                if(res.data.code === 200){
                    this.$message({
                        message: res.data.msg,
                        type: 'success'
                    })
                }
                else {
                    this.$message({
                        message: res.data.msg,
                        type: 'error'
                    })
                }
            }
            )
        },
        addCommonTag(tag) {
            this.inputTag = tag;
        },
        handlePageChange(page) {
            this.currentPage = page;
            this.updateCurrentTableData();
        },
        handleSizeChange() {
            this.currentPage = 1; // reset current page
            this.updateCurrentTableData();
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

</style>
