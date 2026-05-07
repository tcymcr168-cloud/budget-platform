import { requestJson } from '../../shared/api/http';

export type SubmissionStatus =
  | 'NOT_STARTED'
  | 'DRAFT'
  | 'SUBMITTED'
  | 'RETURNED'
  | 'APPROVED'
  | 'LOCKED';

export type FactValueStatus = 'DRAFT' | 'SUBMITTED' | 'APPROVED' | 'LOCKED';

export interface SubmissionTask {
  id: string;
  budgetTemplateId: string;
  budgetModelId: string;
  entityMemberId: string;
  entityMemberCode: string;
  timeMemberId: string;
  timeMemberCode: string;
  categoryMemberId: string;
  categoryMemberCode: string;
  versionMemberId: string;
  versionMemberCode: string;
  ownerUser: string;
  reviewerUser: string;
  status: SubmissionStatus;
  returnReason: string | null;
}

export interface FactValue {
  id: string;
  submissionTaskId: string;
  accountMemberId: string;
  accountMemberCode: string;
  accountMemberName: string;
  amount: number;
  valueStatus: FactValueStatus;
  sourceType: 'BUDGET_SUBMISSION';
  note: string | null;
}

export interface CreateSubmissionTaskInput {
  budgetTemplateId: string;
  entityMemberId: string;
  timeMemberId: string;
  categoryMemberId: string;
  versionMemberId: string;
  ownerUser: string;
  reviewerUser: string;
}

export interface SaveFactValueInput {
  accountMemberId: string;
  amount: number;
  note?: string;
}

export async function listSubmissionTasks(budgetTemplateId: string) {
  const response = await requestJson<SubmissionTask[]>(
    `/api/submissions/tasks?budgetTemplateId=${budgetTemplateId}`,
  );
  return response.data ?? [];
}

export async function createSubmissionTask(input: CreateSubmissionTaskInput) {
  const response = await requestJson<SubmissionTask>('/api/submissions/tasks', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function listFactValues(taskId: string) {
  const response = await requestJson<FactValue[]>(
    `/api/submissions/tasks/${taskId}/values`,
  );
  return response.data ?? [];
}

export async function saveFactValue(taskId: string, input: SaveFactValueInput) {
  const response = await requestJson<FactValue>(`/api/submissions/tasks/${taskId}/values`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function submitSubmissionTask(taskId: string) {
  const response = await requestJson<SubmissionTask>(
    `/api/submissions/tasks/${taskId}/submit`,
    { method: 'POST' },
  );
  return response.data;
}

export async function returnSubmissionTask(taskId: string, reason: string) {
  const response = await requestJson<SubmissionTask>(
    `/api/submissions/tasks/${taskId}/return`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ reason }),
    },
  );
  return response.data;
}

export async function approveSubmissionTask(taskId: string) {
  const response = await requestJson<SubmissionTask>(
    `/api/submissions/tasks/${taskId}/approve`,
    { method: 'POST' },
  );
  return response.data;
}

export async function lockSubmissionTask(taskId: string) {
  const response = await requestJson<SubmissionTask>(
    `/api/submissions/tasks/${taskId}/lock`,
    { method: 'POST' },
  );
  return response.data;
}
