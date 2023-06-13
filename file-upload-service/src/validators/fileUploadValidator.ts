import { Request, Response, NextFunction } from 'express';
import multer, { FileFilterCallback } from 'multer';
import { celebrate, Joi } from 'celebrate';

const upload = multer({
  storage: multer.memoryStorage(),
  fileFilter: fileFilter,
}).single('_file');

function fileFilter(
  req: Request,
  file: Express.Multer.File,
  callback: FileFilterCallback
): void {
  const allowedExtensions = ['.pdf', '.docx', '.jpeg', '.jpg'];
  const fileExtension = file.originalname
    .toLowerCase()
    .substring(file.originalname.lastIndexOf('.'));
  const allowedMimeTypes = [
    'application/pdf',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'image/jpeg',
    'image/jpg',
  ];

  if (
    allowedExtensions.includes(fileExtension) &&
    allowedMimeTypes.includes(file.mimetype)
  ) {
    callback(null, true);
  } else {
    callback(Error('Invalid file type or extension'));
  }
}

class FileUploadValidator {
  validateFileUpload(req: Request, res: Response, next: NextFunction): void {
    upload(req, res, (error: any) => {
      if (error) {
        next(error);
        return;
      }

      if (!req.file) {
        next(new Error('File not provided'));
        return;
      }

      next();
    });
  }

  validateFileType(req: Request, res: Response, next: NextFunction): void {
    celebrate({
      body: Joi.object({
        type: Joi.string().valid('avatar', 'doc').required(),
      }),
    })(req, res, next);
  }
}

export default new FileUploadValidator();
