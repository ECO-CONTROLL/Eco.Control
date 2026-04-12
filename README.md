# 🚰 EcoControll — Backend

Backend do sistema de monitoramento inteligente de cisternas.
Desenvolvido com Ktor + Kotlin para o TCC do curso técnico de Desenvolvimento de Sistemas.

---

## 🏗️ Arquitetura
ESP32 + Sensor Ultrassônico
↓ MQTT
HiveMQ Cloud (Broker)
↓ Subscribe
Ktor Backend (este projeto)
↓ REST API + WebSocket
App Android EcoControll

---

## 🛠️ Tecnologias

- **Kotlin** + **Ktor 3** — servidor backend
- **SQLite** + **Exposed ORM** — banco de dados local
- **HiveMQ MQTT Client** — comunicação IoT
- **JWT** — autenticação
- **WebSocket** — dados em tempo real para o app

---

## ⚙️ Pré-requisitos

- JDK 17 ou superior
- IntelliJ IDEA (Community ou Ultimate)
- Conta gratuita no [HiveMQ Cloud](https://www.hivemq.com/mqtt-cloud-broker/)

---

## 🚀 Como rodar

### 1. Clone o repositório

```bash
git clone https://github.com/ECO-CONTROLL/Eco.Control.git
cd Eco.Control
```

### 2. Configure as credenciais MQTT

Copie o arquivo de exemplo e preencha com suas credenciais:

```bash
cp src/main/kotlin/com/ecocontroll/mqtt/MqttConfig.example.kt \
   src/main/kotlin/com/ecocontroll/mqtt/MqttConfig.kt
```

Edite o `MqttConfig.kt` com os dados do seu cluster HiveMQ:

```kotlin
object MqttConfig {
    const val HOST      = "SEU_CLUSTER.s1.eu.hivemq.cloud"
    const val PORT      = 8883
    const val USER      = "SEU_USUARIO"
    const val PASSWORD  = "SUA_SENHA"
    const val TOPIC     = "cisterna/nivel"
    const val CLIENT_ID = "eco-controll-backend"
}
```

### 3. Rode o servidor

```bash
./gradlew run
```

O servidor sobe em `http://localhost:8080`

---

## 📡 Endpoints da API

Todas as rotas (exceto login) exigem header:
Authorization: Bearer <token>

### Autenticação
| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/api/auth/login` | Login e geração do JWT |

**Body do login:**
```json
{
  "usuario": "admin",
  "senha": "cisterna123"
}
```

### Leituras
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/leituras/ultima` | Última leitura do sensor |
| GET | `/api/leituras` | Últimas 100 leituras |
| GET | `/api/leituras/historico?horas=24` | Histórico agrupado por hora |

### Cisterna
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/cisterna/config` | Configuração atual |
| PUT | `/api/cisterna/config` | Atualiza configuração |

### Alertas
| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/api/alertas` | Todos os alertas |
| GET | `/api/alertas/nao-lidos` | Alertas não lidos |
| PATCH | `/api/alertas/{id}/lido` | Marca alerta como lido |

### WebSocket
ws://localhost:8080/ws/nivel
Recebe leituras em tempo real a cada nova mensagem MQTT.

---

## 📨 Formato MQTT

**Tópico:** `cisterna/nivel`

**Payload esperado:**
```json
{
  "nivelCm": 80.0,
  "timestamp": 1234567890
}
```

---

## 🧪 Testando sem o ESP32

Use o [MQTT Explorer](https://mqtt-explorer.com) para simular o sensor:
Host:     SEU_CLUSTER.s1.eu.hivemq.cloud
Port:     8883
TLS:      ✅ ativado
Username: SEU_USUARIO
Password: SUA_SENHA

Publique no tópico `cisterna/nivel` com o payload acima.

---

## 🔔 Regras de alerta

| Tipo | Condição |
|------|----------|
| `NIVEL_CRITICO` | Nível abaixo de 10% |
| `NIVEL_BAIXO` | Nível abaixo de 30% |
| `NIVEL_CHEIO` | Nível acima de 98% |
| `SENSOR_OFFLINE` | Sem leitura há mais de 5 minutos |

---

## 📁 Estrutura do projeto
src/main/kotlin/com/ecocontroll/
├── Application.kt
├── data/
│   ├── DatabaseFactory.kt
│   ├── repository/
│   └── tables/
├── domain/
│   ├── AlertaService.kt
│   ├── SensorMonitor.kt
│   └── WebSocketBroadcaster.kt
├── models/
├── mqtt/
│   ├── MqttConfig.example.kt  ← versionar
│   └── MqttConfig.kt          ← NÃO versionar (.gitignore)
├── plugins/
└── routes/

---

## 👥 Equipe

Desenvolvido por Vinicius, Samuel, Thiago, Pedro, Icaro, Rafael, Lincoln e Lukas
Curso Técnico de Desenvolvimento de Sistemas — 2024/2025