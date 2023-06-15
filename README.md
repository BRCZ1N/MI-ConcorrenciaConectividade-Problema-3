<h2 align="center">PSistema de Bancos não centralizados </h2>
 
# Índice

- [Desenvolvedores](#desenvolvedores)
- [Solução](#Solução)
- [Componentes](#Componentes)
   - [AppBank](#Posto)
   - [Banco](#Névoa)
   - [Algoritmo de Concorrencia](#Carro)
- [Considerações finais](#consideracoes)

# Desenvolvedores
<br /><a href="https://github.com/BRCZ1N">Bruno Campos</a>
<br /><a href="https://github.com/Oguelo">Alex Júnior</a>

# Introdução

Esse problema foi desenvolvido com a proposta de criar o prototipo de um banco desentralizado, levando em conta um ambiente de extrema concorrencia de requisições 

O relatório presente pretende descrever de forma simples um projeto que se baseia em um sistema de comunicação que utiliza arquitetura em rede desenvolvido na linguagem de programação Java.

O presente projeto foi desenvolvido a partir do uso da comunicação por meio de requisições Http entre servidores de bancos e AppBank de uma maneira não centralizada, onde requisições são enviadas pelo cliente e densenvolvidas a partir de umas pespectiva onde os bancos se comunicam entre si, sem a necessidade de um orgão regulador 

# Fundamentação teórica

<li>O servidor pode ser definido como uma entidade que provê serviços para serem utilizados por clientes, seja ele um usuário ou um sistema. 
<li>O cliente pode ser determinado como uma entidade que utiliza dados providos de servidores.
<li>Relacionamento cliente-servidor, consiste basicamente em uma entidade que requisita serviços, isto é, um cliente e uma entidade que processa e envia respostas ao cliente, isto é, provendo-o, conhecido como servidor.
<li>API REST (Representational State Transfer) é um estilo de arquitetura de software para sistemas distribuídos que utiliza o protocolo HTTP para a comunicação entre diferentes sistemas. É um conjunto de padrões e princípios que definem como as requisições e respostas devem ser feitas, permitindo a integração de diferentes aplicações e sistemas. Em uma arquitetura RESTful, cada recurso é representado por uma URL, e as operações que podem ser realizadas nesses recursos são definidas pelos verbos HTTP (GET, POST, PUT, DELETE, entre outros).
<li>Contêineres são uma forma de virtualização de aplicativos que permite a criação de ambientes isolados e independentes, nos quais é possível executar aplicativos e serviços sem afetar o sistema operacional ou outras aplicações que estejam sendo executadas no mesmo servidor ou máquina.
<li>Docker é uma plataforma de código aberto que permite a criação, distribuição e execução de aplicativos em contêineres.

 # Metodologia geral

Para realização deste projeto a prori pensou-se no emprego de um tipo de arquitetura que poderia se adequar ao projeto, e ficou acordado que a arquitetura utilizada seria de névoa, afinal considerou-se que esse tipo arquitetural poderia fornecer um melhor desempenho com relação ao excesso de dados enviados, tendo em vista que o problema considera que o cenário proposto é na casa dos milhares de veículos, e portanto se sua concentração fosse na núvem isso poderia gerar diversas problemáticas de desempenho na aplicação com relação a quantidade de dados.

Com relação a comunicação entre os nós da rede ficou estabelicido da seguinte forma, cada carro envia requisições HTTP via API REST a uma névoa regional que estivesse dentro dos limites de sua localização. A névoa regional como mencionado anteriormente se comunica com o carro e retorna resposta para essas requisições, além disso ela se comunica via protocolo de comunicação MQTT com os postos de recarga para receber os dados dos postos da região, e portanto receber os dados de localização dos postos, dados de fila, nome do posto e até mesmo o seu identificador, com finalidade de obter e processar os dados que devem ser utilizados pelos usuarios finais, isto é, os carros, por fim a mesma envia os dados do melhor posto de sua região para a núvem via MQTT através de uma bridge MQTT. Finalmente tem-se a núvem que recebe os dados da névoa e armazena-os para retornar as névoas regionais uma lista dos melhores postos de cada região.

Com relação as névoas regionais a sua implementação ocorreu da seguinte forma, utilizou-se de uma aplicação spring para que a comunicação HTTP fosse efetuada, isto é, precisou de controladores, para enviar os dados para os usuários finais e para auxiliar os mesmos foram criados serviços com finalidade de processar algumas informações que foram armazenadas na névoa advindo dos postos e da núvem. Após receber os dados via MQTT eles foram processados e armazenados para que ficassem disponível aos usuários, além disso precisou-se de callbacks para a comunicação MQTT e threads, os callbacks foram utilizados para receber as mensagens via MQTT entre o posto e névoa, e entre névoa e núvem, além disso nessa comunicação névoa-nuvem utilizou-se de uma bridge mqtt, isto é, uma ponte de comunicação entre o broker regional e o broker global, para que fossem trocados os dados entre a névoa regional e o broker global

A aplicação que corresponde aos carros foram desenvolvidos com uma interface que utiliza o método generateBatteryCar para gerar o nível inicial da bateria do automóvel, juntamente com o método generateCurrentDischargeLevel, que geraria o nível de descarga que o carro teria. Além disso, o método generatePosUser é usado para definir a posição relativa aleatória do automóvel. Esses métodos gerariam números aleatórios para cada automóvel que existisse no sistema, então 3 threads são criadas com o objetivo de reduzir o nível de bateria do carro, monitorar o nível de bateria para gerar alertas ao motorista e a thread que irá definir qual estação de rádio o carro se comunicará, dependendo da latitude e longitude gerada automaticamente anteriormente. Finalmente, o motorista tem acesso a um menu que exibe o nível de carga da bateria do carro e também possui um menu com opções para buscar postos com as filas mais curtas na região, buscar os postos mais próximos do carro e até mesmo buscar postos em outras regiões que tenham as melhores condições de acesso.

Com o objetivo de rotear informações para as diferentes nevoas de cada região, uma nuvem foi construída com uma estrutura que permite o recebimento de dados enviados por cada nevoa no formato MQTT e o envio posterior desses dados para outras nevoas presentes no sistema. Para fazer isso, foram utilizadas duas threads, uma para receber dados de todas as nevoas, que possuem um identificador único para evitar a duplicação de dados, e outra para enviar esses dados para cada nevoa.É importante destacar que apenas os dados dos melhores postos de cada região são trocados pelas nevoas. Esses dados são compartilhados para eventual necessidade de acesso a um posto presente em uma região diferente.
 
 O posto de recarga utiliza a geolocalização para determinar sua posição relativa e se conectar à nevoa presente na área. Além disso, ele possui uma interface que permite ao usuário inserir o nome do estabelecimento. Após a inserção do nome, as threads são iniciadas para configurar a conexão MQTT, atualizar a quantidade de carros na fila e enviar esses dados para a nevoa presente na região.

# Componentes do projeto

<h2>- Névoa</h2>
<p2> A névoa processa as requisições advindas dos carros, realiza o tratamento das mesmas, e por fim retorna a resposta da requisição, ainda se considera a sua função de processar os dados advindos dos postos de recarga e comunica-se diretamente com a núvem para envio de dados que devem ser utilizados por outras partes da rede.</p2>
 <h2>- Nuvem</h2>
<p2> A nuvem faz o papel de processamento das informações advindos das névoas e passa-se essas informações processadas para cada névoa, isto é, para cada nó da rede para o usuário ter acesso a essas informações globais .</p2>
<h2>- Interface do Carro</h2>
<p2> O usuário se conecta ao servidor da nevoa e aos serviços através da API Rest. O mesmo, por conseguinte, apresenta as seguintes funcionalidades:</p2>
 <ul>
  <li>1. Visualizar nivel de carga de energia</li>
  <li>2. Buscar posto com menor fila </li>
  <li>3. Buscar posto mais proximo </li>
  <li>4. Buscar melhores postos de outras regiões</li>
  <li>5. Desconectar</li>
</ul>
<h2>- Interface do posto </h2>
<p2> O posto realiza o envio dos dados ao servidor da nevoa de forma continua</p2>
 <ul>
  <li>1. Envia a quantidade na fila e os dados necessários para armazenar a quantidade no servidor são eles: quantidade na fila, sua identificação e posição</li>
  <li>2. Altera o a quantidade de carros na fila</li>
</ul>
 
 # Considerações finais 
<p2> &emsp; O projeto consegue realizar tudo dentro das obrigações mínimas. Na implementação surgiu desafios com relação à compreensão da aplicação do framework Spring boot e no desevolvimento da comunicação MQTT, este projeto pode ainda ser escalavel, com o desenvolvimento da comunicação entre as nevoas sem a utilização de uma nuvem para mediar, assim diminuindo a latencia e deixando as informações fluirem de forma mais eficaz.
