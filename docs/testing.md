# Hasil Testing Program

Tanggal testing: 14 Juni 2026
Tester: Satya

## Cara Menjalankan Program

Compile:
```bash
javac -sourcepath src src/Main.java -d out
```

Run:
```bash
java -cp out Main
```

Hasil:
Program berhasil dicompile dan dijalankan. Dataset berhasil dimuat dengan total 25 node awal dan 43 edge awal.

## Ringkasan Requirement yang Dicek

- Dataset minimal 25 node: terpenuhi.
- Dataset minimal 40 edge: terpenuhi.
- Minimal 5 atribut tambahan selain ID dan nama: terpenuhi melalui `type`, `population`, `criticalLevel`, `logisticsNeeded`, dan `riskLevel`.
- Tree custom: terpenuhi melalui `MinHeap`.
- Graph custom: terpenuhi melalui adjacency list di `Graph`.
- Minimal 2 algoritma graph: terpenuhi melalui Dijkstra dan Kruskal MST.
- Fitur Opsi 8: tambah posko, cari jalur, prioritas posko, total biaya/jarak distribusi, dan simulasi jalan rusak.

## Skenario Normal 1 - Menampilkan Lokasi

Input:
```text
1
```

Expected Output:
Program menampilkan seluruh data lokasi dari `nodes.csv`.

Actual Output:
Program menampilkan daftar lokasi berisi ID, nama lokasi, tipe, populasi, tingkat kritis, kebutuhan logistik, dan risiko. Contoh output:
```text
D01 | Desa Kedungcangkring | Tipe: Desa | Populasi: 850 | Tingkat Kritis: 5 | Kebutuhan: 3.5 Ton | Risiko: 5
G01 | Gudang Pusat Surabaya | Tipe: Gudang | Populasi: 0 | Tingkat Kritis: 1 | Kebutuhan: 0.0 Ton | Risiko: 1
P01 | Posko Sidoarjo Kota | Tipe: Posko | Populasi: 5000 | Tingkat Kritis: 4 | Kebutuhan: 15.5 Ton | Risiko: 3
```

Status:
Berhasil

Catatan:
Daftar lokasi tampil urut berdasarkan ID lokasi.

## Skenario Normal 2 - Search Lokasi

Input:
```text
2
Sidoarjo
```

Expected Output:
Program menampilkan lokasi yang ID atau namanya mengandung kata kunci pencarian.

Actual Output:
```text
=== Hasil Pencarian Lokasi ===
P01 | Posko Sidoarjo Kota | Tipe: Posko | Populasi: 5000 | Tingkat Kritis: 4 | Kebutuhan: 15.5 Ton | Risiko: 3
```

Status:
Berhasil

Catatan:
Search mendukung pencarian sebagian nama dan tidak case-sensitive.

## Skenario Normal 3 - Tambah Data Posko & Kebutuhan

Input:
```text
3
ID lokasi baru: P99
Nama lokasi: Posko Demo Baru
Tipe: 2
Jumlah penduduk/pengungsi: 250
Tingkat kritis: 4
Kebutuhan logistik: 9.5
Tingkat risiko akses: 3
Hubungkan ke jaringan: y
Lokasi terhubung: P01
Jarak: 2.4
```

Expected Output:
Program menambahkan lokasi baru ke sesi program dan menambahkan edge baru ke graph.

Actual Output:
```text
Data lokasi berhasil ditambahkan ke sesi program.
Jalur P99 - P01 berhasil ditambahkan.
```

Status:
Berhasil

Catatan:
Data tambahan bersifat runtime/session. Setelah program ditutup, dataset CSV awal tidak berubah.

## Skenario Normal 4 - Dijkstra

Input:
```text
6
Asal: G01
Tujuan: D01
```

Expected Output:
Program menampilkan rute tercepat dan total jarak.

Actual Output:
```text
Dari: Gudang Pusat Surabaya
Ke: Desa Kedungcangkring
Total Jarak: 24.0 km
Jalur: Gudang Pusat Surabaya -> Posko Sidoarjo Kota -> Posko Tanggulangin -> Desa Kedungcangkring
```

Status:
Berhasil

Catatan:
Validasi ID dilakukan langsung per input. Jika ID asal salah, program meminta ID asal lagi sebelum lanjut ke ID tujuan.

## Skenario Normal 5 - Prioritas Bantuan Min-Heap

Input:
```text
5
```

Expected Output:
Program menampilkan lokasi dengan prioritas bantuan tertinggi.

Actual Output:
```text
1. Desa Kedungcangkring (Tingkat Kritis: 5, Kebutuhan: 3.5 Ton)
2. Desa Besuki (Tingkat Kritis: 5, Kebutuhan: 6.5 Ton)
3. Desa Gempolsari (Tingkat Kritis: 5, Kebutuhan: 7.0 Ton)
4. Desa Lajuk (Tingkat Kritis: 5, Kebutuhan: 7.5 Ton)
5. Desa Renokenongo (Tingkat Kritis: 5, Kebutuhan: 8.0 Ton)
```

Status:
Berhasil

Catatan:
Min-Heap berjalan dan mengeluarkan node dengan tingkat kritis tertinggi sebagai prioritas.

## Skenario Normal 6 - Kruskal MST

Input:
```text
7
```

Expected Output:
Program menampilkan jaringan distribusi minimum dan total jarak jaringan.

Actual Output:
```text
=== Minimum Spanning Tree (Jaringan Distribusi Minimum) ===
Jalur yang dipertahankan untuk menghemat biaya operasional:
Desa Besuki -- Desa Renokenongo == 1.2 km
Desa Besuki -- Posko Porong == 1.5 km
...
Gudang Pusat Surabaya -- Posko Sidoarjo Kota == 15.2 km
Total Jarak Jaringan: 78.6 km
```

Status:
Berhasil

Catatan:
Kruskal MST berhasil menghasilkan jaringan minimum dari edge yang aktif.

## Skenario Normal 7 - Simulasi Jalan Rusak

Input:
```text
8
Lokasi 1: G01
Lokasi 2: P01
Status: 2
```

Expected Output:
Status jalan berubah dan memengaruhi jaringan/rute.

Actual Output:
```text
Jalur G01 - P01 statusnya menjadi: RUSAK/TERTUTUP
```

Saat jaringan ditampilkan ulang, jalur tersebut muncul sebagai:
```text
Gudang Pusat Surabaya terhubung ke: [Posko Sidoarjo Kota - RUSAK] Posko Candi(18.5km)
Posko Sidoarjo Kota terhubung ke: [Gudang Pusat Surabaya - RUSAK] Posko Candi(4.3km) Posko Tanggulangin(6.7km)
```

Status:
Berhasil

Catatan:
Input status menggunakan pilihan angka `1. Aktif` dan `2. Rusak`.

## Edge Case 1 - Menu Tidak Valid

Input:
```text
abc
10
```

Expected Output:
Program menolak input menu yang tidak valid dan meminta user memilih menu 1-9.

Actual Output:
```text
Pilihan tidak valid. Silakan pilih menu 1-9.
```

Status:
Berhasil

Catatan:
Validasi berlaku untuk input non-angka dan angka di luar pilihan menu.

## Edge Case 2 - ID Lokasi Tidak Valid Saat Dijkstra

Input:
```text
6
Asal: X99
Asal ulang: G01
Tujuan: Y99
Tujuan ulang: D01
```

Expected Output:
Program langsung menolak ID yang salah dan meminta input yang sama lagi.

Actual Output:
```text
Masukkan ID lokasi asal: ID lokasi tidak valid. Silakan masukkan ID yang ada di daftar.
Masukkan ID lokasi asal:
Masukkan ID lokasi tujuan: ID lokasi tidak valid. Silakan masukkan ID yang ada di daftar.
Masukkan ID lokasi tujuan:
```

Status:
Berhasil

Catatan:
Program tidak menunggu semua input selesai untuk memvalidasi ID.

## Edge Case 3 - Search Kosong atau Tidak Ditemukan

Input:
```text
2
TidakAdaLokasi
```

Expected Output:
Program memberi tahu bahwa lokasi tidak ditemukan.

Actual Output:
```text
Lokasi tidak ditemukan untuk kata kunci: tidakadalokasi
```

Status:
Berhasil

Catatan:
Input kosong juga ditolak dengan pesan `Kata kunci pencarian tidak boleh kosong.`

## Edge Case 4 - Tambah Data Dengan ID Duplikat

Input:
```text
3
ID lokasi baru: G01
ID lokasi baru: P99
...
```

Expected Output:
Program menolak ID yang sudah digunakan dan meminta ID baru lagi.

Actual Output:
```text
ID lokasi sudah digunakan. Masukkan ID lain.
```

Status:
Berhasil

Catatan:
Validasi dilakukan sebelum data dimasukkan ke graph.

## Edge Case 5 - Status Jalan Tidak Valid

Input:
```text
8
Lokasi 1: G01
Lokasi 2: P01
Status: x
Status ulang: 2
```

Expected Output:
Program menolak status yang tidak valid dan meminta pilihan status lagi.

Actual Output:
```text
Pilihan status tidak valid. Silakan pilih 1 untuk Aktif atau 2 untuk Rusak.
```

Status:
Berhasil

Catatan:
Validasi status dilakukan langsung per input.

## Screenshot Output

Screenshot output program disimpan di folder `docs/screenshots/`.

Daftar file:
```text
01-menu-utama.png
02-daftar-lokasi.png
03-jaringan-distribusi.png
03-prioritas-minheap.png
04-dijkstra.png
05-kruskal-mst.png
06-simulasi-jalan-rusak.png
```

## Script Demo Singkat

Pertama, saya menjalankan program melalui terminal.

Di menu utama, terdapat fitur tampil lokasi, search lokasi, tambah data posko, tampil jaringan, prioritas bantuan, Dijkstra, Kruskal MST, dan simulasi jalan rusak.

Saya mulai dari menampilkan daftar lokasi agar terlihat dataset awal yang digunakan. Dataset memiliki 25 node, 43 edge, dan 5 atribut tambahan selain ID dan nama.

Selanjutnya, saya mencoba search lokasi untuk menunjukkan fitur pencarian data.

Kemudian, saya menambahkan posko baru beserta kebutuhan logistik dan menghubungkannya ke jaringan distribusi.

Setelah itu, saya memilih fitur prioritas bantuan. Pada fitur ini, program menggunakan Min-Heap untuk mengambil lokasi dengan prioritas tertinggi.

Kemudian, saya mencoba fitur rute tercepat. Program menggunakan Dijkstra untuk mencari jalur terpendek dari lokasi asal ke tujuan.

Setelah itu, saya menampilkan MST menggunakan Kruskal untuk membentuk jaringan distribusi minimum.

Terakhir, saya menjalankan simulasi jalan rusak untuk menunjukkan bahwa status rute dapat diubah dan memengaruhi jaringan/rute.
