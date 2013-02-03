mmm-prj
=======
Authors: Clem, Ping, Guillaume, Max

Application Android permettant de choisir et d'organiser son agenda via les
ressources obtenues sur ADE (actuellement les ressources de Univ Rennes 1)

Pour commencer à développer :
  - cloner le dépôt
  - importer le projet dans Eclipse
  - télécharger ActionBarSherlock https://api.github.com/repos/JakeWharton/ActionBarSherlock/zipball/4.2.0
  - Configurer Eclipse pour que tout roule en suivant ça http://stackoverflow.com/a/9604144
  - Ajouter les dépendances pour la map :
    - SDK Manager -> Extras -> Installer "Google Play Services"
    - Copier le projet "Google Services Lib" dans votre workspace ($ANDROID_SDK_FOLDER/extras/google/google_play_services/libproject/google-play-services_lib)
    - Importer ce projet dans Eclipse & l'ajouter en dépendance dans notre projet
    - Pour pouvoir utiliser ma clé débug de l'API Maps, remplacer votre fichier "debug.keystore" avec celui fourni à la racine du repo (le votre est dans ~/.android/ sur OS X & Linux, dans C:\Documents and Settings\ .android\ pour Windows XP, et C:\Users\ .android\ pour Windows Vista and Windows 7)

Have fun ;)
