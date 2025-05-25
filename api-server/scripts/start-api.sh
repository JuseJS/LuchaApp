#!/bin/bash

# Script to start the LuchaApp API server

# Set the path to the .env file
export DOTENV_PATH="/opt/luchaapp/.env"

# Alternative: Set individual environment variables if .env is not found
# Uncomment and modify these lines if needed:
# export JWT_SECRET="your-secret-here"
# export DATABASE_NAME="lucha_canaria"
# 
# For MongoDB with authentication:
# export MONGODB_URI="mongodb://lucha_canaria_app:password@localhost:27017/lucha_canaria?authSource=lucha_canaria"
# 
# For MongoDB without authentication:
# export MONGODB_URI="mongodb://localhost:27017"

# Change to the API directory
cd /opt/luchaapp/api

# Check if the JAR file exists
JAR_FILE="build/libs/luchaapp-api-all.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "Error: JAR file not found at $JAR_FILE"
    echo "Please build the project first with: ./gradlew shadowJar"
    exit 1
fi

# Start the API server
echo "Starting LuchaApp API server..."
echo "Using .env from: $DOTENV_PATH"
java -jar "$JAR_FILE"