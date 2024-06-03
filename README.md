# Projet architecture logicielle | M1 informatique | Semestre 2 | 2024

**Binome**: Anas EL GANA, Marouan BOULLI

## Description de la structure de l'application

Le projet est organisé en plusieurs modules (un module par composant) : 
- load-balancer
- registry
- worker (3 workers sont créés par la suite)

Chaque module correspond à une application spring comprenant un Dockerfile qui permet d'isoler chacun de ces composants dans un conteneur indépendant.

Un docker-compose permet d'orchestrer ces conteneurs en créant un réseau avec un bridge et en leur attribuant des ports de communication : 
- load-balancer : 8084:8082
- registry : 8080:8080
- worker 1 : 8081:8081
- worker 2 : 8082:8081
- worker 3 : 8083:8081

## Lancer l'application

Exécuter successivement les commandes suivantes (au root du projet) :

```
mvn clean install -DskipTests
```
```
docker-compose up --build
```

## Versions

### V1

Disponible dans le tag v1.

Cette version implémente l'application de base fonctionnelle avec le monitoring.

