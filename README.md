# 🛍️ Luqta - Android E-Commerce App

**Luqta** is a hyper-local Arabic-only e-commerce mobile app focused on second-hand product trading in Palestinian communities.  
This app is my graduation project for the **2024–2025 academic year**.

> 🎓 Developed as part of my final year research at ***Islamic University of Gaza***.

[🎥 Watch Demo Video on youtube](https://youtu.be/TQR5NMoQviI?si=vOj-cYJBhJlFpfxj)

---

## 📱 About the App

Luqta is built using **modern Android development practices** including:
- **Jetpack Compose**
- **MVVM Architecture**
- **Clean Architecture principles**
- **Dependency Injection with Koin**
- **Kotlin Coroutines**
- **Modular structure with `domain`, `data`, and `presentation` layers**

The app supports features such as:
- Secure authentication (signup, login, password reset)
- Browsing categories and products
- Viewing product details
- Adding to cart and managing wishlist
- User profile management
- Persistent authentication using secure storage

---

## 📁 Project Structure

Here's a simplified overview of the folder structure:

```bash
📦 luqtaecommerce
┣ 📂 data # Data sources: local storage & network
┣ 📂 domain # Business logic: models, use cases
┣ 📂 presentation # UI screens & viewmodels (Compose-based)
┣ 📂 di # Koin dependency injection modules
┣ 📂 ui # Shared UI components and theme
┣ 📂 navigation # Navigation graph and bottom nav config
┣ 📂 LuqtaApplication.kt
┗ 📂 MainActivity.kt
```

---

## 🛠️ Tech Stack

| Area                 | Technology                      |
|----------------------|---------------------------------|
| Language             | Kotlin                          |
| UI Toolkit           | Jetpack Compose                 |
| Architecture         | MVVM + Clean Architecture       |
| Dependency Injection | Koin                            |
| Network Layer        | Retrofit, OkHttp, Gson          |
| Secure Storage       | Android Keystore + AES (custom) |
| State Management     | ViewModel + StateFlow           |
| WebSockets           | OkHTTP WebSockets               |
| Testing              | JUnit + Espresso                |

---

## 🔐 Configuration
Before running the app, create a file in your project to hold the following temp config values:

> 📄 `AuthPreferences` and `BaseUrlProvider` depend on these settings:

```kotlin
// Replace with your actual API base URL
const val BASE_URL = "https://api.example.com/"

// Temp Auth keys (replace securely for production)
const val ENCRYPTION_KEY_ALIAS = "luqta_key"
```

Make sure to configure your backend server to handle:
- User registration and login
- Product and category endpoints
- Token refresh logic

---

## ▶️ How to Run
1) Clone the repo:

```bash
git clone https://github.com/your-username/luqta-android.git
cd LuqtaAndroidEcommerce
```
2) Open in Android Studio (Arctic Fox or higher).
3)  Sync Gradle and run the app on an emulator or device.
