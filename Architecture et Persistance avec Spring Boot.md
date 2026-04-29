## Qu'est-ce qu'une Entité ?

Dans le contexte de **Spring Data JPA**, une entité est un objet Java (POJO) qui est mappé directement à une table de votre base de données relationnelle.

- **Le Concept :** C'est la représentation logicielle d'une donnée persistante. Chaque instance de cette classe correspond à une ligne (un enregistrement) dans la table.
    
- **Les Annotations Clés :**
    
    - `@Entity` : Indique à JPA que cette classe doit être transformée en table.
        
    - `@Id` : Définit la clé primaire.
        
    - `@Column` : Permet de personnaliser le nom ou les contraintes de la colonne (ex: `length`, `nullable`).
        

---

## 2. Qu'est-ce qu'une Architecture Multicouche ?

L'architecture multicouche (ou _n-tier architecture_) est un modèle de conception dont le but est de **séparer les responsabilités** au sein d'une application. Au lieu d'avoir un seul bloc de code qui gère tout, on divise l'application en strates indépendantes.

### Les 3 couches classiques :

1. **La couche Présentation (Web/Controller) :** Elle reçoit les requêtes HTTP, valide l'entrée utilisateur et renvoie la réponse (JSON, HTML). Elle ne connaît pas la logique métier.
    
2. **La couche Métier (Service) :** C'est le "cerveau" de l'application. Elle contient les règles de gestion, les calculs et la coordination des données.
    
3. **La couche Accès aux Données (Repository/DAO) :** Elle s'occupe exclusivement de la communication avec la base de données (Requêtes SQL, CRUD).
    

> **Pourquoi faire cela ?** Pour faciliter la maintenance, les tests unitaires (on peut tester le Service sans lancer le Web) et permettre l'évolution technologique d'une couche sans impacter les autres.

---

## 3. Mise en place avec Spring Boot

Spring Boot facilite l'implémentation de cette architecture grâce à son système d'annotations et d'**Injection de Dépendance**.

### Étape A : La couche Repository (Data)

On crée une interface qui hérite de `JpaRepository`. Spring génère automatiquement l'implémentation des méthodes de base (save, delete, find).

Java

```
@Repository
public interface BookRepository extends JpaRepository<Book, Long> { }
```

### Étape B : La couche Service (Business)

On annote la classe avec `@Service`. C'est ici que l'on injecte le Repository.

Java

```
@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) { // Injection par constructeur
        this.repository = repository;
    }
    
    public List<Book> getAvailableBooks() {
        // Logique métier : filtrer les livres disponibles
        return repository.findAll().stream().filter(Book::getAvailable).toList();
    }
}
```

### Étape C : La couche Controller (Presentation)

On utilise `@RestController`. Cette couche appelle le Service, et jamais directement le Repository.

Java

```
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public List<Book> list() {
        return bookService.getAvailableBooks();
    }
}
```

---

## Synthèse du flux de données

1. Le **Client** envoie une requête `GET`.
    
2. Le **Controller** réceptionne la demande et appelle le **Service**.
    
3. Le **Service** applique les règles métier et demande les données au **Repository**.
    
4. Le **Repository** effectue la requête SQL et retourne l'**Entité** au Service.
    
5. Le **Service** retourne le résultat au **Controller**, qui le transforme en JSON pour le **Client**.