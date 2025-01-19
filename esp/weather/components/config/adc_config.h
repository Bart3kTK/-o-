#ifndef ADC_CONFIG_H
#define ADC_CONFIG_H

#include "esp_err.h"

esp_err_t adc_init(void);

esp_err_t adc_read(uint8_t channel, float *adc_reading);

esp_err_t adc_read_mh_rd(float *adc_reading);

esp_err_t adc_read_mq135(float *adc_reading);

#endif // ADC_CONFIG_H