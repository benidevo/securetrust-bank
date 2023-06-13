import { container } from 'tsyringe';
import FileUploadService from '../services/FileUpload';

// register dependencies
container.register<FileUploadService>(FileUploadService, {
  useClass: FileUploadService,
});

export default container;
