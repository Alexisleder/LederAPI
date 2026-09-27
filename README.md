# LederAPI

Public Java API contracts for Alexisleder projects. This repository contains
interfaces, immutable models and Bukkit events only; plugin implementations stay
inside their respective private repositories.

## Modules

- `lederprotections-api`: stable integration contract for LederProtections.

LederAPI is a Java library, **not a Minecraft plugin**. Do not place its JAR in
the server's `plugins` directory and do not shade or relocate it into consuming
plugins. LederProtections provides the API classes and implementation at runtime.

## Requirements

- Java 21 for the Minecraft 1.21.x baseline
- Java 25 for a Minecraft 26.1+ development environment
- Bukkit/Spigot/Paper 1.21.x or 26.x compatible API
- LederProtections itself remains the runtime provider

## Local build

```bash
./gradlew clean build
./gradlew publishToMavenLocal
```

On Windows use `gradlew.bat`.

The default artifact targets Java 21 and Spigot API 1.21.1 for the widest supported baseline. To verify the same public contract against the latest platform generation:

```bash
./gradlew clean build \
  -PtargetJavaVersion=25 \
  -PspigotApiVersion=26.3-R0.1-SNAPSHOT
```

## Consuming `lederprotections-api`

```groovy
repositories {
    mavenLocal()
}

dependencies {
    compileOnly('com.alexisleder:lederprotections-api:1.0.0')
}
```

Declare `LederProtections` as `softdepend` or `depend`, then load the service
directly from Bukkit's `ServicesManager`:

```java
LederProtectionsApi api = Bukkit.getServicesManager().load(LederProtectionsApi.class);
if (api == null) {
    return;
}
```

All API methods document their threading and failure behavior. Unless a method
explicitly says otherwise, invoke it from the server's primary thread.

## Compatibility and versioning

The artifact version follows semantic versioning. The API also exposes
`LederProtectionsApi.API_VERSION` so integrations can verify the runtime
contract without depending on the private plugin version.

The public contract is intentionally built from public Bukkit/Spigot types only.
Its source is compile-verified against both Spigot API 1.21.1 with Java 21 and
Spigot API 26.3 with Java 25.

## License

Licensed under the Apache License 2.0. See [LICENSE](LICENSE).
