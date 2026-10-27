# Prueba NACK -> DLQ. Ejecutar desde la raiz del proyecto (mini-pedidos).
$ErrorActionPreference = "Stop"

$cred = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("guest:guest"))
$headers = @{ Authorization = "Basic $cred" }
$urlDlq = "http://localhost:15672/api/queues/%2F/pedidos.dlq"

$antes = (Invoke-RestMethod -Uri $urlDlq -Headers $headers).messages
Write-Host "Mensajes en pedidos.dlq ANTES: $antes"

curl.exe -X POST "http://localhost:8080/pedidos" -H "Content-Type: application/json" --data-binary "@pedido-nack.json"

Start-Sleep -Seconds 7   # el panel de RabbitMQ actualiza sus numeros cada ~5 s

$despues = (Invoke-RestMethod -Uri $urlDlq -Headers $headers).messages
Write-Host "Mensajes en pedidos.dlq DESPUES: $despues"

if ($despues -gt $antes) {
    Write-Host "OK: el pedido invalido llego a la DLQ" -ForegroundColor Green
} else {
    Write-Host "FALLO: la DLQ no recibio el mensaje" -ForegroundColor Red
    exit 1
}
