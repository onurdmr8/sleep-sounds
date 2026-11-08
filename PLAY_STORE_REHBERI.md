# 🚀 Google Play Store Adım Adım Yükleme Rehberi

Bu rehber, **Uyku Sesleri & Beyaz Gürültü (`com.sleep.sounds`)** uygulamasını Google Play Console'a eksiksiz ve hatasız şekilde yüklemeniz için gereken tüm dosyaları, metinleri ve form cevaplarını adım adım içerir.

---

## 📦 1. Hazır Dosyaları İndirin

Bilgisayarınıza aşağıdaki 6 hazır dosyayı indirin (GitHub Release `v1.0.0` veya `play-store-assets/` klasöründen):

1. **İmzalı Uygulama Paketi (`.aab`):**
   - 👉 [app-release.aab İndir](https://github.com/onurdmr8/sleep-sounds/releases/download/v1.0.0/app-release.aab)
2. **Uygulama İkonu (512x512 PNG):**
   - 👉 [icon-512.png İndir](https://github.com/onurdmr8/sleep-sounds/raw/main/play-store-assets/icon-512.png)
3. **Özellik Grafiği / Feature Graphic (1024x500 PNG):**
   - 👉 [feature-graphic-1024x500.png İndir](https://github.com/onurdmr8/sleep-sounds/raw/main/play-store-assets/feature-graphic-1024x500.png)
4. **Telefon Ekran Görüntüleri (1080x1920 PNG - 3 Adet):**
   - 👉 [screenshot-1.png İndir](https://github.com/onurdmr8/sleep-sounds/raw/main/play-store-assets/screenshot-1.png)
   - 👉 [screenshot-2.png İndir](https://github.com/onurdmr8/sleep-sounds/raw/main/play-store-assets/screenshot-2.png)
   - 👉 [screenshot-3.png İndir](https://github.com/onurdmr8/sleep-sounds/raw/main/play-store-assets/screenshot-3.png)

---

## 🛠️ 2. Adım: Google Play Console'da Yeni Uygulama Oluşturma

1. [Google Play Console](https://play.google.com/console) sayfasına giriş yapın ve sağ üstten **Uygulama oluştur (Create app)** butonuna tıklayın.
2. Alanları şu şekilde doldurun:
   - **Uygulama adı (App name):** `Uyku Sesleri & Beyaz Gürültü`
   - **Varsayılan dil (Default language):** `Türkçe (tr-TR)`
   - **Uygulama veya oyun (App or game):** `Uygulama (App)`
   - **Ücretsiz veya ücretli (Free or paid):** `Ücretsiz (Free)`
3. **Beyanlar (Declarations)** kutucuklarını işaretleyip **Uygulama oluştur**'a tıklayın.

---

## 🛡️ 3. Adım: Uygulama İçeriği (App Content / Politikalar)

Sol menüde en altta yer alan **Politika ve programlar > Uygulama içeriği (App content)** sekmesine gidin ve sırasıyla şu beyanları tamamlayın:

### 1) Gizlilik Politikası (Privacy Policy)
- **Gizlilik Politikası URL'si:**
  ```text
  https://github.com/onurdmr8/sleep-sounds/blob/main/PRIVACY_POLICY.md
  ```
- Kaydet'e tıklayın.

### 2) Reklamlar (Ads)
- **"Evet, uygulamam reklam içeriyor" (Yes, my app contains ads)** seçeneğini işaretleyin ve kaydedin.

### 3) Uygulama Erişimi (App Access)
- **"Tüm işlevler özel bir erişim olmadan kullanılabilir" (All functionality is available without special access)** seçeneğini işaretleyin ve kaydedin.

### 4) İçerik Derecelendirmesi (Content Rating / IARC Anketi)
- **E-posta adresi:** `onurdmr0228@gmail.com`
- **Kategori:** `Yardımcı Program, Üretkenlik, İletişim veya Diğer (Utility, Productivity, Communication, or Other)`
- **Anket Soruları:** Şiddet, cinsellik, küfür, kumar, kullanıcılar arası etkileşim, konum paylaşımı vb. tüm sorulara **HAYIR (No)** cevabını verin ve kaydedin.

### 5) Hedef Kitle ve İçerik (Target Audience and Content)
- **Hedef yaş grubu:** `18 yaş ve üzeri` (veya `13-15`, `16-17` ve `18+` işaretleyebilirsiniz; **13 yaş altını işaretlemeyin** ki Aile Politikası kısıtlamalarına takılmasın).
- **"Mağaza girişim kasıtsız olarak çocukların ilgisini çekebilir mi?"** -> **Hayır (No)**.

### 6) Haber Uygulamaları / COVID-19 / Devlet Uygulamaları / Finansal Özellikler / Sağlık Uygulamaları
- **Haber uygulaması mı?** -> **Hayır (No)**
- **COVID-19 temas takibi veya durum uygulaması mı?** -> **Hayır (No)**
- **Devlet uygulaması mı?** -> **Hayır (No)**
- **Finansal özellikler:** **"Uygulamam herhangi bir finansal özellik sağlamıyor"** seçeneğini işaretleyin.
- **Sağlık uygulamaları:** **"Uygulamam herhangi bir sağlık özelliği sunmuyor"** (veya sadece rahatlama/ambient ses olduğu için tıbbi cihaz olmadığını) seçin.

### 7) Reklam Kimliği (Advertising ID) — ⚠️ ÇOK ÖNEMLİ (AdMob İçin)
- **Uygulamanız Reklam Kimliği kullanıyor mu?** -> **Evet (Yes)**
- **Neden kullanılıyor?** -> **`Reklam veya pazarlama (Advertising or marketing)`** ve **`Analiz (Analytics)`** kutucuklarını işaretleyin.

### 8) Veri Güvenliği (Data Safety) — ⚠️ ÇOK ÖNEMLİ (AdMob İçin)
Google AdMob SDK'sı bulunduğu için Veri Güvenliği formunu şu şekilde doldurun:
1. **Uygulamanız gerekli kullanıcı verisi türlerinden herhangi birini topluyor veya paylaşıyor mu?** -> **Evet (Yes)**
2. **Toplanan tüm kullanıcı verileri aktarım sırasında şifreleniyor mu (HTTPS)?** -> **Evet (Yes)**
3. **Kullanıcıların verilerinin silinmesini talep etmeleri için bir yol sağlıyor musunuz?** -> **"Hayır, ancak toplanan veriler otomatik olarak silinir / hesap oluşturulmaz"** (veya seçeneklerdeki "Uygulama kullanıcı hesabı oluşturmaya izin vermiyor" seçeneğini işaretleyin).
4. **Veri Türleri (Data Types) seçim ekranında sadece şunları işaretleyin:**
   - **Konum (Location)** -> `Yaklaşık konum (Approximate location)` (AdMob IP üzerinden yaklaşık ülke/şehir bilir)
   - **Uygulama etkinlikleri (App activity)** -> `Uygulama etkileşimleri (App interactions)` (Reklam tıklamaları/gösterimleri)
   - **Uygulama bilgileri ve performansı (App info and performance)** -> `Kilitlenme günlükleri (Crash logs)` ve `Teşhis (Diagnostics)`
   - **Cihaz veya diğer kimlikler (Device or other IDs)** -> `Cihaz veya diğer kimlikler (Device or other IDs)` (Reklam Kimliği / Advertising ID)
5. Seçtiğiniz bu 4 veri türünün her biri için detay sorulduğunda:
   - **Toplanıyor mu / Paylaşılıyor mu?** -> `Toplanıyor (Collected)` ve `Paylaşılıyor (Shared)` ikisini de işaretleyin.
   - **Veriler geçici olarak mı işleniyor (ephemeral)?** -> `Hayır (No)`
   - **Veri toplama zorunlu mu, isteğe bağlı mı?** -> `Veri toplama zorunludur (Data collection is required)`
   - **Neden toplanıyor / paylaşılıyor?** -> `Reklam veya pazarlama (Advertising or marketing)`, `Analiz (Analytics)` ve `Dolandırıcılığı önleme, güvenlik ve uygunluk (Fraud prevention, security, and compliance)` kutucuklarını işaretleyin.

---

## 🎨 4. Adım: Mağaza Ayarları ve Ana Mağaza Girişi (Store Listing)

### 1) Mağaza Ayarları (Büyüme > Mağazadaki varlığınız > Mağaza ayarları)
- **Uygulama Kategorisi:** `Sağlık ve Fitness (Health & Fitness)` veya `Müzik ve Ses (Music & Audio)`
- **Etiketler (Tags):** Uyku, Rahatlama, Müzik ve Ses
- **İletişim E-postası:** `onurdmr0228@gmail.com`

### 2) Ana Mağaza Girişi Metinleri (Kopyala & Yapıştır)

#### 🇹🇷 Türkçe (Varsayılan)
- **Uygulama Adı (En fazla 30 karakter):**
  ```text
  Uyku Sesleri & Beyaz Gürültü
  ```
- **Kısa Açıklama (En fazla 80 karakter):**
  ```text
  Derin uyku, bebek uykusu ve odaklanma için yağmur, okyanus ve beyaz gürültü.
  ```
- **Tam Açıklama (En fazla 4000 karakter):**
  ```text
  Gece uykuya dalmakta zorlanıyor musunuz, ders çalışırken dikkatiniz mi dağılıyor ya da bebeğinizin huzurla uyumasına yardımcı olacak sakinleştirici sesler mi arıyorsunuz? Uyku Sesleri & Beyaz Gürültü, kendi huzur atmosferinizi oluşturmanız için tasarlanmış minimalist ve modern bir ortam sesleri mikseridir.

  ✨ ÖNE ÇIKAN ÖZELLİKLER

  • 10 Farklı Doğal ve Bilimsel Ortam Sesi:
    - Hafif Yağmur Sesi
    - Okyanus & Deniz Dalgaları
    - Çıtırdayan Şömine Ateşi
    - Gece Rüzgarı Esintisi
    - Gece Cırcır Böcekleri
    - Nehir & Su Deresi Şırıltısı
    - Anne Karnı & Kalp Atışı (Bebek Uykusu İçin)
    - Kahverengi Gürültü (Brown Noise - Derin Uğultu)
    - Pembe Gürültü (Pink Noise - Derin Uyku Frekansı)
    - Beyaz Gürültü (White Noise - Odaklanma ve Maskeleme)

  • Kişiselleştirilebilir Ses Mikseri:
  Birden fazla sesi aynı anda açabilir ve her bir sesin yüksekliğini ayrı ayrı ayarlayabilirsiniz. Örneğin yağmur sesine hafif bir şömine çıtırtısı ve rüzgar ekleyerek kendinize özel gece atmosferinizi yaratın.

  • Akıllı Uyku Zamanlayıcısı (Fade-Out):
  15, 30, 45, 60, 90 veya 120 dakikalık zamanlayıcı kurun. Süre dolarken son 15 saniyede sesler aniden kesilmek yerine yavaşça kısılarak (fade-out) uykunuzu bölmeden kapanır.

  • %100 Çevrimdışı & Gerçek Zamanlı DSP Ses Sentezi:
  Büyük boyutlu ses dosyaları indirmenize gerek kalmadan cihaz üzerinde gerçek zamanlı üretilen kesintisiz (loop izi olmayan) ses teknolojisiyle internet bağlantısı olmadan her yerde çalışır.

  • Göz Yormayan AMOLED Gece Arayüzü:
  Gece karanlığında gözlerinizi yormayan koyu lacivert ve saf siyah tema ile tasarlanmıştır. Ekran kapalıyken de arka planda kesintisiz çalmaya devam eder.
  ```

#### 🇬🇧 İngilizce Çeviri Eklemek İsterseniz (`English - en-US`)
- **App Name:**
  ```text
  Sleep Sounds & White Noise
  ```
- **Short Description:**
  ```text
  Relaxing rain, ocean waves, campfire & white noise mixer with sleep timer.
  ```
- **Full Description:**
  ```text
  Trouble falling asleep, staying focused while studying, or soothing a restless baby? Sleep Sounds & White Noise is a modern, minimalist ambient sound mixer powered by a real-time DSP audio engine.

  ✨ KEY FEATURES

  • 10 Soothing Ambient & Scientific Sounds:
    - Gentle Rain
    - Ocean Waves
    - Crackling Fireplace
    - Soft Night Wind
    - Night Crickets
    - Water Stream & River
    - Womb & Heartbeat (Baby Sleep)
    - Brown Noise (Deep Fan / Cabin Hum)
    - Pink Noise (Deep Sleep Frequency)
    - White Noise (Focus & Sound Masking)

  • Custom Multi-Sound Mixer:
  Play multiple sounds simultaneously and adjust each track's volume independently to craft your perfect sleep or study environment.

  • Smart Sleep Timer with Smooth Fade-Out:
  Choose from 15, 30, 45, 60, 90, or 120-minute presets. During the final 15 seconds, audio gently fades out so your sleep is never disturbed.

  • Offline & Seamless Real-Time Audio:
  No giant audio downloads or jarring loop seams—sounds are synthesized cleanly on your device and continue playing smoothly while your screen is off.

  • Eye-Friendly AMOLED Dark Theme:
  Crafted with deep midnight tones that won't strain your eyes in bed.
  ```

### 3) Grafikleri Yükleyin
- **Uygulama İkonu (512x512):** `icon-512.png` dosyasını yükleyin.
- **Özellik Grafiği (1024x500):** `feature-graphic-1024x500.png` dosyasını yükleyin.
- **Telefon Ekran Görüntüleri:** `screenshot-1.png`, `screenshot-2.png`, `screenshot-3.png` dosyalarını yükleyin (aynı 3 görseli **7 inç ve 10 inç Tablet** bölümlerine de yükleyebilirsiniz).

---

## 📤 5. Adım: `.aab` Paketini Yükleme ve Yayına Gönderme

1. Sol menüden **Test et ve yayınla > Üretim (Production)** (veya yeni kişisel geliştirici hesabıysa önce **Kapalı Test / Closed Testing**) sekmesine tıklayın.
2. **Ülkeler / Bölgeler (Countries / regions)** sekmesine geçip **Ülke/bölge ekle** diyerek tüm ülkeleri seçin ve kaydedin.
3. Sağ üstten **Yeni sürüm oluştur (Create new release)** butonuna tıklayın.
4. **Uygulama paketleri (App bundles)** alanına indirdiğiniz **`app-release.aab`** dosyasını sürükleyip bırakın.
5. **Sürüm adı (Release name):** `1.0 (1)` otomatik gelecektir.
6. **Sürüm notları (Release notes):**
   ```text
   <tr-TR>
   İlk sürüm! 10 farklı rahatlatıcı uyku sesi, kişiselleştirilebilir ses mikseri, yumuşak kapanışlı (fade-out) akıllı uyku zamanlayıcısı ve AMOLED gece modu eklendi.
   </tr-TR>
   <en-US>
   Initial release! Added 10 soothing ambient sounds, customizable multi-track mixer, smart sleep timer with smooth fade-out, and AMOLED dark theme.
   </en-US>
   ```
7. **İleri (Next)** -> **Kaydet (Save)** -> **Yayınlama genel bakışına git (Go to publishing overview)** adımlarını takip edip **İncelemeye gönder (Send for review)** butonuna basın!
