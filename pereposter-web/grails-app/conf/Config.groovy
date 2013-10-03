grails.project.groupId = appName
grails.mime.file.extensions = true
grails.mime.use.accept.header = false
grails.mime.types = [
        all: '*/*',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        form: 'application/x-www-form-urlencoded',
        html: ['text/html', 'application/xhtml+xml'],
        js: 'text/javascript',
        json: ['application/json', 'text/json'],
        multipartForm: 'multipart/form-data',
        rss: 'application/rss+xml',
        text: 'text/plain',
        xml: ['text/xml', 'application/xml']
]

grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.views.gsp.sitemesh.preprocess = true
grails.scaffolding.templates.domainSuffix = 'Instance'
grails.json.legacy.builder = false
grails.enable.native2ascii = true
grails.spring.bean.packages = []
grails.web.disable.multipart = false
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
    development {
        grails.logging.jul.usebridge = true
    }
    production {
        grails.logging.jul.usebridge = false
        // TODO: grails.serverURL = "http://www.changeme.com"
    }
}

// log4j configuration
log4j = {

    error 'org.codehaus.groovy.grails.web.servlet',        // controllers
            'org.codehaus.groovy.grails.web.pages',          // GSP
            'org.codehaus.groovy.grails.web.sitemesh',       // layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping',        // URL mapping
            'org.codehaus.groovy.grails.commons',            // core / classloading
            'org.codehaus.groovy.grails.plugins',            // plugins
            'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
            'org.springframework',
            'org.hibernate',
            'net.sf.ehcache.hibernate'

    appenders {
        console name: 'stdout', layout: pattern(conversionPattern: '%m%n')
    }
    info 'com.googlecode.flyway'
}


grails.gorm.failOnError = true

grails.plugins.springsecurity.active = true
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.pereposter.web.entity.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.pereposter.web.entity.UserRole'
grails.plugins.springsecurity.authority.className = 'com.pereposter.web.entity.Role'
grails.plugins.springsecurity.rememberMe.cookieName = 'pereposter_remember_me'
grails.plugins.springsecurity.password.algorithm='SHA-512'

grails.plugins.springsecurity.controllerAnnotations.staticRules = [
        '/js/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/css/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/images/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/login/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/logout/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/user/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/**': ['ROLE_USER']
]

environments {
    development {
        pereposter.core.initSocialAccount.url = 'http://localhost:8082/service/Social/Account/'
    }
    production {
        pereposter.core.initSocialAccount.url = 'http://localhost:8093/service/Social/Account/'
    }
}