# GPTFriend
GPT-enabled NPCs for Citizens!

## Why

It's fun and AI is cool! Play with it, see if you like it. It's free, so why not?

## Setup

GPTFriend is a [Paper](https://papermc.io/) plugin and Citizens add-on.
Required dependencies are [Citizens](https://www.spigotmc.org/resources/citizens.13811/).
Optional dependencies are [squaremap](https://github.com/jpenilla/squaremap) (optional API),
[UnifiedMetrics](https://github.com/Cubxity/UnifiedMetrics),
and [LuckPerms](https://luckperms.net/) (asynchronous permission check in some places).

## API

### Maven

```XML
<repositories>
    <repository>
        <id>egg82-nexus</id>
        <url>https://nexus.egg82.me/repository/maven-public/</url>
    </repository>
</repositories>
```

```XML
<dependencies>
    <dependency>
        <groupId>me.egg82</groupId>
        <artifactId>gptfriend-api</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Usage

Get an API instance
```Java
GPTAPI api = GPTAPIProvider.getInstance();
```

Get a MapManager instance
```Java
GPTAPI api = GPTAPIProvider.getInstance();
MapManager manager = api.getMapManager();
```

## License
This project is licensed under the GNU GPLv3, see [LICENSE](LICENSE) for details.