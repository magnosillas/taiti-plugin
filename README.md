# Plugin TAITIr

TAITIr é uma ferramenta para predizer os arquivos que um desenvolvedor precisará alterar para realizar uma tarefa de programação, como o desenvolvimento de uma nova funcionalidade de sistema, e assim estimar o risco de conflito de merge entre tarefas a serem desenvolvidas em paralelo por uma equipe. 
Ela se aplica à projetos Rails que adotam a dinâmica de [BDD (Behavior Driven Development)](https://pt.wikipedia.org/wiki/Behavior_Driven_Development) com o [Cucumber](https://cucumber.io/) para a escrita de testes de aceitação automatizados e o [PivotalTracker](https://www.pivotaltracker.com/) para gerenciamento de tarefas do projeto.
TAITIr foi desenvolvida como um plugin para a IDE RubyMine da JetBrains, também compatível com o IntelliJ Ultimate (com o plugin para Ruby).

## Instalação

* Instalar o [RubyMine](https://www.jetbrains.com/pt-br/ruby/) ou o [IntelliJ Ultimate](https://www.jetbrains.com/pt-br/idea/);
* Baixar o arquivo <em>.zip</em> do plugin disponível nas [releases do GitHub](https://github.com/vngabriel/taiti-plugin/releases/download/v0.1/taiti-plugin-0.1.zip);
* Abrir a IDE e, conforme ilustrado pela Figura 1, ir em File > Settings/Preferences > Plugins, clicar na engrenagem superior ao lado da aba <em>Installed</em>, selecionar a opção <em>Install Plugin from Disk</em>, selecionar o arquivo <em>.zip</em> do plugin e clicar em OK.

<figure>
  <figcaption><em>Figura 1 - Tela de plugins da IDE.</em></figcaption>
  <img alt="Engrenagem" src="/doc/engrenagem.png" width="600px"/>
</figure>

## Funcionamento

TAITIr prediz os arquivos que serão alterados durante a realização de uma tarefa de programação com base nos testes do Cucumber que validam o comportamento esperado da funcionalidade de sistema subjacente à tarefa.
No Cucumber, os testes são cenários escritos em arquivos <em>.feature</em>. Logo, por intermédio de TAITIr, o desenvolvedor seleciona cenários de testes para uma tarefa, o que pressupõe que estes já tenham sido projetados.
Os testes que validam o comportamento da tarefa são salvos em um arquivo <em>.csv</em> que é exportado para o PivotalTracker, ficando acessível no post-it da tarefa.

Com base no exposto, é necessário ter uma conta no [PivotalTracker](https://www.pivotaltracker.com/) e que o projeto de software em desenvolvimento esteja hospedado no [GitHub](https://github.com/). 
Além disso, é necessário configurar o plugin para informar o projeto em desenvolvimento.
Para tanto, basta acessar File > Settings/Preferences > Tools > TAITIr e preencher os três primeiros campos da seção <em>Project settings</em> na tela ilustrada pela Figura 2. 
Para saber o <em>token</em> do PivotalTracker, basta acessar o [perfil do usuário](https://www.pivotaltracker.com/profile) e copiar o <em>API Token</em>. 
A seção <em>Test Settings</em> tem valores padrão referentes aos diretórios de armazenamento de arquivos de testes, que só devem ser alterados se realmente necessário.

<figure>
  <figcaption><em>Figura 2 - Tela de configuração do plugin TAITIr.</em></figcaption>
  <img width="600px" alt="Settings" src="/doc/settings.png"/> 
</figure>

Após configurar o plugin, é possível utilizá-lo. 
O plugin tem duas telas principais: <em>Task List</em> (item 1 na Figura 3, uma aba na lateral esquerda da IDE) e <em>Conflicts</em> (item 2 na Figura 3, uma aba na região inferior da IDE).
<em>Task List</em> lista as tarefas do PivotalTracker previstas para o desenvolvedor (item 3 na Figura 3) e as tarefas do PivotalTracker em execução no momento por outros desenvolvedores (item 4 na Figura 3), ou seja, o conjunto de tarefas a serem consideradas na análise de risco de conflito. 

<figure>
  <figcaption><em>Figura 3 - Visão principal do plugin TAITIr.</em></figcaption>
  <img width="800px" alt="Abas do Plugin" src="/doc/plugin1.png"/> 
</figure>

Conforme ilustrado na Figura 3 (item 3, listagem <em>My unstarted tasks</em>), quando o desenvolvedor identifica as tarefas que planeja executar, o plugin informa para cada uma delas o grau de risco de conflito total com outras tarefas em execução no momento. 
Informações mais detalhadas sobre as tarefas podem ser obtidas ao posicionar o mouse sobre a tarefa.

As tarefas em execução são obtidas automaticamente, mas aquelas planejadas para o desenvolvedor devem ser adicionadas por ele. 
Isso pode ser feito ao clicar no item 1 na Figura 4, que dá uma visão detalhada da barra de tarefas existente na tela <em>Task List</em>. 
Ainda na barra de tarefas ilustrada na Figura 4, é possível atualizar as listagens de tarefas ao clicar no item 2, bem como pesquisar pelo título de alguma tarefa de interesse usando a caixa de texto do item 3, em caso das listagens serem extensas.

<figure>
  <figcaption><em>Figura 4 - Visão detalhada das ações possíveis na tela Task List.</em></figcaption>
  <img width="600px" alt="Ações do Task List" src="/doc/plugin2.png"/> 
</figure>

Finalmente, na listagem <em>My unstarted tasks</em> na tela <em>Task List</em> (Figura 3, item 3), é possível visualizar informação detalhada sobre o risco de conflito na tela Conflicts (Figura 5, item 1) ao dar dois cliques sobre alguma tarefa.
Por exemplo, se há uma tarefa planejada e 3 tarefas em execução, é possível saber o risco de conflito entre a tarefa planejada e cada tarefa em execução individualmente e quais são os arquivos potencialmente conflitantes.

<figure>
  <figcaption><em>Figura 5 - Visão detalhada de risco de conflito entre tarefas de programação.</em></figcaption>
  <img height="200px" alt="Conflicts" src="/doc/plugin3.png"/> 
</figure>

Para informar os testes do Cucumber que validam o comportamento esperado de uma tarefa de programação no PivotalTracker, é necessário clicar no item 1 na Figura 4 (tela <em>Task List</em>) para acessar a tela de configuração da tarefa, ilustrada pela Figura 6.
Na Figura 6, o item 1 é o ID da tarefa no PivotalTracker (por exemplo, #123456789 ou 123456789). 
O item 2 é uma listagem de arquivos em estrutura de árvore na qual é possível selecionar um ou mais arquivos <em>.feature</em> que contém cenários de teste relacionados à tarefa.
Já o item 3 exibe o arquivo <em>.feature</em> selecionado e o item 4 é uma tabela que contém todos os cenários selecionados.

<figure>
  <figcaption><em>Figura 6 - Tela de configuração de uma tarefa de programação.</em></figcaption>
  <img width="600px" alt="Tela Add do plugin" src="/doc/plugin_add1.png"/> 
</figure>

Após informar o ID no item 1 e selecionar um arquivo <em>.feature</em> no item 2, a tela fica parecida com a Figura 6, na qual o arquivo selecionado é exibido ao centro e cada cenário de teste nele existente é identificado por um checkbox. 
Marcar um checkbox significa que o referido teste está relacionado à tarefa em configuração. 
Alternativamente, é possível selecionar todos os cenários de testes em um arquivo de uma vez apenas marcando o checkbox que identifica o nome do arquivo na parte central da tela (Área destacada no item 3).
Por fim, é possível verificar todos os testes selecionados em um ou mais arquivos (item 4 da Figura 6), bem como atualizar essa seleção, removendo testes conforme a necessidade.

Uma vez finalizada a seleção dos testes relacionados à tarefa, basta clicar em OK e um arquivo <em>.csv</em> com os cenários de testes selecionados é criado e exportado para o PivotalTracker, sendo anexado como comentário na seção <em>Activity</em> da tarefa, conforme ilustrado pela Figura 7.

<figure>
  <figcaption><em>Figura 7 - Arquivo .csv anexado à uma tarefa no PivotalTracker.</em></figcaption>
  <img width="600px" alt="Tela do plugin" src="/doc/pivotalimg.png"/> 
</figure>

## Autores

Esse projeto foi desenvolvido pelo discente Mágno Gomes, sob orientação da docente Thaís Burity e participação do discente Gabriel Viana e do docente Rodrigo Cardoso, na Universidade Federal do Agreste de Pernambuco.
