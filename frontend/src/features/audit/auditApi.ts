import { requestJson } from '../../shared/api/http';

export type AuditAction =
  | 'CREATE'
  | 'UPDATE'
  | 'DELETE'
  | 'STATUS_CHANGE'
  | 'IMPORT'
  | 'ACCESS_CHANGE';

export interface AuditEvent {
  id: string;
  actorId: string;
  subjectType: string;
  subjectId: string;
  action: AuditAction;
  occurredAt: string;
  detailsJson: string;
}

export interface PageResponse<T> {
  items: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface AuditSearchInput {
  actorId?: string;
  subjectType?: string;
  subjectId?: string;
  action?: AuditAction;
  page: number;
  size: number;
}

export async function searchAuditEvents(input: AuditSearchInput) {
  const params = new URLSearchParams();
  if (input.actorId) {
    params.set('actorId', input.actorId);
  }
  if (input.subjectType) {
    params.set('subjectType', input.subjectType);
  }
  if (input.subjectId) {
    params.set('subjectId', input.subjectId);
  }
  if (input.action) {
    params.set('action', input.action);
  }
  params.set('page', String(input.page));
  params.set('size', String(input.size));

  const response = await requestJson<PageResponse<AuditEvent>>(
    `/api/audit/events?${params.toString()}`,
  );
  return response.data ?? { items: [], page: input.page, size: input.size, totalElements: 0, totalPages: 0 };
}
