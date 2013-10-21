class UrlMappings {

    static mappings = {

        name promo: "/promo/" {
            controller = "main"
            action = [GET: "promo"]
        }

        "/$controller/$action?/$id?(.${format})?" {
            constraints {
            }
        }

        "/"(controller: 'main', action: 'index')
        "500"(view: '/error')
    }
}
