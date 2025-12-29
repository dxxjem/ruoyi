<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="法规编码" prop="regulationCode">
              <el-input v-model="queryParams.regulationCode" placeholder="请输入法规编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="法规名称" prop="regulationName">
              <el-input v-model="queryParams.regulationName" placeholder="请输入法规名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="分类" prop="category">
              <el-input v-model="queryParams.category" placeholder="请输入分类" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="法规状态" clearable>
                <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </transition>

    <el-card shadow="hover">
      <template #header>
        <el-row :gutter="10">
          <el-col :span="1.5">
            <el-button v-has-permi="['compliance:regulation:add']" type="primary" plain icon="Plus" @click="handleAdd()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-has-permi="['compliance:regulation:edit']" type="success" plain :disabled="single" icon="Edit" @click="handleUpdate()">
              修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-has-permi="['compliance:regulation:remove']" type="danger" plain :disabled="multiple" icon="Delete" @click="handleDelete()">
              删除
            </el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" :columns="columns" :search="true" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="regulationList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column v-if="columns[0].visible" label="法规ID" align="center" prop="regulationId" width="100" />
        <el-table-column v-if="columns[1].visible" label="法规编码" align="center" prop="regulationCode" :show-overflow-tooltip="true" />
        <el-table-column
          v-if="columns[2].visible"
          label="法规名称"
          align="center"
          prop="regulationName"
          :show-overflow-tooltip="true"
          min-width="200"
        />
        <el-table-column v-if="columns[3].visible" label="分类" align="center" prop="category" width="120" />
        <el-table-column v-if="columns[4].visible" label="生效日期" align="center" prop="effectiveDate" width="120" />
        <el-table-column v-if="columns[5].visible" label="状态" align="center" prop="status" width="100">
          <template #default="scope">
            <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[6].visible" label="文件大小" align="center" prop="fileSize" width="120">
          <template #default="scope">
            <span v-if="scope.row.fileSize">{{ formatFileSize(scope.row.fileSize) }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[7].visible" label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" fixed="right" width="180" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['compliance:regulation:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['compliance:regulation:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>

    <!-- 添加或修改法规对话框 -->
    <el-dialog ref="formDialogRef" v-model="dialog.visible" :title="dialog.title" width="600px" append-to-body @close="closeDialog">
      <el-form ref="regulationFormRef" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="法规编码" prop="regulationCode">
              <el-input v-model="form.regulationCode" placeholder="请输入法规编码" maxlength="100" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="法规名称" prop="regulationName">
              <el-input v-model="form.regulationName" placeholder="请输入法规名称" maxlength="500" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="分类" prop="category">
              <el-input v-model="form.category" placeholder="请输入分类" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生效日期" prop="effectiveDate">
              <el-date-picker v-model="form.effectiveDate" type="date" placeholder="请选择生效日期" value-format="YYYY-MM-DD" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="法规文件" prop="fileUrl">
              <file-upload v-model="form.fileUrl" :limit="1" :file-size="50" :file-type="['pdf', 'doc', 'docx']" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel()">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Regulation" lang="ts">
import api from '@/api/compliance/regulation';
import { RegulationForm, RegulationQuery, RegulationVO } from '@/api/compliance/regulation/types';
import { to } from 'await-to-js';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));

const regulationList = ref<RegulationVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number | string>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

// 列显隐信息
const columns = ref<FieldOption[]>([
  { key: 0, label: `法规ID`, visible: false },
  { key: 1, label: `法规编码`, visible: true },
  { key: 2, label: `法规名称`, visible: true },
  { key: 3, label: `分类`, visible: true },
  { key: 4, label: `生效日期`, visible: true },
  { key: 5, label: `状态`, visible: true },
  { key: 6, label: `文件大小`, visible: true },
  { key: 7, label: `创建时间`, visible: true }
]);

const queryFormRef = ref<ElFormInstance>();
const regulationFormRef = ref<ElFormInstance>();
const formDialogRef = ref<ElDialogInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: RegulationForm = {
  regulationId: undefined,
  regulationCode: '',
  regulationName: '',
  category: '',
  effectiveDate: '',
  status: '0',
  fileUrl: '',
  fileSize: undefined,
  remark: ''
};

const initData: PageData<RegulationForm, RegulationQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    regulationCode: '',
    regulationName: '',
    category: '',
    status: ''
  },
  rules: {
    regulationCode: [{ required: true, message: '法规编码不能为空', trigger: 'blur' }],
    regulationName: [{ required: true, message: '法规名称不能为空', trigger: 'blur' }]
  }
};

const data = reactive<PageData<RegulationForm, RegulationQuery>>(initData);
const { queryParams, form, rules } = toRefs<PageData<RegulationForm, RegulationQuery>>(data);

/** 查询法规列表 */
const getList = async () => {
  loading.value = true;
  const res = await api.listRegulation(queryParams.value);
  loading.value = false;
  regulationList.value = res.rows;
  total.value = res.total;
};

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
};

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  queryParams.value.pageNum = 1;
  handleQuery();
};

/** 法规状态修改 */
const handleStatusChange = async (row: RegulationVO) => {
  const text = row.status === '0' ? '启用' : '停用';
  try {
    await proxy?.$modal.confirm('确认要"' + text + '""' + row.regulationName + '"法规吗?');
    await api.updateRegulation({
      regulationId: row.regulationId,
      regulationCode: row.regulationCode,
      regulationName: row.regulationName,
      category: row.category,
      effectiveDate: row.effectiveDate,
      status: row.status,
      fileUrl: row.fileUrl,
      fileSize: row.fileSize,
      remark: row.remark
    });
    proxy?.$modal.msgSuccess(text + '成功');
  } catch (err) {
    row.status = row.status === '0' ? '1' : '0';
  }
};

/** 选择条数 */
const handleSelectionChange = (selection: RegulationVO[]) => {
  ids.value = selection.map((item) => item.regulationId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 删除按钮操作 */
const handleDelete = async (row?: RegulationVO) => {
  const regulationIds = row?.regulationId || ids.value;
  const [err] = await to(proxy?.$modal.confirm('是否确认删除法规编号为"' + regulationIds + '"的数据项？') as any);
  if (!err) {
    await api.delRegulation(regulationIds);
    await getList();
    proxy?.$modal.msgSuccess('删除成功');
  }
};

/** 重置操作表单 */
const reset = () => {
  form.value = { ...initFormData };
  regulationFormRef.value?.resetFields();
};

/** 取消按钮 */
const cancel = () => {
  dialog.visible = false;
  reset();
};

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = '新增法规';
};

/** 修改按钮操作 */
const handleUpdate = async (row?: RegulationForm) => {
  reset();
  const regulationId = row?.regulationId || ids.value[0];
  const { data } = await api.getRegulation(regulationId);
  dialog.visible = true;
  dialog.title = '修改法规';
  Object.assign(form.value, data);
};

/** 提交按钮 */
const submitForm = () => {
  regulationFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      if (form.value.regulationId) {
        await api.updateRegulation(form.value);
      } else {
        await api.addRegulation(form.value);
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 格式化文件大小 */
const formatFileSize = (bytes?: number): string => {
  if (!bytes) return '-';
  if (bytes < 1024) return bytes + ' B';
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB';
  return (bytes / 1024 / 1024).toFixed(2) + ' MB';
};

/**
 * 关闭法规弹窗
 */
const closeDialog = () => {
  dialog.visible = false;
  reset();
};

onMounted(() => {
  getList();
});
</script>
