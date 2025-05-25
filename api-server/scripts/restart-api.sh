#!/bin/bash

# Script para reconstruir y reiniciar la API

echo "🛑 Deteniendo API actual..."
pkill -f "luchaapp-api-all.jar" || true

echo "🔧 Construyendo API..."
cd /Volumes/MacSecundario/LuchaApp/api-server
./gradlew clean shadowJar

if [ $? -ne 0 ]; then
    echo "❌ Error al construir la API"
    exit 1
fi

echo "✅ API construida exitosamente"

echo "🚀 Iniciando API..."
java -jar build/libs/luchaapp-api-all.jar &

echo "⏳ Esperando a que la API inicie..."
sleep 5

# Verificar si la API está corriendo
if curl -s http://localhost:8080/api/v1/health > /dev/null; then
    echo "✅ API iniciada correctamente en http://localhost:8080"
else
    echo "❌ Error: La API no responde"
fi