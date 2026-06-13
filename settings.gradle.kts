rootProject.name = "akibaroom"

include(
    "applications:api",
    "domain",
    "infrastructure:jpa",
    "infrastructure:redis",
    "shared:common",
)
