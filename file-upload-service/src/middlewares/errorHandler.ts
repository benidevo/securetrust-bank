import { Request, Response, NextFunction } from 'express';
import { CelebrateError } from 'celebrate';
import { TokenExpiredError, JsonWebTokenError } from 'jsonwebtoken';

import AppError from '../utils/appError';
import { MulterError } from 'multer';
import { systemLogs } from '../utils/logger';
import { ApiErrorResponse } from '../utils/apiResponse';

export default function errorHandler(
  error: Error,
  req: Request,
  res: Response,
  _: NextFunction
): Response {
  if (error instanceof AppError) {
    const response = new ApiErrorResponse(error.statusCode, error.message);
    return response.send(res);
  }

  if (error instanceof CelebrateError) {
    const bodyMessage = error.details.get('body')?.details;
    const queryMessage = error.details.get('query')?.details;
    const paramsMessage = error.details.get('params')?.details;

    const response = new ApiErrorResponse(
      400,
      'Validation error',
      bodyMessage || queryMessage || paramsMessage
    );

    return response.send(res);
  }

  if (error instanceof MulterError && error.code === 'LIMIT_UNEXPECTED_FILE') {
    const response = new ApiErrorResponse(
      400,
      `${error.message} '${error.field}'`
    );

    return response.send(res);
  }

  if (
    error instanceof TokenExpiredError ||
    error instanceof JsonWebTokenError
  ) {
    const response = new ApiErrorResponse(403, 'Invalid or expired token');
    return response.send(res);
  }

  if (error instanceof Error) {
    const response = new ApiErrorResponse(400, error.message);
    return response.send(res);
  }

  systemLogs.error(error);

  const response = new ApiErrorResponse(500, 'Internal server error');
  return response.send(res);
}
