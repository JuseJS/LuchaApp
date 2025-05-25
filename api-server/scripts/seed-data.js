// MongoDB seed data script for testing
// Run after init-mongodb.js to populate with sample data

use lucha_canaria;

print("Starting data seeding...");

// Helper function to generate ObjectId
function newId() {
  return ObjectId();
}

// Create sample teams
print("Creating sample teams...");

const teams = [
  {
    _id: newId(),
    name: "C.L. Benchomo",
    alias: "Benchomo",
    island: "TENERIFE",
    municipality: "La Laguna",
    divisionCategory: "PRIMERA",
    address: "Calle Principal 123",
    phone: "922123456",
    email: "info@benchomo.com",
    foundedYear: 1985,
    presidentName: "Juan Pérez",
    colors: ["Azul", "Blanco"],
    logoUrl: "https://example.com/logos/benchomo.png",
    stadiumName: "Terrero de Benchomo",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: newId(),
    name: "C.L. Rosario",
    alias: "Rosario",
    island: "TENERIFE",
    municipality: "Puerto de la Cruz",
    divisionCategory: "PRIMERA",
    address: "Avenida Marítima 45",
    phone: "922234567",
    email: "info@rosario.com",
    foundedYear: 1978,
    presidentName: "María García",
    colors: ["Rojo", "Negro"],
    logoUrl: "https://example.com/logos/rosario.png",
    stadiumName: "Terrero del Rosario",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: newId(),
    name: "C.L. Guamasa",
    alias: "Guamasa",
    island: "TENERIFE",
    municipality: "La Laguna",
    divisionCategory: "SEGUNDA",
    address: "Plaza del Pueblo 1",
    phone: "922345678",
    email: "info@guamasa.com",
    foundedYear: 1990,
    presidentName: "Pedro Rodríguez",
    colors: ["Verde", "Blanco"],
    logoUrl: "https://example.com/logos/guamasa.png",
    stadiumName: "Terrero de Guamasa",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  },
  {
    _id: newId(),
    name: "C.L. Unión Agüere",
    alias: "Unión Agüere",
    island: "GRAN_CANARIA",
    municipality: "Las Palmas",
    divisionCategory: "PRIMERA",
    address: "Calle Mayor 789",
    phone: "928123456",
    email: "info@unionaguere.com",
    foundedYear: 1982,
    presidentName: "Ana Martín",
    colors: ["Amarillo", "Azul"],
    logoUrl: "https://example.com/logos/unionaguere.png",
    stadiumName: "Terrero de Agüere",
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date()
  }
];

const teamIds = teams.map(team => {
  db.teams.insertOne(team);
  return team._id;
});

print(`Created ${teams.length} teams`);

// Create sample wrestlers
print("Creating sample wrestlers...");

const wrestlerNames = [
  { name: "Carlos", surname: "González", nickname: "El Pollo" },
  { name: "Antonio", surname: "Hernández", nickname: "El Palmero" },
  { name: "Miguel", surname: "Rodríguez", nickname: "Miguelín" },
  { name: "Juan", surname: "Díaz", nickname: "Juanito" },
  { name: "Francisco", surname: "Pérez", nickname: "Paco" },
  { name: "José", surname: "García", nickname: "Pepe" },
  { name: "Luis", surname: "Martín", nickname: "Luisito" },
  { name: "Pedro", surname: "López", nickname: "Pedrito" }
];

const classifications = ["PUNTAL_A", "PUNTAL_B", "PUNTAL_C", "DESTACADO_A", "DESTACADO_B", "DESTACADO_C"];
const categories = ["REGIONAL", "JUVENIL", "CADETE"];
const islands = ["TENERIFE", "GRAN_CANARIA"];

let wrestlerCount = 0;
teamIds.forEach((teamId, teamIndex) => {
  // Create 8 wrestlers per team
  for (let i = 0; i < 8; i++) {
    const wrestler = {
      _id: newId(),
      licenseNumber: `LC${String(wrestlerCount + 1).padStart(5, '0')}`,
      name: wrestlerNames[i].name,
      surname: wrestlerNames[i].surname,
      nickname: wrestlerNames[i].nickname,
      birthDate: new Date(2000 + Math.floor(Math.random() * 10), Math.floor(Math.random() * 12), Math.floor(Math.random() * 28) + 1),
      phone: `922${String(100000 + wrestlerCount).padStart(6, '0')}`,
      height: 170 + Math.floor(Math.random() * 30),
      weight: 70 + Math.floor(Math.random() * 40),
      category: categories[Math.floor(Math.random() * categories.length)],
      classification: classifications[Math.floor(Math.random() * classifications.length)],
      teamId: teamId.toHexString(),
      island: teams[teamIndex].island,
      notes: null,
      imageUrl: `https://example.com/wrestlers/wrestler${wrestlerCount + 1}.jpg`,
      isActive: true,
      createdAt: new Date(),
      updatedAt: new Date()
    };
    
    db.wrestlers.insertOne(wrestler);
    wrestlerCount++;
  }
});

print(`Created ${wrestlerCount} wrestlers`);

// Create sample competition
print("Creating sample competition...");

const competition = {
  _id: newId(),
  name: "Liga Insular de Tenerife",
  season: "2024-2025",
  ageCategory: "SENIOR",
  divisionCategory: "PRIMERA",
  island: "TENERIFE",
  startDate: new Date(2024, 9, 1), // October 1, 2024
  endDate: new Date(2025, 5, 30), // June 30, 2025
  description: "Liga regular de la temporada 2024-2025",
  rules: "Reglamento oficial de la Federación de Lucha Canaria",
  participatingTeamIds: teamIds.slice(0, 2).map(id => id.toHexString()), // First two teams
  matchDays: [],
  isActive: true,
  createdAt: new Date(),
  updatedAt: new Date()
};

// Create match days
for (let i = 1; i <= 10; i++) {
  competition.matchDays.push({
    id: newId().toHexString(),
    number: i,
    date: new Date(2024, 9, i * 7), // Weekly matches
    description: `Jornada ${i}`,
    matchIds: []
  });
}

db.competitions.insertOne(competition);
print("Created competition with 10 match days");

// Create sample matches for the first 3 match days
print("Creating sample matches...");

let matchCount = 0;
for (let dayIndex = 0; dayIndex < 3; dayIndex++) {
  const matchDay = competition.matchDays[dayIndex];
  
  // Create a match between first two teams
  const match = {
    _id: newId(),
    competitionId: competition._id.toHexString(),
    matchDayId: matchDay.id,
    localTeamId: teamIds[0].toHexString(),
    visitorTeamId: teamIds[1].toHexString(),
    date: matchDay.date,
    scheduledTime: new Date(matchDay.date.setHours(20, 0, 0)), // 8 PM
    venue: teams[0].stadiumName,
    status: dayIndex < 2 ? "COMPLETED" : "SCHEDULED",
    localScore: dayIndex < 2 ? 8.5 : null,
    visitorScore: dayIndex < 2 ? 7.5 : null,
    matchActId: null,
    refereeIds: [],
    statistics: dayIndex < 2 ? {
      localScore: 8.5,
      visitorScore: 7.5,
      individualMatches: []
    } : null,
    createdAt: new Date(),
    updatedAt: new Date()
  };
  
  db.matches.insertOne(match);
  matchCount++;
}

print(`Created ${matchCount} matches`);

// Create sample users
print("Creating sample users...");

const users = [
  {
    _id: newId(),
    email: "coach1@luchaapp.com",
    password: "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewKyNiLXCJrdKcf.", // Admin123!
    name: "Juan",
    surname: "Entrenador",
    phone: "922111111",
    role: "COACH",
    permissions: [
      "MANAGE_OWN_TEAM",
      "VIEW_COMPETITIONS",
      "VIEW_MATCHES",
      "SUBMIT_MATCH_ACTS",
      "MANAGE_FAVORITES"
    ],
    associatedTeamId: teamIds[0].toHexString(),
    isActive: true,
    createdAt: new Date(),
    updatedAt: new Date(),
    lastLogin: null
  },
  {
    _id: newId(),
    email: "viewer@luchaapp.com",
    password: "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewKyNiLXCJrdKcf.", // Admin123!
    name: "María",
    surname: "Espectadora",
    phone: "922222222",
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
  }
];

users.forEach(user => db.users.insertOne(user));
print(`Created ${users.length} additional users`);

print("\nSeed data created successfully!");
print("\nTest accounts:");
print("Admin: admin@luchaapp.com / Admin123!");
print("Coach: coach1@luchaapp.com / Admin123!");
print("Viewer: viewer@luchaapp.com / Admin123!");
print("\nDatabase seeding completed!");