package com.example.capstoneproject.model

object DataSourceArticles {
    val dataArticles = listOf(
        Articles(
            1,
            "https://wow.net.id/ternyata-kecepatan-bandwidth-bukan-satu-satunya-faktor",
            "https://wow.net.id/wp-content/uploads/2020/12/kecepatan-e1608614242390.jpg",
            "Ternyata Kecepatan Bandwidth bukan Satu-satunya Faktor..!",
            "22 Oktober 2020",
            "Yes Benar! Anda pernah melihat brosur/selebaran dari ISP yang menjual Bandwdith besar dengan harga murah..? Ternyata untuk penggunaan internet yang stabil besaran Bandwidth bukanlah faktor penentu.\n" +
                    "\n" +
                    "Pernah dengar istilah Ping, Latency, dan Jitter..?\n" +
                    "\n" +
                    "Jika belum atau sudah tapi belum terlalu paham, baiklah mari kita bahas satu persatu yaa..\n" +
                    "\n" +
                    "PING\n" +
                    "\n" +
                    "Ping sebenarnya meniru dari cara kerja sonar untuk mengirim signal dan menerima signal dengan tujuan untuk mengetahui benda-benda di sekitar (Sonar sendiri meniru cara kerja hewan seperti paus dan lumba-lumba). Ping sendiri pertama kali digunakan di tahun 1983 oleh Mike Muuss dengan ECHO_REQUEST dan ECHO_REPLY yang ditujukan untuk apakah target mesin merespon dan berapa lama mesin tersebut merespon.\n" +
                    "\n" +
                    "PING adalah singkatan dari Packet Internet Gopher adalah tools yang akhirnya digunakan di multiplatform Operating System yang salah satunya adalah Windows dengan cara kerja mengirim pesan Internet Control Message Protocol (ICMP) dalam IP Address.\n" +
                    "\n" +
                    "Ping berguna untuk mengetahui apakah PC Anda terhubung dengan internet..? (Ketika di Ping dan mendapat respon itu artinya PC Anda terhubung dengan internet) namun jika tidak mendapat balasan maka bisa jadi koneksi internet mati, kabel LAN, wifi atau Router yang bermasalah.\n" +
                    "\n" +
                    "Definisi sederhana dari Ping adalah tools/alat ukur untuk mengetahui waktu yang dibutuhkan dari komputer Anda kepada Komputer tujuan dan kembali lagi.\n" +
                    "\n" +
                    "JITTER\n" +
                    "\n" +
                    "Sedangkan Jitter menunjukkan variasi delay (Waktu yang di butuhkan untuk berangkat dan kembali) Ping yang juga di tunjukkan dalam bentuk satuan milisecond. Semakin kecil Jitter maka semakin stabil koneksi internet. Jitter hampir tidak terasa ketika browsing internet biasa, namun jika sedang live-streaming, game online, akses server dan lainnya, maka Jitter akan sangat berpengaruh.\n" +
                    "\n" +
                    "LATENCY\n" +
                    "\n" +
                    "Secara sederhana adalah Delay Time (waktu tunda) yang di butuhkan dari sumber ke tujuan dalam satuan milisecond. Bagi gamers istilah ini bisa disebut Lag, jika semakin besar milisecond Latency maka semakin nge-lag game tersebut.\n" +
                    "\n" +
                    "Ping-Latency-Jitter\n" +
                    "\n" +
                    "Jika Ping adalah tools-nya maka Latency adalah istilah penyebutnya sementara Jitter adalah variasinya ; jadi jika digunakan dalam satu kalimat  “Coba di Ping, berapa Latency dan Jitter nya?” Artinya Ping adalah tools-nya, Latency adalah satuannya, Jitter adalah variasi naik turun perubahan latency.\n" +
                    "\n" +
                    "Untuk menggunakan Ping (dalam OS Windows) jalankan program Command Prompt atau search di “kaca pembesar” dan ketik cmd. Kemudian ketik “ping google.com” (tanpa tanda kutip) atau “ping google.com –t” untuk mengetahui berapa milisecond delay dari PC Anda ke situs google.com.\n" +
                    "\n" +
                    "Jika terdapat RTO (Request Time Out) artinya ini adalah packet loss atau data yang tidak ke-sent atau tidak ke-receive.\n" +
                    "\n" +
                    "Akses data internet terbagi dua jenis yaitu waiting to complete dan sent continuously. Untuk data yang menunggu hingga komplit biasanya dilakukan oleh data yang sudah tersimpan di server, misal portal berita, Instagram, Facebook, dan Video-video non live di Youtube. Sedangkan data yang dikirim berkelanjutan adalah ketika livestreaming di Youtube atau Twitch.tv dan juga semua Game Online.\n" +
                    "\n" +
                    "Jika RTO semakin banyak/besar akan berpengaruh kepada aplikasi kita. Jika aplikasi yang kita gunakan adalah tersimpan dalam suatu server maka akan terjadi buffer dalam pemutaran bideo tersebut. Namun jika aplikasi itu adalah live, maka akan ada “lompatan” sekian detik tergantung RTO-nya berapa besar. Jadi jika main game online posisi karakter game tiba-tiba ada di depan seolah lompat atau blank-time ini yang di sebut lagging.\n" +
                    "\n" +
                    "Jadi, besaran bandwidth hanya mengacu kepada berapa besar kapasitas Download/Upload bukan seberapa cepat atau stabil koneksi internet kita. Untuk mengetahui parameter ini semua, Anda bisa membuka aplikasi online semacam speedtest.net dan perhatikan bukan hanya besaran bandwidth tapi Ping dan Jitter berapa milisecond latency-nya."
        ),
        Articles(
            2,
            "https://wow.net.id/bagaimana-cara-memilih-isp",
            "https://wow.net.id/wp-content/uploads/2020/11/isp.jpg",
            "Tip dan Trik Cara Memilih ISP",
            "06 November 2020",
            "Memilih ISP adalah hal yang penting dalam kehidupan sekarang, dikarenakan kebanyakan ter-digitalisasi maka dibutuhkan koneksi yang handal untuk memperoleh koneksi internet yang stabil.\n" +
                    "\n" +
                    "Di Indonesia sendiri ada banyak ISP yang tumbuh, ada yang berbasis fiber optic, wireless, vsat dan lain-lain. Banyaknya ISP yang ada di Indonesia mengakibatkan calon customer terkadang mempunyai kendala tersendiri untuk memilih ISP mana yang cocok untuk keperluan.\n" +
                    "\n" +
                    "Berikut adalah tips dalam memilih ISP\n" +
                    "\n" +
                    "Ketahui kebutuhan Anda\n" +
                    "\n" +
                    "Sebelum memilih ISP, Anda harus tahu kebutuhan bandwidth yang akan Anda perlukan, berapa mega idealnya? kemudian apakah Anda sering mengakses social media? atau Anda membutuhkan koneksi yang bagus untuk streaming? atau Anda membutuhkan koneksi yang bagus ke Eropa/Amerika. Semua kebutuhan tersebut harus Anda pahami dan tanyakan ke sales representatif ISP Anda, jangan malu untuk meminta hasil speedtest, traceroute ke beberapa situs.\n" +
                    "\n" +
                    "Jangkauan wilayah\n" +
                    "\n" +
                    "Dalam memilih ISP, ada baiknya Anda tanyakan dulu apakah wilayah Anda sudah tercover atau belum, ini penting dikarenakan apabila tidak tercover ISP tidak bisa men-deliver bandwidth di tempat Anda.\n" +
                    "\n" +
                    "Media yang digunakan\n" +
                    "\n" +
                    "Silakan berkonsultasi dengan sales, apakah di area Anda dapat tercover wireless atau fiber? Apabila memungkinkan silakan pilih fiber walaupun terkadang lebih mahal, namun fiber memberikan kestabilan dan latency yang lebih bagus daripada wireless.\n" +
                    "\n" +
                    "Apabila memungkinkan, kombinasi dari kedua media tersebut sangatlah bagus, misalkan fiber optic sebagai koneksi utama dan wireless sebagai koneksi backup. Kenapa begitu? — Kedua media tersebut mempunyai keunggulan dan kekurangan yang saling melengkapi, contohnya adalah pada saat gangguan, fiber optic membutuhkan maintenance yang lebih lama dikarenakan proses maintenance yang melibatkan banyak pihak, misal ijin gali, pasang kabel dan lain-lain.\n" +
                    "\n" +
                    "Sedangkan wireless, mempunyai keunggulan dalam kecepatan maintenance dan instalasi, tidak dibutuhkan waktu yang lama apabila ada gangguan.\n" +
                    "\n" +
                    "Profil perusahaan\n" +
                    "\n" +
                    "Sebelum memutuskan menggunakan layanan ISP ada baiknya jika Anda melihat profil perusahaan yang akan dipilih. Silakan cek bagaimana kondisi website-nya, social media-nya, apakah semuanya terurus? bagaimanakah kecepatan respon di social media dan seterusnya. Ini penting, karena dalam hal sekecil itu saja sudah diperhatikan, apalagi Anda yang akan menjadi customer, maka otomatis akan mendapatkan perhatian yang lebih bukan?\n" +
                    "\n" +
                    "Support/after sales\n" +
                    "\n" +
                    "Jangan tertipu oleh murahnya layanan, terkadang murahnya layanan tidak diimbangi dengan support yang responsif. Kita sering mendengar ada customer pada perusahaan X sering gangguan dan sudah berkali-kali komplain tapi tidak ada tindak lanjutnya.\n" +
                    "\n" +
                    "Support sangat penting ketika Anda mulai berselancar internet, problem pasti ada dan jika Anda tidak dapat memecahkan problem tersebut, sudah selayaknya Anda berkonsultasi masalah internet tersebut ke ISP Anda. ISP yang baik biasanya mempunyai informasi yang lengkap bagaimana menghubungi divisi customer care, selalu online atau 24×7, menyediakan beberapa jenis cara untuk berkomunikasi\n" +
                    "\n" +
                    "Cek koneksi\n" +
                    "\n" +
                    "Internet sejatinya adalah gabungan dari sub-internet yang tersebar di beberapa kota, provisi, negara di seluruh dunia, bisa diibaratkan internet adalah jaringan laba-laba. Untuk terhubung ke internet, ISP akan membeli koneksi ke NAP (ISP yang lebih tinggi), atau ISP akan terhubung dengan ISP yang lain melalui exchange yaitu tempat dimana ISP saling terhubung di suatu daerah.\n" +
                    "\n" +
                    "Exchange di Indonesia yang paling terkenal adalah OpenIXP, IIX, Neucentrix, CDC, MCSIX dan lain-lain\n" +
                    "\n" +
                    "Silakan tanyakan ke sales Anda upstream mana saja yang dia punya. Tips, lebih baik apabila ISP mempunyai 2 upstream dan lebih banyak lebih bagus. Jangan lupa untuk menanyakan terhubung ke exchange mana saja, sama seperti upstream, lebih banyak lebih bagus.\n" +
                    "\n" +
                    "Demikian adalah tips dalam memilih ISP, semoga bermanfaat dan jangan lupa cek Layanan Wownet ya!"
        ),
        Articles(
            3,
            "https://wow.net.id/cara-pasang-wifi-rumah-agar-maksimal",
            "https://wow.net.id/wp-content/uploads/2020/06/1-2.jpg",
            "Cara Pasang WiFi di Rumah Agar Maksimal",
            "16 Juni 2020",
            "Saat ini perangkat WIFI di rumah sudah sangat umum di mana-mana, peletakan posisi perangkat WIFI yang tepat akan menunjang sinyal yang bagus dan kecepatan akses internet yang maksimal pula.\n" +
                    "\n" +
                    "Bagaimana peletakan posisi perangkat WIFI yang tepat supaya didapat sinyal yang maksimal ? Berikut beberapa tips penempatan di rumah supaya jaringan internet bisa kencang :\n" +
                    "\n" +
                    "Jangan letakkan di sudut rumah, tapi di posisi titik pusat rumah dan dipasang setinggi mungkin supaya jangkauan penyebaran lebih luas.\n" +
                    "Pasang WIFI extender jika ada penghalang dinding atau sekat atau jangkauan terlalu luas.\n" +
                    "Hindari penempatan dekat dengan perangkat elektronik yang memancarkan frekuensi yang sama.\n" +
                    "Atur posisi antena dengan benar, jika ada beberapa antena, atur secara vertikal dan horisontal agar jangkauan dapat menutupi beberapa sudut rumah.\n" +
                    "Happy surfing !!!"
        ),
        Articles(
            4,
            "https://wow.net.id/tutorial-membuat-efek-green-screen",
            "https://wow.net.id/wp-content/uploads/2020/06/4-2.jpg",
            "Tutorial Membuat Efek Green Screen di Adobe Premiere Pro",
            "17 Juni 2020",
            "1. Buat Video Berlatar Belakang Hijau\n" +
                    "Langkah pertama cara membuat green screen di Adobe Premiere Pro adalah impor dulu video yang akan diedit. Siapkan dua video yang akan diimpor, yaitu video dengan background hijau dan video dengan background lain sebagai penggantinya.\n" +
                    "Ketika mengimpor video, posisikan video dengan background green screen di atas video lain yang akan digunakan sebagai background. Karena itu, sebelum mulai mengedit, ada baiknya Kamu memilih video lain yang akan dijadikan backgorund nantinya.\n" +
                    "\n" +
                    "2. Tampilkan Pilihan Menu Effect\n" +
                    "Jika sudah, langkah selanjutnya adalah pilih menu ‘Effect’. Caranya, pilih menu ‘Window’ kemudian pilih submenu ‘Effect’. Setelah itu akan pilih menu ‘Video Effect’. Pada menu ini akan terdapat banyak pilihan efek yang bisa Kamu gunakan.\n" +
                    "\n" +
                    "3. Pilih Efek Chroma Key\n" +
                    "Dari daftar efek yang ditampilkan, temukan efek ‘Chroma Key’. Setelah menemukan efek tersebut, tarik atau drag ke worksheet dimana video berlatar hijau berada.\n" +
                    "\n" +
                    "4. Pilih ‘Effect Controls’\n" +
                    "Jika sudah, langkah selanjutnya dari tutorial membuat efek green screen pada Adobe Premiere Pro adalah pilih menu ‘Effect Controls’ yang terletak bersebelahan dengan layar tampilan (tab sequence).\n" +
                    "Pada menu ‘Effect Controls’ ini drop down dengan mengklik tanda panah ke bawah di menu ‘Chroma Key’. Setelah itu akan muncul tampilan berupa pilihan warna yang bisa Kamu pilih.\n" +
                    "\n" +
                    "5. Pilih Menu ‘Color’ lalu Pilih Warna ‘Hijau’\n" +
                    "Selanjutnya, Kamu akan melihat beberapa pilihan efek yang bisa digunakan. Pada menu ini, pilih opsi ‘Color’. Maka disana Kamu bisa melihat pipet serta kolom warna yang berwarna putih. Pilih warnanya menjadi hijau.\n" +
                    "\n" +
                    "6. Atur Efek Lainnya\n" +
                    "Langkah terakhir mengatur efek lainnya yang ada di dalam pilihan menu efek tersebut. Jika ingin mengubah background warna hijau menjadi background lain, tinggal atur pada kolom efek ini.\n" +
                    "Efek yang perlu diubah adalah pada opsi ‘Similarity’. Maka nantinya background hijau di video Kamu akan berubah menjadi background sesuai yang dipilih sebelumnya."
        ),
        Articles(
            5,
            "https://wow.net.id/mengenal-bgp-ibgp-ebgp",
            "https://wow.net.id/wp-content/uploads/2020/06/5.jpg",
            "Mengenal BGP: iBGP dan eBGP",
            "18 Juni 2020",
            "Mari mengenal BGP, sebuah elemen penting di Internet.\n" +
                    "\n" +
                    "BGP adalah protokol yang bertugas menghubungkan satu network ke network yang lain. BGP dapat diibaratkan sebagai bahasa yang digunakan oleh router milik ISP untuk ngobrol satu sama lain, dalam percakapannya router ISP ini berbagi informasi IP yang lewat darinya, sehingga komunikasi antarnetwork dapat berjalan dua arah.\n" +
                    "\n" +
                    "Umumnya setiap ISP mempunyai nomer BGP yang unik tidak ada yang sama; kita sebut saja nomer BGP ini sebuah identitas, sebut saja nama. Tentu sangat logis bukan apabila ketika Anda bertanya dengan teman Anda ke manakah jalan menuju Jakarta dari Surabaya, maka teman Anda apabila tidak tahu akan mengatakan — hei, tanyalah ke X karena dia lebih tahu, dan X akan memberikan informasi kalau Y lebih tahu, dan akhirnya Anda sampai ke Z yang dapat mengantarkan Anda ke Jakarta. Seperti itulah jaringan bekerja.\n" +
                    "\n" +
                    "BGP terbagi menjadi dua yaitu iBGP dan eBGP. Tebakan anda benar bila “e” merujuk pada external dan “i” merujuk pada internal. Dalam aplikasinya router ISP ini dapat ngobrol dengan satu network (internal) dan dengan network yang berbeda (external). Seperti pada contoh diatas, jika diibaratkan internal adalah penggunaan bahasa yang sama, maka router internal anda hanya dapat berbicara satu sama lain dalam satu bahasa. Dan akan ada satu router Anda yang akan berbicara multilanguage, ya benar dia adalah router BGP yang dapat belakukan iBGP dan eBGP. Di dunia internet, router semacam itu disebut dengan “border“, sedangkan router berbicara satu sama lain dengan bahasanya sendiri adalah router distribusi.\n" +
                    "\n" +
                    "Nah sudah paham kan……??"
        ),
    )
}