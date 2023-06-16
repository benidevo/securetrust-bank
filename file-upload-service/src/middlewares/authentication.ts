import { Request, Response, NextFunction } from 'express';
import jwt from 'jsonwebtoken';
import fs from 'fs';
import path from 'path';
import { systemLogs } from '../utils/logger';
import { ApiErrorResponse } from '../utils/apiResponse';

const publicKeyPath = path.join(__dirname, '../../public.key');

export const authMiddleware = (
  req: Request,
  res: Response,
  next: NextFunction
) => {
  const { authorization } = req.headers;

  if (!authorization || !authorization.startsWith('Bearer ')) {
    const response = new ApiErrorResponse(403, 'Unauthorized');
    return response.send(res);
  }

  const token = authorization.split(' ')[1];

  try {
    const publicKey = fs.readFileSync(publicKeyPath);

    jwt.verify(token, publicKey, {}, (err) => {
      if (err) {
        const response = new ApiErrorResponse(403, 'Unauthorized');
        return response.send(res);
      }
      next();
    });
  } catch (err) {
    systemLogs.error(err);
    const response = new ApiErrorResponse(403, 'Unauthorized');
    return response.send(res);
  }
};
