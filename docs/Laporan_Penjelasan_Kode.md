# Laporan Penjelasan Kode Sumber (Source Code)
**Topik:** Disaster Relief Distribution Network (Opsi 8)

Dokumen ini berisi pembedahan dan penjelasan mendetail dari seluruh arsitektur kode dan algoritma yang digunakan dalam proyek ini, disusun selayaknya laporan teknis.

---

## 1. Package `model` (Representasi Data)

### 1.1. `LocationNode.java`
Kelas ini berfungsi sebagai cetakan (blueprint) untuk setiap titik lokasi di peta (Gudang, Posko, atau Desa).
*   **Atribut:**
    *   `id` (String): Kode unik lokasi (contoh: "G01", "P02").
    *   `name` (String): Nama lokasi sebenarnya.
    *   `type` (String): Kategori lokasi (Gudang/Posko/Desa).
    *   `population` (int): Jumlah pengungsi.
    *   `criticalLevel` (int): Tingkat kedaruratan dari 1 (Aman) hingga 5 (Kritis).
    *   `logisticsNeeded` (double): Kebutuhan suplai dalam satuan Ton.
*   **Penjelasan Logika:** Kelas ini murni berupa POJO (Plain Old Java Object) yang hanya berisi konstruktor dan fungsi-fungsi *getter* untuk mengambil nilai-nilai dari entitas. Fungsi `toString()` di-_override_ untuk memudahkan pencetakan objek ke terminal.

### 1.2. `RouteEdge.java`
Kelas ini merepresentasikan jalan raya / relasi yang menghubungkan dua `LocationNode`.
*   **Atribut:**
    *   `sourceId` & `destinationId` (String): ID dari dua lokasi yang terhubung.
    *   `distance` (double): Berfungsi sebagai bobot (*weight*) dari relasi / jarak jalan dalam kilometer.
    *   `active` (boolean): Flag yang sangat krusial untuk fitur simulasi *What-If*. Jika `true`, jalan bisa dilewati. Jika `false`, jalan terputus/rusak.
*   **Penjelasan Logika:** Terdapat method khusus `setActive()` yang akan dimanipulasi secara dinamis saat eksekusi (*Runtime*) untuk mensimulasikan bencana susulan.

---

## 2. Package `tree` (Struktur Data Hierarkis)

### 2.1. `MinHeap.java`
Kelas ini mengimplementasikan struktur data Pohon Biner (*Binary Tree*) spesifik yaitu **Min-Heap**, sepenuhnya dari awal (tanpa library Java).
*   **Generik:** Dideklarasikan sebagai `class MinHeap<T extends Comparable<T>>` sehingga tipe data apapun yang bisa dibandingkan bisa dimasukkan ke dalam Heap.
*   **Penyimpanan:** Alih-alih membuat node yang saling terhubung menggunakan pointer kiri/kanan, Min-Heap di sini diimplementasikan menggunakan `ArrayList`.
    *   Anak kiri dari indeks `i` dicari menggunakan rumus `(2 * i) + 1`.
    *   Anak kanan dicari dengan `(2 * i) + 2`.
    *   Induk dicari dengan `(i - 1) / 2`.
*   **Metode Utama:**
    *   `insert()`: Menambahkan elemen baru ke akhir array, kemudian memanggil `heapifyUp()` untuk mengangkat elemen tersebut ke atas sampai posisinya benar.
    *   `extractMin()`: Mengambil elemen di akar (root/index ke-0) yang selalu merupakan nilai paling minimal, menukar posisi elemen terakhir ke akar, lalu memanggil `heapifyDown()` agar struktur pohon kembali valid.
*   **Kompleksitas (Time Complexity):** Penyisipan (Insert) bernilai **O(log N)**, dan Pengambilan (Extract) juga bernilai **O(log N)**. Pencarian nilai minimum selalu **O(1)**.

---

## 3. Package `graph` (Logika Jaringan)

### 3.1. `Graph.java`
Bertugas mengelola jaringan dengan implementasi **Adjacency List** (Daftar Ketetanggaan).
*   **Atribut:** Menggunakan `HashMap<String, LocationNode>` untuk memetakan ID secara instan dengan O(1). Dan menggunakan `HashMap<String, List<RouteEdge>>` sebagai representasi Adjacency List.
*   **Penjelasan Logika:**
    *   `addEdge()`: Karena jalan antar desa bisa dilewati dua arah (*Undirected Graph*), kode secara eksplisit memasukkan edge ke jalur A->B sekaligus ke jalur B->A.
    *   `toggleEdge()`: Mencari rute spesifik antara dua titik, lalu mengganti atribut `active` miliknya. Berguna untuk simulasi pemutusan jalan.

### 3.2. `Dijkstra.java`
Berfungsi menemukan rute penyaluran bantuan tercepat dari satu lokasi ke lokasi lain.
*   **Penjelasan Logika:**
    *   Membuat pemetaan jarak (`distances`) dari titik awal ke semua titik. Titik lain diinisialisasi ke nilai tak terhingga (`Double.MAX_VALUE`). Titik awal diinisialisasi `0.0`.
    *   Memanfaatkan implementasi `MinHeap` khusus yang kita buat untuk menampung kelas pembantu berwujud `DijkstraNode`. Titik dengan jarak terdekat akan selalu berada di puncak antrean (*Priority Queue*).
    *   Melakukan relaksasi/pengecekan: Saat membedah lokasi saat ini, ia akan berkeliling ke semua tetangganya yang *Aktif/Tidak Rusak*. Jika jarak dari titik saat ini ditambah beban jarak tetangga **lebih kecil** dari catatan jarak si tetangga sebelumnya, maka rekor jarak terbaru tersebut akan dicatat di `distances`.
    *   Dilengkapi dengan *Tracker* `Map<String, String> previous` untuk mencatat rekam jejak. Sehingga setelah sampai tujuan, kita bisa menelusuri ke belakang untuk menghasilkan rute cetak.
*   **Kompleksitas:** **O((V + E) log V)** karena memadukan Priority Queue dan Adjacency List.

### 3.3. `KruskalMST.java`
Berfungsi merancang rantai pasok paling hemat (mengeliminasi rute redundan/berlebihan namun semua tetap terhubung).
*   **Penjelasan Logika:**
    *   Kelas ini memuat struktur tambahan **Disjoint-Set (Union-Find)**. Terdapat metode `find()` dengan *Path-Compression* dan `union()` dengan *Rank*. Keduanya krusial agar pencarian jalur *cycle* (siklus putaran) bisa terjadi dengan sangat cepat.
    *   Algoritma mengumpulkan semua Edge dari graf, lalu **mengurutkannya (Sort)** dari bobot jarak yang terpendek hingga terpanjang.
    *   Satu per satu rute yang paling pendek diambil. Menggunakan metode `find()`, Kruskal akan mengecek: *"Apakah menghubungkan dua titik ini akan menciptakan lingkaran (cycle)?"* Jika tidak ada lingkaran, maka sambungan tersebut dimasukkan ke dalam daftar jaringan minimum (`mst.add(edge)`). Jika membentuk lingkaran (artinya mereka sudah terhubung dari jalur lain sebelumnya), maka jalan diabaikan.
*   **Kompleksitas:** **O(E log V)** atau **O(E log E)** akibat proses *sorting* dari rute yang tersedia.

---

## 4. Akar Sistem

### 4.1. `Main.java`
*   **CSV Parsing:** Metode `loadData()` memuat baris demi baris dari `nodes.csv` dan `edges.csv` yang dipisah melalui `line.split(",")` lalu dimampatkan menjadi objek Java utuh.
*   **Sistem CLI:** Berjalan dalam *looping* tanpa henti (`while(true)`) menggunakan `Scanner` Java standar.
*   **Manipulasi Heap:** Pada fitur 3 (Prioritaskan Pengiriman), `Main.java` membuat antrean menggunakan rumus ajaib `(6 - TingkatKritis)` untuk merubah Skala 5 (Maksimal) menjadi angka 1 (Prioritas Paling Atas di sebuah Min-Heap). Sehingga, node paling kritis dipastikan akan keluar paling pertama dari antrean!
