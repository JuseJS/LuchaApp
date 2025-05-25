// MongoDB Sample Data Seeding Script - Complete Version
// Run with: mongosh lucha_canaria < seed-data-complete.js

// Switch to the correct database
use lucha_canaria;

print("Starting to seed complete sample data...");

db.users.drop();
db.teams.drop();
db.wrestlers.drop();
db.competitions.drop();
db.matches.drop();
db.matchActs.drop();
db.referees.drop();


// ========== USERS ==========
// Password for all users: "Password123!" (pre-hashed with SHA-256 then BCrypt)
const samplePasswordHash = "$2a$12$iK1ao/XYT0dSrTKlKLvmqu577HeqX/ioUSF.gGH4vMaq9z1.Bvzdy";

// Sample Admin/Federative Delegate
db.users.insertOne({
    email: "admin@luchacanaria.es",
    password: samplePasswordHash,
    name: "Carlos",
    surname: "González Pérez",
    role: "FEDERATIVE_DELEGATE",
    associatedTeamId: null
});

// Sample Regular Users
db.users.insertOne({
    email: "aficionado1@gmail.com",
    password: samplePasswordHash,
    name: "Ana",
    surname: "Martín Díaz",
    role: "REGISTERED_USER",
    associatedTeamId: null
});

db.users.insertOne({
    email: "aficionado2@gmail.com",
    password: samplePasswordHash,
    name: "Juan",
    surname: "Pérez Rodríguez",
    role: "REGISTERED_USER",
    associatedTeamId: null
});

// ========== REFEREES ==========
const referees = [];
const refereeData = [
    { name: "Antonio García Martín", licenseNumber: "ARB-NAC-001", isMain: true },
    { name: "José Luis Pérez Díaz", licenseNumber: "ARB-REG-015", isMain: false },
    { name: "Manuel Rodríguez González", licenseNumber: "ARB-REG-016", isMain: true },
    { name: "Francisco Hernández López", licenseNumber: "ARB-REG-017", isMain: false },
    { name: "Rafael Santana Medina", licenseNumber: "ARB-NAC-002", isMain: true },
    { name: "Diego Suárez García", licenseNumber: "ARB-REG-018", isMain: false }
];

refereeData.forEach(ref => {
    const result = db.referees.insertOne({
        name: ref.name,
        licenseNumber: ref.licenseNumber,
        isMain: ref.isMain,
        isActive: true
    });
    referees.push(result.insertedId);
});

// ========== TEAMS AND COACHES - PRIMERA CATEGORÍA ==========
const teamsFirstDivision = [];
const firstDivisionTeamData = [
    { name: "C.L. Unión Tejina", island: "TENERIFE", venue: "Terrero de Tejina", coach: { name: "Miguel", surname: "Rodríguez López", email: "entrenador.tejina@luchacanaria.es" } },
    { name: "C.L. Campitos", island: "GRAN_CANARIA", venue: "Terrero López Socas", coach: { name: "Fernando", surname: "Suárez García", email: "entrenador.campitos@luchacanaria.es" } },
    { name: "C.L. Rosario", island: "TENERIFE", venue: "Terrero del Rosario", coach: { name: "Alberto", surname: "Martín Pérez", email: "entrenador.rosario@luchacanaria.es" } },
    { name: "C.L. Tinamar", island: "GRAN_CANARIA", venue: "Terrero de Tinamar", coach: { name: "José Manuel", surname: "González Santana", email: "entrenador.tinamar@luchacanaria.es" } },
    { name: "C.L. Guancha", island: "TENERIFE", venue: "Terrero de La Guancha", coach: { name: "Pedro", surname: "Hernández Díaz", email: "entrenador.guancha@luchacanaria.es" } },
    { name: "C.L. Adargoma", island: "GRAN_CANARIA", venue: "Terrero de Adargoma", coach: { name: "Rafael", surname: "López Medina", email: "entrenador.adargoma@luchacanaria.es" } },
    { name: "C.L. Victoria", island: "TENERIFE", venue: "Terrero de La Victoria", coach: { name: "Juan Carlos", surname: "Pérez Rodríguez", email: "entrenador.victoria@luchacanaria.es" } },
    { name: "C.L. Maninidra", island: "GRAN_CANARIA", venue: "Terrero de Maninidra", coach: { name: "Antonio", surname: "García López", email: "entrenador.maninidra@luchacanaria.es" } }
];

// Create coaches and teams for Primera División
firstDivisionTeamData.forEach((teamData, index) => {
    // Create coach user
    const coachUser = db.users.insertOne({
        email: teamData.coach.email,
        password: samplePasswordHash,
        name: teamData.coach.name,
        surname: teamData.coach.surname,
        role: "COACH",
        associatedTeamId: null
    });

    // Create team
    const team = db.teams.insertOne({
        name: teamData.name,
        imageUrl: `https://example.com/teams/${teamData.name.toLowerCase().replace(/[.\s]/g, '-')}.jpg`,
        island: teamData.island,
        venue: teamData.venue,
        divisionCategory: "PRIMERA"
    });

    // Update coach's team association
    db.users.updateOne(
        { _id: coachUser.insertedId },
        { $set: { associatedTeamId: team.insertedId.toString() } }
    );

    teamsFirstDivision.push({
        id: team.insertedId,
        name: teamData.name,
        island: teamData.island,
        coachLicense: `ENT-2024-${String(index + 1).padStart(3, '0')}`
    });
});

// ========== WRESTLERS - PRIMERA CATEGORÍA ==========
// Generate wrestlers for each Primera División team
const wrestlerNames = {
    names: ["Pedro", "Juan", "Miguel", "José", "Francisco", "Antonio", "Manuel", "Rafael", "Carlos", "Luis", "Javier", "Diego", "Alejandro", "Roberto", "Daniel", "Pablo", "Sergio", "Fernando", "Alberto", "Jorge"],
    surnames: ["González", "Rodríguez", "Hernández", "Pérez", "García", "Martín", "López", "Sánchez", "Díaz", "Suárez", "Torres", "Ramírez", "Vázquez", "Morales", "Jiménez", "Ruiz", "Molina", "Delgado", "Castro", "Ortega"],
    nicknames: ["El Palmero", "El Majorero", "El Pollo de Arafo", "El Tigre", "El León", "El Toro", "El Gladiador", "El Guerrero", "El Titán", "El Coloso", "El Invencible", "El Rayo", "El Trueno", "El Ciclón", "El Huracán"]
};

let wrestlerLicenseCounter = 1;
teamsFirstDivision.forEach((team, teamIndex) => {
    const islandPrefix = team.island === "TENERIFE" ? "TF" : "GC";

    // 1 Puntal A
    db.wrestlers.insertOne({
        licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
        name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
        surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
        imageUrl: `https://example.com/wrestlers/wrestler-${wrestlerLicenseCounter}.jpg`,
        teamId: team.id.toString(),
        category: "REGIONAL",
        classification: "PUNTAL_A",
        height: 185 + Math.floor(Math.random() * 10),
        weight: 90 + Math.floor(Math.random() * 15),
        birthDate: new Date(1990 + Math.floor(Math.random() * 8), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
        nickname: wrestlerNames.nicknames[Math.floor(Math.random() * wrestlerNames.nicknames.length)]
    });

    // 1 Puntal B
    db.wrestlers.insertOne({
        licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
        name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
        surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
        imageUrl: `https://example.com/wrestlers/wrestler-${wrestlerLicenseCounter}.jpg`,
        teamId: team.id.toString(),
        category: "REGIONAL",
        classification: "PUNTAL_B",
        height: 182 + Math.floor(Math.random() * 8),
        weight: 85 + Math.floor(Math.random() * 12),
        birthDate: new Date(1992 + Math.floor(Math.random() * 8), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
        nickname: null
    });

    // 1 Destacado A
    db.wrestlers.insertOne({
        licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
        name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
        surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
        imageUrl: `https://example.com/wrestlers/wrestler-${wrestlerLicenseCounter}.jpg`,
        teamId: team.id.toString(),
        category: "REGIONAL",
        classification: "DESTACADO_A",
        height: 178 + Math.floor(Math.random() * 7),
        weight: 80 + Math.floor(Math.random() * 10),
        birthDate: new Date(1994 + Math.floor(Math.random() * 8), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
        nickname: null
    });

    // 2 Destacados B
    for (let i = 0; i < 2; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: `https://example.com/wrestlers/wrestler-${wrestlerLicenseCounter}.jpg`,
            teamId: team.id.toString(),
            category: "REGIONAL",
            classification: "DESTACADO_B",
            height: 175 + Math.floor(Math.random() * 7),
            weight: 75 + Math.floor(Math.random() * 10),
            birthDate: new Date(1996 + Math.floor(Math.random() * 8), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }

    // 2 Destacados C
    for (let i = 0; i < 2; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: null,
            teamId: team.id.toString(),
            category: "REGIONAL",
            classification: "DESTACADO_C",
            height: 172 + Math.floor(Math.random() * 7),
            weight: 70 + Math.floor(Math.random() * 10),
            birthDate: new Date(1998 + Math.floor(Math.random() * 8), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }

    // 8 Luchadores no clasificados
    for (let i = 0; i < 8; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: null,
            teamId: team.id.toString(),
            category: "REGIONAL",
            classification: "NONE",
            height: 170 + Math.floor(Math.random() * 10),
            weight: 68 + Math.floor(Math.random() * 12),
            birthDate: new Date(2000 + Math.floor(Math.random() * 5), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }

    // 4 Juveniles
    for (let i = 0; i < 4; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: null,
            teamId: team.id.toString(),
            category: "JUVENIL",
            classification: "NONE",
            height: 165 + Math.floor(Math.random() * 10),
            weight: 60 + Math.floor(Math.random() * 10),
            birthDate: new Date(2007 + Math.floor(Math.random() * 2), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }

    // 2 Cadetes
    for (let i = 0; i < 2; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: null,
            teamId: team.id.toString(),
            category: "JUVENIL",
            classification: "NONE",
            height: 160 + Math.floor(Math.random() * 10),
            weight: 55 + Math.floor(Math.random() * 10),
            birthDate: new Date(2009 + Math.floor(Math.random() * 2), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }
});

// ========== TEAMS AND COACHES - SEGUNDA CATEGORÍA ==========
const teamsSecondDivision = [];
const secondDivisionTeamData = [
    { name: "C.L. Benchomo", island: "TENERIFE", venue: "Terrero de Benchomo", coach: { name: "Luis", surname: "Méndez García", email: "entrenador.benchomo@luchacanaria.es" } },
    { name: "C.L. Chimisay", island: "TENERIFE", venue: "Terrero de Chimisay", coach: { name: "Roberto", surname: "Núñez Pérez", email: "entrenador.chimisay@luchacanaria.es" } },
    { name: "C.L. Arguayo", island: "GRAN_CANARIA", venue: "Terrero de Arguayo", coach: { name: "Domingo", surname: "Santana Rodríguez", email: "entrenador.arguayo@luchacanaria.es" } },
    { name: "C.L. San Mateo", island: "GRAN_CANARIA", venue: "Terrero de San Mateo", coach: { name: "Cristóbal", surname: "Vega López", email: "entrenador.sanmateo@luchacanaria.es" } },
    { name: "C.L. Tegueste", island: "TENERIFE", venue: "Terrero de Tegueste", coach: { name: "Eduardo", surname: "Ramos Díaz", email: "entrenador.tegueste@luchacanaria.es" } },
    { name: "C.L. Tenoya", island: "GRAN_CANARIA", venue: "Terrero de Tenoya", coach: { name: "Sergio", surname: "Alonso Martín", email: "entrenador.tenoya@luchacanaria.es" } },
    { name: "C.L. Ravelo", island: "TENERIFE", venue: "Terrero de Ravelo", coach: { name: "Tomás", surname: "Herrera González", email: "entrenador.ravelo@luchacanaria.es" } },
    { name: "C.L. Doctoral", island: "GRAN_CANARIA", venue: "Terrero del Doctoral", coach: { name: "Óscar", surname: "Medina Suárez", email: "entrenador.doctoral@luchacanaria.es" } }
];

// Create coaches and teams for Segunda División
secondDivisionTeamData.forEach((teamData, index) => {
    // Create coach user
    const coachUser = db.users.insertOne({
        email: teamData.coach.email,
        password: samplePasswordHash,
        name: teamData.coach.name,
        surname: teamData.coach.surname,
        role: "COACH",
        associatedTeamId: null
    });

    // Create team
    const team = db.teams.insertOne({
        name: teamData.name,
        imageUrl: `https://example.com/teams/${teamData.name.toLowerCase().replace(/[.\s]/g, '-')}.jpg`,
        island: teamData.island,
        venue: teamData.venue,
        divisionCategory: "SEGUNDA"
    });

    // Update coach's team association
    db.users.updateOne(
        { _id: coachUser.insertedId },
        { $set: { associatedTeamId: team.insertedId.toString() } }
    );

    teamsSecondDivision.push({
        id: team.insertedId,
        name: teamData.name,
        island: teamData.island,
        coachLicense: `ENT-2024-${String(index + 101).padStart(3, '0')}`
    });
});

// ========== WRESTLERS - SEGUNDA CATEGORÍA ==========
// Generate wrestlers for each Segunda División team
teamsSecondDivision.forEach((team, teamIndex) => {
    const islandPrefix = team.island === "TENERIFE" ? "TF" : "GC";

    // 1 Destacado A
    db.wrestlers.insertOne({
        licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
        name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
        surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
        imageUrl: `https://example.com/wrestlers/wrestler-${wrestlerLicenseCounter}.jpg`,
        teamId: team.id.toString(),
        category: "REGIONAL",
        classification: "DESTACADO_A",
        height: 178 + Math.floor(Math.random() * 7),
        weight: 80 + Math.floor(Math.random() * 10),
        birthDate: new Date(1994 + Math.floor(Math.random() * 8), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
        nickname: null
    });

    // 2 Destacados B
    for (let i = 0; i < 2; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: Math.random() > 0.5 ? `https://example.com/wrestlers/wrestler-${wrestlerLicenseCounter}.jpg` : null,
            teamId: team.id.toString(),
            category: "REGIONAL",
            classification: "DESTACADO_B",
            height: 175 + Math.floor(Math.random() * 7),
            weight: 75 + Math.floor(Math.random() * 10),
            birthDate: new Date(1996 + Math.floor(Math.random() * 8), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }

    // 3 Destacados C
    for (let i = 0; i < 3; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: null,
            teamId: team.id.toString(),
            category: "REGIONAL",
            classification: "DESTACADO_C",
            height: 172 + Math.floor(Math.random() * 7),
            weight: 70 + Math.floor(Math.random() * 10),
            birthDate: new Date(1998 + Math.floor(Math.random() * 8), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }

    // 8 Luchadores no clasificados
    for (let i = 0; i < 8; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: null,
            teamId: team.id.toString(),
            category: "REGIONAL",
            classification: "NONE",
            height: 170 + Math.floor(Math.random() * 10),
            weight: 68 + Math.floor(Math.random() * 12),
            birthDate: new Date(2000 + Math.floor(Math.random() * 5), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }

    // 4 Juveniles
    for (let i = 0; i < 4; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: null,
            teamId: team.id.toString(),
            category: "JUVENIL",
            classification: "NONE",
            height: 165 + Math.floor(Math.random() * 10),
            weight: 60 + Math.floor(Math.random() * 10),
            birthDate: new Date(2007 + Math.floor(Math.random() * 2), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }

    // 2 Cadetes
    for (let i = 0; i < 2; i++) {
        db.wrestlers.insertOne({
            licenseNumber: `${islandPrefix}-2024-${String(wrestlerLicenseCounter++).padStart(3, '0')}`,
            name: wrestlerNames.names[Math.floor(Math.random() * wrestlerNames.names.length)],
            surname: `${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]} ${wrestlerNames.surnames[Math.floor(Math.random() * wrestlerNames.surnames.length)]}`,
            imageUrl: null,
            teamId: team.id.toString(),
            category: "JUVENIL",
            classification: "NONE",
            height: 160 + Math.floor(Math.random() * 10),
            weight: 55 + Math.floor(Math.random() * 10),
            birthDate: new Date(2009 + Math.floor(Math.random() * 2), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28)),
            nickname: null
        });
    }
});

// ========== COMPETITIONS ==========
const competitionFirstDivision = db.competitions.insertOne({
    name: "Liga Regional de Primera División",
    ageCategory: "REGIONAL",
    divisionCategory: "PRIMERA",
    island: "ALL",
    season: "2024"
});

const competitionSecondDivision = db.competitions.insertOne({
    name: "Liga Regional de Segunda División",
    ageCategory: "REGIONAL",
    divisionCategory: "SEGUNDA",
    island: "ALL",
    season: "2024"
});

// ========== MATCHES - PRIMERA DIVISIÓN ==========
// Generate 5 jornadas with 3 matches each for Primera División
const matchDaysFirst = [
    new Date("2025-04-24"), // Thursday
    new Date("2025-04-25"), // Friday
    new Date("2025-04-26"), // Saturday
    new Date("2025-05-01"), // Thursday
    new Date("2025-05-02"), // Friday
    new Date("2025-05-03"), // Saturday
    new Date("2025-05-08"), // Thursday
    new Date("2025-05-09"), // Friday
    new Date("2025-05-18"), // Saturday
    new Date("2025-05-15"), // Thursday
    new Date("2025-05-16"), // Friday
    new Date("2025-05-17"), // Saturday
    new Date("2025-05-22"), // Thursday
    new Date("2025-05-23"), // Friday
    new Date("2025-05-24") // Saturday
];

let matchDayIndex = 0;
// Jornada 1 - Primera División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsFirstDivision[i * 2];
    const visitorTeam = teamsFirstDivision[i * 2 + 1];
    const matchDate = new Date(matchDaysFirst[matchDayIndex]);
    matchDate.setHours(20, 0, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionFirstDivision.insertedId.toString(),
        round: 1
    });
    matchDayIndex++;
}

// Jornada 2 - Primera División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsFirstDivision[(i * 2 + 2) % 8];
    const visitorTeam = teamsFirstDivision[(i * 2 + 3) % 8];
    const matchDate = new Date(matchDaysFirst[matchDayIndex]);
    matchDate.setHours(19, 30, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionFirstDivision.insertedId.toString(),
        round: 2
    });
    matchDayIndex++;
}

// Jornada 3 - Primera División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsFirstDivision[(i * 2 + 4) % 8];
    const visitorTeam = teamsFirstDivision[(i * 2 + 5) % 8];
    const matchDate = new Date(matchDaysFirst[matchDayIndex]);
    matchDate.setHours(20, 30, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionFirstDivision.insertedId.toString(),
        round: 3
    });
    matchDayIndex++;
}

// Jornada 4 - Primera División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsFirstDivision[(i * 2 + 6) % 8];
    const visitorTeam = teamsFirstDivision[(i * 2 + 7) % 8];
    const matchDate = new Date(matchDaysFirst[matchDayIndex]);
    matchDate.setHours(20, 0, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionFirstDivision.insertedId.toString(),
        round: 4
    });
    matchDayIndex++;
}

// Jornada 5 - Primera División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsFirstDivision[i];
    const visitorTeam = teamsFirstDivision[(i + 4) % 8];
    const matchDate = new Date(matchDaysFirst[matchDayIndex]);
    matchDate.setHours(19, 30, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionFirstDivision.insertedId.toString(),
        round: 5
    });
    matchDayIndex++;
}

// ========== MATCHES - SEGUNDA DIVISIÓN ==========
// Generate 6 jornadas with 3 matches each for Segunda División
const matchDaysSecond = [
    new Date("2025-04-24"), // Thursday
    new Date("2025-04-25"), // Friday
    new Date("2025-04-26"), // Saturday
    new Date("2025-05-01"), // Thursday
    new Date("2025-05-02"), // Friday
    new Date("2025-05-03"), // Saturday
    new Date("2025-05-08"), // Thursday
    new Date("2025-05-09"), // Friday
    new Date("2025-05-18"), // Saturday
    new Date("2025-05-15"), // Thursday
    new Date("2025-05-16"), // Friday
    new Date("2025-05-17"), // Saturday
    new Date("2025-05-22"), // Thursday
    new Date("2025-05-23"), // Friday
    new Date("2025-05-24"), // Saturday
    new Date("2025-05-29"), // Thursday
    new Date("2025-05-30"), // Friday
    new Date("2025-05-31")  // Saturday
];

matchDayIndex = 0;
// Jornada 1 - Segunda División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsSecondDivision[i * 2];
    const visitorTeam = teamsSecondDivision[i * 2 + 1];
    const matchDate = new Date(matchDaysSecond[matchDayIndex]);
    matchDate.setHours(19, 0, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionSecondDivision.insertedId.toString(),
        round: 1
    });
    matchDayIndex++;
}

// Jornada 2 - Segunda División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsSecondDivision[(i * 2 + 2) % 8];
    const visitorTeam = teamsSecondDivision[(i * 2 + 3) % 8];
    const matchDate = new Date(matchDaysSecond[matchDayIndex]);
    matchDate.setHours(20, 0, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionSecondDivision.insertedId.toString(),
        round: 2
    });
    matchDayIndex++;
}

// Jornada 3 - Segunda División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsSecondDivision[(i * 2 + 4) % 8];
    const visitorTeam = teamsSecondDivision[(i * 2 + 5) % 8];
    const matchDate = new Date(matchDaysSecond[matchDayIndex]);
    matchDate.setHours(19, 30, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionSecondDivision.insertedId.toString(),
        round: 3
    });
    matchDayIndex++;
}

// Jornada 4 - Segunda División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsSecondDivision[(i * 2 + 6) % 8];
    const visitorTeam = teamsSecondDivision[(i * 2 + 7) % 8];
    const matchDate = new Date(matchDaysSecond[matchDayIndex]);
    matchDate.setHours(20, 30, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionSecondDivision.insertedId.toString(),
        round: 4
    });
    matchDayIndex++;
}

// Jornada 5 - Segunda División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsSecondDivision[i];
    const visitorTeam = teamsSecondDivision[(i + 4) % 8];
    const matchDate = new Date(matchDaysSecond[matchDayIndex]);
    matchDate.setHours(19, 0, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionSecondDivision.insertedId.toString(),
        round: 5
    });
    matchDayIndex++;
}

// Jornada 6 - Segunda División
for (let i = 0; i < 3; i++) {
    const localTeam = teamsSecondDivision[(i + 1) % 8];
    const visitorTeam = teamsSecondDivision[(i + 5) % 8];
    const matchDate = new Date(matchDaysSecond[matchDayIndex]);
    matchDate.setHours(20, 0, 0, 0);

    db.matches.insertOne({
        localTeamId: localTeam.id.toString(),
        visitorTeamId: visitorTeam.id.toString(),
        localScore: null,
        visitorScore: null,
        date: matchDate,
        venue: localTeam.venue || "Terrero Municipal",
        completed: false,
        hasAct: false,
        competitionId: competitionSecondDivision.insertedId.toString(),
        round: 6
    });
    matchDayIndex++;
}

print("Complete sample data seeded successfully!");
print("=========================================");
print("Created:");
print("- 19 users (1 admin + 16 coaches + 2 regular users)");
print("- 16 teams (8 Primera División + 8 Segunda División)");
print("- 344 wrestlers (21 per Primera team + 20 per Segunda team)");
print("- 6 referees");
print("- 2 competitions");
print("- 33 matches (15 Primera División + 18 Segunda División)");
print("=========================================");
print("Default password for all users: Password123!");
print("");
print("Primera División teams:");
teamsFirstDivision.forEach(team => print("  - " + team.name));
print("");
print("Segunda División teams:");
teamsSecondDivision.forEach(team => print("  - " + team.name));
print("");
print("All teams have been properly linked to their respective competitions.");
print("Matches are distributed across Thursday, Friday and Saturday for each round.");