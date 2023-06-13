import { Router } from 'express';
import FileUploadController from '../controllers/fileUpload';
import asyncHandler from 'express-async-handler';
import FileUploadValidator from '../validators/fileUploadValidator';

const fileUploadController = new FileUploadController();

const router = Router();

router.post(
  '/api/v1/uploads',
  FileUploadValidator.validateFileUpload,
  FileUploadValidator.validateFileType,
  fileUploadController.create
);

const routes = asyncHandler(router);

export default routes;
