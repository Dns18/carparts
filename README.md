# Autóalkatrész Webshop & Raktárkezelő Rendszer

Egy teljes körű Full-Stack webalkalmazás, amely Java Spring Boot backenddel és modern Bootstrap alapú frontenddel valósítja meg egy autóalkatrész-raktár és webshop működését.

---

## 🛠️ Felhasznált technológiák

* **Backend:** Java 26, Spring Boot, Spring Data JPA, Spring Security (HTTP Basic & Google OAuth2)
* **Adatbázis:** H2 Database (beágyazott, fájl/memória alapú)
* **Frontend:** HTML5, Bootstrap 5 (Bootstrap Icons), Vanilla JavaScript (Fetch API)
* **Build eszköz:** Maven

---

## Főbb funkciók

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

# Auto Parts Webshop & Inventory Management System

A full-stack web application that implements the operation of an auto parts warehouse and webshop using a Java Spring Boot backend and a modern Bootstrap-based frontend.

---

## 🛠️ Technologies Used

* **Backend:** Java 26, Spring Boot, Spring Data JPA, Spring Security (HTTP Basic & Google OAuth2)
* **Database:** H2 Database (embedded, file/memory-based)
* **Frontend:** HTML5, Bootstrap 5 (Bootstrap Icons), Vanilla JavaScript (Fetch API)
* **Build Tool:** Maven

---

## Key Features

1. **Persistent Types (1:N Relationship):**
   * **Categories (`PartCategory`):** e.g., Braking system, Suspension, etc. (Data: name, description, etc.).
   * **Parts (`CarPart`):** Parts include a part number, name, price, stock quantity, and a category via a 1:N relationship.
2. **Full CRUD Operations:** Both entities can be fully created, read, updated, and deleted from the UI.
3. **Business Logic:**
   * **Stock Reduction (Sales):** The system checks the inventory and returns an error message if there is insufficient stock.
   * **Statistics:** Automatically calculates the total inventory value ($\sum \text{price} \times \text{quantity}$) and highlights low-stock items (< 5 pcs).
4. **Authentication & Authorization:**
   * Supports both traditional login and **Google OAuth2** authentication.
   * Role-based access control (separation of ADMIN / USER interfaces).
5. **Profile & Order Management:**
   * Uploading a custom profile avatar (via Base64) or automatic retrieval of the Google account profile picture.
   * History of placed orders and the ability to delete them from personal history.

---
