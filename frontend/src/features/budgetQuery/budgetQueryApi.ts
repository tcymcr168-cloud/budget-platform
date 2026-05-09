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

export interface PageResponse<T> {
  items: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface FactQueryPageInput extends FactQueryFilters {
  page: number;
  size: number;
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

export interface FactSummaryPageInput extends FactSummaryFilters {
  page: number;
  size: number;
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

export interface BudgetActualVariancePageInput extends BudgetActualVarianceFilters {
  page: number;
  size: number;
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

export type FactQueryPage = PageResponse<FactQueryRow>;
export type FactSummaryPage = PageResponse<FactSummaryRow>;
export type BudgetActualVariancePage = PageResponse<BudgetActualVarianceRow>;

function buildQuery(
  filters:
    | FactQueryFilters
    | FactSummaryFilters
    | FactQueryPageInput
    | FactSummaryPageInput
    | BudgetActualVarianceFilters
    | BudgetActualVariancePageInput,
) {
  const params = new URLSearchParams();
  Object.entries(filters).forEach(([key, value]) => {
    if (value !== undefined && value !== '') {
      params.set(key, String(value));
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

export async function queryFactsPage(filters: FactQueryPageInput) {
  const response = await requestJson<FactQueryPage>(
    `/api/budget-query/facts/page?${buildQuery(filters)}`,
  );
  return response.data ?? {
    items: [],
    page: filters.page,
    size: filters.size,
    totalElements: 0,
    totalPages: 0,
  };
}

export async function summarizeFacts(filters: FactSummaryFilters) {
  const response = await requestJson<FactSummaryRow[]>(
    `/api/budget-query/summary?${buildQuery(filters)}`,
  );
  return response.data ?? [];
}

export async function summarizeFactsPage(filters: FactSummaryPageInput) {
  const response = await requestJson<FactSummaryPage>(
    `/api/budget-query/summary/page?${buildQuery(filters)}`,
  );
  return response.data ?? {
    items: [],
    page: filters.page,
    size: filters.size,
    totalElements: 0,
    totalPages: 0,
  };
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

export async function analyzeBudgetActualVariancePage(filters: BudgetActualVariancePageInput) {
  const response = await requestJson<BudgetActualVariancePage>(
    `/api/budget-query/variance/page?${buildQuery(filters)}`,
  );
  return response.data ?? {
    items: [],
    page: filters.page,
    size: filters.size,
    totalElements: 0,
    totalPages: 0,
  };
}
