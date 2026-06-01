# research-automation

## Descrição do projeto

O sistema deve captar arquivos recebidos via Telegram, registrar metadados, armazenar os documentos, extrair texto, 
encaminhar o conteúdo para agentes especializados de análise financeira e devolver resumos e classificações para 
analistas internos.

A base `anthropics/financial-services` foi publicada como um conjunto de plugins e agentes para fluxos de financial 
analysis, equity research, investment banking, private equity e wealth management, podendo ser usada via Cowork ou por 
deploy com Claude Managed Agents API.


## Funcionalidades principais

- Receber relatórios financeiros enviados em chats ou canais monitorados no Telegram.
- Baixar arquivos anexados, como PDF, DOCX e planilhas, para processamento interno.
- Catalogar documentos por banco emissor, data, ativo, setor, tipo de relatório e status de processamento.
- Extrair texto do arquivo e preparar insumos para análise automatizada.
- Acionar agentes especializados como *Market Researcher* e *Earnings Reviewer* para resumir e interpretar os relatórios.
- Salvar análises, tags, riscos, catalysts e recomendações em uma base consultável.
- Disponibilizar busca interna por ativo, instituição, período e tema do relatório.
- Publicar um resumo final para consumo operacional, por exemplo em painel interno ou retorno no próprio Telegram.


## Tecnologias usadas

- Linguagem: Java 21
- Build: Maven
- Dependências: Spring Web, Spring Data JPA, Validation, Lombok, PostgreSQL Driver, H2 (scope test), Spring Boot Actuator


## Estrutura de pacotes do projeto 

```
src/
└── main/
    └── java/
        └── br/com/diegoaugustofp/research_automation
            ├── application/                  ← casos de uso / serviços de aplicação
            │   ├── document/
            │   │   ├── DocumentIngestionService.java
            │   │   ├── DocumentQueryService.java
            │   │   └── dto/
            │   │       ├── DocumentRequestDTO.java
            │   │       └── DocumentResponseDTO.java
            │   ├── analysis/
            │   │   ├── AnalysisOrchestrationService.java
            │   │   └── dto/
            │   │       └── AnalysisResultDTO.java
            │   └── security/
            │       └── UserAccessService.java
            │
            ├── domain/                       ← entidades, enums, regras de domínio
            │   ├── document/
            │   │   ├── Document.java
            │   │   ├── DocumentStatus.java
            │   │   ├── DocumentClassification.java
            │   │   ├── TelegramMessage.java
            │   │   └── DocumentRepository.java
            │   ├── analysis/
            │   │   ├── Analysis.java
            │   │   ├── AnalysisStatus.java
            │   │   └── AnalysisRepository.java
            │   └── user/
            │       ├── User.java
            │       └── UserRole.java
            │
            ├── infrastructure/               ← implementações técnicas externas
            │   ├── persistence/
            │   │   ├── JpaDocumentRepository.java
            │   │   └── JpaAnalysisRepository.java
            │   ├── telegram/
            │   │   ├── TelegramAdapter.java
            │   │   └── TelegramFileDownloader.java
            │   ├── ai/
            │   │   ├── FinancialAnalysisClient.java   ← interface
            │   │   └── AnthropicAnalysisClient.java   ← implementação
            │   ├── storage/
            │   │   └── LocalFileStorageService.java
            │   └── config/
            │       ├── AppConfig.java
            │       └── SecurityConfig.java
            │
            └── interfaces/                   ← entradas da aplicação
                ├── api/
                │   ├── DocumentController.java
                │   ├── AnalysisController.java
                │   └── HealthController.java
                └── scheduler/
                    └── RetryFailedAnalysisJob.java

src/
└── test/
    └── java/
        └── com/empresa/researchautomation/
            ├── application/
            │   ├── document/
            │   │   └── DocumentIngestionServiceTest.java
            │   └── analysis/
            │       └── AnalysisOrchestrationServiceTest.java
            ├── domain/
            │   └── document/
            │       └── DocumentStatusTest.java
            └── interfaces/
                └── api/
                    └── DocumentControllerTest.java
```

## Desenho da solução
A arquitetura em camadas com serviços desacoplados por responsabilidade: ingestão de eventos do Telegram, processamento 
documental, orquestração de análise, persistência e API de consulta.

### Visão de contexto

```mermaid
flowchart LR
TG[Telegram]
USER[Analista Interno]
SYS[Sistema de Análise de Relatórios]
FS[Anthropic Financial Services]
DB[(Banco de Dados)]
ST[(Armazenamento de Arquivos)]

    TG --> SYS
    USER --> SYS
    SYS --> FS
    SYS --> DB
    SYS --> ST
```

### Casos de uso principais

```mermaid
flowchart TD
A[Operador] --> UC1[Receber documento do Telegram]
A --> UC2[Monitorar fila de processamento]
A --> UC3[Reprocessar documento]
B[Analista] --> UC4[Consultar relatórios analisados]
B --> UC5[Ler resumo e insights]
C[Administrador] --> UC6[Gerenciar integrações]
C --> UC7[Gerenciar permissões]
S[Serviço de Análise] --> UC8[Acionar agente financeiro]
```


### Containers da solução

```mermaid
flowchart TB
subgraph Cliente
WEB[Web App Interno]
TELE[Bot Telegram]
end

    subgraph Backend Java
        API[API REST / Application Service]
        ING[Ingestion Module]
        DOC[Document Processing Module]
        ANA[Analysis Orchestrator]
        AUTH[Auth & Access Module]
    end

    subgraph Infraestrutura
        DB[(PostgreSQL)]
        OBJ[(Object Storage)]
        MQ[(Fila / Broker)]
        OBS[Logs e Métricas]
    end

    subgraph IA
        AG[Financial Services Agents]
    end

    TELE --> API
    WEB --> API
    API --> ING
    API --> DOC
    API --> ANA
    API --> AUTH
    ING --> MQ
    DOC --> OBJ
    DOC --> DB
    ANA --> AG
    ANA --> DB
    API --> DB
    API --> OBS
    ANA --> OBS
    ING --> OBS

```


### Componentes internos do backend

```mermaid
flowchart LR
Controller[Controllers]
AppService[Application Services]
Domain[Domain Layer]
Repo[Repositories]
Integration[Integration Clients]
Queue[Async Jobs]

    Controller --> AppService
    AppService --> Domain
    AppService --> Repo
    AppService --> Integration
    AppService --> Queue
```

### Entidades principais do domínio

```mermaid
classDiagram
class Documento {
UUID id
String nomeArquivo
String hash
String origem
LocalDateTime dataRecebimento
StatusDocumento status
}

    class MensagemTelegram {
      Long chatId
      Long messageId
      String sender
      LocalDateTime dataEvento
    }

    class Analise {
      UUID id
      UUID documentoId
      String tipoAnalise
      String resumo
      String status
      LocalDateTime dataAnalise
    }

    class ClassificacaoDocumento {
      String instituicao
      String ticker
      String setor
      String tipoRelatorio
    }

    class Usuario {
      UUID id
      String nome
      Perfil perfil
    }

    Documento --> MensagemTelegram : originadoDe
    Documento --> ClassificacaoDocumento : possui
    Documento --> Analise : gera
    Usuario --> Analise : consulta
```

### Sequência de processamento

```mermaid
sequenceDiagram
participant T as Telegram
participant B as Bot/Adapter
participant A as API Java
participant S as Storage
participant P as Processor
participant G as Agente Financial Services
participant D as Database

    T->>B: Envia mensagem com arquivo
    B->>A: Webhook/update com metadados
    A->>B: Solicita download do arquivo
    B->>S: Armazena documento bruto
    B->>D: Registra documento recebido
    A->>P: Inicia job de extração/classificação
    P->>D: Atualiza status e metadados
    P->>G: Envia conteúdo para análise
    G-->>P: Retorna resumo e insights
    P->>D: Salva análise final
    D-->>A: Documento disponível para consulta
```
### Estados do documento

```mermaid
stateDiagram-v2
[*] --> RECEBIDO
RECEBIDO --> BAIXADO
BAIXADO --> EXTRAIDO
EXTRAIDO --> CLASSIFICADO
CLASSIFICADO --> ANALISE_EM_ANDAMENTO
ANALISE_EM_ANDAMENTO --> ANALISADO
ANALISE_EM_ANDAMENTO --> FALHA
FALHA --> REPROCESSANDO
REPROCESSANDO --> ANALISE_EM_ANDAMENTO
Decisões arquiteturais
```
