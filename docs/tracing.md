# Tracing Manual - Disaster Relief Distribution Network

## 1. Tracing Proses Tree (Min-Heap)

Proses **Extract-Min** pada Min-Heap untuk mengambil prioritas tertinggi (asumsi isi Heap saat ini: `P03` dengan skor darurat 950, `P02` skor 832, `D04` skor 755). 
*(Catatan: Dalam class `MinHeap` kita menggunakan custom `compareTo` dimana skor darurat yang lebih tinggi dianggap lebih kecil urutannya agar menjadi root).*

1. Root heap saat ini adalah node untuk `P03` (Posko Porong) dengan skor `950.0`.
2. Saat `extractMin()` dipanggil:
   - Nilai dari root (`P03`) diambil untuk di-return (dikirimkan bantuannya).
   - Elemen terakhir di array heap (misalnya `D04`) dipindahkan ke root untuk menggantikan posisi root yang kosong.
   - Ukuran heap dikurangi 1.
3. Heapify-Down (Sift Down):
   - Root baru (`D04`, skor 755) dibandingkan dengan anak-anaknya.
   - Anak kirinya adalah `P02` (skor 832).
   - Karena `832 > 755`, `P02` memiliki prioritas lebih tinggi dibanding `D04` (berdasarkan `compareTo` prioritas darurat).
   - Tukar posisi `D04` dan `P02`.
   - Sekarang `P02` menjadi root baru, `D04` berada di bawahnya.
   - Proses ini berlanjut sampai struktur Min-Heap valid kembali secara keseluruhan.
4. Output: `P03` berhasil dikeluarkan sebagai prioritas utama.

## 2. Tracing Proses Graph (Dijkstra)

Contoh tracing algoritma **Dijkstra** untuk mencari rute pengiriman tercepat dari Gudang Pusat (`G01`) ke Desa Kedungcangkring (`D01`):

1. Inisialisasi jarak sementara (distance array): `G01` = 0, node lain = $\infty$ (tak terhingga).
2. Priority Queue (PQ) diisi node awal: `(G01, 0)`.
3. Pop `G01` dari PQ. Tetangga `G01` yang tersedia:
   - `P01` dengan bobot jarak 15.2 $\rightarrow$ update jarak `P01` = 15.2, push `P01` ke PQ.
   - `P05` dengan bobot jarak 18.5 $\rightarrow$ update jarak `P05` = 18.5, push `P05` ke PQ.
4. Node dengan jarak terdekat dari asal yang belum di-visit di PQ adalah `P01` (jarak 15.2). Pop `P01`. Cek tetangganya:
   - `P05` bobot 4.3 $\rightarrow$ 15.2 + 4.3 = 19.5 (Ini lebih besar dari jarak `P05` saat ini 18.5, maka hiraukan).
   - `P02` bobot 6.7 $\rightarrow$ 15.2 + 6.7 = 21.9 $\rightarrow$ update jarak `P02` menjadi 21.9, push `P02` ke PQ.
   - `D11` bobot 5.2 $\rightarrow$ 15.2 + 5.2 = 20.4 $\rightarrow$ update jarak `D11` menjadi 20.4, push `D11` ke PQ.
   - ... dan seterusnya untuk tetangga lain.
5. Pop node berikutnya dari PQ, misal `P05` (jarak 18.5). Tetangganya dicek, tidak ada update jarak yang lebih pendek untuk `P02` (karena 18.5 + 5.1 = 23.6 > 21.9).
6. Proses diulang (pop dari PQ dan relaksasi tetangga) hingga node `D01` akhirnya di-pop dari PQ yang mana jarak terpendeknya telah final.
7. Total jarak terpendek yang ditemukan menuju `D01` adalah **24.0 km**.
   Hasil trace *previous node* (jalur mundur) didapatkan: `D01` $\leftarrow$ `P02` $\leftarrow$ `P01` $\leftarrow$ `G01`.
   **Jalur Final**: `Gudang Pusat Surabaya -> Posko Sidoarjo Kota -> Posko Tanggulangin -> Desa Kedungcangkring`.

## 3. Tracing Edge Case (Graph Terputus)

Skenario Edge Case: Ada lokasi baru yang terisolasi, misalnya Posko `PX2` baru ditambahkan tapi tidak diberi jalur ke manapun (tidak terhubung ke jaringan distribusi utama).
Tracing saat menjalankan **Cek Konektivitas Jaringan (BFS/Traversal)**:

1. Sistem menyiapkan `visited set` (daftar node yang sudah dikunjungi).
2. Iterasi daftar lokasi: Ambil node pertama yang belum ada di `visited`, misal `G01`.
3. Mulai **BFS dari G01**:
   - `queue` diisi `G01`, tandai `G01` telah `visited`.
   - Pop `G01`, periksa edge aktif. Tetangganya `P01`, `P05` dimasukkan ke `queue` dan ditandai `visited`.
   - Proses ini menyebar ke seluruh graph selagi ada jalur aktif. BFS akhirnya berhenti.
4. Karena `G01` terhubung dengan 24 lokasi lain, terbentuk **Komponen 1** (total 25 node).
5. Iterasi kembali ke daftar lokasi utama. Ditemukan `PX2` ternyata belum masuk ke `visited`.
6. Mulai **BFS dari PX2**:
   - `queue` diisi `PX2`, tandai `PX2` telah `visited`.
   - Pop `PX2`, periksa edge. Karena tidak ada jalur/edge dari `PX2`, antrean kosong.
   - BFS dari `PX2` berhenti. Terbentuk **Komponen 2** yang hanya berisi 1 node (`PX2`).
7. Pemeriksaan iterasi selesai. Karena jumlah komponen yang terbentuk $> 1$ (yaitu 2 komponen), program langsung menyimpulkan: **Status: TERPUTUS** (Jaringan terpecah menjadi 2 komponen terpisah).
