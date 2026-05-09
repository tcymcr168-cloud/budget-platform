import type { ApiRequestOptions, ApiResponse } from './types';

export interface ApiSecurityContext {
  userId: string;
  roles: string;
}

const devSecurityContextEnabled =
  import.meta.env.DEV && import.meta.env.VITE_ENABLE_DEV_SECURITY_CONTEXT !== 'false';

const defaultSecurityContext: ApiSecurityContext = {
  userId: 'admin@example.com',
  roles: 'BUDGET_ADMIN',
};

let securityContext: ApiSecurityContext = defaultSecurityContext;

export function setApiSecurityContext(nextContext: ApiSecurityContext) {
  securityContext = {
    userId: nextContext.userId.trim(),
    roles: nextContext.roles.trim(),
  };
}

export function getApiSecurityContext() {
  return securityContext;
}

export function isDevSecurityContextEnabled() {
  return devSecurityContextEnabled;
}

export async function requestJson<T>(
  input: string | URL,
  options: ApiRequestOptions = {},
): Promise<ApiResponse<T>> {
  const headers = new Headers(options.headers);

  if (!headers.has('Accept')) {
    headers.set('Accept', 'application/json');
  }

  if (options.requestId) {
    headers.set('X-Request-Id', options.requestId);
  }

  if (devSecurityContextEnabled && securityContext.userId && !headers.has('X-User-Id')) {
    headers.set('X-User-Id', securityContext.userId);
  }

  if (devSecurityContextEnabled && securityContext.roles && !headers.has('X-User-Roles')) {
    headers.set('X-User-Roles', securityContext.roles);
  }

  const response = await fetch(input, {
    ...options,
    headers,
  });

  const payload = (await response.json()) as ApiResponse<T>;

  if (!response.ok || !payload.success) {
    throw payload.error ?? new Error(`Request failed with status ${response.status}`);
  }

  return payload;
}
