#include "https_task.h"
#include "network_config.h"

#include "esp_event.h"
#include "esp_log.h"

#define TAG "APP_INIT"

void app_init_task(void *pvParameter) {
  ESP_LOGI(TAG, "app_init_task started.");

  if (wifi_init() != ESP_OK) {
    ESP_LOGE(TAG, "Failed to initialize wifi.");
    vTaskDelete(NULL);
  }

  http_post_task_create();

  ESP_LOGI(TAG, "https_task created.");

  ESP_LOGI(TAG, "app_init_task finished.");

  vTaskDelete(NULL);
}

void app_task_create(void) {
  xTaskCreate(&app_init_task, "app_init_task", 4096, NULL, 5, NULL);
}