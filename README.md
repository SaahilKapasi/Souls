# Souls

A plugin that allows players more leniancy with dying!

Players get a configurable amount of souls that will allow them to keep their items when dying in return for losing a soul. Souls regen over time, as long as the player is not in a blacklisted region. This means that you can make it so players only regen souls while they're outside of spawn, incentivizing them to play and not afk. The number of souls a currently has as well as the amount of time till their next soul is tracked and stored in a MySQL database.

Commands:
- /souls - Allows a player to check the number of souls they currently have
- /souls get <name> - An admin command to check how many souls another player currently has
- /souls set/add/remove <name> - Admin commands to modify the amount of souls a player has 
