plugins {
    java
    maven
}

dependencies {
    compile("org.slf4j:slf4j-api:1.7.25")
    compile("com.networknt:service:1.5.20")
    compile("org.web3j:crypto:3.6.0")
    compile("com.fasterxml.jackson.core:jackson-databind:2.9.6")
    testCompile("junit:junit:4.12")
    testCompile("ch.qos.logback:logback-classic:1.2.3")
}