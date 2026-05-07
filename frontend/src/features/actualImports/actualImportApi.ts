import { requestJson } from '../../shared/api/http';

export type ActualImportStatus = 'VALIDATED' | 'COMMITTED' | 'FAILED';
export type ActualImportRowStatus = 'VALID' | 'ERROR';

export interface ActualImportRow {
  id: string;
  rowNumber: number;
  accountCode: string;
  entityCode: string;
  timeCode: string;
  categoryCode: string;
  versionCode: string;
  amount: number | null;
  rowStatus: ActualImportRowStatus;
  errorMessage: string | null;
}

export interface ActualImportBatch {
  id: string;
  budgetModelId: string;
  fileName: string;
  operatorUser: string;
  status: ActualImportStatus;
  totalRows: number;
  validRows: number;
  errorRows: number;
  totalAmount: number;
  errorReport: string | null;
  rows: ActualImportRow[];
}

export interface ValidateActualImportInput {
  budgetModelId: string;
  fileName: string;
  operatorUser: string;
  csvContent: string;
}

export async function validateActualImport(input: ValidateActualImportInput) {
  const response = await requestJson<ActualImportBatch>('/api/actual-imports/validate', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function commitActualImport(batchId: string) {
  const response = await requestJson<ActualImportBatch>(`/api/actual-imports/${batchId}/commit`, {
    method: 'POST',
  });
  return response.data;
}

export async function listActualImportBatches(budgetModelId: string) {
  const response = await requestJson<ActualImportBatch[]>(
    `/api/actual-imports/batches?budgetModelId=${budgetModelId}`,
  );
  return response.data ?? [];
}

export async function listActualImportRows(batchId: string) {
  const response = await requestJson<ActualImportRow[]>(
    `/api/actual-imports/batches/${batchId}/rows`,
  );
  return response.data ?? [];
}
