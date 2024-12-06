function fn() {
    var config = {};

    // Désactiver la vérification SSL
    karate.configure('ssl', true);

    return config;
}
