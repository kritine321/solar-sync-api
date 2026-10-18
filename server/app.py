import os
from flask import Flask, request, jsonify

app = Flask(__name__)

# Banco de dados simples em memória para teste rápido
data_store = {}

@app.route('/')
def home():
    return "Servidor C2 Ativo", 200

@app.route('/beacon', methods=['POST'])
def beacon():
    content = request.json
    device_id = content.get('device_id', 'unknown')
    data_store[device_id] = content
    return jsonify({"status": "received", "command": "wait"}), 200

@app.route('/admin/list', methods=['GET'])
def list_devices():
    return jsonify(data_store), 200

if __name__ == '__main__':
    port = int(os.environ.get("PORT", 5000))
    app.run(host='0.0.0.0', port=port)
