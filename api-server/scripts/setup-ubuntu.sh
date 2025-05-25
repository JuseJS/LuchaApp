#!/bin/bash

# Setup script for Ubuntu Server 20.04
# This script installs all necessary dependencies for the LuchaApp API server

set -e

echo "=== LuchaApp API Server Setup for Ubuntu 20.04 ==="

# Update system
echo "Updating system packages..."
sudo apt update && sudo apt upgrade -y

# Install Java 17
echo "Installing Java 17..."
sudo apt install -y openjdk-17-jdk

# Verify Java installation
java -version

# Install MongoDB (for Ubuntu 20.04 focal)
echo "Installing MongoDB..."
# Import the public key
wget -qO - https://www.mongodb.org/static/pgp/server-7.0.asc | sudo apt-key add -

# Create the list file for Ubuntu 20.04 (focal)
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/7.0 multiverse" | \
   sudo tee /etc/apt/sources.list.d/mongodb-org-7.0.list

sudo apt update
sudo apt install -y mongodb-org

# Configure MongoDB without SSL
echo "Configuring MongoDB..."
sudo tee /etc/mongod.conf > /dev/null <<EOF
storage:
  dbPath: /var/lib/mongodb
  journal:
    enabled: true

systemLog:
  destination: file
  logAppend: true
  path: /var/log/mongodb/mongod.log

net:
  port: 27017
  bindIp: 127.0.0.1

processManagement:
  timeZoneInfo: /usr/share/zoneinfo

security:
  authorization: enabled
EOF

# Start MongoDB
echo "Starting MongoDB..."
sudo systemctl daemon-reload
sudo systemctl enable mongod
sudo systemctl start mongod

# Wait for MongoDB to start
sleep 5

# Create MongoDB admin user
echo "Setting up MongoDB users..."
mongosh <<EOF
use admin
db.createUser({
  user: "admin",
  pwd: "$MONGO_ROOT_PASSWORD",
  roles: [ { role: "userAdminAnyDatabase", db: "admin" }, "readWriteAnyDatabase" ]
})

use lucha_canaria
db.createUser({
  user: "$MONGO_APP_USERNAME",
  pwd: "$MONGO_APP_PASSWORD",
  roles: [ { role: "readWrite", db: "lucha_canaria" } ]
})
EOF

# Install system utilities
echo "Installing system utilities..."
sudo apt install -y htop iotop git curl wget unzip

# Create application user
echo "Creating application user..."
sudo useradd -m -s /bin/bash luchaapp || true

# Create application directories
echo "Creating application directories..."
sudo mkdir -p /opt/luchaapp/api
sudo mkdir -p /var/log/luchaapp
sudo chown -R luchaapp:luchaapp /opt/luchaapp
sudo chown -R luchaapp:luchaapp /var/log/luchaapp

# Setup systemd service
echo "Creating systemd service..."
sudo tee /etc/systemd/system/luchaapp-api.service > /dev/null <<EOF
[Unit]
Description=LuchaApp API Server
After=network.target mongod.service
Requires=mongod.service

[Service]
Type=simple
User=luchaapp
Group=luchaapp
WorkingDirectory=/opt/luchaapp/api
EnvironmentFile=/opt/luchaapp/.env
ExecStart=/usr/bin/java -jar /opt/luchaapp/api/lucha-api-server.jar
Restart=always
RestartSec=10

# Logging
StandardOutput=append:/var/log/luchaapp/api.log
StandardError=append:/var/log/luchaapp/api-error.log

# Security
NoNewPrivileges=true
PrivateTmp=true

[Install]
WantedBy=multi-user.target
EOF

# Setup log rotation
echo "Setting up log rotation..."
sudo tee /etc/logrotate.d/luchaapp > /dev/null <<EOF
/var/log/luchaapp/*.log {
    daily
    rotate 30
    compress
    delaycompress
    missingok
    notifempty
    create 0644 luchaapp luchaapp
    sharedscripts
    postrotate
        systemctl reload luchaapp-api > /dev/null 2>&1 || true
    endscript
}
EOF

# Setup firewall
echo "Configuring firewall..."
sudo ufw allow 22/tcp
sudo ufw allow 8080/tcp
sudo ufw --force enable

echo "=== Setup Complete ==="
echo ""
echo "Next steps:"
echo "1. Copy the .env file to /opt/luchaapp/.env"
echo "2. Copy the API jar file to /opt/luchaapp/api/lucha-api-server.jar"
echo "3. Start the service: sudo systemctl start luchaapp-api"
echo "4. Enable auto-start: sudo systemctl enable luchaapp-api"
echo "5. Check status: sudo systemctl status luchaapp-api"
echo "6. View logs: sudo journalctl -u luchaapp-api -f"