# Contributing

Obrigado por contribuir com este projeto! Este documento descreve as convenções adotadas para manter o histórico do repositório limpo e rastreável.

---

## Índice

- [Convenção de Branches](#convenção-de-branches)
- [Convenção de Commits](#convenção-de-commits)
- [Fluxo de Trabalho](#fluxo-de-trabalho)
- [Pull Requests](#pull-requests)

---

## Convenção de Branches

As branches devem seguir o padrão:

```
<tipo>/<identificador-descritivo>
```

### Tipos

| Tipo | Quando usar |
|------|-------------|
| `feature` | Nova funcionalidade ou história de usuário |
| `fix` | Correção de bug |
| `chore` | Configuração, infraestrutura, tarefas sem impacto no produto |
| `docs` | Documentação |
| `refactor` | Refatoração sem alteração de comportamento |
| `test` | Adição ou ajuste de testes |

### Exemplos

```
feature/US-01-telegram-bot
fix/US-04-duplicate-detection
chore/setup-docker
docs/swagger-endpoints
refactor/document-service
test/document-ingestion-service
```

### Regras

- Use **kebab-case** (letras minúsculas separadas por hífen)
- Inclua o identificador da história (`US-01`, `US-04`) quando existir
- Seja descritivo, mas conciso — evite nomes genéricos como `fix/bug` ou `feature/changes`
- Nunca faça commits diretamente em `main` ou `develop`

---

## Convenção de Commits

Seguimos o padrão [Conventional Commits](https://www.conventionalcommits.org/):

```
<tipo>: <descrição no imperativo, em minúsculas>
```

### Tipos

| Tipo | Quando usar |
|------|-------------|
| `feat` | Nova funcionalidade |
| `fix` | Correção de bug |
| `refactor` | Refatoração sem mudança de comportamento |
| `test` | Adição ou correção de testes |
| `docs` | Documentação |
| `chore` | Tarefas de infraestrutura, build, configuração |
| `style` | Formatação, espaçamento (sem impacto funcional) |
| `perf` | Melhoria de performance |
| `ci` | Alterações em pipelines de CI/CD |

### Exemplos

```
feat: adicionar endpoint POST /documents
fix: corrigir detecção de duplicidade por hash
refactor: extrair lógica de status para DocumentStatusValidator
test: adicionar testes unitários para DocumentIngestionService
docs: documentar endpoint POST /documents no Swagger
chore: configurar Docker Compose
```

### Regras

- Use o **imperativo** na descrição: "adicionar" e não "adicionado" ou "adicionando"
- **Letras minúsculas** na descrição
- **Sem ponto final**
- Máximo de **72 caracteres** na linha do título
- Referencie issues ou histórias quando aplicável:
  ```
  feat: adicionar endpoint POST /documents

  Closes #42
  ```

---

## Fluxo de Trabalho

1. Crie uma branch a partir de `develop`:
   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/US-01-telegram-bot
   ```

2. Faça commits pequenos e coesos ao longo do desenvolvimento.

3. Mantenha a branch atualizada com `develop` via rebase:
   ```bash
   git fetch origin
   git rebase origin/develop
   ```

4. Ao finalizar, abra um Pull Request para `develop`.

---

## Pull Requests

- Preencha o template de PR com descrição, tipo de mudança e checklist
- Vincule a issue ou história de usuário relacionada
- O PR só pode ser mergeado após aprovação de pelo menos **1 revisor**
- Prefira **Squash and Merge** para manter o histórico linear