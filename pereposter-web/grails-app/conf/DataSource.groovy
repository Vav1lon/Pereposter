dataSource {
    pooled = true
    dbCreate = ""
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
    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
}

environments {
    development {
        dataSource {
            pooled = true
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            password = ""
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000"
        }
    }
    production {
        dataSource {
            loggingSql = false
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
