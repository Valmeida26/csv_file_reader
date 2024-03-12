# csv_file_reader
An api that opens a csv file, processes the information in it and populates the database with the information contained in it. IMPORTANT, the api is integrated with spring security and it is necessary to read the README file to understand how users are registered and logged in.

# API BackEnd Java 

Esta é uma API BackEnd escrita em Java que fornece endpoints para ler um arquivo .CSV e popular o banco de dados com as informações desse arquivo .CSV.
Destaca-se por integrar nela os serviços:
     Spring Security
     JWT
     Lombok
     JSON
     		
## Instalação e Configuração

1. Pré-requisitos: 
Antes de começar, certifique-se de ter os seguintes sistemas instalados em sua máquina:
    - [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
    - [Maven](https://maven.apache.org/download.cgi)
    - [Git](https://git-scm.com/downloads)
    - [Banco de Dados - Mysql](https://www.mysql.com/downloads/)
    - [Postman](https://www.postman.com/)

2. Clonar o Repositório: 
Clone este repositório para sua máquina local usando o seguinte comando:
    git clone https://github.com/Valmeida26/csv_file_reader.git

3. Configuração do Aplicativo: 

Edite o arquivo `application.properties` para configurar a conexão com o banco de dados e outras configurações específicas da sua aplicação, 
de preferência conforme o exemplo: 

spring.output.ansi.enabled=ALWAYS
#database config
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/NomeDoSeuBanco?createDatabaseIfNotExist=true
spring.datasource.username=SEU USUÁRIO DO BANCO DE DADOS
spring.datasource.password=SUA SENHA DO BANCO DE DADOS
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.error.include-exception = false
#flyway config
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
chave secreta para criptografar o token
jwt.secret=yg0y7v9uSsN06Hyr1uMb0FNH6BoXZWPIIbiyHkWLGgjIpB2MrTLiCrUmDGEw5Bn
jwt.expiration=86400000


4. Compilar e Executar: 

Execute o seguinte comando para compilar e iniciar a aplicação:
    mvn spring-boot:run

## Uso da API

A API fornece os seguintes endpoints:

Endpoints de gerenciamento de usuários do banco:

Endpoint 1:

Endpoint destinado a cadastrar usuários na aplicação, por padrão todo usuário criado no banco tem permissões de editar apenas o próprio usuário e permissões de editar e olhar os dados dos clientes do banco de dados populados a partir do arquivo CSV. Caso queira ter permissões de administrador sugiro que vá no seu banco de dados, na tabela (user_profile) e altere o valor na coluna (profile) de 2 para 1, assim você obterá acesso de administrador no sistema.    
    
- Método HTTP: `POST`
   
 - URL: /user
   
 - Exemplo de uso:
        
No postman, em Body - Raw, use esse modelo de texto: 
	
{
    "username": "seuNome",
    "password": "suaSenha"
}

    - Result:
        - Será retornado um status code 201 CREATED

Endpoint 2: 

      Endpoint destinado a executar o login no sistema

    - Método HTTP: `POST`

    - URL: /login

    - Exemplo de uso:

        No postman, em Body - Raw, use esse modelo de texto: 

{
    "username": "seuNome",
    "password": "suaSenha"
}

    - Result:
        - Será retornado um status code 200 OK, após o retorno vá em Headers no mesmo local que é    retornado o status code, vá em Authorization - copie o código com nome Bearer ex: 
        Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ2aW5pY2l1cyIsImV4cCI6MTcxMDI4NDY1MH0.LqY27Y2hLISpKU63iGcbea_OwRqxU_zoee1SE-3Q9Z4PZdi9ZF6D1CHLPwyupG1f

Endpoint 3: 

      Endpoint destinado a atualizar os dados do usuário

    - Método HTTP: `PUT`

    - URL: /user/{id} 

    - Exemplo de uso:

	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer copiado no momento do login, após isso no postman, em Body - Raw, use esse modelo de texto: 

{
    "password": "suaNovaSenha"
}

Nesse sistema apenas é permitido atualizar a senha de usuários. 

    - Result:
        - Será retornado um status code 204 NO CONTENT

Endpoint 4: 

      Endpoint destinado a deletar o usuário do banco de dados

    - Método HTTP: `DELETE`

    - URL: /user/{id} 

    - Exemplo de uso:
	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer copiado no momento do login, após isso basta enviar a requisição: 

    - Result:
        - Será retornado um status code 204 NO CONTENT

Endpoint de clientes do banco populados a partir de um arquivo CSV:

Endpoint 1: 

      Endpoint destinado a popular o banco de dados com informações contidas no arquivo .CSV

    - Método HTTP: `POST`

    - URL: /persons/upload

    - Exemplo de uso:

	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer copiado no momento do login, após isso no postman em Body - form-data vá em key e selecione a opção (File), escreva file no campo key e em (Value) busque o arquivo .CSV no seu aparelho, após isso basta fazer a requisição. 

    - Result:
        - Será retornado um status code 200 OK e a mensagem: 
           CSV processado e dados salvos no banco de dados.

Endpoint 2: 

      Endpoint destinado a trazer todos os dados no arquivo CSV em ordem alfabética

    - Método HTTP: `GET`

    - URL: /persons/upload

    - Exemplo de uso:

	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer copiado no momento do login, após isso no postman em Body - form-data vá em key e selecione a opção (File), escreva file no campo key e em (Value) busque o arquivo .CSV no seu aparelho, após isso basta fazer a requisição. 

    - Result:
        - Será retornado um status code 200 OK e a mensagem com uma lista com os dados do arquivo como no ex:
	" name: Abba | lastName: Dooly | age: 31 | gender: Male | e-mail: adooly8r@technorati.com | ip: 45.166.22.45 | birthDate: 11/03/1993"

Endpoint 3: 

      Endpoint destinado a retornar o número de clientes com o sexo masculino e feminino e a média de idade de ambos

    - Método HTTP: `GET`

    - URL: /upload/genders-average

    - Exemplo de uso:
	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer copiado no momento do login, após isso no postman em Body - form-data vá em key e selecione a opção (File), escreva file no campo key e em (Value) busque o arquivo .CSV no seu aparelho, após isso basta fazer a requisição. 

    - Result:
        - Será retornado um status code 200 OK e a mensagem com o resultado da requisição como no ex:
	Número de Homens: INFORMAÇÃO Média de idade masculina: MÉDIA
	Número de Mulheres: INFORMAÇÃO Média de idade feminina: MÉDIA

Endpoint 4: 

      Endpoint destinado a fazer o download do conteúdo dos dados no banco de dados, gerando um novo arquivo CSV

    - Método HTTP: `GET`

    - URL: /persons/download

    - Exemplo de uso:
	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer copiado no momento do login, após isso no postman em Body - form-data vá em key e selecione a opção (File), escreva file no campo key e em (Value) busque o arquivo .CSV no seu aparelho, após isso basta fazer a requisição. 

    - Result:
        - Será retornado um status code 200 OK e a mensagem com o resultado da requisição como no ex:
	1,Abba,Dooly,31,adooly8r@technorati.com,Male,45.166.22.45,11/03/1993
	2,Abbie,McDavid,40,amcdavid4z@wufoo.com,Female,252.101.153.189,11/03/1984...

Endpoint 5: 

      Endpoint destinado a retornar uma lista com todos os dados dos clientes no banco

    - Método HTTP: `GET`

    - URL: /persons/All

    - Exemplo de uso:
	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer  copiado no momento do login, após isso
       	basta fazer a requisição. 

    - Result:
        - Será retornado um status code 200 OK e a mensagem com o resultado da requisição como no ex:

	[
    {
        "id": 1,
        "Nome": "Abba",
        "UltimoNome": "Dooly",
        "Email": "adooly8r@technorati.com",
        "Sexo": "Male",
        "IpAcesso": "45.166.22.45",
        "Idade": 31,
        "Nascimento": "11/03/1993"
    },
	]

Endpoint 6: 

      Endpoint destinado a retornar os dados do usuário após informar a id

    - Método HTTP: `GET`

    - URL: /persons/{id}

    - Exemplo de uso:
	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer copiado no momento do login, após isso basta fazer a requisição. 

    - Result:
        - Será retornado um status code 200 OK e a mensagem com o resultado da requisição como no ex:

	{
    "id": 1,
    "Nome": "Abba",
    "UltimoNome": "Dooly",
    "Email": "adooly8r@technorati.com",
    "Sexo": "Male",
    "IpAcesso": "45.166.22.45",
    "Idade": 31,
    "Nascimento": "11/03/1993"
	}

Endpoint 7: 

      Endpoint destinado a deletar um cliente no banco de dados

    - Método HTTP: `DELETE`

    - URL: /persons/{id}

    - Exemplo de uso:
	Primeiro vá em Headers no postman, em KEY escreva Authorization e em VALUE cole o código Bearer copiado no momento do login, após isso basta fazer a requisição. 

    - Result:
        - Será retornado um status code 204 NO CONTENT

## Contribuição

Sinta-se à vontade para contribuir com melhorias ou correções de bugs. Para contribuir:

1. Faça um fork deste repositório.
2. Crie uma branch com a sua feature: `git checkout -b feature-nova`.
3. Commit suas mudanças: `git commit -m 'Adicionar nova feature'`.
4. Envie para a branch: `git push origin feature-nova`.
