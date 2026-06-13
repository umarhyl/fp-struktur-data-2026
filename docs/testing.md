# Hasil Testing Program

Tanggal testing: 13 Juni 2026
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
Program berhasil dicompile dan dijalankan. Dataset berhasil dimuat dengan total 25 node.

## Test Case 1 - Menampilkan Lokasi

Input:
```text
1
```

Expected Output:
Program menampilkan seluruh data lokasi dari `nodes.csv`.

Actual Output:
Program menampilkan daftar lokasi berisi ID, nama lokasi, tipe, tingkat kritis, dan kebutuhan logistik. Contoh output:
```text
D01 | Desa Kedungcangkring | Tipe: Desa | Tingkat Kritis: 5 | Kebutuhan: 3.5 Ton
G01 | Gudang Pusat Surabaya | Tipe: Gudang | Tingkat Kritis: 1 | Kebutuhan: 0.0 Ton
P01 | Posko Sidoarjo Kota | Tipe: Posko | Tingkat Kritis: 4 | Kebutuhan: 15.5 Ton
```

Status:
Berhasil

Catatan:
Daftar lokasi sudah dirapikan agar tampil urut berdasarkan ID lokasi.

## Test Case 2 - Menampilkan Jaringan Distribusi

Input:
```text
2
```

Expected Output:
Program menampilkan jaringan distribusi berdasarkan data edge dari `edges.csv`.

Actual Output:
Program menampilkan daftar koneksi setiap lokasi beserta jaraknya. Contoh output:
```text
Gudang Pusat Surabaya terhubung ke: Posko Sidoarjo Kota(15.2km) Posko Candi(18.5km)
Posko Sidoarjo Kota terhubung ke: Gudang Pusat Surabaya(15.2km) Posko Candi(4.3km) Posko Tanggulangin(6.7km)
```

Status:
Berhasil

Catatan:
Tidak ada error.

## Test Case 3 - Prioritas Bantuan Min-Heap

Input:
```text
3
```

Expected Output:
Program menampilkan lokasi dengan prioritas bantuan tertinggi.

Actual Output:
Program menampilkan 5 prioritas pengiriman bantuan teratas. Output yang muncul:
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

## Test Case 4 - Rute Tercepat Dijkstra

Input:
```text
4
Asal: G01
Tujuan: D01
```

Expected Output:
Program menampilkan rute tercepat dan total jarak.

Actual Output:
Program menampilkan daftar ID lokasi sebelum meminta input asal dan tujuan. Output rute:
```text
Dari: Gudang Pusat Surabaya
Ke: Desa Kedungcangkring
Total Jarak: 24.0 km
Jalur: Gudang Pusat Surabaya -> Posko Sidoarjo Kota -> Posko Tanggulangin -> Desa Kedungcangkring
```

Status:
Berhasil

Catatan:
Validasi ID berjalan. Jika ID asal atau tujuan tidak ditemukan, program menampilkan pesan bahwa ID tidak valid.
Validasi dilakukan langsung per input. Jika ID asal salah, program langsung meminta ID asal lagi sebelum lanjut ke input tujuan.

## Test Case 5 - Kruskal MST

Input:
```text
5
```

Expected Output:
Program menampilkan jaringan distribusi minimum.

Actual Output:
Program menampilkan daftar jalur MST dan total jarak jaringan. Ringkasan output:
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

## Test Case 6 - Simulasi Jalan Rusak

Input:
```text
6
Lokasi 1: G01
Lokasi 2: P01
Status: 2
```

Expected Output:
Status jalan berubah dan memengaruhi jaringan/rute.

Actual Output:
Program menampilkan daftar ID lokasi sebelum meminta input, lalu meminta status jalan dengan pilihan:
```text
1. Aktif
2. Rusak
```

Setelah memilih `2`, output yang muncul:
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
Input status sudah dibuat lebih aman memakai pilihan angka, bukan `true/false`.
Validasi ID lokasi dan status dilakukan langsung per input. Jika salah, program meminta input yang sama lagi.

## Test Case 7 - Validasi Menu

Input:
```text
abc
8
```

Expected Output:
Program menolak input menu yang tidak valid dan meminta user memilih menu 1-7.

Actual Output:
Program menampilkan pesan:
```text
Pilihan tidak valid. Silakan pilih menu 1-7.
```

Status:
Berhasil

Catatan:
Validasi berlaku untuk input non-angka dan angka di luar pilihan menu.

## Screenshot Output

Screenshot output program sudah disimpan di folder `docs/screenshots/`.

Daftar file sesuai kebutuhan prompt:
```text
01-menu-utama.png
02-daftar-lokasi.png
03-prioritas-minheap.png
04-dijkstra.png
05-kruskal-mst.png
06-simulasi-jalan-rusak.png
```

Tambahan screenshot untuk fitur jaringan distribusi:
```text
03-jaringan-distribusi.png
```

## Script Demo Singkat

Pertama, saya menjalankan program melalui terminal.

Di menu utama, terdapat beberapa fitur utama. Saya mulai dari menampilkan daftar lokasi agar terlihat data yang digunakan.

Selanjutnya, saya memilih fitur prioritas bantuan. Pada fitur ini, program menggunakan Min-Heap untuk mengambil lokasi dengan prioritas tertinggi.

Kemudian, saya mencoba fitur rute tercepat. Program menggunakan algoritma Dijkstra untuk mencari jalur terpendek dari lokasi asal ke tujuan.

Setelah itu, saya menampilkan MST menggunakan Kruskal untuk membentuk jaringan distribusi minimum.

Terakhir, saya menjalankan simulasi jalan rusak untuk menunjukkan bahwa status rute dapat diubah.
