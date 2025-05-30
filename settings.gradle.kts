rootProject.name = "CRC-Microservices"

include(":service-a")
include(":service-b")
include(":service-c")

project(":service-a").projectDir = file("microservices/service-a")
project(":service-b").projectDir = file("microservices/service-b")
project(":service-c").projectDir = file("microservices/service-c")

