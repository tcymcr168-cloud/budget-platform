import { requestJson } from '../../shared/api/http';

export type SecurityRoleCode =
  | 'BUDGET_ADMIN'
  | 'METADATA_MANAGER'
  | 'TEMPLATE_DESIGNER'
  | 'BUDGET_OWNER'
  | 'BUDGET_REVIEWER'
  | 'IMPORT_OPERATOR'
  | 'READ_ONLY';

export type SecurityUserStatus = 'ACTIVE' | 'INACTIVE';
export type SecurityGrantStatus = 'ACTIVE' | 'REVOKED';

export interface SecurityUser {
  id: string;
  username: string;
  displayName: string;
  email: string | null;
  status: SecurityUserStatus;
}

export interface UserRole {
  id: string;
  userId: string;
  workspaceId: string;
  workspaceCode: string;
  roleCode: SecurityRoleCode;
  status: SecurityGrantStatus;
  revokedAt: string | null;
}

export interface EntityScope {
  id: string;
  userId: string;
  workspaceId: string;
  workspaceCode: string;
  entityMemberId: string;
  entityMemberCode: string;
  entityMemberName: string;
  includeDescendants: boolean;
  status: SecurityGrantStatus;
  revokedAt: string | null;
}

export interface CurrentUser {
  userId: string | null;
  roles: SecurityRoleCode[];
  authenticated: boolean;
  authMode: 'DEV_HEADER' | 'JWT' | 'REVERSE_PROXY' | null;
  applicationRoles: UserRole[];
  entityScopes: EntityScope[];
}

export interface CreateSecurityUserInput {
  username: string;
  displayName: string;
  email?: string;
}

export interface GrantUserRoleInput {
  workspaceId: string;
  roleCode: SecurityRoleCode;
}

export interface GrantEntityScopeInput {
  workspaceId: string;
  entityMemberId: string;
  includeDescendants: boolean;
}

export interface SecurityActionReasonInput {
  reason?: string;
}

export async function listSecurityUsers() {
  const response = await requestJson<SecurityUser[]>('/api/security/users');
  return response.data ?? [];
}

export async function createSecurityUser(input: CreateSecurityUserInput) {
  const response = await requestJson<SecurityUser>('/api/security/users', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function disableSecurityUser(userId: string, input: SecurityActionReasonInput) {
  const response = await requestJson<SecurityUser>(`/api/security/users/${userId}/disable`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function enableSecurityUser(userId: string, input: SecurityActionReasonInput) {
  const response = await requestJson<SecurityUser>(`/api/security/users/${userId}/enable`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function listUserRoles(userId: string, workspaceId?: string) {
  const query = workspaceId ? `?workspaceId=${workspaceId}` : '';
  const response = await requestJson<UserRole[]>(`/api/security/users/${userId}/roles${query}`);
  return response.data ?? [];
}

export async function grantUserRole(userId: string, input: GrantUserRoleInput) {
  const response = await requestJson<UserRole>(`/api/security/users/${userId}/roles`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(input),
  });
  return response.data;
}

export async function revokeUserRole(
  userId: string,
  roleId: string,
  input: SecurityActionReasonInput,
) {
  const response = await requestJson<UserRole>(
    `/api/security/users/${userId}/roles/${roleId}/revoke`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(input),
    },
  );
  return response.data;
}

export async function listEntityScopes(userId: string, workspaceId?: string) {
  const query = workspaceId ? `?workspaceId=${workspaceId}` : '';
  const response = await requestJson<EntityScope[]>(
    `/api/security/users/${userId}/entity-scopes${query}`,
  );
  return response.data ?? [];
}

export async function grantEntityScope(userId: string, input: GrantEntityScopeInput) {
  const response = await requestJson<EntityScope>(
    `/api/security/users/${userId}/entity-scopes`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(input),
    },
  );
  return response.data;
}

export async function revokeEntityScope(
  userId: string,
  scopeId: string,
  input: SecurityActionReasonInput,
) {
  const response = await requestJson<EntityScope>(
    `/api/security/users/${userId}/entity-scopes/${scopeId}/revoke`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(input),
    },
  );
  return response.data;
}

export async function getCurrentUser() {
  const response = await requestJson<CurrentUser>('/api/security/me');
  return response.data;
}
