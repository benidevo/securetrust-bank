class AppError {
  public readonly message: string;
  public readonly statusCode: number;
  public readonly error: Array<object> | object | null = null;

  constructor(message: string, statusCode = 400) {
    this.message = message;
    this.statusCode = statusCode;
  }
}

export default AppError;
