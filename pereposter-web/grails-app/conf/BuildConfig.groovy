grails.servlet.version = "3.0"
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "target/work"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
        // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
        //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

//    // configure settings for the test-app JVM, uses the daemon by default
//    test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
//    // configure settings for the run-app JVM
//    run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
//    // configure settings for the run-war JVM
//    war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
//    // configure settings for the Console UI JVM
//    console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven"
grails.project.dependency.resolution = {

    inherits("global") {
    }
    log "error"
    checksums true
    legacyResolve false

    repositories {
        inherits true

        grailsPlugins()
        grailsHome()
        mavenLocal()
        grailsCentral()
        mavenCentral()

        mavenRepo "http://repo.spring.io/milestone/"
    }

    dependencies {
        compile 'org.postgresql:postgresql:9.2-1003-jdbc4'
        compile "joda-time:joda-time-hibernate:1.3"
        compile "joda-time:joda-time:2.3"
        compile 'com.pereposter:pereposter-api:1.0-SNAPSHOT'
        compile 'com.google.guava:guava:15.0'
        compile 'com.googlecode.flyway:flyway-core:2.2.1'
    }

    plugins {
        build ":tomcat:7.0.42"

        compile ':cache:1.1.1'
        compile ":spring-security-core:2.0-RC2"
        compile ":gflyway2:0.2.1"
        compile ":twitter-bootstrap:3.0.0"

        runtime ":hibernate4:4.1.11.2"
        runtime ":jquery:1.10.2"
        runtime ":resources:1.2.1"

    }
}
