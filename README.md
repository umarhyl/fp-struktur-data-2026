# Final Project Struktur Data 2026

## Topik: Opsi 8 — Disaster Relief Distribution Network
Proyek ini adalah implementasi sistem cerdas untuk mengatur distribusi bantuan logistik bencana alam dari Gudang Pusat ke berbagai Posko Bantuan dan Desa terdampak, menggunakan algoritma Graph dan struktur data Tree.

---

## Ringkasan & Latar Belakang Masalah
Dalam situasi bencana alam, kecepatan dan efisiensi pengiriman bantuan logistik adalah kunci untuk menyelamatkan nyawa. Namun, akses infrastruktur (jalan) seringkali rusak atau terputus akibat bencana. Masalah yang harus diselesaikan oleh sistem ini meliputi:
1. **Bagaimana menentukan prioritas posko** mana yang harus dibantu terlebih dahulu berdasarkan tingkat kerusakan dan kebutuhannya?
2. **Bagaimana mencari rute perjalanan tercepat** menuju posko yang terdampak ketika beberapa ruas jalan tidak bisa dilewati?
3. **Bagaimana merancang jaringan distribusi yang paling hemat biaya operasional** agar seluruh daerah terhubung namun dengan konsumsi bahan bakar kendaraan yang minimal?

Sistem ini mensimulasikan penyelesaian masalah di atas secara komprehensif.

---

## Struktur Data Tree: Kenapa Memilih Min-Heap?

Di dalam proyek ini, **Min-Heap** diimplementasikan dari awal (tanpa menggunakan built-in `PriorityQueue` bawaan Java) untuk menyimpan dan mengurutkan antrean posko bantuan.

**Alasan Pemilihan Min-Heap:**
1. **Sangat Cepat untuk Priority Queue:** Dalam pendistribusian bantuan darurat, kita selalu ingin mengambil (meng-ekstrak) lokasi yang **paling krisis** atau **paling butuh bantuan**. Min-Heap memungkinkan kita untuk mengambil prioritas tertinggi dengan kompleksitas waktu **O(log N)** untuk ekstraksi dan insert. Mendapatkan elemen teratas hanya butuh **O(1)**.
2. **Efisiensi Memori:** Dibandingkan dengan AVL Tree, Min-Heap direpresentasikan menggunakan *Array/List* tunggal tanpa membutuhkan objek Node (Left/Right) yang berlebihan, sehingga sangat ringan untuk memori.

---

## Struktur Data Graph & Algoritma
*   **Adjacency List**: Digunakan untuk representasi Graph, karena jaringan jalan raya tergolong *Sparse Graph* (tidak semua lokasi terhubung secara langsung satu sama lain). *Adjacency List* jauh lebih hemat memori dibanding *Adjacency Matrix*.
*   **Algoritma Dijkstra**: Digunakan untuk menyelesaikan fitur "Cari Rute Tercepat" antar titik.
*   **Algoritma Kruskal (Minimum Spanning Tree)**: Menggunakan optimasi *Disjoint-Set (Union-Find)* untuk merancang jalur jaringan operasional paling hemat agar semua titik saling terhubung.

---

## Struktur Proyek & Penjelasan Kode

Aplikasi ini menggunakan pola desain yang rapi berbasis *Object-Oriented Programming (OOP)*.

```text
FP/
├── data/
│   ├── nodes.csv          --> Dataset 25 lokasi (ID, Nama, Tipe, Populasi, Tingkat Kritis, Kebutuhan, Risiko)
│   └── edges.csv          --> Dataset 40+ rute jarak jalan raya antar lokasi (km)
├── src/
│   ├── model/
│   │   ├── LocationNode.java  --> Kelas cetakan untuk objek Lokasi/Posko. Menyimpan atribut 5 data wajib.
│   │   └── RouteEdge.java     --> Kelas cetakan rute, memiliki properti 'distance' (bobot) dan boolean 'active' (apakah jalan bisa dilewati).
│   ├── tree/
│   │   └── MinHeap.java       --> Implementasi struktur data Tree kustom (Heapify-up & Heapify-down).
│   ├── graph/
│   │   ├── Graph.java         --> Mengelola data Adjacency List. Menyediakan fungsi addNode, addEdge, dan toggleEdge.
│   │   ├── Dijkstra.java      --> Memuat algoritma Shortest-Path Dijkstra.
│   │   └── KruskalMST.java    --> Memuat algoritma MST. Mengekstrak edge aktif lalu menyortir bobotnya.
│   └── Main.java              --> Entry-point sistem (CLI Interaktif).
└── README.md
```

---

## Cara Menjalankan & Menggunakan Sistem

Aplikasi ini berjalan penuh melalui Terminal (Command Line Interface).

### 1. Proses Kompilasi (Compile)
Buka terminal di dalam direktori `FP` (tempat file README ini berada), lalu jalankan perintah kompilasi:
```bash
mkdir -p out
javac -sourcepath src src/Main.java -d out
```

### 2. Jalankan Aplikasi
Setelah di-compile, eksekusi program dengan perintah:
```bash
java -cp out Main
```

### 3. Penjelasan Fitur / Menu Interaktif
Saat aplikasi berjalan, dataset akan termuat secara otomatis dan Menu Utama akan muncul. Berikut fungsi dari setiap menu:

*   **Menu 1 (Tampilkan Lokasi)**: Membaca dan menampilkan atribut lengkap dari 25 dataset `LocationNode`, termasuk `riskLevel`.
*   **Menu 2 (Cari Lokasi)**: Mencari lokasi berdasarkan ID atau sebagian nama lokasi.
*   **Menu 3 (Tambah Data Posko & Kebutuhan)**: Menambahkan lokasi baru, mengisi kebutuhan logistik, tingkat kritis, risiko, dan opsional menghubungkannya ke jaringan jalan. Data baru disimpan kembali ke `data/nodes.csv`, sedangkan edge baru disimpan ke `data/edges.csv`.
*   **Menu 4 (Update Kebutuhan Logistik)**: Mengubah kebutuhan logistik lokasi yang sudah ada dan menyimpan perubahannya kembali ke `data/nodes.csv`.
*   **Menu 5 (Tampilkan Jaringan)**: Menampilkan tetangga dari setiap simpul (Adjacency List) dan status jalan.
*   **Menu 6 (Prioritas Pengiriman)**: Menggunakan **Min-Heap** untuk menampilkan Top-5 desa/posko paling darurat berdasarkan skor gabungan: tingkat kritis, risiko akses, kebutuhan logistik, dan populasi terdampak.
*   **Menu 7 (Cari Rute Dijkstra)**: Kamu akan diminta memasukkan ID Lokasi Awal (cth: `G01`) dan ID Tujuan (cth: `P03`). Program akan mencetak jalur yang harus dilalui beserta total jarak tercepatnya.
*   **Menu 8 (Rancang Distribusi Kruskal MST)**: Program akan mencetak jaringan rantai pasok paling efisien yang mempertahankan konektivitas ke semua node (Total biaya/jarak minimal).
*   **Menu 9 (Simulasi Jalan Rusak/Bencana)**: **[What-If Analysis]** Fitur ini memungkinkan pengguna untuk memutus atau mengaktifkan kembali suatu jalan.
    *   *Skenario Pengujian:* Lakukan pencarian Dijkstra (Menu 7), catat rutenya. Lalu gunakan Menu 9 untuk menonaktifkan salah satu jalan yang dilewati oleh Dijkstra tadi dengan memilih status `2. Rusak`. Kemudian jalankan lagi Menu 7. Aplikasi akan secara otomatis menemukan **jalan memutar** untuk menghindari jalan yang rusak tersebut!
*   **Menu 10 (Cek Konektivitas Jaringan)**: Mengecek apakah semua lokasi masih terhubung melalui jalan aktif. Jika graph terputus, program menampilkan jumlah komponen dan daftar lokasi di setiap komponen.
