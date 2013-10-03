modules = {

    css {
        resource url: 'css/bootstrap.min.css', disposition: 'head'
        resource url: 'css/bootstrap-theme.min.css', disposition: 'head'
        resource url: 'css/justified-nav.css', disposition: 'head'
        resource url: 'css/bootstrap-select.min.css', disposition: 'head'
    }

    js {
        dependsOn('jquery')
        resource url: 'js/application.js', disposition: 'head'
        resource url: 'js/bootstrap.min.js', disposition: 'head'
        resource url: 'js/bootstrap-select.min.js', disposition: 'head'
    }

}