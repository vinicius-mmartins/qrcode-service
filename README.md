# qrcode-service

Serviço responsável pelo cadastro de QRCodes.

## Tecnologias

- Java 17
- Gradle
- Spring Boot 3

## Comandos: como executar o projeto

Para executar o projeto basta utilizar o comando:
```shell
./gradlew bootRun
```

Para rodar todos os testes:
```shell
./gradlew test
```

Para construir o artefato:
```shell
./gradlew clean build
```

## Endpoints

### QRCode Imediato

Cadastrar um QRCode imediato:
```shell
curl --location 'localhost:8080/api/v1/qrcodes' \
--header 'Content-Type: application/json' \
--data '{
    "txid": "c435a648-31ce-4120-ae62-b4fec44f6e68",
    "value": "1.0",
    "status":"OPEN",
    "description":"QRCode de teste"
}''
```

Parâmetros da requisição:
```
tdix: Obrigatório. Identificador único (uuid) do QRCode. Pode ser usado para idempotência. Poderia ter outro nome.
value: Obrigatório. Valor do QRCode.
status: Opcional. Status do QRCode. Valores possíveis no cadastro: OPEN. Default: OPEN.
Todos valores de status: OPEN, PAID, EXPIRED, CANCELED.
description: Opcional. Descrição do QRCode.
```
Parâmetros a serem criados e salvos na base:
```
expiration_date: Data de expiração do QRCode. Calculado de acordo com o vencimento e tempo expiração padrão parametrizado.
created_at: Data de criação do QRCode.
updated_at: Data de atualização do QRCode.
```

#### Sucesso

Http Status: 201

```json
{
  "txid": "c435a648-31ce-4120-ae62-b4fec44f6e68",
  "value": "1.0",
  "description": "QRCode de teste",
  "status": "OPEN"
}
```

#### Erro: respostas esperadas


- Caso QRCode com determinado txid já exista:

Http Status 422

```json
[
    {
        "code": "ALREADY_REGISTERED",
        "message": "QRCode already registered"
    }
]
```

- Validações:

Http Status 400

Campos obrigatórios não informados:
```json
[
    {
        "code": "REQUIRED_FIELD",
        "message": "Field txid is required"
    },
    {
        "code": "REQUIRED_FIELD",
        "message": "Field value is required"
    }
]
```

Valor com formato inválido:
```json
[
    {
        "code": "INVALID_FIELD",
        "message": "Numeric field value has a invalid format. Try value like 123.45"
    }
]
```

Valor informado menor que zero:
```json
[
    {
        "code": "INVALID_FIELD",
        "message": "Numeric field value should be greater then Zero"
    }
]
```

Status informado não permitido:
```json
[
  {
    "code": "INVALID_FIELD",
    "message": "Status doesnt match the expected values"
  }
]
```

### QRCode Imediato com data de vencimento

Cadastrar um QRCode imediato com data de vencimento:
```shell
curl --location 'localhost:8080/api/v1/qrcodes/with-due-date' \
--header 'Content-Type: application/json' \
--data '{
    "txid": "ef6b22b3-e866-4b76-8e83-6877e2d0ad2f",
    "value": "39.90",
    "dueDate": "2025-01-01",
    "status":"OPEN",
    "description":"QRCode de teste 2"
}'
```

Parâmetros da requisição:
```
tdix: Obrigatório. Identificador único (uuid) do QRCode. Pode ser usado para idempotência. Poderia ter outro nome.
value: Obrigatório. Valor do QRCode.
status: Opcional. Status do QRCode. Valores possíveis no cadastro: OPEN. Default: OPEN.
Todos valores de status: OPEN, PAID, EXPIRED, CANCELED.
description: Opcional. Descrição do QRCode.
```
Parâmetros a serem criados e salvos na base:
```
expiration_date: Data de expiração do QRCode. Calculado de acordo com o vencimento e tempo expiração padrão parametrizado.
created_at: Data de criação do QRCode.
updated_at: Data de atualização do QRCode.
```

#### Sucesso

Http Status: 201

```json
{
  "txid": "ef6b22b3-e866-4b76-8e83-6877e2d0ad2f",
  "value": "39.90",
  "dueDate": "2025-01-01",
  "description": "QRCode de teste 2",
  "status": "OPEN"
}
```

#### Erro: respostas esperadas

As validações são as mesmas do QRCode Imediato,
incluindo somente a validação da data de vencimento.

Http Status 400

Campos obrigatórios não informados:
```json
[
  {
    "code": "REQUIRED_FIELD",
    "message": "Field value is required"
  },
  {
    "code": "REQUIRED_FIELD",
    "message": "Field txid is required"
  },
  {
    "code": "REQUIRED_FIELD",
    "message": "Field dueDate is required"
  }
]
```

Data de vencimento com formato inválido:
```json
[
    {
        "code": "INVALID_FIELD",
        "message": "Date field dueDate has a invalid format. Try pattern yyyy-MM-dd"
    }
]
```

Data de vencimento no passado:
```json
[
    {
        "code": "INVALID_FIELD",
        "message": "Date field dueDate should be a future date"
    }
]
```

### Swagger

```
http://localhost:8080/swagger-ui/index.html#/
```

## Orientações sobre o projeto

- Incluir novas mensagens de erro no arquivo `messages.properties` ou reutilizar as existentes onde adequado.
- Para melhor rastreabilidade no *logs* adotamos o uso do recurso `MDC` em conjunto com uma tag de log com identificador único.
Ilustração:
```
05-11-2024 22:17:14.110 [http-nio-8080-exec-2] INFO  c.g.v.q.controller.QRCodeController: QRCODE/TXID=c435a648-31ce-4120-ae62-b4fec44f6e68-- Beginning register QRCode 
05-11-2024 22:17:14.115 [http-nio-8080-exec-2] ERROR c.g.v.q.service.QRCodeServiceImpl: QRCODE/TXID=c435a648-31ce-4120-ae62-b4fec44f6e68-- QRCode already exists with txid: c435a648-31ce-4120-ae62-b4fec44f6e68-- 
05-11-2024 22:17:14.122 [http-nio-8080-exec-2] WARN  o.s.w.s.m.m.a.ExceptionHandlerExceptionResolver:  Resolved [com.github.viniciusmartins.qrcode.exception.UnprocessableEntityException] 
```
- Criar uma interface de controller não é necessário, mas para não poluir o controller segregamos todas as anotações do swagger
em interfaces.
