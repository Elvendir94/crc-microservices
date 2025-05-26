rootProject.name = "CRC-Microservices"

include(":service-a")
include(":service-b")

project(":service-a").projectDir = file("microservices/service-a")
project(":service-b").projectDir = file("microservices/service-b")