class UrlMappings {

    static mappings = {

        name social: "/social/" {
            controller = "socialBoard"
            action = [POST: "addNewSocialNetwork", GET: "index"]
        }

        name addSocial: "/social/add" {
            controller = "socialBoard"
            action = [POST: "insert", GET: "add"]
        }


        "/logout" {
            controller = "logout"
            action = "index"
        }

        "/$controller/$action?/$id?" {
            constraints {
            }
        }

        "/"(controller: 'socialBoard', action: 'index')
        "500"(view: '/error')
        "404"(view: '/error')
        "403"(view: '/error')
    }
}
