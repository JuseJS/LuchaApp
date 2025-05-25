#!/bin/bash

# Script de recuperación para completar la instalación después de problemas con MongoDB
# Este script contiene las partes que no se ejecutaron del setup original

set -e

echo "=== Completando instalación de LuchaApp API Server ==="

# Verificar variables de entorno necesarias
if [ -z "$MONGO_APP_USERNAME" ] || [ -z "$MONGO_APP_PASSWORD" ]; then
    echo "Error: Variables de entorno requeridas no configuradas!"
    echo "Por favor configura:"
    echo "  export MONGO_APP_USERNAME='luchaapp'"
    echo "  export MONGO_APP_PASSWORD='your_app_password'"
    exit 1
fi

# Instalar utilidades del sistema
echo "Instalando utilidades del sistema..."
sudo apt install -y htop iotop git curl wget unzip net-tools

# Crear usuario de aplicación
echo "Creando usuario de aplicación..."
sudo useradd -m -s /bin/bash luchaapp || true

# Crear directorios de aplicación
echo "Creando directorios de aplicación..."
sudo mkdir -p /opt/luchaapp/api
sudo mkdir -p /var/log/luchaapp
sudo chown -R luchaapp:luchaapp /opt/luchaapp
sudo chown -R luchaapp:luchaapp /var/log/luchaapp

# Crear archivo de entorno
echo "Creando archivo de entorno..."
sudo tee /opt/luchaapp/.env > /dev/null <<EOF
# MongoDB Configuration
MONGO_HOST=127.0.0.1
MONGO_PORT=27017
MONGO_DATABASE=lucha_canaria
MONGO_USERNAME=$MONGO_APP_USERNAME
MONGO_PASSWORD=$MONGO_APP_PASSWORD
MONGO_AUTH_SOURCE=lucha_canaria

# MongoDB Connection String (alternative)
MONGO_URI=mongodb://$MONGO_APP_USERNAME:$MONGO_APP_PASSWORD@127.0.0.1:27017/lucha_canaria?authSource=lucha_canaria

# Server Configuration
SERVER_PORT=8080
SERVER_HOST=0.0.0.0

# Java Options
JAVA_OPTS=-Xmx512m -Xms256m

# Application Environment
APP_ENV=production
EOF

# Configurar permisos para archivo .env
sudo chown luchaapp:luchaapp /opt/luchaapp/.env
sudo chmod 600 /opt/luchaapp/.env

# Configurar servicio systemd
echo "Creando servicio systemd..."
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
ExecStart=/usr/bin/java \$JAVA_OPTS -jar /opt/luchaapp/api/lucha-api-server.jar
Restart=always
RestartSec=10
SuccessExitStatus=143

# Logging
StandardOutput=append:/var/log/luchaapp/api.log
StandardError=append:/var/log/luchaapp/api-error.log

# Security
NoNewPrivileges=true
PrivateTmp=true
ProtectSystem=strict
ProtectHome=true
ReadWritePaths=/opt/luchaapp /var/log/luchaapp

[Install]
WantedBy=multi-user.target
EOF

# Configurar rotación de logs
echo "Configurando rotación de logs..."
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

# Configurar rotación de logs de MongoDB
sudo tee /etc/logrotate.d/mongodb > /dev/null <<EOF
/var/log/mongodb/*.log {
    daily
    rotate 30
    compress
    delaycompress
    missingok
    notifempty
    create 0640 mongodb mongodb
    sharedscripts
    postrotate
        /bin/kill -SIGUSR1 \$(cat /var/run/mongodb/mongod.pid 2>/dev/null) 2>/dev/null || true
    endscript
}
EOF

# Configurar firewall
echo "Configurando firewall..."
sudo ufw allow 22/tcp comment 'SSH'
sudo ufw allow 8080/tcp comment 'LuchaApp API'
# MongoDB puerto 27017 permanece cerrado al acceso externo
sudo ufw --force enable

# Crear scripts helper
echo "Creando scripts helper..."
sudo tee /opt/luchaapp/start.sh > /dev/null <<'EOF'
#!/bin/bash
sudo systemctl start luchaapp-api
echo "LuchaApp API iniciado"
EOF

sudo tee /opt/luchaapp/stop.sh > /dev/null <<'EOF'
#!/bin/bash
sudo systemctl stop luchaapp-api
echo "LuchaApp API detenido"
EOF

sudo tee /opt/luchaapp/logs.sh > /dev/null <<'EOF'
#!/bin/bash
sudo journalctl -u luchaapp-api -f
EOF

sudo tee /opt/luchaapp/mongo-connect.sh > /dev/null <<'EOF'
#!/bin/bash
source /opt/luchaapp/.env
mongosh --username $MONGO_USERNAME --password $MONGO_PASSWORD --authenticationDatabase $MONGO_DATABASE $MONGO_DATABASE
EOF

sudo tee /opt/luchaapp/mongo-admin.sh > /dev/null <<'EOF'
#!/bin/bash
echo "Conectando a MongoDB como admin..."
echo "Usa 'show dbs' para listar bases de datos"
echo "Usa 'use lucha_canaria' para cambiar a la base de datos de la aplicación"
echo "Usa 'db.getUsers()' para listar usuarios"
echo "Usa 'exit' para salir"
echo ""
mongosh --username admin --authenticationDatabase admin
EOF

# Hacer scripts ejecutables
sudo chmod +x /opt/luchaapp/*.sh
sudo chown luchaapp:luchaapp /opt/luchaapp/*.sh

# Recargar systemd
sudo systemctl daemon-reload

# Mostrar resumen
echo ""
echo "=== Configuración Completada! ==="
echo ""
echo "Estado de servicios:"
echo "- MongoDB: $(sudo systemctl is-active mongod)"
echo "- Firewall: $(sudo ufw status | grep Status: || echo "desconocido")"
echo ""
echo "=== Próximos Pasos ==="
echo ""
echo "1. Verifica que MongoDB esté funcionando correctamente:"
echo "   sudo systemctl status mongod"
echo ""
echo "2. Si MongoDB no está funcionando, revisa los logs:"
echo "   sudo journalctl -u mongod -n 50"
echo ""
echo "3. Copia tu archivo JAR:"
echo "   sudo cp tu-api.jar /opt/luchaapp/api/lucha-api-server.jar"
echo "   sudo chown luchaapp:luchaapp /opt/luchaapp/api/lucha-api-server.jar"
echo ""
echo "4. Inicia el servicio:"
echo "   sudo systemctl start luchaapp-api"
echo "   sudo systemctl enable luchaapp-api"
echo ""
echo "5. Scripts helper disponibles en /opt/luchaapp/:"
echo "   - start.sh: Iniciar el servicio API"
echo "   - stop.sh: Detener el servicio API"
echo "   - logs.sh: Ver logs de la API"
echo "   - mongo-connect.sh: Conectar a MongoDB como usuario de aplicación"
echo "   - mongo-admin.sh: Conectar a MongoDB como admin"
echo ""