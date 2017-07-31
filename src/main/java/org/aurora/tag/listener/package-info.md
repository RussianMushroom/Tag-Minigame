# Listeners

## onEntityDamageByEntity
Deals with the punches thrown by entites. Checks to see whether Player used a stick which consitutes a valid tag.

## onPlayerInteract
Checks whether Player has interacted with signs that either have the tags `[VOTE]` or `[UPGRADE]`.
Fires up the respective methods upon interaction.

## onPlayerTeleportEvent
Prevents Player from teleporting while involved in a Tag game. 

### Bugs
Prevents warping too.

## onPlayerGameModeChange
Prevents Player from changing their game mode while involved in a Tag game.

### Bugs
Cannot remove "{player} has been teleported to {xxx.yyy.zzz}" default message.

## onPlayerKickEvent, onEntityDeathEvent, onPlayerQuitEvent
Deal with Players leaving the Tag game without using the /tag leave command.
