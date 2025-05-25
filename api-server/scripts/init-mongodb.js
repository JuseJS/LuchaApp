// MongoDB initialization script for MongoDB 8.0
// Run this script to set up the database structure and initial data

// Switch to the lucha_canaria database
use lucha_canaria;

// Drop existing collections (optional - comment out in production)
// db.users.drop();
// db.teams.drop();
// db.wrestlers.drop();
// db.competitions.drop();
// db.matches.drop();
// db.matchActs.drop();
// db.referees.drop();

// Create collections with validation schemas for MongoDB 8.0
db.createCollection("users", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["email", "password", "name", "surname", "role", "permissions", "isActive", "createdAt", "updatedAt"],
      properties: {
        email: {
          bsonType: "string",
          pattern: "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
          description: "must be a valid email address"
        },
        password: {
          bsonType: "string",
          minLength: 60,
          description: "must be a bcrypt hashed password"
        },
        name: {
          bsonType: "string",
          minLength: 1,
          description: "user's first name"
        },
        surname: {
          bsonType: "string",
          minLength: 1,
          description: "user's last name"
        },
        phone: {
          bsonType: ["string", "null"],
          description: "optional phone number"
        },
        role: {
          enum: ["GUEST", "REGISTERED_USER", "COACH", "FEDERATIVE_DELEGATE"],
          description: "user role"
        },
        permissions: {
          bsonType: "array",
          items: {
            bsonType: "string"
          },
          description: "list of permissions"
        },
        associatedTeamId: {
          bsonType: ["string", "null"],
          description: "optional team association"
        },
        isActive: {
          bsonType: "bool",
          description: "account status"
        },
        createdAt: {
          bsonType: "date"
        },
        updatedAt: {
          bsonType: "date"
        },
        lastLogin: {
          bsonType: ["date", "null"]
        }
      }
    }
  }
});

db.createCollection("teams", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "imageUrl", "island", "venue", "divisionCategory"],
      properties: {
        name: {
          bsonType: "string",
          minLength: 1
        },
        imageUrl: {
          bsonType: "string",
          description: "team logo image URL"
        },
        island: {
          enum: ["TENERIFE", "GRAN_CANARIA", "LANZAROTE", "FUERTEVENTURA", "LA_PALMA", "LA_GOMERA", "EL_HIERRO"]
        },
        venue: {
          bsonType: "string",
          description: "team's home venue"
        },
        divisionCategory: {
          enum: ["PRIMERA", "SEGUNDA", "TERCERA"]
        }
      }
    }
  }
});

db.createCollection("wrestlers", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["licenseNumber", "name", "surname", "teamId", "category", "classification"],
      properties: {
        licenseNumber: {
          bsonType: "string",
          pattern: "^[A-Z0-9-]+$"
        },
        name: {
          bsonType: "string",
          minLength: 1
        },
        surname: {
          bsonType: "string",
          minLength: 1
        },
        imageUrl: {
          bsonType: ["string", "null"]
        },
        teamId: {
          bsonType: "string"
        },
        category: {
          enum: ["REGIONAL", "JUVENIL"]
        },
        classification: {
          enum: ["PUNTAL_A", "PUNTAL_B", "PUNTAL_C", "DESTACADO_A", "DESTACADO_B", "DESTACADO_C", "NONE"]
        },
        height: {
          bsonType: ["int", "null"],
          minimum: 0,
          maximum: 300
        },
        weight: {
          bsonType: ["int", "null"],
          minimum: 0,
          maximum: 500
        },
        birthDate: {
          bsonType: ["date", "null"]
        },
        nickname: {
          bsonType: ["string", "null"]
        }
      }
    }
  }
});

db.createCollection("competitions", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "ageCategory", "divisionCategory", "island", "season"],
      properties: {
        name: {
          bsonType: "string"
        },
        season: {
          bsonType: "string"
        },
        ageCategory: {
          enum: ["JUVENIL", "REGIONAL"]
        },
        divisionCategory: {
          enum: ["PRIMERA", "SEGUNDA", "TERCERA"]
        },
        island: {
          enum: ["TENERIFE", "GRAN_CANARIA", "LANZAROTE", "FUERTEVENTURA", "LA_PALMA", "LA_GOMERA", "EL_HIERRO"]
        },
        teamIds: {
          bsonType: "array",
          items: {
            bsonType: "string"
          },
          description: "IDs of teams participating in this competition"
        }
      }
    }
  }
});

db.createCollection("matches", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["localTeamId", "visitorTeamId", "date", "venue", "completed", "hasAct"],
      properties: {
        localTeamId: {
          bsonType: "string"
        },
        visitorTeamId: {
          bsonType: "string"
        },
        localScore: {
          bsonType: ["int", "null"]
        },
        visitorScore: {
          bsonType: ["int", "null"]
        },
        date: {
          bsonType: "date"
        },
        venue: {
          bsonType: "string"
        },
        completed: {
          bsonType: "bool"
        },
        hasAct: {
          bsonType: "bool"
        },
        competitionId: {
          bsonType: ["string", "null"],
          description: "ID of the competition this match belongs to"
        },
        round: {
          bsonType: ["int", "null"],
          description: "Round or match day number"
        }
      }
    }
  }
});

db.createCollection("referees", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "licenseNumber", "isMain", "isActive"],
      properties: {
        name: {
          bsonType: "string"
        },
        licenseNumber: {
          bsonType: "string"
        },
        isMain: {
          bsonType: "bool"
        },
        isActive: {
          bsonType: "bool"
        }
      }
    }
  }
});

db.createCollection("favorites", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["userId", "entityId", "entityType", "createdAt"],
      properties: {
        userId: {
          bsonType: "string",
          description: "ID del usuario que marcó como favorito"
        },
        entityId: {
          bsonType: "string",
          description: "ID de la entidad marcada como favorita"
        },
        entityType: {
          enum: ["TEAM", "WRESTLER", "COMPETITION"],
          description: "Tipo de entidad favorita"
        },
        createdAt: {
          bsonType: "date"
        }
      }
    }
  }
});

db.createCollection("matchActs", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["matchId", "competitionId", "competitionName", "season", "category", "isRegional", "isInsular", "venue", "date", "startTime", "mainReferee", "localTeam", "visitorTeam", "localTeamScore", "visitorTeamScore", "isDraft", "isCompleted", "isSigned", "createdAt", "updatedAt"],
      properties: {
        matchId: {
          bsonType: "string"
        },
        competitionId: {
          bsonType: "string"
        },
        competitionName: {
          bsonType: "string"
        },
        season: {
          bsonType: "string"
        },
        category: {
          enum: ["JUVENIL", "REGIONAL"]
        },
        isRegional: {
          bsonType: "bool"
        },
        isInsular: {
          bsonType: "bool"
        },
        venue: {
          bsonType: "string"
        },
        date: {
          bsonType: "date"
        },
        startTime: {
          bsonType: "date"
        },
        endTime: {
          bsonType: ["date", "null"]
        },
        mainReferee: {
          bsonType: "object"
        },
        assistantReferees: {
          bsonType: "array"
        },
        fieldDelegate: {
          bsonType: ["object", "null"]
        },
        localTeam: {
          bsonType: "object"
        },
        visitorTeam: {
          bsonType: "object"
        },
        bouts: {
          bsonType: "array"
        },
        localTeamScore: {
          bsonType: "int"
        },
        visitorTeamScore: {
          bsonType: "int"
        },
        isDraft: {
          bsonType: "bool"
        },
        isCompleted: {
          bsonType: "bool"
        },
        isSigned: {
          bsonType: "bool"
        },
        localTeamComments: {
          bsonType: ["string", "null"]
        },
        visitorTeamComments: {
          bsonType: ["string", "null"]
        },
        refereeComments: {
          bsonType: ["string", "null"]
        },
        createdAt: {
          bsonType: "date"
        },
        updatedAt: {
          bsonType: "date"
        }
      }
    }
  }
});

// Create indexes
print("Creating indexes...");

// User indexes
db.users.createIndex({ "email": 1 }, { unique: true });
// DNI index removed - field no longer used
db.users.createIndex({ "role": 1 });
db.users.createIndex({ "associatedTeamId": 1 });
db.users.createIndex({ "isActive": 1 });

// Team indexes
db.teams.createIndex({ "name": 1 }, { unique: true });
db.teams.createIndex({ "island": 1 });
db.teams.createIndex({ "divisionCategory": 1 });
db.teams.createIndex({ "name": "text" });

// Wrestler indexes
db.wrestlers.createIndex({ "licenseNumber": 1 }, { unique: true });
db.wrestlers.createIndex({ "teamId": 1 });
db.wrestlers.createIndex({ "category": 1 });
db.wrestlers.createIndex({ "classification": 1 });
db.wrestlers.createIndex({ "name": "text", "surname": "text", "nickname": "text" });

// Competition indexes
db.competitions.createIndex({ "season": 1 });
db.competitions.createIndex({ "ageCategory": 1 });
db.competitions.createIndex({ "divisionCategory": 1 });
db.competitions.createIndex({ "island": 1 });
db.competitions.createIndex({ "name": "text" });

// Match indexes
db.matches.createIndex({ "localTeamId": 1 });
db.matches.createIndex({ "visitorTeamId": 1 });
db.matches.createIndex({ "date": 1 });
db.matches.createIndex({ "completed": 1 });
db.matches.createIndex({ "hasAct": 1 });

// MatchAct indexes
db.matchActs.createIndex({ "matchId": 1 }, { unique: true });
db.matchActs.createIndex({ "competitionId": 1 });
db.matchActs.createIndex({ "isDraft": 1 });
db.matchActs.createIndex({ "isCompleted": 1 });

// Referee indexes
db.referees.createIndex({ "licenseNumber": 1 }, { unique: true });
db.referees.createIndex({ "isMain": 1 });
db.referees.createIndex({ "isActive": 1 });
db.referees.createIndex({ "name": "text" });

// Favorites indexes
db.favorites.createIndex({ "userId": 1, "entityId": 1, "entityType": 1 }, { unique: true });
db.favorites.createIndex({ "userId": 1 });
db.favorites.createIndex({ "entityId": 1 });
db.favorites.createIndex({ "entityType": 1 });

print("Indexes created successfully!");

// Create default admin user (password: Admin123!)
print("Creating default admin user...");

db.users.insertOne({
  _id: ObjectId(),
  email: "admin@luchaapp.com",
  password: "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewKyNiLXCJrdKcf.",
  name: "System",
  surname: "Administrator",
  phone: null,
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
  lastLogin: null
});

print("Default admin user created successfully!");
print("Email: admin@luchaapp.com");
print("Password: Admin123!");

// Insert sample teams
const teamIds = [];
const team1 = db.teams.insertOne({
  name: "CL Campitos",
  imageUrl: "https://example.com/teams/campitos.png",
  island: "TENERIFE",
  venue: "Terrero de Campitos",
  divisionCategory: "PRIMERA"
});
teamIds.push(team1.insertedId);

const team2 = db.teams.insertOne({
  name: "CL Victoria",
  imageUrl: "https://example.com/teams/victoria.png",
  island: "TENERIFE",
  venue: "Terrero de La Victoria",
  divisionCategory: "PRIMERA"
});
teamIds.push(team2.insertedId);

print("Sample teams created!");

// Insert sample competition
const competition1 = db.competitions.insertOne({
  name: "Liga Insular de Tenerife",
  ageCategory: "REGIONAL",
  divisionCategory: "PRIMERA",
  island: "TENERIFE",
  season: "2024-2025",
  teamIds: teamIds.map(id => id.toString())
});

print("Sample competition created!");

// Insert sample wrestlers
db.wrestlers.insertOne({
  licenseNumber: "TF-001",
  name: "Pedro",
  surname: "González",
  imageUrl: null,
  teamId: teamIds[0].toString(),
  category: "REGIONAL",
  classification: "PUNTAL_A",
  height: 185,
  weight: 95,
  birthDate: new Date("1995-05-15"),
  nickname: "El Palmero"
});

db.wrestlers.insertOne({
  licenseNumber: "TF-002",
  name: "Juan",
  surname: "Rodríguez",
  imageUrl: null,
  teamId: teamIds[1].toString(),
  category: "REGIONAL",
  classification: "DESTACADO_A",
  height: 178,
  weight: 85,
  birthDate: new Date("1998-08-20"),
  nickname: null
});

print("Sample wrestlers created!");

// Insert sample matches
const match1 = db.matches.insertOne({
  localTeamId: teamIds[0].toString(),
  visitorTeamId: teamIds[1].toString(),
  localScore: 15,
  visitorScore: 12,
  date: new Date("2024-11-15T18:00:00Z"),
  venue: "Terrero de Campitos",
  completed: true,
  hasAct: false,
  competitionId: competition1.insertedId.toString(),
  round: 1
});

const match2 = db.matches.insertOne({
  localTeamId: teamIds[1].toString(),
  visitorTeamId: teamIds[0].toString(),
  localScore: null,
  visitorScore: null,
  date: new Date("2024-11-22T18:00:00Z"),
  venue: "Terrero de La Victoria",
  completed: false,
  hasAct: false,
  competitionId: competition1.insertedId.toString(),
  round: 2
});

print("Sample matches created!");

// Add more teams to make it interesting
const team3 = db.teams.insertOne({
  name: "CL Rosario",
  imageUrl: "https://example.com/teams/rosario.png",
  island: "TENERIFE",
  venue: "Terrero del Rosario",
  divisionCategory: "PRIMERA"
});
teamIds.push(team3.insertedId);

const team4 = db.teams.insertOne({
  name: "CL Guamasa",
  imageUrl: "https://example.com/teams/guamasa.png",
  island: "TENERIFE",
  venue: "Terrero de Guamasa",
  divisionCategory: "PRIMERA"
});
teamIds.push(team4.insertedId);

// Update competition with all team IDs
db.competitions.updateOne(
  { _id: competition1.insertedId },
  { $set: { teamIds: teamIds.map(id => id.toString()) } }
);

// Add more matches
db.matches.insertOne({
  localTeamId: team3.insertedId.toString(),
  visitorTeamId: team4.insertedId.toString(),
  localScore: 14,
  visitorScore: 13,
  date: new Date("2024-11-15T20:00:00Z"),
  venue: "Terrero del Rosario",
  completed: true,
  hasAct: false,
  competitionId: competition1.insertedId.toString(),
  round: 1
});

db.matches.insertOne({
  localTeamId: teamIds[0].toString(),
  visitorTeamId: team3.insertedId.toString(),
  localScore: null,
  visitorScore: null,
  date: new Date("2024-11-29T18:00:00Z"),
  venue: "Terrero de Campitos",
  completed: false,
  hasAct: false,
  competitionId: competition1.insertedId.toString(),
  round: 3
});

print("Additional teams and matches created!");

print("");
print("Database initialization completed!");