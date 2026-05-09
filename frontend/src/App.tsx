import { FormEvent, useEffect, useMemo, useState } from 'react';

import {
  ActualImportBatch,
  ActualImportRow,
  commitActualImport,
  listActualImportBatches,
  listActualImportRows,
  validateActualImport,
} from './features/actualImports/actualImportApi';
import {
  AuditAction,
  AuditEvent,
  PageResponse,
  searchAuditEvents,
} from './features/audit/auditApi';
import {
  activateBudgetModel,
  bindBudgetModelDimension,
  BudgetModel,
  BudgetModelDimensionBinding,
  createBudgetModel,
  deactivateBudgetModel,
  listBudgetModelDimensions,
  listBudgetModels,
} from './features/budgetModels/budgetModelApi';
import {
  activateBudgetTemplate,
  BudgetTemplate,
  createBudgetTemplate,
  createTemplateAxis,
  deactivateBudgetTemplate,
  listBudgetTemplates,
  listTemplateAxes,
  TemplateAxis,
  TemplateAxisType,
} from './features/budgetTemplates/budgetTemplateApi';
import {
  analyzeBudgetActualVariance,
  BudgetActualVarianceRow,
  exportFactsCsv,
  FactQueryRow,
  FactSummaryRow,
  queryFacts,
  QueryGroupBy,
  summarizeFacts,
} from './features/budgetQuery/budgetQueryApi';
import {
  createDimension,
  createMember,
  createWorkspace,
  Dimension,
  DimensionMember,
  DimensionType,
  listDimensions,
  listMembers,
  listWorkspaces,
  Workspace,
} from './features/metadata/metadataApi';
import {
  createSecurityUser,
  CurrentUser,
  EntityScope,
  getCurrentUser,
  grantEntityScope,
  grantUserRole,
  listEntityScopes,
  listSecurityUsers,
  listUserRoles,
  SecurityRoleCode,
  SecurityUser,
  UserRole,
} from './features/security/securityApi';
import {
  approveSubmissionTask,
  createSubmissionTask,
  FactValueStatus,
  FactValue,
  listFactValues,
  listSubmissionTasks,
  lockSubmissionTask,
  returnSubmissionTask,
  saveFactValue,
  SubmissionTask,
  submitSubmissionTask,
} from './features/submissions/submissionApi';
import { isDevSecurityContextEnabled, setApiSecurityContext } from './shared/api/http';

const dimensionTypes: DimensionType[] = [
  'ACCOUNT',
  'ENTITY',
  'TIME',
  'CATEGORY',
  'VERSION',
  'CUSTOM',
];

const axisTypes: TemplateAxisType[] = ['ROW', 'COLUMN', 'FILTER'];

const queryGroupByOptions: QueryGroupBy[] = ['ACCOUNT', 'ENTITY', 'TIME', 'CATEGORY', 'VERSION'];

const auditActionOptions = [
  'CREATE',
  'UPDATE',
  'DELETE',
  'STATUS_CHANGE',
  'IMPORT',
  'ACCESS_CHANGE',
  'AUTH_FAILURE',
] satisfies AuditAction[];

const securityRoleOptions = [
  'BUDGET_ADMIN',
  'METADATA_MANAGER',
  'TEMPLATE_DESIGNER',
  'BUDGET_OWNER',
  'BUDGET_REVIEWER',
  'IMPORT_OPERATOR',
  'READ_ONLY',
] satisfies SecurityRoleCode[];

const initialScopeMembers: Record<DimensionType, DimensionMember[]> = {
  ACCOUNT: [],
  ENTITY: [],
  TIME: [],
  CATEGORY: [],
  VERSION: [],
  CUSTOM: [],
};

function formatActionError(caught: unknown) {
  if (!caught || typeof caught !== 'object') {
    return 'Request failed. Confirm the backend is running.';
  }

  const apiError = caught as { code?: unknown; message?: unknown; traceId?: unknown };
  const message =
    typeof apiError.message === 'string'
      ? apiError.message
      : 'Request failed. Confirm the backend is running.';

  if (apiError.code === 'UNAUTHORIZED' || apiError.code === 'FORBIDDEN') {
    return `${apiError.code}: ${message}`;
  }

  return message;
}

function uniqueRoleCount(roles: UserRole[]) {
  return new Set(roles.map((role) => `${role.workspaceCode}:${role.roleCode}`)).size;
}

function App() {
  const devSecurityContextEnabled = isDevSecurityContextEnabled();
  const [workspaces, setWorkspaces] = useState<Workspace[]>([]);
  const [dimensions, setDimensions] = useState<Dimension[]>([]);
  const [members, setMembers] = useState<DimensionMember[]>([]);
  const [budgetModels, setBudgetModels] = useState<BudgetModel[]>([]);
  const [modelBindings, setModelBindings] = useState<BudgetModelDimensionBinding[]>([]);
  const [budgetTemplates, setBudgetTemplates] = useState<BudgetTemplate[]>([]);
  const [templateAxes, setTemplateAxes] = useState<TemplateAxis[]>([]);
  const [securityUsers, setSecurityUsers] = useState<SecurityUser[]>([]);
  const [userRoles, setUserRoles] = useState<UserRole[]>([]);
  const [entityScopes, setEntityScopes] = useState<EntityScope[]>([]);
  const [currentUser, setCurrentUser] = useState<CurrentUser | null>(null);
  const [auditEvents, setAuditEvents] = useState<AuditEvent[]>([]);
  const [auditPage, setAuditPage] = useState<PageResponse<AuditEvent>>({
    items: [],
    page: 0,
    size: 10,
    totalElements: 0,
    totalPages: 0,
  });
  const [scopeMembers, setScopeMembers] =
    useState<Record<DimensionType, DimensionMember[]>>(initialScopeMembers);
  const [submissionTasks, setSubmissionTasks] = useState<SubmissionTask[]>([]);
  const [factValues, setFactValues] = useState<FactValue[]>([]);
  const [queryRows, setQueryRows] = useState<FactQueryRow[]>([]);
  const [summaryRows, setSummaryRows] = useState<FactSummaryRow[]>([]);
  const [varianceRows, setVarianceRows] = useState<BudgetActualVarianceRow[]>([]);
  const [csvExport, setCsvExport] = useState('');
  const [actualImportBatches, setActualImportBatches] = useState<ActualImportBatch[]>([]);
  const [actualImportRows, setActualImportRows] = useState<ActualImportRow[]>([]);
  const [selectedWorkspaceId, setSelectedWorkspaceId] = useState('');
  const [selectedDimensionId, setSelectedDimensionId] = useState('');
  const [selectedSecurityUserId, setSelectedSecurityUserId] = useState('');
  const [selectedBudgetModelId, setSelectedBudgetModelId] = useState('');
  const [selectedBudgetTemplateId, setSelectedBudgetTemplateId] = useState('');
  const [selectedSubmissionTaskId, setSelectedSubmissionTaskId] = useState('');
  const [selectedImportBatchId, setSelectedImportBatchId] = useState('');
  const [workspaceDraft, setWorkspaceDraft] = useState({ code: '', name: '' });
  const [dimensionDraft, setDimensionDraft] = useState({
    code: '',
    name: '',
    dimensionType: 'ACCOUNT' as DimensionType,
    system: true,
  });
  const [memberDraft, setMemberDraft] = useState({
    code: '',
    name: '',
    parentId: '',
    sortOrder: '',
  });
  const [budgetModelDraft, setBudgetModelDraft] = useState({
    code: '',
    name: '',
    description: '',
  });
  const [bindingDraft, setBindingDraft] = useState({
    dimensionId: '',
    requiredForEntry: true,
    displayOrder: '',
  });
  const [templateDraft, setTemplateDraft] = useState({
    code: '',
    name: '',
    description: '',
  });
  const [axisDraft, setAxisDraft] = useState({
    modelDimensionId: '',
    axisType: 'ROW' as TemplateAxisType,
    memberSelector: 'ALL_LEAF',
    displayOrder: '',
  });
  const [submissionDraft, setSubmissionDraft] = useState({
    entityMemberId: '',
    timeMemberId: '',
    categoryMemberId: '',
    versionMemberId: '',
    ownerUser: 'owner@example.com',
    reviewerUser: 'reviewer@example.com',
  });
  const [factValueDraft, setFactValueDraft] = useState({
    accountMemberId: '',
    amount: '',
    note: '',
  });
  const [queryDraft, setQueryDraft] = useState({
    entityMemberId: '',
    timeMemberId: '',
    categoryMemberId: '',
    versionMemberId: '',
    status: 'APPROVED' as '' | FactValueStatus,
    groupBy: 'ACCOUNT' as QueryGroupBy,
  });
  const [varianceDraft, setVarianceDraft] = useState({
    entityMemberId: '',
    timeMemberId: '',
    budgetCategoryMemberId: '',
    actualCategoryMemberId: '',
    budgetVersionMemberId: '',
    actualVersionMemberId: '',
    status: '' as '' | FactValueStatus,
  });
  const [actualImportDraft, setActualImportDraft] = useState({
    fileName: 'actual-expense.csv',
    operatorUser: 'importer@example.com',
    csvContent: 'account,entity,time,category,version,amount\nTRAVEL,OPS,2026M03,ACTUAL,FINAL,1200.00',
  });
  const [securityUserDraft, setSecurityUserDraft] = useState({
    username: '',
    displayName: '',
    email: '',
  });
  const [roleGrantDraft, setRoleGrantDraft] = useState({
    roleCode: 'READ_ONLY' as SecurityRoleCode,
  });
  const [entityScopeDraft, setEntityScopeDraft] = useState({
    entityMemberId: '',
    includeDescendants: true,
  });
  const [auditDraft, setAuditDraft] = useState({
    actorId: '',
    subjectType: '',
    subjectId: '',
    action: '' as '' | AuditAction,
  });
  const [returnReason, setReturnReason] = useState('');
  const [loading, setLoading] = useState(false);
  const [securityContext, setSecurityContext] = useState({
    userId: 'admin@example.com',
    roles: 'BUDGET_ADMIN',
  });
  const [notice, setNotice] = useState(
    devSecurityContextEnabled ? 'Development identity is using BUDGET_ADMIN.' : 'Ready.',
  );
  const [error, setError] = useState('');

  const selectedWorkspace = useMemo(
    () => workspaces.find((workspace) => workspace.id === selectedWorkspaceId),
    [selectedWorkspaceId, workspaces],
  );

  const selectedDimension = useMemo(
    () => dimensions.find((dimension) => dimension.id === selectedDimensionId),
    [dimensions, selectedDimensionId],
  );

  const selectedSecurityUser = useMemo(
    () => securityUsers.find((user) => user.id === selectedSecurityUserId),
    [securityUsers, selectedSecurityUserId],
  );

  const selectedBudgetModel = useMemo(
    () => budgetModels.find((budgetModel) => budgetModel.id === selectedBudgetModelId),
    [budgetModels, selectedBudgetModelId],
  );

  const selectedBudgetTemplate = useMemo(
    () => budgetTemplates.find((template) => template.id === selectedBudgetTemplateId),
    [budgetTemplates, selectedBudgetTemplateId],
  );

  const selectedSubmissionTask = useMemo(
    () => submissionTasks.find((task) => task.id === selectedSubmissionTaskId),
    [selectedSubmissionTaskId, submissionTasks],
  );

  const unboundDimensions = useMemo(() => {
    const boundIds = new Set(modelBindings.map((binding) => binding.dimensionId));
    return dimensions.filter((dimension) => !boundIds.has(dimension.id));
  }, [dimensions, modelBindings]);

  const unusedTemplateBindings = useMemo(() => {
    const usedBindingIds = new Set(templateAxes.map((axis) => axis.modelDimensionId));
    return modelBindings.filter((binding) => !usedBindingIds.has(binding.id));
  }, [modelBindings, templateAxes]);

  useEffect(() => {
    setApiSecurityContext(securityContext);
  }, [securityContext]);

  useEffect(() => {
    void refreshWorkspaces();
    void refreshSecurityUsers();
    void refreshCurrentUser();
  }, []);

  useEffect(() => {
    if (!selectedWorkspaceId) {
      setDimensions([]);
      setSelectedDimensionId('');
      setBudgetModels([]);
      setSelectedBudgetModelId('');
      setBudgetTemplates([]);
      setSelectedBudgetTemplateId('');
      setScopeMembers(initialScopeMembers);
      setSubmissionTasks([]);
      setSelectedSubmissionTaskId('');
      setQueryRows([]);
      setSummaryRows([]);
      setVarianceRows([]);
      setCsvExport('');
      setActualImportBatches([]);
      setActualImportRows([]);
      setSelectedImportBatchId('');
      return;
    }
    void refreshDimensions(selectedWorkspaceId);
    void refreshBudgetModels(selectedWorkspaceId);
  }, [selectedWorkspaceId]);

  useEffect(() => {
    if (!selectedDimensionId) {
      setMembers([]);
      return;
    }
    void refreshMembers(selectedDimensionId);
  }, [selectedDimensionId]);

  useEffect(() => {
    void refreshScopeMembers(dimensions);
  }, [dimensions]);

  useEffect(() => {
    if (!selectedSecurityUserId) {
      setUserRoles([]);
      setEntityScopes([]);
      return;
    }
    void refreshSecurityGrants(selectedSecurityUserId, selectedWorkspaceId);
  }, [selectedSecurityUserId, selectedWorkspaceId]);

  useEffect(() => {
    if (!selectedBudgetModelId) {
      setModelBindings([]);
      setBudgetTemplates([]);
      setSelectedBudgetTemplateId('');
      setQueryRows([]);
      setSummaryRows([]);
      setVarianceRows([]);
      setCsvExport('');
      setActualImportBatches([]);
      setActualImportRows([]);
      setSelectedImportBatchId('');
      return;
    }
    void refreshModelBindings(selectedBudgetModelId);
    void refreshBudgetTemplates(selectedBudgetModelId);
    void refreshActualImportBatches(selectedBudgetModelId);
  }, [selectedBudgetModelId]);

  useEffect(() => {
    if (!selectedBudgetTemplateId) {
      setTemplateAxes([]);
      setSubmissionTasks([]);
      setSelectedSubmissionTaskId('');
      return;
    }
    void refreshTemplateAxes(selectedBudgetTemplateId);
    void refreshSubmissionTasks(selectedBudgetTemplateId);
  }, [selectedBudgetTemplateId]);

  useEffect(() => {
    if (!selectedSubmissionTaskId) {
      setFactValues([]);
      return;
    }
    void refreshFactValues(selectedSubmissionTaskId);
  }, [selectedSubmissionTaskId]);

  useEffect(() => {
    if (!selectedImportBatchId) {
      setActualImportRows([]);
      return;
    }
    void refreshActualImportRows(selectedImportBatchId);
  }, [selectedImportBatchId]);

  async function runAction(action: () => Promise<void>) {
    setLoading(true);
    setError('');
    try {
      await action();
    } catch (caught) {
      setError(formatActionError(caught));
    } finally {
      setLoading(false);
    }
  }

  async function refreshWorkspaces() {
    await runAction(async () => {
      const nextWorkspaces = await listWorkspaces();
      setWorkspaces(nextWorkspaces);
      if (!selectedWorkspaceId && nextWorkspaces.length > 0) {
        setSelectedWorkspaceId(nextWorkspaces[0].id);
      }
      setNotice('Workspace metadata loaded.');
    });
  }

  async function refreshSecurityUsers() {
    await runAction(async () => {
      const nextUsers = await listSecurityUsers();
      setSecurityUsers(nextUsers);
      setSelectedSecurityUserId((current) =>
        nextUsers.some((user) => user.id === current) ? current : nextUsers[0]?.id ?? '',
      );
      setNotice('Security users loaded.');
    });
  }

  async function refreshCurrentUser() {
    await runAction(async () => {
      const nextCurrentUser = await getCurrentUser();
      setCurrentUser(nextCurrentUser ?? null);
      setNotice(
        nextCurrentUser?.authenticated
          ? `Current user ${nextCurrentUser.userId ?? 'unknown'} resolved.`
          : 'Authentication required.',
      );
    });
  }

  async function refreshSecurityGrants(userId: string, workspaceId: string) {
    await runAction(async () => {
      const [nextRoles, nextScopes] = await Promise.all([
        listUserRoles(userId, workspaceId || undefined),
        workspaceId ? listEntityScopes(userId, workspaceId) : Promise.resolve([]),
      ]);
      setUserRoles(nextRoles);
      setEntityScopes(nextScopes);
      setNotice('Security grants loaded.');
    });
  }

  async function refreshDimensions(workspaceId: string) {
    await runAction(async () => {
      const nextDimensions = await listDimensions(workspaceId);
      setDimensions(nextDimensions);
      setSelectedDimensionId((current) =>
        nextDimensions.some((dimension) => dimension.id === current)
          ? current
          : nextDimensions[0]?.id ?? '',
      );
      setNotice('Dimension metadata loaded.');
    });
  }

  async function refreshMembers(dimensionId: string) {
    await runAction(async () => {
      setMembers(await listMembers(dimensionId));
      setNotice('Member hierarchy loaded.');
    });
  }

  async function refreshScopeMembers(nextDimensions: Dimension[]) {
    if (nextDimensions.length === 0) {
      setScopeMembers(initialScopeMembers);
      return;
    }

    await runAction(async () => {
      const nextScopeMembers: Record<DimensionType, DimensionMember[]> = {
        ACCOUNT: [],
        ENTITY: [],
        TIME: [],
        CATEGORY: [],
        VERSION: [],
        CUSTOM: [],
      };
      for (const dimension of nextDimensions) {
        if (dimension.dimensionType === 'CUSTOM') {
          continue;
        }
        nextScopeMembers[dimension.dimensionType] = [
          ...nextScopeMembers[dimension.dimensionType],
          ...(await listMembers(dimension.id)),
        ];
      }
      setScopeMembers(nextScopeMembers);
      setNotice('Submission scope members loaded.');
    });
  }

  async function refreshBudgetModels(workspaceId: string) {
    await runAction(async () => {
      const nextBudgetModels = await listBudgetModels(workspaceId);
      setBudgetModels(nextBudgetModels);
      setSelectedBudgetModelId((current) =>
        nextBudgetModels.some((budgetModel) => budgetModel.id === current)
          ? current
          : nextBudgetModels[0]?.id ?? '',
      );
      setNotice('Budget models loaded.');
    });
  }

  async function refreshModelBindings(budgetModelId: string) {
    await runAction(async () => {
      setModelBindings(await listBudgetModelDimensions(budgetModelId));
      setNotice('Budget model dimensions loaded.');
    });
  }

  async function refreshBudgetTemplates(budgetModelId: string) {
    await runAction(async () => {
      const nextTemplates = await listBudgetTemplates(budgetModelId);
      setBudgetTemplates(nextTemplates);
      setSelectedBudgetTemplateId((current) =>
        nextTemplates.some((template) => template.id === current)
          ? current
          : nextTemplates[0]?.id ?? '',
      );
      setNotice('Budget templates loaded.');
    });
  }

  async function refreshTemplateAxes(budgetTemplateId: string) {
    await runAction(async () => {
      setTemplateAxes(await listTemplateAxes(budgetTemplateId));
      setNotice('Template axes loaded.');
    });
  }

  async function refreshSubmissionTasks(budgetTemplateId: string) {
    await runAction(async () => {
      const nextTasks = await listSubmissionTasks(budgetTemplateId);
      setSubmissionTasks(nextTasks);
      setSelectedSubmissionTaskId((current) =>
        nextTasks.some((task) => task.id === current) ? current : nextTasks[0]?.id ?? '',
      );
      setNotice('Submission tasks loaded.');
    });
  }

  async function refreshFactValues(taskId: string) {
    await runAction(async () => {
      setFactValues(await listFactValues(taskId));
      setNotice('Submission values loaded.');
    });
  }

  async function refreshActualImportBatches(budgetModelId: string) {
    await runAction(async () => {
      const nextBatches = await listActualImportBatches(budgetModelId);
      setActualImportBatches(nextBatches);
      setSelectedImportBatchId((current) =>
        nextBatches.some((batch) => batch.id === current) ? current : nextBatches[0]?.id ?? '',
      );
      setNotice('Actual import batches loaded.');
    });
  }

  async function refreshActualImportRows(batchId: string) {
    await runAction(async () => {
      setActualImportRows(await listActualImportRows(batchId));
      setNotice('Actual import rows loaded.');
    });
  }

  async function handleCreateWorkspace(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await runAction(async () => {
      const workspace = await createWorkspace(workspaceDraft);
      if (workspace) {
        setWorkspaceDraft({ code: '', name: '' });
        const nextWorkspaces = await listWorkspaces();
        setWorkspaces(nextWorkspaces);
        setSelectedWorkspaceId(workspace.id);
        setNotice(`Workspace ${workspace.code} created.`);
      }
    });
  }

  async function handleCreateDimension(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedWorkspaceId) {
      setError('Create or select a workspace first.');
      return;
    }

    await runAction(async () => {
      const dimension = await createDimension({
        ...dimensionDraft,
        workspaceId: selectedWorkspaceId,
      });
      if (dimension) {
        setDimensionDraft({
          code: '',
          name: '',
          dimensionType: 'ACCOUNT',
          system: true,
        });
        await refreshDimensions(selectedWorkspaceId);
        setSelectedDimensionId(dimension.id);
        setNotice(`Dimension ${dimension.code} created.`);
      }
    });
  }

  async function handleCreateMember(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedDimensionId) {
      setError('Select a dimension first.');
      return;
    }

    await runAction(async () => {
      const sortOrder = memberDraft.sortOrder.trim()
        ? Number(memberDraft.sortOrder)
        : undefined;
      const member = await createMember(selectedDimensionId, {
        code: memberDraft.code,
        name: memberDraft.name,
        parentId: memberDraft.parentId || undefined,
        sortOrder,
      });
      if (member) {
        setMemberDraft({ code: '', name: '', parentId: '', sortOrder: '' });
        await refreshMembers(selectedDimensionId);
        setNotice(`Member ${member.code} created.`);
      }
    });
  }

  async function handleCreateSecurityUser(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await runAction(async () => {
      const user = await createSecurityUser({
        username: securityUserDraft.username,
        displayName: securityUserDraft.displayName,
        email: securityUserDraft.email || undefined,
      });
      if (user) {
        setSecurityUserDraft({ username: '', displayName: '', email: '' });
        const nextUsers = await listSecurityUsers();
        setSecurityUsers(nextUsers);
        setSelectedSecurityUserId(user.id);
        setNotice(`Security user ${user.username} created.`);
      }
    });
  }

  async function handleGrantUserRole(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedSecurityUserId || !selectedWorkspaceId) {
      setError('Select a user and workspace first.');
      return;
    }

    await runAction(async () => {
      const role = await grantUserRole(selectedSecurityUserId, {
        workspaceId: selectedWorkspaceId,
        roleCode: roleGrantDraft.roleCode,
      });
      if (role) {
        await refreshSecurityGrants(selectedSecurityUserId, selectedWorkspaceId);
        setNotice(`${role.roleCode} granted to ${selectedSecurityUser?.username ?? 'user'}.`);
      }
    });
  }

  async function handleGrantEntityScope(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedSecurityUserId || !selectedWorkspaceId) {
      setError('Select a user and workspace first.');
      return;
    }
    if (!entityScopeDraft.entityMemberId) {
      setError('Select an Entity member first.');
      return;
    }

    await runAction(async () => {
      const scope = await grantEntityScope(selectedSecurityUserId, {
        workspaceId: selectedWorkspaceId,
        entityMemberId: entityScopeDraft.entityMemberId,
        includeDescendants: entityScopeDraft.includeDescendants,
      });
      if (scope) {
        setEntityScopeDraft({ entityMemberId: '', includeDescendants: true });
        await refreshSecurityGrants(selectedSecurityUserId, selectedWorkspaceId);
        setNotice(`Entity scope ${scope.entityMemberCode} granted.`);
      }
    });
  }

  async function refreshAuditEvents(page = auditPage.page) {
    await runAction(async () => {
      const nextPage = await searchAuditEvents({
        actorId: auditDraft.actorId || undefined,
        subjectType: auditDraft.subjectType || undefined,
        subjectId: auditDraft.subjectId || undefined,
        action: auditDraft.action || undefined,
        page,
        size: auditPage.size,
      });
      setAuditPage(nextPage);
      setAuditEvents(nextPage.items);
      setNotice(`${nextPage.totalElements} audit events matched.`);
    });
  }

  async function handleSearchAudit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    await refreshAuditEvents(0);
  }

  async function handleCreateBudgetModel(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedWorkspaceId) {
      setError('Create or select a workspace first.');
      return;
    }

    await runAction(async () => {
      const budgetModel = await createBudgetModel({
        workspaceId: selectedWorkspaceId,
        code: budgetModelDraft.code,
        name: budgetModelDraft.name,
        description: budgetModelDraft.description || undefined,
      });
      if (budgetModel) {
        setBudgetModelDraft({ code: '', name: '', description: '' });
        await refreshBudgetModels(selectedWorkspaceId);
        setSelectedBudgetModelId(budgetModel.id);
        setNotice(`Budget model ${budgetModel.code} created.`);
      }
    });
  }

  async function handleBindDimension(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedBudgetModelId) {
      setError('Select a budget model first.');
      return;
    }
    if (!bindingDraft.dimensionId) {
      setError('Select a dimension to bind.');
      return;
    }

    await runAction(async () => {
      const displayOrder = bindingDraft.displayOrder.trim()
        ? Number(bindingDraft.displayOrder)
        : undefined;
      const binding = await bindBudgetModelDimension(selectedBudgetModelId, {
        dimensionId: bindingDraft.dimensionId,
        requiredForEntry: bindingDraft.requiredForEntry,
        displayOrder,
      });
      if (binding) {
        setBindingDraft({ dimensionId: '', requiredForEntry: true, displayOrder: '' });
        await refreshModelBindings(selectedBudgetModelId);
        setNotice(`Dimension ${binding.dimensionCode} bound to model.`);
      }
    });
  }

  async function handleActivateBudgetModel() {
    if (!selectedBudgetModelId || !selectedWorkspaceId) {
      return;
    }
    await runAction(async () => {
      const budgetModel = await activateBudgetModel(selectedBudgetModelId);
      if (budgetModel) {
        await refreshBudgetModels(selectedWorkspaceId);
        setSelectedBudgetModelId(budgetModel.id);
        setNotice(`Budget model ${budgetModel.code} activated.`);
      }
    });
  }

  async function handleDeactivateBudgetModel() {
    if (!selectedBudgetModelId || !selectedWorkspaceId) {
      return;
    }
    await runAction(async () => {
      const budgetModel = await deactivateBudgetModel(selectedBudgetModelId);
      if (budgetModel) {
        await refreshBudgetModels(selectedWorkspaceId);
        setSelectedBudgetModelId(budgetModel.id);
        setNotice(`Budget model ${budgetModel.code} deactivated.`);
      }
    });
  }

  async function handleCreateBudgetTemplate(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedBudgetModelId) {
      setError('Select an active budget model first.');
      return;
    }

    await runAction(async () => {
      const template = await createBudgetTemplate({
        budgetModelId: selectedBudgetModelId,
        code: templateDraft.code,
        name: templateDraft.name,
        description: templateDraft.description || undefined,
      });
      if (template) {
        setTemplateDraft({ code: '', name: '', description: '' });
        await refreshBudgetTemplates(selectedBudgetModelId);
        setSelectedBudgetTemplateId(template.id);
        setNotice(`Template ${template.code} created.`);
      }
    });
  }

  async function handleCreateTemplateAxis(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedBudgetTemplateId) {
      setError('Select a budget template first.');
      return;
    }
    if (!axisDraft.modelDimensionId) {
      setError('Select a model dimension for the axis.');
      return;
    }

    await runAction(async () => {
      const displayOrder = axisDraft.displayOrder.trim()
        ? Number(axisDraft.displayOrder)
        : undefined;
      const axis = await createTemplateAxis(selectedBudgetTemplateId, {
        modelDimensionId: axisDraft.modelDimensionId,
        axisType: axisDraft.axisType,
        memberSelector: axisDraft.memberSelector || undefined,
        displayOrder,
      });
      if (axis) {
        setAxisDraft({
          modelDimensionId: '',
          axisType: 'ROW',
          memberSelector: 'ALL_LEAF',
          displayOrder: '',
        });
        await refreshTemplateAxes(selectedBudgetTemplateId);
        setNotice(`${axis.axisType} axis added for ${axis.dimensionCode}.`);
      }
    });
  }

  async function handleActivateBudgetTemplate() {
    if (!selectedBudgetTemplateId || !selectedBudgetModelId) {
      return;
    }
    await runAction(async () => {
      const template = await activateBudgetTemplate(selectedBudgetTemplateId);
      if (template) {
        await refreshBudgetTemplates(selectedBudgetModelId);
        setSelectedBudgetTemplateId(template.id);
        setNotice(`Template ${template.code} activated.`);
      }
    });
  }

  async function handleDeactivateBudgetTemplate() {
    if (!selectedBudgetTemplateId || !selectedBudgetModelId) {
      return;
    }
    await runAction(async () => {
      const template = await deactivateBudgetTemplate(selectedBudgetTemplateId);
      if (template) {
        await refreshBudgetTemplates(selectedBudgetModelId);
        setSelectedBudgetTemplateId(template.id);
        setNotice(`Template ${template.code} deactivated.`);
      }
    });
  }

  async function handleCreateSubmissionTask(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedBudgetTemplateId) {
      setError('Select an active budget template first.');
      return;
    }

    await runAction(async () => {
      const task = await createSubmissionTask({
        budgetTemplateId: selectedBudgetTemplateId,
        entityMemberId: submissionDraft.entityMemberId,
        timeMemberId: submissionDraft.timeMemberId,
        categoryMemberId: submissionDraft.categoryMemberId,
        versionMemberId: submissionDraft.versionMemberId,
        ownerUser: submissionDraft.ownerUser,
        reviewerUser: submissionDraft.reviewerUser,
      });
      if (task) {
        await refreshSubmissionTasks(selectedBudgetTemplateId);
        setSelectedSubmissionTaskId(task.id);
        setNotice('Submission task created.');
      }
    });
  }

  async function handleSaveFactValue(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedSubmissionTaskId) {
      setError('Select a submission task first.');
      return;
    }

    await runAction(async () => {
      const value = await saveFactValue(selectedSubmissionTaskId, {
        accountMemberId: factValueDraft.accountMemberId,
        amount: Number(factValueDraft.amount),
        note: factValueDraft.note || undefined,
      });
      if (value) {
        setFactValueDraft({ accountMemberId: '', amount: '', note: '' });
        await refreshSubmissionTasks(selectedBudgetTemplateId);
        await refreshFactValues(selectedSubmissionTaskId);
        setNotice(`Draft value saved for ${value.accountMemberCode}.`);
      }
    });
  }

  async function transitionSubmissionTask(
    action: 'submit' | 'return' | 'approve' | 'lock',
  ) {
    if (!selectedSubmissionTaskId || !selectedBudgetTemplateId) {
      return;
    }

    await runAction(async () => {
      if (action === 'submit') {
        await submitSubmissionTask(selectedSubmissionTaskId);
      }
      if (action === 'return') {
        await returnSubmissionTask(selectedSubmissionTaskId, returnReason);
        setReturnReason('');
      }
      if (action === 'approve') {
        await approveSubmissionTask(selectedSubmissionTaskId);
      }
      if (action === 'lock') {
        await lockSubmissionTask(selectedSubmissionTaskId);
      }
      await refreshSubmissionTasks(selectedBudgetTemplateId);
      await refreshFactValues(selectedSubmissionTaskId);
      setNotice(`Submission task ${action} completed.`);
    });
  }

  function buildFactQueryFilters() {
    return {
      budgetModelId: selectedBudgetModelId,
      entityMemberId: queryDraft.entityMemberId || undefined,
      timeMemberId: queryDraft.timeMemberId || undefined,
      categoryMemberId: queryDraft.categoryMemberId || undefined,
      versionMemberId: queryDraft.versionMemberId || undefined,
      status: queryDraft.status || undefined,
    };
  }

  async function handleRunQuery() {
    if (!selectedBudgetModelId) {
      setError('Select a budget model first.');
      return;
    }

    await runAction(async () => {
      const rows = await queryFacts(buildFactQueryFilters());
      setQueryRows(rows);
      setNotice(`${rows.length} fact rows loaded.`);
    });
  }

  async function handleRunSummary() {
    if (!selectedBudgetModelId) {
      setError('Select a budget model first.');
      return;
    }

    await runAction(async () => {
      const rows = await summarizeFacts({
        ...buildFactQueryFilters(),
        groupBy: queryDraft.groupBy,
      });
      setSummaryRows(rows);
      setNotice(`${rows.length} summary rows loaded.`);
    });
  }

  async function handleExportCsv() {
    if (!selectedBudgetModelId) {
      setError('Select a budget model first.');
      return;
    }

    await runAction(async () => {
      const content = await exportFactsCsv(buildFactQueryFilters());
      setCsvExport(content);
      setNotice('CSV export generated.');
    });
  }

  async function handleRunVariance() {
    if (!selectedBudgetModelId) {
      setError('Select a budget model first.');
      return;
    }
    if (!varianceDraft.budgetCategoryMemberId || !varianceDraft.actualCategoryMemberId) {
      setError('Select both budget and actual categories.');
      return;
    }

    await runAction(async () => {
      const rows = await analyzeBudgetActualVariance({
        budgetModelId: selectedBudgetModelId,
        budgetCategoryMemberId: varianceDraft.budgetCategoryMemberId,
        actualCategoryMemberId: varianceDraft.actualCategoryMemberId,
        budgetVersionMemberId: varianceDraft.budgetVersionMemberId || undefined,
        actualVersionMemberId: varianceDraft.actualVersionMemberId || undefined,
        entityMemberId: varianceDraft.entityMemberId || undefined,
        timeMemberId: varianceDraft.timeMemberId || undefined,
        status: varianceDraft.status || undefined,
      });
      setVarianceRows(rows);
      setNotice(`${rows.length} variance rows loaded.`);
    });
  }

  async function handleValidateActualImport(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (!selectedBudgetModelId) {
      setError('Select an active budget model first.');
      return;
    }

    await runAction(async () => {
      const batch = await validateActualImport({
        budgetModelId: selectedBudgetModelId,
        fileName: actualImportDraft.fileName,
        operatorUser: actualImportDraft.operatorUser,
        csvContent: actualImportDraft.csvContent,
      });
      if (batch) {
        await refreshActualImportBatches(selectedBudgetModelId);
        setSelectedImportBatchId(batch.id);
        setActualImportRows(batch.rows);
        setNotice(`Actual import ${batch.status}: ${batch.validRows} valid, ${batch.errorRows} errors.`);
      }
    });
  }

  async function handleCommitActualImport() {
    if (!selectedImportBatchId || !selectedBudgetModelId) {
      setError('Select a validated import batch first.');
      return;
    }

    await runAction(async () => {
      const batch = await commitActualImport(selectedImportBatchId);
      if (batch) {
        await refreshActualImportBatches(selectedBudgetModelId);
        setSelectedImportBatchId(batch.id);
        await handleRunQuery();
        await handleRunSummary();
        setNotice(`Actual import ${batch.fileName} committed.`);
      }
    });
  }

  const memberNameById = new Map(members.map((member) => [member.id, member.name]));

  return (
    <main className="app-shell">
      <header className="top-bar">
        <div>
          <p className="eyebrow">Metadata Management</p>
          <h1>Budget Platform</h1>
        </div>
        <button type="button" onClick={() => void refreshWorkspaces()} disabled={loading}>
          Refresh
        </button>
      </header>

      {devSecurityContextEnabled ? (
        <section className="security-strip" aria-label="Development identity context">
          <label>
            User
            <input
              value={securityContext.userId}
              onChange={(event) =>
                setSecurityContext({ ...securityContext, userId: event.target.value })
              }
            />
          </label>
          <label>
            Role
            <select
              value={securityContext.roles}
              onChange={(event) =>
                setSecurityContext({ ...securityContext, roles: event.target.value })
              }
            >
              {securityRoleOptions.map((role) => (
                <option key={role}>{role}</option>
              ))}
            </select>
          </label>
        </section>
      ) : null}

      <section className="current-user-strip" aria-label="Current authenticated user">
        <div>
          <p className="eyebrow">Current User</p>
          <strong>{currentUser?.userId ?? 'Unauthenticated'}</strong>
        </div>
        <div>
          <p className="eyebrow">Auth Mode</p>
          <span>{currentUser?.authMode ?? 'N/A'}</span>
        </div>
        <div>
          <p className="eyebrow">App Roles</p>
          <span>{currentUser ? uniqueRoleCount(currentUser.applicationRoles) : 0}</span>
        </div>
        <div>
          <p className="eyebrow">Entity Scopes</p>
          <span>{currentUser?.entityScopes.length ?? 0}</span>
        </div>
        <button type="button" onClick={() => void refreshCurrentUser()} disabled={loading}>
          Refresh User
        </button>
      </section>

      <section className="status-row" aria-live="polite">
        <span>{notice}</span>
        {error ? <strong>{error}</strong> : null}
      </section>

      <section className="security-management-layout" aria-label="Security administration">
        <section className="panel" aria-labelledby="security-users-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Security</p>
              <h2 id="security-users-title">Users</h2>
            </div>
            <span>{securityUsers.length}</span>
          </div>

          <form
            className="security-user-form"
            onSubmit={(event) => void handleCreateSecurityUser(event)}
          >
            <label>
              Username
              <input
                required
                value={securityUserDraft.username}
                onChange={(event) =>
                  setSecurityUserDraft({ ...securityUserDraft, username: event.target.value })
                }
              />
            </label>
            <label>
              Display name
              <input
                required
                value={securityUserDraft.displayName}
                onChange={(event) =>
                  setSecurityUserDraft({ ...securityUserDraft, displayName: event.target.value })
                }
              />
            </label>
            <label>
              Email
              <input
                type="email"
                value={securityUserDraft.email}
                onChange={(event) =>
                  setSecurityUserDraft({ ...securityUserDraft, email: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {securityUsers.map((user) => (
              <button
                className="list-row"
                data-selected={user.id === selectedSecurityUserId}
                key={user.id}
                onClick={() => setSelectedSecurityUserId(user.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{user.username}</strong>
                  {user.displayName}
                </span>
                <em>{user.status}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel" aria-labelledby="security-roles-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Workspace</p>
              <h2 id="security-roles-title">Roles</h2>
            </div>
            <span>{selectedWorkspace?.code ?? 'No workspace'}</span>
          </div>

          <form className="security-grant-form" onSubmit={(event) => void handleGrantUserRole(event)}>
            <label>
              Role
              <select
                value={roleGrantDraft.roleCode}
                onChange={(event) =>
                  setRoleGrantDraft({ roleCode: event.target.value as SecurityRoleCode })
                }
              >
                {securityRoleOptions.map((role) => (
                  <option key={role}>{role}</option>
                ))}
              </select>
            </label>
            <button type="submit" disabled={loading || !selectedWorkspaceId || !selectedSecurityUserId}>
              Grant
            </button>
          </form>

          <div className="table-wrap compact-table">
            <table>
              <thead>
                <tr>
                  <th>Workspace</th>
                  <th>Role</th>
                </tr>
              </thead>
              <tbody>
                {userRoles.map((role) => (
                  <tr key={role.id}>
                    <td>{role.workspaceCode}</td>
                    <td>{role.roleCode}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>

        <section className="panel" aria-labelledby="security-scopes-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Entity</p>
              <h2 id="security-scopes-title">Scopes</h2>
            </div>
            <span>{selectedSecurityUser?.username ?? 'No user'}</span>
          </div>

          <form
            className="security-grant-form"
            onSubmit={(event) => void handleGrantEntityScope(event)}
          >
            <label>
              Entity
              <select
                value={entityScopeDraft.entityMemberId}
                onChange={(event) =>
                  setEntityScopeDraft({
                    ...entityScopeDraft,
                    entityMemberId: event.target.value,
                  })
                }
              >
                <option value="">Select entity</option>
                {scopeMembers.ENTITY.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label className="check-row">
              <input
                checked={entityScopeDraft.includeDescendants}
                type="checkbox"
                onChange={(event) =>
                  setEntityScopeDraft({
                    ...entityScopeDraft,
                    includeDescendants: event.target.checked,
                  })
                }
              />
              Include descendants
            </label>
            <button type="submit" disabled={loading || !selectedWorkspaceId || !selectedSecurityUserId}>
              Grant
            </button>
          </form>

          <div className="table-wrap compact-table">
            <table>
              <thead>
                <tr>
                  <th>Entity</th>
                  <th>Desc.</th>
                </tr>
              </thead>
              <tbody>
                {entityScopes.map((scope) => (
                  <tr key={scope.id}>
                    <td>
                      {scope.entityMemberCode} - {scope.entityMemberName}
                    </td>
                    <td>{scope.includeDescendants ? 'Yes' : 'No'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="audit-layout" aria-label="Audit event query">
        <section className="panel" aria-labelledby="audit-search-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Audit</p>
              <h2 id="audit-search-title">Event Search</h2>
            </div>
            <span>{auditPage.totalElements} events</span>
          </div>

          <form className="audit-form" onSubmit={(event) => void handleSearchAudit(event)}>
            <label>
              Actor
              <input
                value={auditDraft.actorId}
                onChange={(event) => setAuditDraft({ ...auditDraft, actorId: event.target.value })}
              />
            </label>
            <label>
              Subject type
              <input
                value={auditDraft.subjectType}
                onChange={(event) =>
                  setAuditDraft({ ...auditDraft, subjectType: event.target.value })
                }
              />
            </label>
            <label>
              Subject id
              <input
                value={auditDraft.subjectId}
                onChange={(event) =>
                  setAuditDraft({ ...auditDraft, subjectId: event.target.value })
                }
              />
            </label>
            <label>
              Action
              <select
                value={auditDraft.action}
                onChange={(event) =>
                  setAuditDraft({ ...auditDraft, action: event.target.value as '' | AuditAction })
                }
              >
                <option value="">All actions</option>
                {auditActionOptions.map((action) => (
                  <option key={action}>{action}</option>
                ))}
              </select>
            </label>
            <button type="submit" disabled={loading}>
              Search
            </button>
          </form>

          <div className="model-actions">
            <button
              type="button"
              onClick={() => void refreshAuditEvents(Math.max(auditPage.page - 1, 0))}
              disabled={loading || auditPage.page <= 0}
            >
              Previous
            </button>
            <button
              type="button"
              onClick={() => void refreshAuditEvents(auditPage.page + 1)}
              disabled={loading || auditPage.page + 1 >= auditPage.totalPages}
            >
              Next
            </button>
          </div>
        </section>

        <section className="panel" aria-labelledby="audit-results-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Read only</p>
              <h2 id="audit-results-title">Audit Events</h2>
            </div>
            <span>
              Page {auditPage.totalPages === 0 ? 0 : auditPage.page + 1} / {auditPage.totalPages}
            </span>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Time</th>
                  <th>Actor</th>
                  <th>Subject</th>
                  <th>Action</th>
                  <th>Details</th>
                </tr>
              </thead>
              <tbody>
                {auditEvents.map((event) => (
                  <tr key={event.id}>
                    <td>{new Date(event.occurredAt).toLocaleString()}</td>
                    <td>{event.actorId}</td>
                    <td>
                      {event.subjectType}
                      <br />
                      {event.subjectId}
                    </td>
                    <td>{event.action}</td>
                    <td>{event.detailsJson}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="metadata-layout" aria-label="Metadata workspace">
        <section className="panel" aria-labelledby="workspace-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 1</p>
              <h2 id="workspace-title">Workspaces</h2>
            </div>
            <span>{workspaces.length}</span>
          </div>

          <form className="form-grid" onSubmit={(event) => void handleCreateWorkspace(event)}>
            <label>
              Code
              <input
                required
                value={workspaceDraft.code}
                onChange={(event) =>
                  setWorkspaceDraft({ ...workspaceDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={workspaceDraft.name}
                onChange={(event) =>
                  setWorkspaceDraft({ ...workspaceDraft, name: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {workspaces.map((workspace) => (
              <button
                className="list-row"
                data-selected={workspace.id === selectedWorkspaceId}
                key={workspace.id}
                onClick={() => setSelectedWorkspaceId(workspace.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{workspace.code}</strong>
                  {workspace.name}
                </span>
                <em>{workspace.status}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel" aria-labelledby="dimension-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 2</p>
              <h2 id="dimension-title">Dimensions</h2>
            </div>
            <span>{selectedWorkspace?.code ?? 'No workspace'}</span>
          </div>

          <form className="form-grid" onSubmit={(event) => void handleCreateDimension(event)}>
            <label>
              Code
              <input
                required
                value={dimensionDraft.code}
                onChange={(event) =>
                  setDimensionDraft({ ...dimensionDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={dimensionDraft.name}
                onChange={(event) =>
                  setDimensionDraft({ ...dimensionDraft, name: event.target.value })
                }
              />
            </label>
            <label>
              Type
              <select
                value={dimensionDraft.dimensionType}
                onChange={(event) =>
                  setDimensionDraft({
                    ...dimensionDraft,
                    dimensionType: event.target.value as DimensionType,
                  })
                }
              >
                {dimensionTypes.map((type) => (
                  <option key={type}>{type}</option>
                ))}
              </select>
            </label>
            <label className="check-row">
              <input
                checked={dimensionDraft.system}
                type="checkbox"
                onChange={(event) =>
                  setDimensionDraft({ ...dimensionDraft, system: event.target.checked })
                }
              />
              System dimension
            </label>
            <button type="submit" disabled={loading || !selectedWorkspaceId}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {dimensions.map((dimension) => (
              <button
                className="list-row"
                data-selected={dimension.id === selectedDimensionId}
                key={dimension.id}
                onClick={() => setSelectedDimensionId(dimension.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{dimension.code}</strong>
                  {dimension.name}
                </span>
                <em>{dimension.dimensionType}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel wide-panel" aria-labelledby="member-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 3</p>
              <h2 id="member-title">Members</h2>
            </div>
            <span>{selectedDimension?.code ?? 'No dimension'}</span>
          </div>

          <form className="member-form" onSubmit={(event) => void handleCreateMember(event)}>
            <label>
              Code
              <input
                required
                value={memberDraft.code}
                onChange={(event) =>
                  setMemberDraft({ ...memberDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={memberDraft.name}
                onChange={(event) =>
                  setMemberDraft({ ...memberDraft, name: event.target.value })
                }
              />
            </label>
            <label>
              Parent
              <select
                value={memberDraft.parentId}
                onChange={(event) =>
                  setMemberDraft({ ...memberDraft, parentId: event.target.value })
                }
              >
                <option value="">Root member</option>
                {members.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Sort
              <input
                min="0"
                type="number"
                value={memberDraft.sortOrder}
                onChange={(event) =>
                  setMemberDraft({ ...memberDraft, sortOrder: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading || !selectedDimensionId}>
              Create
            </button>
          </form>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Code</th>
                  <th>Name</th>
                  <th>Parent</th>
                  <th>Status</th>
                  <th>Leaf</th>
                </tr>
              </thead>
              <tbody>
                {members.map((member) => (
                  <tr key={member.id}>
                    <td>{member.code}</td>
                    <td>{member.name}</td>
                    <td>{member.parentId ? memberNameById.get(member.parentId) : 'Root'}</td>
                    <td>{member.status}</td>
                    <td>{member.leaf ? 'Yes' : 'No'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="model-layout" aria-label="Budget model management">
        <section className="panel" aria-labelledby="model-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 4</p>
              <h2 id="model-title">Budget Models</h2>
            </div>
            <span>{selectedWorkspace?.code ?? 'No workspace'}</span>
          </div>

          <form className="model-form" onSubmit={(event) => void handleCreateBudgetModel(event)}>
            <label>
              Code
              <input
                required
                value={budgetModelDraft.code}
                onChange={(event) =>
                  setBudgetModelDraft({ ...budgetModelDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={budgetModelDraft.name}
                onChange={(event) =>
                  setBudgetModelDraft({ ...budgetModelDraft, name: event.target.value })
                }
              />
            </label>
            <label>
              Description
              <input
                value={budgetModelDraft.description}
                onChange={(event) =>
                  setBudgetModelDraft({
                    ...budgetModelDraft,
                    description: event.target.value,
                  })
                }
              />
            </label>
            <button type="submit" disabled={loading || !selectedWorkspaceId}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {budgetModels.map((budgetModel) => (
              <button
                className="list-row"
                data-selected={budgetModel.id === selectedBudgetModelId}
                key={budgetModel.id}
                onClick={() => setSelectedBudgetModelId(budgetModel.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{budgetModel.code}</strong>
                  {budgetModel.name}
                </span>
                <em>{budgetModel.status}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel" aria-labelledby="binding-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 5</p>
              <h2 id="binding-title">Model Dimensions</h2>
            </div>
            <span>{selectedBudgetModel?.code ?? 'No model'}</span>
          </div>

          <form className="binding-form" onSubmit={(event) => void handleBindDimension(event)}>
            <label>
              Dimension
              <select
                value={bindingDraft.dimensionId}
                onChange={(event) =>
                  setBindingDraft({ ...bindingDraft, dimensionId: event.target.value })
                }
              >
                <option value="">Select dimension</option>
                {unboundDimensions.map((dimension) => (
                  <option key={dimension.id} value={dimension.id}>
                    {dimension.code} - {dimension.dimensionType}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Order
              <input
                min="0"
                type="number"
                value={bindingDraft.displayOrder}
                onChange={(event) =>
                  setBindingDraft({ ...bindingDraft, displayOrder: event.target.value })
                }
              />
            </label>
            <label className="check-row">
              <input
                checked={bindingDraft.requiredForEntry}
                type="checkbox"
                onChange={(event) =>
                  setBindingDraft({
                    ...bindingDraft,
                    requiredForEntry: event.target.checked,
                  })
                }
              />
              Required for entry
            </label>
            <button type="submit" disabled={loading || !selectedBudgetModelId}>
              Bind
            </button>
          </form>

          <div className="model-actions">
            <button
              type="button"
              onClick={() => void handleActivateBudgetModel()}
              disabled={loading || !selectedBudgetModelId}
            >
              Activate
            </button>
            <button
              type="button"
              onClick={() => void handleDeactivateBudgetModel()}
              disabled={loading || !selectedBudgetModelId}
            >
              Deactivate
            </button>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Dimension</th>
                  <th>Role</th>
                  <th>Required</th>
                  <th>Order</th>
                </tr>
              </thead>
              <tbody>
                {modelBindings.map((binding) => (
                  <tr key={binding.id}>
                    <td>
                      {binding.dimensionCode} - {binding.dimensionName}
                    </td>
                    <td>{binding.dimensionRole}</td>
                    <td>{binding.requiredForEntry ? 'Yes' : 'No'}</td>
                    <td>{binding.displayOrder}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="template-layout" aria-label="Budget template management">
        <section className="panel" aria-labelledby="template-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 6</p>
              <h2 id="template-title">Templates</h2>
            </div>
            <span>{selectedBudgetModel?.code ?? 'No model'}</span>
          </div>

          <form className="model-form" onSubmit={(event) => void handleCreateBudgetTemplate(event)}>
            <label>
              Code
              <input
                required
                value={templateDraft.code}
                onChange={(event) =>
                  setTemplateDraft({ ...templateDraft, code: event.target.value })
                }
              />
            </label>
            <label>
              Name
              <input
                required
                value={templateDraft.name}
                onChange={(event) =>
                  setTemplateDraft({ ...templateDraft, name: event.target.value })
                }
              />
            </label>
            <label>
              Description
              <input
                value={templateDraft.description}
                onChange={(event) =>
                  setTemplateDraft({ ...templateDraft, description: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading || !selectedBudgetModelId}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {budgetTemplates.map((template) => (
              <button
                className="list-row"
                data-selected={template.id === selectedBudgetTemplateId}
                key={template.id}
                onClick={() => setSelectedBudgetTemplateId(template.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{template.code}</strong>
                  {template.name}
                </span>
                <em>{template.status}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel" aria-labelledby="axis-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 7</p>
              <h2 id="axis-title">Template Axes</h2>
            </div>
            <span>{selectedBudgetTemplate?.code ?? 'No template'}</span>
          </div>

          <form className="axis-form" onSubmit={(event) => void handleCreateTemplateAxis(event)}>
            <label>
              Model dimension
              <select
                value={axisDraft.modelDimensionId}
                onChange={(event) =>
                  setAxisDraft({ ...axisDraft, modelDimensionId: event.target.value })
                }
              >
                <option value="">Select binding</option>
                {unusedTemplateBindings.map((binding) => (
                  <option key={binding.id} value={binding.id}>
                    {binding.dimensionCode} - {binding.dimensionRole}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Axis
              <select
                value={axisDraft.axisType}
                onChange={(event) =>
                  setAxisDraft({
                    ...axisDraft,
                    axisType: event.target.value as TemplateAxisType,
                  })
                }
              >
                {axisTypes.map((type) => (
                  <option key={type}>{type}</option>
                ))}
              </select>
            </label>
            <label>
              Members
              <select
                value={axisDraft.memberSelector}
                onChange={(event) =>
                  setAxisDraft({ ...axisDraft, memberSelector: event.target.value })
                }
              >
                <option value="ALL_LEAF">All leaf</option>
                <option value="ALL_MEMBERS">All members</option>
                <option value="ROOT_ONLY">Root only</option>
              </select>
            </label>
            <label>
              Order
              <input
                min="0"
                type="number"
                value={axisDraft.displayOrder}
                onChange={(event) =>
                  setAxisDraft({ ...axisDraft, displayOrder: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading || !selectedBudgetTemplateId}>
              Add
            </button>
          </form>

          <div className="model-actions">
            <button
              type="button"
              onClick={() => void handleActivateBudgetTemplate()}
              disabled={loading || !selectedBudgetTemplateId}
            >
              Activate
            </button>
            <button
              type="button"
              onClick={() => void handleDeactivateBudgetTemplate()}
              disabled={loading || !selectedBudgetTemplateId}
            >
              Deactivate
            </button>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Dimension</th>
                  <th>Axis</th>
                  <th>Members</th>
                  <th>Order</th>
                </tr>
              </thead>
              <tbody>
                {templateAxes.map((axis) => (
                  <tr key={axis.id}>
                    <td>
                      {axis.dimensionCode} - {axis.dimensionName}
                    </td>
                    <td>{axis.axisType}</td>
                    <td>{axis.memberSelector}</td>
                    <td>{axis.displayOrder}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="submission-layout" aria-label="Budget submission management">
        <section className="panel" aria-labelledby="submission-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 8</p>
              <h2 id="submission-title">Submission Tasks</h2>
            </div>
            <span>{selectedBudgetTemplate?.code ?? 'No template'}</span>
          </div>

          <form
            className="submission-form"
            onSubmit={(event) => void handleCreateSubmissionTask(event)}
          >
            <label>
              Entity
              <select
                required
                value={submissionDraft.entityMemberId}
                onChange={(event) =>
                  setSubmissionDraft({
                    ...submissionDraft,
                    entityMemberId: event.target.value,
                  })
                }
              >
                <option value="">Select entity</option>
                {scopeMembers.ENTITY.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Time
              <select
                required
                value={submissionDraft.timeMemberId}
                onChange={(event) =>
                  setSubmissionDraft({ ...submissionDraft, timeMemberId: event.target.value })
                }
              >
                <option value="">Select time</option>
                {scopeMembers.TIME.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Category
              <select
                required
                value={submissionDraft.categoryMemberId}
                onChange={(event) =>
                  setSubmissionDraft({
                    ...submissionDraft,
                    categoryMemberId: event.target.value,
                  })
                }
              >
                <option value="">Select category</option>
                {scopeMembers.CATEGORY.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Version
              <select
                required
                value={submissionDraft.versionMemberId}
                onChange={(event) =>
                  setSubmissionDraft({
                    ...submissionDraft,
                    versionMemberId: event.target.value,
                  })
                }
              >
                <option value="">Select version</option>
                {scopeMembers.VERSION.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Owner
              <input
                required
                value={submissionDraft.ownerUser}
                onChange={(event) =>
                  setSubmissionDraft({ ...submissionDraft, ownerUser: event.target.value })
                }
              />
            </label>
            <label>
              Reviewer
              <input
                required
                value={submissionDraft.reviewerUser}
                onChange={(event) =>
                  setSubmissionDraft({ ...submissionDraft, reviewerUser: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading || !selectedBudgetTemplateId}>
              Create
            </button>
          </form>

          <div className="list" role="list">
            {submissionTasks.map((task) => (
              <button
                className="list-row"
                data-selected={task.id === selectedSubmissionTaskId}
                key={task.id}
                onClick={() => setSelectedSubmissionTaskId(task.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>
                    {task.entityMemberCode} / {task.timeMemberCode}
                  </strong>
                  {task.categoryMemberCode} - {task.versionMemberCode}
                </span>
                <em>{task.status}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel" aria-labelledby="value-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 9</p>
              <h2 id="value-title">Draft Values</h2>
            </div>
            <span>{selectedSubmissionTask?.status ?? 'No task'}</span>
          </div>

          <form className="value-form" onSubmit={(event) => void handleSaveFactValue(event)}>
            <label>
              Account
              <select
                required
                value={factValueDraft.accountMemberId}
                onChange={(event) =>
                  setFactValueDraft({
                    ...factValueDraft,
                    accountMemberId: event.target.value,
                  })
                }
              >
                <option value="">Select account</option>
                {scopeMembers.ACCOUNT.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Amount
              <input
                required
                step="0.01"
                type="number"
                value={factValueDraft.amount}
                onChange={(event) =>
                  setFactValueDraft({ ...factValueDraft, amount: event.target.value })
                }
              />
            </label>
            <label>
              Note
              <input
                value={factValueDraft.note}
                onChange={(event) =>
                  setFactValueDraft({ ...factValueDraft, note: event.target.value })
                }
              />
            </label>
            <button type="submit" disabled={loading || !selectedSubmissionTaskId}>
              Save Draft
            </button>
          </form>

          <div className="model-actions">
            <button
              type="button"
              onClick={() => void transitionSubmissionTask('submit')}
              disabled={loading || !selectedSubmissionTaskId}
            >
              Submit
            </button>
            <button
              type="button"
              onClick={() => void transitionSubmissionTask('approve')}
              disabled={loading || !selectedSubmissionTaskId}
            >
              Approve
            </button>
            <button
              type="button"
              onClick={() => void transitionSubmissionTask('lock')}
              disabled={loading || !selectedSubmissionTaskId}
            >
              Lock
            </button>
          </div>

          <form className="return-form" onSubmit={(event) => {
            event.preventDefault();
            void transitionSubmissionTask('return');
          }}>
            <label>
              Return reason
              <input
                value={returnReason}
                onChange={(event) => setReturnReason(event.target.value)}
              />
            </label>
            <button type="submit" disabled={loading || !selectedSubmissionTaskId}>
              Return
            </button>
          </form>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Account</th>
                  <th>Amount</th>
                  <th>Status</th>
                  <th>Note</th>
                </tr>
              </thead>
              <tbody>
                {factValues.map((value) => (
                  <tr key={value.id}>
                    <td>
                      {value.accountMemberCode} - {value.accountMemberName}
                    </td>
                    <td>{value.amount}</td>
                    <td>{value.valueStatus}</td>
                    <td>{value.note ?? ''}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="query-layout" aria-label="Budget query and summary">
        <section className="panel" aria-labelledby="query-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 10</p>
              <h2 id="query-title">Fact Query</h2>
            </div>
            <span>{selectedBudgetModel?.code ?? 'No model'}</span>
          </div>

          <form
            className="query-form"
            onSubmit={(event) => {
              event.preventDefault();
              void handleRunQuery();
            }}
          >
            <label>
              Entity
              <select
                value={queryDraft.entityMemberId}
                onChange={(event) =>
                  setQueryDraft({ ...queryDraft, entityMemberId: event.target.value })
                }
              >
                <option value="">All entities</option>
                {scopeMembers.ENTITY.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Time
              <select
                value={queryDraft.timeMemberId}
                onChange={(event) =>
                  setQueryDraft({ ...queryDraft, timeMemberId: event.target.value })
                }
              >
                <option value="">All time</option>
                {scopeMembers.TIME.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Category
              <select
                value={queryDraft.categoryMemberId}
                onChange={(event) =>
                  setQueryDraft({ ...queryDraft, categoryMemberId: event.target.value })
                }
              >
                <option value="">All categories</option>
                {scopeMembers.CATEGORY.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Version
              <select
                value={queryDraft.versionMemberId}
                onChange={(event) =>
                  setQueryDraft({ ...queryDraft, versionMemberId: event.target.value })
                }
              >
                <option value="">All versions</option>
                {scopeMembers.VERSION.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Status
              <select
                value={queryDraft.status}
                onChange={(event) =>
                  setQueryDraft({
                    ...queryDraft,
                    status: event.target.value as '' | FactValueStatus,
                  })
                }
              >
                <option value="">All statuses</option>
                <option value="DRAFT">DRAFT</option>
                <option value="SUBMITTED">SUBMITTED</option>
                <option value="APPROVED">APPROVED</option>
                <option value="LOCKED">LOCKED</option>
              </select>
            </label>
            <label>
              Group by
              <select
                value={queryDraft.groupBy}
                onChange={(event) =>
                  setQueryDraft({
                    ...queryDraft,
                    groupBy: event.target.value as QueryGroupBy,
                  })
                }
              >
                {queryGroupByOptions.map((option) => (
                  <option key={option}>{option}</option>
                ))}
              </select>
            </label>
            <div className="query-actions">
              <button type="submit" disabled={loading || !selectedBudgetModelId}>
                Query
              </button>
              <button
                type="button"
                onClick={() => void handleRunSummary()}
                disabled={loading || !selectedBudgetModelId}
              >
                Summarize
              </button>
              <button
                type="button"
                onClick={() => void handleExportCsv()}
                disabled={loading || !selectedBudgetModelId}
              >
                CSV
              </button>
            </div>
          </form>
        </section>

        <section className="panel" aria-labelledby="query-results-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Read only</p>
              <h2 id="query-results-title">Results</h2>
            </div>
            <span>{queryRows.length} rows</span>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Account</th>
                  <th>Entity</th>
                  <th>Time</th>
                  <th>Category</th>
                  <th>Version</th>
                  <th>Amount</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {queryRows.map((row) => (
                  <tr key={row.id}>
                    <td>
                      {row.accountCode} - {row.accountName}
                    </td>
                    <td>{row.entityCode}</td>
                    <td>{row.timeCode}</td>
                    <td>{row.categoryCode}</td>
                    <td>{row.versionCode}</td>
                    <td>{row.amount}</td>
                    <td>{row.valueStatus}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="query-layout" aria-label="Budget summary and export">
        <section className="panel" aria-labelledby="summary-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Summary</p>
              <h2 id="summary-title">Basic Aggregation</h2>
            </div>
            <span>{summaryRows.length} groups</span>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Group</th>
                  <th>Member</th>
                  <th>Total</th>
                  <th>Lines</th>
                </tr>
              </thead>
              <tbody>
                {summaryRows.map((row) => (
                  <tr key={`${row.groupBy}-${row.memberId}`}>
                    <td>{row.groupBy}</td>
                    <td>
                      {row.memberCode} - {row.memberName}
                    </td>
                    <td>{row.totalAmount}</td>
                    <td>{row.lineCount}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>

        <section className="panel" aria-labelledby="csv-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Export</p>
              <h2 id="csv-title">CSV Preview</h2>
            </div>
            <span>{csvExport ? `${csvExport.split('\n').length - 1} lines` : 'No export'}</span>
          </div>
          <textarea className="csv-output" readOnly value={csvExport} />
        </section>
      </section>

      <section className="query-layout" aria-label="Budget actual variance analysis">
        <section className="panel" aria-labelledby="variance-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 11</p>
              <h2 id="variance-title">Budget vs Actual</h2>
            </div>
            <span>{selectedBudgetModel?.code ?? 'No model'}</span>
          </div>

          <form
            className="variance-form"
            onSubmit={(event) => {
              event.preventDefault();
              void handleRunVariance();
            }}
          >
            <label>
              Entity
              <select
                value={varianceDraft.entityMemberId}
                onChange={(event) =>
                  setVarianceDraft({ ...varianceDraft, entityMemberId: event.target.value })
                }
              >
                <option value="">All entities</option>
                {scopeMembers.ENTITY.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Time
              <select
                value={varianceDraft.timeMemberId}
                onChange={(event) =>
                  setVarianceDraft({ ...varianceDraft, timeMemberId: event.target.value })
                }
              >
                <option value="">All time</option>
                {scopeMembers.TIME.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Budget category
              <select
                required
                value={varianceDraft.budgetCategoryMemberId}
                onChange={(event) =>
                  setVarianceDraft({
                    ...varianceDraft,
                    budgetCategoryMemberId: event.target.value,
                  })
                }
              >
                <option value="">Select budget</option>
                {scopeMembers.CATEGORY.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Actual category
              <select
                required
                value={varianceDraft.actualCategoryMemberId}
                onChange={(event) =>
                  setVarianceDraft({
                    ...varianceDraft,
                    actualCategoryMemberId: event.target.value,
                  })
                }
              >
                <option value="">Select actual</option>
                {scopeMembers.CATEGORY.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Budget version
              <select
                value={varianceDraft.budgetVersionMemberId}
                onChange={(event) =>
                  setVarianceDraft({
                    ...varianceDraft,
                    budgetVersionMemberId: event.target.value,
                  })
                }
              >
                <option value="">All budget versions</option>
                {scopeMembers.VERSION.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Actual version
              <select
                value={varianceDraft.actualVersionMemberId}
                onChange={(event) =>
                  setVarianceDraft({
                    ...varianceDraft,
                    actualVersionMemberId: event.target.value,
                  })
                }
              >
                <option value="">All actual versions</option>
                {scopeMembers.VERSION.map((member) => (
                  <option key={member.id} value={member.id}>
                    {member.code} - {member.name}
                  </option>
                ))}
              </select>
            </label>
            <label>
              Status
              <select
                value={varianceDraft.status}
                onChange={(event) =>
                  setVarianceDraft({
                    ...varianceDraft,
                    status: event.target.value as '' | FactValueStatus,
                  })
                }
              >
                <option value="">Approved and locked</option>
                <option value="APPROVED">APPROVED</option>
                <option value="LOCKED">LOCKED</option>
              </select>
            </label>
            <button type="submit" disabled={loading || !selectedBudgetModelId}>
              Analyze
            </button>
          </form>
        </section>

        <section className="panel" aria-labelledby="variance-results-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Variance</p>
              <h2 id="variance-results-title">Budget vs Actual Table</h2>
            </div>
            <span>{varianceRows.length} rows</span>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Account</th>
                  <th>Entity</th>
                  <th>Time</th>
                  <th>Budget</th>
                  <th>Actual</th>
                  <th>Variance</th>
                  <th>Variance %</th>
                  <th>Lines</th>
                </tr>
              </thead>
              <tbody>
                {varianceRows.map((row) => (
                  <tr key={`${row.accountMemberId}-${row.entityMemberId}-${row.timeMemberId}`}>
                    <td>
                      {row.accountCode} - {row.accountName}
                    </td>
                    <td>{row.entityCode}</td>
                    <td>{row.timeCode}</td>
                    <td>{row.budgetAmount}</td>
                    <td>{row.actualAmount}</td>
                    <td>{row.varianceAmount}</td>
                    <td>{row.variancePercent === null ? 'N/A' : row.variancePercent}</td>
                    <td>
                      {row.budgetLineCount} / {row.actualLineCount}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>

      <section className="import-layout" aria-label="Actual import management">
        <section className="panel" aria-labelledby="actual-import-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Step 12</p>
              <h2 id="actual-import-title">Actual CSV Import</h2>
            </div>
            <span>{selectedBudgetModel?.code ?? 'No model'}</span>
          </div>

          <form className="import-form" onSubmit={(event) => void handleValidateActualImport(event)}>
            <label>
              File name
              <input
                required
                value={actualImportDraft.fileName}
                onChange={(event) =>
                  setActualImportDraft({ ...actualImportDraft, fileName: event.target.value })
                }
              />
            </label>
            <label>
              Operator
              <input
                required
                value={actualImportDraft.operatorUser}
                onChange={(event) =>
                  setActualImportDraft({ ...actualImportDraft, operatorUser: event.target.value })
                }
              />
            </label>
            <label className="import-csv-field">
              CSV content
              <textarea
                required
                value={actualImportDraft.csvContent}
                onChange={(event) =>
                  setActualImportDraft({ ...actualImportDraft, csvContent: event.target.value })
                }
              />
            </label>
            <div className="import-actions">
              <button type="submit" disabled={loading || !selectedBudgetModelId}>
                Validate
              </button>
              <button
                type="button"
                onClick={() => void handleCommitActualImport()}
                disabled={loading || !selectedImportBatchId}
              >
                Commit
              </button>
            </div>
          </form>

          <div className="list" role="list">
            {actualImportBatches.map((batch) => (
              <button
                className="list-row"
                data-selected={batch.id === selectedImportBatchId}
                key={batch.id}
                onClick={() => setSelectedImportBatchId(batch.id)}
                role="listitem"
                type="button"
              >
                <span>
                  <strong>{batch.fileName}</strong>
                  {batch.validRows} valid / {batch.errorRows} errors / {batch.totalAmount}
                </span>
                <em>{batch.status}</em>
              </button>
            ))}
          </div>
        </section>

        <section className="panel" aria-labelledby="actual-import-rows-title">
          <div className="panel-heading">
            <div>
              <p className="eyebrow">Validation</p>
              <h2 id="actual-import-rows-title">Import Rows</h2>
            </div>
            <span>{actualImportRows.length} rows</span>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Row</th>
                  <th>Account</th>
                  <th>Entity</th>
                  <th>Time</th>
                  <th>Category</th>
                  <th>Version</th>
                  <th>Amount</th>
                  <th>Status</th>
                  <th>Error</th>
                </tr>
              </thead>
              <tbody>
                {actualImportRows.map((row) => (
                  <tr key={row.id}>
                    <td>{row.rowNumber}</td>
                    <td>{row.accountCode}</td>
                    <td>{row.entityCode}</td>
                    <td>{row.timeCode}</td>
                    <td>{row.categoryCode}</td>
                    <td>{row.versionCode}</td>
                    <td>{row.amount ?? ''}</td>
                    <td>{row.rowStatus}</td>
                    <td>{row.errorMessage ?? ''}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </section>
      </section>
    </main>
  );
}

export default App;
