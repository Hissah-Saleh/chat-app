# Chat Application

A simple Android chat app using Socket.IO applying MVVM and clean arcticture.

---

## 📋 Main Features
1. **Login Screen**  
   - Enter a temporary username  
   - Saved locally (SharedPreferences)

2. **Chat Room**  
   - Connects to Node.js Socket.IO server  
   - Global room: real-time text messages  

3. **Media Upload**  
   - “Files” page to pick & send one image/video/audio
   - Preview selected media before sending it
  
## 🎬 Demo

![Chat Demo](https://github.com/Hissah-Saleh/chat-app/blob/main/preview/demo.gif)

## 🚀 How to Run

1. **Clone the repo**  
   ```bash
   git clone git@github.com:Hissah-Saleh/chat-app.git
   cd chat-app
   ```

2. **Start the Server**  
   ```bash
   cd server
   npm install express socket.io multer uuid dotenv
   node server.js
   ```  
   - Runs on `http://<YOUR_MACHINE_IP>:3000` (replace `<YOUR_MACHINE_IP>` with your actual LAN IP, e.g. `192.168.1.42`; set `Constants.CHAT_SERVER_URL` accordingly). **Ensure both your server and Android device/emulator are on the same local network (LAN) so the app can connect.**

3. **Run the Android App**  
   - Open `android/` in Android Studio  
   - **Important:** set `NetworkConstants.CHAT_SERVER_URL` before running (omitting it will crash the app on real devices). If you’re running on the emulator, you can leave the URL as `http://10.0.2.2:3000`.  
     ```kotlin
     object NetworkConstants {
         val CHAT_SERVER_URL = if (isEmulator())
             "http://10.0.2.2:3000"
         else
             "http://<YOUR_MACHINE_IP>:3000"
     }
     ```  
   - Build & install on emulator/device

---

## 🏗 Architecture

- **Pattern:** MVVM (Jetpack Compose + ViewModels)  
- **Layers:**  
  - **Domain:** Use-cases, data models  
  - **Data:** ChatRepository 
  - **UI:** Compose screens + ViewModels  
- **DI:** Hilt for dependency injection  
- **Networking:** Socket.IO Android client, ApiService

---

## 🛠 Challenges & Solutions

- **Node.js server integration**: set up Express + Socket.IO, configured Multer storage and MIME-type checks, and implemented robust error handling and logging in server.js.

---

## ✨ Bonus Features

- Embedded display for images, a video player for video messages, and an audio player for audio messages directly within the chat feed  
- Show Lottie animation on typing indicator  
- Dark mode support  
- Active users counter (shows number of users currently in chat)
- Tap on an image or video message to open a full-screen preview
- Preview media before sending it

---

## 📂 Project Structure

```plaintext
chat-app/
├─ server/    # Node.js Socket.IO server
└─ android/   # Android Studio project
   ├─ app/
   │  ├─ src/
   │  │  ├─ main/
   │  │  │  ├─ java/…
   │  │  │  └─ res/…
   │  └─ build.gradle
   └─ build.gradle
```

---

## 📞 Questions?

Open an issue or reach out if anything’s unclear.

