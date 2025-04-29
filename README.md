# Warden Farm Preventer Plugin

![Minecraft](https://img.shields.io/badge/Minecraft-1.19+-brightgreen) ![License](https://img.shields.io/badge/License-MIT-blue)

A simple yet effective Minecraft plugin designed to prevent players from building Warden farms by relocating Sculk Shriekers whenever a Warden spawns.

## Features

- **Automatic Sculk Shrieker Relocation**: Moves Sculk Shriekers to random locations in the Deep Dark biome when a Warden spawns
- **Configurable Parameters**: Customize search radius, height limits, and maximum attempts
- **Efficient Detection**: Quickly identifies nearby Sculk Shriekers when Wardens spawn
- **Thread-Safe Operations**: Uses asynchronous tasks for location searching to prevent server lag
- **Biome Verification**: Ensures new locations are in the correct Deep Dark biome

## Installation

1. Download the latest `.jar` file from the [Releases](https://github.com/yourusername/warden-farm-preventer/releases) section
2. Place the `.jar` file in your server's `plugins/` directory
3. Restart your server (or use `/reload` if you must)

## Configuration

The plugin creates a `config.yml` file with these default settings:

```yaml
# Radius to search for new locations (blocks)
search-radius: 50

# Minimum Y level to search (-64 for deepslate layers)
min-y: -64

# Maximum Y level to search (0 for ancient city level)
max-y: 0

# Maximum attempts to find a suitable location
max-attempts: 20
```

Edit these values in `plugins/WardenFarmPreventer/config.yml` and reload the plugin with `/reload` or restart your server.

## How It Works

1. When a Warden spawns, the plugin checks for nearby Sculk Shriekers
2. If found, it removes the Sculk Shrieker
3. The plugin searches for a random valid location in the Deep Dark biome:
   - Within configured radius and height limits
   - Must be in Deep Dark biome
   - Must have solid block below and air above
4. Places a new Sculk Shrieker at the found location

This effectively prevents players from creating Warden farms by:
- Breaking any established Sculk Shrieker positions
- Randomizing Sculk Shrieker locations
- Maintaining game balance while allowing natural Warden spawns

## Compatibility

- Tested with Minecraft 1.19+ (Warden introduced versions)
- Works with both Paper and Spigot servers
- Should work with most other plugins (no known conflicts)

## Commands & Permissions

Currently, the plugin works automatically without any commands or special permissions.

## Support

If you encounter any issues or have suggestions:
- Open an issue on [GitHub](https://github.com/b31556/wardenfarmprevention/issues)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your improvements.

---

**Happy mining!** ⛏️
