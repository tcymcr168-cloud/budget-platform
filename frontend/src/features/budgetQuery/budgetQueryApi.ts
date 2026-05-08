import { requestJson } from '../../shared/api/http';
import { FactValueStatus } from '../submissions/submissionApi';

export type QueryGroupBy = 'ACCOUNT' | 'ENTITY' | 'TIME' | 'CATEGORY' | 'VERSION';

export interface FactQueryFilters {
  budgetModelId: string;
  entityMemberId?: string;
  timeMemberId?: string;
  categoryMemberId?: string;
  versionMemberId?: string;
  status?: FactValueStatus;
}

export interface FactQueryRow {
  id: string;
  budgetModelId: string;
  budgetTemplateId: string | null;
  submissionTaskId: string | null;
  importBatchId: string | null;
  accountCode: string;
  accountName: string;
  entityCode: string;
  timeCode: string;
  categoryCode: string;
  versionCode: string;
  amount: number;
  valueStatus: FactValueStatus;
  sourceType: 'BUDGET_SUBMISSION' | 'ACTUAL_IMPORT';
}

export interface FactSummaryRow {
  groupBy: QueryGroupBy;
  memberId: string;
  memberCode: string;
  memberName: string;
  totalAmount: number;
  lineCount: number;
}

export interface FactSummaryFilters extends FactQueryFilters {
  groupBy: QueryGroupBy;
}

export interface BudgetActualVarianceFilters {
  budgetModelId: string;
  budgetCategoryMemberId: string;
  actualCategoryMemberId: string;
  budgetVersionMemberId?: string;
  actualVersionMemberId?: string;
  entityMemberId?: string;
  timeMemberId?: string;
  status?: FactValueStatus;
}

export interface BudgetActualVarianceRow {
  accountMemberId: string;
  accountCode: string;
  accountName: string;
  entityMemberId: string;
  entityCode: string;
  entityName: string;
  timeMemberId: string;
  timeCode: string;
  timeName: string;
  budgetAmount: number;
  actualAmount: number;
  varianceAmount: number;
  variancePercent: number | null;
  budgetLineCount: number;
  actualLineCount: number;
}

function buildQuery(filters: FactQueryFilters | FactSummaryFilters) {
  const params = new URLSearchParams();
  Object.entries(filters).forEach(([key, value]) => {
    if (value) {
      params.set(key, value);
    }
  });
  return params.toString();
}

export async function queryFacts(filters: FactQueryFilters) {
  const response = await requestJson<FactQueryRow[]>(
    `/api/budget-query/facts?${buildQuery(filters)}`,
  );
  return response.data ?? [];
}

export async function summarizeFacts(filters: FactSummaryFilters) {
  const response = await requestJson<FactSummaryRow[]>(
    `/api/budget-query/summary?${buildQuery(filters)}`,
  );
  return response.data ?? [];
}

export async function exportFactsCsv(filters: FactQueryFilters) {
  const response = await fetch(`/api/budget-query/facts.csv?${buildQuery(filters)}`, {
    headers: { Accept: 'text/csv' },
  });

  if (!response.ok) {
    throw new Error(`CSV export failed with status ${response.status}`);
  }

  return response.text();
}

export async function analyzeBudgetActualVariance(filters: BudgetActualVarianceFilters) {
  const response = await requestJson<BudgetActualVarianceRow[]>(
    `/api/budget-query/variance?${buildQuery(filters)}`,
  );
  return response.data ?? [];
}
