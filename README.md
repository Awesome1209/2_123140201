# NewsFeedSimulator_Tugas2

---
## 👤 Identitas Mahasiswa
* **Nama**: Awi Septian Prasetyo
* **NIM**: 123140201
* **Program Studi**: Teknik Informatika
* **Kampus**: Institut Teknologi Sumatera (ITERA)
---

Aplikasi **News Feed Simulator** berbasis **Kotlin Coroutines + Flow** (Kotlin Multiplatform) untuk mensimulasikan aliran berita dan menampilkan daftar berita secara real-time.

## Deskripsi Singkat Tugas
Aplikasi mensimulasikan news feed dengan ketentuan:
- **Flow** memancarkan berita baru setiap **2 detik**
- Menggunakan operator Flow untuk **filter** (berdasarkan kategori) dan **transform** (format siap tampil)
- Menggunakan **StateFlow** untuk menyimpan **jumlah berita yang sudah dibaca**
- Menggunakan **Coroutines (async/await)** untuk mengambil **detail berita** secara asynchronous (simulasi)

## Fitur yang Diimplementasikan
- Stream berita real-time (emit setiap 2 detik)
- Filter kategori: All / Tech / Sports / Biz / Ent
- Tampilan list berita dalam Card (mobile-friendly)
- Read count (StateFlow) bertambah saat item berita diklik
- Fetch detail berita secara async saat item diklik dan ditampilkan sebagai “Last detail”

## Struktur Project (Ringkas)
- `shared/src/commonMain/...`
  - `model/` : model data (News, NewsUi, Category)
  - `data/` : repository (Flow stream + fetch detail)
  - `presentation/` : ViewModel (Flow operators + StateFlow)
  - `ui/` : UI Compose (NewsFeedScreen)
- `composeApp/` : entry point aplikasi (Android + Desktop)

---

# Cara Menjalankan

## A. Menjalankan di Desktop (Windows/macOS/Linux)
1. Buka project di **Android Studio** atau **IntelliJ IDEA**
2. **Gradle Sync**
3. Pilih konfigurasi Run: **Desktop / jvmRun** (nama bisa berbeda tergantung template)
4. Klik **Run (▶)**

> Jika terjadi error rendering (mis. DirectX12), gunakan OpenGL/Software dengan menambahkan:
> `jvmArgs += listOf("-Dskiko.renderApi=OPENGL")`
> atau
> `jvmArgs += listOf("-Dskiko.renderApi=SOFTWARE")`
> pada `composeApp/build.gradle.kts` di dalam `compose.desktop { application { ... } }`

## B. Menjalankan di Android Emulator
1. Buka **Tools → Device Manager**
2. Jalankan emulator (mis. *Medium Phone API ...*)
3. Pilih Run configuration: **androidApp** / **composeApp (Android)**
4. Pilih device emulator sebagai target
5. Klik **Run (▶)**

Jika emulator crash:
- Coba **Cold Boot Now** / **Wipe Data** pada Device Manager
- Ubah **Graphics** emulator menjadi **Software** pada Edit AVD

## C. Menjalankan di Android Phone (Opsional)
1. Aktifkan **Developer Options** dan **USB Debugging**
2. Sambungkan HP ke laptop
3. Pilih device HP di dropdown device Android Studio
4. Klik **Run (▶)**

---

# Cara Pakai Aplikasi
- Tunggu beberapa detik sampai berita mulai muncul (bertambah tiap 2 detik)
- Pilih kategori untuk filter berita
- Tap salah satu berita:
  - **Read count** bertambah
  - “Last detail” akan terisi hasil fetch detail (simulasi async)

---
#Butki Screenshoot
1. ![gambar2](https://github.com/user-attachments/assets/6e6e63c5-80bb-4890-b88e-76727103ae9c)
2. ![gambar2](https://github.com/user-attachments/assets/767ef94b-5ca2-46b1-876d-5504e4ea4135)

