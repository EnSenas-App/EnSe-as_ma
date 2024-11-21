# EnSeñasApp 🤟

EnSeñasApp es una aplicación móvil diseñada para servir como puente de comunicación entre personas sordas y oyentes en Colombia, facilitando tanto el aprendizaje de la Lengua de Señas Colombiana (LSC) como la integración social de la comunidad sorda.

## 🎯 Objetivo

Abordar la brecha de comunicación que enfrentan las aproximadamente 500,000 personas sordas y 5 millones de personas con algún grado de discapacidad auditiva en Colombia, proporcionando herramientas tanto para el aprendizaje de LSC como para la interacción cotidiana.

## 👥 Usuarios Objetivo

1. **Personas oyentes**:
   - Interesadas en aprender LSC
   - Comprometidas con la inclusión social
   - Estudiantes y profesionales que desean comunicarse con la comunidad sorda

2. **Personas sordas**:
   - Que necesitan asistencia en trámites
   - Buscan espacios de interacción social
   - Desean compartir su conocimiento y cultura

## 🌟 Características Principales

### Red Social
- Organización de eventos y encuentros
- Coordinación de cafés de lenguas
- Compartir recursos y material de aprendizaje
- Foro de discusión y apoyo comunitario

[Diagrama de Arquitectura del Módulo Social pendiente]

### Sistema de Aprendizaje
- Evaluación mediante cartas didácticas
- Basado en el diccionario de 1200 palabras del INSOR
- Sistema de seguimiento de progreso

### ChatBot Informativo
- Información sobre LSC
- Historia del lenguaje de señas
- Directorio de organizaciones de apoyo

### Traducción (Beta)
- Traducción de LSC a texto
- Enfoque en contextos administrativos
- Procesamiento en tiempo real

## 🛠 Tecnologías

### Frontend
- Android Studio
- Kotlin
- Jetpack Compose

### Backend
- Firebase
  - Authentication
  - Cloud Firestore
  - Storage
  - Hosting

## 📱 Capturas de Pantalla

[Próximamente]

## 🏗 Arquitectura MVVM

# Arquitectura de EnSeñasApp

## Arquitectura General
```mermaid
graph TB
    subgraph "Presentation Layer"
        A[Activities/Fragments] --> B[ViewModels]
        B --> C[UI State]
    end

    subgraph "Domain Layer"
        D[Use Cases] --> E[Domain Models]
        F[Repository Interfaces]
    end

    subgraph "Data Layer"
        G[Repository Implementations] --> H[Remote Data Source]
        G --> I[Local Data Source]
        
        subgraph "Remote"
            H --> J[Firebase Auth]
            H --> K[Firebase Firestore]
            H --> L[Firebase Storage]
        end
        
        subgraph "Local"
            I --> M[Room Database]
            I --> N[DataStore]
        end
    end

    B --> D
    G --> F

    style A fill:#b7e2ba
    style B fill:#b7e2ba
    style C fill:#b7e2ba
    style D fill:#ffd591
    style E fill:#ffd591
    style F fill:#ffd591
    style G fill:#87ceeb
    style H fill:#87ceeb
    style I fill:#87ceeb
    style J fill:#ffb6c1
    style K fill:#ffb6c1
    style L fill:#ffb6c1
    style M fill:#dda0dd
    style N fill:#dda0dd
```

## Módulo Social - Estructura de Datos
```mermaid
erDiagram
    Users ||--o{ Posts : creates
    Users ||--o{ Comments : writes
    Users ||--o{ Categories : moderates
    Users ||--o{ EventAttendees : attends
    Categories ||--o{ Posts : contains
    Posts ||--o{ Comments : has
    Posts ||--o{ Likes : receives
    Posts ||--|| Events : extends
    Events ||--o{ EventAttendees : has
    Users ||--o{ Likes : gives

    Users {
        string uid PK
        string username
        string email
        string profileImage
        timestamp createdAt
        boolean isAdmin
    }

    Categories {
        string categoryId PK
        string name
        string description
        array moderators
        timestamp createdAt
    }

    Posts {
        string postId PK
        string userId FK
        string categoryId FK
        string type "normal|event"
        string title
        string content
        array attachments
        int viewCount
        int likeCount
        int commentCount
        timestamp createdAt
        timestamp updatedAt
    }

    Events {
        string postId PK "Same as Post ID"
        string location
        string locationDetails
        timestamp eventDate
        timestamp eventEndDate
        int maxAttendees
        int currentAttendees
        boolean requiresApproval
        array organizerIds
        string status "upcoming|ongoing|completed|cancelled"
        object coordinates "lat/lng"
    }

    EventAttendees {
        string eventId FK
        string userId FK
        string status "going|maybe|notGoing"
        int numberOfGuests
        string notes
        timestamp respondedAt
        boolean isApproved
    }

    Comments {
        string commentId PK
        string postId FK
        string userId FK
        string content
        array attachments
        timestamp createdAt
        timestamp updatedAt
    }

    Likes {
        string likeId PK
        string postId FK
        string userId FK
        timestamp createdAt
    }
```

## Flujo de Datos - Social
```mermaid
sequenceDiagram
    participant UI as UI (Activity/Fragment)
    participant VM as ViewModel
    participant UC as UseCase
    participant Repo as Repository
    participant FB as Firebase
    participant DB as Local DB

    UI->>VM: User Action
    VM->>UC: Execute UseCase
    UC->>Repo: Request Data
    
    alt Online Mode
        Repo->>FB: Get Data
        FB-->>Repo: Return Data
        Repo->>DB: Cache Data
    else Offline Mode
        Repo->>DB: Get Cached Data
    end
    
    Repo-->>UC: Return Result
    UC-->>VM: Process Result
    VM-->>UI: Update State
```

## 👨‍💻 Equipo

- David Julian Bustos Cortes
- Julian Camilo Alfonso Carrillo
- Jhon Felipe Delgado Salazar
- Martin Alonso Gomez Uribe

## 🙏 Agradecimientos

- INSOR (Instituto Nacional para Sordos)
- Universidad Nacional de Colombia
- Comunidad sorda colombiana

---