class UrlMappings {

    static mappings = {


        "/user/registration" {
            controller = "user"
            action = [GET: "registration", POST: "save"]
        }

        "/social/add" {
            controller = "socialBoard"
            action = [POST: "addNewScoailNetwork"]
        }


        "/logout" {
            controller = "logout"
            action = "index"
        }

        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'socialBoard', action: 'index')
        "500"(view: '/error')
        "404"(view: '/error')
        "403"(view: '/error')
    }
}
