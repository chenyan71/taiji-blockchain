plugins {
    java
    maven
}


dependencies {
    compile(project(":taiji-core"))
    compile(project(":taiji-crypto"))
    compile("com.networknt:client:1.5.20")
    compile("com.beust:jcommander:1.72")
    compile("ch.qos.logback:logback-classic:1.2.3")
}
