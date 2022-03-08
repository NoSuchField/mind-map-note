var baseUrl = "http://www.mindmapnote.com/api"

if (process.env.NODE_ENV == 'development') {
    baseUrl = "http://www.mindmapnote.com/api"
} else if (process.env.NODE_ENV == 'production') {
    baseUrl = "http://www.mindmapnote.com/api"
}

export default baseUrl