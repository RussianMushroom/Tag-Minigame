# Tag-Minigame
Adds a game of Tag to your server.

## How to play:
Use `/tag join` to join the Lobby, which along with the arena and rip-zone should have been set with the command `/tag set [arena | lobby | rip]`. Upon joining the Lobby, each player should interact with a sign which emulates their "vote" to start the game. Once all players have voted, the game will start.
The last player standing will receive a cash reward if the server has the plugin "Essentials" installed. A randomised reward is also obtainable with the command `/tag reward`.

## Commands

| Commands   | Description   | Permission   |
|------------|---------------|--------------|
|tag|Displays the current status of the game as well as some additional commands.||
|tag join    |Player joins the Lobby with this command. |tag.join      |
|tag start   |**[Admin]** Force starts a game of Tag with the players that have joined.|tag.start     |
|tag leave   |Allows a player to leave a game of Tag, forfeiting their chance at the reward.|tag.leave|
|tag stop  |**[Admin]** Force stops a game of Tag, opening a new one. |tag.stop            	|
|tag help      |Displays a helpboard with useful information to the minigame. |tag.help            	|
|tag leaderboard|Displays all the past winners of Tag|tag.leaderboard|

**NB!!**: `/tag leaderboard` is an upcoming addition and does not feature in the initial release (v1.0.0).

## Additional Features

- All players' inventories are saved before they are cleared and loaded once the game is over.
- The config file contains externalised text so as to make the game customisable for the server.

## Setting Up

In order to make sure that this plugin runs smoothly on the server, make sure that the following points have been applied accordingly:

- Set up the Lobby, Arena and RIP-Zone, respectively, with the following commands:
  - `/tag set [arena | lobby | rip]`
- Make sure that there is a sign in the Lobby with the necessary tag (Default: **[VOTE]**).
  - This enables user-voting in the Lobby.
- Make sure that there is a sign in the Arena with the necessary tag (Default: **[UPGRADE]**).
  - There may be numerous upgrade signs situated throughout the Arena. This allows taggers to receive upgrades that give them the upper hand.
  
  ## Credits
  @ValkieYria: for bringing up the idea and being an active and willing member of the development.
