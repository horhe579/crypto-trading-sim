Crypto Trading Simulator Setup Guide

Run BE:

Install and configure PostgreSQL

Download or use a docker container, create a database

Create or edit application.properties in your project:
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/crypto_trading_sim
spring.datasource.username=postgres
spring.datasource.password=your_password_here

Run FE:

install node v 23: docker pull node:23-alpine

navigate to project folder and install dep npm install

run react app npm run dev