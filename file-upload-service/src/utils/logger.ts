import morgan from 'morgan';
import { createLogger, format, transports } from 'winston';
import 'winston-daily-rotate-file';

const { combine, timestamp, prettyPrint, printf } = format;

const fileRotateTransport = new transports.DailyRotateFile({
  filename: 'logs/combined-%DATE%.log',
  datePattern: 'YYYY-MM-DD',
  maxFiles: '14d',
});

const consoleLogFormat = printf(({ level, message, timestamp, req }) => {
  if (req) {
    return `[${timestamp}] [${level.toUpperCase()}] ${req.method} ${
      req.originalUrl
    }: ${message}`;
  }
  return `[${timestamp}] [${level.toUpperCase()}]: ${message}`;
});

export const systemLogs = createLogger({
  level: 'http',
  format: combine(
    timestamp({
      format: 'YYYY-MM-DD hh:mm:ss.SSS A',
    }),
    prettyPrint()
  ),
  transports: [
    fileRotateTransport,
    new transports.File({
      level: 'error',
      filename: 'logs/error.log',
    }),
    new transports.Console({
      format: consoleLogFormat,
    }),
  ],
  exceptionHandlers: [new transports.File({ filename: 'logs/exception.log' })],
  rejectionHandlers: [new transports.File({ filename: 'logs/rejections.log' })],
});

export const morganMiddleware = morgan(
  function (tokens, req, res) {
    return JSON.stringify({
      method: tokens.method(req, res),
      url: tokens.url(req, res),
      status: Number.parseFloat(tokens.status(req, res)),
      content_length: tokens.res(req, res, 'content-length'),
      response_time: Number.parseFloat(tokens['response-time'](req, res)),
    });
  },
  {
    stream: {
      write: (message) => {
        const data = JSON.parse(message);
        systemLogs.http(`incoming-request`, data);
      },
    },
  }
);
