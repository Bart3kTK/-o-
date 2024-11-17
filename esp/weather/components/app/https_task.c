#include "esp_http_client.h"
#include "esp_log.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include <string.h>

#define POST_REQUEST_DELAY_MS 20000

#define POST_URL "http://localhost:8080/weather/masurements"

#define TAG "HTTPS_TASK"

esp_err_t _http_event_handler(esp_http_client_event_t *evt) {
<<<<<<< HEAD
  switch (evt->event_id) {
  case HTTP_EVENT_REDIRECT:
    ESP_LOGI(TAG, "HTTP_EVENT_REDIRECT");
    break;
  case HTTP_EVENT_ERROR:
    ESP_LOGI(TAG, "HTTP_EVENT_ERROR");
    break;
  case HTTP_EVENT_ON_CONNECTED:
    ESP_LOGI(TAG, "HTTP_EVENT_ON_CONNECTED");
    break;
  case HTTP_EVENT_HEADER_SENT:
    ESP_LOGI(TAG, "HTTP_EVENT_HEADER_SENT");
    break;
  case HTTP_EVENT_ON_HEADER:
    ESP_LOGI(TAG, "HTTP_EVENT_ON_HEADER, key=%s, value=%s", evt->header_key,
             evt->header_value);
    break;
  case HTTP_EVENT_ON_DATA:
    ESP_LOGI(TAG, "HTTP_EVENT_ON_DATA, len=%d", evt->data_len);
    if (!esp_http_client_is_chunked_response(evt->client)) {
      // Write out data
      printf("%.*s", evt->data_len, (char *)evt->data);
    }
    break;
  case HTTP_EVENT_ON_FINISH:
    ESP_LOGI(TAG, "HTTP_EVENT_ON_FINISH");
    break;
  case HTTP_EVENT_DISCONNECTED:
    ESP_LOGI(TAG, "HTTP_EVENT_DISCONNECTED");
    break;
  }
  return ESP_OK;
}

char *prepare_post_data() {

  char *post_data =
      "{\n\t\"pressure\": 21.0, \"temperature1\": 37.0, \"temperature2\": 1.0, "
      "\"rainDetected\": 2.0, \"humidity\": 3.0, \"lightIntensity\": 4.0, "
      "\"gasConcentration\": 5.0\n}";

  return post_data;
}

void http_post_task(void *pvParameters) {

<<<<<<< HEAD
  const char *post_data = prepare_post_data();

  while (1) {
    esp_http_client_config_t config = {
        .url = POST_URL,
        .event_handler = _http_event_handler,
    };
    esp_http_client_handle_t client = esp_http_client_init(&config);

    esp_http_client_set_url(client, POST_URL);
    esp_http_client_set_method(client, HTTP_METHOD_POST);
    esp_http_client_set_post_field(client, post_data, strlen(post_data));

    esp_err_t err = esp_http_client_perform(client);

    if (err == ESP_OK) {
      // ESP_LOGI(TAG, "HTTPS POST Status = %d, content_length = %d",
      // esp_http_client_get_status_code(client),
      // esp_http_client_get_content_length(client));
    } else {
      // ESP_LOGE(TAG, "HTTP POST request failed: %s", esp_err_to_name(err));
    }

    esp_http_client_cleanup(client);

    // Delay for 20 seconds
    vTaskDelay(POST_REQUEST_DELAY_MS / portTICK_PERIOD_MS);
  }
}

void http_post_task_create(void) {
  xTaskCreate(&http_post_task, "https_post_task", 8192, NULL, 5, NULL);
}