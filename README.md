# SecureTrust Bank - Online Banking Platform

SecureTrust Bank is an online banking platform that allows users to create bank accounts, send and receive money securely. This repository contains the backend API implementation for SecureTrust Bank.

## Table of Contents

- [SecureTrust Bank - Online Banking Platform](#securetrust-bank---online-banking-platform)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Features](#features)
  - [Technology Stack](#technology-stack)
  - [License](#license)

## Overview

SecureTrust Bank provides a comprehensive backend API for managing user accounts, authentication, account verification, bank account creation, transaction management, and user profile management. The platform enforces daily transaction limits for newly created accounts and offers an account upgrade process upon user request and verification.

The backend API consists of four microservices:

1. User Account Service: Responsible for user authentication, account verification, and user profile management.
2. Notification Service: Handles sending verification codes and notifications to users via email.
3. Bank Account Service: Manages the creation of bank accounts, sets initial transaction limits, and processes account upgrades.
4. Transaction Service: Handles sending and receiving money between bank accounts, enforces transaction limits, and manages transaction history.

## Features

- User signup and authentication
- User account verification via email
- User profile management
- Bank account creation and management
- Account upgrade process with additional documentation
- Transaction processing and management
- Transaction history tracking

## Technology Stack

The technology stack used in this project is as follows:

- User Account Service:
  - Python
  - Django
  - Django Rest Framework
  - Redis
  - PostgreSQL
  - Docker

- Notification Service:
  - Typescript
  - Node.js
  - Express
  - Docker

- Bank Account Service:
  - Java
  - Spring Boot
  - Redis
  - MongoDB
  - Docker

- Transaction Service:
  - Java
  - Spring Boot
  - Redis
  - PostgreSQL
  - Docker

## License

The SecureTrust Bank backend API is open-source and released under the [MIT License](LICENSE). Feel free to modify and use the code as per the terms of the license.
