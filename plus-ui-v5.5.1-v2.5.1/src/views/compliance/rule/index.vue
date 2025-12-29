<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div v-show="showSearch" class="mb-[10px]">
        <el-card shadow="hover">
          <el-form ref="queryFormRef" :model="queryParams" :inline="true">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="queryParams.ruleCode" placeholder="请输入规则编码" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="queryParams.ruleName" placeholder="请输入规则名称" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="规则类型" prop="ruleType">
              <el-select v-model="queryParams.ruleType" placeholder="请选择规则类型" clearable>
                <el-option label="硬规则" value="HARD" />
                <el-option label="语义规则" value="RAG" />
              </el-select>
            </el-form-item>
            <el-form-item label="分类" prop="category">
              <el-input v-model="queryParams.category" placeholder="请输入分类" clearable @keyup.enter="handleQuery" />
            </el-form-item>
            <el-form-item label="状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="规则状态" clearable>
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
            <el-button v-has-permi="['compliance:rule:add']" type="primary" plain icon="Plus" @click="handleAdd()">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-has-permi="['compliance:rule:edit']" type="success" plain :disabled="single" icon="Edit" @click="handleUpdate()">
              修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button v-has-permi="['compliance:rule:remove']" type="danger" plain :disabled="multiple" icon="Delete" @click="handleDelete()">
              删除
            </el-button>
          </el-col>
          <right-toolbar v-model:show-search="showSearch" :columns="columns" :search="true" @query-table="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" border :data="ruleList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column v-if="columns[0].visible" label="规则ID" align="center" prop="ruleId" width="100" />
        <el-table-column v-if="columns[1].visible" label="规则编码" align="center" prop="ruleCode" :show-overflow-tooltip="true" width="150" />
        <el-table-column v-if="columns[2].visible" label="规则名称" align="center" prop="ruleName" :show-overflow-tooltip="true" min-width="200" />
        <el-table-column v-if="columns[3].visible" label="规则类型" align="center" prop="ruleType" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.ruleType === 'HARD'" type="danger">硬规则</el-tag>
            <el-tag v-else-if="scope.row.ruleType === 'RAG'" type="success">语义规则</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[4].visible" label="分类" align="center" prop="category" width="120" />
        <el-table-column v-if="columns[5].visible" label="关键词" align="center" prop="keywords" width="200">
          <template #default="scope">
            <el-tag v-for="(keyword, index) in scope.row.keywords" :key="index" class="mr-[5px]" size="small">
              {{ keyword }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[6].visible" label="严重程度" align="center" prop="severity" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.severity === 'HIGH'" type="danger">高</el-tag>
            <el-tag v-else-if="scope.row.severity === 'MEDIUM'" type="warning">中</el-tag>
            <el-tag v-else-if="scope.row.severity === 'LOW'" type="info">低</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[7].visible" label="状态" align="center" prop="status" width="100">
          <template #default="scope">
            <el-switch v-model="scope.row.status" active-value="0" inactive-value="1" @change="handleStatusChange(scope.row)"></el-switch>
          </template>
        </el-table-column>
        <el-table-column v-if="columns[8].visible" label="排序" align="center" prop="ruleOrder" width="80" />
        <el-table-column v-if="columns[9].visible" label="创建时间" align="center" prop="createTime" width="180" />
        <el-table-column label="操作" fixed="right" width="180" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button v-hasPermi="['compliance:rule:edit']" link type="primary" icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button v-hasPermi="['compliance:rule:remove']" link type="primary" icon="Delete" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" :total="total" @pagination="getList" />
    </el-card>

    <!-- 添加或修改规则对话框 -->
    <el-dialog ref="formDialogRef" v-model="dialog.visible" :title="dialog.title" width="700px" append-to-body @close="closeDialog">
      <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="规则编码" prop="ruleCode">
              <el-input v-model="form.ruleCode" placeholder="请输入规则编码" maxlength="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规则类型" prop="ruleType">
              <el-select v-model="form.ruleType" placeholder="请选择规则类型" style="width: 100%">
                <el-option label="硬规则" value="HARD" />
                <el-option label="语义规则" value="RAG" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="规则名称" prop="ruleName">
              <el-input v-model="form.ruleName" placeholder="请输入规则名称" maxlength="200" />
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
            <el-form-item label="严重程度" prop="severity">
              <el-select v-model="form.severity" placeholder="请选择严重程度" style="width: 100%">
                <el-option label="高" value="HIGH" />
                <el-option label="中" value="MEDIUM" />
                <el-option label="低" value="LOW" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="关键词" prop="keywords">
              <el-select
                v-model="form.keywords"
                multiple
                filterable
                allow-create
                default-first-option
                :reserve-keyword="false"
                placeholder="请输入关键词，按回车添加"
                style="width: 100%"
              >
              </el-select>
              <div class="text-xs text-gray-500 mt-1">硬规则需要配置关键词数组，用于文档匹配</div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排序号" prop="ruleOrder">
              <el-input-number v-model="form.ruleOrder" :min="0" controls-position="right" style="width: 100%" />
            </el-form-item>
          </el-col>
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
            <el-form-item label="描述">
              <el-input v-model="form.description" type="textarea" placeholder="请输入规则描述" :rows="3" />
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

<script setup name="Rule" lang="ts">
import api from '@/api/compliance/rule';
import { RuleForm, RuleQuery, RuleVO } from '@/api/compliance/rule/types';
import { to } from 'await-to-js';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const { sys_normal_disable } = toRefs<any>(proxy?.useDict('sys_normal_disable'));

const ruleList = ref<RuleVO[]>([]);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<number | string>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

// 列显隐信息
const columns = ref<FieldOption[]>([
  { key: 0, label: `规则ID`, visible: false },
  { key: 1, label: `规则编码`, visible: true },
  { key: 2, label: `规则名称`, visible: true },
  { key: 3, label: `规则类型`, visible: true },
  { key: 4, label: `分类`, visible: true },
  { key: 5, label: `关键词`, visible: true },
  { key: 6, label: `严重程度`, visible: true },
  { key: 7, label: `状态`, visible: true },
  { key: 8, label: `排序`, visible: true },
  { key: 9, label: `创建时间`, visible: true }
]);

const queryFormRef = ref<ElFormInstance>();
const ruleFormRef = ref<ElFormInstance>();
const formDialogRef = ref<ElDialogInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: RuleForm = {
  ruleId: undefined,
  ruleCode: '',
  ruleName: '',
  ruleType: 'HARD',
  category: '',
  keywords: [],
  description: '',
  severity: 'MEDIUM',
  status: '0',
  ruleOrder: 0
};

const initData: PageData<RuleForm, RuleQuery> = {
  form: { ...initFormData },
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    ruleCode: '',
    ruleName: '',
    ruleType: '',
    category: '',
    status: ''
  },
  rules: {
    ruleCode: [{ required: true, message: '规则编码不能为空', trigger: 'blur' }],
    ruleName: [{ required: true, message: '规则名称不能为空', trigger: 'blur' }],
    ruleType: [{ required: true, message: '规则类型不能为空', trigger: 'change' }],
    keywords: [
      {
        required: true,
        message: '关键词不能为空',
        trigger: ['change', 'blur'],
        validator: (_rule: any, _value: any, callback: any) => {
          if (!form.value.keywords || form.value.keywords.length === 0) {
            callback(new Error('请至少添加一个关键词'));
          } else {
            callback();
          }
        }
      }
    ]
  }
};

const data = reactive<PageData<RuleForm, RuleQuery>>(initData);
const { queryParams, form, rules } = toRefs<PageData<RuleForm, RuleQuery>>(data);

/** 查询规则列表 */
const getList = async () => {
  loading.value = true;
  const res = await api.listRule(queryParams.value);
  loading.value = false;
  ruleList.value = res.rows;
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

/** 规则状态修改 */
const handleStatusChange = async (row: RuleVO) => {
  const text = row.status === '0' ? '启用' : '停用';
  try {
    await proxy?.$modal.confirm('确认要"' + text + '""' + row.ruleName + '"规则吗?');
    await api.updateRule({
      ruleId: row.ruleId,
      ruleCode: row.ruleCode,
      ruleName: row.ruleName,
      ruleType: row.ruleType,
      category: row.category,
      keywords: row.keywords,
      description: row.description,
      severity: row.severity,
      status: row.status,
      ruleOrder: row.ruleOrder
    });
    proxy?.$modal.msgSuccess(text + '成功');
  } catch (err) {
    row.status = row.status === '0' ? '1' : '0';
  }
};

/** 选择条数 */
const handleSelectionChange = (selection: RuleVO[]) => {
  ids.value = selection.map((item) => item.ruleId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
};

/** 删除按钮操作 */
const handleDelete = async (row?: RuleVO) => {
  const ruleIds = row?.ruleId || ids.value;
  const [err] = await to(proxy?.$modal.confirm('是否确认删除规则编号为"' + ruleIds + '"的数据项？') as any);
  if (!err) {
    await api.delRule(ruleIds);
    await getList();
    proxy?.$modal.msgSuccess('删除成功');
  }
};

/** 重置操作表单 */
const reset = () => {
  form.value = { ...initFormData };
  ruleFormRef.value?.resetFields();
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
  dialog.title = '新增规则';
};

/** 修改按钮操作 */
const handleUpdate = async (row?: RuleForm) => {
  reset();
  const ruleId = row?.ruleId || ids.value[0];
  const { data } = await api.getRule(ruleId);
  dialog.visible = true;
  dialog.title = '修改规则';
  Object.assign(form.value, data);
};

/** 提交按钮 */
const submitForm = () => {
  ruleFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      if (form.value.ruleId) {
        await api.updateRule(form.value);
      } else {
        await api.addRule(form.value);
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/**
 * 关闭规则弹窗
 */
const closeDialog = () => {
  dialog.visible = false;
  reset();
};

onMounted(() => {
  getList();
});
</script>

<style scoped>
.mr-\[5px\] {
  margin-right: 5px;
}
</style>
