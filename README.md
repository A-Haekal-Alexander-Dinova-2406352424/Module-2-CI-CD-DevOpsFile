# eShop

## Refleksi 1

Saat membangun fitur EShop (create/list/edit/delete), saya berusaha menjaga kode tetap rapi dengan memisahkan tanggung jawab ala MVC ke paket `controller`, `service`, `repository`, dan `model`. Pemisahan ini membuat setiap class punya peran yang jelas: controller mengurus HTTP/HTML, service memegang logika bisnis, dan repository menangani penyimpanan data (saat ini masih in-memory). Saya menggunakan penamaan yang deskriptif seperti `ProductServiceImpl` dan method yang kecil seperti `productListPage()` supaya alurnya mudah dibaca sebagai rangkaian use-case. Untuk maintainability, saya menghindari duplikasi proses "ambil semua produk lalu ubah jadi list" dengan memusatkannya di layer service. Dari sisi secure coding yang sederhana, server yang menghasilkan `productId` (UUID) saat create, bukan menerima ID dari client, sehingga pengguna tidak bisa menentukan ID sesuka hati ketika membuat produk baru. Saya juga menjaga model data tetap bertipe tegas (misalnya quantity sebagai `int`) dan menggunakan endpoint POST untuk aksi yang mengubah state. Kekurangan yang masih ada adalah validasi input masih banyak bergantung pada validasi HTML (`required`, `min`) di sisi client; di aplikasi nyata sebaiknya ada validasi di server (misalnya Bean Validation) dan penanganan error yang baik saat input tidak valid. Peningkatan lain yang penting adalah menambahkan autentikasi/otorisasi sebelum mengizinkan edit/delete, serta menghindari mengekspos objek mutable secara langsung dari repository agar perubahan tidak "bocor" tanpa kontrol.

## Refleksi 2

1. Setelah menulis unit test, saya merasa lebih percaya diri karena perubahan di model/repository bisa cepat terdeteksi. Menurut saya, jumlah unit test yang "ideal" di sebuah class tidak bisa ditentukan dengan angka tetap; lebih masuk akal jika mengikuti banyaknya perilaku (behavior) dan edge case yang dimiliki class tersebut. Saya biasanya mengelompokkan test berdasarkan perilaku dan memastikan setiap test fokus pada satu skenario. Selain happy path, saya juga mencoba menambahkan skenario negatif (misalnya update/delete untuk produk yang tidak ada) karena di situlah bug sering muncul. Untuk menilai apakah test sudah "cukup", saya memakai code coverage sebagai petunjuk awal, lalu mengecek lagi apakah aturan bisnis penting dan batasan input benar-benar diuji dengan assertion yang kuat. Coverage bisa menunjukkan baris mana yang belum tersentuh, tetapi tidak menjamin kualitas assertion atau kombinasi input yang diuji. Jadi, 100% coverage tidak otomatis berarti program bebas bug; test bisa meleset dari requirement, assertion bisa terlalu lemah, atau masalah integrasi (mapping HTTP dan template rendering) belum teruji.

2. Kalau saya menyalin setup yang sama (misalnya `serverPort`, `baseUrl`, `@Value`, dan `setupTest`) ke class functional test baru, kodenya akan cepat jadi repetitif dan lebih sulit dirawat. Duplikasi ini adalah clean code smell (melanggar prinsip DRY), karena perubahan kecil pada setup (contohnya format base URL) harus diperbarui di banyak tempat. Boilerplate yang berulang juga bisa mengaburkan intent dari tiap test, sehingga test jadi kurang enak dibaca. Dalam jangka panjang, setup yang diduplikasi bisa "ngedrift" dan memicu flaky test yang perilakunya tidak konsisten antar suite. Pendekatan yang lebih bersih adalah mengekstrak setup bersama ke base class/helper, membuat JUnit 5 extension, atau menerapkan Page Object pattern untuk halaman-halaman yang diuji, sehingga tiap suite fokus pada interaksi dan assertion saja. Dengan cara ini, menambah test seperti "menghitung jumlah item di list" cukup menambah method test (atau class kecil) tanpa mengulang boilerplate.

## Refleksi Module 2 (CI/CD & DevOps)

1. Salah satu isu code quality yang terdeteksi saat menambahkan tool scanning adalah temuan PMD `UseUtilityClass` pada `EshopApplication`. Setelah saya cek, itu lebih merupakan false-positive karena class tersebut adalah entrypoint sekaligus konfigurasi Spring Boot (bukan utility class murni) dan mengubahnya menjadi private-constructor dapat merusak proses bootstrap Spring. Strategi saya adalah memastikan dulu rule yang memunculkan temuan (menggunakan ruleset quickstart yang sama dengan workflow), lalu memilih perbaikan yang paling minim dan tidak mengubah perilaku runtime aplikasi. Saya melakukan refactor kecil pada entrypoint dengan memindahkan `SpringApplication.run(...)` ke method instance `run(...)` yang dipanggil dari `main`, sehingga class tidak lagi terdeteksi sebagai utility class oleh PMD tanpa perlu suppression. Setelah itu saya verifikasi ulang bahwa PMD tidak lagi melaporkan isu yang sama di run berikutnya. Selain itu, untuk meningkatkan kualitas pengujian dan mendorong stabilitas CI, saya menambahkan unit test tambahan untuk menutup edge case di service/repository dan entrypoint aplikasi sehingga coverage JaCoCo naik signifikan (dari sekitar 49% menjadi 100%). Saya juga memperbaiki bug input quantity yang terlalu besar (sebelumnya memicu Bad Request 400) dengan memperluas tipe quantity dan menambahkan handling binding error pada controller agar user mendapat feedback dari form, bukan error page.

2. Menurut saya, workflow yang ada sekarang sudah memenuhi definisi Continuous Integration, karena setiap push dan pull request akan menjalankan proses build/test secara otomatis di environment yang konsisten (runner GitHub Actions). Ini membantu memastikan perubahan kecil segera terintegrasi dan tervalidasi, sehingga error bisa terdeteksi lebih awal sebelum masuk ke branch utama. Untuk aspek Continuous Deployment, saya menambahkan workflow deploy yang otomatis berjalan ketika ada perubahan di `main` dan melakukan deploy ke PaaS (Koyeb) setelah test lulus. Workflow ini menggunakan secret `KOYEB_API_TOKEN` untuk autentikasi; jika secret atau konfigurasi service di PaaS belum dipasang, langkah deploy akan diskip dan kondisinya lebih mirip Continuous Delivery (siap deploy, tetapi masih ada setup/manual step yang diperlukan). Dengan begitu, pipeline ini sudah punya komponen CI yang kuat, dan komponen CD yang otomatis setelah prasyarat PaaS terpenuhi.

## Refleksi Module 3 (Maintainability & SOLID)

1. Prinsip SOLID yang saya terapkan:
   - SRP (Single Responsibility Principle): saya memisahkan handler untuk `Car` dari `Product` dengan membuat `CarController` terpisah, dan memindahkan aturan bisnis (generate `id`) ke layer service supaya repository fokus pada penyimpanan data. Concern "logging/print id" saya pisahkan ke komponen khusus (`IdLogger`) agar controller tidak mencampur concern logging dengan alur request.
   - DIP (Dependency Inversion Principle): controller bergantung pada abstraksi (`CarService`), dan service bergantung pada abstraksi (`CarRepository`). Implementasi konkretnya (`CarServiceImpl`, `InMemoryCarRepository`) di-inject oleh Spring. Pola yang sama saya terapkan pada logging: controller bergantung pada interface `IdLogger`, bukan pada implementasi logging tertentu (misalnya `Slf4jIdLogger`).
   - OCP (Open-Closed Principle): dengan adanya abstraksi `CarRepository`, saya bisa menambah implementasi repository baru (misalnya repository berbasis database) tanpa perlu mengubah kode di controller/service.
   - LSP (Liskov Substitution Principle): saya menghilangkan pewarisan `CarController extends ProductController` karena tidak merepresentasikan relasi substitusi yang tepat dan berpotensi membawa perilaku yang tidak relevan.
   - ISP (Interface Segregation Principle): interface dibuat kecil dan spesifik agar client hanya bergantung pada method yang dibutuhkan (contoh: `CarRepository` dan `IdLogger`).

2. Keuntungan menerapkan SOLID pada project ini (dengan contoh):
   - Perubahan lebih terlokalisasi: contoh, mengganti storage `Car` dari in-memory ke database cukup menambah implementasi `CarRepository` baru tanpa mengubah `CarController`.
   - Lebih mudah di-test: `CarServiceImpl` dapat diuji dengan membuat stub/mock `CarRepository`, karena dependensinya berupa interface.
   - Coupling lebih rendah: controller tidak lagi mengikat diri ke class implementasi spesifik, sehingga refactor service/repository lebih aman.
   - Logging lebih fleksibel: saya bisa mengganti cara mencetak `id` (misalnya dari console ke file/monitoring) dengan mengganti implementasi `IdLogger` tanpa mengubah kode controller.

3. Kerugian jika tidak menerapkan SOLID pada project ini (dengan contoh):
   - Coupling tinggi dan ripple effect: contoh, jika controller langsung bergantung pada `CarServiceImpl`, perubahan internal service mudah "bocor" ke layer atas dan memaksa perubahan di banyak tempat.
   - Desain pewarisan yang salah meningkatkan risiko bug: endpoint atau behavior yang seharusnya khusus `Product` bisa ikut "terbawa" saat controller lain mewarisi controller tersebut.
   - Tanggung jawab bercampur lintas layer: contoh, jika generate `id` diletakkan di repository, aturan bisnis dan persistence tercampur sehingga reasoning lebih sulit dan potensi duplikasi meningkat saat fitur berkembang.

## Refleksi Module 4 (Refactoring & TDD)

1. Menurut saya, alur TDD di module ini cukup berguna karena memaksa saya menjabarkan requirement kecil-kecil dulu sebelum menulis implementasi. Saat mengerjakan `Order`, `Payment`, dan validasi sub-fitur pembayaran, test lebih dulu membantu saya memastikan perilaku yang benar-benar penting: status default, penolakan input tidak valid, update status yang berdampak ke order terkait, dan perbedaan happy path vs unhappy path. Pendekatan ini juga membuat refactor terasa lebih aman; ketika saya memindahkan hardcoded string ke enum dan mengekstrak validator pembayaran, saya bisa segera mengecek apakah perilaku luar tetap sama. Kalau ada hal yang perlu saya perbaiki lain kali, itu adalah menjaga siklus tetap lebih kecil lagi. Di beberapa bagian saya masih cenderung menulis beberapa test sekaligus sebelum kembali ke implementasi. Ke depan, saya perlu lebih disiplin membuat satu perilaku kecil, membuatnya hijau, lalu refactor segera, supaya feedback loop tetap cepat dan desain yang muncul benar-benar dipandu oleh test.

2. Secara umum, test yang saya tulis sudah cukup mengikuti prinsip F.I.R.S.T. `Fast`: seluruh unit test berjalan cepat karena repository masih in-memory dan dependency eksternal dimock dengan Mockito. `Independent`: tiap test membuat data sendiri di `setUp` dan tidak bergantung pada urutan eksekusi test lain. `Repeatable`: hasilnya konsisten karena tidak tergantung jaringan, database, atau state global yang berubah-ubah. `Self-validating`: semua test menggunakan assertion/verifikasi yang jelas, jadi hasil pass/fail bisa dibaca tanpa inspeksi manual. `Timely`: pada Module 4 saya menulis test lebih dulu sebelum implementasi utama dan sebelum refactor besar. Meski begitu, masih ada ruang perbaikan. Model `Order` saat ini masih membuat `UUID` dan timestamp langsung dari dalam class; test saya hanya mengecek bahwa nilainya ada, bukan mengontrol sumber waktunya. Kalau logika waktu nanti makin kompleks, saya sebaiknya menginjeksi `Clock` atau abstraksi generator ID agar test tetap cepat, deterministik, dan lebih presisi.
## Link Hasil Deploy
dule-2-ci-cd-devopsfile-eshop-paling-keren-7bc4b742.koyeb.app

## Bonus Reflection (Bonus 2)

1. Menurut saya, kode partner saya sudah fungsional dan test coverage-nya kuat, jadi perubahan perilaku gampang terdeteksi. Namun, masih ada beberapa aspek yang bisa ditingkatkan dari sisi maintainability: logic validasi pembayaran di `PaymentServiceImpl` cenderung "branchy" (banyak `if` per method), masih banyak string literal untuk method/status/key, serta ada bagian service yang bisa dibuat lebih konsisten (misalnya gaya dependency injection dan error handling).

2. Kontribusi saya ke kode partner:
   - Melakukan refactor di branch `refactor/2406352424` pada repository partner dan membuat PR `#21` ke branch `order`.
   - Menambah struktur validator pembayaran (strategy) agar validasi tiap method terpisah dan lebih mudah dikembangkan.
   - Merapikan `OrderServiceImpl` (constructor injection, simplify update status) dan memperbaiki beberapa validasi/utility di `Order` dan `OrderStatus`.
   - Memberikan review (inline comments) pada PR partner `#20` (order -> main) sebagai masukan per baris.

3. Code smells yang saya temukan:
   - Long conditional / kompleksitas cabang pada validasi payment method (mudah membesar saat method bertambah).
   - Magic strings (status/method/key) yang tersebar.
   - Inconsistency pada dependency injection (field injection vs constructor injection).
   - Unnecessary object creation pada update status order (membuat `Order` baru hanya untuk mengganti status).
   - API yang rawan NPE (contoh: `createOrder` mengembalikan `null` untuk duplikasi ID) dan exception tanpa message.

4. Langkah refactor yang saya sarankan dan eksekusi:
   - Mengekstrak validasi payment data menjadi interface `PaymentDataValidator` dengan implementasi per method (voucher/bank transfer/cash on delivery), lalu `PaymentServiceImpl` memilih validator berdasarkan method.
   - Mengubah `OrderServiceImpl` menjadi constructor injection dan menyederhanakan `updateStatus` menjadi update status pada object yang sama lalu `save`.
   - Memperkuat validasi pada `Order` (guard clause untuk products kosong) dan memperbaiki `OrderStatus.contains` agar lebih robust (handle `null` dan cek value enum).
   - Saran lanjutan (belum dieksekusi karena berpotensi mengubah behavior yang sudah dites): hindari `return null` pada `createOrder`, serta ganti magic strings menjadi enum/konstanta terpusat.

Bukti/Link:
- PR partner yang saya review (inline comments ada di tab Files changed): https://github.com/A-Fakhri-Husaini-Romza-2406436972/Modul-1-Coding-Standards/pull/20
- PR refactor yang saya buat (refactor/2406352424 -> order): https://github.com/A-Fakhri-Husaini-Romza-2406436972/Modul-1-Coding-Standards/pull/21
