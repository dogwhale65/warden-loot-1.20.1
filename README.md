# Warden Loot Mod

![Minecraft 1.20.1](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen.svg)
![Fabric](https://img.shields.io/badge/Mod%20Loader-Fabric-blue.svg)

A Fabric mod for Minecraft 1.20.1 that makes Wardens drop random Epic or Rare items when killed.

## Features

- **Automatic Loot Drops**: When a Warden is killed, it will drop one random Epic or Rare item
- **Configurable Everything**: JSON config file to customize drops, exclusions, and behavior
- **Performance Optimized**: Items are cached at startup for optimal performance
- **Server-Side Compatible**: Works on both single-player and multiplayer servers

## Configuration

The mod creates a config file at `config/warden-loot.json` with the following options:

```json
{
  "excludedItems": [
    "minecraft:dragon_egg",
    "minecraft:command_block",
    "minecraft:chain_command_block",
    "minecraft:repeating_command_block",
    "minecraft:structure_block",
    "minecraft:jigsaw",
    "minecraft:barrier",
    "minecraft:light",
    "minecraft:debug_stick",
    "minecraft:knowledge_book",
    "minecraft:structure_void",
    "minecraft:minecart_with_command_block",
    "minecraft:minecart_with_repeating_command_block",
    "minecraft:minecart_with_chain_command_block"
  ],
  "enableDrops": true,
  "dropChance": 100,
  "logDrops": false
}
```

### Configuration Options

- **`excludedItems`**: List of item IDs to exclude from drops (format: `"minecraft:item_name"`)
- **`enableDrops`**: Whether Wardens should drop items at all (`true`/`false`)
- **`dropChance`**: Percentage chance for drops (0-100, where 100 = always drop)
- **`logDrops`**: Whether to log drops to the console (`true`/`false`)

### Adding/Removing Items from Exclusions

To exclude additional items, add them to the `excludedItems` list:

```json
"excludedItems": [
  "minecraft:dragon_egg",
  "minecraft:netherite_sword"
]
```

To allow previously excluded items, simply remove them from the list.

## Examples of Items That Can Drop

**Epic Items (Purple rarity):**
- Enchanted Golden Apples

**Rare Items (Yellow rarity):**
- Golden Apples
- Music Discs

**Excluded by Default:**
- Dragon Egg
- Command blocks
- Structure blocks
- Creative-only items

## Dependencies

- **Fabric API** (required)

## License

This mod is released under the CC0-1.0 license