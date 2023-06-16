<h2 align="center">Sistema de bancos distribuidos </h2>
 
# Índice

- [Desenvolvedores](#desenvolvedores)
- [Solução](#solução)
- [Componentes](#componentes)
   - [Aplicação cliente](#Cliente)
   - [Servidor do banco](#Banco)
- [Considerações finais](#considerações)

# Desenvolvedores
<br /><a href="https://github.com/BRCZ1N">Bruno Campos</a>
<br /><a href="https://github.com/Oguelo">Alex Júnior</a>

# Introdução

Esse problema foi desenvolvido com a proposta de criar o prototipo de um banco descentralizado, levando em conta um ambiente de extrema concorrencia de requisições. O relatório presente pretende descrever de forma simples um projeto que se baseia em um sistema de comunicação que utiliza arquitetura em rede desenvolvido na linguagem de programação Java. O presente projeto foi desenvolvido a partir do uso da comunicação por meio de requisições Http entre servidores dos bancos e a aplicação cliente de uma maneira não centralizada, onde requisições são enviadas pelo cliente e densenvolvidas a partir de uma pespectiva onde os bancos se comunicam entre si, sem a necessidade de um orgão regulador e finalmente utilizando algoritmo de controle de concorrência.

# Fundamentação teórica

<li>O servidor pode ser definido como uma entidade que provê serviços para serem utilizados por clientes, seja ele um usuário ou um sistema. 
<li>O cliente pode ser determinado como uma entidade que utiliza dados providos de servidores.
<li>O relacionamento P2P, ou Peer-to-Peer (par a par), refere-se a um modelo de comunicação e compartilhamento de recursos em rede. Nesse tipo de relacionamento, os dispositivos ou computadores conectados à rede são considerados iguais e têm a capacidade de agir tanto como cliente quanto como servidor, compartilhando recursos diretamente entre si, sem a necessidade de um servidor centralizado..
<li>API REST (Representational State Transfer) é um estilo de arquitetura de software para sistemas distribuídos que utiliza o protocolo HTTP para a comunicação entre diferentes sistemas. É um conjunto de padrões e princípios que definem como as requisições e respostas devem ser feitas, permitindo a integração de diferentes aplicações e sistemas. Em uma arquitetura RESTful, cada recurso é representado por uma URL, e as operações que podem ser realizadas nesses recursos são definidas pelos verbos HTTP (GET, POST, PUT, DELETE, entre outros).
<li>Contêineres são uma forma de virtualização de aplicativos que permite a criação de ambientes isolados e independentes, nos quais é possível executar aplicativos e serviços sem afetar o sistema operacional ou outras aplicações que estejam sendo executadas no mesmo servidor ou máquina.
<li>Docker é uma plataforma de código aberto que permite a criação, distribuição e execução de aplicativos em contêineres.
<li>Docker é uma plataforma de código aberto que permite a criação, distribuição e execução de aplicativos em contêineres.
<li> O algoritmo de Ricart e Agrawala é um método para garantir que apenas um processo tenha acesso a um recurso compartilhado em um sistema distribuído. Os processos enviam mensagens de solicitação e confirmação uns aos outros, permitindo que apenas um processo acesse o recurso por vez. Isso evita conflitos e garante a exclusão mútua.

# Metodologia geral

Para realização deste projeto a prori pensou-se no emprego de um tipo de arquitetura que poderia se adequar ao projeto, e ficou acordado que a arquitetura utilizada seria P2P, afinal os bancos se comportariam uma hora como clientes e outrora como servidores, além disso para fins de controle de concorrência utilizou-se da ideia do algoritmo de concorrencia em ambientes distribuidos de Ricart e Agrawala.

O servidor do banco foi elaborado da seguinte forma, utilizou-se de uma aplicação spring para que a comunicação HTTP fosse efetuada, isto é, precisou de controladores, para enviar os dados para os usuários finais e para trocar os dados entre as instituições bancárias, para cada banco utilizou-se de serviços de armazenamento temporário de dados para que fossem armazenados a contas dos clientes, cada conta do banco deveria ter um identificador que seria gerado na hora da criação da conta nos serviços de conta, além disso cada conta da instituição deveria haver um saldo, uma lista de beneficiários da conta, uma senha e finalmente um objeto contendo uma representação simbolica de informações sobre o banco, isto é, uma entidade contendo o endereço ip e o identificador do banco, portanto cada banco tinha o serviço de criar e armazenar contas, autenticar contas, enviar o saldo para o cliente, comunicar-se com outros bancos para que as requisições que envolvem-se regiões críticas, isto é, as contas dos bancos fossem realizadas, além disso cada banco tem o dever de retornar uma resposta ao banco solicitante, além depositar saldo conforme fosse requerido, final o mesmo tratava condições de erro como saldo insuficiente, retornando por fim para cada operação uma resposta adequada.
 
Com relação a aplicação que emulava o cliente basicamente, ela teria acesso aos identificadores e endereços ip de todas as instituições bancárias para que cada operação possível fossem enviado ao servidor do banco, seja: realizar login, registrar uma conta, depositar saldo, transferir saldo, desconectar de uma conta ou alterar o banco de acesso. Com a finalidade de mostrar ao usuário final o efeito dos caminhos de requisição que ele pode seguir ao enviar/realizar uma operação à uma instituição bancária.
 
Com relação a comunicação entre os nós da rede ficou estabelicido da seguinte forma através da concepção do Ricart e Agrawala, cada banco envia requisições HTTP via API REST a um outro banco para requisitar acesso a região crítica, cada banco que recebe tal requisição responderá um "OK" para o solicitante  em três condições: a primeira delas é não possuir uma requisição que deseja acessar a região crítica e não estiver usando a região crítica no momento, a segunda condição é que se o mesmo não estiver na região crítica mas quer acessa-la então o mesmo compara os relógios lógicos do banco requisitor e do banco requerente, e finalmente se o mesmo não estiver na região crítica mas quer acessa-la e tendo os relógios lógicos com o mesmo valor então para desempatar utilizou-se do identificador do banco atributido a cada banco da rede em questão.

# Componentes do projeto

<h2>- Banco</h2>
<p2> Processa os dados advindos do cliente visando controle de concorrencia entre as contas do banco, para por fim realizar as operações possíveis</p2>
<h2>- Interface do cliente</h2>
<p2> O usuário se conecta ao servidor ao banco requerinte através da API Rest. O mesmo, por conseguinte, apresenta as seguintes funcionalidades:</p2>
 <ul>
  <li>1. Logar em um dos bancos banco</li>
  <li>2. Cadastrar uma conta em um dos bancos</li>
  <li>3. Depositar saldo na conta logada na instituição escolhida </li>
  <li>4. Transferir saldo na conta logada para uma das instituições bancárias </li>
  <li>5. Desconectar da conta</li>
  <li>6. Alterar o banco de acesso</li>
</ul>
 
# Dica de utilização 
 
 <p2> No arquivo armazenado neste github possui um docker-compose para a aplicação cliente e um para a aplicação banco, se for utilizado no portainer é possível diretamente sem quais quer problemas executar o .jar do arquivo das aplicações cliente e do servidor do   banco que estão armazenadas no docker hub, e finalmente é possível apenas simplesmente inicializar o container e executa com o ".jar" do container docker se for o cliente, mas o banco apenas precisa-se apenas incializar o docker-compose</p2>
  <p2> ATENÇÃO: Os bancos setados na aplicação estão destinados a rede do larsid da UEFS então respectivamente para as máquinas 3 , 4 , 5 e 6, caso queira realizar em outras máquinas ou diminuir a quantidade de bancos basta alterar na enumeração dos bancos      presente na classe de enumeração "BanksEnum" e criar uma nova imagem docker do projeto, e finalmente executar o container</p2>
 
 # Considerações finais 
<p2> &emsp; O projeto consegue realizar tudo dentro das obrigações mínimas. Na implementação surgiu desafios com relação à compreensão e implentação do algoritmo de controle de concorrencia de Ricart e Agrawala, e como isso poderia afetar o desenvolvimento do sistema da aplicação, ademais é possível evoluir o projeto no aspecto relacionado a timeout de requisições visto que essa versão do algoritmo de Ricart e Agrawala pode apresentar problemas caso um dos nós da rede falhe, isto é, afetará a rede como um todo.
