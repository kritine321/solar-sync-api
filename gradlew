#!/bin/sh
#
# Copyright James Duncan O' reigny & Ben Manes. All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing this file.

set -e
set -x

PROJECT_DIR="$( pwd )"
GRADLE_USER_HOME="${GRADLE_USER_HOME:-$HOME/.gradle}"
#
# Garante que o gradlew tenha permissão de execução e existe
if [ -f "${PROJECT_DIR}/gradlew" ]; then
  chmod +x "${PROJECT_DIR}/gradlew"
  GRADLE_CMD="${PROJECT_DIR}/gradlew"
else
  echo "Erro: gradlew não encontrado. Verifique se ele foi commitado no repositório."
  exit 1
fi

# Executa o build com a flag --no-daemon para evitar problemas de memória em CI
$GRADLE_CMD assembleDebug --no-daemon --stacktrace
