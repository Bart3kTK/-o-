#define TAG "MEASURES_TASK"

#include "measures_task.h"
#include "dht11.h"
#include "esp_log.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"

static SemaphoreHandle_t xMutex;
measures_data measures;

measures_data get_measures(void) {
  measures_data ret;
  xSemaphoreTake(xMutex, portMAX_DELAY);
  ret = measures;
  xSemaphoreGive(xMutex);
  return ret;
}

void measures_task(void *pvParameters) {
  struct dht11_reading reading;

  while (1) {
    reading = DHT11_read();
    if (reading.status == DHT11_OK) {
      xSemaphoreTake(xMutex, portMAX_DELAY);
      measures.temperature = reading.temperature;
      measures.humidity = reading.humidity;
      xSemaphoreGive(xMutex);
      ESP_LOGI(TAG, "Temperature: %d, Humidity: %d", measures.temperature,
               measures.humidity);
    } else {
      ESP_LOGE(TAG, "Failed to read DHT11 sensor.");
    }

    vTaskDelay(2000 / portTICK_PERIOD_MS);
  }
}

void measures_task_create(void) {
  xMutex = xSemaphoreCreateMutex();
  measures = (measures_data){0, 0};
  xTaskCreate(&measures_task, "measures_task", 4096, NULL, 5, NULL);
}
