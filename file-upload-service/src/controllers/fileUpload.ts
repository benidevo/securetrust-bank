import { container } from 'tsyringe';
import { Request, Response } from 'express';
import { ApiResponse } from '../utils/apiResponse';
import FileUploadService from '../services/FileUpload';

export default class FileUploadController {
  public async create(req: Request, res: Response): Promise<Response> {
    const { type } = req.body;
    const fileUploadService = container.resolve(FileUploadService);

    const fileUrl = await fileUploadService.execute(type, req.file);

    const response = new ApiResponse(200, 'File upload successful', {
      file_url: fileUrl,
    });

    return response.send(res);
  }
}
