![da-android-real-estate-manager](https://user-images.githubusercontent.com/78255467/211861909-49492dc9-09db-42f3-bde8-e7b3d0c7c7b9.svg)
[![forthebadge](https://forthebadge.com/images/badges/built-for-android.svg)](https://forthebadge.com)

# VERSION FRANCAISE
(English translation in the 2nd part of the documentation below)


# REAL ESTATE MANAGER
Ce dépôt contient une application pour le P9 du parcours **DA ANDROID**, intitulée **Real Estate Manager**.


## Technologies
**100% KOTLIN**, ce projet a été réalisé à partir de l'IDE Android Studio.


## Fonctionnalités

Cette applcation permet à un utilisateur de :

- Créer un compte utilisateur à partir d'une adresse e-mail
- Afficher les biens immobiliers situés autour de lui sur une carte, ou sous forme de liste
- Filtrer la liste des biens immobiliers selon ses critères (surface, prix, nombre de pièces, date de publication...)
- Consulter la fiche détaillée d'un bien (adresse, surface, prix, nombre de pièces, services de proximité...)
- Modifier son compte (nom d'utilisateur, avatar, adresse e-mail...)
- Supprimer son compte utilisateur
- Ajouter un ou plusieurs biens immobiliers dans sa liste de favoris
- Consulter sa liste de favoris

Elle permet aussi à un agent immobilier de :

- Créer son compte agent immobilier
- Modifier ou supprimer son compte agent
- Créer un bien immobilier et le publier dans l'application
- Modifier un bien immobilier créé par ses soins
- Afficher la liste des biens immobiliers dont il est en charge


![Fonctionnalités 1](https://user-images.githubusercontent.com/78255467/214594809-8d521a28-7e3e-4c74-8638-730af7b3884a.png)

![Fonctionnalités 2](https://user-images.githubusercontent.com/78255467/214577592-1b2d163a-8c71-4157-b1bf-049974381aba.png)



## Tests

L'application a été testée par émulation sur les appareils suivants :
  - Pixel 4 (API 30)
  - Pixel 5X (API 30)
  - Nexus 5 (API 30)
  - Nexus 5X (API 33)

Elle a également été testée dans des conditions réelles, sur un smartphone physique Samsung Galaxy S20 et sur une tablette Galaxy A7 (API 33).


## Langues

L'application est entièrement traduite en **Anglais** et en **Français**, selon la langue de l'appareil que vous utilisez.


## Bibliothèques / API

Cette application utilise les bibliothèques / API suivantes :
- Material Design
- Firebase (UI, Firestore Database, Storage)
- Glide
- Easy Permissions
- Google Map
- Google Places (nearbySearch)
- JUnit
- Mockito
- RX Java
- Smarteist (Image slider)
- OK HTTP 3
- Room (SQLite database)


## Installation

- Ouvrez votre IDE préféré (ici Android Studio)
- Ouvrez un nouveau projet et choississez "Get From Version Control"
- Clonez l'application en copiant / collant ce lien : https://github.com/benlinux1/DA_ANDROID_REAL_ESTATE_MANAGER.git
- Spécifiez un dossier de destination sur votre ordinateur et cliquez sur "clone"
- Voici les étapes en images :

![GIT](https://user-images.githubusercontent.com/78255467/212286561-8427a318-39f6-45fa-a7d3-a85da5fdb3c2.png)

- L'application va ensuite se compiler dans Android Studio


## Utilisation / Lancement de l'application

Vous pouvez utiliser cette application via un émulateur, pour une simulation sur le mobile ou la tablette de votre choix.
Pour ce faire, rendez-vous dans le ruban à droite de la fenêtre puis "create device" et laissez-vous guider.

Vous pouvez également utiliser cette application sur votre smartphone personnel en choississant l'option "physical"
Ensuite, lancez l'application en cliquant sur le bouton triangulaire "Run", comme ci-dessous :

![Android Studio](https://user-images.githubusercontent.com/78255467/163193524-89842086-ca39-475c-afc2-e39e3e586f68.png)


## Contribuez au projet

Real Estate Manager est un projet open source. Vous pouvez donc en utiliser le code source à votre guise pour développer vos propres fonctionnalités.


## Auteurs

Notre équipe : BenLinux & FranckBlack


==========================================================================================
==========================================================================================


![da-android-real-estate-manager](https://user-images.githubusercontent.com/78255467/211861909-49492dc9-09db-42f3-bde8-e7b3d0c7c7b9.svg)
[![forthebadge](https://forthebadge.com/images/badges/built-for-android.svg)](https://forthebadge.com)


# ENGLISH VERSION
(version française en 1ère partie de la documentation)


# Real Estate Manager

This repository contains a mini app for the **DA Android** 9th projet, untitled **Real Estate Manager**.


## Technologies

**100% KOTLIN**, this project was made with Android Studio IDE.


## Features

This app allows an user to :

- Create an account with an email address
- Display all properties located around him on a dynamic map, or as a list
- Filter the properties list according to his own requirements (surface, price, number of rooms, date of publication...)
- Consult detailled sheet of properties (address, surface, price, number of bedrooms, local services...)
- Update his account (user name, avatar, email...)
- Delete his account
- Add one or few properties in his favorites list
- Display his favorites

It also allows a realtor to :

- Create a realtor account accounding to his agency's password
- Update or delete his realtor account
- Create a property in the app and post it
- Update a property that he posted
- Display all properties for which he's responsible

![Features 1](https://user-images.githubusercontent.com/78255467/212286298-82cb7b8d-b9a6-4ce2-a2ad-57c4ea1531ad.png)

![Features 2](https://user-images.githubusercontent.com/78255467/214597277-81cd544a-8c4f-48ca-b8d2-d97895c024a4.png)



## Tests

This app has been tested on following devices :

  - Pixel 4 (API 30)
  - Pixel 5X (API 30)
  - Nexus 5 (API 30)
  - Nexus 5X (API 33)

It was also tested in real conditions, on a physical phone Samsung Galaxy S20 and Galaxy Tab A7 (API 33).


## Languages

Real Estate Manager is **fully available in English and in French languages**, automatically set according to your device's general language.


## Libraries / API

This app uses the following libraries / API :
- Material Design
- Firebase (UI, Firestore Database, Storage)
- Glide
- Easy Permissions
- Google Map
- Google Places (nearbySearch)
- JUnit
- Mockito
- RX Java
- Smarteist (Image slider)
- OK HTTP 3
- Room (SQLite database)


## Install
- Open your favorite IDE (here Android Studio)
- Open a new project and choose "Get From Version Control" option
- Clone the app by copying / pasting this repository link : https://github.com/benlinux1/DA_ANDROID_REAL_ESTATE_MANAGER.git
- Specify a destination folder name on your computer and click "clone"
- Here are the steps to illustrate in pictures :

![GIT](https://user-images.githubusercontent.com/78255467/212286561-8427a318-39f6-45fa-a7d3-a85da5fdb3c2.png)

- Next, the app will compile in Android Studio


## Use / How to launch app

You can use this app with an emulator, for a chosen mobile simulation.
To do that, go to the tools ribbon in the right part of the window and click "create device". Then, follow the instructions on screen.

You can also use this app with your own smartphone, choosing "physical" option in device manager,
Then, launch app clicking on the green triangle button "Run", like in this example :

![Android Studio](https://user-images.githubusercontent.com/78255467/163193524-89842086-ca39-475c-afc2-e39e3e586f68.png)


## Contribute to the project

Real Estate Manager is an open source project. Feel free to fork the source and contribute with your own features.


## Authors

Our code squad : BenLinux & FranckBlack
