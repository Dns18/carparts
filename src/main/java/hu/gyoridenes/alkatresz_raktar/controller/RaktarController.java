package hu.gyoridenes.alkatresz_raktar.controller;

import hu.gyoridenes.alkatresz_raktar.model.AppUser;
import hu.gyoridenes.alkatresz_raktar.model.CarPart;
import hu.gyoridenes.alkatresz_raktar.model.CartItem;
import hu.gyoridenes.alkatresz_raktar.model.PartCategory;
import hu.gyoridenes.alkatresz_raktar.repository.CarPartRepository;
import hu.gyoridenes.alkatresz_raktar.repository.PartCategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RaktarController {

    private final CarPartRepository partRepository;
    private final PartCategoryRepository categoryRepository;
    private final hu.gyoridenes.alkatresz_raktar.repository.AppUserRepository userRepository;
    private final hu.gyoridenes.alkatresz_raktar.repository.CartItemRepository cartRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final hu.gyoridenes.alkatresz_raktar.repository.OrderHistoryRepository orderHistoryRepository;

    public RaktarController(CarPartRepository partRepository,
                            PartCategoryRepository categoryRepository,
                            hu.gyoridenes.alkatresz_raktar.repository.AppUserRepository userRepository,
                            hu.gyoridenes.alkatresz_raktar.repository.CartItemRepository cartRepository,
                            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder,
                            hu.gyoridenes.alkatresz_raktar.repository.OrderHistoryRepository orderHistoryRepository) {
        this.partRepository = partRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.passwordEncoder = passwordEncoder;
        this.orderHistoryRepository = orderHistoryRepository;
    }

    // --- KATEGÓRIÁK ÉS ALKATRÉSZEK ---
    @GetMapping("/categories")
    public List<PartCategory> getCategories() { return categoryRepository.findAll(); }

    @PostMapping("/categories")
    public PartCategory createCategory(@RequestBody PartCategory category) { return categoryRepository.save(category); }

    @PutMapping("/categories/{id}")
    public PartCategory updateCategory(@PathVariable Long id, @RequestBody PartCategory category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) { categoryRepository.deleteById(id); }

    @GetMapping("/parts")
    public List<CarPart> getParts() { return partRepository.findAll(); }

    @PostMapping("/parts")
    public CarPart createPart(@RequestBody CarPart part) { return partRepository.save(part); }

    @PutMapping("/parts/{id}")
    public CarPart updatePart(@PathVariable Long id, @RequestBody CarPart part) {
        part.setId(id);
        return partRepository.save(part);
    }

    @DeleteMapping("/parts/{id}")
    public void deletePart(@PathVariable Long id) { partRepository.deleteById(id); }

    @PostMapping("/parts/{id}/sell")
    public ResponseEntity<String> sellPart(@PathVariable Long id, @RequestParam(defaultValue = "1") int amount) {
        CarPart part = partRepository.findById(id).orElseThrow();
        if (part.getStockQuantity() < amount) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hiba: Nincs elegendő raktárkészlet az eladáshoz!");
        }
        part.setStockQuantity(part.getStockQuantity() - amount);
        partRepository.save(part);
        return ResponseEntity.ok("Sikeres eladás!");
    }

    // --- FELHASZNÁLÓK ÉS AUTHENTIKÁCIÓ ---
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser newUser) {
        if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Ez a felhasználónév már foglalt!");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRole("USER");
        userRepository.save(newUser);
        return ResponseEntity.ok("Sikeres regisztráció!");
    }

    private AppUser getOrCreateUser(Principal principal) {
        if (principal instanceof OAuth2AuthenticationToken oauthToken) {
            OAuth2User oauthUser = oauthToken.getPrincipal();
            String email = oauthUser.getAttribute("email");
            String googlePicture = oauthUser.getAttribute("picture"); // A Google képe

            if (email == null || email.isBlank()) {
                throw new IllegalStateException("A Google-fiók nem adott vissza e-mail címet.");
            }

            AppUser user = userRepository.findByUsername(email).orElseGet(() -> {
                AppUser newUser = new AppUser();
                newUser.setUsername(email);
                newUser.setEmail(email);
                newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                newUser.setRole("USER");
                return userRepository.save(newUser);
            });

            // Ha van Google képe és még nincs beállítva, vagy megváltozott, elmentjük
            if (googlePicture != null && !googlePicture.equals(user.getAvatarUrl())) {
                user.setAvatarUrl(googlePicture);
                userRepository.save(user);
            }
            return user;
        }

        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Felhasználó nem található: " + username));
    }

    @GetMapping("/auth/role")
    public ResponseEntity<String> getRole(Principal principal) {
        if (principal == null) return ResponseEntity.ok("GUEST");
        return ResponseEntity.ok(getOrCreateUser(principal).getRole());
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        AppUser user = getOrCreateUser(principal);
        boolean isOAuth = principal instanceof OAuth2AuthenticationToken;

        java.util.Map<String, Object> body = new java.util.HashMap<>();
        body.put("username", user.getUsername());
        body.put("email", user.getEmail());
        body.put("role", user.getRole());
        body.put("oauthProvider", isOAuth ? "google" : null);
        body.put("avatarUrl", user.getAvatarUrl()); // Visszaküldjük a képet a frontendnek

        return ResponseEntity.ok(body);
    }

    // --- PROFILKÉP FELTÖLTÉSE (Lokális usereknek) ---
    @PostMapping("/auth/avatar")
    public ResponseEntity<String> uploadAvatar(@RequestBody java.util.Map<String, String> payload, Principal principal) {
        AppUser user = getOrCreateUser(principal);
        user.setAvatarUrl(payload.get("avatarUrl"));
        userRepository.save(user);
        return ResponseEntity.ok("Profilkép frissítve!");
    }

    // --- KOSÁR ÉS RENDELÉSEK ---
    @GetMapping("/cart")
    public List<CartItem> getMyCart(Principal principal) {
        return cartRepository.findByUser(getOrCreateUser(principal));
    }

    @PostMapping("/cart/add/{partId}")
    public ResponseEntity<String> addToCart(@PathVariable Long partId, Principal principal) {
        AppUser user = getOrCreateUser(principal);
        CarPart part = partRepository.findById(partId).orElseThrow();

        if (part.getStockQuantity() <= 0) return ResponseEntity.badRequest().body("Nincs raktáron!");

        CartItem item = new CartItem();
        item.setUser(user);
        item.setPart(part);
        item.setQuantity(1);
        cartRepository.save(item);

        return ResponseEntity.ok("Kosárba rakva!");
    }

    @PostMapping("/cart/checkout")
    @org.springframework.transaction.annotation.Transactional
    public ResponseEntity<String> checkout(Principal principal) {
        AppUser user = getOrCreateUser(principal);
        List<CartItem> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) return ResponseEntity.badRequest().body("Üres a kosár!");

        for (CartItem item : cartItems) {
            CarPart part = item.getPart();
            if (part.getStockQuantity() < item.getQuantity()) {
                return ResponseEntity.badRequest().body("Nincs elég készlet: " + part.getName());
            }

            part.setStockQuantity(part.getStockQuantity() - item.getQuantity());
            partRepository.save(part);

            hu.gyoridenes.alkatresz_raktar.model.OrderHistory history = new hu.gyoridenes.alkatresz_raktar.model.OrderHistory();
            history.setUser(user);
            history.setPartName(part.getName());
            history.setQuantity(item.getQuantity());
            history.setTotalPrice(part.getPrice() * item.getQuantity());
            history.setOrderDate(LocalDateTime.parse(LocalDateTime.now().toString()));

            orderHistoryRepository.save(history);
        }

        cartRepository.deleteByUser(user);
        return ResponseEntity.ok("Sikeres rendelés!");
    }

    @GetMapping("/orders/my")
    public List<hu.gyoridenes.alkatresz_raktar.model.OrderHistory> getMyOrders(Principal principal) {
        return orderHistoryRepository.findByUserOrderByOrderDateDesc(getOrCreateUser(principal));
    }

    // --- ÚJ: RENDELÉS TÖRLÉSE ---
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id, Principal principal) {
        AppUser user = getOrCreateUser(principal);
        hu.gyoridenes.alkatresz_raktar.model.OrderHistory order = orderHistoryRepository.findById(id).orElse(null);

        if (order != null && order.getUser().getId().equals(user.getId())) {
            orderHistoryRepository.delete(order);
            return ResponseEntity.ok("Törölve");
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Nincs jogosultság");
    }
}