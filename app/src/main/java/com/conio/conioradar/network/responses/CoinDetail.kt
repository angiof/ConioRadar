data class CoinDetail(
    val description: Description,
    val links: Links
)

data class Description(
    val en: String
)

data class Links(
    val homepage: List<String>
)

