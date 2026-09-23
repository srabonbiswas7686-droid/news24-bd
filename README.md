# NEWS24 BD — Modern Bengali News Channel Application

[![Platform](https://img.shields.io/badge/Platform-Android%20%7C%20Jetpack%20Compose-green)](https://developer.android.com/jetpack/compose)
[![Language](https://img.shields.io/badge/Language-Kotlin%20%7C%20Bengali-red)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

**NEWS24 BD** is a responsive, mobile-first Bengali news application built with modern Kotlin and Jetpack Compose. Designed with a clean Bangladeshi news aesthetic, high readability for Bengali Unicode typography, live breaking news ticker, comprehensive category feeds, video broadcasts, photo gallery, offline bookmarks with Room database, and an editorial management dashboard.

---

## 1. Project Features

- **ব্রেকিং নিউজ টিকার (Breaking News Ticker):** Live horizontal scrolling breaking news bar with pause/play controls and instant tap navigation.
- **হোম পেজ (Home Page):** Featured Hero News card, latest stories, top 5 ranked popular news with readership counts, video carousel, and photo gallery.
- **সংবাদ বিভাগসমূহ (11 Categories):**
  - সর্বশেষ (Latest)
  - বাংলাদেশ (Bangladesh)
  - রাজনীতি (Politics)
  - আন্তর্জাতিক (International)
  - অর্থনীতি (Economy)
  - খেলাধুলা (Sports)
  - প্রযুক্তি (Technology)
  - বিনোদন (Entertainment)
  - শিক্ষা (Education)
  - স্বাস্থ্য (Health)
  - লাইফস্টাইল (Lifestyle)
- **বিস্তারিত সংবাদ পাতা (Article Detail Page):** Full article view, source, author, publish time, text size scaler ($A^-$ / $A^+$) for reading comfort, social sharing (WhatsApp, Facebook, Copy Link, Native Share API), related articles, and bookmarking.
- **অনুসন্ধান ব্যবস্থা (Search System):** Real-time headline, category, and keyword search with filter suggestions and zero-state handling.
- **সংরক্ষিত সংবাদ (Offline Bookmarks):** Saved news articles persisted locally via Android Room database.
- **ভিডিও ও ফটো গ্যালারি (Multimedia):** Responsive video player modal and full-screen image lightbox with photo credits.
- **পাঠক অ্যাকাউন্ট ও প্রোফাইল (User Account):** Reader sign-in/registration UI, notification preferences, and reading statistics.
- **অ্যাডমিন ড্যাশবোর্ড (Admin Panel):** Total views & news counters, dynamic article publisher with category selector, breaking news switch, and article manager.

---

## 2. Local Development Instructions

### Prerequisites
- **Android Studio Ladybug (or newer)**
- **JDK 17 or JDK 21**
- **Android SDK Platform 36**

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/news24-bd.git
   cd news24-bd
   ```
2. Open the project in Android Studio.
3. Allow Gradle to sync dependencies automatically.
4. Run the app on an Android device or emulator with API 24+:
   ```bash
   ./gradlew installDebug
   ```

---

## 3. Environment Variables & Secrets

API keys are managed securely using the Secrets Gradle Plugin via `.env` file (never hardcoded into version control):

Create a `.env` file in the project root:
```env
# Optional Gemini AI API Key (if enabling AI summary assistant)
# GEMINI_API_KEY=your_gemini_api_key_here

# Backend API Endpoint (if connecting live CMS)
# NEWS_API_ENDPOINT=https://api.news24bd.com/v1
```

---

## 4. GitHub Deployment Instructions

1. Initialize git and commit:
   ```bash
   git init
   git add .
   git commit -m "Initial commit: NEWS24 BD complete application"
   ```
2. Create a new repository on GitHub: `news24-bd`.
3. Add the remote and push:
   ```bash
   git remote add origin https://github.com/your-username/news24-bd.git
   git branch -M main
   git push -u origin main
   ```

---

## 5. Web / Vercel Deployment Instructions

If you wish to deploy the news portal on Vercel:
1. Export the project to GitHub as shown above.
2. Link your GitHub repo to [Vercel](https://vercel.com).
3. The codebase is architected with clean data contracts and headless CMS ready structure (`NewsModels.kt` / `SampleNewsData.kt`) that mirror JSON REST endpoints.
4. For client-side web hosting or PWA export, web assets can consume the identical `/api/news` format.

---

## 6. Backend Configuration (Firebase / Supabase)

To connect **Firebase** or **Supabase**:
- **Firebase Firestore:**
  1. Add `google-services.json` to the `/app/` directory.
  2. In `app/build.gradle.kts`, uncomment `implementation(libs.firebase.firestore)`.
  3. Replace the `customNewsDao` stream in `NewsRepository.kt` with a Firestore collection snapshot listener.
- **Supabase / REST API:**
  1. In `app/build.gradle.kts`, `retrofit` and `moshi` are already included.
  2. Implement a Retrofit service pointing to your Supabase PostgREST URL with the `apikey` header.

---

## 7. How to Change the App Name & Logo

- **App Name:** Update `<string name="app_name">NEWS24 BD</string>` in `app/src/main/res/values/strings.xml`, `rootProject.name` in `settings.gradle.kts`, and `name` in `metadata.json`.
- **Launcher Icon:** Replace `app/src/main/res/drawable/ic_news24_logo.jpg` or modify the vector paths in `ic_launcher_foreground.xml` and `ic_launcher_background.xml`.
- **Branding Header:** Edit `app/src/main/java/com/example/ui/components/NewsHeader.kt`.

---

## 8. How to Add Real News Data

- **Via Admin UI:** Navigate to Menu $\to$ অ্যাডমিন ড্যাশবোর্ড $\to$ "সংবাদ যোগ" to publish articles directly on device.
- **Via Code:** Open `app/src/main/java/com/example/data/SampleNewsData.kt` and add new `NewsArticle` objects to the `sampleArticles` list.
- **Via API:** Update `NewsRepository.kt` to fetch articles from your remote JSON feed.

---

## Disclaimer
All sample articles and media files in this initial demo release are marked with `[নমুনা]` for demonstration purposes and do not represent actual unverified news reports.
