# 📡 BLE Secret Message Beacon

## 📌 What the app does  
BLE Secret Message Beacon is an Android app that lets two nearby phones exchange short messages over Bluetooth Low Energy **without internet**.  
- **Advertise** a short message as BLE manufacturer data.  
- **Scan** to receive nearby messages instantly.  
- Works in a single toggle screen — both sending & receiving happen in one place.  
- Automatically detects when Bluetooth is off and prompts the user to enable it.

---

## 🧑‍💻 My role and contributions  
I designed and implemented the app **end-to-end**:  
- Created a **clean MVI architecture** following Google’s Clean Architecture guidelines.  
- Designed the UI in **Jetpack Compose** with previews and state-driven rendering.  
- Implemented BLE advertising, scanning, and message decoding logic.  
- Added **Bluetooth state detection** with automatic UI updates.

---

## 🛠 Tech stack  
- **Language:** Kotlin  
- **Architecture:** Clean Architecture + MVI  
- **UI:** Jetpack Compose  
- **DI:** Hilt  
- **BLE:** Android Bluetooth Low Energy APIs  
- **Versioning:** Gradle TOML Version Catalog  
- **Min SDK:** 23+  

---

## 🔄 Architecture & Data Flow  
┌──────────────┐
│    UI Layer  │
│ (Jetpack Compose)
│  - Renders state
│  - Sends user intents
└───────▲──────┘
│
│ Intents
▼
┌──────────────┐
│ ViewModel    │
│ (MVI Pattern)
│ - Processes intents
│ - Calls Use Cases
│ - Emits new state
└───────▲──────┘
│
│
▼
┌──────────────┐
│ Domain Layer │
│ - BLE Use Cases
│ - Bluetooth state checks
└───────▲──────┘
│
│
▼
┌──────────────┐
│ Data Layer   │
│ - BLE APIs
│ - Advertiser
│ - Scanner
└──────────────┘

**BLE Flow:**  
1. User enters a message → sends **AdvertiseIntent** → ViewModel triggers BLE advertiser.  
2. Nearby device scans and detects manufacturer data → decodes it into a message.  
3. ViewModel updates `receivedMessages` → UI re-renders automatically.  

---

## 🚀 How to run it  
1. **Clone the repository**  
   ```bash
   git clone https://github.com/yourusername/ble-secret-message-beacon.git
   cd ble-secret-message-beacon
   ```
2. **Open in Android Studio** (Giraffe or newer).  
3. **Sync Gradle** to fetch dependencies.  
4. **Build & run** on a **physical Android device** (BLE features won’t work on the emulator).  
5. When prompted, **enable Bluetooth** and grant location permissions.  
