# Disaster Relief Distribution Network (Sistem Distribusi Bantuan Bencana)

> **Final Project Struktur Data dan Pemrograman Berorientasi Objek (Strukdat & PBO) 2026**
> 
> Topik: Opsi 8 — Disaster Relief Distribution Network

---

## Latar Belakang Masalah

Dalam situasi pasca-bencana alam, distribusi logistik dan bantuan medis sering kali terhambat oleh rusaknya infrastruktur jalan dan terbatasnya sumber daya transportasi. Tantangan utama yang harus diselesaikan meliputi:

1. **Penentuan Prioritas:** Mengidentifikasi wilayah (Posko/Desa) yang paling membutuhkan bantuan secara instan berdasarkan tingkat keparahan, kebutuhan logistik, dan jumlah populasi terdampak.
2. **Pencarian Rute Optimal:** Menemukan rute perjalanan tercepat dari Gudang Pusat menuju lokasi tujuan dengan menghindari jalur yang terputus atau rusak.
3. **Efisiensi Jaringan Distribusi:** Merancang ulang skema rantai pasok secara keseluruhan guna meminimalisir biaya operasional tanpa memutus konektivitas antar wilayah.

Sistem *Disaster Relief Distribution Network* hadir sebagai solusi cerdas berbasis perangkat lunak yang mensimulasikan penyelesaian masalah operasional di atas secara komprehensif menggunakan implementasi struktur data **Graph** dan **Tree**.

---

## Struktur Data & Arsitektur

### 1. Struktur Data Graph
Jaringan infrastruktur direpresentasikan menggunakan struktur data Graph.
*   **Adjacency List:** Dipilih sebagai representasi Graph karena jaringan jalan raya memiliki karakteristik *Sparse Graph* (tidak setiap simpul terhubung secara langsung). Penggunaan *Adjacency List* memberikan efisiensi memori yang jauh lebih baik dibandingkan *Adjacency Matrix*.
*   **Simpul (Node) & Sisi (Edge):** `LocationNode` merepresentasikan titik lokasi (Gudang, Posko, Desa), sedangkan `RouteEdge` merepresentasikan jalan raya yang menghubungkan dua lokasi dengan bobot berupa jarak tempuh (km) dan status jalan (bisa dilewati/rusak).

### 2. Struktur Data Tree (Min-Heap)
Sistem ini mengimplementasikan struktur data **Min-Heap** dari awal (tanpa menggunakan `PriorityQueue` bawaan Java) sebagai antrean prioritas penyaluran bantuan.
*   **Kompleksitas Waktu:** Pengambilan lokasi dengan prioritas tertinggi dapat dilakukan dalam kompleksitas waktu **O(log N)** untuk ekstraksi, dan **O(1)** untuk melihat elemen teratas (peek).
*   **Metrik Prioritas:** Prioritas dievaluasi berdasarkan skor komposit yang mencakup: tingkat kritis, risiko hambatan akses, defisit kebutuhan logistik, dan besaran populasi terdampak.
*   **Efisiensi Memori:** Implementasi menggunakan *Array/List* internal sehingga menghindari alokasi memori objek referensi anak (Left/Right node) seperti pada AVL Tree, menjadikannya sangat ringan.

---

## Algoritma Utama

Sistem ini didukung oleh dua algoritma Graph fundamental:

1.  **Algoritma Dijkstra (Shortest Path):**
    Digunakan untuk komputasi rute tercepat/terpendek antara dua lokasi secara dinamis. Algoritma ini mempertimbangkan status *real-time* dari kondisi jalan raya (hanya mengevaluasi *edge* yang aktif).
2.  **Algoritma Kruskal (Minimum Spanning Tree):**
    Dioptimalkan menggunakan struktur data *Disjoint-Set (Union-Find)*. Kruskal beroperasi dengan mengekstrak seluruh *edge* aktif, menyortirnya berdasarkan bobot, dan merangkai jaringan operasional terpadu (*Minimum Spanning Tree*) guna menjamin semua lokasi tetap terjangkau dengan total biaya/jarak paling efisien.

---

## Struktur Direktori Proyek

Proyek ini menggunakan paradigma *Object-Oriented Programming (OOP)* dengan hierarki direktori yang terorganisir:

```text
FP_Strukdat_KelompokXX/
├── data/
│   ├── nodes.csv          # Dataset simpul/lokasi (ID, Nama, Populasi, Tingkat Kritis, dsb.)
│   └── edges.csv          # Dataset relasi rute jalan antar lokasi beserta jaraknya
├── docs/                  # Direktori khusus untuk dokumentasi laporan dan tracing manual
│   ├── laporan.pdf        # (Target format untuk pengumpulan)
│   └── tracing.pdf        # (Target format untuk pengumpulan)
├── src/
│   ├── model/             # Kelas entitas: LocationNode.java, RouteEdge.java
│   ├── tree/              # Implementasi Tree: MinHeap.java
│   ├── graph/             # Implementasi Algoritma Graph: Graph.java, Dijkstra.java, KruskalMST.java
│   ├── util/              # Kelas utilitas pembantu (jika ada)
│   └── Main.java          # Entry-point aplikasi interaktif berbasis CLI
└── README.md              # Dokumentasi utama proyek
```

> [!NOTE]
> Pastikan struktur folder saat pengumpulan sesuai dengan panduan *Deliverables* mata kuliah. Laporan berekstensi `.md` pada folder `docs/` harus dikonversi terlebih dahulu ke dalam format `.pdf`.

---

## Panduan Instalasi & Penggunaan

Sistem ini dirancang untuk dijalankan melalui antarmuka *Command Line (Terminal/CMD)* tanpa memerlukan dependensi eksternal.

### 1. Kompilasi (Compile)
Buka terminal pada direktori *root* proyek (tempat file `README.md` berada), lalu jalankan perintah kompilasi berikut:
```bash
mkdir -p out
javac -sourcepath src src/Main.java -d out
```

### 2. Eksekusi Program (Run)
Setelah proses kompilasi berhasil tanpa galat (*error*), jalankan program dengan perintah:
```bash
java -cp out Main
```

### 3. Eksplorasi Fitur (Menu Interaktif)
Sistem secara otomatis memuat *dataset* dari folder `data/` saat pertama kali dijalankan. Pengguna dapat menavigasi menu interaktif berikut:

| Nomor | Menu | Deskripsi Fungsi |
| :---: | :--- | :--- |
| **1** | Tampilkan Lokasi | Menampilkan daftar seluruh `LocationNode` beserta atribut lengkapnya. |
| **2** | Cari Lokasi | Melakukan pencarian lokasi secara spesifik menggunakan parameter ID atau string Nama Lokasi. |
| **3** | Tambah Data Posko | Registrasi node lokasi baru beserta atribut kebutuhannya, dan pembaruan struktur `nodes.csv` & `edges.csv`. |
| **4** | Update Kebutuhan | Melakukan mutasi/pembaruan *state* kebutuhan logistik pada suatu lokasi. |
| **5** | Tampilkan Jaringan | Memvisualisasikan representasi tekstual dari *Adjacency List* beserta status aktif jalan. |
| **6** | Prioritas Pengiriman | Mengeksekusi ekstraksi top-5 dari antrean prioritas **Min-Heap** untuk prioritas pengiriman darurat. |
| **7** | Cari Rute (Dijkstra) | Menghitung dan menelusuri rute tempuh terpendek dari titik awal hingga titik tujuan berdasarkan *edge* aktif. |
| **8** | Rancang Distribusi (Kruskal) | Mengeksekusi komputasi *Minimum Spanning Tree* untuk rekomendasi infrastruktur distribusi terhemat. |
| **9** | Simulasi Jalan Bencana | Modul simulasi interaktif (*What-If Analysis*) untuk melakukan *toggle* status aktif/rusak pada suatu jalan. |
| **10** | Cek Konektivitas | Algoritma *Connected Components* untuk memvalidasi apakah seluruh titik masih dapat diakses melalui jalan aktif. |

---

## What-If Analysis: Skenario Simulasi Bencana

Sistem ini memfasilitasi analisis dinamik (*What-If Analysis*) untuk mensimulasikan putusnya jalur transportasi secara *real-time*:

1. **Jalankan Skenario Normal:** Lakukan simulasi **Cari Rute (Menu 7)** dari suatu titik A ke titik B. Catat urutan jalan yang dilewati.
2. **Berikan Gangguan Akses:** Gunakan **Simulasi Jalan Bencana (Menu 9)** untuk menyabotase (mengubah status menjadi `2. Rusak`) salah satu segmen jalan vital yang ada pada rute sebelumnya.
3. **Validasi Pemulihan:** Jalankan kembali **Menu 7** dengan tujuan yang sama. Sistem secara otonom akan memotong jalur yang rusak dan merekomendasikan **jalan memutar (detour)** terbaik berikutnya!

---
*Dikembangkan untuk Tugas Akhir Strukdat & PBO.*
