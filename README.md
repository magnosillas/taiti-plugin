# taiti-plugin

TAITI é uma ferramenta para predizer os arquivos que um desenvolvedor precisará alterar para realizar uma tarefa de programação, como o desenvolvimento de uma nova funcionalidade de sistema, e assim estimar o risco de conflito de merge entre tarefas a serem desenvolvidas em paralelo por uma equipe. 
Ela se aplica à projetos Rails que adotam a dinâmica de [BDD (Behavior Driven Development)](https://pt.wikipedia.org/wiki/Behavior_Driven_Development) com o [Cucumber](https://cucumber.io/) para a escrita de testes de aceitação automatizados e o [PivotalTracker](https://www.pivotaltracker.com/) para gerenciamento de tarefas do projeto.
TAITI foi desenvolvida como um plugin para a IDE RubyMine da JetBrains, também compatível com o IntelliJ Ultimate (com o plugin para Ruby).

## Como instalar TAITI

TAITI está em desenvolvimento, segundo uma abordagem iterativa e incremental. Para fins de avaliação da interface gráfica, foi desenvolvida uma primeira versão que apenas lida com a organização da informação necessária para TAITI predizer os arquivos que o desenvolvedor precisará alterar para realizar uma tarefa de programação.
Para instalar TAITI é preciso:

* Instalar o [RubyMine](https://www.jetbrains.com/pt-br/ruby/) ou o [IntelliJ Ultimate](https://www.jetbrains.com/pt-br/idea/)
* Instalar o arquivo .zip do plugin disponível nas [releases do GitHub](https://github.com/vngabriel/taiti-plugin/releases/download/v0.1/taiti-plugin-0.1.zip)
* Abrir a IDE instalada e ir em File > Settings/Preferences > Plugins > Clicar na engrenagem superior que está ao lado da aba "Installed": 

<img alt="Engrenagem" src="/doc/engrenagem.png" width="600px"/>

* Clicar em Install Plugin from Disk > Selecionar o arquivo .zip do plugin instalado anteriormente > OK

Depois desses passos, o plugin já deve estar instalado na sua IDE.

## Como o plugin funciona

TAITI prediz os arquivos que serão alterados durante a realização de uma tarefa de programação com base nos testes do Cucumber relacionados à tarefa a ser realizada. 
No Cucumber, os testes são cenários escritos em arquivos features. Logo, por intermédio de TAITI, o desenvolvedor seleciona cenários de testes para uma tarefa, o que pressupõe que estes já tenham sido projetados. 
Os testes selecionados são salvos em um arquivo CSV que é exportado para o PivotalTracker.

(Depois disso, o plugin irá recolher todos os Scenarios salvos nas tarefas do PivotalTracker para fazer a análise de conflito.) -> essa parte ainda será desenvolvida.

## Como usar o plugin

Primeiramente, é necessário ter uma conta no [PivotalTracker](https://www.pivotaltracker.com/) e que o repositório remoto do seu projeto esteja no [GitHub](https://github.com/).
Para configurar o plugin é necessário acessar File > Settings/Preferences > Tools > TAITI, onde será mostrado esta tela:

<img width="600px" alt="Settings" src="/doc/settings.png"/> 

Basta preencher esses três campos com as respectivas informações. (Para conseguir o token do PivotalTracker basta acessar o seu [perfil](https://www.pivotaltracker.com/profile) e copiar o API Token)


Após configurar o plugin, você pode acessá-lo na barra de menu em Tools > TAITI, onde será mostrado a tela principal do plugin:

<img width="600px" alt="Tela do plugin" src="/doc/plugin1.png"/> 

No campo Task ID colocará o ID da tarefa que você está trabalhando no PivotalTracker (por exemplo, #123 ou 123). 
Logo abaixo do campo de texto tem uma árvore de arquivos onde você pode selecionar o arquivo feature onde estão os testes do Cucumber que está relacionado a sua tarefa:

<img width="600px" alt="Tela do plugin" src="/doc/plugin2.png"/> 

Selecionado os cenários necessários para a tarefa, você terá uma tela parecida com esta:

<img width="600px" alt="Tela do plugin" src="/doc/plugin3.png"/> 

Onde na tabela abaixo da árvore de arquivos você pode verificar os cenários selecionados e removê-los se necessário.

Com tudo selecionado basta clicar em OK, e o arquivo CSV, com os cenários selecionados, será criado e será exportado para a sua tarefa do PivotalTracker onde deverá ter um comentário com um arquivo anexado em "Activity":

<img alt="Tela do plugin" src="/doc/pivotal.png"/> 

No futuro, quando for implementado a análise de conflitos o plugin irá recolher todos os arquivos CSV do PivotalTracker para fazer a análise, por isso é importante não excluí-los, nem criar novos comentários com o seguinte padrão: "[TAITI] Scenarios".











