import 'dotenv/config';
import nodemailer from 'nodemailer';
import environment from './environment';

let transporter: nodemailer.Transporter;

const { nodeEnv, mailhogPort, mailhogServer } = environment;

if (nodeEnv == 'development' || 'test') {
  transporter = nodemailer.createTransport({
    host: mailhogServer,
    port: mailhogPort,
  });
} else if (nodeEnv == 'production') {
  transporter = nodemailer.createTransport({
    // TODO: configure mailgun for production
  });
}

export default transporter;
