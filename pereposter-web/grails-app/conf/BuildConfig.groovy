grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.7
grails.project.source.level = 1.7
grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
    inherits("global") {}
    log "error"
    checksums true
    legacyResolve false

    repositories {
        inherits true
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()

        mavenRepo "http://repository.codehaus.org"
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.com/maven2/"
    }

    dependencies {
        compile "joda-time:joda-time-hibernate:1.3"
        compile "joda-time:joda-time:2.3"
        compile 'com.google.guava:guava:15.0'
        compile 'org.apache.httpcomponents:httpclient:4.3'
        compile 'org.postgresql:postgresql:9.2-1003-jdbc4'
        compile 'com.googlecode.flyway:flyway-core:2.2.1'
    }

    plugins {
        runtime ":hibernate:$grailsVersion"
        runtime ":resources:1.2.1"
        runtime ":jquery:1.10.2"
        compile ":spring-security-core:1.2.7.3"
        //compile ":mail:1.0.1"

        build ":tomcat:$grailsVersion"

        compile ':cache:1.1.1'
        compile ":gflyway2:0.2.1"
    }
}
