import { Response } from 'express';

class BaseApiResponse {
  success: boolean;
  message: string;
  data: Array<object> | object;
  error: Array<object> | object | null = null;
  statusCode: number;

  constructor(
    statusCode: number,
    message: string,
    data: Array<object> | object | null = null,
    error: Array<object> | object | null = null
  ) {
    this.message = message;
    this.data = data;
    this.statusCode = statusCode;
    this.error = error;
  }

  send(res: Response): Response {
    return res.status(this.statusCode).json({
      success: this.success,
      message: this.message,
      data: this.data,
      error: this.error,
    });
  }
}

export class ApiErrorResponse extends BaseApiResponse {
  success: boolean = false;

  constructor(
    statusCode: number,
    message: string,
    error: Array<object> | object | null = null
  ) {
    super(statusCode, message);
    this.error = error;
  }
}

export class ApiResponse extends BaseApiResponse {
  success: boolean = true;

  constructor(
    statusCode: number,
    message: string,
    data: Array<object> | object | null = null
  ) {
    super(statusCode, message, data);
  }
}
