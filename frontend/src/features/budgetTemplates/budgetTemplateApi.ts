import { BudgetModelDimensionBinding } from '../budgetModels/budgetModelApi';
import { requestJson } from '../../shared/api/http';

export type BudgetTemplateStatus = 'DRAFT' | 'ACTIVE' | 'INACTIVE';
export type TemplateAxisType = 'ROW' | 'COLUMN' | 'FILTER';

export interface BudgetTemplate {
  id: string;
  budgetModelId: string;
  code: string;
  name: string;
  description: string | null;
  status: BudgetTemplateStatus;
}

export interface TemplateAxis {
  id: string;
  budgetTemplateId: string;
  modelDimensionId: string;
  dimensionId: string;
  dimensionCode: string;
  dimensionName: string;
  dimensionRole: BudgetModelDimensionBinding['dimensionRole'];
  axisType: TemplateAxisType;
  memberSelector: string;
  displayOrder: number;
}

export interface CreateBudgetTemplateInput {
  budgetModelId: string;
  code: string;
  name: string;
  description?: string;
}

export interface CreateTemplateAxisInput {
  modelDimensionId: string;
  axisType: TemplateAxisType;
  memberSelector?: string;
  displayOrder?: number;
}

export async function listBudgetTemplates(budgetModelId: string) {
  const response = await requestJson<BudgetTemplate[]>(
    `/api/budget-templates?budgetModelId=${budgetModelId}`,
  );
  return response.data ?? [];
}

export async function createBudgetTemplate(input: CreateBudgetTemplateInput) {
  const response = await requestJson<BudgetTemplate>('/api/budget-templates', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function listTemplateAxes(budgetTemplateId: string) {
  const response = await requestJson<TemplateAxis[]>(
    `/api/budget-templates/${budgetTemplateId}/axes`,
  );
  return response.data ?? [];
}

export async function createTemplateAxis(
  budgetTemplateId: string,
  input: CreateTemplateAxisInput,
) {
  const response = await requestJson<TemplateAxis>(
    `/api/budget-templates/${budgetTemplateId}/axes`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(input),
    },
  );
  return response.data;
}

export async function activateBudgetTemplate(budgetTemplateId: string) {
  const response = await requestJson<BudgetTemplate>(
    `/api/budget-templates/${budgetTemplateId}/activate`,
    { method: 'POST' },
  );
  return response.data;
}

export async function deactivateBudgetTemplate(budgetTemplateId: string) {
  const response = await requestJson<BudgetTemplate>(
    `/api/budget-templates/${budgetTemplateId}/deactivate`,
    { method: 'POST' },
  );
  return response.data;
}
