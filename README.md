# 💊 Système de Gestion de Pharmacie

Une application de bureau (Desktop) robuste et moderne développée en **Java** pour la gestion complète des opérations d'une pharmacie. L'application utilise une architecture **MVC (Modèle-Vue-Contrôleur)**, une interface graphique **Java Swing** interactive et une base de données **MySQL** pour la persistance des données.

---

## 🚀 Fonctionnalités Clés

L'application est découpée en plusieurs modules accessibles via des onglets dédiés :

*   📊 **Tableau de Bord (Dashboard)** : Statistiques en temps réel sur les ventes, les produits les plus demandés, le chiffre d'affaires sur une période et les alertes.
*   📦 **Gestion des Médicaments** : Ajout, modification, suppression et recherche de médicaments. Suivi des compositions, formes pharmaceutiques, dosages et prix.
*   ⚠️ **Gestion du Stock & Alertes** : Alerte automatique en cas de stock faible (quantité sous le seuil critique) et détection des produits périmés ou proches de la date de péremption.
*   👥 **Gestion des Clients** : Fichier client complet avec coordonnées (nom, prénom, téléphone, adresse) et historique des achats.
*   🏥 **Gestion des Ordonnances** : Enregistrement des ordonnances médicales présentées par les clients (médecin traitant, date, notes) pour la vente réglementée.
*   🤝 **Gestion des Fournisseurs** : Base de données des fournisseurs de médicaments pour faciliter le réapprovisionnement.
*   📋 **Gestion des Commandes** : Création et suivi des commandes de réapprovisionnement auprès des fournisseurs avec gestion du statut (En attente, Reçue, Annulée).
*   💰 **Gestion des Ventes & Facturation** :
    *   Enregistrement des ventes avec vérification automatique et mise à jour dynamique des stocks.
    *   Génération automatique et impression de factures détaillées sous format texte (.txt) enregistrées localement dans le dossier `/factures`.
*   🏢 **Profil de la Pharmacie** : Configuration des informations de l'établissement (nom, adresse, téléphone, responsable).

---

## 🛠️ Technologies Utilisées

*   **Langage** : Java (JDK 17 ou supérieur)
*   **Interface Graphique** : Java Swing (AWT / javax.swing)
*   **Base de Données** : MySQL
*   **Persistance** : JDBC (Java Database Connectivity) avec le driver MySQL Connector/J
*   **Architecture** : Modèle-Vue-Contrôleur (MVC) pour une séparation claire des responsabilités

---

## 📂 Structure du Projet

Le projet est organisé selon l'architecture MVC :

```text
src/
├── Main/              # Point d'entrée de l'application (Main.java)
├── model/             # Classes de données (Client, Medicament, Vente, Commande, etc.)
├── dao/               # Data Access Objects - Communication avec la base de données MySQL
├── controller/        # Contrôleurs pour la logique métier et le traitement des données
├── view/              # Interfaces graphiques Swing (GestionPharmacieView et les différents Panels)
└── module-info.java   # Déclaration des modules requis (java.sql, java.desktop)
```

---

## 🗄️ Initialisation de la Base de Données

Pour faire fonctionner l'application, vous devez importer le schéma de base de données suivant dans votre serveur MySQL :

```sql
CREATE DATABASE IF NOT EXISTS gestion_pharmacie;
USE gestion_pharmacie;

-- Table Pharmacie
CREATE TABLE IF NOT EXISTS Pharmacie (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    adresse VARCHAR(255),
    telephone VARCHAR(20),
    email VARCHAR(100),
    responsable VARCHAR(100)
);

-- Table Client
CREATE TABLE IF NOT EXISTS Client (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    adresse VARCHAR(255)
);

-- Table Fournisseur
CREATE TABLE IF NOT EXISTS Fournisseur (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nomSociete VARCHAR(150) NOT NULL,
    contact VARCHAR(100),
    adresse VARCHAR(255)
);

-- Table Medicament
CREATE TABLE IF NOT EXISTS Medicament (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nomCommercial VARCHAR(150) NOT NULL,
    composition VARCHAR(255),
    forme VARCHAR(50),
    dosage VARCHAR(50),
    prix DECIMAL(10, 2) NOT NULL,
    quantiteStock INT DEFAULT 0,
    datePeremption DATE NOT NULL,
    seuilAlerte INT DEFAULT 5
);

-- Table Ordonnance
CREATE TABLE IF NOT EXISTS Ordonnance (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idClient INT NOT NULL,
    medecin VARCHAR(100) NOT NULL,
    dateOrdonnance DATE NOT NULL,
    notes TEXT,
    FOREIGN KEY (idClient) REFERENCES Client(id) ON DELETE CASCADE
);

-- Table Vente
CREATE TABLE IF NOT EXISTS Vente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idClient INT NOT NULL,
    idOrdonnance INT,
    dateVente DATE NOT NULL,
    FOREIGN KEY (idClient) REFERENCES Client(id) ON DELETE CASCADE,
    FOREIGN KEY (idOrdonnance) REFERENCES Ordonnance(id) ON DELETE SET NULL
);

-- Table LigneVente
CREATE TABLE IF NOT EXISTS LigneVente (
    idVente INT NOT NULL,
    idMedicament INT NOT NULL,
    quantite INT NOT NULL,
    prixUnitaire DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (idVente, idMedicament),
    FOREIGN KEY (idVente) REFERENCES Vente(id) ON DELETE CASCADE,
    FOREIGN KEY (idMedicament) REFERENCES Medicament(id) ON DELETE CASCADE
);

-- Table Commande
CREATE TABLE IF NOT EXISTS Commande (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idFournisseur INT NOT NULL,
    dateCommande DATE NOT NULL,
    statut VARCHAR(50) DEFAULT 'En attente',
    FOREIGN KEY (idFournisseur) REFERENCES Fournisseur(id) ON DELETE CASCADE
);

-- Table LigneCommande
CREATE TABLE IF NOT EXISTS LigneCommande (
    idCommande INT NOT NULL,
    idMedicament INT NOT NULL,
    quantiteCommandee INT NOT NULL,
    prixAchat DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (idCommande, idMedicament),
    FOREIGN KEY (idCommande) REFERENCES Commande(id) ON DELETE CASCADE,
    FOREIGN KEY (idMedicament) REFERENCES Medicament(id) ON DELETE CASCADE
);
```

---

## 🔧 Configuration de la Connexion

Modifiez les paramètres de connexion à votre base de données dans le fichier :
`src/dao/Utilitaire.java`

```java
String url = "jdbc:mysql://localhost:3306/gestion_pharmacie";
conn = DriverManager.getConnection(url, "VOTRE_UTILISATEUR", "VOTRE_MOT_DE_PASSE");
```

---

## 🏃 Exécution du Projet

1.  Assurez-vous que votre serveur MySQL est démarré et que la base de données a été configurée et initialisée.
2.  Ajoutez le driver `mysql-connector-j` dans le build path de votre projet (dans le dossier `lib/`).
3.  Exécutez la classe principale : `src/Main/Main.java`.
