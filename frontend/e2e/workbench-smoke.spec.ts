import { expect, test } from '@playwright/test';

const workspace = {
  id: 'workspace-1',
  code: 'MVP_WS',
  name: 'MVP Workspace',
  status: 'ACTIVE',
};

const dimensions = [
  {
    id: 'dim-account',
    workspaceId: workspace.id,
    code: 'ACCOUNT',
    name: 'Account',
    dimensionType: 'ACCOUNT',
    system: true,
    status: 'ACTIVE',
  },
  {
    id: 'dim-entity',
    workspaceId: workspace.id,
    code: 'ENTITY',
    name: 'Entity',
    dimensionType: 'ENTITY',
    system: true,
    status: 'ACTIVE',
  },
  {
    id: 'dim-time',
    workspaceId: workspace.id,
    code: 'TIME',
    name: 'Time',
    dimensionType: 'TIME',
    system: true,
    status: 'ACTIVE',
  },
  {
    id: 'dim-category',
    workspaceId: workspace.id,
    code: 'CATEGORY',
    name: 'Category',
    dimensionType: 'CATEGORY',
    system: true,
    status: 'ACTIVE',
  },
  {
    id: 'dim-version',
    workspaceId: workspace.id,
    code: 'VERSION',
    name: 'Version',
    dimensionType: 'VERSION',
    system: true,
    status: 'ACTIVE',
  },
];

const membersByDimension = {
  'dim-account': [
    {
      id: 'member-account-travel',
      dimensionId: 'dim-account',
      code: 'TRAVEL',
      name: 'Travel Expense',
      parentId: null,
      sortOrder: null,
      leaf: true,
      status: 'ACTIVE',
    },
  ],
  'dim-entity': [
    {
      id: 'member-entity-ops',
      dimensionId: 'dim-entity',
      code: 'OPS',
      name: 'Operations',
      parentId: null,
      sortOrder: null,
      leaf: true,
      status: 'ACTIVE',
    },
  ],
  'dim-time': [
    {
      id: 'member-time-202605',
      dimensionId: 'dim-time',
      code: '2026-05',
      name: '2026-05',
      parentId: null,
      sortOrder: null,
      leaf: true,
      status: 'ACTIVE',
    },
  ],
  'dim-category': [
    {
      id: 'member-category-budget',
      dimensionId: 'dim-category',
      code: 'BUDGET',
      name: 'Budget',
      parentId: null,
      sortOrder: null,
      leaf: true,
      status: 'ACTIVE',
    },
  ],
  'dim-version': [
    {
      id: 'member-version-working',
      dimensionId: 'dim-version',
      code: 'WORKING',
      name: 'Working',
      parentId: null,
      sortOrder: null,
      leaf: true,
      status: 'ACTIVE',
    },
  ],
};

const budgetModel = {
  id: 'model-1',
  workspaceId: workspace.id,
  code: 'MVP_MODEL',
  name: 'MVP Model',
  description: null,
  status: 'ACTIVE',
};

const budgetTemplate = {
  id: 'template-1',
  budgetModelId: budgetModel.id,
  code: 'OPEX_TEMPLATE',
  name: 'OPEX Template',
  status: 'ACTIVE',
};

const submissionTask = {
  id: 'task-1',
  budgetTemplateId: budgetTemplate.id,
  budgetModelId: budgetModel.id,
  entityMemberId: 'member-entity-ops',
  entityCode: 'OPS',
  timeMemberId: 'member-time-202605',
  timeCode: '2026-05',
  categoryMemberId: 'member-category-budget',
  categoryCode: 'BUDGET',
  versionMemberId: 'member-version-working',
  versionCode: 'WORKING',
  ownerUser: 'owner@example.com',
  reviewerUser: 'reviewer@example.com',
  status: 'APPROVED',
};

function api(data: unknown) {
  return {
    status: 200,
    contentType: 'application/json',
    body: JSON.stringify({ success: true, data }),
  };
}

test.beforeEach(async ({ page }) => {
  await page.route((url) => url.pathname.startsWith('/api/'), async (route) => {
    const url = new URL(route.request().url());
    const path = url.pathname;

    if (path === '/api/security/me') {
      await route.fulfill(
        api({
          userId: 'admin@example.com',
          roles: ['BUDGET_ADMIN'],
          authenticated: true,
          authMode: 'DEV_HEADER',
          applicationRoles: [],
          entityScopes: [],
        }),
      );
      return;
    }

    if (path === '/api/security/users') {
      await route.fulfill(
        api([
          {
            id: 'user-1',
            username: 'admin@example.com',
            displayName: 'Budget Admin',
            email: 'admin@example.com',
            status: 'ACTIVE',
          },
        ]),
      );
      return;
    }

    if (path === '/api/security/users/user-1/roles' || path === '/api/security/users/user-1/entity-scopes') {
      await route.fulfill(api([]));
      return;
    }

    if (path === '/api/metadata/workspaces') {
      await route.fulfill(api([workspace]));
      return;
    }

    if (path === '/api/metadata/dimensions') {
      await route.fulfill(api(dimensions));
      return;
    }

    const memberMatch = path.match(/^\/api\/metadata\/dimensions\/([^/]+)\/members$/);
    if (memberMatch) {
      await route.fulfill(api(membersByDimension[memberMatch[1] as keyof typeof membersByDimension] ?? []));
      return;
    }

    if (path === '/api/budget-models') {
      await route.fulfill(api([budgetModel]));
      return;
    }

    if (path === '/api/budget-models/model-1/dimensions') {
      await route.fulfill(
        api(
          dimensions.map((dimension, index) => ({
            id: `binding-${dimension.id}`,
            budgetModelId: budgetModel.id,
            dimensionId: dimension.id,
            dimensionCode: dimension.code,
            dimensionName: dimension.name,
            dimensionRole: dimension.dimensionType,
            requiredForEntry: true,
            displayOrder: (index + 1) * 10,
          })),
        ),
      );
      return;
    }

    if (path === '/api/budget-templates') {
      await route.fulfill(api([budgetTemplate]));
      return;
    }

    if (path === '/api/budget-templates/template-1/axes') {
      await route.fulfill(api([]));
      return;
    }

    if (path === '/api/submissions/tasks') {
      await route.fulfill(api([submissionTask]));
      return;
    }

    if (path === '/api/submissions/tasks/task-1/values') {
      await route.fulfill(
        api([
          {
            id: 'fact-1',
            submissionTaskId: submissionTask.id,
            accountMemberId: 'member-account-travel',
            accountCode: 'TRAVEL',
            accountName: 'Travel Expense',
            amount: 930.25,
            valueStatus: 'APPROVED',
            note: null,
          },
        ]),
      );
      return;
    }

    if (path === '/api/budget-query/facts/page') {
      await route.fulfill(
        api({
          items: [
            {
              id: 'fact-1',
              budgetModelId: budgetModel.id,
              budgetTemplateId: budgetTemplate.id,
              submissionTaskId: submissionTask.id,
              importBatchId: null,
              accountCode: 'TRAVEL',
              accountName: 'Travel Expense',
              entityCode: 'OPS',
              timeCode: '2026-05',
              categoryCode: 'BUDGET',
              versionCode: 'WORKING',
              amount: 930.25,
              valueStatus: 'APPROVED',
              sourceType: 'BUDGET_SUBMISSION',
            },
          ],
          page: Number(url.searchParams.get('page') ?? 0),
          size: 25,
          totalElements: 1,
          totalPages: 1,
        }),
      );
      return;
    }

    if (path === '/api/actual-imports/batches' || path === '/api/audit/events') {
      await route.fulfill(api(path === '/api/audit/events' ? { items: [], page: 0, size: 25, totalElements: 0, totalPages: 0 } : []));
      return;
    }

    await route.fulfill(api([]));
  });
});

test('renders workbench shell and runs paged fact query', async ({ page }) => {
  await page.goto('/');

  await expect(page.getByRole('heading', { name: 'Budget Platform' })).toBeVisible();
  await expect(page.getByText('MVP_WS').first()).toBeVisible();
  await expect(page.getByText('MVP_MODEL').first()).toBeVisible();
  await expect(page.getByText('OPEX_TEMPLATE').first()).toBeVisible();

  await page.getByRole('button', { name: 'Query' }).click();

  const results = page.getByLabel('Results');
  await expect(results.getByRole('cell', { name: 'TRAVEL - Travel Expense', exact: true })).toBeVisible();
  await expect(results.getByRole('cell', { name: '930.25', exact: true })).toBeVisible();
  await expect(results.getByText('1 rows')).toBeVisible();
});
