package com.alexisleder.lederprotections.api.model;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Vista pública e inmutable de una protección.
 *
 * <p>No incluye identificadores de WorldGuard, reglas internas ni objetos de
 * persistencia. El campo {@link #id()} es la identidad estable que deben guardar
 * los addons.</p>
 *
 * @param id identificador persistente
 * @param displayName nombre visible
 * @param typeId tipo de protección
 * @param ownerId UUID del dueño
 * @param ownerName último nombre conocido del dueño
 * @param coOwnerIds UUID de codueños
 * @param memberIds UUID de miembros
 * @param blockedPlayerIds UUID bloqueados
 * @param bounds límites actuales
 * @param stoneX X de la piedra
 * @param stoneY Y de la piedra
 * @param stoneZ Z de la piedra
 * @param teleportHome home personalizado, o vacío si usa el fallback de la piedra
 * @param createdAt marca temporal de creación
 */
public record ProtectionSnapshot(
        String id,
        String displayName,
        String typeId,
        UUID ownerId,
        String ownerName,
        Set<UUID> coOwnerIds,
        Set<UUID> memberIds,
        Set<UUID> blockedPlayerIds,
        ProtectionBoundsSnapshot bounds,
        int stoneX,
        int stoneY,
        int stoneZ,
        Optional<ProtectionTeleportHomeSnapshot> teleportHome,
        long createdAt
) {

    /** Crea un snapshot defensivo. */
    public ProtectionSnapshot {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(displayName, "displayName");
        Objects.requireNonNull(typeId, "typeId");
        Objects.requireNonNull(ownerId, "ownerId");
        Objects.requireNonNull(ownerName, "ownerName");
        Objects.requireNonNull(coOwnerIds, "coOwnerIds");
        Objects.requireNonNull(memberIds, "memberIds");
        Objects.requireNonNull(blockedPlayerIds, "blockedPlayerIds");
        Objects.requireNonNull(bounds, "bounds");
        Objects.requireNonNull(teleportHome, "teleportHome");

        coOwnerIds = Set.copyOf(coOwnerIds);
        memberIds = Set.copyOf(memberIds);
        blockedPlayerIds = Set.copyOf(blockedPlayerIds);
    }

    /**
     * Obtiene el mundo de la protección.
     *
     * @return nombre del mundo
     */
    public String worldName() {
        return bounds.worldName();
    }
}
