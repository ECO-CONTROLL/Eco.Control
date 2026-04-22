# 🚰 EcoControll — Backend

Backend do sistema de monitoramento inteligente de cisternas.  
Desenvolvido com Ktor + Kotlin para o TCC do curso técnico de Desenvolvimento de Sistemas (2025/2026).


# 🏗️ Arquitetura do Sistema

    Hardware: ESP32 + Sensor Ultrassônico detectam o nível.

    Protocolo: Envio via MQTT para o broker HiveMQ Cloud.

    Backend: Este servidor (Ktor) consome os dados, armazena no SQLite e autentica usuários via JWT.

    Frontend: App Android recebe dados em tempo real via WebSockets.

# 🚀 Como Executar

1. Para clonar o Repositório Primeiro, baixe o projeto para sua máquina:
```
git clone https://github.com/ECO-CONTROLL/Eco.Control.git  
cd Eco.Control  
```
2. Configurar o Ambiente (.env):

Dentro da pasta raiz do projeto, crie um arquivo chamado .env e preencha com suas credenciais:
Snippet de código
```
//Configurações do JWT
JWT_SECRET=coloque_uma_chave_longa_e_aleatoria
JWT_ISSUER=ecocontroll-backend

//Configurações do HiveMQ (MQTT)
MQTT_HOST=seu_cluster.s1.eu.hivemq.cloud
MQTT_PORT=8883
MQTT_USER=seu_usuario
MQTT_PASSWORD=sua_senha
MQTT_TOPIC=cisterna/nivel
MQTT_CLIENT_ID=eco-controll-backend
```
3. Configurar o IntelliJ (Plugin EnvFile)

* Para o projeto ler o arquivo .env que você acabou de criar, siga estes passos:

* Vá em Settings > Plugins e instale o plugin EnvFile.

* No topo do IntelliJ, clique em Edit Configurations (ao lado do botão Play).

* Selecione a sua aplicação e clique na aba EnvFile.

* Marque a caixa Enable EnvFile.

* Clique no ícone de +, selecione .env file e escolha o arquivo .env que você criou no passo 2.

4. Rodar o Servidor

Agora é só dar o Play no IntelliJ ou usar o terminal:

```
./gradlew run  
```
O servidor estará disponível em: http://seuip:8080

# Como Executar o back end + front end

1. Realize a instalação do back end no intellij e do front end no Android Studio

2. Após isso, execute o comando:
```
./gradlew run  
```

3. Abra o app no celular, obrigatoriamente na mesma rede do computador e digite o usuario e senha de teste:
```
admin
cisterna123 
```

4. Feche o app e abra novamente e veja ele abrir automaticamente pois verificou seu ultimo login!

## 📡 Documentação da API

### Autenticação
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `POST` | `/api/auth/login` | Login e geração do token JWT |

> **Nota:** Header necessário para as demais rotas: `Authorization: Bearer <token>`

### Monitoramento
| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/leituras/ultima` | Retorna o estado atual do sensor |
| `GET` | `/api/leituras` | Lista as últimas 100 leituras |
| `WS` | `/ws/nivel` | Canal WebSocket para dados em tempo real |  

Caso não tenha o ESP32 em mãos, use o MQTT Explorer:

1.Conecte ao seu broker HiveMQ usando as mesmas credenciais do .env.
2.Publique no tópico cisterna/nivel o seguinte JSON:
```
{
  "nivelCm": 85.5,
  "timestamp": 1713542384
}
```

# 📁 Organização do Projeto


src/main/kotlin/com/ecocontroll/  
├── data/           # Banco de Dados (Exposed + SQLite)  
├── domain/         # Regras de Negócio e Serviços (Alertas, Sensores)  
├── mqtt/           # Cliente MQTT e Configurações (Lê do .env)  
├── plugins/        # Configurações do Ktor (Auth, Serialization)  
└── routes/         # Endpoints da API REST e WebSockets

# 👥 Equipe
- Vinicius Machado  
- Pedro Caldas  
