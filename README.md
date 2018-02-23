# stemmer-bahasa
java stemmer engine untuk pemenggalan prefix, kata dasar, dan suffix berdasarkan 2 kbbi serta algoritma nazief dan adriani.
Technology stack (Java, Spring, Hibernate, JSoup, dan MySql)

# Modul - Modul
1. stemmer-rest-ws, modul sample untuk menggunakan library stemmer. modul berbasis spring restful api.
2. stemmer-engine, modul core library stemmer menggunakan kbbi dan algoritma nazief dan adriani.

# Setting Database
1. Di dalam module stemmer-engine (src/main/resources/sql) terdapat sql code yang harus diimport ke mysql (saat ini hanya support mysql).

# Menjalankan aplikasi sample
1. Untuk menjalankan aplikasi sample anda harus menggunakan build tools apache maven.
2. Melalui command prompt / terminal masuk ke directory modul stemmer-rest-ws dan lakukan command mvn clean install dan mvn jetty:run jika menggunakan jetty container.
3. Jika ingin di deploy di tomcat maka sama seperti step nomor 2 namun dengan command berbeda yaitu mvn clean install saja. maka akan ada file war di directory target.

# Informasi lebih lanjut
email ke mimsapp@gmail.com