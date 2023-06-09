<template>
  <div>
    <el-table
        :data="tableData"
        style="width: 100%"
        :border="isBorder"
        v-loading="loading"
        ref="multipleTable"
        :header-cell-style="{ background: '#f0f6ff' }"
        @selection-change="(e) => handleSelectionChange(e)"
    >
      <el-table-column
          fixed
          width="55"
          label="选择"
          align="center"
          v-if="isType == 'checkbox'"
      >
        <template v-slot="{ row }">
          <el-checkbox v-model="row.isChecked" @change="handleChecked(row)" />
        </template>
      </el-table-column>
      <el-table-column
          fixed
          width="55"
          align="center"
          type="selection"
          :reserve-selection="true"
          v-else-if="isType == 'selection'"
      />
      <el-table-column
          fixed
          :index="1"
          label="序号"
          width="56"
          type="index"
          align="center"
          v-if="isIndex"
      />
      <template v-for="(item, index) in columns">
        <!-- 插槽：文字需要特殊显示 | 操作 -->
        <el-table-column
            v-if="item.slot"
            :key="index"
            :label="item.label"
            :fixed="item.fixed"
            :align="item.align"
            :width="item.width"
            show-overflow-tooltip
        >
          <template v-slot="{ row }">
            <slot :name="item.slot" :row="row"></slot>
          </template>
        </el-table-column>
        <!-- 文字不需要特殊显示 -->
        <el-table-column
            v-else
            :key="index"
            :prop="item.prop"
            :label="item.label"
            :fixed="item.fixed"
            :width="item.width"
            :align="item.align"
            show-overflow-tooltip
        >
          <template v-slot="scope">
            <span>{{
                scope.row[item.prop] ? scope.row[item.prop] : "————"
              }}</span>
          </template>
        </el-table-column>
      </template>
    </el-table>
    <el-pagination
        v-if="isPagination"
        background
        style="text-align: right; padding: 6px 0"
        @size-change="(e) => handlePageSize(e)"
        @current-change="(e) => handlePageNumber(e)"
        :current-page="isPagination.pageNumber"
        :page-sizes="isPagination.pageSizeOptions"
        :page-size="isPagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="isPagination.total"
    />
  </div>
</template>

<script>
export default {
  name: "data-table",
  props: {
    isBorder: { type: Boolean, default: () => true }, //是否添加边框
    loading: { type: Boolean, default: () => false }, //是否展示loading效果
    isIndex: { type: Boolean, default: () => true }, //是否值展示
    isType: { type: String }, // checkbox | selection 单选 | 多选
    isPagination: { type: Object, default: () => {} }, //是否显示分页
    tableData: { type: Array, default: () => [] }, // 数据
    columns: { type: Array, default: () => [], required: true }, //表头
  },
  methods: {
    // 一页显示条数
    handlePageSize(e) {
      this.$emit("handlePageSize", e);
    },
    // 页码
    handlePageNumber(e) {
      this.$emit("handlePageNumber", e);
    },
    // 多选
    handleSelectionChange(e) {
      this.$emit("selection-change", e);
    },
    // 单选
    handleChecked(row) {
      if (row.isChecked) {
        this.tableData.map((item) => {
          if (item.id != row.id) {
            this.$set(item, "isChecked", false);
          }
        });
        this.$emit("selection-change", row);
      } else {
        this.$emit("selection-change", "");
      }
    },
  },
};
</script>

<style lang="" scoped>
</style>