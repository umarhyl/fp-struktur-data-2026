# Laporan Kecil Final Project Struktur Data

## 1. Judul Project

**Disaster Relief Distribution Network**

Project ini merupakan sistem sederhana berbasis Java untuk membantu distribusi bantuan bencana dari gudang utama ke posko dan desa terdampak.

## 2. Nama Anggota Kelompok

- 
- 
- 
- 
- 

## 3. Deskripsi Masalah

Dalam kondisi bencana, bantuan logistik harus dikirim dengan cepat dan tepat ke lokasi yang paling membutuhkan. Masalah yang muncul adalah banyak lokasi terdampak memiliki tingkat kebutuhan berbeda, jalur distribusi bisa rusak, dan rute terbaik bisa berubah sewaktu-waktu.

Project ini dibuat untuk membantu:

- menentukan prioritas lokasi penerima bantuan,
- mencari rute pengiriman tercepat,
- merancang jaringan distribusi minimum,
- mensimulasikan jalan rusak,
- mengecek apakah jaringan distribusi masih terhubung.

## 4. Dataset

Dataset dibuat sendiri dan disimpan dalam folder `data/`.

File dataset:

- `data/nodes.csv`
- `data/edges.csv`

Dataset awal berisi:

- 25 node/lokasi
- 43 edge/jalur distribusi

Atribut pada node:

- `id`
- `name`
- `type`
- `population`
- `criticalLevel`
- `logisticsNeeded`
- `riskLevel`

Contoh lokasi:

- `G01` - Gudang Pusat Surabaya
- `P01` - Posko Sidoarjo Kota
- `D01` - Desa Kedungcangkring

## 5. Struktur Graph yang Digunakan

Struktur graph digunakan untuk merepresentasikan jaringan distribusi bantuan.

Implementasi graph terdapat pada:

```text
src/graph/Graph.java
```

Graph yang digunakan adalah **undirected weighted graph**.

- Node merepresentasikan gudang, posko, dan desa.
- Edge merepresentasikan jalan distribusi.
- Bobot edge merepresentasikan jarak dalam kilometer.
- Setiap edge memiliki status aktif atau rusak.

Graph disimpan menggunakan **adjacency list** karena jaringan jalan tidak menghubungkan semua lokasi secara langsung.

## 6. Struktur Tree yang Digunakan

Struktur tree yang digunakan adalah **Min-Heap**.

Implementasi terdapat pada:

```text
src/tree/MinHeap.java
```

Min-Heap digunakan untuk menentukan prioritas pengiriman bantuan. Prioritas dihitung menggunakan skor darurat:

```text
Skor = (Kritis x 100) + (Risiko x 40) + (Kebutuhan x 5) + (Populasi / 100)
```

Lokasi dengan skor paling tinggi dianggap paling prioritas untuk menerima bantuan.

## 7. Algoritma yang Digunakan

Algoritma yang digunakan:

1. **Dijkstra**
   - Digunakan untuk mencari rute pengiriman tercepat dari lokasi asal ke tujuan.
   - File: `src/graph/Dijkstra.java`

2. **Kruskal MST**
   - Digunakan untuk membuat jaringan distribusi minimum.
   - File: `src/graph/KruskalMST.java`

3. **Traversal Graph**
   - Digunakan untuk mengecek apakah jaringan masih terhubung atau terputus.
   - Fitur ini ada pada menu cek konektivitas jaringan.

4. **Min-Heap**
   - Digunakan untuk mengambil lokasi dengan prioritas bantuan tertinggi.
   - File: `src/tree/MinHeap.java`

## 8. Design Decision Log

Beberapa keputusan desain:

- Menggunakan adjacency list karena graph jaringan jalan bersifat tidak penuh dan lebih hemat memori.
- Menggunakan Min-Heap karena sistem perlu mengambil lokasi prioritas tertinggi secara cepat.
- Menggunakan Dijkstra karena jalur distribusi memiliki bobot jarak.
- Menggunakan Kruskal MST untuk mencari jaringan minimum yang tetap menghubungkan lokasi.
- Menambahkan `riskLevel` agar dataset lebih realistis untuk konteks bencana.
- Menyimpan tambah dan update data ke CSV agar data tetap ada setelah program ditutup.
- Menambahkan cek konektivitas agar kondisi graph terputus bisa didemokan secara jelas.

## 9. Tracing Manual

Sesuai ketentuan, tracing manual untuk 1 proses Tree, 1 proses Graph, dan 1 Edge case telah dipisahkan ke dalam dokumen tersendiri.
Silakan lihat file: `docs/tracing.md` (atau `tracing.pdf` jika sudah diexport).

## 10. Screenshot Hasil Program

Screenshot hasil program disimpan pada folder:

```text
docs/screenshots/
```

Daftar screenshot:

- `01-menu-utama.png`
- `02-daftar-lokasi.png`
- `03-jaringan-distribusi.png`
- `03-prioritas-minheap.png`
- `04-dijkstra.png`
- `05-kruskal-mst.png`
- `06-simulasi-jalan-rusak.png`
- `07-konektivitas-jaringan.png`
- `08-update-kebutuhan-logistik.png`

## 11. Analisis Kompleksitas

### Min-Heap

- Insert: `O(log n)`
- Extract min: `O(log n)`
- Digunakan untuk menentukan prioritas bantuan.

### Dijkstra

Kompleksitas:

```text
O((V + E) log V)
```

Keterangan:

- `V` adalah jumlah node.
- `E` adalah jumlah edge.

### Kruskal MST

Kompleksitas:

```text
O(E log E)
```

Bagian paling mahal adalah proses sorting edge berdasarkan bobot.

### Cek Konektivitas

Kompleksitas:

```text
O(V + E)
```

Karena setiap node dan edge aktif dicek untuk menentukan komponen jaringan.

## 12. What-if Analysis & Aspek HOTS

### Aspek HOTS: Kapan Menggunakan MST vs Dijkstra
Dalam sistem pengiriman bantuan logistik:
- **Dijkstra** digunakan saat kita perlu mengirim bantuan spesifik dari **satu titik asal ke satu titik tujuan**. Misalnya, sebuah truk dari Gudang Pusat (`G01`) ingin menuju Desa Kedungcangkring (`D01`). Dijkstra menjamin kita menemukan jalur tersingkat agar pengiriman cepat sampai. Fokus utamanya adalah **rute perjalanan optimal**.
- **Minimum Spanning Tree (Kruskal)** digunakan saat kita merencanakan **pemulihan infrastruktur secara keseluruhan**. Misalnya, setelah gempa, banyak jalan rusak dan pemerintah hanya punya anggaran terbatas. MST memastikan seluruh posko dan desa bisa terhubung kembali ke jaringan utama dengan **total biaya perbaikan jalan (total jarak) paling minimum**. Fokus utamanya adalah **konektivitas jaringan minimal**, bukan rute tercepat antar dua titik.

### Skenario What-if
Skenario what-if yang tersedia:

### Jalan Rusak

User dapat memilih dua lokasi dan mengubah status jalur menjadi rusak.

Contoh:

```text
G01 - P01 dibuat rusak
```

Dampaknya:

- jalur tersebut tidak dipakai oleh Dijkstra,
- jaringan distribusi menampilkan status rusak,
- rute pengiriman dapat berubah.

### Graph Terputus

Jika ada lokasi yang tidak terhubung ke jaringan aktif, fitur cek konektivitas akan menampilkan bahwa graph terputus.

Output contoh:

```text
Status: TERPUTUS
Jaringan aktif terpecah menjadi 2 komponen.
```

Fitur ini membantu menjelaskan kondisi ketika bantuan tidak bisa dikirim ke lokasi tertentu karena tidak ada jalur aktif.

## 13. Kesimpulan

Project ini berhasil mengimplementasikan konsep Tree dan Graph untuk menyelesaikan masalah distribusi bantuan bencana.

Sistem sudah memiliki fitur utama:

- menampilkan dataset lokasi,
- mencari lokasi,
- menambah data posko,
- update kebutuhan logistik,
- mencari rute tercepat dengan Dijkstra,
- menentukan prioritas bantuan dengan Min-Heap,
- membuat jaringan minimum dengan Kruskal MST,
- simulasi jalan rusak,
- cek konektivitas jaringan.

Dengan fitur tersebut, program sudah sesuai dengan topik **Disaster Relief Distribution Network** dan memenuhi kebutuhan utama final project Struktur Data.
