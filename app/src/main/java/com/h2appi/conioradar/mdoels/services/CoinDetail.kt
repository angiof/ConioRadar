data class CoinDetail(
    val description: Description,
    val links: Links
)

data class Description(
    val en: String
)

data class Links(
    val homepage: List<String> // Lista di URL della homepage
)

// Utilizzo esempio per accedere ai dati:
// val urlHomepagePrincipale = coinDetail.links.homepage.firstOrNull { it.isNotBlank() } ?: "URL non fornito"
