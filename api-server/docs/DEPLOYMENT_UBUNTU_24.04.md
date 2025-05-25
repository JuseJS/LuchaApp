# Guía Completa de Despliegue en Ubuntu Server 24.04
## LuchaApp API Server

Esta guía detalla el proceso completo de instalación y configuración del API Server de LuchaApp en Ubuntu Server 24.04 LTS desde una instalación limpia.

## 📋 Requisitos del Sistema

- **Ubuntu Server 24.04 LTS** (instalación limpia)
- **Mínimo 2GB RAM** (recomendado 4GB)
- **20GB de espacio en disco**
- **Conexión a Internet**
- **Acceso SSH al servidor**

## 🌐 Configuración de Red y Dominio

### Dominio
- **Dominio público**: `guanxemc.asuscomm.com`
- **Puerto API**: `8080`
- **Puerto MongoDB**: `27017` (solo acceso local)

### Configuración del Router ASUS

Debes redireccionar los siguientes puertos en tu router:

1. **Puerto SSH (opcional pero recomendado)**:
   - Puerto externo: `2222`
   - Puerto interno: `22`
   - IP del servidor: `[IP_LOCAL_DEL_SERVIDOR]`

2. **Puerto API**:
   - Puerto externo: `8080`
   - Puerto interno: `8080`
   - IP del servidor: `[IP_LOCAL_DEL_SERVIDOR]`

3. **Configuración en el router ASUS**:
   ```
   Administración > Sistema > WAN > DDNS
   - Habilitar DDNS: Sí
   - Servidor: WWW.ASUS.COM
   - Nombre de host: guanxemc.asuscomm.com
   
   Administración > Sistema > WAN > Puerto Virtual / Port Forwarding
   - Añadir las reglas mencionadas arriba
   ```

## 📁 Archivos Necesarios

Antes de comenzar, necesitarás transferir los siguientes archivos al servidor:

1. **Scripts de instalación**:
   - `/api-server/scripts/setup-ubuntu.sh`
   - `/api-server/scripts/init-mongodb.js`

2. **Archivo JAR de la aplicación**:
   - `/api-server/build/libs/lucha-api-server-1.0.0.jar`

3. **Archivo de configuración**:
   - `.env` (basado en `.env.production.template`)

## 🚀 Proceso de Instalación

### Paso 1: Preparación Inicial

1. Conectarse al servidor:
```bash
ssh usuario@[IP_DEL_SERVIDOR]
```

2. Actualizar el sistema:
```bash
sudo apt update && sudo apt upgrade -y
```

3. Crear directorio temporal para la instalación:
```bash
mkdir ~/lucha-install
cd ~/lucha-install
```

### Paso 2: Transferir Archivos al Servidor

Desde tu máquina local:

```bash
# Transferir scripts
scp api-server/scripts/setup-ubuntu.sh usuario@[IP_DEL_SERVIDOR]:~/lucha-install/
scp api-server/scripts/init-mongodb.js usuario@[IP_DEL_SERVIDOR]:~/lucha-install/

# Transferir archivo JAR (primero compilarlo si no existe)
cd api-server
./gradlew clean shadowJar
scp build/libs/lucha-api-server-1.0.0.jar usuario@[IP_DEL_SERVIDOR]:~/lucha-install/
```

### Paso 3: Crear Archivo de Configuración

En el servidor, crear el archivo `.env`:

```bash
nano ~/lucha-install/.env
```

Contenido del archivo (ajustar las contraseñas):

```env
# ========================================
# CONFIGURACIÓN DE SERVIDOR
# ========================================
API_HOST=0.0.0.0
PORT=8080

# ========================================
# CONFIGURACIÓN MONGODB
# ========================================
MONGO_HOST=localhost
MONGO_PORT=27017
DATABASE_NAME=lucha_canaria

# Usuario root de MongoDB
MONGO_ROOT_USERNAME=admin
MONGO_ROOT_PASSWORD=TuContraseñaSeguraAqui123!

# Usuario de la aplicación
MONGO_APP_USERNAME=lucha_canaria_app
MONGO_APP_PASSWORD=OtraContraseñaSegura456!

# URI de conexión
MONGODB_URI=mongodb://lucha_canaria_app:OtraContraseñaSegura456!@localhost:27017/lucha_canaria?authSource=lucha_canaria

# ========================================
# CONFIGURACIÓN JWT
# ========================================
JWT_SECRET=TuSecretoJWTMuySeguro789!MinimoDe32Caracteres

# ========================================
# CONFIGURACIÓN CORS
# ========================================
CORS_ALLOWED_HOSTS=http://localhost:3000,http://guanxemc.asuscomm.com:8080,https://guanxemc.asuscomm.com
```

### Paso 4: Ejecutar Script de Instalación

1. Hacer el script ejecutable:
```bash
chmod +x ~/lucha-install/setup-ubuntu.sh
```

2. Exportar variables de entorno necesarias:
```bash
source ~/lucha-install/.env
export MONGO_ROOT_PASSWORD
export MONGO_APP_USERNAME
export MONGO_APP_PASSWORD
```

3. Ejecutar el script de instalación:
```bash
cd ~/lucha-install
sudo -E ./setup-ubuntu.sh
```

### Paso 5: Inicializar Base de Datos

Una vez MongoDB esté instalado y funcionando:

```bash
# Verificar que MongoDB está funcionando
sudo systemctl status mongod

# Inicializar la base de datos
mongosh < init-mongodb.js
```

### Paso 6: Configurar la Aplicación

1. Copiar archivos a su ubicación final:
```bash
sudo cp ~/lucha-install/.env /opt/luchaapp/.env
sudo cp ~/lucha-install/lucha-api-server-1.0.0.jar /opt/luchaapp/api/lucha-api-server.jar
sudo chown -R luchaapp:luchaapp /opt/luchaapp
```

2. Iniciar el servicio:
```bash
sudo systemctl start luchaapp-api
sudo systemctl enable luchaapp-api
```

3. Verificar que está funcionando:
```bash
sudo systemctl status luchaapp-api
curl http://localhost:8080/api/v1/health
```

### Paso 7: Configuración de Firewall

El script ya configura UFW, pero verificar:

```bash
sudo ufw status
```

Debe mostrar:
```
Status: active

To                         Action      From
--                         ------      ----
22/tcp                     ALLOW       Anywhere
8080/tcp                   ALLOW       Anywhere
```

## 🔧 Comandos Útiles

### Gestión del Servicio

```bash
# Iniciar servicio
sudo systemctl start luchaapp-api

# Detener servicio
sudo systemctl stop luchaapp-api

# Reiniciar servicio
sudo systemctl restart luchaapp-api

# Ver estado
sudo systemctl status luchaapp-api

# Ver logs en tiempo real
sudo journalctl -u luchaapp-api -f

# Ver últimas 100 líneas de logs
sudo journalctl -u luchaapp-api -n 100
```

### Gestión de MongoDB

```bash
# Estado de MongoDB
sudo systemctl status mongod

# Conectar a MongoDB
mongosh -u admin -p [MONGO_ROOT_PASSWORD] --authenticationDatabase admin

# Backup de la base de datos
mongodump -u backup_user -p [BACKUP_PASSWORD] --authenticationDatabase admin --db lucha_canaria --out /backup/$(date +%Y%m%d)

# Restaurar backup
mongorestore -u admin -p [MONGO_ROOT_PASSWORD] --authenticationDatabase admin --db lucha_canaria /backup/20240115/lucha_canaria
```

## 🔍 Verificación de la Instalación

1. **Verificar acceso local**:
```bash
curl http://localhost:8080/api/v1/health
```

2. **Verificar acceso externo** (desde otra máquina):
```bash
curl http://guanxemc.asuscomm.com:8080/api/v1/health
```

3. **Verificar logs**:
```bash
tail -f /var/log/luchaapp/api.log
```

## 🛠️ Solución de Problemas

### El servicio no inicia

1. Verificar logs:
```bash
sudo journalctl -u luchaapp-api -n 50
```

2. Verificar permisos:
```bash
ls -la /opt/luchaapp/
ls -la /var/log/luchaapp/
```

3. Verificar archivo .env:
```bash
sudo cat /opt/luchaapp/.env
```

### MongoDB no conecta

1. Verificar que MongoDB está corriendo:
```bash
sudo systemctl status mongod
```

2. Verificar configuración de MongoDB:
```bash
sudo cat /etc/mongod.conf
```

3. Probar conexión manual:
```bash
mongosh -u lucha_canaria_app -p [PASSWORD] --authenticationDatabase lucha_canaria
```

### No se puede acceder desde fuera

1. Verificar firewall:
```bash
sudo ufw status verbose
```

2. Verificar que la aplicación escucha en todas las interfaces:
```bash
sudo netstat -tlnp | grep 8080
```

3. Verificar redirección de puertos en el router

## 📝 Mantenimiento

### Actualización de la Aplicación

1. Compilar nueva versión:
```bash
# En tu máquina local
cd api-server
./gradlew clean shadowJar
```

2. Transferir al servidor:
```bash
scp build/libs/lucha-api-server-1.0.0.jar usuario@[IP_SERVIDOR]:/tmp/
```

3. Actualizar en el servidor:
```bash
sudo systemctl stop luchaapp-api
sudo cp /tmp/lucha-api-server-1.0.0.jar /opt/luchaapp/api/lucha-api-server.jar
sudo chown luchaapp:luchaapp /opt/luchaapp/api/lucha-api-server.jar
sudo systemctl start luchaapp-api
```

### Backup Automático

El script configura rotación de logs automática. Para backups de MongoDB, crear un cron job:

```bash
sudo crontab -e
```

Añadir:
```cron
0 2 * * * mongodump -u backup_user -p [BACKUP_PASSWORD] --authenticationDatabase admin --db lucha_canaria --out /backup/$(date +\%Y\%m\%d) && find /backup -mtime +30 -delete
```

## 🔐 Seguridad

1. **Cambiar todas las contraseñas** del archivo `.env`
2. **Configurar SSH con claves** en lugar de contraseñas
3. **Mantener el sistema actualizado**:
   ```bash
   sudo apt update && sudo apt upgrade
   ```
4. **Monitorear logs regularmente**
5. **Configurar fail2ban** para protección adicional:
   ```bash
   sudo apt install fail2ban
   ```

## 📞 Soporte

Si encuentras problemas durante la instalación:

1. Revisa los logs del sistema
2. Verifica que todos los servicios están corriendo
3. Asegúrate de que las contraseñas no contienen caracteres problemáticos
4. Verifica la configuración de red y firewall