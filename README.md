# 🐉 Dragon Ball Universe — Android App

A beautifully designed Android app built with **Jetpack Compose** and **MVVM architecture**, powered by the [Dragon Ball API](https://www.dragonball-api.com).

---

## 📱 Screenshots

| Home Screen | Character Detail | Planets |
|---|---|---|
| Characters grid with search & filters | Full stats, Ki power, transformations | Paginated planet list with status |

---

## ✨ Features

- **58 Characters** — paginated grid with full details
- **43 Transformations** — scrollable transformation picker on detail screen
- **20 Planets** — with alive/destroyed status filter
- **Real-time Search** — debounced name search (450ms)
- **Advanced Filters** — filter by Race, Affiliation, Gender
- **Planet Filter** — toggle between All / Destroyed / Alive planets
- **Shimmer Loading** — animated skeleton while fetching data
- **Edge-to-edge UI** — transparent status & navigation bars
- **Dragon Ball Splash Screen** — animated star on dark background
- **Error States** — retry button with themed error screen
- **Dark Theme** — Dragon Ball orange/gold palette throughout

---

## 🏗️ Architecture

```
com.dragonball.app/
├── data/
│   ├── model/          # Data classes + Filter enums + UiState
│   ├── remote/         # Retrofit API service interface
│   └── repository/     # Single source of truth (Repository pattern)
├── di/                 # Hilt dependency injection modules
├── ui/
│   ├── components/     # Reusable Composables (cards, chips, loaders)
│   ├── navigation/     # NavGraph + Screen sealed class
│   ├── screens/
│   │   ├── home/       # Character grid + search + filter panel
│   │   ├── detail/     # Character detail + Planet detail
│   │   └── planets/    # Planet list with filters
│   └── theme/          # Colors, Typography, Theme
└── viewmodel/          # HomeViewModel, CharacterDetailViewModel,
                        # PlanetsViewModel, PlanetDetailViewModel
```

**Pattern:** `UI → ViewModel → Repository → API Service`

---

## 🛠️ Tech Stack

| Library | Version | Purpose |
|---|---|---|
| Jetpack Compose | BOM 2024.09.03 | UI framework |
| Hilt | 2.51.1 | Dependency injection |
| Retrofit | 2.11.0 | HTTP client |
| OkHttp | 4.12.0 | Network layer + logging |
| Gson | 2.11.0 | JSON parsing |
| Coil | 2.7.0 | Async image loading |
| Navigation Compose | 2.8.2 | Screen navigation |
| Coroutines + Flow | 1.9.0 | Async & reactive state |
| Splash Screen API | 1.0.1 | Android 12+ splash |
| Material 3 | — | UI components |

---

## 🌐 API Reference

**Base URL:** `https://www.dragonball-api.com/api`

| Endpoint | Description | Response |
|---|---|---|
| `GET /characters?page=1&limit=10` | Paginated character list | `CharactersResponse` |
| `GET /characters/{id}` | Single character with planet & transformations | `Character` |
| `GET /characters?name=Goku` | Filter by name (no pagination) | `List<Character>` |
| `GET /characters?race=Saiyan&affiliation=Z+Fighter` | Filter by race + affiliation | `List<Character>` |
| `GET /planets?page=1&limit=10` | Paginated planet list | `PlanetsResponse` |
| `GET /planets/{id}` | Single planet with residents | `Planet` |
| `GET /planets?isDestroyed=true` | Filter destroyed planets | `List<Planet>` |

> ⚠️ **Filter endpoints return a flat array** (no pagination wrapper). Never mix `page`/`limit` with filter params.

---

## 🎨 Color Palette

| Color | Hex | Usage |
|---|---|---|
| Dragon Orange | `#F5A623` | Primary, accents, borders |
| Ki Yellow | `#FFD600` | Ki power, stars, highlights |
| Saiyan Blue | `#1565C0` | Secondary actions |
| Dark Background | `#0A0E1A` | App background |
| Dark Card | `#1C2537` | Card surfaces |
| Alive Green | `#4CAF50` | Planet alive status |
| Destroyed Red | `#E53935` | Planet destroyed / errors |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Iguana or newer
- JDK 17
- Android SDK 26+

### Build & Run

```bash
# Clone the project
git clone https://github.com/yourname/DragonBallApp.git
cd DragonBallApp

# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug
```

### gradle.properties (required)
```properties
org.gradle.jvmargs=-Xmx4g -XX:MaxMetaspaceSize=1g -Dfile.encoding=UTF-8
org.gradle.daemon=true
org.gradle.parallel=true
org.gradle.caching=true
android.suppressUnsupportedCompileSdk=35
android.useAndroidX=true
android.enableJetifier=false
kotlin.code.style=official
```

---

## 📂 Project Structure

```
DragonBallApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/dragonball/app/   # All Kotlin source files
│   │   ├── res/
│   │   │   ├── drawable/              # ic_splash_dragon_ball.xml
│   │   │   ├── values/                # colors, strings, themes
│   │   │   └── xml/                   # network_security_config.xml
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml             # Version catalog
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## 🔒 Network Security

The app uses a **Network Security Config** to trust system certificates on all Android versions:

```xml
<!-- res/xml/network_security_config.xml -->
<network-security-config>
    <base-config cleartextTrafficPermitted="false">
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </base-config>
</network-security-config>
```

---

## 📄 License

```
MIT License — free to use, modify, and distribute.
```

---

*Built with ❤️ and lots of Ki energy 🔥*
