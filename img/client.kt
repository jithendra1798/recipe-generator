import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

fun main() {
    val client = OkHttpClient()

    val url = "http://10.28.201.231:8000/process-names"

    // Your JSON payload
    val json = """{"names": ["Alice", "Bob", "Charlie"]}"""
    val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
    val body = json.toRequestBody(mediaType)

    val request = Request.Builder()
        .url(url)
        .post(body)   // POST request with JSON body
        .build()

    client.newCall(request).execute().use { response ->
        println("Response code: ${response.code}")
        println("Response body: ${response.body?.string()}")
    }
}
