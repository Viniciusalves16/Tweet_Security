Tweet Security - Desafio

Descrição do projeto:
Este projeto tem como objetivo desenvolver um sistema que implemente as melhores práticas de uso do módulo Spring Security.
O sistema simula um pequeno aplicativo semelhante ao Twitter, permitindo que os usuários publiquem, excluam e visualizem tweets, tudo isso com base em seus níveis de acesso.


Técnologias:
- Spring Boot
- Spring MVC
- Spring Security


Práticas adotadas:
- API REST
- Injeção de dependências
- Tratamentos de respostas de erro
- Configuração de acessos

Desenvolvimento:
Para iniciar o desenvolvimento, é necessário clonar o projeto do GitHub num diretório de sua preferência


mvn clean install:
O comando irá baixar todas as dependências do projeto e criar um diretório target com os artefatos construídos, que incluem o arquivo jar do projeto.
Além disso, serão executados os testes unitários, e se algum falhar, o Maven exibirá essa informação no console.


Configuração:
Para executar o projeto, é necessário utilizar o intellij, para que o mesmo identifique as dependências necessárias para a execução no repositório .m2 do Maven.
Uma vez importado o projeto, será criado um arquivo .classpath que irá informar qual a classe principal para a execução.
