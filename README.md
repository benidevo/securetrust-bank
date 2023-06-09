# SecureTrust Bank - Online Banking Platform

SecureTrust Bank is an online banking platform that allows users to create bank accounts, send and receive money securely. This repository contains the backend API implementation for SecureTrust Bank.

## Table of Contents

- [SecureTrust Bank - Online Banking Platform](#securetrust-bank---online-banking-platform)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Features](#features)
  - [Technology Stack](#technology-stack)
  - [Run The App](#run-the-app)
  - [Docs](#docs)
  - [License](#license)

## Overview

SecureTrust Bank provides a comprehensive backend API for managing user accounts, authentication, account verification, bank account creation, transaction management, and user profile management. The platform enforces daily transaction limits for newly created accounts and offers an account upgrade process upon user request and verification.

The backend API consists of six microservices:

1. User Account Service: Responsible for user authentication, account verification, and user profile management.
2. Notification Service: Handles sending verification codes and notifications to users via email.
3. Bank Account Service: Manages the creation of bank accounts, sets initial transaction limits, and processes account upgrades.
4. Transaction Service: Handles sending and receiving money between bank accounts, enforces transaction limits, and manages transaction history.
5. File Upload Service: This microservice is responsible for handling file uploads within the application. It provides the functionality to securely upload files, store them in a storage system, and retrieve them when needed.
6. Search Service: Enables fast and accurate search functionality across the application using Elasticsearch. Users can easily search for users, bank accounts, transactions, and other relevant entities, enhancing the overall user experience.

## Features

- User signup and authentication
- User account verification via email
- User profile management
- Bank account creation and management
- Account upgrade process with additional documentation
- Transaction processing and management
- Transaction history tracking
- File upload, processing and retrieval

## Technology Stack

The technology stack used in this project is as follows:

- User Account Service: Python | Django | Redis | PostgreSQL | RabbitMQ | Docker

- Notification Service: Typescript | Node.js | Express | RabbitMQ | Docker

- Bank Account Service: Java | Spring Boot | Redis | MySQL | Docker

- Transaction Service:  Java | Spring Boot | Redis | MongoDB | Docker

- File Upload Service: Typescript | Node.js | Express | AWS S3 | Docker

- Search Service: Elasticsearch | Docker

## Run The App

  ***Only run this command the first time you clone the project***</br>
  Ensure you are in the base directory and in a unix (macOS or Linux or WSL) environment:

  ```bash
  sh setup.sh
  ```

  This will take some time to pull images and build the containers. Subsequently, run:

  To stop the app:

  ```bash
  make down
  ```

  Other commands can be found in the Makefile in the root directory.

## Docs

- User Account Service: <http://localhost/api/v1/user-account/api-docs>
- Bank Account Service: <http://localhost/api/v1/bank-account/api-docs>

## License

The SecureTrust Bank backend API is open-source and released under the [MIT License](LICENSE). Feel free to modify and use the code as per the terms of the license.
