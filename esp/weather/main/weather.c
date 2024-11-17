#include <stdio.h>

#include "esp_log.h"

#include "app_init_task.h"

#define TAG "WEATHER_APP"

void app_main(void) {

  ESP_LOGI(TAG, "Weather app started.");

  app_task_create();
}
