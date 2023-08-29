# taiti-plugin

TAITI é uma ferramenta para predizer os arquivos que um desenvolvedor precisará alterar para realizar uma tarefa de programação, como o desenvolvimento de uma nova funcionalidade de sistema, e assim estimar o risco de conflito de merge entre tarefas a serem desenvolvidas em paralelo por uma equipe. 
Ela se aplica à projetos Rails que adotam a dinâmica de [BDD (Behavior Driven Development)](https://pt.wikipedia.org/wiki/Behavior_Driven_Development) com o [Cucumber](https://cucumber.io/) para a escrita de testes de aceitação automatizados e o [PivotalTracker](https://www.pivotaltracker.com/) para gerenciamento de tarefas do projeto.
TAITI foi desenvolvida como um plugin para a IDE RubyMine da JetBrains, também compatível com o IntelliJ Ultimate (com o plugin para Ruby).

## Instalação

TAITI está em desenvolvimento, segundo uma abordagem iterativa e incremental. Para fins de avaliação da interface gráfica, foi desenvolvida uma primeira versão que apenas lida com a organização da informação necessária para TAITI predizer os arquivos que o desenvolvedor precisará alterar para realizar uma tarefa de programação.
Para instalar TAITI é preciso:

* Instalar o [RubyMine](https://www.jetbrains.com/pt-br/ruby/) ou o [IntelliJ Ultimate](https://www.jetbrains.com/pt-br/idea/)
* Instalar o arquivo .zip do plugin disponível nas [releases do GitHub](https://github.com/vngabriel/taiti-plugin/releases/download/v0.1/taiti-plugin-0.1.zip)
* Abrir a IDE instalada e ir em File > Settings/Preferences > Plugins > Clicar na engrenagem superior ao lado da aba "Installed" (Figura 1) > Install Plugin from Disk > Selecionar o arquivo .zip do plugin instalado anteriormente > OK:

<figure>
  <figcaption><em>Figura 1 - Tela de plugins da IDE.</em></figcaption>
  <img alt="Engrenagem" src="/doc/engrenagem.png" width="600px"/>
</figure>

Depois desses passos, o plugin deve estar instalado na sua IDE.

## Funcionamento

TAITI prediz os arquivos que serão alterados durante a realização de uma tarefa de programação com base nos testes do Cucumber relacionados à tarefa a ser realizada.
No Cucumber, os testes são cenários escritos em arquivos features. Logo, por intermédio de TAITI, o desenvolvedor seleciona cenários de testes para uma tarefa, o que pressupõe que estes já tenham sido projetados.
Os testes selecionados são salvos em um arquivo CSV que é exportado para o PivotalTracker.

## Como usar

Primeiramente, é necessário ter uma conta no [PivotalTracker](https://www.pivotaltracker.com/) e que o repositório remoto do seu projeto esteja no [GitHub](https://github.com/).
Para configurar o plugin é necessário acessar File > Settings/Preferences > Tools > TAITI, e basta preencher os três primeiros campos mostrado na tela a seguir (Figura 2) com suas as respectivas informações sobre o projeto a ser utilizado, já o Test Settings tem valores padrões, mude apenas se necessário (para conseguir o token do PivotalTracker basta acessar o seu [perfil](https://www.pivotaltracker.com/profile) e copiar o API Token).

<figure>
  <figcaption><em>Figura 2 - Tela de configuração do plugin TAITI.</em></figcaption>
  <img width="600px" alt="Settings" src="/doc/settings.png"/> 
</figure>

Após configurar o plugin, você pode acessar suas telas abrindo as abas TaskList e Conflicts, que estão localizadas nas opções mais a esquerda e nas abas inferiores respectivamente, sendo mostrado a visão principal do plugin (Figura 3). Na seção 1 temos o botão de atribuir cenários para as tarefas do PivotalTracker e assim adicionar tarefas no plugin. A seção 2 é o botão para recarregar as informações e listas, sendo necesário utilizar uma vez após a configuração do plugin para carregar o plugin. A seção 3 temos a lista de tarefas que pertence ao usuário, tarefas essas que estão planejadas para começar e pode ser avaliado o risco de conflitos delas para determinar a ordem de execução delas. Já a seção 4 são as tarefas já cadastradas que pertence a outros usuários do seu projeto e podem conflitar com suas tarefas.

<figure>
  <figcaption><em>Figura 3 - Visão principal do plugin TAITI.</em></figcaption>
  <img width="600px" alt="Abas do Plugin" src="/doc/plugin1.png"/> 
</figure>

Após recarregar as informações do plugin teremos as tabelas de tarefas preenchidas, isso considerando se houver tarefas já cadastradas, (Figura 4). Na lista, observamos parte do título da tarefa e a média do nível de conflito da tarefa com outras em execução. Caso mais informações sejam necessárias, ao posicionar o mouse sobre cada tarefa, o título completo, ID e proprietário serão exibidos.

<figure>
  <figcaption><em>Figura 4 - Lista de Tarefas Carregadas.</em></figcaption>
  <img width="600px" alt="Lista de tarefas do Plugin" src="/doc/plugin2.png"/> 
</figure>

Ao dar dois cliques sobre uma das tarefas, é viável visualizar todos os conflitos a ela relacionados. Isso inclui a tarefa que está em conflito, o risco relativo entre cada par de tarefas e informações cruciais como o link para a tarefa em conflito no PivotalTracker, ID da tarefa, descrição e os arquivos envolvidos nos conflitos.

<figure>
  <figcaption><em>Figura 5 - Conflitos entre Tarefas.</em></figcaption>
  <img width="600px" alt="Conflicts" src="/doc/plugin3.png"/> 
</figure>

Para conectarmos as tarefas do PivotalTracker com os cenários de teste do projeto e assim adicionando na lista do plugin basta clicar no botão "Add" explicado anteriormente, assim abrirá a tela de configuração da tarefa (Figura 6).
Na seção 1 é inserido o ID da tarefa que você está trabalhando atualmente no PivotalTracker (por exemplo, #123456789 ou 123456789), a seção 2 é uma listagem de arquivos em estrutura de árvore, onde você seleciona um ou mais arquivos feature onde estão os testes do Cucumber relacionados à sua tarefa,
a seção 3 é exibido o arquivo feature selecionado e a seção 4 é uma tabela com todos os cenários selecionados.

<figure>
  <figcaption><em>Figura 6 - Tela adicionar tarefa do plugin TAITI.</em></figcaption>
  <img width="600px" alt="Tela Add do plugin" src="/doc/plugin_add1.png"/> 
</figure>

Após inserir o ID na seção 1 e selecionar um arquivo feature na seção 2, sua tela fica parecida com a tela da Figura 7. Onde o arquivo selecionado é exibido ao centro e cada teste nele existente é identificado por um checkbox. Ao marcar o checkbox, você informa que o referido teste está relacionado à tarefa. 
Alternativamente, é possível selecionar todos os testes em um arquivo, apenas marcando o checkbox que identifica o nome do arquivo na parte central da tela.

<figure>
  <figcaption><em>Figura 7 - Tela do plugin com o arquivo selecionado.</em></figcaption>
  <img width="600px" alt="Tela do plugin" src="/doc/plugin_add2.png"/> 
</figure>

Selecionado os cenários necessários para a tarefa, sua tela fica parecida com a tela da Figura 8. Onde na tabela (seção 4) há uma listagem de todos os testes selecionados, em um ou mais arquivos, e você pode atualizá-la, removendo algum teste caso julgue necessário.

<figure>
  <figcaption><em>Figura 8 - Tela do plugin com os cenários selecionados.</em></figcaption>
  <img width="600px" alt="Tela do plugin" src="/doc/plugin_add3.png"/> 
</figure>

Uma vez finalizada a seleção dos testes relacionados à tarefa, basta clicar em OK e um arquivo CSV com os cenários selecionados é criado e exportado para o PivotalTracker, sendo anexado como comentário na seção Activity da tarefa.

<figure>
  <figcaption><em>Figura 9 - Tela do comentário com o arquivo CSV anexado na aba Activity do PivotalTracker .</em></figcaption>
  <img alt="Tela do plugin" src="/doc/pivotal.png"/> 
</figure>

## Autores

Esse projeto está sendo desenvolvido por Mágno Gomes, sob orientação de Thaís Burity e participação de Gabriel Viana, na Universidade Federal do Agreste de Pernambuco.