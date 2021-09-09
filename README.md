# taiti-plugin
========================

O TAITI é uma ferramenta que funciona no contexto de BDD e tenta evitar os conflitos de merge através da priorização de tarefas baseada em teste, atualmente ela é um plugin para a IDE RubyMine e IntelliJ Ultimate da JetBrains.

## Como instalar TAITI

Atualamente o plugin TAITI não está finalizado, por isso ele não está publicado no marketplace de plugins da JetBrains.
Mas para fins de experimentação do plugin, para usá-lo é preciso:

* Instalar o [RubyMine](https://www.jetbrains.com/pt-br/ruby/) ou o [IntelliJ Ultimate](https://www.jetbrains.com/pt-br/idea/)
* Instalar o arquivo .zip do plugin disponível nas [releases do GitHub](https://github.com/vngabriel/taiti-plugin/releases/download/v0.1/taiti-plugin-0.1.zip)
* Abrir a IDE instalada e ir em File > Settings/Preferences > Plugins > Clicar na engrenagem superior que está ao lado da aba "Installed" > Install Plugin from Disk > Selecionar o arquivo .zip do plugin instalado anteriormente > OK

Depois desses passos o plugin já deve estar instalado na sua IDE.

## Como o plugin funciona

O TAITI é uma ferramenta que analisa risco de conflitos a partir de arquivos do [Cucumber](https://cucumber.io/), sendo assim, depois dos testes estarem escritos
é necessários selecionar os Scenarios dos testes relacionados a tarefa que você deseja realizar, assim, depois de selecionado todos os Scenarios,
o plugin irá salvá-los em um arquivo CSV e mandá-lo para o PivotalTracker. 

(Depois disso, o plugin irá recolher todos os Scenarios salvos nas tarefas do PivotalTracker para fazer a análise de conflito.) -> essa parte ainda será desenvolvida.

## Como usar o plugin

Primeiramento é necessário ter uma conta no [PivotalTracker](https://www.pivotaltracker.com/) e que o repositório remoto do seu projeto esteja no [GitHub](https://github.com/).
Para configurar o plugin é necessário acessar File > Settings/Preferences > Tools > TAITI, onde será mostrado esta tela:

<img width="600px" alt="Settings" src="/doc/settings.png"/> 

Basta preencher esses três campos com as respectivas informações. (Para conseguir o token do PivotalTracker basta acessar seu [perfil](https://www.pivotaltracker.com/profile) e copiar o API Token)


Após configurar o plugin, você pode acessá-lo na barra de menu em Tools > TAITI, onde será mostrado a tela principal do plugin:

<img width="600px" alt="Tela do plugin" src="/doc/plugin1.png"/> 

No campo Task ID você colocará o ID da tarefa que você está trabalhando no PivotalTracker (por exemplo, #123 ou 123). 
Logo abaixo do campo de texto tem uma árvore de arquivos onde você pode selecionar o arquivo Cucumber que está relacionado a sua tarefa:

<img width="600px" alt="Tela do plugin" src="/doc/plugin2.png"/> 

Selecionado os Scenarios necessários para a tarefa, você terá uma tela mais ou menos assim:

<img width="600px" alt="Tela do plugin" src="/doc/plugin3.png"/> 

Onde na tabela abaixo da árvore de arquivos você pode verificar os Scenarios selecionados e removê-los se necessário.

Com tudo selecionado basta clicar em OK, e na sua tarefa do PivotalTracker deverá ter um comentário com um arquivo anexado em "Activity":

<img alt="Tela do plugin" src="/doc/pivotal.png"/> 

No futuro, quando for implementado a análise de conflitos o plugin irá recolher todos os arquivos CSV do PivotalTracker para fazer a análise, por isso é importante não excluí-los, nem criar novos comentários com o seguinte padrão: "[TAITI] Scenarios".











