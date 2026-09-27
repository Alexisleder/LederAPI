package com.alexisleder.lederprotections.api.model;

/**
 * Rol público de acceso dentro de una protección.
 */
public enum ProtectionAccessRole {

    ADMIN,
    OWNER,
    CO_OWNER,
    MEMBER,
    VISITOR,
    BLOCKED;

    /**
     * Indica si el rol puede usar funciones reservadas a miembros.
     *
     * @return true para admin, dueño, codueño o miembro
     */
    public boolean isMemberOrHigher() {
        return this == ADMIN || this == OWNER || this == CO_OWNER || this == MEMBER;
    }
}
