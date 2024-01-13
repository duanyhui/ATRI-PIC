<template>
    <div>
        <el-table ref="table" :data="tableData" style="width: 100%" >
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
            <el-table-column prop="mail" label="邮箱"></el-table-column>
            <el-table-column prop="address" label="地区"></el-table-column>
            <el-table-column prop="updatetime" sortable label="上传时间"></el-table-column>
            <el-table-column prop="status" sortable label="状态"></el-table-column>
            <el-table-column prop="tags" label="Tag">
                <template slot-scope="scope">
                    <span v-for="(item, index) in scope.row.tags" :key="index">{{ item }}、</span>
                </template>
            </el-table-column>
            <el-table-column prop="seenum" sortable label="浏览量"></el-table-column>
            <el-table-column prop="likenum" sortable label="喜欢数"></el-table-column>
        </el-table>
<!--        <el-button type="primary" @click="Forbid">禁止</el-button>-->
<!--        <el-button type="primary" @click="Delete">删除</el-button>-->
        <div class="pageselect">
            <el-button type="primary" @click="Forbid">禁止</el-button>
            <el-button type="primary" @click="Delete">删除</el-button>
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
import {DeletePic, ForbidPic, GetPicList} from "@/api/api";

export default {
    name: "AllPicList",
    data() {
        return {
            tableData: [],
            selectedPids: [],
            total: 0,
            currentPage: 1,
            pageSize: 20,
            sizeOptions: [5, 10, 15, 20, 50, 100, 500, 1000, 5000],  // Options for page size
        }
    },
    created() {
        this.getData(this.currentPage, this.pageSize)
    },
    methods: {
        getData(page, pageSize) {
            GetPicList(page, pageSize).then((res) => {
                this.total = res.data.data.total;
                this.tableData = res.data.data.records;
                for (let i = 0; i < this.tableData.length; i++) {
                    this.tableData[i].createTime = this.tableData[i].createTime.replace("T", " ").substr(0, 16);
                }
            })
        },
        Delete() {
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
        Forbid() {
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
