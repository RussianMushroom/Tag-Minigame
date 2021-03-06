# TagMinigame
Adds a game of Tag to your server.
As of `1.5.5` one can now create muliple instances of a Tag game which allows for the creation of different arenas.

## How to play:
Use `/tag join [arena name]` (Or a sign, see below) to join the Lobby, which along with the arena and rip-zone should have been set with the command `/tag set [arena | lobby | rip] [arena name]`. Upon joining the Lobby, each player should interact with a sign which emulates their "vote" to start the game. Once all players have voted, the game will start.
The last player standing will receive a cash reward if the server has the plugin "Essentials" installed. Furthermore, with the plugin "RewardLib" the winner receives a credit that they can redeem for an item prize.

## Commands

| Commands   | Description   | Permission   | Implemented |
|------------|---------------|--------------|------------|
|tag|Displays the current status of the game as well as some additional commands.||:white_check_mark:|
|tag join [arena name]   |Player joins the Lobby with this command. |tag.join      |:white_check_mark:|
|tag start [arena name]  |**[Admin]** Force starts a game of Tag with the players that have joined.|tag.start     |:white_check_mark:|
|tag leave   |Allows a player to leave a game of Tag, forfeiting their chance at the reward.|tag.leave|:white_check_mark:|
|tag stop [arena name]  |**[Admin]** Force stops a game of Tag, opening a new one. |tag.stop            	|:white_check_mark:|
|tag help      |Displays a helpboard with useful information to the minigame. |tag.help            	|:white_check_mark:|
|tag leaderboard|Displays all the past winners of Tag|tag.leaderboard|:white_check_mark:|
|tag createarena|**[Admin]** Creates a new arena if it does not already exist|tag.createarena|:white_check_mark:|
|tag status [arena name]|Displays whether the selected Tag arena is open.|tag.status|:white_check_mark:|

## Additional Features

- ~~All players' inventories are saved before they are cleared and loaded once the game is over.~~ This was removed due to severe bugs.
- The config file contains externalised text so as to make the game customisable for the server.
- `/tag leaderboard` keeps track of all the players' wins and losses.
- Holographic countdown, requires **ProtocolLib!**

## Setting Up

In order to make sure that this plugin runs smoothly on the server, make sure that the following points have been applied accordingly:

- Set up the arena with the command `/tag createarena [arena name]`. Each arena starts with a maximum amount of players set to 10.
- Set up the Lobby, Arena and RIP-Zone, respectively, with the following commands:
  - `/tag set [arena | lobby | rip] [arena name]`
- Make sure that there is a sign in the Lobby with the necessary tag (Default: **[VOTE]**).
  - This enables user-voting in the Lobby.
  - You can also set a sign for leaving (Default: **[LEAVE]**) to forego the command.
- Make sure that there is a sign in the Arena with the necessary tag (Default: **[UPGRADE]**).
  - There may be numerous upgrade signs situated throughout the Arena. This allows taggers to receive upgrades that give them the upper hand.
  
## Upcoming Features

- ~~MySQL support to save players' wins as credits that can be redeemed for item prizes.~~ 
  - This is now to be supported by a new plugin called RewardLib (Adds global support for reward systems).
- ~~Support for multiple arenas. Create an arena using `/tag createarena [arena name]`.~~ 
  - Supported since 1.5.5.
  
## Credits

@ValkieYria: for bringing up the idea and being an active and willing member of the development.
