import type { ApiRequestOptions, ApiResponse } from './types';

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
