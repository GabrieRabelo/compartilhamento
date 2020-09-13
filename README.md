# Compartilhamento

Repositório com código fonte do projeto Compartilhamento(TownSQ)

## Softwares / runtimes necessários
* JDK 11 
* Docker e Docker-Compose
* Git
* Como editor de código, recomenda-se o uso do IntelliJ (pode-se usar o VSCode também)

Se você estiver usando o IntelliJ, recomenda-se instalar o plugin Lombok. Caso use o VSCode, instale os seguintes plugins.

* vscode-lombok
* Java Extension Pack
* Spring Boot Tools
* thymeleaf

## Processo de instalação

#### Clonar o projeto:

Execute o comando a seguir em um prompt de comand / terminal dentro de uma pasta de sua escolha (recomenda-se o uso da pasta $HOME).

`git clone http://tools.ages.pucrs.br/compartilhamento/compartilhamento.git`

#### Executar o projeto

Em um primeiro momento, abra o terminal da raíz do projeto e rode o comando

`./mvnw package`

Se você preferir, abra o diretório do projeto no IntelliJ e aguarde as dependências do Maven e o classpath ser atualizado. Isso poder demorar alguns bons minutos.

Após a instalação das dependências e plugins do Maven, vamos subir os containers do Docker. Para isso, novamente na raiz do projeto, execute o seguinte comando

`docker-compose up -d`

Com isso, podemos rodar as migrações do banco de dados e executar a aplicação Spring Boot.

Na raíz do projeto, execute o comando

`./mvnw liquibase:update`

Em seguida, para executar a aplicação, rode o seguinte comando

`./mvnw spring-boot:start`

No IntelliJ, esses comandos podem ser executados utilizando a interface gráfica do programa. O mesmo pode ser feito no VSCode.

#### Limpeza das classes compiladas

Para limpar as classes compiladas, execute `./mvnw clean`

#### IMPORTANTE

Sempre que for desenvolver código novo, utilize uma nova branch. Para isso, rode `git checkout -b <nome-da-branch>`

#### ARQUIVOS FALTANTES

Os arquivos de configuração / docker-compose não estão sendo rastreados pelo git. Assim, é preciso fazer o download deles e colocar nas pastas do projeto. O docker-compose.yaml fica na raíz do projeto e os outros arquivos na pasta src/main/resources.

[ignored-files.zip](http://tools.ages.pucrs.br/compartilhamento/compartilhamento-wiki/wikis/uploads/9c3bbb87494af9800c382216ce42cea3/ignored-files.zip)