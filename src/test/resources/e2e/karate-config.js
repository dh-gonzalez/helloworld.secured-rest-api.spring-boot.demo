function fn() {
    var config = {};

    // Accept self-signed certificates
    // TODO : this has to be removed with signed certificates
    karate.configure('ssl', true);

    return config;
}
