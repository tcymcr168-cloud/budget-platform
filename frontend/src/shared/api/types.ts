export interface ApiError {
  code: string;
  message: string;
  traceId: string;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T | null;
  error: ApiError | null;
}

export interface ApiRequestOptions extends RequestInit {
  requestId?: string;
}
