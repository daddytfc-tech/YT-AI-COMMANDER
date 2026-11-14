# YT AI COMMANDER — Android starter project

यह ZIP एक **Android Studio starter project** है, तैयार/इंस्टॉल होने वाला APK नहीं। इसमें शुरुआती dashboard और settings UI है। असली YouTube/Gmail/AI काम चालू करने के लिए नीचे दिए गए integrations पूरे करने होंगे।

## अभी क्या शामिल है
- Kotlin + Jetpack Compose Android app
- Dashboard placeholder cards
- Google connection placeholder
- Unlisted upload detection interval setting
- AI comment reply और Gmail report के demo toggles

## असली फीचर्स चालू करने के लिए जरूरी सेटअप

### 1) Google Cloud
1. Google Cloud Console में नया project बनाएं।
2. YouTube Data API v3, YouTube Analytics API और Gmail API enable करें।
3. OAuth consent screen configure करें।
4. Android OAuth client बनाएं; package name `com.ytaicommander` और अपने signing certificate का SHA-1 दें।
5. YouTube scopes: `youtube.readonly`, `youtube.force-ssl`, `yt-analytics.readonly` (जरूरत के अनुसार least privilege रखें)।
6. Gmail sending के लिए `gmail.send` scope जोड़ें। Gmail API का इस्तेमाल तभी होगा जब user अनुमति दे।

### 2) Unlisted वीडियो पहचानना
YouTube ऐप से अपलोड किए गए वीडियो को पहचानने का तरीका:
- OAuth से `channels.list(mine=true, part=contentDetails)` call करें।
- `contentDetails.relatedPlaylists.uploads` से Uploads playlist ID लें।
- `playlistItems.list` से नवीनतम uploads पढ़ें।
- `videos.list(part=snippet,status,statistics,contentDetails)` से details लें।
- नए video IDs को स्थानीय database/backend में save करें ताकि duplicate processing न हो।
यह background polling होगा; तुरंत पहचान या Android में लगातार background execution की गारंटी नहीं है।

### 3) AI SEO और auto-replies
- AI model API key **APK में hard-code न करें**। एक सुरक्षित backend रखें।
- Backend को केवल जरूरी video title/description/comment text भेजें।
- AI reply को language, context, repetition और spam checks से validate करें।
- Duplicate replies, abusive content, personal-data requests और sensitive topics पर auto-reply रोकें।
- शुरुआत में approval mode रखें; बाद में user चाहे तो सीमित auto-reply enable करे।
- YouTube replies `comments.insert` से होते हैं और OAuth permission चाहिए।

### 4) Gmail reports
- Gmail API `users.messages.send` इस्तेमाल करें।
- `gmail.send` scope पर user consent लें।
- Daily report में views, likes, comments, subscribers (जहाँ उपलब्ध हों), top video और growth comparison शामिल करें।

### 5) Viral score
कोई API “viral” की गारंटी नहीं देती। App हाल के 1h/24h/7d view growth, likes और comments के आधार पर trend label दे सकता है। Views के लिए YouTube Analytics API का उपयोग करें; कुछ metrics उपलब्ध होने में देरी हो सकती है।

## Build APK
Android Studio में इस folder को खोलें, Gradle sync करें, फिर:
**Build → Build Bundle(s) / APK(s) → Build APK(s)**

या Android SDK/Gradle सेटअप वाले कंप्यूटर पर:
```bash
./gradlew assembleDebug
```
APK path:
`app/build/outputs/apk/debug/app-debug.apk`

## सुरक्षा
- Google password कभी app में न लें।
- OAuth tokens को सुरक्षित storage में रखें।
- API keys / AI secrets को APK में न रखें।
- User को auto-replies, metadata updates और email permissions बंद करने का विकल्प दें।
- YouTube API quota और Google OAuth verification/limited-scope rules लागू हो सकते हैं।
