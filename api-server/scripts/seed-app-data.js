// MongoDB Sample Data Seeding Script - Based on App Models
// Run with: mongosh lucha_canaria < seed-app-data.js

// Switch to the correct database
use lucha_canaria;

print("Starting to seed sample data based on app models...");

// ========== USERS ==========
// Password for all users: "Password123!" (pre-hashed with SHA-256 then BCrypt)
// The BCrypt hash below is for the SHA-256 hash: "TGexM+itEKiubiNkEbs+IqkwCOa3A/BNaF8TxDj1zUk="
const samplePasswordHash = "$2a$12$iK1ao/XYT0dSrTKlKLvmqu577HeqX/ioUSF.gGH4vMaq9z1.Bvzdy";

// Sample Admin/Federative Delegate
db.users.insertOne({
    email: "admin@luchacanaria.es",
    password: samplePasswordHash,
    name: "Carlos",
    surname: "González Pérez",
    phone: "+34 666 123 456",
    role: "FEDERATIVE_DELEGATE",
    permissions: [
        "MANAGE_USERS",
        "MANAGE_TEAMS", 
        "MANAGE_WRESTLERS",
        "MANAGE_COMPETITIONS",
        "MANAGE_MATCHES",
        "MANAGE_REFEREES",
        "VIEW_ALL",
        "MANAGE_FAVORITES"
    ],
    associatedTeamId: null,
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date(),
    lastLogin: new Date()
});

// Sample Coach
const coachUser = db.users.insertOne({
    email: "entrenador@uniontejina.es",
    password: samplePasswordHash,
    name: "Miguel",
    surname: "Rodríguez López",
    phone: "+34 666 234 567",
    role: "COACH",
    permissions: [
        "MANAGE_OWN_TEAM",
        "MANAGE_OWN_WRESTLERS",
        "VIEW_COMPETITIONS",
        "VIEW_MATCHES",
        "MANAGE_FAVORITES"
    ],
    associatedTeamId: null, // Will be updated after team creation
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date(),
    lastLogin: null
});

// Sample Regular User
db.users.insertOne({
    email: "aficionado@gmail.com",
    password: samplePasswordHash,
    name: "Ana",
    surname: "Martín Díaz",
    phone: null,
    role: "REGISTERED_USER",
    permissions: [
        "VIEW_COMPETITIONS",
        "VIEW_TEAMS",
        "VIEW_WRESTLERS",
        "VIEW_MATCHES",
        "MANAGE_FAVORITES"
    ],
    associatedTeamId: null,
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date(),
    lastLogin: null
});

// ========== TEAMS ==========
const team1 = db.teams.insertOne({
    name: "C.L. Unión Tejina",
    imageUrl: "https://example.com/teams/union-tejina.jpg",
    island: "TENERIFE",
    venue: "Terrero de Tejina",
    divisionCategory: "PRIMERA",
    // Additional fields for API compatibility
    alias: "Unión Tejina",
    municipality: "San Cristóbal de La Laguna",
    address: "Calle La Palmita, 15, Tejina",
    phone: "+34 922 540 123",
    email: "info@uniontejina.es",
    foundedYear: 1978,
    presidentName: "Juan Alberto Pérez González",
    colors: ["Azul", "Blanco"],
    logoUrl: "https://example.com/teams/union-tejina.jpg",
    stadiumName: "Terrero de Tejina",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
});

const team2 = db.teams.insertOne({
    name: "C.L. Campitos",
    imageUrl: "https://example.com/teams/campitos.jpg",
    island: "GRAN_CANARIA",
    venue: "Terrero López Socas",
    divisionCategory: "PRIMERA",
    // Additional fields for API compatibility
    alias: "Campitos",
    municipality: "Las Palmas de Gran Canaria",
    address: "Avenida de Escaleritas, 85",
    phone: "+34 928 251 234",
    email: "club@campitos.es",
    foundedYear: 1945,
    presidentName: "María del Carmen Suárez Medina",
    colors: ["Verde", "Blanco"],
    logoUrl: "https://example.com/teams/campitos.jpg",
    stadiumName: "Terrero López Socas",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
});

// Update coach's team association
db.users.updateOne(
    { _id: coachUser.insertedId },
    { $set: { associatedTeamId: team1.insertedId.toString() } }
);

// ========== WRESTLERS ==========
// Wrestler from Team 1 - Puntal A
const wrestler1 = db.wrestlers.insertOne({
    licenseNumber: "TF-2024-001",
    name: "Pedro",
    surname: "González Hernández",
    imageUrl: "https://example.com/wrestlers/pedro-gonzalez.jpg",
    teamId: team1.insertedId.toString(),
    category: "REGIONAL",
    classification: "PUNTAL_A",
    height: 185,
    weight: 92,
    birthDate: new Date("1995-03-15"),
    nickname: "El Palmero",
    // Additional fields for API compatibility
    phone: "+34 666 345 678",
    island: "TENERIFE",
    isActive: true,
    notes: "Campeón insular 2023, especialista en media cadera",
    createdAt: new Date(),
    updatedAt: new Date()
});

// Wrestler from Team 2 - Destacado A
const wrestler2 = db.wrestlers.insertOne({
    licenseNumber: "GC-2024-015",
    name: "Francisco",
    surname: "Santana Pérez",
    imageUrl: "https://example.com/wrestlers/paco-santana.jpg",
    teamId: team2.insertedId.toString(),
    category: "REGIONAL",
    classification: "DESTACADO_A",
    height: 178,
    weight: 85,
    birthDate: new Date("1998-07-22"),
    nickname: "Paco Santana",
    // Additional fields for API compatibility
    phone: "+34 666 456 789",
    island: "GRAN_CANARIA",
    isActive: true,
    notes: "Subcampeón regional 2023",
    createdAt: new Date(),
    updatedAt: new Date()
});

// Young wrestler - Juvenil
const wrestler3 = db.wrestlers.insertOne({
    licenseNumber: "TF-2024-050",
    name: "David",
    surname: "Rodríguez López",
    imageUrl: null,
    teamId: team1.insertedId.toString(),
    category: "JUVENIL",
    classification: "NONE",
    height: 170,
    weight: 68,
    birthDate: new Date("2008-11-10"),
    nickname: null,
    // Additional fields for API compatibility
    phone: null,
    island: "TENERIFE",
    isActive: true,
    notes: "Promesa juvenil, buen trabajo de piernas",
    createdAt: new Date(),
    updatedAt: new Date()
});

// ========== REFEREES ==========
const referee1 = db.referees.insertOne({
    name: "Antonio García Martín",
    licenseNumber: "ARB-NAC-001",
    isMain: true,
    isActive: true,
    // Additional fields for API compatibility
    dni: "43123456A",
    phone: "+34 666 567 890",
    email: "agarcia@arbitros.luchacanaria.es",
    category: "NACIONAL",
    island: "TENERIFE",
    createdAt: new Date(),
    updatedAt: new Date()
});

const referee2 = db.referees.insertOne({
    name: "José Luis Pérez Díaz",
    licenseNumber: "ARB-REG-015",
    isMain: false,
    isActive: true,
    // Additional fields for API compatibility
    dni: "43234567B",
    phone: "+34 666 678 901",
    email: "jlperez@arbitros.luchacanaria.es",
    category: "REGIONAL",
    island: "GRAN_CANARIA",
    createdAt: new Date(),
    updatedAt: new Date()
});

// ========== COMPETITIONS ==========
const matchDay1Id = new ObjectId();
const matchDay2Id = new ObjectId();

const competition = db.competitions.insertOne({
    name: "Liga Regional de Primera División",
    ageCategory: "REGIONAL",
    divisionCategory: "PRIMERA",
    island: "TENERIFE", // Main island for the competition
    season: "2024",
    // matchDays and teams are typically loaded separately in the app
    // For API compatibility, we'll add the full structure
    startDate: new Date("2024-01-15"),
    endDate: new Date("2024-06-30"),
    description: "Competición regional de máxima categoría con los mejores equipos de Canarias",
    rules: "Sistema de liga todos contra todos, ida y vuelta. 3 puntos por victoria, 1 por empate.",
    participatingTeamIds: [
        team1.insertedId.toString(),
        team2.insertedId.toString()
    ],
    matchDays: [
        {
            id: matchDay1Id,
            number: 1,
            date: new Date("2024-01-20"),
            description: "Primera jornada de liga",
            matchIds: []
        },
        {
            id: matchDay2Id,
            number: 2,
            date: new Date("2024-01-27"),
            description: "Segunda jornada de liga",
            matchIds: []
        }
    ],
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
});

// ========== MATCHES ==========
const match1 = db.matches.insertOne({
    // App model fields
    localTeamId: team1.insertedId.toString(),
    visitorTeamId: team2.insertedId.toString(),
    localScore: 6,
    visitorScore: 6,
    date: new Date("2024-01-20T20:00:00"),
    venue: "Terrero de Tejina",
    completed: true,
    hasAct: true,
    // Additional fields for API compatibility
    competitionId: competition.insertedId.toString(),
    matchDayId: matchDay1Id.toString(),
    scheduledTime: "20:00",
    status: "COMPLETED",
    matchActId: null, // Will be set after creating match act
    refereeIds: [
        referee1.insertedId.toString(),
        referee2.insertedId.toString()
    ],
    statistics: {
        localScore: 6,
        visitorScore: 6,
        individualMatches: 12,
        localFalls: 8,
        visitorFalls: 7,
        draws: 2,
        duration: 95
    },
    createdAt: new Date(),
    updatedAt: new Date()
});

const match2 = db.matches.insertOne({
    // App model fields
    localTeamId: team2.insertedId.toString(),
    visitorTeamId: team1.insertedId.toString(),
    localScore: null,
    visitorScore: null,
    date: new Date("2024-01-27T19:30:00"),
    venue: "Terrero López Socas",
    completed: false,
    hasAct: false,
    // Additional fields for API compatibility
    competitionId: competition.insertedId.toString(),
    matchDayId: matchDay2Id.toString(),
    scheduledTime: "19:30",
    status: "SCHEDULED",
    matchActId: null,
    refereeIds: [
        referee1.insertedId.toString()
    ],
    statistics: null,
    createdAt: new Date(),
    updatedAt: new Date()
});

// ========== MATCH ACTS ==========
const matchAct = db.matchActs.insertOne({
    // App model fields
    matchId: match1.insertedId.toString(),
    competitionId: competition.insertedId.toString(),
    competitionName: "Liga Regional de Primera División",
    season: "2024",
    category: "REGIONAL",
    isRegional: true,
    isInsular: false,
    venue: "Terrero de Tejina",
    date: new Date("2024-01-20"),
    startTime: new Date("2024-01-20T20:00:00"),
    endTime: new Date("2024-01-20T21:35:00"),
    
    mainReferee: {
        id: referee1.insertedId.toString(),
        name: "Antonio García Martín",
        licenseNumber: "ARB-NAC-001",
        isMain: true,
        isActive: true
    },
    
    assistantReferees: [
        {
            id: referee2.insertedId.toString(),
            name: "José Luis Pérez Díaz",
            licenseNumber: "ARB-REG-015",
            isMain: false,
            isActive: true
        }
    ],
    
    fieldDelegate: {
        name: "Juan Manuel Rodríguez",
        dni: "43345678C"
    },
    
    localTeam: {
        teamId: team1.insertedId.toString(),
        clubName: "C.L. Unión Tejina",
        wrestlers: [
            {
                wrestlerId: wrestler1.insertedId.toString(),
                licenseNumber: "TF-2024-001",
                number: 1
            }
        ],
        captain: {
            wrestlerId: wrestler1.insertedId.toString(),
            licenseNumber: "TF-2024-001",
            number: 1
        },
        coach: {
            name: "Miguel Rodríguez López",
            licenseNumber: "ENT-2024-001"
        }
    },
    
    visitorTeam: {
        teamId: team2.insertedId.toString(),
        clubName: "C.L. Campitos",
        wrestlers: [
            {
                wrestlerId: wrestler2.insertedId.toString(),
                licenseNumber: "GC-2024-015",
                number: 1
            }
        ],
        captain: {
            wrestlerId: wrestler2.insertedId.toString(),
            licenseNumber: "GC-2024-015",
            number: 1
        },
        coach: {
            name: "Fernando Suárez García",
            licenseNumber: "ENT-2024-010"
        }
    },
    
    bouts: [
        {
            id: new ObjectId(),
            localWrestler: {
                wrestlerId: wrestler1.insertedId.toString(),
                licenseNumber: "TF-2024-001",
                number: 1
            },
            visitorWrestler: {
                wrestlerId: wrestler2.insertedId.toString(),
                licenseNumber: "GC-2024-015",
                number: 1
            },
            localFalls: 1,
            visitorFalls: 1,
            localPenalties: 0,
            visitorPenalties: 0,
            winner: "DRAW",
            order: 1
        }
    ],
    
    localTeamScore: 6,
    visitorTeamScore: 6,
    isDraft: false,
    isCompleted: true,
    isSigned: true,
    localTeamComments: "Buena luchada, muy equilibrada",
    visitorTeamComments: "De acuerdo con el resultado",
    refereeComments: "Sin incidencias",
    
    // Additional fields for API compatibility
    time: "20:00",
    referee: {
        id: referee1.insertedId.toString(),
        name: "Antonio García Martín",
        licenseNumber: "ARB-NAC-001",
        isMain: true
    },
    additionalReferees: [
        {
            id: referee2.insertedId.toString(),
            name: "José Luis Pérez Díaz",
            licenseNumber: "ARB-REG-015",
            isMain: false
        }
    ],
    matchBouts: [
        {
            id: new ObjectId(),
            localWrestler: {
                wrestlerId: wrestler1.insertedId.toString(),
                licenseNumber: "TF-2024-001",
                number: 1
            },
            visitorWrestler: {
                wrestlerId: wrestler2.insertedId.toString(),
                licenseNumber: "GC-2024-015",
                number: 1
            },
            falls: [
                {
                    id: new ObjectId(),
                    type: "REGULAR",
                    winner: "LOCAL",
                    time: "00:45",
                    inSeparation: false
                },
                {
                    id: new ObjectId(),
                    type: "REGULAR",
                    winner: "VISITOR",
                    time: "02:30",
                    inSeparation: false
                }
            ],
            penalties: [],
            winner: "DRAW",
            order: 1
        }
    ],
    finalScore: {
        local: 6,
        visitor: 6
    },
    observations: "Luchada muy equilibrada. Gran ambiente en el terrero.",
    createdAt: new Date(),
    updatedAt: new Date()
});

// Update match with the match act ID
db.matches.updateOne(
    { _id: match1.insertedId },
    { $set: { matchActId: matchAct.insertedId.toString() } }
);

// Update competition match days with match IDs
db.competitions.updateOne(
    { _id: competition.insertedId },
    { 
        $set: { 
            "matchDays.0.matchIds": [match1.insertedId.toString()],
            "matchDays.1.matchIds": [match2.insertedId.toString()]
        }
    }
);

print("Sample data seeded successfully!");
print("=================================");
print("Created:");
print("- 3 users (1 admin, 1 coach, 1 regular user)");
print("- 2 teams (with app model fields)");
print("- 3 wrestlers (with app model fields)");
print("- 2 referees (with app model fields)");
print("- 1 competition (with proper structure)");
print("- 2 matches (1 completed, 1 scheduled)");
print("- 1 match act (with full bout details)");
print("=================================");
print("Default password for all users: Password123!");
print("To add more data, copy and modify the examples above.");