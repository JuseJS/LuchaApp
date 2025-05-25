#!/bin/bash

# Deployment script for LuchaApp API
# This script builds and deploys the API to the server

set -e

# Configuration
SERVER_USER=${SERVER_USER:-luchaapp}
SERVER_HOST=${SERVER_HOST:-guanxemc.asuscomm.com}
SERVER_PATH="/opt/luchaapp/api"
SERVER_PORT=${SERVER_PORT:-8080}

echo "=== Building LuchaApp API ==="

# Clean and build
echo "Building JAR file..."
./gradlew clean shadowJar

# Check if build was successful
if [ ! -f "build/libs/lucha-api-server-1.0.0.jar" ]; then
    echo "Build failed! JAR file not found."
    exit 1
fi

echo "Build successful!"

# Deploy to server
echo "=== Deploying to server ==="

# Copy JAR file
echo "Copying JAR file to server..."
scp build/libs/lucha-api-server-1.0.0.jar ${SERVER_USER}@${SERVER_HOST}:${SERVER_PATH}/lucha-api-server.jar

# Copy .env file if it doesn't exist on server
echo "Checking .env file..."
ssh ${SERVER_USER}@${SERVER_HOST} "test -f /opt/luchaapp/.env" || {
    echo "Copying .env file..."
    scp ../.env ${SERVER_USER}@${SERVER_HOST}:/opt/luchaapp/.env
}

# Restart service
echo "Restarting API service..."
ssh ${SERVER_USER}@${SERVER_HOST} "sudo systemctl restart luchaapp-api"

# Check service status
echo "Checking service status..."
ssh ${SERVER_USER}@${SERVER_HOST} "sudo systemctl status luchaapp-api --no-pager"

echo "=== Deployment complete ==="
echo "API should be accessible at http://${SERVER_HOST}:${SERVER_PORT}/api/v1/health"
echo ""
echo "Make sure port ${SERVER_PORT} is forwarded in your router configuration"