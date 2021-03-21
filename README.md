# Desafio Java SpringBoot

### Endpoints

Devem ser disponibilizados os seguintes endpoints para operação do Catalogo de produtos.


| Verbo HTTP  |  Resource path    |           Descrição           |
|-------------|:-----------------:|------------------------------:|
| POST        |  /products        |   Criação de um produto       |
| PUT         |  /products/{id}   |   Atualização de um produto   |
| GET         |  /products/{id}   |   Busca de um produto por ID  |
| GET         |  /products        |   Lista de produtos           |
| GET         |  /products/search |   Lista de produtos filtrados |
| DELETE      |  /products/{id}   |   Deleção de um produto       |

### Executando

Atualmente o microserviço utiliza banco de dados em memória, H2, além disso alguns dados já estão inseridos, para facilar o uso da API e testes.

Para executar o modo mais fácil é utilizando uma IDE com suporte ao Build do SpringBoot via Maven, no entanto o comando também irá executar o servidor e carregar os dados que já estão prontos para serem inseridos.

```bash
mvn spring-boot:run
```

Para executar o comando é necessário que estejam condfiguradas as váriáveis refentes ao Maven e ao JDK.
