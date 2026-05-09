import { expect, test } from '@playwright/test';

interface Workspace {
  id: string;
  code: string;
  name: string;
  status: 'ACTIVE';
}

interface BudgetModel {
  id: string;
  workspaceId: string;
  code: string;
  name: string;
  description: string | null;
  status: 'DRAFT';
}

interface BudgetTemplate {
  id: string;
  budgetModelId: string;
  code: string;
  name: string;
  description: string | null;
  status: 'DRAFT';
}

function api(data: unknown) {
  return {
    status: 200,
    contentType: 'application/json',
    body: JSON.stringify({ success: true, data }),
  };
}

test.beforeEach(async ({ page }) => {
  const workspaces: Workspace[] = [];
  const budgetModels: BudgetModel[] = [];
  const budgetTemplates: BudgetTemplate[] = [];

  await page.route((url) => url.pathname.startsWith('/api/'), async (route) => {
    const request = route.request();
    const url = new URL(request.url());
    const path = url.pathname;
    const method = request.method();

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
      await route.fulfill(api([]));
      return;
    }

    if (path === '/api/metadata/workspaces' && method === 'GET') {
      await route.fulfill(api(workspaces));
      return;
    }

    if (path === '/api/metadata/workspaces' && method === 'POST') {
      const input = request.postDataJSON() as { code: string; name: string };
      const workspace: Workspace = {
        id: `workspace-${workspaces.length + 1}`,
        code: input.code,
        name: input.name,
        status: 'ACTIVE',
      };
      workspaces.push(workspace);
      await route.fulfill(api(workspace));
      return;
    }

    if (path === '/api/metadata/dimensions') {
      await route.fulfill(api([]));
      return;
    }

    if (path.match(/^\/api\/metadata\/dimensions\/[^/]+\/members$/)) {
      await route.fulfill(api([]));
      return;
    }

    if (path === '/api/budget-models' && method === 'GET') {
      const workspaceId = url.searchParams.get('workspaceId');
      await route.fulfill(api(budgetModels.filter((model) => model.workspaceId === workspaceId)));
      return;
    }

    if (path === '/api/budget-models' && method === 'POST') {
      const input = request.postDataJSON() as {
        workspaceId: string;
        code: string;
        name: string;
        description?: string;
      };
      const budgetModel: BudgetModel = {
        id: `model-${budgetModels.length + 1}`,
        workspaceId: input.workspaceId,
        code: input.code,
        name: input.name,
        description: input.description ?? null,
        status: 'DRAFT',
      };
      budgetModels.push(budgetModel);
      await route.fulfill(api(budgetModel));
      return;
    }

    const modelDimensionsMatch = path.match(/^\/api\/budget-models\/([^/]+)\/dimensions$/);
    if (modelDimensionsMatch) {
      await route.fulfill(api([]));
      return;
    }

    if (path === '/api/budget-templates' && method === 'GET') {
      const budgetModelId = url.searchParams.get('budgetModelId');
      await route.fulfill(
        api(budgetTemplates.filter((template) => template.budgetModelId === budgetModelId)),
      );
      return;
    }

    if (path === '/api/budget-templates' && method === 'POST') {
      const input = request.postDataJSON() as {
        budgetModelId: string;
        code: string;
        name: string;
        description?: string;
      };
      const budgetTemplate: BudgetTemplate = {
        id: `template-${budgetTemplates.length + 1}`,
        budgetModelId: input.budgetModelId,
        code: input.code,
        name: input.name,
        description: input.description ?? null,
        status: 'DRAFT',
      };
      budgetTemplates.push(budgetTemplate);
      await route.fulfill(api(budgetTemplate));
      return;
    }

    if (path.match(/^\/api\/budget-templates\/[^/]+\/axes$/)) {
      await route.fulfill(api([]));
      return;
    }

    if (path === '/api/submissions/tasks') {
      await route.fulfill(api([]));
      return;
    }

    if (path === '/api/actual-imports/batches') {
      await route.fulfill(api([]));
      return;
    }

    if (path === '/api/audit/events') {
      await route.fulfill(api({ items: [], page: 0, size: 25, totalElements: 0, totalPages: 0 }));
      return;
    }

    await route.fulfill(api([]));
  });
});

test('creates workspace, budget model, and template through the workbench UI', async ({ page }) => {
  await page.goto('/');

  const workspacePanel = page.locator('section[aria-labelledby="workspace-title"]');
  await workspacePanel.getByLabel('Code').fill('AUTO_WS');
  await workspacePanel.getByLabel('Name').fill('Autopilot Workspace');
  await workspacePanel.getByRole('button', { name: 'Create' }).click();
  await expect(workspacePanel.getByText('AUTO_WS')).toBeVisible();
  await expect(workspacePanel.getByText('Autopilot Workspace')).toBeVisible();

  const modelPanel = page.locator('section[aria-labelledby="model-title"]');
  await expect(modelPanel.getByText('AUTO_WS')).toBeVisible();
  await modelPanel.getByLabel('Code').fill('AUTO_MODEL');
  await modelPanel.getByLabel('Name').fill('Autopilot Model');
  await modelPanel.getByLabel('Description').fill('Created by browser smoke.');
  await modelPanel.getByRole('button', { name: 'Create' }).click();
  await expect(modelPanel.getByText('AUTO_MODEL')).toBeVisible();
  await expect(modelPanel.getByText('Autopilot Model')).toBeVisible();

  const templatePanel = page.locator('section[aria-labelledby="template-title"]');
  await expect(templatePanel.getByText('AUTO_MODEL')).toBeVisible();
  await templatePanel.getByLabel('Code').fill('AUTO_TEMPLATE');
  await templatePanel.getByLabel('Name').fill('Autopilot Template');
  await templatePanel.getByLabel('Description').fill('Created by browser smoke.');
  await templatePanel.getByRole('button', { name: 'Create' }).click();
  await expect(templatePanel.getByText('AUTO_TEMPLATE')).toBeVisible();
  await expect(templatePanel.getByText('Autopilot Template')).toBeVisible();
});
