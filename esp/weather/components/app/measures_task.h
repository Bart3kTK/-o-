#ifndef MEASURES_TASK_H
#define MEASURES_TASK_H

#include "freertos/FreeRTOS.h"
#include "freertos/semphr.h"
#include "freertos/task.h"

typedef struct measures_data {
  int temperature;
  int humidity;
} measures_data;

measures_data get_measures(void);

void measures_task(void *pvParameters);

void measures_task_create(void);

#endif // MEASURES_TASK_H