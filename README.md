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
Para configurar o plugin é necessário acessar File > Settings/Preferences > Tools > TAITI, e basta preencher os três campos mostrado na tela a seguir (Figura 2) com suas as respectivas informações (para conseguir o token do PivotalTracker basta acessar o seu [perfil](https://www.pivotaltracker.com/profile) e copiar o API Token).

<figure>
  <figcaption><em>Figura 2 - Tela de configuração do plugin TAITI.</em></figcaption>
  <img width="600px" alt="Settings" src="/doc/settings.png"/> 
</figure>

Após configurar o plugin, você pode acessá-lo na barra de menu em Tools > TAITI, onde é mostrado a tela principal do plugin (Figura 3).
Na seção 1 é inserido o ID da tarefa que você está trabalhando atualmente no PivotalTracker (por exemplo, #123456789 ou 123456789), a seção 2 é uma listagem de arquivos em estrutura de árvore, onde você seleciona um ou mais arquivos feature onde estão os testes do Cucumber relacionados à sua tarefa,
a seção 3 é exibido o arquivo feature selecionado e a seção 4 é uma tabela com todos os cenários selecionados.

<figure>
  <figcaption><em>Figura 3 - Tela principal do plugin TAITI.</em></figcaption>
  <img width="600px" alt="Tela do plugin" src="/doc/plugin1.png"/> 
</figure>

Após inserir o ID na seção 1 e selecionar um arquivo feature na seção 2, sua tela fica parecida com a tela da Figura 4. Onde o arquivo selecionado é exibido ao centro e cada teste nele existente é identificado por um checkbox. Ao marcar o checkbox, você informa que o referido teste está relacionado à tarefa. 
Alternativamente, é possível selecionar todos os testes em um arquivo, apenas marcando o checkbox que identifica o nome do arquivo na parte central da tela.

<figure>
  <figcaption><em>Figura 4 - Tela do plugin com o arquivo selecionado.</em></figcaption>
  <img width="600px" alt="Tela do plugin" src="/doc/plugin2.png"/> 
</figure>

Selecionado os cenários necessários para a tarefa, sua tela fica parecida com a tela da Figura 5. Onde na tabela (seção 4) há uma listagem de todos os testes selecionados, em um ou mais arquivos, e você pode atualizá-la, removendo algum teste caso julgue necessário.

<figure>
  <figcaption><em>Figura 5 - Tela do plugin com os cenários selecionados.</em></figcaption>
  <img width="600px" alt="Tela do plugin" src="/doc/plugin3.png"/> 
</figure>

Uma vez finalizada a seleção dos testes relacionados à tarefa, basta clicar em OK e um arquivo CSV com os cenários selecionados é criado e exportado para o PivotalTracker, sendo anexado como comentário na seção Activity da tarefa.

<figure>
  <figcaption><em>Figura 6 - Tela do comentário com o arquivo CSV anexado na aba Activity do PivotalTracker .</em></figcaption>
  <img alt="Tela do plugin" src="/doc/pivotal.png"/> 
</figure>

## Autores

Esse projeto está sendo desenvolvido por Mágno Gomes, sob orientação de Thaís Burity e participação de Gabriel Viana, na Universidade Federal do Agreste de Pernambuco.