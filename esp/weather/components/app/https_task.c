#include "esp_http_client.h"
#include "esp_log.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include <string.h>

#include "https_task.h"
#include "measures_task.h"

#define POST_REQUEST_DELAY_MS 20000

#define POST_URL "http://192.168.232.163:8080/weather/measurments"
#define TAG "HTTPS_TASK"

esp_err_t _http_event_handler(esp_http_client_event_t *evt) {
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

void generate_random_data(char *buffer, size_t buffer_size) {
  float pressure = (float)(rand() % 100) / 10.0;
  float temperature1 = (float)(rand() % 100) / 10.0;
  float temperature2 = (float)(rand() % 100) / 10.0;
  bool rainDetected = rand() % 2;
  float humidity = (float)(rand() % 100) / 10.0;
  float lightIntensity = (float)(rand() % 100) / 10.0;
  float gasConcentration = (float)(rand() % 100) / 10.0;

  printf("{\n\t\"pressure\": %.1f,\n\t\"temperature1\": "
         "%.1f,\n\t\"temperature2\": "
         "%.1f,\n\t"
         "\"rainDetected\": %s,\n\t\"humidity\": %.1f,\n\t\"lightIntensity\": "
         "%.1f,\n\t"
         "\"gasConcentration\": %.1f\n}",
         pressure, temperature1, temperature2, rainDetected ? "true" : "false",
         humidity, lightIntensity, gasConcentration);
  snprintf(buffer, buffer_size,
           "{\n\t\"pressure\": %.1f,\n\t\"temperature1\": "
           "%.1f,\n\t\"temperature2\": "
           "%.1f,\n\t"
           "\"rainDetected\": %s,\n\t\"humidity\": "
           "%.1f,\n\t\"lightIntensity\": %.1f,\n\t"
           "\"gasConcentration\": %.1f\n}",
           pressure, temperature1, temperature2,
           rainDetected ? "true" : "false", humidity, lightIntensity,
           gasConcentration);
}

void get_sensor_data(char *buffer, size_t buffer_size) {

  measures_data measures = get_measures();

  float pressure = 0.0;
  float temperature2 = 0.0;
  bool rainDetected = false;
  float lightIntensity = 0.0;
  float gasConcentration = 0.0;

  snprintf(buffer, buffer_size,
           "{\n\t\"pressure\": %.1f,\n\t\"temperature1\": "
           "%d,\n\t\"temperature2\": "
           "%.1f,\n\t"
           "\"rainDetected\": %s,\n\t\"humidity\": "
           "%d,\n\t\"lightIntensity\": %.1f,\n\t"
           "\"gasConcentration\": %.1f\n}",
           pressure, measures.temperature, temperature2,
           rainDetected ? "true" : "false", measures.humidity, lightIntensity,
           gasConcentration);
}

void http_post_task(void *pvParameters) {

  char post_data[256];

  while (1) {

    get_sensor_data(post_data, sizeof(post_data));

    esp_http_client_config_t config = {
        .url = POST_URL,
        .event_handler = _http_event_handler,
    };
    esp_http_client_handle_t client = esp_http_client_init(&config);

    esp_http_client_set_url(client, POST_URL);
    esp_http_client_set_method(client, HTTP_METHOD_POST);

    esp_http_client_set_header(client, "Content-Type", "application/json");
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