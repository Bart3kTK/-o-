#ifndef MEASURES_TASK_H
#define MEASURES_TASK_H

#include "freertos/FreeRTOS.h"
#include "freertos/semphr.h"
#include "freertos/task.h"

typedef struct measures_data {
  int temperature1;
  float temperature2;
  int humidity;
  uint16_t light_level;
  uint32_t pressure;
  bool rain_detected;
  float gas_concentration;
} measures_data;

measures_data get_measures(void);

void set_measures(measures_data *data);

void measures_task(void *pvParameters);

void measures_task_create(void);

#endif // MEASURES_TASK_H