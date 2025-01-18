#define TAG "MEASURES_TASK"

#include "measures_task.h"
#include "esp_log.h"
#include "freertos/FreeRTOS.h"
#include "freertos/task.h"
#include "dht11.h"

void measures_task(void *pvParameters) {
    struct dht11_reading reading;

    while(1) {
        reading = DHT11_read();
        if(reading.status == DHT11_OK) {
            ESP_LOGI(TAG, "Temperature: %d, Humidity: %d", reading.temperature, reading.humidity);
        } else {
            ESP_LOGE(TAG, "Failed to read data from DHT11 sensor.");
        }

        
        vTaskDelay(2000 / portTICK_PERIOD_MS);
    }


}
