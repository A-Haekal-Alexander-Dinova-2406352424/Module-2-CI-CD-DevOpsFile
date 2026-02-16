# Kepatuhan Riwayat Commit (Tutorial 1 / `70-90.pdf`)

Dokumen ini memetakan **checkpoint commit yang diminta** di `70-90.pdf` (halaman **69-89**) ke **commit dan merge commit** yang benar-benar ada di repository ini.

## Cara Verifikasi (perintah)

- Lihat graf lengkap: `git log --graph --decorate --oneline --all`
- Lihat file yang berubah pada sebuah commit: `git show --name-status <commit>`
- Buktikan merge commit eksplisit (punya 2 parent): `git show -s --pretty=format:"%h parents:%p %s" <merge_commit>`

## Scaffold Proyek (PDF hlm 69-71)

Kebutuhan PDF: buat project Spring Boot, buat repository Git, commit, dan push.

- Commit: `83ccb72` - `Add Spring Boot project scaffold`
  - Menambahkan file hasil generate project (Gradle wrapper, `build.gradle.kts`, `src/`, dan lain-lain).

## Iterasi Fitur 1 - Branch `list-product` (PDF hlm 72-76)

Kebutuhan PDF: buat branch `list-product`, lalu commit tiap langkah.

### Model: `Product.java` (PDF hlm 72)
- Branch: `list-product`
- Commit: `e0a537e` - `Add Product model`
  - Menambahkan: `src/main/java/id/ac/ui/cs/advprog/eshop/model/Product.java`

### Repository: `ProductRepository.java` (PDF hlm 73)
- Branch: `list-product`
- Commit: `eb5db30` - `Add Product repository`
  - Menambahkan: `src/main/java/id/ac/ui/cs/advprog/eshop/repository/ProductRepository.java`

### Service: `ProductService.java` + `ProductServiceImpl.java` (PDF hlm 74)
- Branch: `list-product`
- Commit: `a391db0` - `Add Product service`
  - Menambahkan: `src/main/java/id/ac/ui/cs/advprog/eshop/service/ProductService.java`
  - Menambahkan: `src/main/java/id/ac/ui/cs/advprog/eshop/service/ProductServiceImpl.java`

### Controller: `ProductController.java` (PDF hlm 75)
- Branch: `list-product`
- Commit: `e24aa36` - `Add Product controller`
  - Menambahkan: `src/main/java/id/ac/ui/cs/advprog/eshop/controller/ProductController.java`

### Templates: `createProduct.html` + `productList.html` (PDF hlm 76)
- Branch: `list-product`
- Commit: `a80d8d4` - `Add product templates`
  - Menambahkan: `src/main/resources/templates/createProduct.html`
  - Menambahkan: `src/main/resources/templates/productList.html`

### Merge `list-product` -> `main` (PDF hlm 77)
- Merge commit eksplisit di `main`: `92de5ea` - `Merge pull request #1 from .../list-product`
  - Bukti (2 parent): `92de5ea parents:83ccb72 a80d8d4 Merge pull request #1 from A-Haekal-Alexander-Dinova-2406352424/list-product`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/backup/pull/1

## Exercise 1 - Fitur Edit/Delete (PDF hlm 77)

Kebutuhan PDF: buat branch `edit-product` dan `delete-product`, commit, push, lalu merge ke `main`.

### Branch `edit-product`
- Commit: `4ca5cfe` - `Add edit product feature`
- Merge commit eksplisit di `main`: `127986f` - `Merge pull request #2 from .../edit-product`
  - Bukti (2 parent): `127986f parents:92de5ea 4ca5cfe Merge pull request #2 from A-Haekal-Alexander-Dinova-2406352424/edit-product`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/backup/pull/2

### Branch `delete-product`
- Commit: `bc3dbcf` - `Add delete product feature`
- Merge commit eksplisit di `main`: `f4aea52` - `Merge pull request #3 from .../delete-product`
  - Bukti (2 parent): `f4aea52 parents:127986f bc3dbcf Merge pull request #3 from A-Haekal-Alexander-Dinova-2406352424/delete-product`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/backup/pull/3

## Persiapan Testing di `main` (PDF hlm 78)

Kebutuhan PDF: update `build.gradle.kts` (versi + dependencies + task `unitTest`/`functionalTest`), commit, push.

- Commit: `15d7e3c` - `Add unit and functional test setup`
  - Memodifikasi: `build.gradle.kts`
- Commit: `002ded7` - `Fix Gradle test task config`
  - Memodifikasi: `build.gradle.kts`
  - Catatan: commit ini memperbaiki konfigurasi Kotlin DSL supaya task `unitTest` dan `functionalTest` bisa berjalan.

## Unit Test - Branch `unit-test` (PDF hlm 79-81 + Exercise 2 hlm 88)

Kebutuhan PDF: buat branch `unit-test`, commit unit test, tambah unit test untuk edit/delete, commit, push, merge.

### `ProductTest.java` (PDF hlm 79)
- Branch: `unit-test`
- Commit: `8fa60be` - `Add Product unit tests`
  - Menambahkan: `src/test/java/id/ac/ui/cs/advprog/eshop/model/ProductTest.java`

### `ProductRepositoryTest.java` (PDF hlm 80-81)
- Branch: `unit-test`
- Commit: `c6f8e8f` - `Add ProductRepository unit tests`
  - Menambahkan: `src/test/java/id/ac/ui/cs/advprog/eshop/repository/ProductRepositoryTest.java`

### Exercise 2: Unit test untuk Edit/Delete (PDF hlm 88)
- Branch: `unit-test`
- Commit: `f47f4c8` - `Add edit and delete unit tests`
  - Memodifikasi: `src/test/java/id/ac/ui/cs/advprog/eshop/repository/ProductRepositoryTest.java`

### Merge `unit-test` -> `main`
- Merge commit eksplisit di `main`: `021bd40` - `Merge pull request #4 from .../unit-test`
  - Bukti (2 parent): `021bd40 parents:002ded7 f47f4c8 Merge pull request #4 from A-Haekal-Alexander-Dinova-2406352424/unit-test`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/backup/pull/4

## Functional Test - Branch `functional-test` (PDF hlm 85-87 + Exercise 2 hlm 88)

Kebutuhan PDF: buat branch `functional-test`, buat `HomePageFunctionalTest`, commit; lalu buat `CreateProductFunctionalTest`, commit, push, merge.

### `HomePageFunctionalTest.java` (PDF hlm 85-87)
- Branch: `functional-test`
- Commit: `a15228b` - `Add home page functional test`
  - Menambahkan: `src/test/java/id/ac/ui/cs/advprog/eshop/functional/HomePageFunctionalTest.java`
  - Menambahkan: `src/main/resources/static/index.html` (untuk memenuhi syarat title = `ADV Shop` dan `<h3>Welcome</h3>`)

### Exercise 2: `CreateProductFunctionalTest.java` (PDF hlm 88)
- Branch: `functional-test`
- Commit: `d54880a` - `Add create product functional test`
  - Menambahkan: `src/test/java/id/ac/ui/cs/advprog/eshop/functional/CreateProductFunctionalTest.java`

### Merge `functional-test` -> `main`
- Merge commit eksplisit di `main`: `f117016` - `Merge pull request #5 from .../functional-test`
  - Bukti (2 parent): `f117016 parents:021bd40 d54880a Merge pull request #5 from A-Haekal-Alexander-Dinova-2406352424/functional-test`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/backup/pull/5

## Refleksi di `README.md` (PDF hlm 77 + hlm 89)

Kebutuhan PDF: tulis Refleksi 1 dan Refleksi 2 di `README.md` pada branch `main`.

- Commit: `c69dd81` - `Add tutorial reflections`
  - Memodifikasi: `README.md`

## Catatan / Deviasi (Transparansi)

- PDF mendefinisikan checkpoint yang wajib; repository ini punya beberapa commit tambahan yang masih relevan dan tidak mengubah requirement utama:
  - `78b769d` - `Initial commit` (sebelum scaffold project).
  - `4f3771b` - `Fix Gradle test task config` (commit perbaikan konfigurasi yang identik dengan `002ded7`, dibuat di branch `unit-test` dan ikut terbawa saat merge).
  - `e67235a` - `Fix delete redirect` (perbaikan redirect setelah aksi create/edit/delete supaya kembali ke `/product/list` dan tidak memicu error 405 saat delete).
    - Merge commit eksplisit di `main`: `1aa8bcd` - `Merge pull request #8 from .../fix-delete-redirect`
    - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/Module-1-Coding-Standard/pull/8
- Tidak ada rewrite history (tidak ada force-push, tidak ada squash merge). Semua branch fitur digabung ke `main` lewat PR dengan merge commit eksplisit (bukan fast-forward).
- Fix-delete-redirect (yang nantinya bernama bugfix) di merge ke delete product karena perbedaannya dengan delete-product dianggap tidak signifikan enough untuk menjadi branch yang baru

# Kepatuhan Riwayat Commit (Module 02 / CI/CD & DevOps)

Dokumen ini memetakan checkpoint commit yang diminta pada **Module 02 - CI/CD & DevOps** (bagian Tutorial & Exercise) ke commit dan merge commit yang ada di repository ini.

## Tutorial - Branch `ci-cd`

Kebutuhan PDF: buat branch `ci-cd`, tambahkan JaCoCo + konfigurasi `test`, lalu buat workflow CI dan OSSF Scorecard, commit, push, dan merge ke `main` via PR.

- Commit: `0e78ff7` - `Add JaCoCo and configure test task`
  - Menambahkan plugin `jacoco` dan memastikan task `test` tidak menjalankan functional test, serta selalu menghasilkan report JaCoCo.
- Commit: `6b54b85` - `Add CI workflow`
  - Menambahkan: `.github/workflows/ci.yml` (trigger `push` dan `pull_request`).
- Commit: `3dfa527` - `Add OSSF Scorecard workflow`
  - Menambahkan: `.github/workflows/scorecards.yml` (OSSF Scorecard).
- Merge `ci-cd` -> `main` (merge commit eksplisit): `1593dbe` - `Merge pull request #1 from .../ci-cd`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/Module-2-CI-CD-DevOpsFile/pull/1

### Follow-up Fix (Workflow)

- Commit: `d09cec8` - `Fix Scorecards workflow permissions`
  - Menambahkan permission `contents: read` dan `actions: read` agar workflow Scorecards bisa checkout dan upload hasil analisis.
- Merge `ci-cd` -> `main` (merge commit eksplisit): `fc0e730` - `Merge pull request #5 from .../ci-cd`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/Module-2-CI-CD-DevOpsFile/pull/5

- Commit: `aa797a2` - `Add Scorecards permissions for private repo`
  - Menambahkan permission read tambahan (`issues`, `pull-requests`, `checks`) agar Scorecards bisa berjalan di private repository.
- Merge `ci-cd` -> `main` (merge commit eksplisit): `2fdbb15` - `Merge pull request #8 from .../ci-cd`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/Module-2-CI-CD-DevOpsFile/pull/8

- Commit: `ee2f03e` - `Allow Scorecards SARIF upload to fail`
  - Menghindari workflow Scorecards gagal ketika fitur Code Scanning (Advanced Security) tidak tersedia pada private repository, dengan `continue-on-error` pada step upload SARIF.
- Merge `ci-cd` -> `main` (merge commit eksplisit): `c09f8c8` - `Merge pull request #11 from .../ci-cd`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/Module-2-CI-CD-DevOpsFile/pull/11

## Exercise - Branch `module-2-exercise`

Kebutuhan PDF: buat branch `module-2-exercise`, tingkatkan code coverage, tambahkan tool scanning/analysis (PMD), perbaiki minimal satu isu, lalu implement auto-deploy workflow, commit, push, dan merge ke `main` via PR.

- Commit: `db49543` - `Add unit tests for service and controller`
  - Menambahkan unit test untuk `ProductServiceImpl` dan `ProductController`.
  - Code coverage (JaCoCo `test`): **49% -> 96%**.
- Commit: `f5087dc` - `Add PMD workflow`
  - Menambahkan: `.github/workflows/pmd.yml` (trigger setiap `push` ke setiap branch).
  - PMD version diset ke `7.0.0-rc4` sesuai instruksi PDF untuk kompatibilitas Java 21.
- Commit: `158689b` - `Suppress PMD warning in EshopApplication`
  - Memperbaiki temuan PMD `UseUtilityClass` (false-positive) dengan suppression pada `EshopApplication`.
- Commit: `04b2235` - `Make Gradle wrapper executable`
  - Membuat `gradlew` executable supaya workflow Linux bisa menjalankan `./gradlew ...`.
- Commit: `09b645b` - `Add Render deployment workflow and Dockerfile`
  - Menambahkan: `.github/workflows/deploy.yml` (auto-deploy via Render deploy hook).
  - Menambahkan: `Dockerfile` serta konfigurasi `server.port=${PORT:8080}` untuk kebutuhan PaaS.
- Merge `module-2-exercise` -> `main` (merge commit eksplisit): `61115e8` - `Merge pull request #2 from .../module-2-exercise`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/Module-2-CI-CD-DevOpsFile/pull/2

### Follow-up Fix (Workflow)

- Commit: `9502932` - `Fix SARIF upload permissions`
  - Menambahkan permission `actions: read` agar langkah `upload-sarif` tidak gagal dengan error `Resource not accessible by integration`.
- Commit: `5b50f6f` - `Skip deploy when Render secret is missing`
  - Menghindari workflow deploy gagal ketika secret `RENDER_DEPLOY_HOOK` belum dikonfigurasi.
- Merge `module-2-exercise` -> `main` (merge commit eksplisit): `7c59da7` - `Merge pull request #6 from .../module-2-exercise`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/Module-2-CI-CD-DevOpsFile/pull/6

- Commit: `9956957` - `Skip SARIF upload when code scanning is unavailable`
  - Menghindari workflow PMD gagal ketika fitur Code Scanning (Advanced Security) tidak tersedia pada private repository.
- Merge `module-2-exercise` -> `main` (merge commit eksplisit): `04617b5` - `Merge pull request #10 from .../module-2-exercise`
  - PR: https://github.com/A-Haekal-Alexander-Dinova-2406352424/Module-2-CI-CD-DevOpsFile/pull/10
