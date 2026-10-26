# Soup HUD 1.8.9

HUD leve e personalizável para SoupPvP (Minecraft Java 1.8.9 + Forge 11.15.1.2318). Mod somente de cliente.

Gera: `build/libs/SoupHUD-1.8.9.jar`

## Estrutura

```
SoupHUD/
├── build.gradle                  ForgeGradle 2.1, Forge 1.8.9-11.15.1.2318, mappings stable_20
├── settings.gradle / gradle.properties / .gitignore
├── .github/workflows/build.yml   compilação automática no GitHub Actions
└── src/main
    ├── resources/mcmod.info
    └── java/com/soupmod/souphud
        ├── SoupHudMod.java            classe principal
        ├── config/                    ConfigManager (persistência), HudConfig, ElementSettings, ElementType, CountMode, DisplayMode
        ├── counter/                   InventoryScanner, SoupCounter, BowlCounter, FreeSlotCounter
        ├── hud/                       HudManager, HudRenderer, HudElement (+ Soup/Bowl/FreeSlots/Status), HudLayout, HudIcons
        ├── gui/                       SettingsScreen, HudEditorScreen, SliderButton, ColorPresets
        ├── input/KeyManager.java      teclas configuráveis
        └── event/                     EventRegistry, ClientTickHandler, RenderHandler
```

## Ambiente de desenvolvimento (local)

Requisitos: **JDK 8** (JAVA_HOME apontando para ele) e **Gradle 2.14.1** (o ForgeGradle 2.1 não funciona com Gradle mais novo).

1. Baixe o MDK: `forge-1.8.9-11.15.1.2318-1.8.9-mdk.zip` (files.minecraftforge.net, versão 1.8.9).
2. Do MDK, copie para a raiz deste projeto: `gradlew`, `gradlew.bat` e a pasta `gradle/` (traz o Gradle 2.14.1 e o wrapper). Não copie o build.gradle do MDK.
3. Na raiz do projeto:

```
gradlew setupDecompWorkspace
gradlew eclipse        (ou: gradlew idea)
```

Execute no IDE ou com `gradlew runClient` para testar sem gerar o jar.

## Compilar e gerar o jar

```
gradlew build
```

O arquivo final fica em `build/libs/SoupHUD-1.8.9.jar` (o `-sources.jar` não é o mod).

### Alternativa: GitHub Actions
Suba o projeto para um repositório, faça push na branch `main`/`master` e baixe o artefato `SoupHUD-1.8.9` na aba Actions. O workflow instala Java 8 e Gradle 2.14.1 e roda `gradle setupCIWorkspace build`.

## Instalação e teste

1. Instale o Forge 1.8.9 (11.15.1.2318) e abra o jogo uma vez.
2. Copie `SoupHUD-1.8.9.jar` para `.minecraft/mods`.
3. Entre em um mundo ou servidor. Sopas, tigelas e espaços livres já aparecem no lado esquerdo.
4. Teste: `/give @p mushroom_stew 5` e `/give @p bowl 10` no singleplayer; confira se os números sobem, se o alerta aparece ao ter poucas sopas e se "CHEIO" aparece com o inventário lotado.

Teclas padrão (mudam em Opções > Controles > "Soup HUD"):
- **Shift direito**: configurações (com prévia ao vivo)
- **Ctrl direito**: editor de HUD (arrastar com o mouse; ESC salva e sai)

Configurações ficam em `.minecraft/config/souphud.json` e persistem entre sessões. Para cores fora da paleta, edite o inteiro RGB (ex.: `16733525` = 0xFF5555) no JSON com o jogo fechado.

## Como funciona (resumo técnico)

- `ClientTickHandler` lê os slots reais (`player.inventory.mainInventory`) 1x por tick, no fim do tick, sem alocar objetos. Pilhas somam `stackSize`; slots vazios (`null`) contam como livres.
- `HudManager` só reconstrói textos/larguras quando a configuração, as contagens ou a fonte mudam; nos outros frames só desenha.
- Posições são frações da tela em coordenadas de GUI e são limitadas à área visível, arredondadas para inteiro (sem tremor, funciona em qualquer resolução/escala de GUI).
- Nenhum clique, mouse, item ou pacote é tocado. Só leitura do inventário e desenho no `RenderGameOverlayEvent.Post`.

## Como modificar e adicionar elementos

Ajustar um elemento existente: edite o método `build()` da classe (ex.: `SoupElement`). Textos, ícones e cores saem de `add(icone, texto, corRGB)`.

Criar um elemento novo (ex.: contagem de poções):
1. `config/ElementType.java`: adicione uma constante (nome, posição padrão, cor, modos).
2. `config/HudConfig.java`: adicione o campo `ElementSettings` e inclua o caso nos `switch` de `get()` e `set()`, e no `copyFrom()`.
3. Se precisar de um dado novo: crie `counter/PotionCounter extends InventoryCounter` (implemente `matches`) e exponha em `InventoryScanner` (scan no `update`).
4. Crie `hud/PotionElement extends HudElement` com o `build()`.
5. `hud/HudManager.java`: acrescente `new PotionElement()` no array `elements`.

Menu de configurações e editor passam a incluí-lo automaticamente (percorrem `ElementType.values()`).

## Limitações técnicas e decisões

- **Transparência dos ícones**: o renderizador de itens do 1.8.9 ignora o alpha do OpenGL, então a transparência vale para fundo e texto; ícones ficam sempre opacos.
- **Cores**: o menu usa uma paleta de 10 cores (cicla ao clicar). Cores livres via `souphud.json`.
- **O que conta como sopa**: apenas `mushroom_stew` (sopa de cogumelo). Item segurado pelo mouse com o inventário aberto não entra na contagem.
- **Servidores**: `acceptableRemoteVersions = "*"` permite entrar em servidores sem o mod. O Forge precisa estar instalado no cliente.
- **Primeira compilação**: o ForgeGradle 2.1 baixa dependências de repositórios antigos; se algum endereço falhar, tente de novo ou veja o log para o repositório que mudou.
