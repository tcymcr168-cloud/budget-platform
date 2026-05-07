import { DimensionType } from '../metadata/metadataApi';
import { requestJson } from '../../shared/api/http';

export type BudgetModelStatus = 'DRAFT' | 'ACTIVE' | 'INACTIVE';

export interface BudgetModel {
  id: string;
  workspaceId: string;
  code: string;
  name: string;
  description: string | null;
  status: BudgetModelStatus;
}

export interface BudgetModelDimensionBinding {
  id: string;
  budgetModelId: string;
  dimensionId: string;
  dimensionCode: string;
  dimensionName: string;
  dimensionRole: DimensionType;
  requiredForEntry: boolean;
  displayOrder: number;
}

export interface CreateBudgetModelInput {
  workspaceId: string;
  code: string;
  name: string;
  description?: string;
}

export interface BindBudgetModelDimensionInput {
  dimensionId: string;
  requiredForEntry?: boolean;
  displayOrder?: number;
}

export async function listBudgetModels(workspaceId: string) {
  const response = await requestJson<BudgetModel[]>(
    `/api/budget-models?workspaceId=${workspaceId}`,
  );
  return response.data ?? [];
}

export async function createBudgetModel(input: CreateBudgetModelInput) {
  const response = await requestJson<BudgetModel>('/api/budget-models', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function listBudgetModelDimensions(budgetModelId: string) {
  const response = await requestJson<BudgetModelDimensionBinding[]>(
    `/api/budget-models/${budgetModelId}/dimensions`,
  );
  return response.data ?? [];
}

export async function bindBudgetModelDimension(
  budgetModelId: string,
  input: BindBudgetModelDimensionInput,
) {
  const response = await requestJson<BudgetModelDimensionBinding>(
    `/api/budget-models/${budgetModelId}/dimensions`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(input),
    },
  );
  return response.data;
}

export async function activateBudgetModel(budgetModelId: string) {
  const response = await requestJson<BudgetModel>(
    `/api/budget-models/${budgetModelId}/activate`,
    { method: 'POST' },
  );
  return response.data;
}

export async function deactivateBudgetModel(budgetModelId: string) {
  const response = await requestJson<BudgetModel>(
    `/api/budget-models/${budgetModelId}/deactivate`,
    { method: 'POST' },
  );
  return response.data;
}
