#define TAG "MEASURES_TASK"

#include "measures_task.h"
#include "adc_config.h"
#include "bh1750.h"
#include "bmp180.h"
#include "dht11.h"
#include "esp_log.h"
#include "freertos/FreeRTOS.h"
#include "freertos/semphr.h"
#include "freertos/task.h"
#include "hardware_config.h"

static SemaphoreHandle_t xMutex;
measures_data measures;

extern i2c_dev_t bh1750;

measures_data get_measures(void) {
  measures_data ret;
  xSemaphoreTake(xMutex, portMAX_DELAY);
  ret = measures;
  xSemaphoreGive(xMutex);
  return ret;
}

void measures_task(void *pvParameters) {
  struct dht11_reading reading;
  uint16_t level = 0;
  uint32_t pressure = 0;
  float temperature2 = 0.0;

  while (1) {
    reading = DHT11_read();
    if (reading.status == DHT11_OK) {
      xSemaphoreTake(xMutex, portMAX_DELAY);
      measures.temperature1 = reading.temperature;
      measures.humidity = reading.humidity;
      xSemaphoreGive(xMutex);
      ESP_LOGI(TAG, "Temperature: %d, Humidity: %d", measures.temperature1,
               measures.humidity);
    } else {
      ESP_LOGE(TAG, "Failed to read DHT11 sensor.");
    }

    esp_err_t res = bh1750_read(&bh1750, &level);

    if (res == ESP_OK) {
      ESP_LOGI(TAG, "Light level: %d", level);
    } else {
      ESP_LOGE(TAG, "Failed to read BH1750 sensor.");
    }

    xSemaphoreTake(xMutex, portMAX_DELAY);
    measures.light_level = level;
    xSemaphoreGive(xMutex);

    res = bmp180_read_pressure(&pressure);
    if (res != ESP_OK) {
      ESP_LOGE(TAG, "Reading of pressure from BMP180 failed, err = %d", res);
    }

    ESP_LOGI(TAG, "Pressure: %ld", pressure);

    xSemaphoreTake(xMutex, portMAX_DELAY);
    measures.pressure = pressure;
    xSemaphoreGive(xMutex);

    res = bmp180_read_temperature(&temperature2);
    if (res != ESP_OK) {
      ESP_LOGE(TAG, "Reading of temperature from BMP180 failed, err = %d", res);
    }

    ESP_LOGI(TAG, "Temperature: %.2f", temperature2);

    xSemaphoreTake(xMutex, portMAX_DELAY);
    measures.temperature2 = temperature2;
    xSemaphoreGive(xMutex);

    // bool is_rain = gpio_get_level(GPIO_NUM_34);

    // ESP_LOGI(TAG, "Rain detected: %d", is_rain);

    // xSemaphoreTake(xMutex, portMAX_DELAY);
    // measures.rain_detected = is_rain;
    // xSemaphoreGive(xMutex);

    float mq135_reading = 0.0;

    res = adc_read_mq135(&mq135_reading);

    if (res != ESP_OK) {
      ESP_LOGE(TAG, "Reading of MQ135 sensor failed, err = %d", res);
    }

    ESP_LOGI(TAG, "MQ135 reading: %.2f", mq135_reading);

    xSemaphoreTake(xMutex, portMAX_DELAY);
    measures.gas_concentration = mq135_reading / 2.45 * 100;
    xSemaphoreGive(xMutex);

    float mh_rd_reading = 0.0;

    res = adc_read_mh_rd(&mh_rd_reading);

    if (res != ESP_OK) {
      ESP_LOGE(TAG, "Reading of MH RD sensor failed, err = %d", res);
    }

    ESP_LOGI(TAG, "MH RD reading: %.2f", mh_rd_reading);

    xSemaphoreTake(xMutex, portMAX_DELAY);
    measures.rain_detected = mh_rd_reading < 1.0;
    xSemaphoreGive(xMutex);

    vTaskDelay(2000 / portTICK_PERIOD_MS);
  }
}

void measures_task_create(void) {
  xMutex = xSemaphoreCreateMutex();
  if (xMutex == NULL) {
    ESP_LOGE(TAG, "Failed to create mutex.");
  }
  measures = (measures_data){0, 0, 0, 0, 0, 0.0, 0.0};
  xTaskCreate(&measures_task, "measures_task", 4096, NULL, 5, NULL);
}
