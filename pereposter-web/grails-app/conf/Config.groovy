grails.project.groupId = appName

grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
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
        hal: ['application/hal+json', 'application/hal+xml'],
        xml: ['text/xml', 'application/xml']
]

grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']
grails.views.default.codec = "html"
grails.controllers.defaultScope = 'singleton'

grails {
    views {
        gsp {
            encoding = 'UTF-8'
            htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
            codecs {
                expression = 'html' // escapes values inside ${}
                scriptlet = 'html' // escapes output from scriptlets in GSPs
                taglib = 'none' // escapes output from taglibs
                staticparts = 'none' // escapes output from static template parts
            }
        }
        // escapes all not-encoded output at final stage of outputting
        filteringCodecForContentType {
            //'text/html' = 'html'
        }
    }
}

grails.converters.encoding = "UTF-8"
grails.scaffolding.templates.domainSuffix = 'Instance'
grails.json.legacy.builder = false
grails.enable.native2ascii = true
grails.spring.bean.packages = []
grails.web.disable.multipart = false
grails.exceptionresolver.params.exclude = ['password']
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

grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.pereposter.web.entity.internal.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.pereposter.web.entity.internal.UserRole'
grails.plugin.springsecurity.authority.className = 'com.pereposter.web.entity.internal.Role'
grails.plugin.springsecurity.rememberMe.cookieName = 'pereposter_remember_me'
grails.plugin.springsecurity.password.algorithm = 'SHA-512'

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        '/js/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/css/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/images/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/login/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/logout/**': ['IS_AUTHENTICATED_FULLY'],
        '/user/**': ['ROLE_USER'],
        '/promo/**': ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/**': ['IS_AUTHENTICATED_ANONYMOUSLY']
]