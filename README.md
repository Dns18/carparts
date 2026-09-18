# Autóalkatrész Webshop & Raktárkezelő Rendszer

Egy teljes körű Full-Stack webalkalmazás, amely Java Spring Boot backenddel és modern Bootstrap alapú frontenddel valósítja meg egy autóalkatrész-raktár és webshop működését.

---

## 🛠️ Felhasznált technológiák

* **Backend:** Java 26, Spring Boot, Spring Data JPA, Spring Security (HTTP Basic & Google OAuth2)
* **Adatbázis:** H2 Database (beágyazott, fájl/memória alapú)
* **Frontend:** HTML5, Bootstrap 5 (Bootstrap Icons), Vanilla JavaScript (Fetch API)
* **Build eszköz:** Maven

---

## ✨ Főbb funkciók és teljesített szempontok

1. **Perzisztált típusok (1:N kapcsolat):**
   * **Kategóriák (`PartCategory`):** Pl. Fékrendszer, Futómű, stb. (Adatok: név, leírás, stb.).
   * **Alkatrészek (`CarPart`):** Az alkatrészekhez tartozik cikkszám, név, ár, raktárkészlet és egy 1:N kapcsolat révén a kategória.
2. **Teljes körű CRUD műveletek:** Mindkét entitás teljes körűen létrehozható, olvasható, szerkeszthető és törölhető a felületről.
3. **Üzleti logika:**
   * **Készletcsökkentés (Eladás):** A rendszer ellenőrzi a raktárkészletet, és ha nincs elegendő áru, hibaüzenetet dob.
   * **Statisztikák:** Automatikusan számolja a teljes raktárértéket ($\sum \text{ár} \times \text{mennyiség}$) és jelzi a kritikus készletű tételeket (< 5 db).
4. **Autentikáció és Jogosultságkezelés:**
   * Támogatja a hagyományos bejelentkezést és a **Google OAuth2** alapú bejelentkezést is.
   * Szerepkör alapú hozzáférés (ADMIN / USER felületek szétválasztása).
5. **Profil- és Rendeléskezelés:**
   * Saját profilkép feltöltése (Base64 kódolással) vagy a Google-fiók profilképének automatikus átvétele.
   * Leadott rendelések története, azok törlésének lehetősége a saját előzményekből.

---
