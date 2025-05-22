package com.luchacanaria.features.auth.domain.model

enum class Permission {
    // Read permissions
    READ_WRESTLERS,
    READ_TEAMS,
    READ_COMPETITIONS,
    READ_MATCHES,
    READ_STATISTICS,

    // Create permissions
    CREATE_WRESTLERS,
    CREATE_TEAMS,
    CREATE_COMPETITIONS,
    CREATE_MATCHES,

    // Management permissions
    MANAGE_WRESTLERS,
    MANAGE_TEAMS,
    MANAGE_COMPETITIONS,
    MANAGE_MATCHES,
    MANAGE_MATCH_ACTS,
    MANAGE_TEAM_WRESTLERS,

    // Admin permissions
    DELETE_ANY,
    MANAGE_USERS,
    SYSTEM_ADMINISTRATION
}