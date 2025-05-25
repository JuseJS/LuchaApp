#!/bin/bash

# Script para verificar la configuración de variables de entorno

echo "=== Verificación de Configuración de LuchaApp API ==="
echo

# Función para verificar una variable
check_var() {
    local var_name=$1
    local var_value="${!var_name}"
    local is_required=$2
    
    if [ -n "$var_value" ]; then
        # Ocultar valores sensibles
        if [[ "$var_name" == *"PASSWORD"* ]] || [[ "$var_name" == "JWT_SECRET" ]] || [[ "$var_name" == "MONGODB_URI" ]]; then
            echo "✓ $var_name = ****** (configurado)"
        else
            echo "✓ $var_name = $var_value"
        fi
    else
        if [ "$is_required" == "required" ]; then
            echo "✗ $var_name = (NO CONFIGURADO - REQUERIDO)"
        else
            echo "○ $var_name = (no configurado - opcional)"
        fi
    fi
}

# Cargar el archivo .env si existe
if [ -n "$DOTENV_PATH" ] && [ -f "$DOTENV_PATH" ]; then
    echo "Cargando variables desde: $DOTENV_PATH"
    set -a
    source "$DOTENV_PATH"
    set +a
elif [ -f "/opt/luchaapp/.env" ]; then
    echo "Cargando variables desde: /opt/luchaapp/.env"
    set -a
    source "/opt/luchaapp/.env"
    set +a
elif [ -f "../.env" ]; then
    echo "Cargando variables desde: ../.env"
    set -a
    source "../.env"
    set +a
else
    echo "No se encontró archivo .env, usando solo variables de entorno del sistema"
fi

echo
echo "=== Configuración del Servidor ==="
check_var "API_HOST" "optional"
check_var "PORT" "optional"

echo
echo "=== Configuración de MongoDB ==="
check_var "MONGO_HOST" "optional"
check_var "MONGO_PORT" "optional"
check_var "DATABASE_NAME" "required"
check_var "MONGODB_URI" "required"
check_var "MONGO_APP_USERNAME" "optional"
check_var "MONGO_APP_PASSWORD" "optional"

echo
echo "=== Configuración JWT ==="
check_var "JWT_SECRET" "required"
check_var "JWT_ISSUER" "optional"
check_var "JWT_AUDIENCE" "optional"
check_var "JWT_EXPIRATION_HOURS" "optional"

echo
echo "=== Configuración CORS ==="
check_var "CORS_ALLOWED_HOSTS" "optional"

echo
echo "=== Verificación de Conectividad ==="

# Verificar MongoDB
if [ -n "$MONGODB_URI" ]; then
    echo -n "Probando conexión a MongoDB... "
    # Extraer host y puerto de la URI
    if [[ "$MONGODB_URI" =~ mongodb://([^:]+:)?([^@]+@)?([^:/]+)(:([0-9]+))? ]]; then
        MONGO_CHECK_HOST="${BASH_REMATCH[3]}"
        MONGO_CHECK_PORT="${BASH_REMATCH[5]:-27017}"
        
        if nc -z -v -w5 "$MONGO_CHECK_HOST" "$MONGO_CHECK_PORT" 2>/dev/null; then
            echo "✓ MongoDB accesible en $MONGO_CHECK_HOST:$MONGO_CHECK_PORT"
        else
            echo "✗ No se puede conectar a MongoDB en $MONGO_CHECK_HOST:$MONGO_CHECK_PORT"
        fi
    else
        echo "✗ No se pudo parsear la URI de MongoDB"
    fi
else
    echo "✗ MONGODB_URI no configurado"
fi

echo
echo "=== Resumen ==="
echo "Para ejecutar la API, asegúrate de que todas las variables requeridas estén configuradas."
echo "Puedes copiar .env.example a .env y modificar los valores según tu configuración."