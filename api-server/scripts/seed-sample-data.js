// MongoDB Sample Data Seeding Script
// Run with: mongosh lucha_canaria < seed-sample-data.js

// Switch to the correct database
use lucha_canaria;

print("Starting to seed sample data...");

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
    name: "Club de Lucha Unión Tejina",
    alias: "Unión Tejina",
    island: "TENERIFE",
    municipality: "San Cristóbal de La Laguna",
    divisionCategory: "PRIMERA",
    address: "Calle La Palmita, 15, Tejina",
    phone: "+34 922 540 123",
    email: "info@uniontejina.es",
    foundedYear: 1978,
    presidentName: "Juan Alberto Pérez González",
    colors: ["Azul", "Blanco"],
    logoUrl: "https://example.com/logos/union-tejina.png",
    stadiumName: "Terrero de Tejina",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
});

const team2 = db.teams.insertOne({
    name: "Club de Lucha Campitos",
    alias: "Campitos",
    island: "GRAN_CANARIA",
    municipality: "Las Palmas de Gran Canaria",
    divisionCategory: "PRIMERA",
    address: "Avenida de Escaleritas, 85",
    phone: "+34 928 251 234",
    email: "club@campitos.es",
    foundedYear: 1945,
    presidentName: "María del Carmen Suárez Medina",
    colors: ["Verde", "Blanco"],
    logoUrl: "https://example.com/logos/campitos.png",
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
// Wrestler from Team 1
db.wrestlers.insertOne({
    licenseNumber: "TF-2024-001",
    name: "Pedro",
    surname: "González Hernández",
    nickname: "El Palmero",
    birthDate: new Date("1995-03-15"),
    phone: "+34 666 345 678",
    height: 185,
    weight: 92,
    category: "SENIOR",
    classification: "PUNTAL_A",
    teamId: team1.insertedId.toString(),
    island: "TENERIFE",
    isActive: true,
    notes: "Campeón insular 2023, especialista en media cadera",
    imageUrl: "https://example.com/wrestlers/pedro-gonzalez.jpg",
    createdAt: new Date(),
    updatedAt: new Date()
});

// Wrestler from Team 2
db.wrestlers.insertOne({
    licenseNumber: "GC-2024-015",
    name: "Francisco",
    surname: "Santana Pérez",
    nickname: "Paco Santana",
    birthDate: new Date("1998-07-22"),
    phone: "+34 666 456 789",
    height: 178,
    weight: 85,
    category: "SENIOR",
    classification: "DESTACADO_A",
    teamId: team2.insertedId.toString(),
    island: "GRAN_CANARIA",
    isActive: true,
    notes: "Subcampeón regional 2023",
    imageUrl: "https://example.com/wrestlers/paco-santana.jpg",
    createdAt: new Date(),
    updatedAt: new Date()
});

// Young wrestler
db.wrestlers.insertOne({
    licenseNumber: "TF-2024-050",
    name: "David",
    surname: "Rodríguez López",
    nickname: null,
    birthDate: new Date("2008-11-10"),
    phone: null,
    height: 170,
    weight: 68,
    category: "JUVENIL",
    classification: "NONE",
    teamId: team1.insertedId.toString(),
    island: "TENERIFE",
    isActive: true,
    notes: "Promesa juvenil, buen trabajo de piernas",
    imageUrl: null,
    createdAt: new Date(),
    updatedAt: new Date()
});

// ========== REFEREES ==========
const referee1 = db.referees.insertOne({
    name: "Antonio García Martín",
    licenseNumber: "ARB-NAC-001",
    dni: "43123456A",
    phone: "+34 666 567 890",
    email: "agarcia@arbitros.luchacanaria.es",
    category: "NACIONAL",
    island: "TENERIFE",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
});

const referee2 = db.referees.insertOne({
    name: "José Luis Pérez Díaz",
    licenseNumber: "ARB-REG-015",
    dni: "43234567B",
    phone: "+34 666 678 901",
    email: "jlperez@arbitros.luchacanaria.es",
    category: "REGIONAL",
    island: "GRAN_CANARIA",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
});

// ========== COMPETITIONS ==========
const competition = db.competitions.insertOne({
    name: "Liga Regional de Primera División",
    season: "2024",
    ageCategory: "SENIOR",
    divisionCategory: "PRIMERA",
    island: null, // Regional competition
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
            id: new ObjectId(),
            number: 1,
            date: new Date("2024-01-20"),
            description: "Primera jornada de liga",
            matchIds: []
        },
        {
            id: new ObjectId(),
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
    competitionId: competition.insertedId.toString(),
    matchDayId: competition.insertedId.matchDays[0].id.toString(),
    localTeamId: team1.insertedId.toString(),
    visitorTeamId: team2.insertedId.toString(),
    date: new Date("2024-01-20T20:00:00"),
    scheduledTime: "20:00",
    venue: "Terrero de Tejina",
    status: "COMPLETED",
    localScore: 6,
    visitorScore: 6,
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
    competitionId: competition.insertedId.toString(),
    matchDayId: competition.insertedId.matchDays[1].id.toString(),
    localTeamId: team2.insertedId.toString(),
    visitorTeamId: team1.insertedId.toString(),
    date: new Date("2024-01-27T19:30:00"),
    scheduledTime: "19:30",
    venue: "Terrero López Socas",
    status: "SCHEDULED",
    localScore: null,
    visitorScore: null,
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
    matchId: match1.insertedId.toString(),
    competitionId: competition.insertedId.toString(),
    competitionName: "Liga Regional de Primera División",
    date: new Date("2024-01-20"),
    time: "20:00",
    venue: "Terrero de Tejina",
    
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
    
    fieldDelegate: {
        name: "Juan Manuel Rodríguez",
        dni: "43345678C"
    },
    
    localTeam: {
        teamId: team1.insertedId.toString(),
        clubName: "Club de Lucha Unión Tejina",
        wrestlers: [
            {
                wrestlerId: new ObjectId().toString(),
                licenseNumber: "TF-2024-001",
                number: 1
            },
            {
                wrestlerId: new ObjectId().toString(),
                licenseNumber: "TF-2024-002",
                number: 2
            }
        ],
        captain: {
            wrestlerId: new ObjectId().toString(),
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
        clubName: "Club de Lucha Campitos",
        wrestlers: [
            {
                wrestlerId: new ObjectId().toString(),
                licenseNumber: "GC-2024-015",
                number: 1
            },
            {
                wrestlerId: new ObjectId().toString(),
                licenseNumber: "GC-2024-016",
                number: 2
            }
        ],
        captain: {
            wrestlerId: new ObjectId().toString(),
            licenseNumber: "GC-2024-015",
            number: 1
        },
        coach: {
            name: "Fernando Suárez García",
            licenseNumber: "ENT-2024-010"
        }
    },
    
    matchBouts: [
        {
            id: new ObjectId(),
            localWrestler: {
                wrestlerId: new ObjectId().toString(),
                licenseNumber: "TF-2024-001",
                number: 1
            },
            visitorWrestler: {
                wrestlerId: new ObjectId().toString(),
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
                }
            ],
            penalties: [],
            winner: "LOCAL",
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

print("Sample data seeded successfully!");
print("=================================");
print("Created:");
print("- 3 users (1 admin, 1 coach, 1 regular user)");
print("- 2 teams");
print("- 3 wrestlers");
print("- 2 referees");
print("- 1 competition with 2 match days");
print("- 2 matches (1 completed, 1 scheduled)");
print("- 1 match act (for the completed match)");
print("=================================");
print("Default password for all users: Password123!");
print("To add more data, copy and modify the examples above.");