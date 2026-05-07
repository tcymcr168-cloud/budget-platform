import { requestJson } from '../../shared/api/http';

export type MetadataStatus = 'ACTIVE' | 'INACTIVE';

export type DimensionType =
  | 'ACCOUNT'
  | 'ENTITY'
  | 'TIME'
  | 'CATEGORY'
  | 'VERSION'
  | 'CUSTOM';

export interface Workspace {
  id: string;
  code: string;
  name: string;
  status: MetadataStatus;
}

export interface Dimension {
  id: string;
  workspaceId: string;
  code: string;
  name: string;
  dimensionType: DimensionType;
  system: boolean;
  status: MetadataStatus;
}

export interface DimensionMember {
  id: string;
  dimensionId: string;
  code: string;
  name: string;
  parentId: string | null;
  sortOrder: number | null;
  leaf: boolean;
  status: MetadataStatus;
}

export interface CreateWorkspaceInput {
  code: string;
  name: string;
}

export interface CreateDimensionInput {
  workspaceId: string;
  code: string;
  name: string;
  dimensionType: DimensionType;
  system: boolean;
}

export interface CreateMemberInput {
  code: string;
  name: string;
  parentId?: string;
  sortOrder?: number;
}

export async function listWorkspaces() {
  const response = await requestJson<Workspace[]>('/api/metadata/workspaces');
  return response.data ?? [];
}

export async function createWorkspace(input: CreateWorkspaceInput) {
  const response = await requestJson<Workspace>('/api/metadata/workspaces', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function listDimensions(workspaceId: string) {
  const response = await requestJson<Dimension[]>(
    `/api/metadata/dimensions?workspaceId=${workspaceId}`,
  );
  return response.data ?? [];
}

export async function createDimension(input: CreateDimensionInput) {
  const response = await requestJson<Dimension>('/api/metadata/dimensions', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function listMembers(dimensionId: string) {
  const response = await requestJson<DimensionMember[]>(
    `/api/metadata/dimensions/${dimensionId}/members`,
  );
  return response.data ?? [];
}

export async function createMember(dimensionId: string, input: CreateMemberInput) {
  const response = await requestJson<DimensionMember>(
    `/api/metadata/dimensions/${dimensionId}/members`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(input),
    },
  );
  return response.data;
}
