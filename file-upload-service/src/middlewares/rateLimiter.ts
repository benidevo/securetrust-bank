import AppError from '../utils/appError';
import { Request, Response, NextFunction } from 'express';
import { RateLimiterMemory } from 'rate-limiter-flexible';

const limiter = new RateLimiterMemory({
  points: 10,
  duration: 5,
});

export default async function rateLimiter(
  request: Request,
  response: Response,
  next: NextFunction
): Promise<void> {

  try {
    await limiter.consume(request.ip);

    return next();
  } catch (error) {
    next(new AppError('Too many requests.', 429));
  }
}
