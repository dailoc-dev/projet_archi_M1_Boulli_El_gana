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

**Remarque** : il y a une erreur de relative path dans les pom.xml des modules (pas le pom.xml en root du projet). 
Il faut modifier la balise relative path comme suit :  
````xml
<parent>
    <groupId>org.archi</groupId>
    <artifactId>architecture_project</artifactId>
    <version>1.0-SNAPSHOT</version>
    <relativePath>../pom.xml</relativePath>>
</parent>
````

### V2

Disponible dans le tag v2.

Cette version ajoute une fonctionnalité de service mapping. Deux services "/hello" et "/goodbye" sont créés. Chaque worker est associé à un ou plusieurs services qu'il peut supporter : 
- worker 1 : "/hello", "/goodbye"
- worker 2 : "/hello"
- worker 3 : "/goodbye"

Le load-balancer se charge d'identifier le service demandé et les workers le supportant. Ensuite, il choisit au hasard un de ces workers pour fournir la réponse à l'utilisateur.

Le mapping workers/services est fait dynamiquement grâce à des variables d'environnement définies dans le docker-compose.yml.

Il est possible pour l'utilisateur d'ajouter un paramètre "name" à la requête. Le load-balancer vérifie la présence de ce paramètre, s'il est présent il renvoie une réponse du type "Hello {name}, I'm {worker.id}" ; sinon il renvoie une réponse plus générique "Hello from {worker.id}".

**Exemples pour tester** : 
- http://localhost:8084/hello
- http://localhost:8084/hello?name=Pascal
- http://localhost:8084/goodbye
- http://localhost:8084/goodbye?name=Pascal

Exécuter plusieurs fois la requête pour voir le changement aléatoire de worker.


### V3

Disponible dans le tag v3.

Ajout des noeuds et du WebServer. Désormais, le docker-compose ne gère plus le lancement des conteneurs pour les workers ; c'est le docker du node qui s'en charge.

Le WebServer identifie les services et le nombre de workers demandés, puis il démarre les workers correspondants.

--> Ne fonctionne pas, il y a encore un problème avec le lancement du node container...