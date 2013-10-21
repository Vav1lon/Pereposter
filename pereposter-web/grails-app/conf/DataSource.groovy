dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    username = "pereposter_user"
    password = "PKaggA3s1V7CMWXoMSQJCwo7K"
    url = "jdbc:postgresql://localhost:5432/pereposter"
    dialect = "org.hibernate.dialect.PostgreSQLDialect"
    loggingSql = true
}

hibernate {
    cache.use_second_level_cache = false
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
}

environments {
    development {
        dataSource {
            dbCreate = ""
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    production {
        dataSource {
            loggingSql = false
            dbCreate = "validate"
            pooled = true
            properties {
                maxActive = -1
                minEvictableIdleTimeMillis = 1800000
                timeBetweenEvictionRunsMillis = 1800000
                numTestsPerEvictionRun = 3
                testOnBorrow = true
                testWhileIdle = true
                testOnReturn = true
                validationQuery = "SELECT 1"
            }
        }
    }
}