var baseUrl = "http://127.0.0.1:8090/api"

if (process.env.NODE_ENV == 'development') {
    baseUrl = "http://127.0.0.1:8090/api"
} else if (process.env.NODE_ENV == 'production') {
    baseUrl = "https://www.mindmapnote.com/api"
}

export default baseUrl